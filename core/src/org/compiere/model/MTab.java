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

import org.compiere.model.persistence.X_AD_Tab;
import org.compiere.util.*;

/**
 *	Tab Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MTab.java 4204 2012-06-30 01:06:30Z xapiens $
 */
public class MTab extends X_AD_Tab
{

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Tab_ID id
	 *	@param trxName transaction
	 */
	public MTab (Properties ctx, int AD_Tab_ID, String trxName)
	{
		super (ctx, AD_Tab_ID, trxName);
		if (AD_Tab_ID == 0)
		{
		//	setAD_Window_ID (0);
		//	setAD_Table_ID (0);
		//	setName (null);
			setEntityType (ENTITYTYPE_UserMaintained);	// U
			setHasTree (false);
			setIsReadOnly (false);
			setIsSingleRow (false);
			setIsSortTab (false);	// N
			setIsTranslationTab (false);
			setSeqNo (0);
			setTabLevel (0);
			setIsInsertRecord(true);
			setIsAdvancedTab(false);
		}
	}	//	M_Tab

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MTab (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	M_Tab

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 */
	public MTab (MWindow parent)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setAD_Window_ID(parent.getAD_Window_ID());
		setEntityType(parent.getEntityType());
	}	//	M_Tab

	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param from copy from
	 */
	public MTab (MWindow parent, MTab from)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		copyValues(from, this);
		setClientOrg(parent);
		setAD_Window_ID(parent.getAD_Window_ID());
		setEntityType(parent.getEntityType());
	}	//	M_Tab
	
	
	/**	The Fields						*/
	private MField[]		m_fields	= null;

	/**
	 * 	Get Fields
	 *	@param reload reload data
	 *	@return array of lines
	 *	@param trxName transaction
	 */
	public MField[] getFields (boolean reload, String trxName)
	{
		if (m_fields != null && !reload)
			return m_fields;
		String sql = "SELECT * FROM AD_Field WHERE AD_Tab_ID=? ORDER BY SeqNo";
		ArrayList<MField> list = new ArrayList<MField>();
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, getAD_Tab_ID());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MField (getCtx(), rs, trxName));
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
		m_fields = new MField[list.size ()];
		list.toArray (m_fields);
		return m_fields;
	}	//	getFields

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	protected boolean beforeSave (boolean newRecord)
	{
	//	UPDATE AD_Tab SET IsInsertRecord='N' WHERE IsInsertRecord='Y' AND IsReadOnly='Y'
		if (isReadOnly() && isInsertRecord())
			setIsInsertRecord(false);
		return true;
	}

	public static MTab getbyIdentifier(String identifier) {
		MTab retValue = null;
		String whereClause = "Identifier = ? "; 
		try {
			retValue = new Query(Env.getCtx(), X_AD_Tab.Table_Name, whereClause, null).setParameters(identifier).first();				
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("X22");
		}
		return retValue;
	}

	public static MTab getbyName(MWindow window, String tabname) {
		MTab retValue = null;
		for (MTab tab : window.getTabs(true, null))
		{
			if (tab.getName().equals(tabname))
			{
				retValue = tab;
				break;
			}
		}
		return retValue;
	}


}	//	M_Tab
