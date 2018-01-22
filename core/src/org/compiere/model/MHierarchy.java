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

import org.compiere.model.persistence.X_PA_Hierarchy;
import org.compiere.model.reference.REF_AD_TreeTypeType;
import org.compiere.util.*;

/**
 * 	Reporting Hierarchy Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MHierarchy.java 5583 2015-08-05 14:11:58Z xapiens $
 */
public class MHierarchy extends X_PA_Hierarchy
{
	/**
	 * 	Get MHierarchy from Cache
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID id
	 *	@return MHierarchy
	 */
	public static MHierarchy get (Properties ctx, int PA_Hierarchy_ID)
	{
		Integer key = new Integer (PA_Hierarchy_ID);
		MHierarchy retValue = (MHierarchy)s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MHierarchy (ctx, PA_Hierarchy_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	} //	get

	/**	Cache						*/
	private static CCache<Integer, MHierarchy> s_cache 
		= new CCache<Integer, MHierarchy> ("PA_Hierarchy_ID", 20);
	
	/**
	 * 	Default Constructor
	 *	@param ctx context
	 *	@param PA_Hierarchy_ID id
	 *	@param trxName trx
	 */
	public MHierarchy (Properties ctx, int PA_Hierarchy_ID, String trxName)
	{
		super (ctx, PA_Hierarchy_ID, trxName);
	}	//	MHierarchy

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName trx
	 */
	public MHierarchy (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MHierarchy
	
	/**
	 * 	Get AD_Tree_ID based on tree type
	 *	@param TreeType Tree Type
	 *	@return id or 0
	 */
	public int getAD_Tree_ID (String TreeType)
	{
		if (REF_AD_TreeTypeType.Activity.equals(TreeType))
			return getAD_Tree_Activity_ID();
		if (REF_AD_TreeTypeType.BPartner.equals(TreeType))
			return getAD_Tree_BPartner_ID();
		if (REF_AD_TreeTypeType.Campaign.equals(TreeType))
			return getAD_Tree_Campaign_ID();
		if (REF_AD_TreeTypeType.ElementValue.equals(TreeType))
			return getAD_Tree_Account_ID();
		if (REF_AD_TreeTypeType.Organization.equals(TreeType))
			return getAD_Tree_Org_ID();
		if (REF_AD_TreeTypeType.Product.equals(TreeType))
			return getAD_Tree_Product_ID();
		if (REF_AD_TreeTypeType.Project.equals(TreeType))
			return getAD_Tree_Project_ID();
		if (REF_AD_TreeTypeType.SalesRegion.equals(TreeType))
			return getAD_Tree_SalesRegion_ID();
		//
		log.warning("Not supported: " + TreeType);
		return 0;
	}	//	getAD_Tree_ID
	
}	//	MHierarchy
