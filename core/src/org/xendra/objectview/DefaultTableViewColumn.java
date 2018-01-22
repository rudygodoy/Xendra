package org.xendra.objectview;

import java.awt.Component;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.Format;
import java.util.Comparator;

import org.xendra.objectview.RowBase;


/**
 * This class provides default implementations for the <code>TableViewColumn</code> interface.
 *
 * @author St�phane Brunner, Last modified by: $Author: stbrunner $ 
 * @version $Revision: 1.12 $ $Date: 2004/09/05 19:22:16 $.
 * Revision history:
 * $Log: DefaultTableViewColumn.java,v $
 * Revision 1.12  2004/09/05 19:22:16  stbrunner
 * add select by first letter
 *
 * Revision 1.11  2004/05/31 14:39:13  stbrunner
 * *** empty log message ***
 *
 * Revision 1.10  2004/02/15 19:29:42  stbrunner
 * *** empty log message ***
 *
 * Revision 1.9  2003/06/06 06:31:32  stbrunner
 * Change description
 *
 */
public class DefaultTableViewColumn extends AbstractTableViewColumn {
    private final String mName;
    private final java.lang.reflect.Method mGetter;
    private final java.lang.reflect.Method mSetter;
    private java.lang.reflect.Method mGetDisplay;
    private java.lang.reflect.Method mSetDisplay;
    private final Comparator mComparator;
    private final boolean mDefaultVisible;
    private boolean mColorColumn;
    private final java.text.Format mFormat;
    private final Class mColumnClass;
    private boolean mSortable;
    private boolean mSearchable;
    //
    private boolean mKey;
    private boolean mDescription;
    private boolean mNextLine;
    private boolean mDefault;
    private Integer mAD_Column_ID;
	private Integer mDisplayType;
	private boolean m_IsOverWrite;
	
    /**
     * Constructor declaration.
     * @param pName column name
     */
    public DefaultTableViewColumn(String pName) {
        this(pName, null);
    }

    /**
     * Constructor declaration.
     * @param pName column name
     * @param pGetter the getter method
     */
    public DefaultTableViewColumn(String pName, Method pGetter) {
        this(pName, pGetter, null);
    }

    /**
     * Constructor declaration
     *
     * @param pName column name
     * @param pGetter the getter method
     * @param pSetter the setter method
     */
    public DefaultTableViewColumn(String pName, Method pGetter, Method pSetter) {
        this(pName, null, pGetter, pSetter);
    }

    /**
     * Constructor declaration
     *
     * @param pName column name
     * @param pComparator the comparator of rows Objects for the current column
     * @param pGetter the getter method
     * @param pSetter the setter method
     */
    public DefaultTableViewColumn(String pName, Comparator pComparator,
            Method pGetter, Method pSetter) {
        this(pName, pComparator, pGetter, pSetter, true);
    }

    /**
     * Constructor declaration
     * @param pName column name
     * @param pComparator the comparator of rows Objects for the current column
     * @param pGetter the getter method
     * @param pSetter the setter method
     * @param pDefaultVisible the default visiblity
     */
    public DefaultTableViewColumn(String pName, Comparator pComparator,
            Method pGetter, Method pSetter, boolean pDefaultVisible) {
        this(pName, pComparator, pGetter, pSetter, pDefaultVisible, null, false, 0, 0);
    }

    /**
     * Constructor declaration
     * @param pName column name
     * @param pGetter the getter method
     * @param pSetter the setter method
     * @param pDefaultVisible the default visiblity
     * @param pFormat the formater
     */
    public DefaultTableViewColumn(String pName, Method pGetter, Method pSetter,
            boolean pDefaultVisible, Format pFormat) {
        this(pName, null, pGetter, pSetter, true, pFormat, false, 0, 0);
    }

    /**
     * Constructor declaration
     * @param pName column name
     * @param pGetter the getter method
     * @param pSetter the setter method
     * @param pDefaultVisible the default visiblity
     */
    public DefaultTableViewColumn(String pName, Method pGetter, Method pSetter,
            boolean pDefaultVisible) {
        this(pName, null, pGetter, pSetter,  pDefaultVisible, null, false, 0, 0);
    }

