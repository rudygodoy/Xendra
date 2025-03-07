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

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.persistence.X_C_BP_DocType;
import org.compiere.model.persistence.X_C_BP_Withholding;
import org.compiere.model.persistence.X_C_BPartner;
import org.compiere.model.persistence.X_I_BPartner;
import org.compiere.model.reference.REF_AD_TreeTypeType;
import org.compiere.model.reference.REF_C_BP_DocTypeI;
import org.compiere.model.reference.REF_C_BPartnerSOCreditStatus;
import org.compiere.util.*;
import org.xendra.utils.Sunat;

/**
 *	Business Partner Model
 * <p>
 * Change log:
 * <ul>
 * <li>2007-01-31 - teo_sarca - [ 1568774 ] Walk-In BP: invalid created/updated values 
 * </ul>
 *
 *  @author Jorg Janke
 *  @version $Id: MBPartner.java 5762 2016-02-18 02:16:12Z xapiens $
 */
public class MBPartner extends X_C_BPartner
{
	/**
	 * 	Get Empty Template Business Partner
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@return Template Business Partner or null
	 */
	public static MBPartner getTemplate (Properties ctx, int AD_Client_ID)
	{
		MBPartner template = getBPartnerCashTrx (ctx, AD_Client_ID);
		if (template == null)
			template = new MBPartner (ctx, 0, null);
		//	Reset
		if (template != null)
		{
			template.set_ValueNoCheck ("C_BPartner_ID", new Integer(0));
			template.setValue ("");
			template.setName ("");
			template.setName2 (null);
			template.setDUNS("");
			template.setFirstSale(null);
			//
			template.setSO_CreditLimit (Env.ZERO);
			template.setSO_CreditUsed (Env.ZERO);
			template.setTotalOpenBalance (Env.ZERO);
			//	s_template.setRating(null);
			//
			template.setActualLifeTimeValue(Env.ZERO);
			template.setPotentialLifeTimeValue(Env.ZERO);
			template.setAcqusitionCost(Env.ZERO);
			template.setShareOfCustomer(0);
			template.setSalesVolume(0);
			// Reset Created, Updated to current system time ( teo_sarca )
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			template.set_ValueNoCheck("Created", ts);
			template.set_ValueNoCheck("Updated", ts);
			// overwrite the identifier 
			template.setIdentifier(Util.getUUID());
		}
		return template;
	}	//	getTemplate

	/**
	 * 	Get Cash Trx Business Partner
	 * 	@param ctx context
	 * 	@param AD_Client_ID client
	 * 	@return Cash Trx Business Partner or null
	 */
	public static MBPartner getBPartnerCashTrx (Properties ctx, int AD_Client_ID)
	{
		MBPartner retValue = null;
		String sql = "SELECT * FROM C_BPartner "
				+ "WHERE C_BPartner_ID IN (SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo WHERE AD_Client_ID=?)";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MBPartner (ctx, rs, null);
			else
				s_log.log(Level.SEVERE, "Not found for AD_Client_ID=" + AD_Client_ID);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
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
		return retValue;
	}	//	getBPartnerCashTrx

