package org.xendra.modeleditor.wizard.newtab;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.columba.core.gui.base.MultiLineLabel;
import org.columba.core.gui.base.WizardTextField;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.persistence.X_AD_Column;
import org.compiere.model.persistence.X_AD_Tab;
import org.compiere.swing.CTextField;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.xendra.modeleditor.util.ResourceLoader;
import org.xendra.modeleditor.wizard.common.components;

import net.javaprog.ui.wizard.AbstractStep;
import net.javaprog.ui.wizard.DataModel;
import net.miginfocom.swing.MigLayout;

public class NewTabConfirm extends AbstractStep {
	protected DataModel data;
	protected JLabel name = new JLabel();
	protected JLabel tablename = new JLabel();
	protected JLabel description = new JLabel();
	protected JLabel help = new JLabel();
	protected JLabel entitytypename = new JLabel();
	public NewTabConfirm(DataModel data) {		
		super(ResourceLoader.getString("dialog", "newtabconfirmwizard", "identity"),
				ResourceLoader.getString("dialog", "newtabconfirmwizard",   "identity_description"));
		this.data = data;		
		setCanGoNext(true);
	}	
	@Override
	public void prepareRendering() {
		CTextField textname = (CTextField) components.getInstance().getControls().get(X_AD_Tab.COLUMNNAME_Name);
		name.setText(textname.getText());
		VLookup texttable = (VLookup) components.getInstance().getControls().get(X_AD_Tab.COLUMNNAME_AD_Table_ID);
		tablename.setText(texttable.getDisplay());
		CTextField textdescription = (CTextField) components.getInstance().getControls().get(X_AD_Tab.COLUMNNAME_Description);
		description.setText(textdescription.getText());
		CTextField texthelp = (CTextField) components.getInstance().getControls().get(X_AD_Tab.COLUMNNAME_Help);
		help.setText(texthelp.getText());
		VLookup lookentityname = (VLookup) components.getInstance().getControls().get(X_AD_Tab.COLUMNNAME_EntityType);
		entitytypename.setText(lookentityname.getDisplay());
	}
	
	@Override
	protected JComponent createComponent() {
		JComponent component = new JPanel();
		component.setLayout(new BoxLayout(component, BoxLayout.Y_AXIS));
		component.add(new MultiLineLabel(ResourceLoader.getString("dialog", "newtablewizard", "identity_text")));
		component.add(Box.createVerticalStrut(40));
		WizardTextField textfield = new WizardTextField();		
		textfield.addLabel(new JLabel(Msg.translate(Env.getCtx(), X_AD_Column.COLUMNNAME_Description)));
		JPanel pan = new JPanel();
		pan.setLayout(new MigLayout());
		pan.add(new JLabel(Msg.translate(Env.getCtx(), X_AD_Tab.COLUMNNAME_Name)));
		pan.add(name, "wrap");
		pan.add(new JLabel(Msg.translate(Env.getCtx(), X_AD_Tab.COLUMNNAME_AD_Table_ID)));
		pan.add(tablename, "wrap");
		pan.add(new JLabel(Msg.translate(Env.getCtx(), X_AD_Tab.COLUMNNAME_Description)));
		pan.add(description, "wrap");
		pan.add(new JLabel(Msg.translate(Env.getCtx(), X_AD_Tab.COLUMNNAME_Help)));
		pan.add(help, "wrap");
		pan.add(new JLabel(Msg.translate(Env.getCtx(), X_AD_Tab.COLUMNNAME_EntityType)));
		pan.add(entitytypename, "wrap");
		textfield.addTextField(pan);			
		textfield.addExample(new JLabel("explication"));	
		component.add(textfield);
		return component;		
	}		
}
