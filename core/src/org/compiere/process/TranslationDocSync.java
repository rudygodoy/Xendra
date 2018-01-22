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
package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.model.MClient;
import org.compiere.model.MColumn;
import org.compiere.model.MTable;
import org.compiere.util.*;

import org.xendra.annotations.XendraProcess;


/**
 *	Document Translation Sync 
 *	
 *  @author Jorg Janke
 *  @version $Id: TranslationDocSync.java 5583 2015-08-05 14:11:58Z xapiens $
 */
@XendraProcess(value="TranslationDocSync",
name="Synchronize Doc Translation",
description="Synchronize Document Translation",
help="If the current client has not multi-lingual documents enabled, the translations for documents are synchronized with the main record (i.e. it copies the content of the main record to the translation records).  This process is necessary when swiching to a mono-lingual environment as there the terminoligy is maintained not in the translation records.<br>This applies to the client defined document translations, e.g. for UoM, Payment Terms, Product Info, etc.",
Identifier="c9d2a309-1c90-63f4-47d0-fe9be896fe09",
classname="org.compiere.process.TranslationDocSync",
updated="2015-06-20 10:10:12")
public class TranslationDocSync extends SvrProcess
{
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception
	 */
	protected String doIt() throws Exception
	{
		MClient client = MClient.get(getCtx());
		if (client.isMultiLingualDocument())
			throw new XendraUserError("@AD_Client_ID@: @IsMultiLingualDocument@");
		//
		log.info("" + client);
		String sql = "SELECT * FROM AD_Table "
			+ "WHERE TableName LIKE '%_Trl' AND TableName NOT LIKE 'AD%' "
			+ "ORDER BY TableName";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				processTable (new MTable(getCtx(), rs, null), client.getAD_Client_ID());
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log (Level.SEVERE, sql, e);
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
		
		return "OK";
	}	//	doIt
	
	/**
	 * 	Process Translation Table
	 *	@param table table
	 */
	private void processTable (MTable table, int AD_Client_ID)
	{
		StringBuffer sql = new StringBuffer();
		MColumn[] columns = table.getColumns(false);
		for (int i = 0; i < columns.length; i++)
		{
			MColumn column = columns[i];
			if (column.getAD_Reference_ID() == DisplayType.String
				|| column.getAD_Reference_ID() == DisplayType.Text)
			{
				String columnName = column.getColumnName();
				if (sql.length() != 0)
					sql.append(",");
				sql.append(columnName);
			}
		}
		String baseTable = table.getTableName();
		baseTable = baseTable.substring(0, baseTable.length()-4);
		
		log.config(baseTable + ": " + sql);
		String columnNames = sql.toString();
		
		sql = new StringBuffer();
		sql.append("UPDATE ").append(table.getTableName()).append(" t SET (")
			.append(columnNames).append(") = (SELECT ").append(columnNames)
			.append(" FROM ").append(baseTable).append(" b WHERE t.")
			.append(baseTable).append("_ID=b.").append(baseTable).append("_ID) WHERE AD_Client_ID=")
			.append(AD_Client_ID);
		
		int no = DB.executeUpdate(sql.toString(), get_TrxName());
		addLog(0, null, new BigDecimal(no), baseTable);
	}	//	processTable
	
}	//	TranslationDocSync