	/**
	 * 	Get BPartner with Value
	 *	@param ctx context 
	 *	@param Value value
	 *	@return BPartner or null
	 */
	public static MBPartner get (Properties ctx, String Value)
	{
		if (Value == null || Value.length() == 0)
			return null;
		MBPartner retValue = null;
		int AD_Client_ID = Env.getAD_Client_ID(ctx);
		String sql = "SELECT * FROM C_BPartner WHERE Value=? AND AD_Client_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setString(1, Value);
			pstmt.setInt(2, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MBPartner(ctx, rs, null);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return retValue;
	}	//	get

	/**
	 * 	Get BPartner with Value
	 *	@param ctx context 
	 *	@param Value value
	 *	@return BPartner or null
	 */
	public static MBPartner get (Properties ctx, int C_BPartner_ID)
	{		
		MBPartner partner = new Query(Env.getCtx(), X_C_BPartner.Table_Name, " C_BPartner_ID = ? ", null)
		.setParameters(C_BPartner_ID).setClient_ID().first();
		return partner;
	}	//	get

	/**
	 * 	Get Not Invoiced Shipment Value
	 *	@param C_BPartner_ID partner
	 *	@return value in accounting currency
	 */
	public static BigDecimal getNotInvoicedAmt (int C_BPartner_ID)
	{
		BigDecimal retValue = null;
		String sql = "SELECT SUM(COALESCE("
				+ "currencyBase((ol.QtyDelivered-ol.QtyInvoiced)*ol.PriceActual,o.C_Currency_ID,o.DateOrdered, o.AD_Client_ID,o.AD_Org_ID) ,0)) "
				+ "FROM C_OrderLine ol"
				+ " INNER JOIN C_Order o ON (ol.C_Order_ID=o.C_Order_ID) "
				+ "WHERE o.IsSOTrx='Y' AND Bill_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return retValue;
	}	//	getNotInvoicedAmt


	/**	Static Logger				*/
	private static CLogger		s_log = CLogger.getCLogger (MBPartner.class);

	/**************************************************************************
	 * 	Constructor for new BPartner from Template
	 * 	@param ctx context
	 */
	public MBPartner (Properties ctx)
	{
		this (ctx, -1, null);
	}	//	MBPartner

	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 * 	@param trxName transaction
	 */
	public MBPartner (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MBPartner

	/**
	 * 	Default Constructor
	 * 	@param ctx context
	 * 	@param C_BPartner_ID partner or 0 or -1 (load from template)
	 * 	@param trxName transaction
	 */
	public MBPartner (Properties ctx, int C_BPartner_ID, String trxName)
	{
		super (ctx, C_BPartner_ID, trxName);
		//
		if (C_BPartner_ID == -1)
		{
			initTemplate (Env.getContextAsInt(ctx, "AD_Client_ID"));
			C_BPartner_ID = 0;
		}
		if (C_BPartner_ID == 0)
		{
			//	setValue ("");
			//	setName ("");
			//	setName2 (null);
			//	setDUNS("");
			//
			setSendEMail (false);
			setIsOneTime (false);
			setIsSummary (false);
			setIsTaxExempt(false);
			setIsDiscountPrinted(false);
			//
			setSO_CreditLimit (Env.ZERO);
			setSO_CreditUsed (Env.ZERO);
			setTotalOpenBalance (Env.ZERO);
			setSOCreditStatus(REF_C_BPartnerSOCreditStatus.NoCreditCheck);
			//
			setFirstSale(null);
			setActualLifeTimeValue(Env.ZERO);
			setPotentialLifeTimeValue(Env.ZERO);
			setAcqusitionCost(Env.ZERO);
			setShareOfCustomer(0);
			setSalesVolume(0);
			setIsVendor(isVendor());
			setIsCustomer(isCustomer());
			setIsSalesRep(isSalesRep());
			setIsProspect(isProspect());
			setIsEmployee(isEmployee());
		}
		log.fine(toString());
	}	//	MBPartner

	/**
	 * 	Import Contstructor
	 *	@param impBP import
	 */
	public MBPartner (X_I_BPartner impBP)
	{
		this (impBP.getCtx(), 0, impBP.get_TrxName());
		setClientOrg(impBP);
		setUpdatedBy(impBP.getUpdatedBy());
		//
		String value = impBP.getValue();
		if (value == null || value.length() == 0)
			value = impBP.getEMail();
		if (value == null || value.length() == 0)
			value = impBP.getContactName();
		setValue(value);
		String name = impBP.getName();
		if (name == null || name.length() == 0)
			name = impBP.getContactName();
		if (name == null || name.length() == 0)
			name = impBP.getEMail();
		setName(name);
		setName2(impBP.getName2());
		setDescription(impBP.getDescription());
		//	setHelp(impBP.getHelp());
		setDUNS(impBP.getDUNS());
		setTaxID(impBP.getTaxID());
		setNAICS(impBP.getNAICS());
		setC_BP_Group_ID(impBP.getC_BP_Group_ID());

	}	//	MBPartner


	/** Users							*/
	private MUser[]					m_contacts = null;
	/** Addressed						*/
	private MBPartnerLocation[]		m_locations = null;
	/** BP Bank Accounts				*/
	private MBPBankAccount[]		m_accounts = null;
	/** */
	private MWithholding[]			m_Withholdings = null;
	private MWithholding[]			m_POWithholdings = null;
	/** Prim Address					*/
	private Integer					m_primaryC_BPartner_Location_ID = null;
	/** Prim User						*/
	private Integer					m_primaryAD_User_ID = null;
	/** Credit Limit recently calcualted		*/
	private boolean 				m_TotalOpenBalanceSet = false;
	/** BP Group						*/
	private MBPGroup				m_group = null;

	/**
	 * 	Load Default BPartner
	 * 	@param AD_Client_ID client
	 * 	@return true if loaded
	 */
	private boolean initTemplate (int AD_Client_ID)
	{
		if (AD_Client_ID == 0)
			throw new IllegalArgumentException ("Client_ID=0");

		boolean success = true;
		String sql = "SELECT * FROM C_BPartner "
				+ "WHERE C_BPartner_ID IN (SELECT C_BPartnerCashTrx_ID FROM AD_ClientInfo WHERE AD_Client_ID=?)";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				success = load (rs);
			else
			{
				load(0, null);
				success = false;
				log.severe ("None found");
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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
		setStandardDefaults();
		//	Reset
		set_ValueNoCheck ("C_BPartner_ID", I_ZERO);
		setValue ("");
		setName ("");
		setName2(null);
		return success;
	}	//	getTemplate


	/**
	 * 	Get All Contacts
	 * 	@param reload if true users will be requeried
	 *	@return contacts
	 */
	public MUser[] getContacts (boolean reload)
	{
		if (reload || m_contacts == null || m_contacts.length == 0)
			;
		else
			return m_contacts;
		//
		ArrayList<MUser> list = new ArrayList<MUser>();
		String sql = "SELECT * FROM AD_User WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MUser (getCtx(), rs, null));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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

		m_contacts = new MUser[list.size()];
		list.toArray(m_contacts);
		return m_contacts;
	}	//	getContacts

	/**
	 * 	Get specified or first Contact
	 * 	@param AD_User_ID optional user
	 *	@return contact or null
	 */
	public MUser getContact (int AD_User_ID)
	{
		MUser[] users = getContacts(false);
		if (users.length == 0)
			return null;
		for (int i = 0; AD_User_ID != 0 && i < users.length; i++)
		{
			if (users[i].getAD_User_ID() == AD_User_ID)
				return users[i];
		}
		return users[0];
	}	//	getContact


	/**
	 * 	Get All Locations
	 * 	@param reload if true locations will be requeried
	 *	@return locations
	 */
	public MBPartnerLocation[] getLocations (boolean reload)
	{
		if (reload || m_locations == null || m_locations.length == 0)
			;
		else
			return m_locations;
		//
		List<MBPartnerLocation> list = new ArrayList<MBPartnerLocation>();
		String where = "C_BPartner_ID=?";
		int count = new Query(Env.getCtx(), MBPartnerLocation.Table_Name, where, get_TrxName()).setParameters(getC_BPartner_ID()).count();
		if (count > 0) {
			list = new Query(Env.getCtx(), MBPartnerLocation.Table_Name, where, get_TrxName())
			.setParameters(getC_BPartner_ID())
			.list();
		} 
		m_locations = new MBPartnerLocation[list.size()];
		list.toArray(m_locations);
		return m_locations;
	}	//	getLocations

	/**
	 * 	Get explicit or first bill Location
	 * 	@param C_BPartner_Location_ID optional explicit location
	 *	@return location or null
	 */
	public MBPartnerLocation getLocation(int C_BPartner_Location_ID)
	{
		MBPartnerLocation[] locations = getLocations(false);
		if (locations.length == 0)
			return null;
		MBPartnerLocation retValue = null;
		for (int i = 0; i < locations.length; i++)
		{
			if (locations[i].getC_BPartner_Location_ID() == C_BPartner_Location_ID)
				return locations[i];
			if (retValue == null && locations[i].isBillTo())
				retValue = locations[i];
		}
		if (retValue == null)
			return locations[0];
		return retValue;
	}	//	getLocation


	/**
	 * 	Get explicit or first bill Location
	 *	@return location or null
	 */
	public MBPartnerLocation getBillLocation()
	{
		MBPartnerLocation[] locations = getLocations(false);
		if (locations.length == 0)
			return null;
		MBPartnerLocation retValue = null;
		for (int i = 0; i < locations.length; i++)
		{
			if (locations[i].isBillTo())
			{
				retValue = locations[i];
				break;
			}
		}
		return retValue;
	}	//	getLocation

	/**
	 * 	Get explicit or first ship Location
	 *	@return location or null
	 */
	public MBPartnerLocation getShipLocation()
	{
		MBPartnerLocation[] locations = getLocations(false);
		if (locations.length == 0)
			return null;
		MBPartnerLocation retValue = null;
		for (int i = 0; i < locations.length; i++)
		{
			if (locations[i].isShipTo())
			{
				retValue = locations[i];
				break;
			}
		}
		return retValue;
	}	//	getLocation

	/**
	 * 	Get Bank Accounts
	 * 	@param requery requery
	 *	@return Bank Accounts
	 */
	public MBPBankAccount[] getBankAccounts (boolean requery)
	{
		if (m_accounts != null && m_accounts.length >= 0 && !requery)	//	re-load
			return m_accounts;
		//
		ArrayList<MBPBankAccount> list = new ArrayList<MBPBankAccount>();
		String sql = "SELECT * FROM C_BP_BankAccount WHERE C_BPartner_ID=? AND IsActive='Y'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MBPBankAccount (getCtx(), rs, get_TrxName()));
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
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

		m_accounts = new MBPBankAccount[list.size()];
		list.toArray(m_accounts);
		return m_accounts;
	}	//	getBankAccounts


	/**************************************************************************
	 *	String Representation
	 * 	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MBPartner[ID=")
		.append(get_ID())
		.append(",Value=").append(getValue())
		.append(",Name=").append(getName())
		.append(",Open=").append(getTotalOpenBalance())
		.append ("]");
		return sb.toString ();
	}	//	toString

	/**
	 * 	Set Client/Org
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 */
	public void setClientOrg (int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	Set Linked Organization.
	 * 	(is Button)
	 *	@param AD_OrgBP_ID 
	 */
	public void setAD_OrgBP_ID (int AD_OrgBP_ID)
	{
		if (AD_OrgBP_ID == 0)
			super.setAD_OrgBP_ID (null);
		else
			super.setAD_OrgBP_ID (String.valueOf(AD_OrgBP_ID));
	}	//	setAD_OrgBP_ID

	/** 
	 * 	Get Linked Organization.
	 * 	(is Button)
	 * 	The Business Partner is another Organization 
	 * 	for explicit Inter-Org transactions 
	 * 	@return AD_Org_ID if BP
	 */
	public int getAD_OrgBP_ID_Int() 
	{
		String org = super.getAD_OrgBP_ID();
		if (org == null)
			return 0;
		int AD_OrgBP_ID = 0;
		try
		{
			AD_OrgBP_ID = Integer.parseInt (org);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, org, ex);
		}
		return AD_OrgBP_ID;
	}	//	getAD_OrgBP_ID_Int

	/**
	 * 	Get Primary C_BPartner_Location_ID
	 *	@return C_BPartner_Location_ID
	 */
	public int getPrimaryC_BPartner_Location_ID()
	{
		if (m_primaryC_BPartner_Location_ID == null)
		{
			MBPartnerLocation[] locs = getLocations(false);
			for (int i = 0; m_primaryC_BPartner_Location_ID == null && i < locs.length; i++)
			{
				if (locs[i].isBillTo())
				{
					setPrimaryC_BPartner_Location_ID (locs[i].getC_BPartner_Location_ID());
					break;
				}
			}
			//	get first
			if (m_primaryC_BPartner_Location_ID == null && locs.length > 0)
				setPrimaryC_BPartner_Location_ID (locs[0].getC_BPartner_Location_ID()); 
		}
		if (m_primaryC_BPartner_Location_ID == null)
			return 0;
		return m_primaryC_BPartner_Location_ID.intValue();
	}	//	getPrimaryC_BPartner_Location_ID

	/**
	 * 	Get Primary C_BPartner_Location
	 *	@return C_BPartner_Location
	 */
	public MBPartnerLocation getPrimaryC_BPartner_Location()
	{
		if (m_primaryC_BPartner_Location_ID == null)
		{
			m_primaryC_BPartner_Location_ID = getPrimaryC_BPartner_Location_ID();
		}
		if (m_primaryC_BPartner_Location_ID == null)
			return null;
		return new MBPartnerLocation(getCtx(), m_primaryC_BPartner_Location_ID, null);
	}	//	getPrimaryC_BPartner_Location

	/**
	 * 	Get Primary AD_User_ID
	 *	@return AD_User_ID
	 */
	public int getPrimaryAD_User_ID()
	{
		if (m_primaryAD_User_ID == null)
		{
			MUser[] users = getContacts(false);
			//	for (int i = 0; i < users.length; i++)
			//	{
			//	}
			if (m_primaryAD_User_ID == null && users.length > 0)
				setPrimaryAD_User_ID(users[0].getAD_User_ID());
		}
		if (m_primaryAD_User_ID == null)
			return 0;
		return m_primaryAD_User_ID.intValue();
	}	//	getPrimaryAD_User_ID

	/**
	 * 	Set Primary C_BPartner_Location_ID
	 *	@param C_BPartner_Location_ID id
	 */
	public void setPrimaryC_BPartner_Location_ID(int C_BPartner_Location_ID)
	{
		m_primaryC_BPartner_Location_ID = new Integer (C_BPartner_Location_ID);
	}	//	setPrimaryC_BPartner_Location_ID

	/**
	 * 	Set Primary AD_User_ID
	 *	@param AD_User_ID id
	 */
	public void setPrimaryAD_User_ID(int AD_User_ID)
	{
		m_primaryAD_User_ID = new Integer (AD_User_ID);
	}	//	setPrimaryAD_User_ID


	/**
	 * 	Calculate Total Open Balance and SO_CreditUsed.
	 *  (includes drafted invoices)
	 */
	public void setTotalOpenBalance ()
	{
		BigDecimal SO_CreditUsed = null;
		BigDecimal TotalOpenBalance = null;
		String sql = "SELECT "
				//	SO Credit Used
				+ "COALESCE((SELECT SUM(currencyBase(invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID),i.C_Currency_ID,i.DateOrdered, i.AD_Client_ID,i.AD_Org_ID)) FROM C_Invoice_v i "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID AND i.IsSOTrx='Y' AND i.IsPaid='N'),0)  +"
				+ "COALESCE((SELECT SUM(currencyBase(BOEOpen(b.C_BOE_ID),b.C_Currency_ID,b.DateBOE, b.AD_Client_ID,b.AD_Org_ID)) FROM C_BOE b "
				+ "WHERE b.C_BPartner_ID=bp.C_BPartner_ID AND b.IsSOTrx='Y' AND b.IsPaid='N'),0)  ,"
				//	Balance (incl. unallocated payments)
				+ "COALESCE((SELECT SUM(currencyBase(invoiceOpen(i.C_Invoice_ID,i.C_InvoicePaySchedule_ID),i.C_Currency_ID,i.DateOrdered, i.AD_Client_ID,i.AD_Org_ID)*i.MultiplierAP) FROM C_Invoice_v i "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID AND i.IsPaid='N'),0) - "
				+ "COALESCE((SELECT SUM(currencyBase(p.PayAmt,p.C_Currency_ID,p.DateTrx,p.AD_Client_ID,p.AD_Org_ID)) FROM C_Payment_v p "
				+ "WHERE p.C_BPartner_ID=bp.C_BPartner_ID AND p.IsAllocated='N'"
				+ " AND p.C_Charge_ID IS NULL AND NOT EXISTS (SELECT * FROM C_AllocationLine al WHERE p.C_Payment_ID=al.C_Payment_ID)),0) "
				+ "FROM C_BPartner bp "
				+ "WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
			{
				SO_CreditUsed = rs.getBigDecimal(1);
				TotalOpenBalance = rs.getBigDecimal(2);
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		//
		m_TotalOpenBalanceSet = true;
		if (SO_CreditUsed != null)
			super.setSO_CreditUsed (SO_CreditUsed);
		if (TotalOpenBalance != null)
			super.setTotalOpenBalance(TotalOpenBalance);
		setSOCreditStatus();
	}	//	setTotalOpenBalance

