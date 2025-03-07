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
package org.apache.ecs;

/**
    This interface is intended to be implemented by elements that require
    javascript mouse event attributes.

    @version $Id: MouseEvents.java,v 1.1 2007/06/14 23:42:10 sergioaguayo Exp $
    @author <a href="mailto:snagy@servletapi.com">Stephan Nagy</a>
    @author <a href="mailto:jon@clearink.com">Jon S. Stevens</a>
*/
public interface MouseEvents
{
    /**
        make sure implementing classes have a setOnClick method.

        The onclick event occurs when the pointing device button is clicked
        over an element. This attribute may be used with most elements.
    */
    public abstract void setOnClick(String script);

    /**
        make sure implementing classes have a setOnDblClick method.

        The ondblclick event occurs when the pointing device button is double
        clicked over an element. This attribute may be used with most elements.
    */
    public abstract void setOnDblClick(String script);

    /**
        make sure implementing classes have a setOnMouseDown method.

        The onmousedown event occurs when the pointing device button is pressed
        over an element. This attribute may be used with most elements.
    */
    public abstract void setOnMouseDown(String script);

    /**
        make sure implementing classes have a setOnMouseUp method.

        The onmouseup event occurs when the pointing device button is released
        over an element. This attribute may be used with most elements.
    */
    public abstract void setOnMouseUp(String script);

    /**
        make sure implementing classes have a setOnMouseOver method.

        The onmouseover event occurs when the pointing device is moved onto an
        element. This attribute may be used with most elements.
    */
    public abstract void setOnMouseOver(String script);

    /**
        make sure implementing classes have a setOnMouseMove method.

        The onmousemove event occurs when the pointing device is moved while it
        is over an element. This attribute may be used with most elements.
    */
    public abstract void setOnMouseMove(String script);

    /**
        make sure implementing classes have a setOnMouseOut method.

        The onmouseout event occurs when the pointing device is moved away from
        an element. This attribute may be used with most elements.
    */
    public abstract void setOnMouseOut(String script);
}
