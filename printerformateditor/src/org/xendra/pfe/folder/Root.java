package org.xendra.pfe.folder;

import java.util.List;

import org.columba.api.command.IWorkerStatusController;
import org.columba.api.plugin.IExtension;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.PluginHandlerNotFoundException;
import org.columba.core.gui.dialog.ErrorDialog;
import org.columba.core.plugin.PluginManager;
import org.compiere.model.Query;
import org.compiere.model.persistence.X_AD_Plugin;
import org.compiere.util.Env;
import org.jdom.Element;
import org.xendra.model.AbstractFolder;
import org.xendra.pfe.plugin.IExtensionHandlerKeys;

public class Root extends AbstractFolder {

	public Root(Element node) {
		super(new Element("root"));
		loadChildren();
	}
	public Class getDefaultChild() {
		return null;
	}
	public void createChildren(IWorkerStatusController c) {
	}
	public String getName() {
		return "Root";
	}
	public void loadChildren() {
		removeAllChildren();
		List<X_AD_Plugin> plugins = new Query(Env.getCtx(), X_AD_Plugin.Table_Name, "", null)
		.list();
		String type = "plugin";
		for (X_AD_Plugin plugin:plugins) {
			Element item = new Element("plugin");
			item.setAttribute("uid", String.valueOf(plugin.getAD_Plugin_ID()));
			item.setAttribute("name", plugin.getName());
			Object[] args = { item };
			IExtensionHandler handler = null;
			try {
				handler = PluginManager.getInstance().getExtensionHandler(IExtensionHandlerKeys.ORG_XENDRA_PFE_FOLDER);
			} catch (PluginHandlerNotFoundException ex) {
				ErrorDialog.createDialog(ex.getMessage(),  ex);
			}			
			AbstractFolder folder = null;			
			try {
				IExtension extension = handler.getExtension(type);
				folder = (AbstractFolder) extension.instanciateExtension(args);
				this.add(folder);
			} catch (Exception e) {
				e.printStackTrace();
			}									
		}		
	}
}