	/**
	 * 	Set Actual Life Time Value from DB
	 */
	public void setActualLifeTimeValue()
	{
		BigDecimal ActualLifeTimeValue = null;
		String sql = "SELECT "
				+ "COALESCE ((SELECT SUM(currencyBase(i.GrandTotal,i.C_Currency_ID,i.DateOrdered, i.AD_Client_ID,i.AD_Org_ID)) FROM C_Invoice_v i "
				+ "WHERE i.C_BPartner_ID=bp.C_BPartner_ID AND i.IsSOTrx='Y'),0) " 
				+ "FROM C_BPartner bp "
				+ "WHERE C_BPartner_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			pstmt.setInt (1, getC_BPartner_ID());
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				ActualLifeTimeValue = rs.getBigDecimal(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		if (ActualLifeTimeValue != null)
			super.setActualLifeTimeValue (ActualLifeTimeValue);
	}	//	setActualLifeTimeValue

	/**
	 * 	Get Total Open Balance
	 * 	@param calculate if null calculate it
	 *	@return Open Balance
	 */
	public BigDecimal getTotalOpenBalance (boolean calculate)
	{
		if (getTotalOpenBalance().signum() == 0 && calculate)
			setTotalOpenBalance();
		return super.getTotalOpenBalance ();
	}	//	getTotalOpenBalance


	/**
	 * 	Set Credit Status
	 */
	public void setSOCreditStatus ()
	{
		BigDecimal creditLimit = getSO_CreditLimit(); 
		//	Nothing to do
		if (REF_C_BPartnerSOCreditStatus.NoCreditCheck.equals(getSOCreditStatus())
				|| REF_C_BPartnerSOCreditStatus.CreditStop.equals(getSOCreditStatus())
				|| Env.ZERO.compareTo(creditLimit) == 0)
			return;

		//	Above Credit Limit
		if (creditLimit.compareTo(getTotalOpenBalance(!m_TotalOpenBalanceSet)) < 0)
			setSOCreditStatus(REF_C_BPartnerSOCreditStatus.CreditHold);
		else
		{
			//	Above Watch Limit
			BigDecimal watchAmt = creditLimit.multiply(getCreditWatchRatio());
			if (watchAmt.compareTo(getTotalOpenBalance()) < 0)
				setSOCreditStatus(REF_C_BPartnerSOCreditStatus.CreditWatch);
			else	//	is OK
				setSOCreditStatus (REF_C_BPartnerSOCreditStatus.CreditOK);
		}
	}	//	setSOCreditStatus


	/**
	 * 	Get SO CreditStatus with additional amount
	 * 	@param additionalAmt additional amount in Accounting Currency
	 *	@return sinulated credit status
	 */
	public String getSOCreditStatus (BigDecimal additionalAmt)
	{
		if (additionalAmt == null || additionalAmt.signum() == 0)
			return getSOCreditStatus();
		//
		BigDecimal creditLimit = getSO_CreditLimit(); 
		//	Nothing to do
		if (REF_C_BPartnerSOCreditStatus.NoCreditCheck.equals(getSOCreditStatus())
				|| REF_C_BPartnerSOCreditStatus.CreditStop.equals(getSOCreditStatus())
				|| Env.ZERO.compareTo(creditLimit) == 0)
			return getSOCreditStatus();

		//	Above (reduced) Credit Limit
		creditLimit = creditLimit.subtract(additionalAmt);
		if (creditLimit.compareTo(getTotalOpenBalance(!m_TotalOpenBalanceSet)) < 0)
			return REF_C_BPartnerSOCreditStatus.CreditHold;

		//	Above Watch Limit
		BigDecimal watchAmt = creditLimit.multiply(getCreditWatchRatio());
		if (watchAmt.compareTo(getTotalOpenBalance()) < 0)
			return REF_C_BPartnerSOCreditStatus.CreditWatch;

		//	is OK
		return REF_C_BPartnerSOCreditStatus.CreditOK;
	}	//	getSOCreditStatus

	/**
	 * 	Get Credit Watch Ratio
	 *	@return BP Group ratio or 0.9
	 */
	public BigDecimal getCreditWatchRatio()
	{
		return getBPGroup().getCreditWatchRatio();
	}	//	getCreditWatchRatio

	/**
	 * 	Credit Status is Stop or Hold.
	 *	@return true if Stop/Hold
	 */
	public boolean isCreditStopHold()
	{
		String status = getSOCreditStatus();
		return REF_C_BPartnerSOCreditStatus.CreditStop.equals(status)
				|| REF_C_BPartnerSOCreditStatus.CreditHold.equals(status);
	}	//	isCreditStopHold

	
	/**
	 * 	Set Total Open Balance
	 *	@param TotalOpenBalance
	 */
	public void setTotalOpenBalance (BigDecimal TotalOpenBalance)
	{
		m_TotalOpenBalanceSet = false;
		super.setTotalOpenBalance (TotalOpenBalance);
	}	//	setTotalOpenBalance

	/**
	 * 	Get BP Group
	 *	@return group
	 */
	public MBPGroup getBPGroup()
	{
		if (m_group == null)
		{
			if (getC_BP_Group_ID() == 0)
				m_group = MBPGroup.getDefault(getCtx());
			else
				m_group = MBPGroup.get(getCtx(), getC_BP_Group_ID());
		}
		return m_group;
	}	//	getBPGroup

	/**
	 * 	Get BP Group
	 *	@param group group
	 */
	public void setBPGroup(MBPGroup group)
	{
		m_group = group;
		if (m_group == null)
			return;
		setC_BP_Group_ID(m_group.getC_BP_Group_ID());
		if (m_group.getC_Dunning_ID() != 0)
			setC_Dunning_ID(m_group.getC_Dunning_ID());
		if (m_group.getM_PriceList_ID() != 0)
			setM_PriceList_ID(m_group.getM_PriceList_ID());
		if (m_group.getPO_PriceList_ID() != 0)
			setPO_PriceList_ID(m_group.getPO_PriceList_ID());
		if (m_group.getM_DiscountSchema_ID() != 0)
			setM_DiscountSchema_ID(m_group.getM_DiscountSchema_ID());
		if (m_group.getPO_DiscountSchema_ID() != 0)
			setPO_DiscountSchema_ID(m_group.getPO_DiscountSchema_ID());
	}	//	setBPGroup

	/**
	 * 	Get PriceList
	 *	@return price List
	 */
	public int getM_PriceList_ID ()
	{
		int ii = super.getM_PriceList_ID();
		if (ii == 0)
			ii = getBPGroup().getM_PriceList_ID();
		return ii;
	}	//	getM_PriceList_ID

	/**
	 * 	Get PO PriceList
	 *	@return price list
	 */
	public int getPO_PriceList_ID ()
	{
		int ii = super.getPO_PriceList_ID();
		if (ii == 0)
			ii = getBPGroup().getPO_PriceList_ID();
		return ii;
	}	//

	/**
	 * 	Get DiscountSchema
	 *	@return Discount Schema
	 */
	public int getM_DiscountSchema_ID ()
	{
		int ii = super.getM_DiscountSchema_ID ();
		if (ii == 0)
			ii = getBPGroup().getM_DiscountSchema_ID();
		return ii;
	}	//	getM_DiscountSchema_ID

	/**
	 * 	Get PO DiscountSchema
	 *	@return po discount
	 */
	public int getPO_DiscountSchema_ID ()
	{
		int ii = super.getPO_DiscountSchema_ID ();
		if (ii == 0)
			ii = getBPGroup().getPO_DiscountSchema_ID();
		return ii;
	}	//	getPO_DiscountSchema_ID

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord || is_ValueChanged("C_BP_Group_ID"))
		{
			MBPGroup grp = getBPGroup();
			if (grp == null)
			{
				log.saveWarning("Error", Msg.parseTranslation(getCtx(), "@NotFound@:  @C_BP_Group_ID@"));
				return false;
			}
			setBPGroup(grp);	//	setDefaults
		}
		if (newRecord || is_ValueChanged("TaxID"))
		{			
			X_C_BP_DocType dt = new Query(Env.getCtx(), X_C_BP_DocType.Table_Name, "C_BP_DocType_ID = ?", null)
			.setParameters(getC_BP_DocType_ID()).first();
			if (dt != null)
			{
				if (dt.getName().equals("RUC") && !Sunat.isValidRUC(getTaxID()))
				{
					log.saveWarning("Error", Msg.parseTranslation(getCtx(), "@NotFound@:  @InvalidRUC@"));
					return false;
				}
			}
		}
		return true;
	}	//	beforeSave

	/**************************************************************************
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord && success)
		{
			//	Trees
			insert_Tree(REF_AD_TreeTypeType.BPartner);
			//	Accounting
			insert_Accounting("C_BP_Customer_Acct", "C_BP_Group_Acct", "p.C_BP_Group_ID=" + getC_BP_Group_ID());
			insert_Accounting("C_BP_Vendor_Acct", "C_BP_Group_Acct", "p.C_BP_Group_ID=" + getC_BP_Group_ID());
			insert_Accounting("C_BP_Employee_Acct", "C_AcctSchema_Default", null);
		}

		//	Value/Name change
		if (success && !newRecord 
				&& (is_ValueChanged("Value") || is_ValueChanged("Name")))
			MAccount.updateValueDescription(getCtx(), "C_BPartner_ID=" + getC_BPartner_ID(), get_TrxName());

		return success;
	}	//	afterSave

	/**
	 * 	Before Delete
	 *	@return true
	 */
	protected boolean beforeDelete ()
	{
		return delete_Accounting("C_BP_Customer_Acct") 
				&& delete_Accounting("C_BP_Vendor_Acct")
				&& delete_Accounting("C_BP_Employee_Acct");
	}	//	beforeDelete

	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	protected boolean afterDelete (boolean success)
	{
		if (success)
			delete_Tree(REF_AD_TreeTypeType.BPartner);
		return success;
	}	//	afterDelete

	public MWithholding[] getWithholdings(boolean IsSOTrx) {
		if (m_Withholdings != null && IsSOTrx)
			return m_Withholdings;		
		else if (m_POWithholdings != null && !IsSOTrx)	//	re-load
			return m_POWithholdings;			

		List<MWithholding> holdings  = new ArrayList<MWithholding>();
		String query = "C_BPartner_ID = ? AND IsActive='Y' AND IsTemporaryExempt='N' AND IsSOTrx='Y'";
		List<X_C_BP_Withholding> list = new Query(Env.getCtx(), X_C_BP_Withholding.Table_Name, query , null)
		.setParameters(getC_BPartner_ID()).list();
		if (list.size() > 0)
		{
			for (X_C_BP_Withholding bpwh:list)
			{
				MWithholding wh = new MWithholding(Env.getCtx(), bpwh.getC_Withholding_ID(), null);
				holdings.add(wh);
			}
		}
		m_Withholdings = new MWithholding[holdings.size()];
		holdings.toArray(m_Withholdings);

		List<MWithholding> POholdings  = new ArrayList<MWithholding>();
		query = "C_BPartner_ID = ? AND IsActive='Y' AND IsTemporaryExempt='N' AND IsSOTrx='N'";
		List<X_C_BP_Withholding> POlist = new Query(Env.getCtx(), X_C_BP_Withholding.Table_Name, query , null)
		.setParameters(getC_BPartner_ID()).list();
		if (POlist.size() > 0)
		{
			for (X_C_BP_Withholding bpwh:POlist)
			{
				MWithholding wh = new MWithholding(Env.getCtx(), bpwh.getC_Withholding_ID(), null);
				POholdings.add(wh);
			}
		}
		m_POWithholdings = new MWithholding[POholdings.size()];
		POholdings.toArray(m_POWithholdings);

		if (IsSOTrx)
			return m_Withholdings;
		else 
			return m_POWithholdings;
	}

	public BigDecimal getPaidTo3Party(boolean IsSOTrx)
	{
		BigDecimal percent = BigDecimal.ZERO;
		MWithholding[] withholdings = getWithholdings(IsSOTrx);
		for (MWithholding holding:withholdings)
		{
			if (holding.isPaidTo3Party())
			{
				if (percent.equals(BigDecimal.ZERO))
					percent = holding.getPercent();
				else if (holding.getPercent().compareTo(percent) < 0)
					percent = holding.getPercent();
			}
		}
		return percent;
	}
	public boolean IsTaxWithholding(boolean IsSOTrx)
	{
		Boolean IsTaxWithholding = false;
		MWithholding[] withholdings = getWithholdings(IsSOTrx);
		for (MWithholding holding:withholdings)
		{
			if (holding.isTaxWithholding())
			{
				IsTaxWithholding = true;
				break;
			}
		}				
		return IsTaxWithholding;
	}

	public BigDecimal getCreditAvailable() {	
		BigDecimal creditavailable = getSO_CreditLimit().add(getSO_CreditOverLimit())
				.subtract(getSO_CreditUsed());
		if (creditavailable == null)
			creditavailable = BigDecimal.ZERO;
		return creditavailable;		
	}
}	//	MBPartner
