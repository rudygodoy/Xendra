package org.xendra.cost;

import java.awt.Dimension;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import org.compiere.model.Query;
import org.compiere.model.persistence.X_AD_Column;
import org.compiere.model.persistence.X_AD_Table;
import org.compiere.model.persistence.X_C_DocType;
import org.compiere.model.persistence.X_M_MaterialPolicy;
import org.compiere.model.persistence.X_M_Transaction;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.xendra.core.resourceloader.ResourceLoader;

import net.javaprog.ui.wizard.AbstractStep;
import net.javaprog.ui.wizard.DataModel;
import net.javaprog.ui.wizard.DefaultDataLookup;

import net.miginfocom.swing.MigLayout;

public class PolicySequence extends AbstractStep implements ActionListener {
	private DataModel data;
	private CLabel noLabel = new CLabel();
	private CLabel yesLabel = new CLabel();
	private CButton bAdd = new CButton();
	private CButton bRemove = new CButton();
	private DefaultListModel noModel = new DefaultListModel();
	private DefaultListModel yesModel = new DefaultListModel();
	private JList noList = new JList(noModel);
	private JList yesList = new JList(yesModel);
	private JScrollPane noPane = new JScrollPane(noList);
	private JScrollPane yesPane = new JScrollPane(yesList);
	private boolean isRender = false;
	private static CLogger log = CLogger.getCLogger(PolicySequence.class);
	public PolicySequence(DataModel data) {
		super(ResourceLoader.getString("materialpolicywizard", "sequence"),
				ResourceLoader.getString("materialpolicywizard", "sequence_description"));
		this.data = data;		
		setCanGoNext(true);
	}

	@Override
	public void prepareRendering() {
		if (!isRender) {
			X_AD_Table table = new Query(Env.getCtx(), X_AD_Table.Table_Name, "TableName = ?", null).setParameters(X_M_Transaction.Table_Name).first();
			List<X_AD_Column> columns = new Query(Env.getCtx(), X_AD_Column.Table_Name, "AD_Table_ID = ?", null)
			.setParameters(table.getAD_Table_ID()).list();
			for (X_AD_Column column:columns) {
				ValueNamePair pp = new ValueNamePair(column.getColumnName(), column.getName());
				noModel.addElement(pp);
			}
			isRender  = true;
		}
	}

	@Override
	protected JComponent createComponent() {
		JComponent comp = new JPanel();                
		comp.setLayout(new MigLayout("center", "[][][]", "[][][][grow]"));
		//
		noLabel.setText("No");
		yesLabel.setText("Yes");
		//
		bAdd.setIcon(Env.getImageIcon("Detail24.gif"));
		bAdd.setMargin(new Insets(2, 2, 2, 2));
		bAdd.addActionListener(this);
		bRemove.setIcon(Env.getImageIcon("Parent24.gif"));
		bRemove.setMargin(new Insets(2, 2, 2, 2));
		bRemove.addActionListener(this);
		//
		yesPane.setPreferredSize(new Dimension(200, 500));
		yesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		noPane.setPreferredSize(new Dimension(200, 500));
		noList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//
		comp.add(noLabel, "cell 0 0,span 1 1,push 0 0, align center, gap 0 0 0 0");		
		comp.add(yesLabel,"cell 2 0,span 1 1,push 0 0, align center, gap 0 0 0 0");
		comp.add(noPane,  "cell 0 1,span 1 5,push 0 0, align center, gap 4 4 4 4");
		comp.add(yesPane, "cell 2 1,span 1 5,push 0 0, align center, gap 4 4 4 4");
		comp.add(bRemove, "cell 1 2,span 1 1,push 0 0, align center, gap 4 4 4 4");
		comp.add(bAdd,    "cell 1 1,span 1 1,push 0 0, align center, gap 4 4 4 4");			
		noLabel.setText(Msg.getMsg(Env.getCtx(), "Available"));
		yesLabel.setText(Msg.getMsg(Env.getCtx(), "Select"));		
		Method method = null;
		try {			
			method = yesList.getClass().getMethod("getModel", null);
		} catch (NoSuchMethodException nsme) {
		}		
		String PolicyID = (String) data.getData(X_M_MaterialPolicy.COLUMNNAME_M_MaterialPolicy_ID);
		if (Integer.valueOf(PolicyID) > 0) {
			X_M_MaterialPolicy p = new Query(Env.getCtx(), X_M_MaterialPolicy.Table_Name, "M_MaterialPolicy_ID = ?", null)
			.setParameters(Integer.valueOf(PolicyID)).first();
			X_AD_Table table = new Query(Env.getCtx(), X_AD_Table.Table_Name, "TableName = ?", null)
			.setParameters(X_M_Transaction.Table_Name).first();
			String sequence = (String) p.getProperties().get("seqno");
			StringTokenizer st = new StringTokenizer(sequence, ",");
			while (st.hasMoreElements()) {
				String tk = (String) st.nextElement();
				X_AD_Column column = new Query(Env.getCtx(), X_AD_Column.Table_Name, "LOWER(ColumnName) = ? AND AD_Table_ID = ?", null)
				.setParameters(tk, table.getAD_Table_ID()).first();
				if (column != null) {
					ValueNamePair pp = new ValueNamePair(column.getColumnName(), column.getName());
					noModel.removeElement(pp);
					yesModel.addElement(pp);
				}
			}
		}						
		data.registerDataLookup(X_M_Transaction.COLUMNNAME_SeqNo,new DefaultDataLookup(yesList, method, null));
		JComponent policy = new JPanel(new MigLayout());
		policy.add(new JLabel(ResourceLoader.getString("materialpolicywizard", "sequence_description")),"wrap");
		policy.add(comp);
		return policy;				

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == bAdd) {
			List<ValueNamePair> xxlist = noList.getSelectedValuesList();
			for (ValueNamePair kp:xxlist)
			{
				noModel.removeElement(kp);
				log.config("Add=" + kp);
				yesModel.addElement(kp);
				yesList.setSelectedValue(kp, true);
			}						
		} else if (e.getSource() == bRemove) {
			List<ValueNamePair> list = yesList.getSelectedValuesList();
			for (ValueNamePair kp:list)
			{
				yesModel.removeElement(kp);
				log.config("Remove=" + kp);
				noModel.addElement(kp);
			}			
		}
	}
}
