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
/** Generated Model for M_ProductionLine
 *  @author Xendra (generated) 
 *  @version Release 2.00 - $Id: GenerateModel.java 5535 2015-04-28 06:51:56Z xapiens $ */
public class X_M_ProductionLine extends PO
{
/** Standard Constructor
@param ctx context
@param M_ProductionLine_ID id
@param trxName transaction
*/
public X_M_ProductionLine (Properties ctx, int M_ProductionLine_ID, String trxName)
{
super (ctx, M_ProductionLine_ID, trxName);
/** if (M_ProductionLine_ID == 0)
{
setLine (0);	
// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_ProductionLine WHERE M_ProductionPlan_ID=@M_ProductionPlan_ID@
setM_AttributeSetInstance_ID (0);
setM_Locator_ID (0);	
// @M_Locator_ID@
setMovementQty (Env.ZERO);
setM_Product_ID (0);
setM_ProductionLine_ID (0);
setM_ProductionPlan_ID (0);
setProcessed (false);	
// N
}
 */
}
/** Load Constructor 
@param ctx context
@param rs result set 
@param trxName transaction
*/
public X_M_ProductionLine (Properties ctx, ResultSet rs, String trxName)
{
super (ctx, rs, trxName);
}
/** AD_Table_ID=326 */
public static int Table_ID=MTable.getTable_ID("M_ProductionLine");

@XendraTrl(Identifier="edf9e540-e88a-9efb-06f4-57ef393a6794")
public static String es_PE_TAB_ProductionLine_Description="Ìtem de Producción";

@XendraTrl(Identifier="edf9e540-e88a-9efb-06f4-57ef393a6794")
public static String es_PE_TAB_ProductionLine_Help="La pestaña Ìtem de Producción despliega los movimientos de ingreso y egreso reales de inventario que fueron generados por un plan de producción.";

@XendraTrl(Identifier="edf9e540-e88a-9efb-06f4-57ef393a6794")
public static String es_PE_TAB_ProductionLine_Name="Ìtem de Producción";

@XendraTab(Name="Production Line",Description="Production Line",
Help="The Production Line Tab displays the actual movements in and out of inventory generated by a Production Plan.",
AD_Window_ID="735b6408-f744-4421-e308-a3bb880dfd24",SeqNo=30,TabLevel=2,IsSingleRow=false,
IsInfoTab=false,IsTranslationTab=false,IsReadOnly=false,AD_Column_ID="",HasTree=false,
WhereClause="",OrderByClause="",CommitWarning="",AD_Process_ID="",AD_ColumnSortOrder_ID="",
AD_ColumnSortYesNo_ID="",IsSortTab=false,Included_Tab_ID="",ReadOnlyLogic="",DisplayLogic="",
IsInsertRecord=true,IsAdvancedTab=false,Parent_Column_ID="",
Identifier="edf9e540-e88a-9efb-06f4-57ef393a6794",Synchronized="2012-03-17 18:47:08.603")
public static final String TABNAME_ProductionLine="edf9e540-e88a-9efb-06f4-57ef393a6794";

@XendraTrl(Identifier="ec785717-d7b3-37da-e7bb-9c46d055f341")
public static String es_PE_TABLE_M_ProductionLine_Name="Línea de Producción";

@XendraTable(Name="Production Line",AD_Package_ID="2809ac2d-7d43-20f8-05f0-a478f2e50204",
AD_Plugin_ID="67dff047-7c04-1001-e4d1-ad0b0ce9a44a",
Description="Document Line representing a production",Help="",TableName="M_ProductionLine",
AccessLevel="3",AD_Window_ID="735b6408-f744-4421-e308-a3bb880dfd24",AD_Val_Rule_ID="",IsKey=1,
LoadSeq=135,IsSecurityEnabled=false,IsDeleteable=true,IsHighVolume=true,IsChangeLog=false,
IsView=false,PO_Window_ID="",ID="org.xendra.material",
Identifier="ec785717-d7b3-37da-e7bb-9c46d055f341",Synchronized="2020-03-03 21:38:52.0")
/** TableName=M_ProductionLine */
public static final String Table_Name="M_ProductionLine";


@XendraIndex(Name="m_productionline_prodplan",Identifier="dcec49ae-1266-f3c9-4049-4ffe0c35d103",
Column_Names="m_productionplan_id",IsUnique="false",
TableIdentifier="dcec49ae-1266-f3c9-4049-4ffe0c35d103",Synchronized="2014-06-01 19:03:58.0")
public static final String INDEXNAME_m_productionline_prodplan = "dcec49ae-1266-f3c9-4049-4ffe0c35d103";

protected static KeyNamePair Model = new KeyNamePair(Table_ID,"M_ProductionLine");

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
    Table_ID = MTable.getTable_ID("M_ProductionLine");
POInfo poi = POInfo.getPOInfo (ctx, Table_ID);
return poi;
}
/** Info
@return info
*/
public String toString()
{
StringBuffer sb = new StringBuffer ("X_M_ProductionLine[").append(get_ID()).append("]");
return sb.toString();
}
/** Set Description.
@param Description Optional short DESCRIPTION of the record */
public void setDescription (String Description)
{
if (Description != null && Description.length() > 255)
{
log.warning("Length > 255 - truncated");
Description = Description.substring(0,254);
}
set_Value (COLUMNNAME_Description, Description);
}
/** Get Description.
@return Optional short DESCRIPTION of the record */
public String getDescription() 
{
String value = (String)get_Value(COLUMNNAME_Description);
if (value == null)
  return "";
if (value.contains("\""))
  value = value.replaceAll("\"","'");
if (value.contains("\n"))
  value = value.replaceAll("\n"," ");
if (value.contains(";"))
  value = value.replaceAll(";",":");
return value;
}

