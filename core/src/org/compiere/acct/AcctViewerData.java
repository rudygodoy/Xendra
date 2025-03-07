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
package org.compiere.acct;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;

import org.compiere.model.MAcctSchema;
import org.compiere.model.MAcctSchemaElement;
import org.compiere.model.MColumn;
import org.compiere.model.MFactAcct;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MRefList;
import org.compiere.report.core.*;
import org.compiere.swing.CComboBox;
import org.compiere.util.*;


/**
 *  Account Viewer State - maintaines State information for the Account Viewer
 *
 *  @author Jorg Janke
 *  @version  $Id: AcctViewerData.java 4912 2013-05-29 16:23:57Z xapiens $
 */
class AcctViewerData
{
	/**
	 *  Constructor
	 *	@param ctx context
	 *  @param windowNo window no
	 *  @param ad_Client_ID client
	 * 	@param ad_Table_ID table
	 */
	public AcctViewerData (Properties ctx, int windowNo, int ad_Client_ID, int ad_Table_ID)
	{
		WindowNo = windowNo;
		AD_Client_ID = ad_Client_ID;
		if (AD_Client_ID == 0)
			AD_Client_ID = Env.getContextAsInt(Env.getCtx(), WindowNo, "AD_Client_ID");
		if (AD_Client_ID == 0)
			AD_Client_ID = Env.getContextAsInt(Env.getCtx(), "AD_Client_ID");
		AD_Table_ID = ad_Table_ID;
		//
		ASchemas = MAcctSchema.getClientAcctSchema(ctx, AD_Client_ID);
		ASchema = ASchemas[0];
	}   //  AcctViewerData

	/** Window              */
	public int              WindowNo;
	/** Client				*/
	public int              AD_Client_ID;
	/** All Acct Schema		*/
	public MAcctSchema[]    ASchemas = null;
	/** This Acct Schema	*/
	public MAcctSchema      ASchema = null;

	//  Selection Info
	/** Document Query		*/
	public boolean          documentQuery = false;
	/** Acct Schema			*/
	public int              C_AcctSchema_ID = 0;
	/** Posting Type		*/
	public String			PostingType = "";
	/** Organization		*/
	public int              AD_Org_ID = 0;
	/** Source Currency            */
	public Integer          sourceC_Currency_ID = 0;
	/** Date From			*/
	public Timestamp        DateFrom = null;
	/** Date To				*/
	public Timestamp        DateTo = null;

	//  Dodument Table Selection Info
	/** Table ID			*/
	public int              AD_Table_ID;
	/** Record				*/
	public int              Record_ID;
	/** account line of reference  */
	public int              Ref_Acct_ID;

	/** Containing Column and Query     */
	public HashMap<String,String>	whereInfo = new HashMap<String,String>();
	/** Containing TableName and AD_Table_ID    */
	public HashMap<String,Integer>	tableInfo = new HashMap<String,Integer>();

	//  Display Info
	/** Display Qty			*/
	boolean          displayQty = false;
	/** Display Source Surrency	*/
	boolean          displaySourceAmt = false;
	/** Display Document info	*/
	//boolean          displayDocumentInfo = false;
	//
	String           sortBy1 = "";
	String           sortBy2 = "";
	String           sortBy3 = "";
	String           sortBy4 = "";
	//
	boolean          group1 = false;
	boolean          group2 = false;
	boolean          group3 = false;
	boolean          group4 = false;
	
	//boolean groupByDocument = false;

	/** Leasing Columns		*/
	private int				m_leadingColumns = 0;
	/** UserElement1 Reference	*/
	private String 			m_ref1 = null;
	/** UserElement2 Reference	*/
	private String 			m_ref2 = null;
	//private boolean groupByDocument = false;
	private boolean summary = false; 
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(AcctViewerData.class);

	/**
	 *  Dispose
	 */
	public void dispose()
	{
		ASchemas = null;
		ASchema = null;
		//
		whereInfo.clear();
		whereInfo = null;
		//
		Env.clearWinContext(WindowNo);
	}   //  dispose

	
	/**************************************************************************
	 *  Fill Accounting Schema
	 *  @param cb JComboBox to be filled
	 */
	protected void fillAcctSchema (JComboBox cb)
	{
		for (int i = 0; i < ASchemas.length; i++)
			cb.addItem(new KeyNamePair(ASchemas[i].getC_AcctSchema_ID(),
				ASchemas[i].getName()));
	}   //  fillAcctSchema

