package fi.sardion.dctm.deploy.objectmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.documentum.fc.common.IDfAttr;
import com.rsa.certj.provider.pki.URLDecoder;
import com.rsa.certj.spi.pki.PKIException;

/**
 * An attribute sub task for object type. <b>NOTE:</b> attribute task tags are
 * order specific so that attributes that are more repository specific are
 * defined first.
 * <p>
 * <ul>
 * <li>Created: 29 Sep 2010 10:23:27</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.objectmodel.Attribute</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public class Attribute extends Task {

    /**
     * <code>BOOLEAN = "boolean";</code>
     * <ul>
     * <li>Created: 19 Sep 2010 11:26:32</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public static final String BOOLEAN = "boolean"; //$NON-NLS-1$
    /**
     * <code>DOUBLE = "double";</code>
     * <ul>
     * <li>Created: 19 Sep 2010 11:31:50</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public static final String DOUBLE = "double"; //$NON-NLS-1$
    /**
     * <code>ID = "id";</code>
     * <ul>
     * <li>Created: 19 Sep 2010 11:32:07</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public static final String ID = "id"; //$NON-NLS-1$
    /**
     * <code>INTEGER = "integer";</code>
     * <ul>
     * <li>Created: 19 Sep 2010 11:32:22</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public static final String INTEGER = "integer"; //$NON-NLS-1$
    /**
     * <code>STRING = "string";</code>
     * <ul>
     * <li>Created: 19 Sep 2010 11:32:30</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public static final String STRING = "string"; //$NON-NLS-1$
    /**
     * <code>TIME = "time";</code>
     * <ul>
     * <li>Created: 19 Sep 2010 11:32:47</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public static final String TIME = "time"; //$NON-NLS-1$
    /**
     * Default search operations for the different data types.
     * <code>DEFAULT_OPS = new HashMap<String, String>(6);</code>
     * <ul>
     * <li>Created: 19 Sep 2010 18:09:48</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private static final Map<String, String> DEFAULT_OPS = new HashMap<String, String>(
	    6);
    /**
     * <code>TEXT_SEARCH_OPS = Arrays
	    .asList(new String[] { "=", "<>", ">", "<", ">=", "<=", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		    "begins with", "contains", "does not contain", "ends with", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		    "in", "not in", "between", "is null", "is not null", "not" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$</code>
     * <ul>
     * <li>Created: 19 Sep 2010 18:10:39</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private static final List<String> TEXT_SEARCH_OPS = Arrays
	    .asList(new String[] { "=", "<>", ">", "<", ">=", "<=", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	            "begins with", "contains", "does not contain", "ends with", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	            "in", "not in", "between", "is null", "is not null", "not" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
    /**
     * Initialise the default search operations as follows:
     * <ul>
     * <li>BOOLEAN: =,<>,is null,is not null</li>
     * <li>DOUBLE: =,<>,>,<,>=,<=,in,not in,between,is null,is not null</li>
     * <li>ID: =,<>,is null,is not null</li>
     * <li>INTEGER: =,<>,>,<,>=,<=,in,not in,between,is null,is not null</li>
     * <li>STRING: =,begins with,contains,does not contain,ends with,is null,is
     * not null</li>
     * <li>TIME: =,<>,>,<,>=,<=,between,is null,is not null</li>
     * </ul>
     * 
     * @since %since%
     */
    static {
	Attribute.DEFAULT_OPS
	        .put(Attribute.BOOLEAN, "=,<>,is null,is not null"); //$NON-NLS-1$
	Attribute.DEFAULT_OPS.put(Attribute.DOUBLE,
	        "=,<>,>,<,>=,<=,in,not in,between,is null,is not null"); //$NON-NLS-1$
	Attribute.DEFAULT_OPS.put(Attribute.ID, "=,<>,is null,is not null"); //$NON-NLS-1$
	Attribute.DEFAULT_OPS.put(Attribute.INTEGER,
	        "=,<>,>,<,>=,<=,in,not in,between,is null,is not null"); //$NON-NLS-1$
	Attribute.DEFAULT_OPS
	        .put(Attribute.STRING,
	                "=,begins with,contains,does not contain,ends with,is null,is not null"); //$NON-NLS-1$
	Attribute.DEFAULT_OPS.put(Attribute.TIME,
	        "=,<>,>,<,>=,<=,between,is null,is not null"); //$NON-NLS-1$
    }
    /**
     * Should caching be allowed in the value assistance query.
     * <ul>
     * <li>Created: 19 Sep 2010 18:14:37</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private boolean allowCaching = false;
    /**
     * The data type of the attribute.
     * <ul>
     * <li>Created: 19 Sep 2010 18:15:16</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String dataType = null;
    /**
     * The default value for the attribute.
     * <ul>
     * <li>Created: 19 Sep 2010 18:15:49</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String defaultValue = null;
    /**
     * The value assistance dql query.
     * <ul>
     * <li>Created: 19 Sep 2010 18:16:15</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String dql = null;
    private char[] illegalCharacters = null;
    /**
     * Is the value assistance list complete.
     * <ul>
     * <li>Created: 19 Sep 2010 18:16:46</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private int isComplete = -1;
    /**
     * Is the attribute hiddedn.
     * <ul>
     * <li>Created: 19 Sep 2010 18:17:03</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private int isHidden = -1;
    /**
     * Is the attribute repeating.
     * <ul>
     * <li>Created: 19 Sep 2010 18:17:19</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private int isRepeating = -1;
    /**
     * Is a value required for this attribute.
     * <ul>
     * <li>Created: 19 Sep 2010 18:17:36</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private int isRequired = -1;
    /**
     * Should this attribute be displayed in the advanced search dialog.
     * <ul>
     * <li>Created: 19 Sep 2010 18:17:58</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private int isSearchable = -1;
    /**
     * The label for this attribute.
     * <ul>
     * <li>Created: 19 Sep 2010 18:18:35</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String label = null;
    /**
     * The length of the attribute. Used only if the data type is string.
     * <ul>
     * <li>Created: 19 Sep 2010 18:18:51</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private int length = -1;
    /**
     * The name of the attribute.
     * <ul>
     * <li>Created: 19 Sep 2010 18:19:34</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String name = null;
    /**
     * Whether this task was created automatically or by the
     * {@link fi.sardion.dctm.deploy.objectmodel.Type objecttype} task. Value is
     * true if task is initialised directly by ant false if initialised by
     * {@link fi.sardion.dctm.deploy.objectmodel.Type objecttype} task.
     * <ul>
     * <li>Created: 19 Sep 2010 18:21:19</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private boolean notAutomatic = true;
    /**
     * Is the attribute read only.
     * <ul>
     * <li>Created: 19 Sep 2010 18:26:24</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private int readOnly = -1;
    /**
     * A list of repositories where these attribute data dictionary settings
     * apply. If the list is empty values are applied to all repositories.
     * <ul>
     * <li>Created: 19 Sep 2010 18:26:58</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private List<String> repositories = new ArrayList<String>();
    /**
     * Search operators for this attribute.
     * <ul>
     * <li>Created: 19 Sep 2010 18:41:37</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private List<Integer> searchOps = null;

    /**
     * Default constructor used by ant.
     * <ul>
     * <li>Created: 19 Sep 2010 18:42:04</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Attribute() {
	super();
    }

    /**
     * Constructor used by the {@link fi.sardion.dctm.deploy.objectmodel.Type
     * objecttype} task.
     * <ul>
     * <li>Created: 19 Sep 2010 18:42:28</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param isAutomatic value set to true when calling from
     *            {@link fi.sardion.dctm.deploy.objectmodel.Type objecttype}
     *            task.
     * @since %since%
     */
    protected Attribute(final boolean isAutomatic) {
	this();
	this.notAutomatic = !isAutomatic;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object attribute) {
	return toString().equals(attribute.toString());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	return toString().hashCode();
    }

    /**
     * Set whether to allow caching in value assistance queries. String value
     * passed in is transformed to a boolean using
     * <code>Boolean.parseBoolean(String);</code>
     * <ul>
     * <li>Created: 19 Sep 2010 20:43:48</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param allow caching.
     * @since %since%
     */
    public void setAllowcaching(final String allow) {
	this.allowCaching = Boolean.parseBoolean(allow);
    }

    /**
     * Set the allowed search operators. This is given as a comma (,) separated
     * list which is turned into a list using
     * <code>Arrays.asList(theSearchOps.split(","));</code> after which each
     * element is converted to an integer. If this converting fails it is
     * investigated whether the element matches one of the
     * {@link #TEXT_SEARCH_OPS} values and its corresponding int value is used.
     * <ul>
     * <li>Created: 19 Sep 2010 20:44:32</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param theSearchOps the search operations that can be used with this
     *            attribute.
     * @since %since%
     */
    public void setAllowedsearchops(final String theSearchOps) {
	final List<String> tempOps = Arrays.asList(theSearchOps.split(",")); //$NON-NLS-1$
	this.searchOps = new ArrayList<Integer>(tempOps.size());
	for (String op : tempOps) {
	    op = op.trim();
	    try {
		final Integer searchOperator = new Integer(op);
		if (!this.searchOps.contains(searchOperator)) {
		    this.searchOps.add(searchOperator);
		}
	    } catch (final NumberFormatException nfex) {
		for (int index = 0, count = Attribute.TEXT_SEARCH_OPS.size(); index < count; index++) {
		    if (Attribute.TEXT_SEARCH_OPS.get(index).equalsIgnoreCase(
			    op)) {
			final Integer searchOperator = new Integer(index + 1);
			if (!this.searchOps.contains(searchOperator)) {
			    this.searchOps.add(searchOperator);
			}
		    }
		}
	    }
	}
    }

    /**
     * Set the label to be used with this attribute.
     * <ul>
     * <li>Created: 21 Sep 2010 09:13:27</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aLabel the attribute label.
     * @since %since%
     */
    public void setAttributelabel(final String aLabel) {
	this.label = aLabel;
    }

    /**
     * Set the data type of this attribute. Value must be the integer value of
     * the type or one of the following: {@link Attribute#BOOLEAN boolean},
     * {@link Attribute#DOUBLE double}, {@link Attribute#ID id} ,
     * {@link Attribute#INTEGER integer}, {@link Attribute#STRING} or
     * {@link Attribute#TIME time}. If these conditions aren't met an exception
     * is thrown. An exception is also raised if the data type isn't string and
     * a length of the attribute is provided. If no default search operation has
     * been provided for the attribute one of the {@link Attribute#DEFAULT_OPS
     * default search ops} is given based on the data type.
     * <ul>
     * <li>Created: 21 Sep 2010 09:13:58</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aType the data type of the attribute.
     * @since %since%
     */
    public void setDatatype(final String aType) {
	this.dataType = aType.toLowerCase().trim();
	if (String.valueOf(IDfAttr.DM_BOOLEAN).equals(getDataType())) {
	    this.dataType = Attribute.BOOLEAN;
	} else if (String.valueOf(IDfAttr.DM_DOUBLE).equals(getDataType())) {
	    this.dataType = Attribute.DOUBLE;
	} else if (String.valueOf(IDfAttr.DM_ID).equals(getDataType())) {
	    this.dataType = Attribute.ID;
	} else if (String.valueOf(IDfAttr.DM_INTEGER).equals(getDataType())) {
	    this.dataType = Attribute.INTEGER;
	} else if (String.valueOf(IDfAttr.DM_STRING).equals(getDataType())) {
	    this.dataType = Attribute.STRING;
	} else if (String.valueOf(IDfAttr.DM_TIME).equals(getDataType())) {
	    this.dataType = Attribute.TIME;
	}
	if (!Attribute.BOOLEAN.equals(getDataType())
	        && !Attribute.DOUBLE.equals(getDataType())
	        && !Attribute.ID.equals(getDataType())
	        && !Attribute.INTEGER.equals(getDataType())
	        && !Attribute.STRING.equals(getDataType())
	        && !Attribute.TIME.equals(getDataType())) {
	    throw new BuildException(
		    String
		            .format(
		                    "Invalid data type '%s'. Data type must be one of the following %s, %s, %s, %s, %s or %s.", //$NON-NLS-1$
		                    new Object[] { aType, Attribute.BOOLEAN,
		                            Attribute.DOUBLE, Attribute.ID,
		                            Attribute.INTEGER,
		                            Attribute.STRING, Attribute.TIME }));
	} else if ((getLength() > 0) && !Attribute.STRING.equals(getDataType())) {
	    throw new BuildException(
		    String
		            .format(
		                    "When data lenght %s is defined only %s data type is allowed. Data type defined is %s.", //$NON-NLS-1$
		                    new Object[] { String.valueOf(getLength()),
		                            Attribute.STRING, getDataType() }));
	} else if ((getAllowedSearchOps() == null) && notAutomatic()) {
	    setAllowedsearchops(Attribute.DEFAULT_OPS.get(getDataType()));
	}
    }

    /**
     * Sets the default value for the attribute. NOTE: You can control the list
     * of reserved defaults from the Type.properties file in this same package.
     * <ul>
     * <li>Created: 21 Sep 2010 10:05:03</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aDefault the default value for the attribute.
     * @since %since%
     */
    public void setDefault(final String aDefault) {
	this.defaultValue = aDefault;
    }

    /**
     * Set a list of illegal characters for this attribute. Due to xml
     * limitations this string must be given as URL encoded.
     * <ul>
     * <li>Created: 5.10.2010 15.12.52</li>
     * <li>Author: Christopher Harper, account: ebf1231</li>
     * </ul>
     * 
     * @param theIllegalCharacters string of characters that are illegal.
     * @since %since%
     */
    public void setIllegalcharacters(final String theIllegalCharacters) {
	try {
	    this.illegalCharacters = URLDecoder.decode(theIllegalCharacters)
		    .toCharArray();
	} catch (final PKIException pkiex) {
	    throw new BuildException(String.format(
		    "Failed to URL decode the string %s.", //$NON-NLS-1$
		    new Object[] { theIllegalCharacters }), pkiex);
	}
    }

    /**
     * Set whether the list provided by the value assistance query is complete.
     * String value passed in is transformed to a boolean using
     * <code>Boolean.parseBoolean(String);</code>
     * <ul>
     * <li>Created: 21 Sep 2010 11:06:17</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param complete is the value list complete.
     * @since %since%
     */
    public void setIsComplete(final String complete) {
	if (Boolean.parseBoolean(complete)) {
	    this.isComplete = 1;
	} else {
	    this.isComplete = 0;
	}
    }

    /**
     * Set whether this attribute is hidden on this type and sub types. String
     * value passed in is transformed to a boolean using
     * <code>Boolean.parseBoolean(String);</code>
     * <ul>
     * <li>Created: 21 Sep 2010 11:08:40</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param hidden should the attribute be hidden.
     * @since %since%
     */
    public void setIshidden(final String hidden) {
	if (Boolean.parseBoolean(hidden)) {
	    this.isHidden = 1;
	} else {
	    this.isHidden = 0;
	}
    }

    /**
     * Set whether this attribute is read only on this type and sub types.
     * String value passed in is transformed to a boolean using
     * <code>Boolean.parseBoolean(String);</code>
     * <ul>
     * <li>Created: 21 Sep 2010 11:17:07</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param only is read only.
     * @since %since%
     */
    public void setIsreadonly(final String only) {
	if (Boolean.parseBoolean(only)) {
	    this.readOnly = 1;
	} else {
	    this.readOnly = 0;
	}
    }

    /**
     * Set whether the attribute is repeating. String value passed in is
     * transformed to a boolean using <code>Boolean.parseBoolean(String);</code>
     * <ul>
     * <li>Created: 21 Sep 2010 11:19:16</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param repeating is the attribute repeating.
     * @since %since%
     */
    public void setIsrepeating(final String repeating) {
	if (Boolean.parseBoolean(repeating)) {
	    this.isRepeating = 1;
	} else {
	    this.isRepeating = 0;
	}
    }

    /**
     * Set whether the attribute is required. String value passed in is
     * transformed to a boolean using <code>Boolean.parseBoolean(String);</code>
     * <ul>
     * <li>Created: 29 Sep 2010 10:31:29</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param required is a value required for this attribute.
     * @since %since%
     */
    public void setIsrequired(final String required) {
	if (Boolean.parseBoolean(required)) {
	    this.isRequired = 1;
	} else {
	    this.isRequired = 0;
	}
    }

    /**
     * Set whether the attribute is searchable. String value passed in is
     * transformed to a boolean using <code>Boolean.parseBoolean(String);</code>
     * <ul>
     * <li>Created: 29 Sep 2010 10:32:08</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param search does the attribute appear in the advanced search dialog.
     * @since %since%
     */
    public void setIssearchable(final String search) {
	if (Boolean.parseBoolean(search)) {
	    this.isSearchable = 1;
	} else {
	    this.isSearchable = 0;
	}
    }

    /**
     * Set the length of an attribute. The data type of the attribute must be
     * string. String value passed in is transformed to a boolean using
     * <code>Integer.parseInt(String);</code>
     * <ul>
     * <li>Created: 29 Sep 2010 10:37:10</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aLength the length of the attribute.
     * @since %since%
     */
    public void setLength(final String aLength) {
	this.length = Integer.parseInt(aLength);
	if ((getDataType() != null) && !Attribute.STRING.equals(getDataType())
	        && (getLength() > 0)) {
	    throw new BuildException(String.format(
		    "You cannot spesify a length %s for data type %s.", //$NON-NLS-1$
		    new Object[] { aLength, getDataType() }));
	}
    }

    /**
     * Set the name of the attribute.
     * <ul>
     * <li>Created: 29 Sep 2010 10:39:46</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aName name of the attribute.
     * @since %since%
     */
    public void setName(final String aName) {
	this.name = aName;
    }

    /**
     * Set the comma separated list of repositories where this attribute is
     * valid. String value passed in is transformed to a list using
     * <code>Arrays.asList(String.split(","));</code>.<b>NOTE</b>: If no
     * repositories are given this setting is executed for all repositories.
     * <ul>
     * <li>Created: 29 Sep 2010 10:40:10</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param theRepositories the comma separated list of repositories.
     * @since %since%
     */
    public void setRepositories(final String theRepositories) {
	this.repositories = Arrays.asList(theRepositories.split(",")); //$NON-NLS-1$
	Collections.sort(this.repositories);
    }

    /**
     * Set the query to be used for value assistance. <b>NOTE</b>: The query is
     * tested when the attribute is altered and the stack trace is printed to
     * the log where you can locate it. The build will not fail even if the
     * value assistance query fails.
     * <ul>
     * <li>Created: 29 Sep 2010 10:44:02</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param query the query to return values for the attribute.
     * @since %since%
     */
    public void setValueAssistanceQuery(final String query) {
	this.dql = query;
    }

    /**
     * Create a string representation of the attribute. Included value are:
     * name, data type (length if string), list of repositories or '[all
     * repositories]' if no value is given.
     * <ul>
     * <li>Created: 29 Sep 2010 10:49:28</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return a string representation of the attribute.
     * @since %since%
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	final StringBuilder string = new StringBuilder(60).append(getName())
	        .append(' ').append(getDataType());
	if (getLength() > 0) {
	    string.append(" (").append(getLength()).append(')'); //$NON-NLS-1$
	}
	if (getRepositories().size() == 0) {
	    string.append(" [all repositories]"); //$NON-NLS-1$
	} else {
	    string.append(" [").append(getRepositories().toString()) //$NON-NLS-1$
		    .append(']');
	}
	return string.toString();
    }

    /**
     * If an value assistance query is provided can those value be cached?
     * <ul>
     * <li>Created: 29 Sep 2010 10:52:19</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return whether value assistance query results can be cached.
     * @since %since%
     */
    protected boolean allowCaching() {
	return this.allowCaching;
    }

    /**
     * Get the allowed search operators.
     * <ul>
     * <li>Created: 29 Sep 2010 10:54:03</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the allowed search operators.
     * @since %since%
     */
    protected List<Integer> getAllowedSearchOps() {
	return this.searchOps;
    }

    /**
     * Get the attribute label.
     * <ul>
     * <li>Created: 29 Sep 2010 10:55:06</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the attribute label.
     * @since %since%
     */
    protected String getAttributeLabel() {
	return this.label;
    }

    /**
     * Get the data type of the attribute.
     * <ul>
     * <li>Created: 29 Sep 2010 10:57:28</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the attribute data type.
     * @since %since%
     */
    protected String getDataType() {
	return this.dataType;
    }

    /**
     * Get the default value for the attribute.
     * <ul>
     * <li>Created: 29 Sep 2010 10:59:04</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the default value for the attribute.
     * @since %since%
     */
    protected String getDefault() {
	return this.defaultValue;
    }

    protected char[] getIllegalCharacters() {
	return this.illegalCharacters;
    }

    /**
     * Get the attribute length.
     * <ul>
     * <li>Created: 29 Sep 2010 10:59:48</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the attribute length.
     * @since %since%
     */
    protected int getLength() {
	return this.length;
    }

    /**
     * Get the attribute name.
     * <ul>
     * <li>Created: 29 Sep 2010 11:04:55</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the attribute name.
     * @since %since%
     */
    protected String getName() {
	return this.name;
    }

    /**
     * Get the list of repositories where this dd change is applicable.
     * <ul>
     * <li>Created: 29 Sep 2010 11:06:52</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return list of repositories.
     * @since %since%
     */
    protected List<String> getRepositories() {
	return this.repositories;
    }

    /**
     * Get the value assistance query.
     * <ul>
     * <li>Created: 29 Sep 2010 11:10:38</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the query.
     * @since %since%
     */
    protected String getValueAssistanceQuery() {
	return this.dql;
    }

    /**
     * Check whether this attribute has data dictionary changes.
     * <ul>
     * <li>Created: 29 Sep 2010 11:11:01</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return whether the attribute has data dictionary changes.
     * @since %since%
     */
    protected boolean hasDDChanges() {
	return (isComplete() != -1) || (isHidden() != -1)
	        || (isReadOnly() != -1) || (isRequired() != -1)
	        || (isSearchable() != -1) || (getAllowedSearchOps() != null)
	        || (getAttributeLabel() != null)
	        || (getValueAssistanceQuery() != null)
	        || (getDefault() != null);
    }

    /**
     * Get whether the list provided by the value assistance query is complete.
     * <ul>
     * <li>Created: 29 Sep 2010 11:12:53</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return whether the list is complete.
     * @since %since%
     */
    protected int isComplete() {
	return this.isComplete;
    }

    /**
     * Check whether this attribute is for the current repository.
     * <ul>
     * <li>Created: 29 Sep 2010 11:14:25</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param repositoryName name of the current repository.
     * @return true if this attribute settings are for the current repository.
     * @since %since%
     */
    protected boolean isForThisRepository(final String repositoryName) {
	return (getRepositories().size() == 0)
	        || getRepositories().contains(repositoryName);
    }

    /**
     * Return whether this attribute is hidded.
     * <ul>
     * <li>Created: 29 Sep 2010 11:15:45</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return is hidden.
     * @since %since%
     */
    protected int isHidden() {
	return this.isHidden;
    }

    /**
     * Is this attribute read only.
     * <ul>
     * <li>Created: 29 Sep 2010 11:16:16</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return is read only.
     * @since %since%
     */
    protected int isReadOnly() {
	return this.readOnly;
    }

    /**
     * Is this attribute repeating.
     * <ul>
     * <li>Created: 29 Sep 2010 11:16:45</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return is the attribute repeating.
     * @since %since%
     */
    protected int isRepeating() {
	return this.isRepeating;
    }

    /**
     * Is this attribute required.
     * <ul>
     * <li>Created: 29 Sep 2010 11:17:10</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return is the attribute required.
     * @since %since%
     */
    protected int isRequired() {
	return this.isRequired;
    }

    /**
     * Is the attribute searchable.
     * <ul>
     * <li>Created: 29 Sep 2010 11:17:33</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return can the attribute be searched.
     * @since %since%
     */
    protected int isSearchable() {
	return this.isSearchable;
    }

    /**
     * Whether the attribute was created programatically.
     * <ul>
     * <li>Created: 29 Sep 2010 11:18:02</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return if the attribute was created manually.
     * @since %since%
     */
    private boolean notAutomatic() {
	return this.notAutomatic;
    }
}
/*-
 * $Log$
 */
