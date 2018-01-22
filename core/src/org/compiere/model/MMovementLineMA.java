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

import org.compiere.model.persistence.X_M_MovementLineMA;
import org.compiere.util.*;


/**
 *	Movement Material Allocation
 *	
 *  @author Jorg Janke
 *  @version $Id: MMovementLineMA.java 3654 2011-11-04 01:49:49Z xapiens $
 */
public class MMovementLineMA extends X_M_MovementLineMA
{
	/**
	 * 	Get Material Allocations for Line
	 *	@param ctx context
	 *	@param M_MovementLine_ID line
	 *	@param trxName trx
	 *	@return allocations
	 */
	public static MMovementLineMA[] get (Properties ctx, int M_MovementLine_ID, String trxName)
	{
		ArrayList<MMovementLineMA> list = new ArrayList<MMovementLineMA>();
		String sql = "SELECT * FROM M_MovementLineMA WHERE M_MovementLine_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trxName);
			pstmt.setInt (1, M_MovementLine_ID);
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MMovementLineMA (ctx, rs, trxName));
			}
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
		
		MMovementLineMA[] retValue = new MMovementLineMA[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	get
	
	/**
	 * 	Delete all Material Allocation for Movement
	 *	@param M_Movement_ID movement
	 *	@param trxName transaction
	 *	@return number of rows deleted or -1 for error
	 */
	public static int deleteMovementMA (int M_Movement_ID, String trxName)
	{
		String sql = "DELETE FROM M_MovementLineMA ma WHERE EXISTS "
			+ "(SELECT * FROM M_MovementLine l WHERE l.M_MovementLine_ID=ma.M_MovementLine_ID"
			+ " AND M_Movement_ID=" + M_Movement_ID + ")";
		return DB.executeUpdate(sql, trxName);
	}	//	deleteInOutMA
	
	/**	Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MMovementLineMA.class);

	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_MovementLineMA_ID ignored
	 *	@param trxName trx
	 */
	public MMovementLineMA (Properties ctx, int M_MovementLineMA_ID,
		String trxName)
	{
		super (ctx, M_MovementLineMA_ID, trxName);
		if (M_MovementLineMA_ID != 0)
			throw new IllegalArgumentException("Multi-Key");
	}	//	MMovementLineMA

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result ser
	 *	@param trxName trx
	 */
	public MMovementLineMA (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MMovementLineMA
	
	/**
	 * 	Parent Constructor
	 *	@param parent parent
	 *	@param M_AttributeSetInstance_ID asi
	 *	@param MovementQty qty
	 */
	public MMovementLineMA (MMovementLine parent, int M_AttributeSetInstance_ID, BigDecimal MovementQty)
	{
		this (parent.getCtx(), 0, parent.get_TrxName());
		setClientOrg(parent);
		setM_MovementLine_ID(parent.getM_MovementLine_ID());
		//
		setM_AttributeSetInstance_ID(M_AttributeSetInstance_ID);
		setMovementQty(MovementQty);
	}	//	MMovementLineMA
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MMovementLineMA[");
		sb.append("M_MovementLine_ID=").append(getM_MovementLine_ID())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append(", Qty=").append(getMovementQty())
			.append ("]");
		return sb.toString ();
	}	//	toString

}	//	MMovementLineMA
