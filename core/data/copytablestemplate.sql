COPY (SELECT * FROM AD_CLIENT) to 'PGDIR/ad_client.csv' WITH (FORMAT csv, header false, delimiter '|'); 

COPY (SELECT * FROM AD_CLIENTINFO) to 'PGDIR/ad_clientinfo.csv' WITH (FORMAT csv, header false, delimiter '|'); 
COPY (SELECT * FROM AD_ORG) to 'PGDIR/ad_org.csv' WITH (FORMAT csv, header false, delimiter '|'); 
COPY (SELECT * FROM AD_ROLE) to 'PGDIR/ad_role.csv' WITH (FORMAT csv, header false, delimiter '|'); 
COPY (SELECT * FROM AD_ROLE_ORGACCESS) to 'PGDIR/ad_role_orgaccess.csv' WITH (FORMAT csv, header false, delimiter '|'); 
COPY (SELECT * FROM AD_REFERENCE) to 'PGDIR/ad_reference.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_REFERENCE_TRL) to 'PGDIR/ad_reference_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_REF_LIST) to 'PGDIR/ad_ref_list.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_REF_LIST_TRL) to 'PGDIR/ad_ref_list_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_REF_TABLE) to 'PGDIR/ad_ref_table.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_ELEMENT) to 'PGDIR/ad_element.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_ELEMENT_TRL) to 'PGDIR/ad_element_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_IMAGE) to 'PGDIR/ad_image.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WINDOW) to 'PGDIR/ad_window.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WINDOW_TRL) to 'PGDIR/ad_window_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_VAL_RULE) to 'PGDIR/ad_val_rule.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_ReportView) to 'PGDIR/ad_reportview.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PrintFont) to 'PGDIR/ad_printfont.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PrintColor) to 'PGDIR/ad_printcolor.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PrintTableFormat) to 'PGDIR/ad_printtableformat.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PrintPaper) to 'PGDIR/ad_printpaper.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PrintFormat) to 'PGDIR/ad_printformat.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Process) to 'PGDIR/ad_process.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Process_Trl) to 'PGDIR/ad_process_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Process_Para) to 'PGDIR/ad_process_para.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Process_Para_Trl) to 'PGDIR/ad_process_para_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Table) to 'PGDIR/ad_table.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Column) to 'PGDIR/ad_column.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Message) to 'PGDIR/ad_message.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Message_trl) to 'PGDIR/ad_message_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Language) to 'PGDIR/ad_language.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_Currency) to 'PGDIR/c_currency.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Table_Trl) to 'PGDIR/ad_table_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Column_Trl) to 'PGDIR/ad_column_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_City) to 'PGDIR/c_city.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_Region) to 'PGDIR/c_region.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_Country) to 'PGDIR/c_country.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_System) to 'PGDIR/ad_system.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_User) to 'PGDIR/ad_user.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_User_Roles) to 'PGDIR/ad_user_roles.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_SUBJECT) to 'PGDIR/c_subject.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PREFERENCE) to 'PGDIR/ad_preference.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_IMPFORMAT) to 'PGDIR/ad_impformat.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_IMPFORMAT_ROW) to 'PGDIR/ad_impformat_row.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_FORM) to 'PGDIR/ad_form.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_FORM_TRL) to 'PGDIR/ad_form_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TAB) to 'PGDIR/ad_tab.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TAB_TRL) to 'PGDIR/ad_tab_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_FIELD) to 'PGDIR/ad_field.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_FIELD_TRL) to 'PGDIR/ad_field_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_FIELDGROUP) to 'PGDIR/ad_fieldgroup.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_FIELDGROUP_TRL) to 'PGDIR/ad_fieldgroup_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_ENTITYTYPE) to 'PGDIR/ad_entitytype.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WORKBENCH) to 'PGDIR/ad_workbench.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TASK) to 'PGDIR/ad_task.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_MENU) to 'PGDIR/ad_menu.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TREE) to 'PGDIR/ad_tree.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TREEBAR) to 'PGDIR/ad_treebar.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TREENODE) to 'PGDIR/ad_treenode.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TREENODEBP) to 'PGDIR/ad_treenodebp.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TREENODEMM) to 'PGDIR/ad_treenodemm.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_UOM) to 'PGDIR/c_uom.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_TAX) to 'PGDIR/c_tax.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_TAX_TRL) to 'PGDIR/c_tax_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_TAXCATEGORY) to 'PGDIR/c_taxcategory.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_TAXCATEGORY_TRL) to 'PGDIR/c_taxcategory_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_UOM_TRL) to 'PGDIR/c_uom_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_MENU_TRL) to 'PGDIR/ad_menu_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WORKFLOW_ACCESS) to 'PGDIR/ad_workflow_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TASK_ACCESS) to 'PGDIR/ad_task_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_FORM_ACCESS) to 'PGDIR/ad_form_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TOOL_ACCESS) to 'PGDIR/ad_tool_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PRIVATE_ACCESS) to 'PGDIR/ad_private_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_COLUMN_ACCESS) to 'PGDIR/ad_column_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PLUGIN_ACCESS) to 'PGDIR/ad_plugin_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PROCESS_ACCESS) to 'PGDIR/ad_process_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_RECORD_ACCESS) to 'PGDIR/ad_record_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WINDOW_ACCESS) to 'PGDIR/ad_window_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_TABLE_ACCESS) to 'PGDIR/ad_table_access.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM M_PRODUCT_CATEGORY) to 'PGDIR/m_product_category.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM M_ATTRIBUTESET) to 'PGDIR/m_attributeset.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM M_ATTRIBUTESETINSTANCE) to 'PGDIR/m_attributesetinstance.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_PLUGIN) to 'PGDIR/ad_plugin.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_Package) to 'PGDIR/ad_package.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM A_PRINTERDRIVER) to 'PGDIR/a_printerdriver.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WORKFLOW) to 'PGDIR/ad_workflow.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WF_NODE) to 'PGDIR/ad_wf_node.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WF_NODE_TRL) to 'PGDIR/ad_wf_node_trl.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WF_RESPONSIBLE) to 'PGDIR/ad_wf_responsible.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM AD_WF_NODENEXT) to 'PGDIR/ad_wf_nodenext.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_ConversionType) to 'PGDIR/c_conversiontype.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_DOCTYPE) to 'PGDIR/c_doctype.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_BP_DOCTYPE) to 'PGDIR/c_bp_doctype.csv' WITH (FORMAT csv, header false, delimiter '|');
COPY (SELECT * FROM C_DOCUMENTTAX) to 'PGDIR/c_documenttax.csv' WITH (FORMAT csv, header false, delimiter '|');