@XendraTrl(Identifier="56250f0d-70c0-39ac-85e8-a63664a65fc8")
public static String es_PE_FIELD_ProductionLine_Description_Name="Observación";

@XendraTrl(Identifier="56250f0d-70c0-39ac-85e8-a63664a65fc8")
public static String es_PE_FIELD_ProductionLine_Description_Description="Observación corta opcional del registro";

@XendraTrl(Identifier="56250f0d-70c0-39ac-85e8-a63664a65fc8")
public static String es_PE_FIELD_ProductionLine_Description_Help="Una Observación esta limitada a 255 caracteres";

@XendraField(AD_Column_ID="Description",IsCentrallyMaintained=true,
AD_Tab_ID="edf9e540-e88a-9efb-06f4-57ef393a6794",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=60,IsReadOnly=false,SeqNo=120,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2012-03-17 18:47:14.0",
Identifier="56250f0d-70c0-39ac-85e8-a63664a65fc8")
public static final String FIELDNAME_ProductionLine_Description="56250f0d-70c0-39ac-85e8-a63664a65fc8";

@XendraTrl(Identifier="59ee3dd8-8bd4-7e52-9695-725e26e275d5")
public static String es_PE_COLUMN_Description_Name="Descripción";

@XendraColumn(AD_Element_ID="2c699aa9-808d-eb67-0fe1-3328890909ca",ColumnName="Description",
AD_Reference_ID=10,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=255,DefaultValue="",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=false,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=false,SeqNo=0,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="1",IsSelectionColumn=true,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="59ee3dd8-8bd4-7e52-9695-725e26e275d5",
Synchronized="2019-08-30 22:23:27.0")
/** Column name Description */
public static final String COLUMNNAME_Description = "Description";
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
@XendraTrl(Identifier="b4ed77a2-a246-48c9-8e81-d6629b656754")
public static String es_PE_COLUMN_Identifier_Name="Identifier";

