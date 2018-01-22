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

import java.util.*;

/**
 *  Base Resource Bundle
 *
 * 	@author 	Eldir Tomassen
 * 	@version 	$Id: ALoginRes_nl.java 508 2007-11-24 23:06:53Z el_man $
 */
public final class ALoginRes_nl extends ListResourceBundle
{
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Verbinding" },
	{ "Defaults",           "Standaard" },
	{ "Login",              "Aanmelden bij Xendra" },
	{ "File",               "Bestand" },
	{ "Exit",               "Afsluiten" },
	{ "Help",               "Help" },
	{ "About",              "Info" },
	{ "Host",               "Server" },
	{ "Database",           "Database" },
	{ "User",               "Gebruikersnaam" },
	{ "EnterUser",          "Voer uw gebruikersnaam in" },
	{ "Password",           "Wachtwoord" },
	{ "EnterPassword",      "Voer uw wachtwoord in" },
	{ "Language",           "Taal" },
	{ "SelectLanguage",     "Selecteer uw taal" },
	{ "Role",               "Rol" },
	{ "Client",             "Client" },
	{ "Organization",       "Organisatie" },
	{ "Date",               "Datum" },
	{ "Warehouse",          "Magazijn" },
	{ "Printer",            "Printer" },
	{ "Connected",          "Verbonden" },
	{ "NotConnected",       "Niet verbonden" },
	{ "DatabaseNotFound",   "Database niet gevonden" },
	{ "UserPwdError",       "Foute gebruikersnaam of wachtwoord" },
	{ "RoleNotFound",       "Rol niet gevonden of incompleet" },
	{ "Authorized",         "Geautoriseerd" },
	{ "Ok",                 "Ok" },
	{ "Cancel",             "Annuleren" },
	{ "VersionConflict",    "Versie Conflict:" },
	{ "VersionInfo",        "Server <> Client" },
	{ "PleaseUpgrade",      "Uw Xendra installatie dient te worden bijgewerkt." }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes
