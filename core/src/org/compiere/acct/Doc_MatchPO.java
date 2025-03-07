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
package org.compiere.acct;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MConversionRate;
import org.compiere.model.MMatchPO;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MProductCategoryAcct;
import org.compiere.model.ProductCost;
import org.compiere.model.reference.REF_C_AcctSchemaCostingMethod;
import org.compiere.model.reference.REF_C_DocTypeDocBaseType;
import org.compiere.util.*;

/**
 *  Post MatchPO Documents.
 *  <pre>
 *  Table:              C_MatchPO (473)
 *  Document Types:     MXP
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_MatchPO.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_MatchPO extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
	protected Doc_MatchPO (MAcctSchema[] ass, ResultSet rs, String trxName)
	{
		super(ass, MMatchPO.class, rs, REF_C_DocTypeDocBaseType.MatchPO, trxName);
	}   //  Doc_MatchPO

	private int         m_C_OrderLine_ID = 0;
	private MOrderLine	m_oLine = null;
	//
	private int         m_M_InOutLine_ID = 0;
	private int         m_C_InvoiceLine_ID = 0;
	private ProductCost m_pc;
	private int			m_M_AttributeSetInstance_ID = 0;

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	protected String loadDocumentDetails ()
	{
		setC_Currency_ID (Doc.NO_CURRENCY);
		MMatchPO matchPO = (MMatchPO)getPO();
		setDateDoc(matchPO.getDateTrx());
		//
		m_M_AttributeSetInstance_ID = matchPO.getM_AttributeSetInstance_ID();
		setQty (matchPO.getQty());
		//
		m_C_OrderLine_ID = matchPO.getC_OrderLine_ID();
		m_oLine = new MOrderLine (getCtx(), m_C_OrderLine_ID, getTrxName());
		//
		m_M_InOutLine_ID = matchPO.getM_InOutLine_ID();
		m_C_InvoiceLine_ID = matchPO.getC_InvoiceLine_ID();
		//
		m_pc = new ProductCost (Env.getCtx(), 
			getM_Product_ID(), m_M_AttributeSetInstance_ID, getTrxName());
		m_pc.setQty(getQty());
		return null;
	}   //  loadDocumentDetails

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return Zero - always balanced
	 */
	public BigDecimal getBalance()
	{
		return Env.ZERO;
	}   //  getBalance

	
	/**
	 *  Create Facts (the accounting logic) for
	 *  MXP.
	 *  <pre>
	 *      Product PPV     <difference>
	 *      PPV_Offset                  <difference>
	 *  </pre>
	 *  @param as accounting schema
	 *  @return Fact
	 */
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		ArrayList<Fact> facts = new ArrayList<Fact>();
		//
		if (getM_Product_ID() == 0		//  Nothing to do if no Product
			|| getQty().signum() == 0
			|| m_M_InOutLine_ID == 0)	//  No posting if not matched to Shipment
		{
			log.fine("No Product/Qty - M_Product_ID=" + getM_Product_ID()
				+ ",Qty=" + getQty());
			return facts;
		}

		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		setC_Currency_ID(as.getC_Currency_ID());
		
		//	Purchase Order Line
		BigDecimal poCost = m_oLine.getPriceCost();
		if (poCost == null || poCost.signum() == 0)
			poCost = m_oLine.getPriceActual();
		poCost = poCost.multiply(getQty());			//	Delivered so far
		//	Different currency
		if (m_oLine.getC_Currency_ID() != as.getC_Currency_ID())
		{
			MOrder order = m_oLine.getParent();
			BigDecimal rate = MConversionRate.getRate(order.getC_Currency_ID(), as.getC_Currency_ID(),
				order.getDateAcct(), order.getC_ConversionType_ID(),
				m_oLine.getAD_Client_ID(), m_oLine.getAD_Org_ID());
			if (rate == null)
			{
				p_Error = "Purchase Order not convertible - " + as.getName();
				return null;
			}
			poCost = poCost.multiply(rate);
			if (poCost.scale() > as.getCostingPrecision())
				poCost = poCost.setScale(as.getCostingPrecision(), BigDecimal.ROUND_HALF_UP);
		}

		//	Create PO Cost Detail Record first
		// MZ Goodwill
		// Create Cost Detail Matched PO using Total Amount and Total Qty based on OrderLine
		MMatchPO[] mPO = MMatchPO.getOrderLine(getCtx(), m_oLine.getC_OrderLine_ID(), getTrxName());
		BigDecimal tQty = Env.ZERO;
		BigDecimal tAmt = Env.ZERO;
		BigDecimal priceCost = m_oLine.getPriceCost();
		if (priceCost == null || priceCost.signum() == 0)
			priceCost = m_oLine.getPriceActual();
		for (int i = 0 ; i < mPO.length ; i++)
		{
			if (mPO[i].isPosted() &&  mPO[i].getM_MatchPO_ID() != get_ID())
			{
				tQty = tQty.add(mPO[i].getQty());
				tAmt = tAmt.add(priceCost.multiply(mPO[i].getQty()));
			}
		}

		//	Different currency
		if (m_oLine.getC_Currency_ID() != as.getC_Currency_ID())
		{
			MOrder order = m_oLine.getParent();
			BigDecimal rate = MConversionRate.getRate(order.getC_Currency_ID(), as.getC_Currency_ID(),
				order.getDateAcct(), order.getC_ConversionType_ID(),
				m_oLine.getAD_Client_ID(), m_oLine.getAD_Org_ID());
			if (rate == null)
			{
				p_Error = "Purchase Order not convertible - " + as.getName();
				return null;
			}
			tAmt = tAmt.multiply(rate);
			if (tAmt.scale() > as.getCostingPrecision())
				tAmt = tAmt.setScale(as.getCostingPrecision(), BigDecimal.ROUND_HALF_UP);
		}
		
		tAmt = tAmt.add(poCost);
		tQty = tQty.add(getQty());		

		// Set Total Amount and Total Quantity from Matched PO 
		//MCostDetail.createOrder(as, m_oLine.getAD_Org_ID(), 
		//		getM_Product_ID(), m_M_AttributeSetInstance_ID,
		//	m_C_OrderLine_ID, 0,		//	no cost element
		//		tAmt, tQty,			//	Delivered
		//		m_oLine.getDescription(), getTrxName());
		// end MZ

		//	Calculate PPV for standard costing
		String costingMethod = as.getCostingMethod();
		MProduct product = MProduct.get(getCtx(), getM_Product_ID(), getTrxName());
		MProductCategoryAcct pca = MProductCategoryAcct.get(getCtx(), 
			product.getM_Product_Category_ID(), as.getC_AcctSchema_ID(), getTrxName());
		if (pca.getCostingMethod() != null)
			costingMethod = pca.getCostingMethod();
		
		//get standard cost and also makesure cost for other costing method is updated
		BigDecimal costs = m_pc.getProductCosts(as, getAD_Org_ID(), 
			REF_C_AcctSchemaCostingMethod.StandardCosting, m_C_OrderLine_ID, false, getDateAcct());	//	non-zero costs

		if (REF_C_AcctSchemaCostingMethod.StandardCosting.equals(costingMethod))
		{
			//	No Costs yet - no PPV
			if (costs == null || costs.signum() == 0)
			{
				p_Error = "Resubmit - No Costs for " + product.getName();
				log.log(Level.SEVERE, p_Error);
				return null;
			}
	
			//	Difference
			BigDecimal difference = poCost.subtract(costs);
			//	Nothing to post
			if (difference.signum() == 0)
			{
				log.log(Level.FINE, "No Cost Difference for M_Product_ID=" + getM_Product_ID());
				return facts;
			}
	
			//  Product PPV
			FactLine cr = fact.createLine(null,
				m_pc.getAccount(ProductCost.ACCTTYPE_P_PPV, as),
				as.getC_Currency_ID(), difference);
			if (cr != null)
			{
				cr.setQty(getQty());
				cr.setC_BPartner_ID(m_oLine.getC_BPartner_ID());
				cr.setC_Activity_ID(m_oLine.getC_Activity_ID());
				cr.setC_Campaign_ID(m_oLine.getC_Campaign_ID());
				cr.setC_Project_ID(m_oLine.getC_Project_ID());
				cr.setC_UOM_ID(m_oLine.getC_UOM_ID());
				cr.setUser1_ID(m_oLine.getUser1_ID());
				cr.setUser2_ID(m_oLine.getUser2_ID());
			}
	
			//  PPV Offset
			FactLine dr = fact.createLine(null,
				getAccount(Doc.ACCTTYPE_PPVOffset, as),
				as.getC_Currency_ID(), difference.negate());
			if (dr != null)
			{
				dr.setQty(getQty().negate());
				dr.setC_BPartner_ID(m_oLine.getC_BPartner_ID());
				dr.setC_Activity_ID(m_oLine.getC_Activity_ID());
				dr.setC_Campaign_ID(m_oLine.getC_Campaign_ID());
				dr.setC_Project_ID(m_oLine.getC_Project_ID());
				dr.setC_UOM_ID(m_oLine.getC_UOM_ID());
				dr.setUser1_ID(m_oLine.getUser1_ID());
				dr.setUser2_ID(m_oLine.getUser2_ID());
			}
			
			//
			facts.add(fact);
			return facts;
		}
		else
		{
			return facts;
		}
	}   //  createFact

	/**
	 *  Update Product Info (old).
	 *  - Costing (CostStandardPOQty, CostStandardPOAmt)
	 *  @param C_AcctSchema_ID accounting schema
	 *  @deprecated old costing
	 */
	private void updateProductInfo (int C_AcctSchema_ID)
	{
		log.fine("M_MatchPO_ID=" + get_ID());

		//  update Product Costing
		//  requires existence of currency conversion !!
		StringBuffer sql = new StringBuffer (
			"UPDATE M_Product_Costing pc "
			+ "SET (CostStandardPOQty,CostStandardPOAmt) = "
			+ "(SELECT CostStandardPOQty + m.Qty,"
			+ " CostStandardPOAmt + currencyConvert(ol.PriceActual,0,ol.C_Currency_ID,a.C_Currency_ID,ol.DateOrdered,null,ol.AD_Client_ID,ol.AD_Org_ID)*m.Qty "
			+ "FROM M_MatchPO m, C_OrderLine ol, C_AcctSchema a "
			+ "WHERE m.C_OrderLine_ID=ol.C_OrderLine_ID"
			+ " AND pc.M_Product_ID=ol.M_Product_ID"
			+ " AND pc.C_AcctSchema_ID=a.C_AcctSchema_ID"
			+ "AND m.M_MatchPO_ID=").append(get_ID()).append(") ")
			.append("WHERE pc.C_AcctSchema_ID=").append(C_AcctSchema_ID)
			.append(" AND pc.M_Product_ID=").append(getM_Product_ID());
		int no = DB.executeUpdate(sql.toString(), getTrxName());
		log.fine("M_Product_Costing - Updated=" + no);
	}   //  updateProductInfo


	public void createFact_ID() {
		
	}

}   //  Doc_MatchPO