@XendraColumn(AD_Element_ID="f9dfa544-a995-1afd-baaf-53bbadefbbcb",ColumnName="Identifier",
AD_Reference_ID=10,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=36,DefaultValue="",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=false,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=false,SeqNo=0,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="0",IsSelectionColumn=false,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="b4ed77a2-a246-48c9-8e81-d6629b656754",
Synchronized="2019-08-30 22:23:27.0")
/** Column name Identifier */
public static final String COLUMNNAME_Identifier = "Identifier";
/** Set IsCosted.
@param IsCosted IsCosted */
public void setIsCosted (boolean IsCosted)
{
set_Value (COLUMNNAME_IsCosted, Boolean.valueOf(IsCosted));
}
/** Get IsCosted.
@return IsCosted */
public boolean isCosted() 
{
Object oo = get_Value(COLUMNNAME_IsCosted);
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

@XendraTrl(Identifier="e595278b-61db-eed5-3d91-0e768b6bf5f6")
public static String es_PE_COLUMN_IsCosted_Name="IsCosted";

@XendraColumn(AD_Element_ID="1d2cca33-7933-6933-e8cd-036ca4cb806e",ColumnName="IsCosted",
AD_Reference_ID=20,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=1,DefaultValue="N",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=false,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=false,SeqNo=0,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="0",IsSelectionColumn=false,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="e595278b-61db-eed5-3d91-0e768b6bf5f6",
Synchronized="2019-08-30 22:23:27.0")
/** Column name IsCosted */
public static final String COLUMNNAME_IsCosted = "IsCosted";
/** Set Line No.
@param Line Unique line for this document */
public void setLine (int Line)
{
set_Value (COLUMNNAME_Line, Integer.valueOf(Line));
}
/** Get Line No.
@return Unique line for this document */
public int getLine() 
{
Integer ii = (Integer)get_Value(COLUMNNAME_Line);
if (ii == null) return 0;
return ii.intValue();
}
/** Get Record ID/ColumnName
@return ID/ColumnName pair
*/public KeyNamePair getKeyNamePair() 
{
return new KeyNamePair(get_ID(), String.valueOf(getLine()));
}

@XendraTrl(Identifier="74fd8ee3-2bff-d367-fcad-ba8b3e1f5c1b")
public static String es_PE_FIELD_ProductionLine_LineNo_Name="No. Línea";

@XendraTrl(Identifier="74fd8ee3-2bff-d367-fcad-ba8b3e1f5c1b")
public static String es_PE_FIELD_ProductionLine_LineNo_Description="No. Línea único para este documento";

@XendraTrl(Identifier="74fd8ee3-2bff-d367-fcad-ba8b3e1f5c1b")
public static String es_PE_FIELD_ProductionLine_LineNo_Help="Indica el No. Línea para un documento. También controlará el orden de despliegue de las líneas dentro de un documento";

@XendraField(AD_Column_ID="Line",IsCentrallyMaintained=true,
AD_Tab_ID="edf9e540-e88a-9efb-06f4-57ef393a6794",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=11,IsReadOnly=false,SeqNo=60,SortNo=1,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2012-03-17 18:47:14.0",
Identifier="74fd8ee3-2bff-d367-fcad-ba8b3e1f5c1b")
public static final String FIELDNAME_ProductionLine_LineNo="74fd8ee3-2bff-d367-fcad-ba8b3e1f5c1b";

@XendraTrl(Identifier="84d632ab-97bc-085c-1a5d-d35730915b00")
public static String es_PE_COLUMN_Line_Name="No. Línea";

@XendraColumn(AD_Element_ID="986ef077-61da-f9cf-1c6d-aefe3a35113b",ColumnName="Line",
AD_Reference_ID=11,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=22,
DefaultValue="@SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_ProductionLine WHERE M_ProductionPlan_ID=@M_ProductionPlan_ID@",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=true,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=true,SeqNo=1,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="0",IsSelectionColumn=false,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="84d632ab-97bc-085c-1a5d-d35730915b00",
Synchronized="2019-08-30 22:23:27.0")
/** Column name Line */
public static final String COLUMNNAME_Line = "Line";
/** Set Attribute Set Instance.
@param M_AttributeSetInstance_ID Product Attribute Set Instance */
public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
{
if (M_AttributeSetInstance_ID < 0) throw new IllegalArgumentException ("M_AttributeSetInstance_ID is mandatory.");
set_Value (COLUMNNAME_M_AttributeSetInstance_ID, Integer.valueOf(M_AttributeSetInstance_ID));
}
/** Get Attribute Set Instance.
@return Product Attribute Set Instance */
public int getM_AttributeSetInstance_ID() 
{
Integer ii = (Integer)get_Value(COLUMNNAME_M_AttributeSetInstance_ID);
if (ii == null) return 0;
return ii.intValue();
}

@XendraTrl(Identifier="94e5750e-1f78-42e2-5b21-45063422ae22")
public static String es_PE_FIELD_ProductionLine_AttributeSetInstance_Name="Instancia del Conjunto de Atributos";

@XendraTrl(Identifier="94e5750e-1f78-42e2-5b21-45063422ae22")
public static String es_PE_FIELD_ProductionLine_AttributeSetInstance_Description="Instancia del conjunto de atributos del producto";

@XendraTrl(Identifier="94e5750e-1f78-42e2-5b21-45063422ae22")
public static String es_PE_FIELD_ProductionLine_AttributeSetInstance_Help="Valor actual de Instancia del conjunto de atributos del producto";

@XendraField(AD_Column_ID="M_AttributeSetInstance_ID",IsCentrallyMaintained=true,
AD_Tab_ID="edf9e540-e88a-9efb-06f4-57ef393a6794",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=26,IsReadOnly=false,SeqNo=80,SortNo=0,IsSameLine=true,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2012-03-17 18:47:14.0",
Identifier="94e5750e-1f78-42e2-5b21-45063422ae22")
public static final String FIELDNAME_ProductionLine_AttributeSetInstance="94e5750e-1f78-42e2-5b21-45063422ae22";

@XendraTrl(Identifier="0e5d6ba5-36bb-646c-1c4b-e654c88627f8")
public static String es_PE_COLUMN_M_AttributeSetInstance_ID_Name="Instancia del Conjunto de Atributos";

@XendraColumn(AD_Element_ID="137807f2-3c52-e193-3ceb-f10b819cf955",
ColumnName="M_AttributeSetInstance_ID",AD_Reference_ID=35,AD_Reference_Value_ID="",
AD_Val_Rule_ID="",FieldLength=22,DefaultValue="",IsKey=false,IsInternal=false,IsParent=false,
IsMandatory=true,IsUpdateable=true,ReadOnlyLogic="",IsIdentifier=false,SeqNo=0,IsTranslated=false,
Callout="",VFormat="",ValueMin="",ValueMax="",Version="1",IsSelectionColumn=false,AD_Process_ID="",
IsAlwaysUpdateable=false,ColumnSQL="",IsAllowLogging=false,
Identifier="0e5d6ba5-36bb-646c-1c4b-e654c88627f8",Synchronized="2019-08-30 22:23:27.0")
/** Column name M_AttributeSetInstance_ID */
public static final String COLUMNNAME_M_AttributeSetInstance_ID = "M_AttributeSetInstance_ID";
/** Set Locator.
@param M_Locator_ID Warehouse Locator */
public void setM_Locator_ID (int M_Locator_ID)
{
if (M_Locator_ID < 1) throw new IllegalArgumentException ("M_Locator_ID is mandatory.");
set_Value (COLUMNNAME_M_Locator_ID, Integer.valueOf(M_Locator_ID));
}
/** Get Locator.
@return Warehouse Locator */
public int getM_Locator_ID() 
{
Integer ii = (Integer)get_Value(COLUMNNAME_M_Locator_ID);
if (ii == null) return 0;
return ii.intValue();
}

@XendraTrl(Identifier="d95f65c4-14c5-342c-7fe0-f556531ca9e3")
public static String es_PE_FIELD_ProductionLine_Locator_Name="Ubicación";

@XendraTrl(Identifier="d95f65c4-14c5-342c-7fe0-f556531ca9e3")
public static String es_PE_FIELD_ProductionLine_Locator_Description="Ubicación de Almacén";

@XendraTrl(Identifier="d95f65c4-14c5-342c-7fe0-f556531ca9e3")
public static String es_PE_FIELD_ProductionLine_Locator_Help="El ID de la ubicación indica en que parte del almacén se localiza el producto";

@XendraField(AD_Column_ID="M_Locator_ID",IsCentrallyMaintained=true,
AD_Tab_ID="edf9e540-e88a-9efb-06f4-57ef393a6794",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=14,IsReadOnly=false,SeqNo=110,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2012-03-17 18:47:14.0",
Identifier="d95f65c4-14c5-342c-7fe0-f556531ca9e3")
public static final String FIELDNAME_ProductionLine_Locator="d95f65c4-14c5-342c-7fe0-f556531ca9e3";

@XendraTrl(Identifier="3c3f8bc6-aecd-caf2-0fb4-4d24f6fe27fd")
public static String es_PE_COLUMN_M_Locator_ID_Name="Ubicación";

@XendraColumn(AD_Element_ID="67f044ce-2864-93f9-fd0d-320945150933",ColumnName="M_Locator_ID",
AD_Reference_ID=31,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=22,
DefaultValue="@M_Locator_ID@",IsKey=false,IsInternal=false,IsParent=false,IsMandatory=true,
IsUpdateable=true,ReadOnlyLogic="",IsIdentifier=false,SeqNo=0,IsTranslated=false,Callout="",
VFormat="",ValueMin="",ValueMax="",Version="1",IsSelectionColumn=true,AD_Process_ID="",
IsAlwaysUpdateable=false,ColumnSQL="",IsAllowLogging=false,
Identifier="3c3f8bc6-aecd-caf2-0fb4-4d24f6fe27fd",Synchronized="2019-08-30 22:23:27.0")
/** Column name M_Locator_ID */
public static final String COLUMNNAME_M_Locator_ID = "M_Locator_ID";
/** Set Movement Quantity.
@param MovementQty Quantity of a product moved. */
public void setMovementQty (BigDecimal MovementQty)
{
if (MovementQty == null) throw new IllegalArgumentException ("MovementQty is mandatory.");
set_Value (COLUMNNAME_MovementQty, MovementQty);
}
/** Get Movement Quantity.
@return Quantity of a product moved. */
public BigDecimal getMovementQty() 
{
BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MovementQty);
if (bd == null) return Env.ZERO;
return bd;
}

