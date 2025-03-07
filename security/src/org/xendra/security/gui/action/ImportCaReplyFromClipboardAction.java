package org.xendra.security.gui.action;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.resourceloader.ImageLoader;
import org.kse.crypto.Password;
import org.kse.crypto.keystore.KeyStoreType;
import org.kse.crypto.x509.X509CertUtil;
import org.kse.gui.dialogs.DViewCertificate;
import org.kse.gui.error.DError;
import org.kse.gui.error.DProblem;
import org.kse.gui.error.Problem;
import org.kse.utilities.history.HistoryAction;
import org.kse.utilities.history.KeyStoreHistory;
import org.kse.utilities.history.KeyStoreState;
import org.xendra.Constants;
import org.xendra.security.util.ResourceLoader;
import org.xendra.security.gui.frame.SecurityEditorFrameController;

public class ImportCaReplyFromClipboardAction extends AuthorityCertificatesAction implements HistoryAction {
	
	public ImportCaReplyFromClipboardAction(IFrameMediator frameMediator) {
		super(frameMediator);
		putValue(NAME, ResourceLoader.getString("menu", "mainframe", "ImportCaReplyFromClipboard"));
		putValue(SMALL_ICON, ImageLoader.getResource("org.xendra.security", "importcareplyclipboard.png"));
		((SecurityEditorFrameController) frameMediator).register(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			KeyStoreHistory history = ((SecurityEditorFrameController) getFrameMediator()).getActiveKeyStoreHistory();
			KeyStoreState currentState = history.getCurrentState();

			String alias = ((SecurityEditorFrameController) getFrameMediator()).getSelectedEntryAlias();

			Password password = getEntryPassword(alias, currentState);

			if (password == null) {
				return;
			}

			KeyStoreState newState = currentState.createBasisForNextState(this);

			KeyStore keyStore = newState.getKeyStore();
			KeyStoreType keyStoreType = KeyStoreType.resolveJce(keyStore.getType());

			Key privateKey = keyStore.getKey(alias, password.toCharArray());

			X509Certificate[] certs = openCaReply();

			if (certs == null || certs.length == 0) {
				return;
			}

			certs = X509CertUtil.orderX509CertChain(certs);

			X509Certificate[] exitingEntryCerts = X509CertUtil.orderX509CertChain(X509CertUtil
					.convertCertificates(keyStore.getCertificateChain(alias)));

			if (!exitingEntryCerts[0].getPublicKey().equals(certs[0].getPublicKey())) {
				JOptionPane.showMessageDialog(getFrameMediator().getContainer().getFrame(), res.getString("ImportCaReplyFromClipboardAction.NoMatchPubKeyCaReply.message"),
						res.getString("ImportCaReplyFromClipboardAction.ImportCaReply.Title"), JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Holds the new certificate chain for the entry should the import succeed
			X509Certificate[] newCertChain = null;
			if (!applicationSettings.getEnableImportCaReplyTrustCheck()) {
				newCertChain = certs;
			} else {
				KeyStore caCertificates = getCaCertificates();
				KeyStore windowsTrustedRootCertificates = getWindowsTrustedRootCertificates();

				// PKCS #7 reply - try and match the self-signed root with any
				// of the certificates in the CA Certificates or current KeyStore
				if (certs.length > 1) {
					X509Certificate rootCert = certs[certs.length - 1];
					String matchAlias = null;

					if (caCertificates != null) { // Match against CA Certificates KeyStore
						matchAlias = X509CertUtil.matchCertificate(caCertificates, rootCert);
					}

					// Match against Windows Trusted Root Certificates KeyStore
					if (windowsTrustedRootCertificates != null && matchAlias == null) {
						matchAlias = X509CertUtil.matchCertificate(windowsTrustedRootCertificates, rootCert);
					}

					if (matchAlias == null) { // Match against current KeyStore
						matchAlias = X509CertUtil.matchCertificate(keyStore, rootCert);
					}

					if (matchAlias == null) {
						// No match for the root certificate - display the certificate to the user for confirmation
						JOptionPane.showMessageDialog(getFrameMediator().getContainer().getFrame(),
								res.getString("ImportCaReplyFromClipboardAction.NoMatchRootCertCaReplyConfirm.message"),
								res.getString("ImportCaReplyFromClipboardAction.ImportCaReply.Title"),
								JOptionPane.INFORMATION_MESSAGE);

						DViewCertificate dViewCertificate = new DViewCertificate(getFrameMediator().getContainer().getFrame(), MessageFormat.format(
								res.getString("ImportCaReplyFromClipboardAction.CertDetailsFile.Title"), "Clipboard"),
								new X509Certificate[] { rootCert }, null, DViewCertificate.NONE);
						dViewCertificate.setLocationRelativeTo(getFrameMediator().getContainer().getFrame());
						dViewCertificate.setVisible(true);
						int selected = JOptionPane.showConfirmDialog(getFrameMediator().getContainer().getFrame(),
								res.getString("ImportCaReplyFromClipboardAction.AcceptCaReply.message"),
								res.getString("ImportCaReplyFromClipboardAction.ImportCaReply.Title"), JOptionPane.YES_NO_OPTION);
						if (selected != JOptionPane.YES_OPTION) {
							return;
						}

						newCertChain = certs;
					} else {
						newCertChain = certs;
					}
				}
				// Single X.509 certificate reply - try and establish a chain of
				// trust from the certificate and ending with a root CA self-signed certificate
				else {
					// Establish trust against current KeyStore
					ArrayList<KeyStore> compKeyStores = new ArrayList<KeyStore>();
					compKeyStores.add(keyStore);

					if (caCertificates != null) {
						// Establish trust against CA Certificates KeyStore
						compKeyStores.add(caCertificates);
					}

					if (windowsTrustedRootCertificates != null) {
						// Establish trust against Windows Trusted Root Certificates KeyStore
						compKeyStores.add(windowsTrustedRootCertificates);
					}

					X509Certificate[] trustChain = X509CertUtil.establishTrust(certs[0],
							compKeyStores.toArray(new KeyStore[compKeyStores.size()]));

					if (trustChain != null) {
						newCertChain = trustChain;
					} else {
						// Cannot establish trust for the certificate - fail
						JOptionPane.showMessageDialog(getFrameMediator().getContainer().getFrame(),
								res.getString("ImportCaReplyFromClipboardAction.NoTrustCaReply.message"),
								res.getString("ImportCaReplyFromClipboardAction.ImportCaReply.Title"), JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			}

			if (keyStoreType.isFileBased()) {
				// TODO: why or when is delete actually necessary???
				keyStore.deleteEntry(alias);
				keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), newCertChain);
			} else {
				keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), newCertChain);
			}

			currentState.append(newState);

			((SecurityEditorFrameController) getFrameMediator()).updateControls(true);

			JOptionPane.showMessageDialog(getFrameMediator().getContainer().getFrame(), res.getString("ImportCaReplyFromClipboardAction.ImportCaReplySuccessful.message"),
					res.getString("ImportCaReplyFromClipboardAction.ImportCaReply.Title"), JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			DError.displayError(getFrameMediator().getContainer().getFrame(), ex);
		}
	}

	private X509Certificate[] openCaReply() {

		X509Certificate[] certs = null;

		try {

			// get clip board contents, but only string types, not files
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable t = clipboard.getContents(null);
			if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String data;
				data = (String) t.getTransferData(DataFlavor.stringFlavor);
				ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());

				// try to extract certs from clip board data
				certs = X509CertUtil.loadCertificates(bais);
				if (certs.length == 0) {
					JOptionPane.showMessageDialog(getFrameMediator().getContainer().getFrame(), MessageFormat.format(
							res.getString("ImportCaReplyFromClipboardAction.NoCertsFound.message"), "Clipboard"), res
							.getString("ImportCaReplyFromClipboardAction.OpenCaReply.Title"),
							JOptionPane.WARNING_MESSAGE);
				}
			}

			return certs;
		} catch (Exception ex) {
			String problemStr = MessageFormat.format(
					res.getString("ImportCaReplyFromClipboardAction.NoOpenCaReply.Problem"), "Clipboard");

			String[] causes = new String[] { res.getString("ImportCaReplyFromClipboardAction.NotCaReply.Cause"),
					res.getString("ImportCaReplyFromClipboardAction.CorruptedCaReply.Cause") };

			Problem problem = new Problem(problemStr, causes, ex);

			DProblem dProblem = new DProblem(getFrameMediator().getContainer().getFrame(),
					res.getString("ImportCaReplyFromClipboardAction.ProblemOpeningCaReply.Title"),
					problem);
			dProblem.setLocationRelativeTo(getFrameMediator().getContainer().getFrame());
			dProblem.setVisible(true);

			return null;
		}
	}

    public String getHistoryDescription() {
            return (String) getValue(NAME);
    }
}