    /**
     * Constructor declaration<br>
     * column class = String if format not null else return type of getter
     * sortable and searchable = true is getter return type is comparable.
     *
     * @param pName column name
     * @param pComparator the comparator of rows Objects for the current column (if null create a default - use comparable
     * interface)
     * @param pGetter the getter method (if null the getValue must be redefine)
     * @param pSetter the setter method (if null isn't editable)
     * @param pDefaultVisible the default visiblity
     * @param pFormat the formater
     */
    public DefaultTableViewColumn(String pName, Comparator pComparator,
            Method pGetter, Method pSetter,  boolean pDefaultVisible,
            Format pFormat, boolean pIsColorColumn, Integer AD_Column_ID, Integer DisplayType) {
        Class get = pGetter == null ? Object.class : pGetter.getReturnType();
        mFormat = pFormat != null ? pFormat : getFormat(get);
        mColumnClass = mFormat != null ? String.class : get;
        mComparator = pComparator != null ? pComparator : getComparator(get);
        mDefaultVisible = pDefaultVisible;
        mColorColumn = pIsColorColumn;
        mName = pName;
        mGetter = pGetter;
        mSetter = pSetter;
        mSortable = true; //Comparable.class.isAssignableFrom(get) || comparator != null;
        mSearchable = mSortable;
        mAD_Column_ID = AD_Column_ID;
        mDisplayType = DisplayType;
    }

    public DefaultTableViewColumn(String pName, Comparator pComparator,
            Method pGetter, Method pSetter,  boolean pDefaultVisible,
            Format pFormat, boolean pSortable, boolean pSearchable,  boolean pIsColorColumn, Integer AD_Column_ID, Integer DisplayType) {
        this(pName, pComparator, pGetter, pSetter,  pDefaultVisible, pFormat, pIsColorColumn, AD_Column_ID, DisplayType);

        mSortable = pSortable;
        mSearchable = pSearchable;
    }   
    
    /**
     * Constructor declaration
     * @param pName column name
     * @param pComparator the comparator of rows Objects for the current column
     * @param pGetter the getter method
     * @param pSetter the setter method
     * @param pDefaultVisible the default visiblity
     * @param pFormat the formater
     * @param pSortable is searchable
     * @param pSearchable is sortable
     */
    public DefaultTableViewColumn(String pName, Comparator pComparator,
            Method pGetter, Method pSetter,  boolean pDefaultVisible,
            Format pFormat, boolean pSortable, boolean pSearchable, 
            boolean pIsKey, boolean pDescription, boolean pNextLine, boolean pDefault, 
            boolean pIsColorColumn, Integer AD_Column_ID, Integer DisplayType, Method pGetDisplay, Method pSetDisplay, boolean pOverwrite) {
        this(pName, pComparator, pGetter, pSetter,  pDefaultVisible, pFormat, pIsColorColumn, AD_Column_ID, DisplayType);

        mKey = pIsKey;
        mDescription = pDescription;
        mNextLine = pNextLine;
        mDefault = pDefault;
        mSortable = pSortable;
        mSearchable = pSearchable;
        mGetDisplay = pGetDisplay;
        mSetDisplay = pSetDisplay;
        m_IsOverWrite = pOverwrite;        
    }

    /**
     * Returns the column name.
     * @return the column name
     */
    public String getName() {
        return mName;
    }
    
    public Integer getDisplayType() {
    	return mDisplayType;
    }

    public Integer getAD_Column_ID() {
    	return mAD_Column_ID;
    }
    /**
     * Returns the column class.
     * @return the column class
     */
    public Class getColumnClass() {
        return mColumnClass;
    }

    /**
     * Returns the column comparator.
     * @return the column comparator
     */
    public Comparator getComparator() {
        return mComparator;
    }

    /**
     * Create default formatter, if getter class is Date use DateFormat.getDateTimeInstance() formatter
     * @param pGet getter class
     * @return the new formatter
     */
    private Format getFormat(Class pGet) {
        if (java.util.Date.class.isAssignableFrom(pGet)) {
            return java.text.DateFormat.getDateTimeInstance();
        }
        return null;
    }