@XendraTrl(Identifier="ba1c57b9-7d99-2935-72a6-33096fa22e60")
public static String es_PE_FIELD_ProductionLine_MovementQuantity_Name="Cantidad del Movimiento";

@XendraTrl(Identifier="ba1c57b9-7d99-2935-72a6-33096fa22e60")
public static String es_PE_FIELD_ProductionLine_MovementQuantity_Description="Cantidad de un producto movido";

@XendraTrl(Identifier="ba1c57b9-7d99-2935-72a6-33096fa22e60")
public static String es_PE_FIELD_ProductionLine_MovementQuantity_Help="La Cantidad del Movimiento indica la cantidad de un producto que ha sido movido";

@XendraField(AD_Column_ID="MovementQty",IsCentrallyMaintained=true,
AD_Tab_ID="edf9e540-e88a-9efb-06f4-57ef393a6794",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=26,IsReadOnly=false,SeqNo=100,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2012-03-17 18:47:14.0",
Identifier="ba1c57b9-7d99-2935-72a6-33096fa22e60")
public static final String FIELDNAME_ProductionLine_MovementQuantity="ba1c57b9-7d99-2935-72a6-33096fa22e60";

@XendraTrl(Identifier="3a743dc5-3978-65ec-79d7-057d6d281f94")
public static String es_PE_COLUMN_MovementQty_Name="Cantidad del Movimiento";

