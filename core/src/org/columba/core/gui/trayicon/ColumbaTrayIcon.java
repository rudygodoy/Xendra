//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.

package org.columba.core.gui.trayicon;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.JPopupMenu;

import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.base.OSInfo;
import org.columba.core.gui.base.SelfClosingPopupMenu;
import org.columba.core.gui.menu.MenuXMLDecoder;
import org.columba.core.io.DiskIO;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.shutdown.ShutdownManager;
import org.compiere.util.CLogMgt;

/**
 * Uses the JDIC api to add a tray icon to the system default tray.
 * 
 * @author Timo Stich <tstich@users.sourceforge.net>
 */
public class ColumbaTrayIcon {

	private IFrameMediator frameMediator;

	/**
	 * Default icon for the TrayIcon.
	 */
	public static final Icon DEFAULT_ICON = ImageLoader.getMiscIcon("trayicon.png");

	private static ColumbaTrayIcon instance = new ColumbaTrayIcon();

	private JPopupMenu menu;

	private TrayIconInterface activeIcon;

	protected ColumbaTrayIcon() {
		activeIcon = new DefaultTrayIcon();

	}

	/**
	 * Gets the instance of the ColumbaTrayIcon.
	 * 
	 * @return singleton instance
	 */
	public static ColumbaTrayIcon getInstance() {
		return instance;
	}

	/**
	 * Add the tray icon to the default system tray.
	 * 
	 */
	public void addToSystemTray(IFrameMediator frameMediator) {

		initPopupMenu();

		this.frameMediator = frameMediator;
		activeIcon.addToTray(DEFAULT_ICON, "Columba");
		activeIcon.setPopupMenu(menu);

		ShutdownManager.getInstance().register(new Runnable() {
			public void run() {
				ColumbaTrayIcon.getInstance().removeFromSystemTray();
			}

		});
	}

	/**
	 * Sets the tooltip of the tray icon.
	 * 
	 * @param tooltip
	 */
	public void setTooltip(String tooltip) {
		activeIcon.setTooltip(tooltip);
	}

	/**
	 * Sets the icon of the tray icon.
	 * 
	 * @param icon
	 */
	public void setIcon(Icon icon) {
		activeIcon.setIcon(icon);
	}

	/**
	 * Removes the tray icon from the system tray.s
	 */
	public void removeFromSystemTray() {
		activeIcon.removeFromTray();
	}

	private void initPopupMenu() {
		if (menu == null) {
			menu = new JPopupMenu();

			try {
				InputStream is = DiskIO
						.getResourceStream("org/columba/core/action/trayiconmenu.xml");

				menu = new MenuXMLDecoder(frameMediator).createPopupMenu(is);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// menu.add(new CMenuItem(new OpenNewMailWindowAction(null)));
			// menu.add(new CMenuItem(new
			// OpenNewAddressbookWindowAction(null)));
			// menu.addSeparator();
			// menu.add(new CMenuItem(new AboutDialogAction(null)));
			// menu.add(new CMenuItem(new ShowHelpAction(null)));
			// menu.addSeparator();
			// menu.add(new CMenuItem(new ExitAction(null)));

			new SelfClosingPopupMenu(menu);
		}
	}

	/**
	 * @return Returns the activeIcon.
	 */
	public TrayIconInterface getActiveIcon() {
		return activeIcon;
	}

	/**
	 * @param activeIcon
	 *            The activeIcon to set.
	 */
	public void initActiveIcon() {
		try {
			if (OSInfo.isLinux()) {
				//activeIcon = new JDICTrayIcon();
			} else if (OSInfo.isWin32Platform()) {
				activeIcon = new JDICTrayIcon();
			} else if (OSInfo.isMac()) {
				// tray icon not supported on Mac
			}
		} catch (Exception e) {
			if (CLogMgt.DEBUG)
				e.printStackTrace();

			activeIcon = new DefaultTrayIcon();
		} catch (Error e) {
			if (CLogMgt.DEBUG)
				e.printStackTrace();

			activeIcon = new DefaultTrayIcon();
		}
	}

}