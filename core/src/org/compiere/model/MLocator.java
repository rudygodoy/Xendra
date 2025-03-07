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

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.model.persistence.X_M_Locator;
import org.compiere.util.*;

/**
 *	Warehouse Locator Object
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MLocator.java 3654 2011-11-04 01:49:49Z xapiens $
 */
public class MLocator extends X_M_Locator
{
	/**
	 * 	Get oldest Default Locator of warehouse with locator
	 *	@param ctx context
	 *	@param M_Locator_ID locator
	 *	@return locator or null
	 */
	public static MLocator getDefault (Properties ctx, int M_Locator_ID)
	{
		String trxName = null;
		MLocator retValue = null;
		String sql = "SELECT * FROM M_Locator l "
			+ "WHERE IsDefault='Y'"
			+ " AND EXISTS (SELECT * FROM M_Locator lx "
				+ "WHERE l.M_Warehouse_ID=lx.M_Warehouse_ID AND lx.M_Locator_ID=?) "
			+ "ORDER BY Created";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Locator_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				retValue = new MLocator (ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
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
	}	//	getDefault
	
	
	//cchaucca:ini
	/**
	 * 	Get oldest Default Locator of warehouse with locator
	 *	@param ctx context
	 *	@param M_Locator_ID locator
	 *	@return locator or null
	 */
	public static MLocator getDefaultLocator(Properties ctx, int M_Warehouse_ID)
	{
		String trxName = null;
		MLocator retValue = null;
		String sql = "SELECT * FROM M_Locator l "
			+ "WHERE IsDefault='Y'"
			+ " AND m_warehouse_id=? "
			+ "ORDER BY Created";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_Warehouse_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MLocator (ctx, rs, trxName);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
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
	}	//	getDefault
	//cchaucca:fin

	/**
	 * 	Get the Locator with the combination or create new one
	 *	@param ctx Context
	 *	@param M_Warehouse_ID warehouse
	 *	@param Value value
	 *	@param X x
	 *	@param Y y
	 *	@param Z z
	 * 	@return locator
	 */
	 public static MLocator get (Properties ctx, int M_Warehouse_ID, String Value,
		 String X, String Y, String Z)
	 {
		MLocator retValue = null;
		String sql = "SELECT * FROM M_Locator WHERE M_Warehouse_ID=? AND X=? AND Y=? AND Z=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, M_Warehouse_ID);
			pstmt.setString(2, X);
			pstmt.setString(3, Y);
			pstmt.setString(4, Z);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				retValue = new MLocator (ctx, rs, null);
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (SQLException ex)
		{
			s_log.log(Level.SEVERE, "get", ex);
		}
		try
		{
			if (pstmt != null)
				pstmt.close();
		}
		catch (SQLException ex1)
		{
		}
		pstmt = null;
		//
		if (retValue == null)
		{
			MWarehouse wh = MWarehouse.get (ctx, M_Warehouse_ID);
			retValue = new MLocator (wh, Value);
			retValue.setXYZ(X, Y, Z);
			retValue.save();
		}
		return retValue;
	 }	//	get

	/**
	 * 	Get Locator from Cache
	 *	@param ctx context
	 *	@param M_Locator_ID id
	 *	@return MLocator
	 */
	public static MLocator get (Properties ctx, int M_Locator_ID)
	{
		if (s_cache == null)
			s_cache	= new CCache<Integer,MLocator>("M_Locator", 20);
		Integer key = new Integer (M_Locator_ID);
		MLocator retValue = (MLocator) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MLocator (ctx, M_Locator_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer,MLocator> s_cache; 
	 
	/**	Logger						*/
	private static CLogger		s_log = CLogger.getCLogger (MLocator.class);
	
	
	/**************************************************************************
	 * 	Standard Locator Constructor
	 *	@param ctx Context
	 *	@param M_Locator_ID id
	 *	@param trxName transaction
	 */
	public MLocator (Properties ctx, int M_Locator_ID, String trxName)
	{
		super (ctx, M_Locator_ID, trxName);
		if (M_Locator_ID == 0)
		{
		//	setM_Locator_ID (0);		//	PK
		//	setM_Warehouse_ID (0);		//	Parent
			setIsDefault (false);
			setPriorityNo (50);
		//	setValue (null);
		//	setX (null);
		//	setY (null);
		//	setZ (null);
		}
	}	//	MLocator

	/**
	 * 	New Locator Constructor with XYZ=000
	 *	@param warehouse parent
	 *	@param Value value
	 */
	public MLocator (MWarehouse warehouse, String Value)
	{
		this (warehouse.getCtx(), 0, warehouse.get_TrxName());
		setClientOrg(warehouse);
		setM_Warehouse_ID (warehouse.getM_Warehouse_ID());		//	Parent
		setValue (Value);
		setXYZ("0","0","0");
	}	//	MLocator

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MLocator (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MLocator

	/**
	 *	Get String Representation
	 * 	@return Value
	 */
	public String toString()
	{
		return getValue();
	}	//	getValue

	/**
	 * 	Set Location
	 *	@param X x
	 *	@param Y y
	 *	@param Z z
	 */
	public void setXYZ (String X, String Y, String Z)
	{
		setX (X);
		setY (Y);
		setZ (Z);
	}	//	setXYZ
	
	
	/**
	 * 	Get Warehouse Name
	 * 	@return name
	 */
	public String getWarehouseName()
	{
		MWarehouse wh = MWarehouse.get(getCtx(), getM_Warehouse_ID());
		if (wh.get_ID() == 0)
			return "<" + getM_Warehouse_ID() + ">";
		return wh.getName();
	}	//	getWarehouseName

	/**
	 * 	Can Locator Store Product
	 *	@param M_Product_ID id
	 *	@return true if can be stored
	 */
	public boolean isCanStoreProduct (int M_Product_ID)
	{
		//	Default Locator
		if (M_Product_ID == 0 || isDefault())
			return true;
		
		int count = 0;
		PreparedStatement pstmt = null;
		//	Already Stored
		String sql = "SELECT COUNT(*) FROM M_Storage s WHERE s.M_Locator_ID=? AND s.M_Product_ID=?";
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			pstmt.setInt (1, getM_Locator_ID());
			pstmt.setInt (2, M_Product_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				count = rs.getInt(1);
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
		}
		//	Default Product Locator
		if (count == 0)
		{
			sql = "SELECT COUNT(*) FROM M_Product s WHERE s.M_Locator_ID=? AND s.M_Product_ID=?";
			try
			{
				pstmt = DB.prepareStatement (sql, null);
				pstmt.setInt (1, getM_Locator_ID());
				pstmt.setInt (2, M_Product_ID);
				ResultSet rs = pstmt.executeQuery ();
				if (rs.next ())
					count = rs.getInt(1);
				rs.close ();
				pstmt.close ();
				pstmt = null;
			}
			catch (Exception e)
			{
				log.log (Level.SEVERE, sql, e);
			}
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
		
		return count != 0;
	}	//	isCanStoreProduct


	public BigDecimal getAvailable() {		
		BigDecimal Used = DB.getSQLValueBD(null, "SELECT sum(qtyonhand) from m_storage  where m_locator_id = ? AND isactive = 'Y'", this.getM_Locator_ID());
		if (Used == null)
			Used = BigDecimal.ZERO;
		BigDecimal Available = getVolume().subtract(Used);
		if (Available.compareTo(BigDecimal.ZERO) < 0) {
			Available = BigDecimal.ZERO;
		}
		return Available;
	}	
}	//	MLocator
