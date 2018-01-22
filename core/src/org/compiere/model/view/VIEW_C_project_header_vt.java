package org.compiere.model.view;


import org.xendra.annotations.*;
import org.apache.commons.lang.text.StrBuilder;
import org.compiere.model.View;

public class VIEW_C_project_header_vt 
{
 	@XendraView(Identifier="eb63ba55-a29d-eccf-ac58-f812e57c1fc3",
Synchronized="2013-07-09 19:02:34.0",
Name="c_project_header_vt",
Owner="xendra",
Extension="")
	public static final String Identifier = "eb63ba55-a29d-eccf-ac58-f812e57c1fc3";

	public static final String getComments() 
{
 	StrBuilder sb = new StrBuilder();
 	sb.appendln("@Synchronized=2013-07-09 19:02:34.0");
	sb.appendln("@Identifier=eb63ba55-a29d-eccf-ac58-f812e57c1fc3");
	return sb.toString();
}
	public static final String getDefinition() 
{
 	StrBuilder sb = new StrBuilder();
 	sb.appendln("SELECT p.ad_client_id, p.ad_org_id, p.isactive, p.created, p.createdby, p.updated, p.updatedby, pt.ad_language, p.c_project_id, p.value, p.name AS projectname, p.description, p.note, p.issummary, p.projectcategory, oi.c_location_id AS org_location_id, oi.taxid, p.c_projecttype_id, pjt.name AS projecttypename, p.c_phase_id, pjp.name AS projectphasename, p.salesrep_id, COALESCE(ubp.name, u.name) AS salesrep_name, p.c_bpartner_id, bp.value AS bpvalue, bp.taxid AS bptaxid, bp.naics, bp.duns, bpg.greeting AS bpgreeting, bp.name, bp.name2, bpcg.greeting AS bpcontactgreeting, bpc.title, bpc.phone, NULLIF((bpc.name)::text, (bp.name)::text) AS contactname, bpl.c_location_id, bp.referenceno, pt.name AS paymentterm, pt.documentnote AS paymenttermnote, p.poreference, p.c_currency_id, p.m_pricelist_version_id, p.c_campaign_id, p.plannedamt, p.plannedqty, p.plannedmarginamt, p.invoicedamt, p.invoicedqty, p.projectbalanceamt, p.iscommitment, p.committedamt, p.committedqty, p.datecontract, p.datefinish, p.iscommitceiling, p.m_warehouse_id FROM (((((((((((c_project p LEFT JOIN c_bpartner bp ON ((p.c_bpartner_id = bp.c_bpartner_id))) JOIN ad_orginfo oi ON ((p.ad_org_id = oi.ad_org_id))) LEFT JOIN c_paymentterm_trl pt ON ((p.c_paymentterm_id = pt.c_paymentterm_id))) LEFT JOIN c_projecttype pjt ON ((p.c_projecttype_id = pjt.c_projecttype_id))) LEFT JOIN c_phase pjp ON ((p.c_phase_id = pjp.c_phase_id))) LEFT JOIN ad_user u ON ((p.salesrep_id = u.ad_user_id))) LEFT JOIN c_bpartner ubp ON ((u.c_bpartner_id = ubp.c_bpartner_id))) LEFT JOIN c_greeting bpg ON ((bp.c_greeting_id = bpg.c_greeting_id))) LEFT JOIN ad_user bpc ON ((p.ad_user_id = bpc.ad_user_id))) LEFT JOIN c_greeting bpcg ON ((bpc.c_greeting_id = bpcg.c_greeting_id))) LEFT JOIN c_bpartner_location bpl ON ((p.c_bpartner_location_id = bpl.c_bpartner_location_id)));");
	return sb.toString();
}
}
