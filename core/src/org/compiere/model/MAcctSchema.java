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
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.persistence.X_C_AcctSchema;
import org.compiere.model.persistence.X_M_CostElement;
import org.compiere.model.reference.REF_C_AcctSchemaCommitmentType;
import org.compiere.model.reference.REF_C_AcctSchemaCostingLevel;
import org.compiere.model.reference.REF_C_AcctSchemaCostingMethod;
import org.compiere.model.reference.REF_C_AcctSchemaGAAP;
import org.compiere.model.reference.REF_C_AcctSchemaTaxCorrectionType;
import org.compiere.util.*;

/**
 *  Accounting Schema Model (base)
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MAcctSchema.java 5583 2015-08-05 14:11:58Z xapiens $
 */
public class MAcctSchema extends X_C_AcctSchema
{
	/**
	 *  Get AccountSchema of Client
	 * 	@param ctx context
	 *  @param C_AcctSchema_ID schema id
	 *  @return Accounting schema
	 */
	public static MAcctSchema get (Properties ctx, int C_AcctSchema_ID)
	{
		return get(ctx, C_AcctSchema_ID, null);
	}	//	get

	/**
	 *  Get AccountSchema of Client
	 * 	@param ctx context
	 *  @param C_AcctSchema_ID schema id
	 *  @param trxName optional trx
	 *  @return Accounting schema
	 */
	public static MAcctSchema get (Properties ctx, int C_AcctSchema_ID, String trxName)
	{
		//  Check Cache
		Integer key = new Integer(C_AcctSchema_ID);
		MAcctSchema retValue = (MAcctSchema)s_cache.get(key);
		if (retValue != null)
			return retValue;
		retValue = new MAcctSchema (ctx, C_AcctSchema_ID, trxName);
		if (trxName == null && retValue.get_ID() > 0)
			s_cache.put(key, retValue);
		return retValue;
	}	//	get
	
	/**
	 *  Get AccountSchema of Client
	 * 	@param ctx context
	 *  @param AD_Client_ID client or 0 for all
	 *  @return Array of AcctSchema of Client
	 */
	public static MAcctSchema[] getClientAcctSchema (Properties ctx, int AD_Client_ID)
	{
		return getClientAcctSchema(ctx, AD_Client_ID, null);
	}	//	getClientAcctSchema
	
	/**
	 *  Get AccountSchema of Client
	 * 	@param ctx context
	 *  @param AD_Client_ID client or 0 for all
	 *  @param trxName optional trx 
	 *  @return Array of AcctSchema of Client
	 */
	public static MAcctSchema[] getClientAcctSchema (Properties ctx, int AD_Client_ID, String trxName)
	{
		//  Check Cache
		Integer key = new Integer(AD_Client_ID);
		if (s_schema.containsKey(key))
			return (MAcctSchema[])s_schema.get(key);

		//  Create New
		ArrayList<MAcctSchema> list = new ArrayList<MAcctSchema>();
		MClientInfo info = MClientInfo.get(ctx, AD_Client_ID, trxName); 
		MAcctSchema as = MAcctSchema.get (ctx, info.getC_AcctSchema1_ID(), trxName);
		if (as.get_ID() != 0 && trxName == null)
			list.add(as);
		
		//	Other
		String sql = "SELECT C_AcctSchema_ID FROM C_AcctSchema acs "
			+ "WHERE IsActive='Y'"
			+ " AND EXISTS (SELECT * FROM C_AcctSchema_GL gl WHERE acs.C_AcctSchema_ID=gl.C_AcctSchema_ID)"
			+ " AND EXISTS (SELECT * FROM C_AcctSchema_Default d WHERE acs.C_AcctSchema_ID=d.C_AcctSchema_ID)"; 
		if (AD_Client_ID != 0)
			sql += " AND AD_Client_ID=?";
		sql += " ORDER BY C_AcctSchema_ID";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, trxName);
			if (AD_Client_ID != 0)
				pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt(1);
				if (id != info.getC_AcctSchema1_ID())	//	already in list
				{
					as = MAcctSchema.get (ctx, id, trxName);
					if (as.get_ID() != 0 && trxName == null)
						list.add(as);
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		//  Save
		MAcctSchema[] retValue = new MAcctSchema [list.size()];
		list.toArray(retValue);
		if (trxName == null)
			s_schema.put(key, retValue);
		return retValue;
	}   //  getClientAcctSchema