@XendraColumn(AD_Element_ID="36c2f62a-1d3b-a3ff-a433-9fc2eec47380",ColumnName="MovementQty",
AD_Reference_ID=29,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=22,DefaultValue="",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=true,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=true,SeqNo=2,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="1",IsSelectionColumn=false,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="3a743dc5-3978-65ec-79d7-057d6d281f94",
Synchronized="2019-08-30 22:23:27.0")
/** Column name MovementQty */
public static final String COLUMNNAME_MovementQty = "MovementQty";
/** Set Product.
@param M_Product_ID Product, Service, Item */
public void setM_Product_ID (int M_Product_ID)
{
if (M_Product_ID < 1) throw new IllegalArgumentException ("M_Product_ID is mandatory.");
set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
}
/** Get Product.
@return Product, Service, Item */
public int getM_Product_ID() 
{
Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
if (ii == null) return 0;
return ii.intValue();
}

@XendraTrl(Identifier="f66aaac6-78db-790f-35ae-f7c615a429a2")
public static String es_PE_FIELD_ProductionLine_Product_Name="Producto";

@XendraTrl(Identifier="f66aaac6-78db-790f-35ae-f7c615a429a2")
public static String es_PE_FIELD_ProductionLine_Product_Description="Producto; servicio ó artículo.";