    /**
     * Create default comparator
     * @param pGet getter class
     * @return the new comparator
     */
    private Comparator getComparator(Class pGet) {
        if (Comparable.class.isAssignableFrom(pGet)) {
            return new Comparator() {
                /**
                 * Compares its two arguments for order.
                 *
                 * @param pO1 the first object to be compared.
                 * @param pO2 the second object to be compared.
                 * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to,
                 *    or greater than the second.
                 */
                public int compare(Object pO1, Object pO2) {
                    try {
                        pO1 = mGetter.invoke(pO1, null);
                        pO2 = mGetter.invoke(pO2, null);
                        if (pO2 == null && pO1 == null) {
                            return 0;
                        }
                        if (pO2 == null) {
                            return -1;
                        }
                        if (pO1 == null) {
                            return 1;
                        }
                        return ((Comparable) pO1).compareTo(pO2);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }

            };
        }
        else {
            return new Comparator() {
                /**
                 * Compares its two arguments for order.
                 * @param pO1 the first object to be compared.
                 * @param pO2 the second object to be compared.
                 * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to,
                 *    or greater than the second.
                 */
                public int compare(Object pO1, Object pO2) {
                    return String.valueOf(getValue(pO1)).compareTo(
                            String.valueOf(getValue(pO2)));
                }

            };
        }
    }

    /**
     * Return the cell value.
     * @param pRowObject the row Object
     * @return the cell value
     */
    public Object getValue(Object pRowObject) {
        try {
        	if (pRowObject == null)
        		return null;
            Object value = mGetter == null ? pRowObject : mGetter.invoke(pRowObject, null);
            return mFormat == null ? value : mFormat.format(value);
        }
        catch (Exception e) {
            e.printStackTrace();
            return pRowObject;
        }
    }

    /**
     * Set the cell value (use formatter).
     * @param pRowObject the row Object
     * @param pValue the new value
     */
    public void setValue(Object pRowObject, Object pValue) {
        try {
            pValue = mFormat == null ? pValue : (Object) mFormat.parseObject(String.valueOf(pValue).toString());        	       
//            if (pValue instanceof Number)
//            	if ( mSetter.getParameterTypes().equals(BigDecimal.class) )
//            		pValue = new BigDecimal (((Number)pValue).doubleValue());
            if (pValue instanceof Number)
            {
				for (Class<?> t : mSetter.getParameterTypes()) {
		            	if ( t.equals(BigDecimal.class) )
		            	{
		            		pValue = new BigDecimal (((Number)pValue).doubleValue());		            		
		            	}
				}
            }
//            pValue = mFormat == null ? pValue : mFormat.format(String
//                    .valueOf(pValue));            
            mSetter.invoke(pRowObject, new Object[]{pValue});                        
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the default visibility.
     * @return the default visibility.
     */
    public boolean isDefaultVisible() {
        return mDefaultVisible;
    }

    /**
     * The sortable status.
     * @return true if the column is sortable
     */
    public boolean isSortable() {
        return mSortable;
    }

    /**
     * The searchable status.
     * @return true if the column is searchable
     */
    public boolean isSearchable() {
        return mSearchable;
    }

    /**
     * The key status.
     * @return true if the column is the key
     */
    public boolean isKey() {
        return mKey;
    }
    
    public boolean IsDescription() {
    	return mDescription;
    }
    
    public boolean IsNextLine() {
    	return mNextLine;
    }
    
    public boolean IsDefault() {
    	return mDefault;
    }
    /**
     * Returns setter not null.  This is the default implementation for all cells.
     * @param  pRowObject  the row being queried
     * @return the sediteble status
     */
    public boolean isCellEditable(Object pRowObject) {
    	Boolean enabled = false;
    	if (pRowObject != null && ((RowBase) pRowObject).IsEnabled())
        	enabled = mSetter != null;
    	return enabled;
    }
    
	public boolean isColorColumn() {
		return mColorColumn;
	}

	public void setColorColumn(boolean colorColumn) {
		mColorColumn = colorColumn;
	}

	public boolean isOverWrite() {
		return m_IsOverWrite;
	}
	
	public void setOverWrite(boolean overwrite) {
		m_IsOverWrite = overwrite;
	}
	
	public Object getDisplay(Object pRowObject, Integer col, Object value) {
        try {
        	if (pRowObject == null)
        		return null;
            Object retvalue = mGetDisplay == null ? null : mGetDisplay.invoke(pRowObject, new Object[]{col, value});
            return retvalue;
        }
        catch (Exception e) {
            e.printStackTrace();
            return pRowObject;
        }		
	}
	public Object setDisplay(Object pRowObject, Integer col, Object pValue, Object pDisplayValue) {
        try {
        	if (pRowObject == null)
        		return null;
            Object value = mSetDisplay == null ? pRowObject : mSetDisplay.invoke(pRowObject, new Object[]{col, pValue, pDisplayValue});
            return value;
        }
        catch (Exception e) {
            e.printStackTrace();
            return pRowObject;
        }		
	}
}