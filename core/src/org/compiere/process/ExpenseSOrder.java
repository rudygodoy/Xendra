/******************************************************************************
 * Product: Xendra ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.process;

import java.math.*;
import java.sql.*;

import org.compiere.model.MBPartner;
import org.compiere.model.MConversionRate;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProject;
import org.compiere.model.MTimeExpense;
import org.compiere.model.MTimeExpenseLine;
import org.compiere.model.reference.REF_C_DocTypeSubType;

import java.util.logging.*;

import org.compiere.util.*;

import org.xendra.annotations.XendraProcess;
import org.xendra.annotations.XendraProcessParameter;

/**
 *	Create Sales Orders from Expense Reports
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ExpenseSOrder.java 5583 2015-08-05 14:11:58Z xapiens $
 */
@XendraProcess(value="S_ExpenseSOrder",
name="Create Sales Orders from Expense",
description="Create Sales Orders for Customers from Expense Reports",
help="",
Identifier="0b3d2301-72ac-16c6-73d7-2201af7b1910",
classname="org.compiere.process.ExpenseSOrder",
updated="2015-06-20 10:10:12")	
public class ExpenseSOrder extends SvrProcess
{
	/**	 BPartner				*/
	@XendraProcessParameter(Name="Business Partner ",
			                ColumnName="C_BPartner_ID",
			                Description="Identifies a Business Partner",
			                Help="A Business Partner is anyone with whom you transact.  This can include Vendor, Customer, Employee or Salesperson",
			                AD_Reference_ID=DisplayType.Search,
			                SeqNo=10,
			                ReferenceValueID="",
			                ValRuleID="",
			                FieldLength=0,
			                IsMandatory=false,
			                IsRange=false,
			                DefaultValue="",
			                DefaultValue2="",
			                vFormat="",
			                valueMin="",
			                valueMax="",
			                DisplayLogic="",
			                ReadOnlyLogic="",
			                Identifier="e456fe94-21a6-db7c-519d-2b5b73ca16c4")	
	private int			p_C_BPartner_ID = 0;
	/** Date Drom				*/
	@XendraProcessParameter(Name="Expense Date",
			                ColumnName="DateExpense",
			                Description="Date of expense",
			                Help="Date of expense",
			                AD_Reference_ID=DisplayType.Date,
			                SeqNo=20,
			                ReferenceValueID="",
			                ValRuleID="",
			                FieldLength=0,
			                IsMandatory=false,
			                IsRange=true,
			                DefaultValue="",
			                DefaultValue2="",
			                vFormat="",
			                valueMin="",
			                valueMax="",
			                DisplayLogic="",
			                ReadOnlyLogic="",
			                Identifier="6013d0cf-1ffc-5647-f18f-6802fe91a570")	
	private Timestamp	p_DateFrom = null;
	/** Date To					*/
	private Timestamp	m_DateTo = null;

	/** No SO generated			*/
	private int			m_noOrders = 0;
	/**	Current Order			*/
	private MOrder		m_order = null;
	
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("DateExpense"))
			{
				p_DateFrom = (Timestamp)para[i].getParameter();
				m_DateTo = (Timestamp)para[i].getParameter_To();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message to be translated
	 *  @throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		StringBuffer sql = new StringBuffer("SELECT * FROM S_TimeExpenseLine el "
			+ "WHERE el.AD_Client_ID=?"						//	#1
			+ " AND el.C_BPartner_ID>0 AND el.IsInvoiced='Y'"	//	Business Partner && to be invoiced
			+ " AND el.C_OrderLine_ID IS NULL"					//	not invoiced yet
			+ " AND EXISTS (SELECT * FROM S_TimeExpense e "		//	processed only
				+ "WHERE el.S_TimeExpense_ID=e.S_TimeExpense_ID AND e.Processed='Y')");		
		if (p_C_BPartner_ID != 0)
			sql.append(" AND el.C_BPartner_ID=?");			//	#2
		if (p_DateFrom != null || m_DateTo != null)
		{
			sql.append(" AND EXISTS (SELECT * FROM S_TimeExpense e "
				+ "WHERE el.S_TimeExpense_ID=e.S_TimeExpense_ID");
			if (p_DateFrom != null)
				sql.append(" AND e.DateReport >= ?");		//	#3
			if (m_DateTo != null)
				sql.append(" AND e.DateReport <= ?");		//	#4
			sql.append(")");
		}
		sql.append(" ORDER BY el.C_BPartner_ID, el.C_Project_ID, el.S_TimeExpense_ID, el.Line");

		//
		MBPartner oldBPartner = null;
		int old_Project_ID = -1;
		MTimeExpense te = null;
		//
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			int par = 1;
			pstmt.setInt(par++, getAD_Client_ID());
			if (p_C_BPartner_ID != 0)
				pstmt.setInt(par++, p_C_BPartner_ID);
			if (p_DateFrom != null)
				pstmt.setTimestamp(par++, p_DateFrom);
			if (m_DateTo != null)
				pstmt.setTimestamp(par++, m_DateTo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())				//	********* Expense Line Loop
			{
				MTimeExpenseLine tel = new MTimeExpenseLine(getCtx(), rs, get_TrxName());
				if (!tel.isInvoiced())
					continue;
				
				//	New BPartner - New Order
				if (oldBPartner == null 
					|| oldBPartner.getC_BPartner_ID() != tel.getC_BPartner_ID())
				{
					completeOrder ();
					oldBPartner = new MBPartner (getCtx(), tel.getC_BPartner_ID(), get_TrxName());
				}
				//	New Project - New Order
				if (old_Project_ID != tel.getC_Project_ID())
				{
					completeOrder ();
					old_Project_ID = tel.getC_Project_ID();
				}
				if (te == null || te.getS_TimeExpense_ID() != tel.getS_TimeExpense_ID())
					te = new MTimeExpense (getCtx(), tel.getS_TimeExpense_ID(), get_TrxName());
				//
				processLine (te, tel, oldBPartner);
			}								//	********* Expense Line Loop
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		finally
		{
			try
			{
				if (pstmt != null)
					pstmt.close ();
			}
			catch (Exception e)
			{}
			pstmt = null;
		}
		completeOrder ();

		return "@Created@=" + m_noOrders;
	}	//	doIt

	/**
	 * 	Process Expense Line
	 *	@param te header
	 *	@param tel line
	 *	@param bp bp
	 */
	private void processLine (MTimeExpense te, MTimeExpenseLine tel, MBPartner bp)
	{
		if (m_order == null)
		{
			log.info("New Order for " + bp + ", Project=" + tel.getC_Project_ID());
			m_order = new MOrder (getCtx(), 0, get_TrxName());
			m_order.setAD_Org_ID(tel.getAD_Org_ID());
			m_order.setC_DocTypeTarget_ID(REF_C_DocTypeSubType.OnCreditOrder);
			//
			m_order.setBPartner(bp);
			if (m_order.getC_BPartner_Location_ID() == 0)
			{
				log.log(Level.SEVERE, "No BP Location: " + bp);
				addLog(0, te.getDateReport(), 
					null, "No Location: " + te.getDocumentNo() + " " + bp.getName());
				m_order = null;
				return;
			}
			m_order.setM_Warehouse_ID(te.getM_Warehouse_ID());
			if (tel.getC_Activity_ID() != 0)
				m_order.setC_Activity_ID(tel.getC_Activity_ID());
			if (tel.getC_Campaign_ID() != 0)							
				m_order.setC_Campaign_ID(tel.getC_Campaign_ID());
			if (tel.getC_Project_ID() != 0)
			{
				m_order.setC_Project_ID(tel.getC_Project_ID());
				//	Optionally Overwrite BP Price list from Project
				MProject project = new MProject (getCtx(), tel.getC_Project_ID(), get_TrxName());
				if (project.getM_PriceList_ID() != 0)
					m_order.setM_PriceList_ID(project.getM_PriceList_ID());
			}
			m_order.setSalesRep_ID(te.getDoc_User_ID());
			//
			if (!m_order.save())
			{
				throw new IllegalStateException("Cannot save Order");
			}
		}
		else
		{
			//	Update Header info
			if (tel.getC_Activity_ID() != 0 && tel.getC_Activity_ID() != m_order.getC_Activity_ID())
				m_order.setC_Activity_ID(tel.getC_Activity_ID());
			if (tel.getC_Campaign_ID() != 0 && tel.getC_Campaign_ID() != m_order.getC_Campaign_ID())
				m_order.setC_Campaign_ID(tel.getC_Campaign_ID());
			if (!m_order.save())
				new IllegalStateException("Cannot save Order");
		}
		
		//	OrderLine
		MOrderLine ol = new MOrderLine (m_order);
		//
		if (tel.getM_Product_ID() != 0)
			ol.setM_Product_ID(tel.getM_Product_ID(), 
				tel.getC_UOM_ID());
		if (tel.getS_ResourceAssignment_ID() != 0)
			ol.setS_ResourceAssignment_ID(tel.getS_ResourceAssignment_ID());
		ol.setQty(tel.getQtyInvoiced());		//	
		ol.setDescription(tel.getDescription());
		//
		ol.setC_Project_ID(tel.getC_Project_ID());
		ol.setC_ProjectPhase_ID(tel.getC_ProjectPhase_ID());
		ol.setC_ProjectTask_ID(tel.getC_ProjectTask_ID());
		ol.setC_Activity_ID(tel.getC_Activity_ID());
		ol.setC_Campaign_ID(tel.getC_Campaign_ID());
		//
		BigDecimal price = tel.getPriceInvoiced();	//	
		if (price != null && price.compareTo(Env.ZERO) != 0)
		{
			if (tel.getC_Currency_ID() != m_order.getC_Currency_ID())
				price = MConversionRate.convert(getCtx(), price, 
					tel.getC_Currency_ID(), m_order.getC_Currency_ID(), 
					m_order.getAD_Client_ID(), m_order.getAD_Org_ID());
			ol.setPrice(price);
		}
		else
			ol.setPrice();
		if (tel.getC_UOM_ID() != 0 && ol.getC_UOM_ID() == 0)
			ol.setC_UOM_ID(tel.getC_UOM_ID());
		ol.setTax();
		if (!ol.save())
		{
			throw new IllegalStateException("Cannot save Order Line");
		}
		//	Update TimeExpense Line
		tel.setC_OrderLine_ID(ol.getC_OrderLine_ID());
		if (tel.save())
			log.fine("Updated " + tel + " with C_OrderLine_ID");
		else
			log.log(Level.SEVERE, "Not Updated " + tel + " with C_OrderLine_ID");
			
	}	//	processLine
	
	
	/**
	 * 	Complete Order
	 */
	private void completeOrder ()
	{
		if (m_order == null)
			return;
		m_order.setDocAction(DocAction.ACTION_Prepare);
		m_order.processIt(DocAction.ACTION_Prepare);
		if (!m_order.save())
			throw new IllegalStateException("Cannot save Order");
		m_noOrders++;
		addLog (m_order.get_ID(), m_order.getDateOrdered(), m_order.getGrandTotal(), m_order.getDocumentNo());
		m_order = null;
	}	//	completeOrder

}	//	ExpenseSOrder