@XendraTrl(Identifier="f66aaac6-78db-790f-35ae-f7c615a429a2")
public static String es_PE_FIELD_ProductionLine_Product_Help="Identifica un artículo que puede ser comprado ó vendido es esta organización.";

@XendraField(AD_Column_ID="M_Product_ID",IsCentrallyMaintained=true,
AD_Tab_ID="edf9e540-e88a-9efb-06f4-57ef393a6794",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=26,IsReadOnly=false,SeqNo=70,SortNo=2,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2012-03-17 18:47:14.0",
Identifier="f66aaac6-78db-790f-35ae-f7c615a429a2")
public static final String FIELDNAME_ProductionLine_Product="f66aaac6-78db-790f-35ae-f7c615a429a2";

@XendraTrl(Identifier="d28b7d2b-f43a-a174-ccbf-7f714e053472")
public static String es_PE_COLUMN_M_Product_ID_Name="Producto";

@XendraColumn(AD_Element_ID="b4701f57-f2fc-567c-58e2-a6aefbc3932c",ColumnName="M_Product_ID",
AD_Reference_ID=30,AD_Reference_Value_ID="",AD_Val_Rule_ID="4281f8eb-238e-6b28-eb66-8cc866e6312c",
FieldLength=22,DefaultValue="",IsKey=false,IsInternal=false,IsParent=false,IsMandatory=true,
IsUpdateable=true,ReadOnlyLogic="",IsIdentifier=false,SeqNo=0,IsTranslated=false,
Callout="org.compiere.model.CalloutProduction.product",VFormat="",ValueMin="",ValueMax="",
Version="1",IsSelectionColumn=true,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="d28b7d2b-f43a-a174-ccbf-7f714e053472",
Synchronized="2019-08-30 22:23:27.0")
/** Column name M_Product_ID */
public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";
/** Set Production Line.
@param M_ProductionLine_ID Document Line representing a production */
public void setM_ProductionLine_ID (int M_ProductionLine_ID)
{
if (M_ProductionLine_ID < 1) throw new IllegalArgumentException ("M_ProductionLine_ID is mandatory.");
set_ValueNoCheck (COLUMNNAME_M_ProductionLine_ID, Integer.valueOf(M_ProductionLine_ID));
}
/** Get Production Line.
@return Document Line representing a production */
public int getM_ProductionLine_ID() 
{
Integer ii = (Integer)get_Value(COLUMNNAME_M_ProductionLine_ID);
if (ii == null) return 0;
return ii.intValue();
}

