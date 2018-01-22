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
package org.compiere.apps;

import org.compiere.process.*;
import org.compiere.util.*;

/**
 *  ASync Process Base Class
 *
 *  @author     Jorg Janke
 *  @version    $Id: ASyncProcessBase.java 508 2007-11-24 23:06:53Z el_man $
 */
public abstract class ASyncProcessBase implements ASyncProcess
{
	/**
	 *  Constructor
	 *  @param pi process info
	 */
	public ASyncProcessBase(ProcessInfo pi)
	{
		m_pi = pi;
	}   //  ASyncProcessBase

	private ProcessInfo m_pi;
	private boolean     m_isLocked = false;
	private Splash      m_splash;

	/**
	 *  Start ASync Worker
	 */
	void start()
	{
		if (isUILocked())   //  don't start twice
			return;
		ASyncWorker worker = new ASyncWorker (this, m_pi);
		worker.start();     //  calls lockUI, executeASync, unlockUI
	}   //  start

	/**
	 *  Lock User Interface.
	 *  Called from the Worker before processing
	 *  @param pi process info
	 */
	public void lockUI (ProcessInfo pi)
	{
		m_isLocked = true;
		m_splash = new Splash (Msg.getMsg(Env.getCtx(), "Processing"));
		m_splash.toFront();
	}   //  lockUI

	/**
	 *  Unlock User Interface.
	 *  Called from the Worker when processing is done
	 *  @param pi process info
	 */
	public void unlockUI (ProcessInfo pi)
	{
		m_isLocked = false;
		m_splash.dispose();
		m_splash = null;
	}   //  unlockUI

	/**
	 *  Is the UI locked (Internal method)
	 *  @return true, if UI is locked
	 */
	public boolean isUILocked()
	{
		return m_isLocked;
	}   //  isLoacked

	/**
	 *  Method to be executed async
	 *  Called from the Worker
	 *  @param pi process info
	 */
	public abstract void executeASync (ProcessInfo pi);

}   //  ASyncProcessBase
