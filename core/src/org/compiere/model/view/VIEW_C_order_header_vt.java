package org.compiere.model.view;


import org.xendra.annotations.*;
import org.apache.commons.lang.text.StrBuilder;
import org.compiere.model.View;

public class VIEW_C_order_header_vt 
{
 	@XendraView(Identifier="bf00fba2-43fd-a8f8-e675-50948d199ab7",
Synchronized="2013-07-09 19:02:34.0",
Name="c_order_header_vt",
Owner="xendra",
Extension="")
	public static final String Identifier = "bf00fba2-43fd-a8f8-e675-50948d199ab7";

	public static final String getComments() 
{
 	StrBuilder sb = new StrBuilder();
 	sb.appendln("@Synchronized=2013-07-09 19:02:34.0");
	sb.appendln("@Identifier=bf00fba2-43fd-a8f8-e675-50948d199ab7");
	return sb.toString();
}
	public static final String getDefinition() 
{
 	StrBuilder sb = new StrBuilder();
 	sb.appendln("SELECT o.ad_client_id, o.ad_org_id, o.isactive, o.created, o.createdby, o.updated, o.updatedby, dt.ad_language, o.c_order_id, o.issotrx, o.documentno, o.docstatus, o.c_doctype_id, o.c_bpartner_id, bp.value AS bpvalue, bp.taxid AS bptaxid, bp.naics, bp.duns, oi.c_location_id AS org_location_id, oi.taxid, o.m_warehouse_id, wh.c_location_id AS warehouse_location_id, dt.printname AS documenttype, dt.documentnote AS documenttypenote, o.salesrep_id, COALESCE(ubp.name, u.name) AS salesrep_name, o.dateordered, o.datepromised, bpg.greeting AS bpgreeting, bp.name, bp.name2, bpcg.greeting AS bpcontactgreeting, bpc.title, bpc.phone, NULLIF((bpc.name)::text, (bp.name)::text) AS contactname, bpl.c_location_id, ((l.postal)::text || (l.postal_add)::text) AS postal, bp.referenceno, o.bill_bpartner_id, o.bill_location_id, o.bill_user_id, bbp.value AS bill_bpvalue, bbp.taxid AS bill_bptaxid, bbp.name AS bill_name, bbp.name2 AS bill_name2, bbpc.title AS bill_title, bbpc.phone AS bill_phone, NULLIF((bbpc.name)::text, (bbp.name)::text) AS bill_contactname, bbpl.c_location_id AS bill_c_location_id, o.description, o.poreference, o.c_currency_id, pt.name AS paymentterm, pt.documentnote AS paymenttermnote, o.c_charge_id, o.chargeamt, o.totallines, o.grandtotal, o.grandtotal AS amtinwords, o.m_pricelist_id, o.istaxincluded, o.volume, o.weight, o.c_campaign_id, o.c_project_id, o.c_activity_id, o.m_shipper_id, o.deliveryrule, o.deliveryviarule, o.priorityrule, o.invoicerule FROM (((((((((((((((c_order o JOIN c_doctype_trl dt ON ((o.c_doctype_id = dt.c_doctype_id))) JOIN m_warehouse wh ON ((o.m_warehouse_id = wh.m_warehouse_id))) JOIN c_paymentterm_trl pt ON (((o.c_paymentterm_id = pt.c_paymentterm_id) AND ((dt.ad_language)::text = (pt.ad_language)::text)))) JOIN c_bpartner bp ON ((o.c_bpartner_id = bp.c_bpartner_id))) LEFT JOIN c_greeting_trl bpg ON (((bp.c_greeting_id = bpg.c_greeting_id) AND ((dt.ad_language)::text = (bpg.ad_language)::text)))) JOIN c_bpartner_location bpl ON ((o.c_bpartner_location_id = bpl.c_bpartner_location_id))) JOIN c_location l ON ((bpl.c_location_id = l.c_location_id))) LEFT JOIN ad_user bpc ON ((o.ad_user_id = bpc.ad_user_id))) LEFT JOIN c_greeting_trl bpcg ON (((bpc.c_greeting_id = bpcg.c_greeting_id) AND ((dt.ad_language)::text = (bpcg.ad_language)::text)))) JOIN ad_orginfo oi ON ((o.ad_org_id = oi.ad_org_id))) LEFT JOIN ad_user u ON ((o.salesrep_id = u.ad_user_id))) LEFT JOIN c_bpartner ubp ON ((u.c_bpartner_id = ubp.c_bpartner_id))) JOIN c_bpartner bbp ON ((o.bill_bpartner_id = bbp.c_bpartner_id))) JOIN c_bpartner_location bbpl ON ((o.bill_location_id = bbpl.c_bpartner_location_id))) LEFT JOIN ad_user bbpc ON ((o.bill_user_id = bbpc.ad_user_id)));");
	return sb.toString();
}
}