@XendraTrl(Identifier="33f02155-356d-7693-21d7-7af045f78251")
public static String es_PE_FIELD_ProductionLine_ProductionLine_Name="Item de Producción";

@XendraTrl(Identifier="33f02155-356d-7693-21d7-7af045f78251")
public static String es_PE_FIELD_ProductionLine_ProductionLine_Description="Item del documento representando una producción.";

@XendraTrl(Identifier="33f02155-356d-7693-21d7-7af045f78251")
public static String es_PE_FIELD_ProductionLine_ProductionLine_Help="Indica el Item del documento de producción (si es aplicable) para esta transacción.";

@XendraField(AD_Column_ID="M_ProductionLine_ID",IsCentrallyMaintained=true,
AD_Tab_ID="edf9e540-e88a-9efb-06f4-57ef393a6794",AD_FieldGroup_ID="",IsDisplayed=false,
DisplayLogic="",DisplayLength=14,IsReadOnly=false,SeqNo=10,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2012-03-17 18:47:14.0",
Identifier="33f02155-356d-7693-21d7-7af045f78251")
public static final String FIELDNAME_ProductionLine_ProductionLine="33f02155-356d-7693-21d7-7af045f78251";
/** Column name M_ProductionLine_ID */
public static final String COLUMNNAME_M_ProductionLine_ID = "M_ProductionLine_ID";
/** Set Production Plan.
@param M_ProductionPlan_ID Plan for how a product is produced */
public void setM_ProductionPlan_ID (int M_ProductionPlan_ID)
{
if (M_ProductionPlan_ID < 1) throw new IllegalArgumentException ("M_ProductionPlan_ID is mandatory.");
set_ValueNoCheck (COLUMNNAME_M_ProductionPlan_ID, Integer.valueOf(M_ProductionPlan_ID));
}
/** Get Production Plan.
@return Plan for how a product is produced */
public int getM_ProductionPlan_ID() 
{
Integer ii = (Integer)get_Value(COLUMNNAME_M_ProductionPlan_ID);
if (ii == null) return 0;
return ii.intValue();
}

@XendraTrl(Identifier="7ddfdd68-5ae7-379b-6f6f-4add6147c9ab")
public static String es_PE_FIELD_ProductionLine_ProductionPlan_Name="Plan de Producción";

@XendraTrl(Identifier="7ddfdd68-5ae7-379b-6f6f-4add6147c9ab")
public static String es_PE_FIELD_ProductionLine_ProductionPlan_Description="Plan de cómo un producto es producido";

@XendraTrl(Identifier="7ddfdd68-5ae7-379b-6f6f-4add6147c9ab")
public static String es_PE_FIELD_ProductionLine_ProductionPlan_Help="El plan de producción identifica las partidas y pasos en la generación de un producto.";

@XendraField(AD_Column_ID="M_ProductionPlan_ID",IsCentrallyMaintained=true,
AD_Tab_ID="edf9e540-e88a-9efb-06f4-57ef393a6794",AD_FieldGroup_ID="",IsDisplayed=true,
DisplayLogic="",DisplayLength=14,IsReadOnly=true,SeqNo=50,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2012-03-17 18:47:14.0",
Identifier="7ddfdd68-5ae7-379b-6f6f-4add6147c9ab")
public static final String FIELDNAME_ProductionLine_ProductionPlan="7ddfdd68-5ae7-379b-6f6f-4add6147c9ab";

@XendraTrl(Identifier="72756808-c6ab-277e-eef5-4c3686e8e14f")
public static String es_PE_COLUMN_M_ProductionPlan_ID_Name="Plan de Producción";

