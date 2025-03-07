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
package org.columba.core.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.compiere.util.CLogger;


public class StreamThread extends Thread {

    private static final CLogger LOG = CLogger.getCLogger("org.columba.core.base"); //$NON-NLS-1$

    InputStream is;
    String type;
    StringBuffer buf;

    public StreamThread(InputStream theInputStream, String theType) {
        this.is = theInputStream;
        this.type = theType;

        buf = new StringBuffer();
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;

            while ((line = br.readLine()) != null) {
                LOG.fine(type + ">" + line); //$NON-NLS-1$
                buf.append(line + "\n"); //$NON-NLS-1$
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String getBuffer() {
        return buf.toString();
    }
}
