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

import java.applet.*;
import java.awt.*;
import org.compiere.*;
import org.compiere.util.*;


/**
 *	Application Applet
 *	
 *  @author Jorg Janke
 *  @version $Id: AApplet.java 508 2007-11-24 23:06:53Z el_man $
 */
public class AApplet extends Applet
{

	/**
	 * 	Adempiere Application Applet
	 *	@throws java.awt.HeadlessException
	 */
	public AApplet () throws HeadlessException
	{
		super ();
	}	//	AApplet

	
	/**************************************************************************
	 * 	init
	 */
	public void init ()
	{
		super.init ();
		TextArea ta = new TextArea(Xendra.getSummary());
		add (ta);
	}	//	init
	
	/**
	 * 	start
	 */
	public void start ()
	{
		super.start ();
		showStatus(Xendra.getSummary());
		//
		Splash splash = Splash.getSplash();
		Xendra.startup(true);	//	needs to be here for UI
		AMenu menu = new AMenu();
	}	//	start
	
	/**
	 * 	stop
	 */
	public void stop ()
	{
		super.stop ();
	}	//	stop
	
	/**
	 * 	destroy
	 */
	public void destroy ()
	{
		super.destroy ();
		Env.exitEnv(0);
	}	//	destroy
	
}	//	AApplet
