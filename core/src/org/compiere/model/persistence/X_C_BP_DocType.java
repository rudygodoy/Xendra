/******************************************************************************
 * Product: Xendra ERP & CRM Smart Business Solution                          *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software. you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY  without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model.persistence;

/** Generated Model - DO NOT CHANGE */
import org.compiere.model.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import org.xendra.annotations.*;
import org.compiere.util.*;
/** Generated Model for C_BP_DocType
 *  @author Xendra (generated) 
 *  @version Release 2.00 - $Id: GenerateModel.java 5535 2015-04-28 06:51:56Z xapiens $ */
public class X_C_BP_DocType extends PO
{
/** Standard Constructor
@param ctx context
@param C_BP_DocType_ID id
@param trxName transaction
*/
public X_C_BP_DocType (Properties ctx, int C_BP_DocType_ID, String trxName)
{
super (ctx, C_BP_DocType_ID, trxName);
/** if (C_BP_DocType_ID == 0)
{
setC_BP_DocType_ID (0);
setName (null);
setValue (null);
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_C_BP_DocType (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=1000308 */
public static int Table_ID=MTable.getTable_ID("C_BP_DocType");

@XendraTrl(Identifier="dcb8d484-efde-44e6-958b-6c66cfd46a3c")
public static String es_PE_TAB_BusinessPartnerDocumentType_Name="S.N. Tipo Doc.";

@XendraTab(Name="Business Partner Document Type",Description="",Help="",
AD_Window_ID="cabf6f9a-388a-41ef-9f4f-d25b977dd95d",SeqNo=10,TabLevel=0,IsSingleRow=false,
IsInfoTab=false,IsTranslationTab=false,IsReadOnly=false,AD_Column_ID="",HasTree=false,
WhereClause="",OrderByClause="",CommitWarning="",AD_Process_ID="",AD_ColumnSortOrder_ID="",
AD_ColumnSortYesNo_ID="",IsSortTab=false,Included_Tab_ID="",ReadOnlyLogic="",DisplayLogic="",
IsInsertRecord=true,IsAdvancedTab=false,Parent_Column_ID="",
Identifier="dcb8d484-efde-44e6-958b-6c66cfd46a3c",Synchronized="2019-03-22 08:42:32.0")
public static final String TABNAME_BusinessPartnerDocumentType="dcb8d484-efde-44e6-958b-6c66cfd46a3c";

@XendraTrl(Identifier="f79160b4-2084-43c8-981d-c5d18ff58a7a")
public static String es_PE_TABLE_C_BP_DocType_Name="Business Partner Document Type";

@XendraTable(Name="Business Partner Document Type",
AD_Package_ID="98af94a4-a4cd-bdc0-5651-5880caf79899",
AD_Plugin_ID="67dff047-7c04-1001-e4d1-ad0b0ce9a44a",Description="",Help="",
TableName="C_BP_DocType",AccessLevel="3",AD_Window_ID="",AD_Val_Rule_ID="",IsKey=1,LoadSeq=0,
IsSecurityEnabled=false,IsDeleteable=true,IsHighVolume=false,IsChangeLog=false,IsView=false,
PO_Window_ID="",ID="org.xendra.commercial",Identifier="f79160b4-2084-43c8-981d-c5d18ff58a7a",
Synchronized="2020-03-03 21:36:37.0")
/** TableName=C_BP_DocType */
public static final String Table_Name="C_BP_DocType";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"C_BP_DocType");

protected BigDecimal accessLevel = BigDecimal.valueOf(3);
/** AccessLevel
@return 3 - Client - Org 
*/
protected int get_AccessLevel()
{
return accessLevel.intValue();
}
/** Load Meta Data
@param ctx context
@return PO Info
*/
protected POInfo initPO (Properties ctx)
{
  if (Table_ID == 0)
    Table_ID = MTable.getTable_ID("C_BP_DocType");
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
/** Info
@return info
*/
public String toString()
{
StringBuffer sb = new StringBuffer ("X_C_BP_DocType[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Business Partner Document Type.
@param C_BP_DocType_ID Business Partner Document Type */
public void setC_BP_DocType_ID (int C_BP_DocType_ID)
{
if (C_BP_DocType_ID < 1) throw new IllegalArgumentException ("C_BP_DocType_ID is mandatory.");
set_ValueNoCheck (COLUMNNAME_C_BP_DocType_ID, Integer.valueOf(C_BP_DocType_ID));
}
/** Get Business Partner Document Type.
@return Business Partner Document Type */
public int getC_BP_DocType_ID() 
{
Integer ii = (Integer)get_Value(COLUMNNAME_C_BP_DocType_ID);
if (ii == null) return 0;
return ii.intValue();
}

@XendraTrl(Identifier="0fae3efb-7f6b-49f5-a3ff-e238226e04fb")
public static String es_PE_FIELD_BusinessPartnerDocumentType_BusinessPartnerDocumentType_Name="Business Partner Document Type";

@XendraField(AD_Column_ID="C_BP_DocType_ID",IsCentrallyMaintained=true,
AD_Tab_ID="dcb8d484-efde-44e6-958b-6c66cfd46a3c",AD_FieldGroup_ID="",IsDisplayed=false,
DisplayLogic="",DisplayLength=10,IsReadOnly=false,SeqNo=0,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2019-03-22 08:42:32.0",
Identifier="0fae3efb-7f6b-49f5-a3ff-e238226e04fb")
public static final String FIELDNAME_BusinessPartnerDocumentType_BusinessPartnerDocumentType="0fae3efb-7f6b-49f5-a3ff-e238226e04fb";
/** Column name C_BP_DocType_ID */
public static final String COLUMNNAME_C_BP_DocType_ID = "C_BP_DocType_ID";
/** Set Identifier.
@param Identifier Identifier */
public void setIdentifier (String Identifier)
{
if (Identifier != null && Identifier.length() > 36)
{
log.warning("Length > 36 - truncated");
Identifier = Identifier.substring(0,35);
}
set_Value (COLUMNNAME_Identifier, Identifier);
}
/** Get Identifier.
@return Identifier */
public String getIdentifier() 
{
String value = (String)get_Value(COLUMNNAME_Identifier);
if (value == null)
  return "";
return value;
}
@XendraTrl(Identifier="dc75c613-a63d-45e8-89ba-eb6a9027db3d")
public static String es_PE_FIELD_BusinessPartnerDocumentType_Identifier_Name="Identifier";

@XendraField(AD_Column_ID="Identifier",IsCentrallyMaintained=true,
AD_Tab_ID="dcb8d484-efde-44e6-958b-6c66cfd46a3c",AD_FieldGroup_ID="",IsDisplayed=false,
DisplayLogic="",DisplayLength=36,IsReadOnly=false,SeqNo=0,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2019-03-22 08:42:32.0",
Identifier="dc75c613-a63d-45e8-89ba-eb6a9027db3d")
public static final String FIELDNAME_BusinessPartnerDocumentType_Identifier="dc75c613-a63d-45e8-89ba-eb6a9027db3d";

@XendraTrl(Identifier="1719326e-588d-4593-83df-2c01f6703333")
public static String es_PE_COLUMN_Identifier_Name="Identifier";

@XendraColumn(AD_Element_ID="f9dfa544-a995-1afd-baaf-53bbadefbbcb",ColumnName="Identifier",
AD_Reference_ID=10,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=36,DefaultValue="",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=false,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=false,SeqNo=0,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="0",IsSelectionColumn=false,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="1719326e-588d-4593-83df-2c01f6703333",
Synchronized="2019-08-30 22:21:14.0")
/** Column name Identifier */
public static final String COLUMNNAME_Identifier = "Identifier";
/** Set Name.
@param Name Alphanumeric identifier of the entity */
public void setName (String Name)
{
if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
if (Name.length() > 60)
{
log.warning("Length > 60 - truncated");
Name = Name.substring(0,59);
}
set_Value (COLUMNNAME_Name, Name);
}
/** Get Name.
@return Alphanumeric identifier of the entity */
public String getName() 
{
String value = (String)get_Value(COLUMNNAME_Name);
if (value == null)
  return "";
return value;
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), getName());
}

@XendraTrl(Identifier="1c13b5f5-485e-4661-81d2-81902eb5c7bc")
public static String es_PE_FIELD_BusinessPartnerDocumentType_Name_Name="Nombre";

@XendraTrl(Identifier="1c13b5f5-485e-4661-81d2-81902eb5c7bc")
public static String es_PE_FIELD_BusinessPartnerDocumentType_Name_Description="Identificador alfanumérico de la entidad.";

@XendraTrl(Identifier="1c13b5f5-485e-4661-81d2-81902eb5c7bc")
public static String es_PE_FIELD_BusinessPartnerDocumentType_Name_Help="El nombre de una entidad (registro) se usa como una opción de búsqueda predeterminada adicional a la clave de búsqueda. El nombre es de hasta 60 caracteres de longitud.";

@XendraField(AD_Column_ID="Name",IsCentrallyMaintained=true,
AD_Tab_ID="dcb8d484-efde-44e6-958b-6c66cfd46a3c",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=60,IsReadOnly=false,SeqNo=50,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2019-03-22 08:42:32.0",
Identifier="1c13b5f5-485e-4661-81d2-81902eb5c7bc")
public static final String FIELDNAME_BusinessPartnerDocumentType_Name="1c13b5f5-485e-4661-81d2-81902eb5c7bc";

@XendraTrl(Identifier="0267c8a8-4ab7-44a9-bf04-a717edea707b")
public static String es_PE_COLUMN_Name_Name="Name";

@XendraColumn(AD_Element_ID="c0b362bf-e56d-aa8a-9021-c8c580f635de",ColumnName="Name",
AD_Reference_ID=10,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=60,DefaultValue="",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=true,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=true,SeqNo=1,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="0",IsSelectionColumn=false,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="0267c8a8-4ab7-44a9-bf04-a717edea707b",
Synchronized="2019-08-30 22:21:14.0")
/** Column name Name */
public static final String COLUMNNAME_Name = "Name";
/** Set Tax ID.
@param TaxID Tax Identification */
public void setTaxID (String TaxID)
{
if (TaxID != null && TaxID.length() > 20)
{
log.warning("Length > 20 - truncated");
TaxID = TaxID.substring(0,19);
}
set_Value (COLUMNNAME_TaxID, TaxID);
}
/** Get Tax ID.
@return Tax Identification */
public String getTaxID() 
{
String value = (String)get_Value(COLUMNNAME_TaxID);
if (value == null)
  return "";
return value;
}
@XendraTrl(Identifier="4bce4da7-5ee9-4bec-8b8e-da540a80a2d4")
public static String es_PE_FIELD_BusinessPartnerDocumentType_TaxID_Name="Código de Gobierno";

@XendraTrl(Identifier="4bce4da7-5ee9-4bec-8b8e-da540a80a2d4")
public static String es_PE_FIELD_BusinessPartnerDocumentType_TaxID_Description="Código de Identificación";

@XendraTrl(Identifier="4bce4da7-5ee9-4bec-8b8e-da540a80a2d4")
public static String es_PE_FIELD_BusinessPartnerDocumentType_TaxID_Help="El código de Identificación es el número de identificación gubernamental de esta entidad";

@XendraField(AD_Column_ID="TaxID",IsCentrallyMaintained=true,
AD_Tab_ID="dcb8d484-efde-44e6-958b-6c66cfd46a3c",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=20,IsReadOnly=false,SeqNo=60,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2019-03-22 08:42:32.0",
Identifier="4bce4da7-5ee9-4bec-8b8e-da540a80a2d4")
public static final String FIELDNAME_BusinessPartnerDocumentType_TaxID="4bce4da7-5ee9-4bec-8b8e-da540a80a2d4";

@XendraTrl(Identifier="54e9b924-7a34-44a6-9533-9cf01c2f9d4e")
public static String es_PE_COLUMN_TaxID_Name="Tax ID";

@XendraColumn(AD_Element_ID="e0a9d0f7-835b-4f80-4c13-bff62213e232",ColumnName="TaxID",
AD_Reference_ID=10,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=20,DefaultValue="",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=false,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=false,SeqNo=0,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="0",IsSelectionColumn=false,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="54e9b924-7a34-44a6-9533-9cf01c2f9d4e",
Synchronized="2019-08-30 22:21:14.0")
/** Column name TaxID */
public static final String COLUMNNAME_TaxID = "TaxID";
/** Set Search Key.
@param Value Search key for the record in the format required - must be unique */
public void setValue (String Value)
{
if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
if (Value.length() > 40)
{
log.warning("Length > 40 - truncated");
Value = Value.substring(0,39);
}
set_Value (COLUMNNAME_Value, Value);
}
/** Get Search Key.
@return Search key for the record in the format required - must be unique */
public String getValue() 
{
String value = (String)get_Value(COLUMNNAME_Value);
if (value == null)
  return "";
return value;
}
@XendraTrl(Identifier="ac635a50-a93b-4568-baba-6bb075fe8014")
public static String es_PE_FIELD_BusinessPartnerDocumentType_SearchKey_Name="Clave de Búsqueda";

@XendraTrl(Identifier="ac635a50-a93b-4568-baba-6bb075fe8014")
public static String es_PE_FIELD_BusinessPartnerDocumentType_SearchKey_Description="Clave de búsqueda para el registro en el formato requerido; debe ser única";

@XendraTrl(Identifier="ac635a50-a93b-4568-baba-6bb075fe8014")
public static String es_PE_FIELD_BusinessPartnerDocumentType_SearchKey_Help="Una clave de búsqueda le permite a usted un método rápido de encontrar un registro en particular";

@XendraField(AD_Column_ID="Value",IsCentrallyMaintained=true,
AD_Tab_ID="dcb8d484-efde-44e6-958b-6c66cfd46a3c",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=40,IsReadOnly=false,SeqNo=40,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2019-03-22 08:42:32.0",
Identifier="ac635a50-a93b-4568-baba-6bb075fe8014")
public static final String FIELDNAME_BusinessPartnerDocumentType_SearchKey="ac635a50-a93b-4568-baba-6bb075fe8014";

@XendraTrl(Identifier="d504e726-e8de-4d1b-a110-0a9fca51c01e")
public static String es_PE_COLUMN_Value_Name="Search Key";

@XendraColumn(AD_Element_ID="e75e100c-9b41-a643-2cb3-46eeced683d8",ColumnName="Value",
AD_Reference_ID=10,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=40,DefaultValue="",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=true,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=false,SeqNo=0,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="0",IsSelectionColumn=false,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="d504e726-e8de-4d1b-a110-0a9fca51c01e",
Synchronized="2019-08-30 22:21:14.0")
/** Column name Value */
public static final String COLUMNNAME_Value = "Value";
}
