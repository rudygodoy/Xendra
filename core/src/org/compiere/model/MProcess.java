/******************************************************************************
 * Product: Xendra ERP & CRM Smart Business Solution                          *
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

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;






import org.adempiere.util.ProcessUtil;
import org.compiere.model.persistence.X_AD_Process;
import org.compiere.model.persistence.X_AD_Rule;
import org.compiere.model.persistence.X_AD_WF_Node;
import org.compiere.model.reference.REF_AD_TableAccessLevels;
import org.compiere.print.MReportView;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CCache;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;


/**
 *  Process Model
 *
 *  @author Jorg Janke
 *  @version $Id: MProcess.java 5583 2015-08-05 14:11:58Z xapiens $
 * 
 * @author Teo Sarca, www.arhipac.ro
 * 			<li>BF [ 1757523 ] Server Processes are using Server's context
 * 			<li>FR [ 2214883 ] Remove SQL code and Replace for Query
 */
public class MProcess extends X_AD_Process
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2404724380401712390L;
	private static Hashtable rules = new Hashtable();

	/**
	 * 	Get MProcess from Cache
	 *	@param ctx context
	 *	@param AD_Process_ID id
	 *	@return MProcess
	 */
	public static MProcess get (Properties ctx, int AD_Process_ID)
	{
		Integer key = new Integer (AD_Process_ID);
		MProcess retValue = (MProcess) s_cache.get (key);
		if (retValue != null)
			return retValue;
		retValue = new MProcess (ctx, AD_Process_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**
	 * 	Get MProcess from Menu
	 *	@param ctx context
	 *	@param AD_Menu_ID id
	 *	@return MProcess or null
	 */
	public static MProcess getFromMenu (Properties ctx, int AD_Menu_ID)
	{
		String whereClause = "EXISTS (SELECT 1 FROM AD_Menu m"
							+" WHERE m.AD_Process_ID=AD_Process.AD_Process_ID AND m.AD_Menu_ID=?)";
		MProcess p = new Query(ctx, MProcess.Table_Name, whereClause, null)
			.setParameters(new Object[]{AD_Menu_ID})
			.firstOnly();
		if (p != null)
		{
			s_cache.put (p.get_ID(), p);
		}
		return p;
	}	//	getFromMenu


	/**	Cache						*/
	private static CCache<Integer,MProcess>	s_cache	= new CCache<Integer,MProcess>(Table_Name, 20);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Process_ID process
	 *	@param trxName transaction name
	 */
	public MProcess (Properties ctx, int AD_Process_ID, String trxName)
	{
		super (ctx, AD_Process_ID, trxName);
		if (AD_Process_ID == 0)
		{
		//	setValue (null);
		//	setName (null);
			setIsReport (false);
			setIsServerProcess(false);
			setAccessLevel (REF_AD_TableAccessLevels.All);
			setEntityType (ENTITYTYPE_UserMaintained);
			setIsBetaFunctionality(false);
		}
	}	//	MProcess

	/**
	 * 	Load Contsructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction name
	 */
	public MProcess (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MProcess


	/**	Parameters					*/
	private MProcessPara[]		m_parameters = null;

	/**
	 * 	Get Parameters
	 *	@return parameters
	 */
	public MProcessPara[] getParameters()
	{
		if (m_parameters != null)
			return m_parameters;
		//
		String whereClause = MProcessPara.COLUMNNAME_AD_Process_ID+"=?";
		List<MProcessPara> list = new Query(getCtx(), MProcessPara.Table_Name, whereClause, get_TrxName())
			.setParameters(new Object[]{get_ID()})
			.setOrderBy(MProcessPara.COLUMNNAME_SeqNo)
			.list();
		//
		m_parameters = new MProcessPara[list.size()];
		list.toArray(m_parameters);
		return m_parameters;
	}	//	getParameters

	/**
	 * 	Get Parameter with ColumnName
	 *	@param name column name
	 *	@return parameter or null
	 */
	public MProcessPara getParameter(String name)
	{
		getParameters();
		for (int i = 0; i < m_parameters.length; i++)
		{
			if (m_parameters[i].getColumnName().equals(name))
				return m_parameters[i];
		}
		return null;
	}	//	getParameter
	
	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MProcess[")
			.append (get_ID())
			.append("-").append(getName())
			.append ("]");
		return sb.toString ();
	}	//	toString

	
	/**************************************************************************
	 * 	Process w/o parameter
	 *	@param Record_ID record
	 *	@param trx transaction
	 *	@return Process Instance
	 */
	public MPInstance processIt (int Record_ID, Trx trx)
	{
		MPInstance pInstance = new MPInstance (this, Record_ID);
		//	Lock
		pInstance.setIsProcessing(true);
		pInstance.saveEx();

		boolean ok = true;

		ProcessInfo processInfo = new ProcessInfo("", this.getAD_Process_ID());
		processInfo.setAD_PInstance_ID(pInstance.getAD_PInstance_ID());
		ok = processIt(processInfo, trx);

		//	Unlock
		pInstance.setResult(ok ? MPInstance.RESULT_OK : MPInstance.RESULT_ERROR);
		pInstance.setErrorMsg(processInfo.getSummary());
		pInstance.setIsProcessing(false);
		pInstance.saveEx();
		//
		pInstance.log();
		return pInstance;
	}	//	process

	/**
	 * 	Process It (sync)
	 *	@param pi Process Info
	 *	@param trx transaction
	 *	@return true if OK
	 */
	public boolean processIt (ProcessInfo pi, Trx trx)
	{
		if (pi.getAD_PInstance_ID() == 0)
		{
			MPInstance pInstance = new MPInstance (this, pi.getRecord_ID());
			//	Lock
			pInstance.setIsProcessing(true);
			pInstance.saveEx();
		}
		
		boolean ok = false;
		
//		if (getAD_Rule_ID() > 0)
//		{
//			String error = executeRule();
//			if (error.length() > 0)
//			{
//				pi.setSummary(error, ok);	
//				log.warning(error);				
//			}
//			//else
//			//	ok = true;
//			//return ok;
//		}		

		//	Java Class
		String Classname = getClassname();
		if (Classname != null && Classname.length() > 0)
		{
			pi.setClassName(Classname);
			ok = startClass(pi, trx);
		}
		else
		{
			//	PL/SQL Procedure
			String ProcedureName = getProcedureName();
			if (ProcedureName != null && ProcedureName.length() > 0)
			{
				ok = startProcess (ProcedureName, pi, trx);
			}
			else
			{
				String msg = "No Classname or ProcedureName for " + getName();
				pi.setSummary(msg, ok);
				log.warning(msg);
			}
		}
		
		return ok;
	}	//	process

//	private String executeRule() {		
//		String error = "";		
//		if (!rules.containsKey(getAD_Rule_ID()))
//		{
//			MRule startrule = MRule.get(Env.getCtx(), getAD_Rule_ID());
//			if (startrule.isActive())
//			{
//				List<MRule> rulelist = new Query(Env.getCtx(), X_AD_Rule.Table_Name,"AD_Rule_ID = ? OR Parent_ID = ?", null)
//				.setParameters(startrule.getAD_Rule_ID(), startrule.getAD_Rule_ID()).list();
//				if (rulelist != null)
//				{
//					if (getRuleType().equals(X_AD_Rule.RULETYPE_Listener))
//					{
//						HashMap properties = new HashMap();
//						for (MRule rule:rulelist)
//						{
//							String queuename = "";
//							String listenerclass = "";
//							
//							properties = (HashMap) rule.getProperties();
//							String type = (String) properties.get("type");		
//							System.out.println("type->"+type);
//							if (type.equals("kiebase"))
//							{
//								queuename = (String) properties.get("queuename");
//								listenerclass = (String) properties.get("queuelistenerclass");
//							}
//							else if (type.equals("listen"))
//							{
//								queuename = (String) properties.get("queuename");
//								listenerclass = (String) properties.get("queuelistenerclass");				
//							}
//							System.out.println("queuename->"+queuename);
//							System.out.println("listenerclass->"+listenerclass);
//							if (queuename.length() > 0)
//							{
//								System.out.println("agregando Listener "+listenerclass);
//								Env.addlistener(queuename, listenerclass, getAD_Rule_ID());
//							}
//						}												
//						System.out.println("terminado revision de reglas para "+startrule.getName());
////						Listener listener = new Listener(rulelist, getAD_Rule_ID());
////						error = listener.getMessageError();
////						if (error.length() == 0)
////						{
////							rules.put(getAD_Rule_ID(), startrule);
////							//listener.start();
////							Thread lpdThread = new Thread(listener);
////							lpdThread.setName("Listener "+startrule.getName());
////							lpdThread.start();							
////						}
//					}
//					else if (getRuleType().equals(X_AD_Rule.RULETYPE_Acceptor))
//					{
//						Env.setRules(rulelist, getAD_Rule_ID());
////						if (error.length() == 0)
////						{
////							rules.put(getAD_Rule_ID(), startrule);
////						}
//					}
//				}
//			}
//		}
//		return error;
//	}

	/**
	 * 	Is this a Java Process
	 *	@return true if java process
	 */
	public boolean isJavaProcess()
	{
		String Classname = getClassname();
		return (Classname != null && Classname.length() > 0);
	}	//	is JavaProcess
	
	/**
	 *  Start Database Process
	 *  @param ProcedureName PL/SQL procedure name
	 *  @param pInstance process instance
	 *	see ProcessCtl.startProcess
	 *  @return true if success
	 */
	private boolean startProcess (String ProcedureName, ProcessInfo processInfo, Trx trx)
	{
		int AD_PInstance_ID = processInfo.getAD_PInstance_ID();
		//  execute on this thread/connection
		log.info(ProcedureName + "(" + AD_PInstance_ID + ")");
		
		return ProcessUtil.startDatabaseProcedure(processInfo, ProcedureName, trx);
	}   //  startProcess


	/**
	 *  Start Java Class (sync).
	 *      instanciate the class implementing the interface ProcessCall.
	 *  The class can be a Server/Client class (when in Package
	 *  org adempiere.process or org.compiere.model) or a client only class
	 *  (e.g. in org.compiere.report)
	 *
	 *  @param Classname    name of the class to call
	 *  @param pi	process info
	 *  @param trx transaction
	 *  @return     true if success
	 *	see ProcessCtl.startClass
	 */
	private boolean startClass (ProcessInfo pi, Trx trx)
	{
		log.info(pi.getClassName());
		
		return ProcessUtil.startJavaProcess(getCtx(), pi, trx);
	}   //  startClass

	
	/**
	 * 	Is it a Workflow
	 *	@return true if Workflow
	 */
	public boolean isWorkflow()
	{
		return getAD_Workflow_ID() > 0;
	}	//	isWorkflow
	
	
	/**
	 * 	Update Statistics
	 *	@param seconds sec
	 */
	public void addStatistics (int seconds)
	{
		setStatistic_Count(getStatistic_Count() + 1);
		setStatistic_Seconds(getStatistic_Seconds() + seconds);
	}	//	addStatistics
	
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (newRecord)	//	Add to all automatic roles
		{
			MRole[] roles = MRole.getOf(getCtx(), "IsManual='N'");
			for (int i = 0; i < roles.length; i++)
			{
				
				MProcessAccess pa = new MProcessAccess(this, roles[i].getAD_Role_ID());
				pa.saveEx();
			}
		}
		//	Menu/Workflow
		else if (is_ValueChanged("IsActive") || is_ValueChanged("Name") 
			|| is_ValueChanged("Description") || is_ValueChanged("Help"))
		{
			MMenu[] menues = MMenu.get(getCtx(), "AD_Process_ID=" + getAD_Process_ID(), get_TrxName());
			for (int i = 0; i < menues.length; i++)
			{
				menues[i].setIsActive(isActive());
				menues[i].setName(getName());
				menues[i].setDescription(getDescription());
				menues[i].saveEx();
			}
			X_AD_WF_Node[] nodes = MWindow.getWFNodes(getCtx(), "AD_Process_ID=" + getAD_Process_ID(), get_TrxName());
			for (int i = 0; i < nodes.length; i++)
			{
				boolean changed = false;
				if (nodes[i].isActive() != isActive())
				{
					nodes[i].setIsActive(isActive());
					changed = true;
				}
				if (nodes[i].isCentrallyMaintained())
				{
					nodes[i].setName(getName());
					nodes[i].setDescription(getDescription());
					nodes[i].setHelp(getHelp());
					changed = true;
				}
				if (changed)
					nodes[i].saveEx();
			}
		}
		return success;
	}	//	afterSave
	
	/**
	 * Grant independence to GenerateModel from AD_Process_ID
	 * @param value
	 * @param trxName
	 * @return
	 */
	public static int getProcess_ID(String value, String trxName)
	{
		int retValue = DB.getSQLValueEx(trxName, "SELECT AD_Process_ID FROM AD_Process WHERE Value=?", value);
		return retValue;
	}
	
	/**
	 * Copy settings from another process
	 * overwrites existing data
	 * (including translations)
	 * and saves.
	 * Not overwritten: name, value, entitytype
	 * @param source 
	 */
	public void copyFrom (MProcess source)
	{

		log.log(Level.FINE, "Copying from:" + source + ", to: " + this);
		setAccessLevel(source.getAccessLevel());
		setAD_Form_ID(source.getAD_Form_ID());
		setAD_PrintFormat_ID(source.getAD_PrintFormat_ID());
		setAD_ReportView_ID(source.getAD_ReportView_ID());
		setAD_Workflow_ID(source.getAD_Workflow_ID());
		setClassname(source.getClassname());
		setDescription(source.getDescription());
		setHelp(source.getHelp());
		setIsBetaFunctionality(source.isBetaFunctionality());
		setIsDirectPrint(source.isDirectPrint());
		setIsReport(source.isReport());
		setIsServerProcess(source.isServerProcess());
		setJasperReport(source.getJasperReport());
		setProcedureName(source.getProcedureName());
		setShowHelp(source.getShowHelp());
		
		saveEx();
		
		// copy parameters 
		// TODO? Perhaps should delete existing first?
		MProcessPara[] parameters = source.getParameters();
		for ( MProcessPara sourcePara : parameters )
		{
			MProcessPara targetPara = new MProcessPara(this);
			targetPara.copyFrom (sourcePara);  // saves automatically
		}
	}

	/**
	 * 	Process It without closing the given transaction - used from workflow engine.
	 *	@param pi Process Info
	 *	@param trx transaction
	 *	@return true if OK
	 */
	public boolean processItWithoutTrxClose (ProcessInfo pi, Trx trx)
	{
		if (pi.getAD_PInstance_ID() == 0)
		{
			MPInstance pInstance = new MPInstance (this, pi.getRecord_ID());
			//	Lock
			pInstance.setIsProcessing(true);
			pInstance.save();
		}

		boolean ok = false;

		//	Java Class
		String Classname = getClassname();
		if (Classname != null && Classname.length() > 0)
		{
			pi.setClassName(Classname);
			ok = startClassWithoutTrxClose(pi, trx);
		}
		else
		{
			//	PL/SQL Procedure
			String ProcedureName = getProcedureName();
			if (ProcedureName != null && ProcedureName.length() > 0)
			{
				ok = startProcess (ProcedureName, pi, trx);
			}
			else
			{
				String msg = "No Classname or ProcedureName for " + getName();
				pi.setSummary(msg, ok);
				log.warning(msg);
			}
		}
		
		return ok;
	}	//	processItWithoutTrxClose
	
	/**
	 *  Start Java Class (sync) without closing the given transaction.
	 *      instanciate the class implementing the interface ProcessCall.
	 *  The class can be a Server/Client class (when in Package
	 *  org adempiere.process or org.compiere.model) or a client only class
	 *  (e.g. in org.compiere.report)
	 *
	 *  @param Classname    name of the class to call
	 *  @param pi	process info
	 *  @param trx transaction
	 *  @return     true if success
	 *	see ProcessCtl.startClass
	 */
	private boolean startClassWithoutTrxClose (ProcessInfo pi, Trx trx)
	{
		log.info(pi.getClassName());
		return ProcessUtil.startJavaProcessWithoutTrxClose(getCtx(), pi, trx);
	}   //  startClassWithoutTrxClose


	public static MProcess getbyID(String ProcessID) {
		String whereClause = "AD_Process_ID = "+ProcessID;
		MProcess p = new Query(Env.getCtx(), MProcess.Table_Name, whereClause, null)
		.first();
		return p;
	}

	public static MProcess getbyClassName(String classname) {
		String whereClause = "classname = ?";
		MProcess p = null;
		System.out.println(whereClause+classname);
		 p = new Query(Env.getCtx(), MProcess.Table_Name, whereClause, null)
		.setParameters(classname)
		.first();
		return p;
	}	
	public static MProcess getbyClassName(String classname, String jaspername) {
		String whereClause = "classname = ?";
		MProcess p = null;
		if (jaspername.length() > 0)	
		{
			whereClause += " AND jasperreport = ?";
			 p = new Query(Env.getCtx(), MProcess.Table_Name, whereClause, null)
			.setParameters(classname, jaspername)
			.first();
		}
		else
		{
			System.out.println(whereClause+classname);
			 p = new Query(Env.getCtx(), MProcess.Table_Name, whereClause, null)
			.setParameters(classname)
			.first();
		}
		return p;
	}

	public static MProcess getbyReportViewName(String reportviewname) {
		String whereClause = "name = ?";
		MProcess p = null;
		MReportView r = new Query(Env.getCtx(), MReportView.Table_Name, whereClause, null)
		.setParameters(reportviewname)
		.first();
		if (r != null)
		{
			whereClause = "AD_ReportView_ID = ? ";
			p = new Query(Env.getCtx(), MProcess.Table_Name, whereClause, null)
			.setParameters(r.getAD_ReportView_ID())
			.first();			
		}
		return p;
	}		
	public static MProcess getbyJasperName(String jaspername) {
		// extract the name
		int slash = jaspername.lastIndexOf("/");
		int dot   = jaspername.lastIndexOf(".");
		if (slash > 0 && dot > 0)
		{
			jaspername = jaspername.substring(slash + 1, dot );
		}
		String whereClause = "jasperreport like '%"+jaspername+"%'";
		MProcess p = null;			
		p = new Query(Env.getCtx(), MProcess.Table_Name, whereClause, null)
		.first();			
		return p;
	}

	public static MProcess getbyValue(String value) {
		String whereClause = "value = ?";
		MProcess p = new Query(Env.getCtx(), MProcess.Table_Name, whereClause, null)
		.setParameters(value)
		.first();
		if (p != null)
		{
			s_cache.put (p.get_ID(), p);
		}
		return p;
	}
	
	public static MProcess getbyIdentifier(String identifier) {
		String whereClause = "identifier = ?";
		MProcess p = new Query(Env.getCtx(), MProcess.Table_Name, whereClause, null)
		.setParameters(identifier)
		.first();
		if (p != null)
		{
			s_cache.put (p.get_ID(), p);
		}
		return p;
	}
	
}	//	MProcess