	/** Cache of Client AcctSchema Arrays		**/
	private static CCache<Integer,MAcctSchema[]> s_schema = new CCache<Integer,MAcctSchema[]>("AD_ClientInfo", 3);	//  3 clients
	/**	Cache of AcctSchemas 					**/
	private static CCache<Integer,MAcctSchema> s_cache = new CCache<Integer,MAcctSchema>("C_AcctSchema", 3);	//  3 accounting schemas

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MAcctSchema.class);	
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_AcctSchema_ID id
	 *	@param trxName transaction
	 */
	public MAcctSchema (Properties ctx, int C_AcctSchema_ID, String trxName)
	{
		super (ctx, C_AcctSchema_ID, trxName);
		if (C_AcctSchema_ID == 0)
		{
		//	setC_Currency_ID (0);
		//	setName (null);
			setAutoPeriodControl (true);
			setPeriod_OpenFuture(2);
			setPeriod_OpenHistory(2);
			setCostingMethod (REF_C_AcctSchemaCostingMethod.StandardCosting);
			setCostingLevel(REF_C_AcctSchemaCostingLevel.Client);
			setIsAdjustCOGS(false);
			setGAAP (REF_C_AcctSchemaGAAP.InternationalGAAP);
			setHasAlias (true);
			setHasCombination (false);
			setIsAccrual (true);	// Y
			setCommitmentType(REF_C_AcctSchemaCommitmentType.None);
			setIsDiscountCorrectsTax (false);
			setTaxCorrectionType(REF_C_AcctSchemaTaxCorrectionType.None);
			setIsTradeDiscountPosted (false);
			setIsPostServices(false);
			setIsExplicitCostAdjustment(false);
			setSeparator ("-");	// -
		}
	}	//	MAcctSchema
	
	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MAcctSchema (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MAcctSchema

	/**
	 * 	Parent Constructor
	 *	@param client client
	 *	@param currency currency
	 */
	public MAcctSchema (MClient client, KeyNamePair currency)
	{
		this (client.getCtx(), 0, client.get_TrxName());
		setClientOrg(client);
		setC_Currency_ID (currency.getKey());
		setName (client.getName() + " " + getGAAP() + "/" + get_ColumnCount() + " " + currency.getName());
	}	//	MAcctSchema


	/** Element List       */
	private MAcctSchemaElement[]	m_elements = null;
	/** GL Info				*/
	private MAcctSchemaGL			m_gl = null;
	/** Default Info		*/
	private MAcctSchemaDefault		m_default = null;
	
	private MAccount				m_SuspenseError_Acct = null;
	private MAccount				m_CurrencyBalancingIncome_Acct = null;
	private MAccount				m_CurrencyBalancingLoss_Acct = null;
	private MAccount				m_DueTo_Acct = null;
	private MAccount				m_DueFrom_Acct = null;
	/** Accounting Currency Precision	*/
	private int						m_stdPrecision = -1;
	/** Costing Currency Precision		*/
	private int						m_costPrecision = -1;
	
	/** Only Post Org					*/
	private MOrg					m_onlyOrg = null;
	/** Only Post Org Childs			*/
	private Integer[] 				m_onlyOrgs = null; 

	/**************************************************************************
	 *  AcctSchema Elements
	 *  @return ArrayList of AcctSchemaElement
	 */
	public MAcctSchemaElement[] getAcctSchemaElements()
	{
		if (m_elements == null)
			m_elements = MAcctSchemaElement.getAcctSchemaElements(this);
		return m_elements;
	}   //  getAcctSchemaElements

	/**
	 *  Get AcctSchema Element
	 *  @param elementType segment type - AcctSchemaElement.ELEMENTTYPE_
	 *  @return AcctSchemaElement
	 */
	public MAcctSchemaElement getAcctSchemaElement (String elementType)
	{
		if (m_elements == null)
			getAcctSchemaElements();
		for (int i = 0; i < m_elements.length; i++)
		{
			MAcctSchemaElement ase = m_elements[i];
			if (ase.getElementType().equals(elementType))
				return ase;
		}
		return null;
	}   //  getAcctSchemaElement

	/**
	 *  Has AcctSchema Element
	 *  @param segmentType segment type - AcctSchemaElement.SEGMENT_
	 *  @return true if schema has segment type
	 */
	public boolean isAcctSchemaElement (String segmentType)
	{
		return getAcctSchemaElement(segmentType) != null;
	}   //  isAcctSchemaElement

	/**
	 * 	Get AcctSchema GL info
	 *	@return GL info
	 */
	public MAcctSchemaGL getAcctSchemaGL()
	{
		if (m_gl == null)
			m_gl = MAcctSchemaGL.get(getCtx(), getC_AcctSchema_ID());
		if (m_gl == null)
			throw new IllegalStateException("No GL Definition for C_AcctSchema_ID=" + getC_AcctSchema_ID());
		return m_gl;
	}	//	getAcctSchemaGL
	
	/**
	 * 	Get AcctSchema Defaults
	 *	@return defaults
	 */
	public MAcctSchemaDefault getAcctSchemaDefault()
	{
		if (m_default == null)
			m_default = MAcctSchemaDefault.get(getCtx(), getC_AcctSchema_ID());
		if (m_default == null)
			throw new IllegalStateException("No Default Definition for C_AcctSchema_ID=" + getC_AcctSchema_ID());
		return m_default;
	}	//	getAcctSchemaDefault

	/**
	 *	String representation
	 *  @return String rep
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("AcctSchema[");
			sb.append(get_ID()).append("-").append(getName())
				.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Is Suspense Balancing active
	 *	@return suspense balancing
	 */
	public boolean isSuspenseBalancing()
	{
		if (m_gl == null)
			getAcctSchemaGL();
		return m_gl.isUseSuspenseBalancing() && m_gl.getSuspenseBalancing_Acct() != 0;
	}	//	isSuspenseBalancing

	/**
	 *	Get Suspense Error Account
	 *  @return suspense error account
	 */
	public MAccount getSuspenseBalancing_Acct()
	{
		if (m_SuspenseError_Acct != null)
			return m_SuspenseError_Acct;
		if (m_gl == null)
			getAcctSchemaGL();
		int C_ValidCombination_ID = m_gl.getSuspenseBalancing_Acct();
		m_SuspenseError_Acct = MAccount.get(getCtx(), C_ValidCombination_ID);
		return m_SuspenseError_Acct;
	}	//	getSuspenseBalancing_Acct

	/**
	 * 	Is Currency Balancing active
	 *	@return suspense balancing
	 */
	public boolean isCurrencyBalancing()
	{
		if (m_gl == null)
			getAcctSchemaGL();
		return m_gl.isUseCurrencyBalancing();
	}	//	isSuspenseBalancing

	/**
	 *	Get Currency Balancing Account for incomes
	 *  @return currency balancing account
	 */
	public MAccount getCurrencyBalancingIncome_Acct()
	{
		if (m_CurrencyBalancingIncome_Acct != null)
			return m_CurrencyBalancingIncome_Acct;
		if (m_gl == null)
			getAcctSchemaGL();
		int C_ValidCombination_ID = m_gl.getCurrencyBalancingIncome_Acct();
		m_CurrencyBalancingIncome_Acct = MAccount.get(getCtx(), C_ValidCombination_ID);
		return m_CurrencyBalancingIncome_Acct;
	}	//	getCurrencyBalancingIncome_Acct

	/**
	 *	Get Currency Balancing Account for loses
	 *  @return currency balancing account
	 */
	public MAccount getCurrencyBalancingLoss_Acct()
	{
		if (m_CurrencyBalancingLoss_Acct != null)
			return m_CurrencyBalancingLoss_Acct;
		if (m_gl == null)
			getAcctSchemaGL();
		int C_ValidCombination_ID = m_gl.getCurrencyBalancingLoss_Acct();
		m_CurrencyBalancingLoss_Acct = MAccount.get(getCtx(), C_ValidCombination_ID);
		return m_CurrencyBalancingLoss_Acct;
	}	//	getCurrencyBalancingLoss_Acct

	/**
	 *	Get Due To Account for Segment
	 *  @param segment ignored
	 *  @return Account
	 */
	public MAccount getDueTo_Acct(String segment)
	{
		if (m_DueTo_Acct != null)
			return m_DueTo_Acct;
		if (m_gl == null)
			getAcctSchemaGL();
		int C_ValidCombination_ID = m_gl.getIntercompanyDueTo_Acct();
		m_DueTo_Acct = MAccount.get(getCtx(), C_ValidCombination_ID);
		return m_DueTo_Acct;
	}	//	getDueTo_Acct

	/**
	 *	Get Due From Account for Segment
	 *  @param segment ignored
	 *  @return Account
	 */
	public MAccount getDueFrom_Acct(String segment)
	{
		if (m_DueFrom_Acct != null)
			return m_DueFrom_Acct;
		if (m_gl == null)
			getAcctSchemaGL();
		int C_ValidCombination_ID = m_gl.getIntercompanyDueFrom_Acct();
		m_DueFrom_Acct = MAccount.get(getCtx(), C_ValidCombination_ID);
		return m_DueFrom_Acct;
	}	//	getDueFrom_Acct

	/**
	 * 	Set Only Org Childs
	 *	@param orgs
	 */
	public void setOnlyOrgs (Integer[] orgs)
	{
		m_onlyOrgs = orgs;
	}	//	setOnlyOrgs
	
	/**
	 * 	Set Only Org Childs
	 *	@return orgs
	 */
	public Integer[] getOnlyOrgs()
	{
		return m_onlyOrgs;
	}	//	getOnlyOrgs

	/**
	 * 	Skip creating postings for this Org.
	 * 	Requires setOnlyOrgs (MReportTree requires MTree in Basis)
	 *	@param AD_Org_ID
	 *	@return true if to skip
	 */
	public boolean isSkipOrg (int AD_Org_ID)
	{
		if (getAD_OrgOnly_ID() == 0)
			return false;
		//	Only Organization
		if (getAD_OrgOnly_ID() == AD_Org_ID)
			return false;
		if (m_onlyOrg == null)
			m_onlyOrg = MOrg.get(getCtx(), getAD_OrgOnly_ID());
		//	Not Summary Only - i.e. skip it
		if (!m_onlyOrg.isSummary())
			return true;
		if (m_onlyOrgs == null)
			return false;
		for (int i = 0; i < m_onlyOrgs.length; i++)
		{
			if (AD_Org_ID == m_onlyOrgs[i].intValue())
				return false;
		}
		return true;
	}	//	isSkipOrg
	
	/**
	 * 	Get Std Precision of accounting Currency
	 *	@return precision
	 */
	public int getStdPrecision()
	{
		if (m_stdPrecision < 0)
		{
			MCurrency cur = MCurrency.get(getCtx(), getC_Currency_ID());
			m_stdPrecision = cur.getStdPrecision();
			m_costPrecision = cur.getCostingPrecision();
		}
		return m_stdPrecision;
	}	//	getStdPrecision

	/**
	 * 	Get Costing Precision of accounting Currency
	 *	@return precision
	 */
	public int getCostingPrecision()
	{
		if (m_costPrecision < 0)
			getStdPrecision();
		return m_costPrecision;
	}	//	getCostingPrecision
	
	
	/**
	 * 	Check Costing Setup.
	 * 	Make sure that there is a Cost Type and Cost Element
	 */
	public void checkCosting()
	{
		log.info(toString());
		//  Create Cost Element
		if (getM_CostElement_ID() == 0)
		{
			//MCostElement ce = MCostElement.getMaterialCostElement(this, REF_C_AcctSchemaCostingMethod.AveragePO);
			//setM_CostElement_ID(ce.getM_CostElement_ID());
			setCostingMethod (REF_C_AcctSchemaCostingMethod.AveragePO);			
		}
		if (getCostedFrom() == null)
		{
			setCostedFrom(Timestamp.valueOf("2010-01-01 01:01:01"));
		}
//		//	Create Cost Type
//		if (getM_CostType_ID() == 0)
//		{
//			MCostType ct = new MCostType (getCtx(), 0, get_TrxName());
//			ct.setClientOrg(getAD_Client_ID(), 0);
//			ct.setName(getName());
//			ct.save();
//			setM_CostType_ID(ct.getM_CostType_ID());
//		}
//		if (getM_CostElement_ID() > 0)
//		{
//			MCostElement ce  = new MCostElement(Env.getCtx(), this.getM_CostElement_ID(), get_TrxName());
//			if (ce.getM_CostElement_ID() == 0)
//				s_log.severe("No Cost Element defined");
//		}
//		else
//			s_log.severe("No Cost Element defined");
		
		//	Default Costing Level
		if (getCostingLevel() == null)
			setCostingLevel(REF_C_AcctSchemaCostingLevel.Client);
		if (getGAAP() == null)
			setGAAP (REF_C_AcctSchemaGAAP.InternationalGAAP);
	}	//	checkCosting
	
	/**
	 * 	Is Client Costing Level (default)
	 *	@return true if Client
	 */
	public boolean isCostingLevelClient()
	{
		String s = getCostingLevel();
		if (s == null || REF_C_AcctSchemaCostingLevel.Client.equals(s))
			return true;
		return false;
	}	//	isCostingLevelClient
	
	/**
	 * 	Is Org Costing Level
	 *	@return true if Org
	 */
	public boolean isCostingLevelOrg()
	{
		return REF_C_AcctSchemaCostingLevel.Organization.equals(getCostingLevel());
	}	//	isCostingLevelOrg
	
	/**
	 * 	Is Batch Costing Level
	 *	@return true if Batch
	 */
	public boolean isCostingLevelBatch()
	{
		return REF_C_AcctSchemaCostingLevel.BatchLot.equals(getCostingLevel());
	}	//	isCostingLevelBatch

	/**
	 * 	Create Commitment Accounting
	 *	@return true if creaet commitments
	 */
	public boolean isCreateCommitment()
	{
		String s = getCommitmentType();
		if (s == null)
			return false;
		return REF_C_AcctSchemaCommitmentType.CommitmentOnly.equals(s)
			|| REF_C_AcctSchemaCommitmentType.CommitmentReservation.equals(s);
	}	//	isCreateCommitment

	/**
	 * 	Create Commitment/Reservation Accounting
	 *	@return true if create reservations
	 */
	public boolean isCreateReservation()
	{
		String s = getCommitmentType();
		if (s == null)
			return false;
		return REF_C_AcctSchemaCommitmentType.CommitmentReservation.equals(s);
	}	//	isCreateReservation

	/**
	 * 	Get Tax Correction Type
	 *	@return tax correction type
	 */
	public String getTaxCorrectionType()
	{
		if (super.getTaxCorrectionType() == null)	//	created 07/23/06 2.5.3d
			setTaxCorrectionType(isDiscountCorrectsTax() 
				? REF_C_AcctSchemaTaxCorrectionType.Write_OffAndDiscount : REF_C_AcctSchemaTaxCorrectionType.None);
		return super.getTaxCorrectionType ();
	}	//	getTaxCorrectionType
	
	/**
	 * 	Tax Correction
	 *	@return true if not none
	 */
	public boolean isTaxCorrection()
	{
		return !getTaxCorrectionType().equals(REF_C_AcctSchemaTaxCorrectionType.None);
	}	//	isTaxCorrection
	
	/**
	 * 	Tax Correction for Discount
	 *	@return true if tax is corrected for Discount 
	 */
	public boolean isTaxCorrectionDiscount()
	{
		return getTaxCorrectionType().equals(REF_C_AcctSchemaTaxCorrectionType.DiscountOnly)
			|| getTaxCorrectionType().equals(REF_C_AcctSchemaTaxCorrectionType.Write_OffAndDiscount);
	}	//	isTaxCorrectionDiscount

	/**
	 * 	Tax Correction for WriteOff
	 *	@return true if tax is corrected for WriteOff 
	 */
	public boolean isTaxCorrectionWriteOff()
	{
		return getTaxCorrectionType().equals(REF_C_AcctSchemaTaxCorrectionType.Write_OffOnly)
			|| getTaxCorrectionType().equals(REF_C_AcctSchemaTaxCorrectionType.Write_OffAndDiscount);
	}	//	isTaxCorrectionWriteOff

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (getAD_Org_ID() != 0)
			setAD_Org_ID(0);
		if (super.getTaxCorrectionType() == null)
			setTaxCorrectionType(isDiscountCorrectsTax() 
				? REF_C_AcctSchemaTaxCorrectionType.Write_OffAndDiscount : REF_C_AcctSchemaTaxCorrectionType.None);
		checkCosting();
		//	Check Primary
		if (getAD_OrgOnly_ID() != 0)
		{
			MClientInfo info = MClientInfo.get(getCtx(), getAD_Client_ID());
			if (info.getC_AcctSchema1_ID() == getC_AcctSchema_ID())
				setAD_OrgOnly_ID(0);
		}
		return true;
	}	//	beforeSave

	public static MAcctSchema getFirst() {
		// TODO Auto-generated method stub
		return null;
	}
	
}	//	MAcctSchema
