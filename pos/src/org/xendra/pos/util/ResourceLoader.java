//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.xendra.pos.util;

import org.columba.core.resourceloader.GlobalResourceLoader;


public class ResourceLoader extends GlobalResourceLoader {
    protected static final String POS_RESOURCE_PATH = "org.xendra.pos.i18n";

    /**
 * No need for creating instances of this class.
 */
    private ResourceLoader() {
    }

    //This is used when package name and file name are different.
    //Example: getString("dialog", "composer", "statusbar_label");
    public static String getString(String sPath, String sName, String sID) {
        return GlobalResourceLoader.getString(generateBundlePath(
                POS_RESOURCE_PATH, sPath), sName, sID);
    }

    //This one is used when the package name and the file name are the same.
    //Example: getString("action", "menu_folder_newfolder");
    public static String getString(String sName, String sID) {
        return GlobalResourceLoader.getString(generateBundlePath(
                POS_RESOURCE_PATH, sName), sName, sID);
    }
}
