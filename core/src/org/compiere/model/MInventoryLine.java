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

import org.compiere.model.persistence.X_M_InventoryLine;
import org.compiere.model.reference.REF_M_InventoryType;
import org.compiere.util.*;

/**
 *  Physical Inventory Line Model
 *
 *  @author Jorg Janke
 *  @version $Id: MInventoryLine.java 5583 2015-08-05 14:11:58Z xapiens $
 */
public class MInventoryLine extends X_M_InventoryLine 
{
	/**
	 * 	Get Inventory Line with parameters
	 *	@param inventory inventory
	 *	@param M_Locator_ID locator
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID asi
	 *	@return line or null
	 */
	public static MInventoryLine get (MInventory inventory, 
		int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID)
	{
		MInventoryLine retValue = null;
		String sql = "SELECT * FROM M_InventoryLine "
			+ "WHERE M_Inventory_ID=? AND M_Locator_ID=?"
			+ " AND M_Product_ID=? AND M_AttributeSetInstance_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, inventory.get_TrxName());
			pstmt.setInt (1, inventory.getM_Inventory_ID());
			pstmt.setInt(2, M_Locator_ID);
			pstmt.setInt(3, M_Product_ID);
			pstmt.setInt(4, M_AttributeSetInstance_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MInventoryLine (inventory.getCtx(), rs, inventory.get_TrxName());
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
	}	//	get
	
	
	/**	Logger				*/
	private static CLogger	s_log	= CLogger.getCLogger (MInventoryLine.class);
	
	
	/**************************************************************************
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param M_InventoryLine_ID line
	 *	@param trxName transaction
	 */
	public MInventoryLine (Properties ctx, int M_InventoryLine_ID, String trxName)
	{
		super (ctx, M_InventoryLine_ID, trxName);
		if (M_InventoryLine_ID == 0)
		{
		//	setM_Inventory_ID (0);			//	Parent
		//	setM_InventoryLine_ID (0);		//	PK
		//	setM_Locator_ID (0);			//	FK
			setLine(0);
		//	setM_Product_ID (0);			//	FK
			setM_AttributeSetInstance_ID(0);	//	FK
			setInventoryType (REF_M_InventoryType.InventoryDifference);
			setQtyBook (Env.ZERO);
			setQtyCount (Env.ZERO);
			setProcessed(false);
		}
	}	//	MInventoryLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MInventoryLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MInventoryLine

	/**
	 * 	Detail Constructor.
	 * 	Locator/Product/AttributeSetInstance must be unique
	 *	@param inventory parent
	 *	@param M_Locator_ID locator
	 *	@param M_Product_ID product
	 *	@param M_AttributeSetInstance_ID instance
	 *	@param QtyBook book value
	 *	@param QtyCount count value
	 */
	public MInventoryLine (MInventory inventory, 
		int M_Locator_ID, int M_Product_ID, int M_AttributeSetInstance_ID,
		BigDecimal QtyBook, BigDecimal QtyCount)
	{
		this (inventory.getCtx(), 0, inventory.get_TrxName());
		if (inventory.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		m_parent = inventory;
		setM_Inventory_ID (inventory.getM_Inventory_ID());		//	Parent
		setClientOrg (inventory.getAD_Client_ID(), inventory.getAD_Org_ID());
		setM_Locator_ID (M_Locator_ID);		//	FK
		setM_Product_ID (M_Product_ID);		//	FK
		setM_AttributeSetInstance_ID (M_AttributeSetInstance_ID);
		//
		if (QtyBook != null)
			setQtyBook (QtyBook);
		if (QtyCount != null && QtyCount.signum() != 0)
			setQtyCount (QtyCount);
		m_isManualEntry = false;
	}	//	MInventoryLine

	/** Manually created				*/
	private boolean 	m_isManualEntry = true;
	/** Parent							*/
	private MInventory 	m_parent = null;
	/** Product							*/
	private MProduct 	m_product = null;
	
	/**
	 * 	Get Qty Book
	 *	@return Qty Book
	 */
	public BigDecimal getQtyBook ()
	{
		BigDecimal bd = super.getQtyBook ();
		if (bd == null)
			bd = Env.ZERO;
		return bd;
	}	//	getQtyBook

	/**
	 * 	Get Qty Count
	 *	@return Qty Count
	 */
	public BigDecimal getQtyCount ()
	{
		BigDecimal bd = super.getQtyCount();
		if (bd == null)
			bd = Env.ZERO;
		return bd;
	}	//	getQtyBook

	/**
	 * 	Get Product
	 *	@return product or null if not defined
	 */
	public MProduct getProduct()
	{
		int M_Product_ID = getM_Product_ID();
		if (M_Product_ID == 0)
			return null;
		if (m_product != null && m_product.getM_Product_ID() != M_Product_ID)
			m_product = null;	//	reset
		if (m_product == null)
			m_product = MProduct.get(getCtx(), M_Product_ID, get_TrxName());
		return m_product;
	}	//	getProduct
	
	/**
	 * 	Set Count Qty - enforce UOM 
	 *	@param QtyCount qty
	 */
	public void setQtyCount (BigDecimal QtyCount)
	{
		if (QtyCount != null)
		{
			MProduct product = getProduct();
			if (product != null)
			{
				int precision = product.getUOMPrecision(); 
				QtyCount = QtyCount.setScale(precision, BigDecimal.ROUND_HALF_UP);
			}
		}
		super.setQtyCount(QtyCount);
	}	//	setQtyCount

	/**
	 * 	Set Internal Use Qty - enforce UOM 
	 *	@param QtyInternalUse qty
	 */
	public void setQtyInternalUse (BigDecimal QtyInternalUse)
	{
		if (QtyInternalUse != null)
		{
			MProduct product = getProduct();
			if (product != null)
			{
				int precision = product.getUOMPrecision(); 
				QtyInternalUse = QtyInternalUse.setScale(precision, BigDecimal.ROUND_HALF_UP);
			}
		}
		super.setQtyInternalUse(QtyInternalUse);
	}	//	setQtyInternalUse

	
	/**
	 * 	Add to Description
	 *	@param description text
	 */
	public void addDescription (String description)
	{
		String desc = getDescription();
		if (desc == null)
			setDescription(description);
		else
			setDescription(desc + " | " + description);
	}	//	addDescription

	/**
	 * 	Get Parent
	 *	@param parent parent
	 */
	protected void setParent(MInventory parent)
	{
		m_parent = parent; 
	}	//	setParent

	/**
	 * 	Get Parent
	 *	@return parent
	 */
	public MInventory getParent()
	{
		if (m_parent == null)
			m_parent = new MInventory (getCtx(), getM_Inventory_ID(), get_TrxName());
		return m_parent;
	}	//	getParent
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MInventoryLine[");
		sb.append (get_ID())
			.append("-M_Product_ID=").append (getM_Product_ID())
			.append(",QtyCount=").append(getQtyCount())
			.append(",QtyInternalUse=").append(getQtyInternalUse())
			.append(",QtyBook=").append(getQtyBook())
			.append(",M_AttributeSetInstance_ID=").append(getM_AttributeSetInstance_ID())
			.append("]");
		return sb.toString ();
	}	//	toString
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		if (newRecord && m_isManualEntry)
		{
			//	Product requires ASI
			if (getM_AttributeSetInstance_ID() == 0)
			{
				MProduct product = MProduct.get(getCtx(), getM_Product_ID(), get_TrxName());
				if (product.getM_AttributeSet_ID() != 0)
				{
					MAttributeSet mas = MAttributeSet.get(getCtx(), product.getM_AttributeSet_ID(), get_TrxName());
					if (mas.isInstanceAttribute() 
						&& (mas.isMandatory() || mas.isMandatoryAlways()))
					{
						log.saveError("FillMandatory", Msg.getElement(getCtx(), "M_AttributeSetInstance_ID"));
						return false;
					}
				}
			}	//	No ASI
		}	//	new or manual
		
		//	Set Line No
		if (getLine() == 0)
		{
			String sql = "SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_InventoryLine WHERE M_Inventory_ID=?";
			int ii = DB.getSQLValue (get_TrxName(), sql, getM_Inventory_ID());
			setLine (ii);
		}

		//	Enforce Qty UOM
		if (newRecord || is_ValueChanged("QtyCount"))
			setQtyCount(getQtyCount());
		if (newRecord || is_ValueChanged("QtyInternalUse"))
			setQtyInternalUse(getQtyInternalUse());
		
		//	InternalUse Inventory
		if (getQtyInternalUse().signum() != 0)
		{
			if (!REF_M_InventoryType.ChargeAccount.equals(getInventoryType()))
				setInventoryType(REF_M_InventoryType.ChargeAccount);
			//
			if (getC_Charge_ID() == 0)
			{
				log.saveError("InternalUseNeedsCharge", "");
				return false;
			}
		}
		else if (REF_M_InventoryType.ChargeAccount.equals(getInventoryType()))
		{
			if (getC_Charge_ID() == 0)
			{
				log.saveError("FillMandatory", Msg.getElement(getCtx(), "C_Charge_ID"));
				return false;
			}
		}
		else if (getC_Charge_ID() != 0)
			setC_Charge_ID(0);
		
		//	Set AD_Org to parent if not charge
		if (getC_Charge_ID() == 0)
			setAD_Org_ID(getParent().getAD_Org_ID());
		
		return true;
	}	//	beforeSave

	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Create MA
		if (newRecord && success 
			&& m_isManualEntry && getM_AttributeSetInstance_ID() == 0)
			createMA();
		return success;
	}	//	afterSave
	
	/**
	 * 	Create Material Allocations for new Instances
	 */
	private void createMA()
	{
		MStorage[] storages = MStorage.getAll(getCtx(), getM_Product_ID(), 
			getM_Locator_ID(), get_TrxName());
		boolean allZeroASI = true;
		for (int i = 0; i < storages.length; i++)
		{
			if (storages[i].getM_AttributeSetInstance_ID() != 0)
			{
				allZeroASI = false;
				break;
			}
		}
		if (allZeroASI)
			return;
		
		MInventoryLineMA ma = null; 
		BigDecimal sum = Env.ZERO;
		for (int i = 0; i < storages.length; i++)
		{
			MStorage storage = storages[i];
			if (storage.getQtyOnHand().signum() == 0)
				continue;
			if (ma != null 
				&& ma.getM_AttributeSetInstance_ID() == storage.getM_AttributeSetInstance_ID())
				ma.setMovementQty(ma.getMovementQty().add(storage.getQtyOnHand()));
			else
				ma = new MInventoryLineMA (this, 
					storage.getM_AttributeSetInstance_ID(), storage.getQtyOnHand());
			if (!ma.save())
				;
			sum = sum.add(storage.getQtyOnHand());
		}
		if (sum.compareTo(getQtyBook()) != 0)
		{
			log.warning("QtyBook=" + getQtyBook() + " corrected to Sum of MA=" + sum);
			setQtyBook(sum);
		}
	}	//	createMA
	
}	//	MInventoryLine
