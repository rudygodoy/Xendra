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

import org.compiere.model.persistence.X_C_UOM;
import org.compiere.util.*;

/**
 *	Unit Of Measure Model
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MUOM.java 3654 2011-11-04 01:49:49Z xapiens $
 */
public class MUOM extends X_C_UOM
{
	/** X12 Element 355 Code	Minute	*/
	static final String		X12_MINUTE = "MJ";
	/** X12 Element 355 Code	Hour	*/
	static final String		X12_HOUR = "HR";
	/** X12 Element 355 Code	Day 	*/
	static final String		X12_DAY = "DA";
	/** X12 Element 355 Code	Work Day (8 hours / 5days)	 	*/
	static final String		X12_DAY_WORK = "WD";
	/** X12 Element 355 Code	Week 	*/
	static final String		X12_WEEK = "WK";
	/** X12 Element 355 Code	Month 	*/
	static final String		X12_MONTH = "MO";
	/** X12 Element 355 Code	Work Month (20 days / 4 weeks) 	*/
	static final String		X12_MONTH_WORK = "WM";
	/** X12 Element 355 Code	Year 	*/
	static final String		X12_YEAR = "YR";

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MUOM.class);

	/**
	 * 	Get Minute C_UOM_ID
	 *  @param ctx context
	 * 	@return C_UOM_ID for Minute
	 */
	public static int getMinute_UOM_ID (Properties ctx)
	{
		if (Ini.isClient())
		{
			Iterator it = s_cache.values().iterator();
			while (it.hasNext())
			{
				MUOM uom = (MUOM)it.next();
				if (uom.isMinute())
					return uom.getC_UOM_ID();
			}
		}
		//	Server
		int C_UOM_ID = 0;
		String sql = "SELECT C_UOM_ID FROM C_UOM "
			+ "WHERE IsActive='Y' AND X12DE355='MJ'";	//	HardCoded
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				C_UOM_ID = rs.getInt(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		return C_UOM_ID;
	}	//	getMinute_UOM_ID

	/**
	 * 	Get Default C_UOM_ID
	 *	@param ctx context for AD_Client
	 *	@return C_UOM_ID
	 */
	public static int getDefault_UOM_ID (Properties ctx)
	{
		String sql = "SELECT C_UOM_ID "
			+ "FROM C_UOM "
			+ "WHERE AD_Client_ID IN (0,?) "
			+ "ORDER BY IsDefault DESC, AD_Client_ID DESC, C_UOM_ID";
		return DB.getSQLValue(null, sql, Env.getAD_Client_ID(ctx));
	}	//	getDefault_UOM_ID

	/*************************************************************************/

	/**	UOM Cache				*/
	private static CCache<Integer,MUOM>	s_cache 
		= new CCache<Integer,MUOM>("C_UOM", 30);

	/**
	 * 	Get UOM from Cache
	 * 	@param ctx context
	 *	@param C_UOM_ID ID
	 * 	@return UOM
	 */
	public static MUOM get (Properties ctx, int C_UOM_ID)
	{
		if (s_cache.size() == 0)
			loadUOMs (ctx);
		//
		Integer ii = new Integer (C_UOM_ID);
		MUOM uom = (MUOM)s_cache.get(ii);
		if (uom != null)
			return uom;
		//
		uom = new MUOM (ctx, C_UOM_ID, null);
		s_cache.put(new Integer(C_UOM_ID), uom);
		return uom;
	}	//	getUOMfromCache

	public static MUOM get (Properties ctx, String Symbol) {
		MUOM uom = new Query(Env.getCtx(), MUOM.Table_Name, "uomsymbol = ?", null)
			.setParameters(Symbol).first();
		return uom;
	}
	/**
	 * 	Get Precision
	 * 	@param ctx context
	 *	@param C_UOM_ID ID
	 * 	@return Precision
	 */
	public static int getPrecision (Properties ctx, int C_UOM_ID)
	{
		MUOM uom = get(ctx, C_UOM_ID);
		return uom.getStdPrecision();
	}	//	getPrecision

	/**
	 * 	Load All UOMs
	 * 	@param ctx context
	 */
	private static void loadUOMs (Properties ctx)
	{
		String sql = MRole.getDefault(ctx, false).addAccessSQL(
			"SELECT * FROM C_UOM "
			+ "WHERE IsActive='Y'",
			"C_UOM", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MUOM uom = new MUOM(ctx, rs, null);
				s_cache.put (new Integer(uom.getC_UOM_ID()), uom);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
	}	//	loadUOMs
	
	
	/**************************************************************************
	 *	Constructor.
	 *	@param ctx context
	 *  @param C_UOM_ID UOM ID
	 *  @param trxName transaction
	 */
	public MUOM (Properties ctx, int C_UOM_ID, String trxName)
	{
		super (ctx, C_UOM_ID, trxName);
		if (C_UOM_ID == 0)
		{
		//	setName (null);
		//	setX12DE355 (null);
			setIsDefault (false);
			setStdPrecision (2);
			setCostingPrecision (6);
		}
	}	//	UOM

	/**
	 *	Load Constructor.
	 *	@param ctx context
	 *  @param rs result set
	 *  @param trxName transaction
	 */
	public MUOM (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	UOM

	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("UOM[");
		sb.append("ID=").append(get_ID())
			.append(", Name=").append(getName());
		return sb.toString();
	}	//	toString

	/**
	 * 	Round qty
	 *	@param qty quantity
	 *	@param stdPrecision true if std precisison
	 *	@return rounded quantity
	 */
	public BigDecimal round (BigDecimal qty, boolean stdPrecision)
	{
		int precision = getStdPrecision();
		if (!stdPrecision)
			precision = getCostingPrecision();
		if (qty.scale() > precision)
			return qty.setScale(getStdPrecision(), BigDecimal.ROUND_HALF_UP);
		return qty;
	}	//	round

	/**
	 * 	Minute
	 *	@return true if UOM is minute
	 */
	public boolean isMinute()
	{
		return X12_MINUTE.equals(getX12DE355());
	}
	/**
	 * 	Hour
	 *	@return true if UOM is hour
	 */
	public boolean isHour()
	{
		return X12_HOUR.equals(getX12DE355());
	}
	/**
	 * 	Day
	 *	@return true if UOM is Day
	 */
	public boolean isDay()
	{
		return X12_DAY.equals(getX12DE355());
	}
	/**
	 * 	WorkDay
	 *	@return true if UOM is work day
	 */
	public boolean isWorkDay()
	{
		return X12_DAY_WORK.equals(getX12DE355());
	}
	/**
	 * 	Week
	 *	@return true if UOM is Week
	 */
	public boolean isWeek()
	{
		return X12_WEEK.equals(getX12DE355());
	}
	/**
	 * 	Month
	 *	@return true if UOM is Month
	 */
	public boolean isMonth()
	{
		return X12_MONTH.equals(getX12DE355());
	}
	/**
	 * 	WorkMonth
	 *	@return true if UOM is Work Month
	 */
	public boolean isWorkMonth()
	{
		return X12_MONTH_WORK.equals(getX12DE355());
	}
	/**
	 * 	Year
	 *	@return true if UOM is year
	 */
	public boolean isYear()
	{
		return X12_YEAR.equals(getX12DE355());
	}

	//cchaucca:ini
	/**
	 * 	Load All UOMs for product category
	 * 	@param ctx context
	 */
	static public MUOM[] getUOMofProductCategory (Properties ctx,int M_Product_Category_ID)
	{
		ArrayList<MUOM> list = new ArrayList<MUOM>();
		try
		{
			String sql = "select * from c_uom where C_UOM_ID IN "+ 
			"(SELECT U.C_UOM_ID FROM M_PRODUCT_CATEGORY_UOM U WHERE  "+ 
			"U.M_PRODUCT_CATEGORY_ID=? AND U.IsActive='Y')";
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setInt(1,M_Product_Category_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MUOM(ctx, rs, null));
			rs.close();
			pstmt.close();
			pstmt=null;
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "UOM.loadUOMs", e);
		}
		MUOM result[] = new MUOM[list.size ()];
		list.toArray (result);
		return result;
	}	//	loadUOMs
	//cchaucca:fin

}	//	MUOM