	/**
	 *  Fill Posting Type
	 *  @param cb JComboBox to be filled
	 */
	protected void fillPostingType (JComboBox cb)
	{
		int AD_Reference_ID = 125;
		ValueNamePair[] pt = MRefList.getList(AD_Reference_ID, true);
		for (int i = 0; i < pt.length; i++)
			cb.addItem(pt[i]);
	}   //  fillPostingType

	/**
	 *  Fill Table with
	 *      ValueNamePair (TableName, translatedKeyColumnName)
	 *  and tableInfo with (TableName, AD_Table_ID)
	 *  and select the entry for AD_Table_ID
	 *
	 *  @param cb JComboBox to be filled
	 */
	protected void fillTable (JComboBox cb)
	{
		ValueNamePair select = null;
		//
		String sql = "SELECT AD_Table_ID, TableName FROM AD_Table t "
			+ "WHERE EXISTS (SELECT * FROM AD_Column c"
			+ " WHERE t.AD_Table_ID=c.AD_Table_ID AND c.ColumnName='Posted')"
			+ " AND IsView='N'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt(1);
				String tableName = rs.getString(2);
				String name = Msg.translate(Env.getCtx(), tableName+"_ID");
				//
				ValueNamePair pp = new ValueNamePair(tableName, name);
				cb.addItem(pp);
				tableInfo.put (tableName, new Integer(id));
				if (id == AD_Table_ID)
					select = pp;
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		if (select != null)
			cb.setSelectedItem(select);
	}   //  fillTable

	/**
	 *  Fill Org
	 *
	 *  @param cb JComboBox to be filled
	 */
	protected void fillOrg (JComboBox cb)
	{
		KeyNamePair pp = new KeyNamePair(0, "");
		cb.addItem(pp);
		String sql = "SELECT AD_Org_ID, Name FROM AD_Org WHERE AD_Client_ID=? ORDER BY Value";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Client_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				cb.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}   //  fillOrg

	public void fillCurrency(CComboBox cb) {
		KeyNamePair pp = new KeyNamePair(0, "");
		cb.addItem(pp);
		String sql = "SELECT C_Currency_ID, Cursymbol from C_Currency WHERE IsActive='Y'";
		try
		{	
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				cb.addItem(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
	}
	
	/**
	 *  Get Button Text
	 *
	 *  @param tableName table
	 *  @param columnName column
	 *  @param selectSQL sql
	 *  @return Text on button
	 */
	protected String getButtonText (String tableName, String columnName, String selectSQL)
	{
		//  SELECT (<embedded>) FROM tableName avd WHERE avd.<selectSQL>
		StringBuffer sql = new StringBuffer ("SELECT (");
		Language language = Env.getLanguage(Env.getCtx());
		sql.append(MLookupFactory.getLookup_TableDirEmbed(language, columnName, "avd"))
			.append(") FROM ").append(tableName).append(" avd WHERE avd.").append(selectSQL);
		String retValue = "<" + selectSQL + ">";
		try
		{
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql.toString());
			if (rs.next())
				retValue = rs.getString(1);
			rs.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql.toString(), e);
		}
		return retValue;
	}   //  getButtonText

	/**************************************************************************

	/**
	 *  Create Query and submit
	 *  @return Report Model
	 */
	protected RModel query()
	{
		//  Set Where Clause
		StringBuffer whereClause = new StringBuffer();
		StringBuffer SummarywhereClause = new StringBuffer();
		//  Add Organization
		if (C_AcctSchema_ID != 0)
			whereClause.append(RModel.TABLE_ALIAS)
				.append(".C_AcctSchema_ID=").append(C_AcctSchema_ID);

		//	Posting Type Selected
		if (PostingType != null && PostingType.length() > 0)
		{
			if (whereClause.length() > 0)
				whereClause.append(" AND ");
			whereClause.append(RModel.TABLE_ALIAS)
				.append(".PostingType='").append(PostingType).append("'");
		}
		
		//
		if (documentQuery)
		{
			if (whereClause.length() > 0)
				whereClause.append(" AND ");
			whereClause.append(RModel.TABLE_ALIAS).append(".AD_Table_ID=").append(AD_Table_ID)
				.append(" AND ").append(RModel.TABLE_ALIAS).append(".Record_ID=").append(Record_ID);
		}
		else
		{
			//  get values (Queries)
			Iterator it = whereInfo.values().iterator();
			while (it.hasNext())
			{
				String where = (String)it.next();
				if (where != null && where.length() > 0)    //  add only if not empty
				{
					if (whereClause.length() > 0)
						whereClause.append(" AND ");
					whereClause.append(RModel.TABLE_ALIAS).append(".").append(where);
				}
			}
			if (DateFrom != null || DateTo != null)
			{								
				if (DateFrom != null && DateTo != null)
					SummarywhereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct) BETWEEN ")
						.append(DB.TO_DATE(DateFrom)).append(" AND ").append(DB.TO_DATE(DateTo));
				else if (DateFrom != null)
					SummarywhereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct) >= ")
						.append(DB.TO_DATE(DateFrom));
				else    //  DateTo != null
					SummarywhereClause.append("TRUNC(").append(RModel.TABLE_ALIAS).append(".DateAcct) <= ")
						.append(DB.TO_DATE(DateTo));
				if (!summary)
				{
					if (whereClause.length() > 0)
						whereClause.append(" AND ");					
					whereClause.append(SummarywhereClause);
				}
			}
			//  Add Organization
			if (AD_Org_ID != 0)
			{
				if (whereClause.length() > 0)
					whereClause.append(" AND ");
				whereClause.append(RModel.TABLE_ALIAS).append(".AD_Org_ID=").append(AD_Org_ID);
			}
			if (sourceC_Currency_ID > 0)
			{
//				if (groupByDocument)
//				{
//					StringBuffer sourceCurrency = new StringBuffer();
//					sourceCurrency.append("CASE WHEN COALESCE(")
//					.append(RModel.TABLE_ALIAS).append(".InvoiceDocumentNo,'EMPTY') = 'EMPTY' THEN false ELSE ")
//					.append("getCurrencySourcebyDocumentNo(").append(RModel.TABLE_ALIAS).append(".Serial,").append(RModel.TABLE_ALIAS).append(".InvoiceDocumentNo)=").append(sourceC_Currency_ID)
//					.append(" END ");
//					if (whereClause.length() > 0)
//						whereClause.append(" AND ");				
//					whereClause.append(sourceCurrency);
//				}
//				else
//				{
					StringBuffer sourceCurrency = new StringBuffer();
					sourceCurrency.append(RModel.TABLE_ALIAS).append(".C_Currency_ID=").append(sourceC_Currency_ID);
					if (whereClause.length() > 0)
						whereClause.append(" AND ");				
					whereClause.append(sourceCurrency);					
//				}
			}
//			if (groupByDocument)
//			{
//				StringBuffer groupbyDocument = new StringBuffer();
//				groupbyDocument.append("CASE WHEN COALESCE(")
//				.append(RModel.TABLE_ALIAS).append(".InvoiceDocumentNo,'EMPTY') = 'EMPTY' THEN true ELSE ")
//				.append("COALESCE(").append(RModel.TABLE_ALIAS).append(".serial,'') || ")
//				.append(RModel.TABLE_ALIAS).append(".InvoiceDocumentNo NOT IN (SELECT COALESCE(serial,'') || InvoiceDocumentNo from Fact_Acct fa ")
//				.append(" WHERE ")
//				.append(whereClause.toString().replace(RModel.TABLE_ALIAS, "fa"))
//				.append(" Group By Serial,InvoiceDocumentNo Having (SUM(AmtSourceDr) = SUM(AmtSourceCr)) Order By Serial,InvoiceDocumentNo)")
//				.append(" END ");
//				if (whereClause.length() > 0)
//					whereClause.append(" AND ");				
//				whereClause.append(groupbyDocument);
//			}
		}

		//  Set Order By Clause
		StringBuffer orderClause = new StringBuffer();
//		if (this.groupByDocument)
//			orderClause.append(" COALESCE(TRIM(").append(RModel.TABLE_ALIAS).append(".Serial) || '-','') || COALESCE(").append(RModel.TABLE_ALIAS).append(".InvoiceDocumentNo,'') || CASE WHEN C_BPartner_ID > 0 THEN C_BPARTNER_ID::TEXT ELSE '' END  ");
		if (sortBy1.length() > 0)
		{
			if (orderClause.length() > 0)
				orderClause.append(",");			
			orderClause.append(RModel.TABLE_ALIAS).append(".").append(sortBy1);
		}
		if (sortBy2.length() > 0)
		{
			if (orderClause.length() > 0)
				orderClause.append(",");
			orderClause.append(RModel.TABLE_ALIAS).append(".").append(sortBy2);
		}
		if (sortBy3.length() > 0)
		{
			if (orderClause.length() > 0)
				orderClause.append(",");
			orderClause.append(RModel.TABLE_ALIAS).append(".").append(sortBy3);
		}
		if (sortBy4.length() > 0)
		{
			if (orderClause.length() > 0)
				orderClause.append(",");
			orderClause.append(RModel.TABLE_ALIAS).append(".").append(sortBy4);
		}
		if (orderClause.length() == 0)
			orderClause.append(RModel.TABLE_ALIAS).append(".Fact_Acct_ID");

		RModel rm = getRModel();

		//  Groups