@XendraColumn(AD_Element_ID="19bd576a-c4f4-8c57-56b5-4b85a67d32d6",
ColumnName="M_ProductionPlan_ID",AD_Reference_ID=19,AD_Reference_Value_ID="",AD_Val_Rule_ID="",
FieldLength=22,DefaultValue="",IsKey=false,IsInternal=false,IsParent=true,IsMandatory=true,
IsUpdateable=false,ReadOnlyLogic="",IsIdentifier=true,SeqNo=3,IsTranslated=false,Callout="",
VFormat="",ValueMin="",ValueMax="",Version="1",IsSelectionColumn=false,AD_Process_ID="",
IsAlwaysUpdateable=false,ColumnSQL="",IsAllowLogging=false,
Identifier="72756808-c6ab-277e-eef5-4c3686e8e14f",Synchronized="2019-08-30 22:23:27.0")
/** Column name M_ProductionPlan_ID */
public static final String COLUMNNAME_M_ProductionPlan_ID = "M_ProductionPlan_ID";
/** Set Processed.
@param Processed The document has been processed */
public void setProcessed (boolean Processed)
{
set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
}
/** Get Processed.
@return The document has been processed */
public boolean isProcessed() 
{
Object oo = get_Value(COLUMNNAME_Processed);
if (oo != null) 
{
 if (oo instanceof Boolean) return ((Boolean)oo).booleanValue();
 return "Y".equals(oo);
}
return false;
}

@XendraTrl(Identifier="3776f3c5-86a5-795b-af1f-76bb9e4ce72e")
public static String es_PE_FIELD_ProductionLine_Processed_Name="Procesado";

@XendraTrl(Identifier="3776f3c5-86a5-795b-af1f-76bb9e4ce72e")
public static String es_PE_FIELD_ProductionLine_Processed_Description="El documento ha sido procesado";

@XendraTrl(Identifier="3776f3c5-86a5-795b-af1f-76bb9e4ce72e")
public static String es_PE_FIELD_ProductionLine_Processed_Help="El cuadro de verificación procesada indica que un documento ha sido procesado";

@XendraField(AD_Column_ID="Processed",IsCentrallyMaintained=true,
AD_Tab_ID="edf9e540-e88a-9efb-06f4-57ef393a6794",AD_FieldGroup_ID="",IsDisplayed=false,
DisplayLogic="",DisplayLength=1,IsReadOnly=false,SeqNo=20,SortNo=0,IsSameLine=false,
IsHeading=false,IsFieldOnly=false,Included_Tab_ID="",Synchronized="2012-03-17 18:47:14.0",
Identifier="3776f3c5-86a5-795b-af1f-76bb9e4ce72e")
public static final String FIELDNAME_ProductionLine_Processed="3776f3c5-86a5-795b-af1f-76bb9e4ce72e";

@XendraTrl(Identifier="66527b69-7f9d-7643-62ce-5e88cc54048a")
public static String es_PE_COLUMN_Processed_Name="Procesado";

@XendraColumn(AD_Element_ID="3f7035d2-fa5e-7416-fc0c-e49e43f9e083",ColumnName="Processed",
AD_Reference_ID=20,AD_Reference_Value_ID="",AD_Val_Rule_ID="",FieldLength=1,DefaultValue="N",
IsKey=false,IsInternal=false,IsParent=false,IsMandatory=true,IsUpdateable=true,ReadOnlyLogic="",
IsIdentifier=false,SeqNo=0,IsTranslated=false,Callout="",VFormat="",ValueMin="",ValueMax="",
Version="1",IsSelectionColumn=false,AD_Process_ID="",IsAlwaysUpdateable=false,ColumnSQL="",
IsAllowLogging=false,Identifier="66527b69-7f9d-7643-62ce-5e88cc54048a",
Synchronized="2019-08-30 22:23:27.0")
/** Column name Processed */
public static final String COLUMNNAME_Processed = "Processed";
}
