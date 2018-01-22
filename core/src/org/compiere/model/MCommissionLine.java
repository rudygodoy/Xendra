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

import org.compiere.model.persistence.X_C_CommissionLine;
import org.compiere.util.*;


/**
 *	Commission Line Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MCommissionLine.java 4091 2012-04-10 20:33:02Z xapiens $
 */
public class MCommissionLine extends X_C_CommissionLine
{
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_CommissionLine_ID id
	 *	@param trxName transaction
	 */
	public MCommissionLine(Properties ctx, int C_CommissionLine_ID, String trxName)
	{
		super(ctx, C_CommissionLine_ID, trxName);
		if (C_CommissionLine_ID == 0)
		{
		//	setC_Commission_ID (0);
			setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM C_CommissionLine WHERE C_Commission_ID=@C_Commission_ID@
			setAmtMultiplier (Env.ZERO);
			setAmtSubtract (Env.ZERO);
			setCommissionOrders (false);
			setIsPositiveOnly (false);
			setQtyMultiplier (Env.ZERO);
			setQtySubtract (Env.ZERO);
		}
	}	//	MCommissionLine

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MCommissionLine(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCommissionLine

	
	
}	//	MCommissionLine