//		if (this.groupByDocument)
//			rm.setGroup("InvoiceDocumentNo");
		if (group1 && sortBy1.length() > 0)
			rm.setGroup(sortBy1);
		if (group2 && sortBy2.length() > 0)
			rm.setGroup(sortBy2);
		if (group3 && sortBy3.length() > 0)
			rm.setGroup(sortBy3);
		if (group4 && sortBy4.length() > 0)
			rm.setGroup(sortBy4);

		//  Totals
		rm.setFunction("AmtAcctDr", RModel.FUNCTION_SUM);
		rm.setFunction("AmtAcctCr", RModel.FUNCTION_SUM);
		rm.setFunction("AmtSourceDr", RModel.FUNCTION_SUM);
		rm.setFunction("AmtSourceCr", RModel.FUNCTION_SUM);
		//if (groupByDocument)
		rm.setFunction("DiffAcct", RModel.FUNCTION_SUM);
		rm.setFunction("DiffSource", RModel.FUNCTION_SUM);
		rm.query (Env.getCtx(), whereClause.toString(), SummarywhereClause.toString(), orderClause.toString(), summary);

		return rm;
	}   //  query

	/**
	 *  Create Report Model (Columns)
	 *  @return Report Model
	 */
	private RModel getRModel()
	{
		Properties ctx = Env.getCtx();
		RModel rm = new RModel("Fact_Acct");
		//  Add Key (Lookups)
		ArrayList keys = createKeyColumns();
		int max = m_leadingColumns;
		if (max == 0)
			max = keys.size();
		for (int i = 0; i < max; i++)
		{
			String column = (String)keys.get(i);
			if (column != null && column.startsWith("Date"))
				rm.addColumn(new RColumn(ctx, column, DisplayType.Date));
			else if (column != null && column.endsWith("_ID"))
				rm.addColumn(new RColumn(ctx, column, DisplayType.TableDir));
			else if (column != null && column.startsWith("InvoiceDocument"))
				rm.addColumn(new RColumn(ctx, "DocumentNo", DisplayType.Text, "COALESCE(TRIM(Serial) || '-','') || COALESCE(InvoiceDocumentNo,'') || CASE WHEN C_BPartner_ID > 0 THEN ' (' || C_BPARTNER_ID::TEXT || ')' ELSE '' END AS InvoiceDocumentNo", 0, null));
		}
		//  Main Info
		rm.addColumn(new RColumn(ctx, "AmtAcctDr", DisplayType.Amount));
		rm.addColumn(new RColumn(ctx, "AmtAcctCr", DisplayType.Amount));
//		if (groupByDocument)
//		{
			rm.addColumn(new RColumn(ctx, "DiffAcct", DisplayType.Amount," AmtAcctDr-AmtAcctCr as DiffAcct" ));
//		}
		if (displaySourceAmt)
		{
			if (!keys.contains("DateTrx"))
				rm.addColumn(new RColumn(ctx, "DateTrx", DisplayType.Date));
			rm.addColumn(new RColumn(ctx, "C_Currency_ID", DisplayType.TableDir));
			rm.addColumn(new RColumn(ctx, "AmtSourceDr", DisplayType.Amount));
			rm.addColumn(new RColumn(ctx, "AmtSourceCr", DisplayType.Amount));
			rm.addColumn(new RColumn(ctx, "Rate", DisplayType.Amount,
				"CASE WHEN (AmtSourceDr + AmtSourceCr) = 0 THEN 0"
				+ " ELSE (AmtAcctDr + AmtAcctCr) / (AmtSourceDr + AmtSourceCr) END"));
			rm.addColumn(new RColumn(ctx, "DiffSource", DisplayType.Amount," AmtSourceDr-AmtSourceCr as DiffSource" ));
		}
		// Doc Type
		rm.addColumn(new RColumn(ctx, "C_DocType_ID", DisplayType.TableDir));
		// relation fact_acct 
		rm.addColumn(new RColumn(ctx, "Ref_Acct_ID", DisplayType.TableDir));
		// add document reference
		if (!keys.contains("InvoiceDocumentNo"))
			rm.addColumn(new RColumn(ctx, "InvoiceDocumentNo", DisplayType.Text, "COALESCE(Serial || '-','') || COALESCE(InvoiceDocumentNo,'')", 0, null));
		//	Remaining Keys
		for (int i = max; i < keys.size(); i++)
		{
			String column = (String)keys.get(i);
			if (column != null && column.startsWith("Date"))
				rm.addColumn(new RColumn(ctx, column, DisplayType.Date));
			else if (column.startsWith("UserElement"))
			{
				if (column.indexOf('1') != -1)
					rm.addColumn(new RColumn(ctx, column, DisplayType.TableDir, null, 0, m_ref1));
				else
					rm.addColumn(new RColumn(ctx, column, DisplayType.TableDir, null, 0, m_ref2));
			}
			else if (column != null && column.endsWith("_ID"))
				rm.addColumn(new RColumn(ctx, column, DisplayType.TableDir));
		}
		//	Info
		if (!keys.contains("DateAcct"))
			rm.addColumn(new RColumn(ctx, "DateAcct", DisplayType.Date));
		if (!keys.contains("DateTrx")) 
			rm.addColumn(new RColumn(ctx, "DateTrx", DisplayType.Date));
		if (!keys.contains("C_BPartner_ID")) 
			rm.addColumn(new RColumn(ctx, "C_BPartner_ID", DisplayType.TableDir));		
		if (!keys.contains("C_Period_ID"))
			rm.addColumn(new RColumn(ctx, "C_Period_ID", DisplayType.TableDir));
		if (displayQty)
		{
			rm.addColumn(new RColumn(ctx, "C_UOM_ID", DisplayType.TableDir));
			rm.addColumn(new RColumn(ctx, "Qty", DisplayType.Quantity));
		}
		//if (displayDocumentInfo)
		//{
			rm.addColumn(new RColumn(ctx, "AD_Table_ID", DisplayType.TableDir));
			rm.addColumn(new RColumn(ctx, "Record_ID", DisplayType.ID));
			rm.addColumn(new RColumn(ctx, "Description", DisplayType.String));
		//}
		if (PostingType == null || PostingType.length() == 0) {
			// X_Fact_Acct PostingType
			MColumn col = MColumn.getbyIdentifier("c695cca6-2527-c616-8ba3-7da65210895c", null);
			rm.addColumn(new RColumn(ctx, "PostingType", DisplayType.List, // teo_sarca, [ 1664208 ]
					RModel.TABLE_ALIAS+".PostingType", col.getAD_Reference_Value_ID(),
					null));
		}
		return rm;
	}   //  createRModel

	/**
	 *  Create the key columns in sequence
	 *  @return List of Key Columns
	 */
	private ArrayList createKeyColumns()
	{
		ArrayList<String> columns = new ArrayList<String>();
		m_leadingColumns = 0;
		//  Sorting Fields
//		if (groupByDocument)
//			columns.add("InvoiceDocumentNo");
		columns.add(sortBy1);               //  may add ""
		if (!columns.contains(sortBy2))
			columns.add(sortBy2);
		if (!columns.contains(sortBy3))
			columns.add(sortBy3);
		if (!columns.contains(sortBy4))
			columns.add(sortBy4);

		//  Add Account Segments
		MAcctSchemaElement[] elements = ASchema.getAcctSchemaElements();
		for (int i = 0; i < elements.length; i++)
		{
			if (m_leadingColumns == 0 && columns.contains("AD_Org_ID") && columns.contains("Account_ID"))
				m_leadingColumns = columns.size();
			//
			MAcctSchemaElement ase = elements[i];
			String columnName = ase.getColumnName();
			if (columnName.startsWith("UserElement"))
			{
				if (columnName.indexOf('1') != -1)
					m_ref1 = ase.getDisplayColumnName();
				else
					m_ref2 = ase.getDisplayColumnName();
			}
			if (!columns.contains(columnName))
				columns.add(columnName);
		}
		if (m_leadingColumns == 0 && columns.contains("AD_Org_ID") && columns.contains("Account_ID"))
			m_leadingColumns = columns.size();
		return columns;
	}   //  createKeyColumns


//	public void setgroupByDocument(boolean b) {
//		groupByDocument = b;		
//	}

	public void setsummary(boolean b) {
		summary = b;
	}

	public void setsourceC_Currency_ID(Integer pCurrency) {
		sourceC_Currency_ID = pCurrency;
	}	
}   //  AcctViewerData
