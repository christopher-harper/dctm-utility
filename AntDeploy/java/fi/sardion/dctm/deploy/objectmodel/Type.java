package fi.sardion.dctm.deploy.objectmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfType;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfAttr;

import fi.sardion.dctm.deploy.util.DCTMTask;

/**
 * <p>
 * A task for an repository object type.
 * <ul>
 * <li>Created: 29 Sep 2010 11:30:00</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.objectmodel.Type</li>
 * </ul>
 * </p>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public class Type extends DCTMTask {

    /**
     * List of reserved words for default values.
     * <ul>
     * <li>Created: 29 Sep 2010 11:31:15</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private static final List<String> RESERVED_DEFAULTS;
    /**
     * Initialise the reserved default values from the properties file:
     * <code>fi.sardion.dctm.deploy.objectmodel.Type.properties</code>.
     * <ul>
     * <li>Created: 29 Sep 2010 11:31:15</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    static {
	final ResourceBundle settings = ResourceBundle.getBundle(Type.class
	        .getName(), Locale.getDefault());
	final int count = Integer.parseInt(settings
	        .getString("RESERVED_DEFAULT_COUNT")); //$NON-NLS-1$
	RESERVED_DEFAULTS = new ArrayList<String>(count + 2);
	for (int index = 0; index < count; index++) {
	    Type.RESERVED_DEFAULTS.add(settings.getString("RESERVED_DEFAULT_" //$NON-NLS-1$
		    + index).toUpperCase());
	}
    }
    /**
     * List of attribute names this type has.
     * <code>attributeNames = new ArrayList<String>();</code>
     * <ul>
     * <li>Created: 29 Sep 2010 11:33:42</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private final List<String> attributeNames = new ArrayList<String>();
    private Attributes attributes = new Attributes();
    private boolean isSystemType = true;
    private String label = null;
    private boolean modify = false;
    private String name;
    private int state = -1;
    private final List<Type> subTypes = new ArrayList<Type>();
    private String superType = null;

    public Type() {
	super();
    }

    public void addObjecttype(final Type type) {
	this.subTypes.add(type);
    }

    public void addTypeattributes(final Attributes theAttributes) {
	this.attributes = theAttributes;
    }

    @Override
    public void execute() throws BuildException {
	try {
	    if (isForThisRepository(getSession().getDocbaseName())) {
		populateAttributeNames();
		IDfType type = null;
		final String dql = new StringBuilder().append(
		        "select count(*) as cnt from dm_type where name = '") //$NON-NLS-1$
		        .append(getName()).append('\'').toString();
		final IDfCollection result = new DfQuery(dql).execute(
		        getSession(), IDfQuery.EXEC_QUERY);
		log(dql);
		try {
		    if (result.next() && (result.getInt("cnt") == 1)) { //$NON-NLS-1$
			log(String.format(
			        "Type %s was present in the repository.", //$NON-NLS-1$
			        new Object[] { getName() }));
			type = getSession().getType(getName());
		    } else {
			log(String.format(
			        "Type %s was not present in the repository.", //$NON-NLS-1$
			        new Object[] { getName() }));
		    }
		} finally {
		    close(result);
		}
		if (type != null) {
		    final IDfType theSuperType = type.getSuperType();
		    if ((getSuperType() == null) && (theSuperType != null)) {
			setSupertype(theSuperType.getName());
			final Type tempSuper = new Type();
			tempSuper.addObjecttype(this);
			tempSuper.setName(theSuperType.getName());
			tempSuper.setSession(getSession());
			tempSuper.setIssystemtype(String.valueOf(true));
			tempSuper.execute();
		    } else {
			for (int index = 0, count = type.getTypeAttrCount(); index < count; index++) {
			    final IDfAttr idfAttribute = type
				    .getTypeAttr(index);
			    if ((theSuperType == null)
				    || (theSuperType
				            .findTypeAttrIndex(idfAttribute
				                    .getName()) == -1)) {
				final Attribute attribute = new Attribute(true);
				attribute.setName(idfAttribute.getName());
				attribute.setDatatype(String
				        .valueOf(idfAttribute.getDataType()));
				if (Attribute.STRING.equals(attribute
				        .getDataType())) {
				    attribute.setLength(String
					    .valueOf(idfAttribute.getLength()));
				}
				if (!containsAttribute(attribute)) {
				    getTypeAttributes().addTypeattribute(
					    attribute);
				}
			    }
			}
		    }
		} else {
		    if (isSystemType()) {
			throw new BuildException(
			        String
			                .format(
			                        "Type %s was marked as a system type but couln't be located in the repository.", //$NON-NLS-1$
			                        new Object[] { getName() }));
		    }
		    type = executeCreate(getSession().getType(getSuperType()));
		}
		executeTypeAlter(type);
		executeAttributeAlters(type);
		for (final Type subType : getSubTypes()) {
		    subType.setSupertype(getName());
		    subType.setSession(getSession());
		    subType.execute();
		}
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException(String.format(
		    "Failure returning type %s.", new Object[] { getName() }), //$NON-NLS-1$
		    dex);
	}
    }

    public void setIssystemtype(final String isSystem) {
	this.isSystemType = Boolean.parseBoolean(isSystem);
    }

    public void setLifecyclestate(final String aState) {
	this.state = Integer.parseInt(aState);
    }

    public void setModifyType(final String aModify) {
	this.modify = Boolean.parseBoolean(aModify);
    }

    public void setName(final String aName) {
	this.name = aName;
    }

    public void setSupertype(final String aType) {
	this.superType = aType;
    }

    public void setTypelabel(final String aLabel) {
	this.label = aLabel;
    }

    protected boolean containsAttribute(final Attribute attribute) {
	return getTypeAttributes().getAttributes().contains(attribute);
    }

    protected void executeAttributeAlters(final IDfType type) {
	for (final Attribute attribute : getAttributes()) {
	    try {
		if (attribute.hasDDChanges()
		        && attribute.isForThisRepository(getSession()
		                .getDocbaseName())) {
		    if (type.findTypeAttrIndex(attribute.getName()) == -1) {
			log(String
			        .format(
			                "Could not alter attribute %s since it's not present on the type %s.", //$NON-NLS-1$
			                new Object[] { attribute.getName(),
			                        type.getName() }));
		    } else {
			StringBuilder dql = new StringBuilder(500).append(
			        "alter type ") //$NON-NLS-1$
			        .append(getName()).append(" modify ").append( //$NON-NLS-1$
			                attribute.getName()).append(
			                "(\n\ttruncate allowed_search_ops\n"); //$NON-NLS-1$
			for (int index = 0; index < 16; index++) {
			    dql.append("\t, set allowed_search_ops[").append( //$NON-NLS-1$
				    index).append("] = ").append(index + 1) //$NON-NLS-1$
				    .append('\n');
			}
			dql.append("\t, set default_search_op = 1\n\t, drop "); //$NON-NLS-1$
			dql.append("mapping table\n\t, drop value assistance"); //$NON-NLS-1$
			dql.append("\n\t, drop default\n\t, drop check\n\t, "); //$NON-NLS-1$
			dql.append("set help_text = null\n\t, set label_text"); //$NON-NLS-1$
			dql.append(" = null\n\t, set is_required = null\n\t,"); //$NON-NLS-1$
			dql.append(" set is_hidden = null\n\t, set read_only"); //$NON-NLS-1$
			dql.append(" = null\n\t, set is_searchable = null\n)"); //$NON-NLS-1$
			dql.append(" publish"); //$NON-NLS-1$
			try {
			    close(new DfQuery(dql.toString()).execute(
				    getSession(), IDfQuery.EXEC_QUERY));
			    log(String.format(
				    "Executed alter attribute statement:\n%s",//$NON-NLS-1$
				    new Object[] { dql.toString() }));
			} catch (final DfException dex) {
			    printStack(dex);
			    throw new BuildException(
				    String
				            .format(
				                    "Failure executing attribute alter statement:\n%s", //$NON-NLS-1$
				                    new Object[] { dql
				                            .toString() }));
			}
			if (attribute.getDataType() == null) {
			    attribute.setDatatype(String.valueOf(type
				    .getTypeAttrDataType(attribute.getName())));
			}
			boolean hasChanges = false;
			try {
			    dql = new StringBuilder()
				    .append("alter type ").append( //$NON-NLS-1$
				            getName())
				    .append(" modify ").append( //$NON-NLS-1$
				            attribute.getName());
			    if (Attribute.STRING
				    .equals(attribute.getDataType())
				    && (type.getTypeAttrLength(attribute
				            .getName()) < attribute.getLength())) {
				dql.append(' ').append(attribute.getDataType())
				        .append('(').append(
				                attribute.getLength()).append(
				                ')');
			    }
			    dql.append("(\n"); //$NON-NLS-1$
			    if ((attribute.getAllowedSearchOps() != null)
				    && (attribute.getAllowedSearchOps().size() > 0)) {
				hasChanges = true;
				dql.append("\ttruncate allowed_search_ops\n"); //$NON-NLS-1$
				for (int index = 0, count = attribute
				        .getAllowedSearchOps().size(); index < count; index++) {
				    if (index == 0) {
					dql
					        .append(
					                "\t, set default_search_op = ") //$NON-NLS-1$
					        .append(
					                attribute
					                        .getAllowedSearchOps()
					                        .get(index)
					                        .intValue())
					        .append('\n');
				    }
				    dql
					    .append(
					            "\t, set allowed_search_ops[").append( //$NON-NLS-1$
					            index)
					    .append("] = ").append( //$NON-NLS-1$
					            attribute
					                    .getAllowedSearchOps()
					                    .get(index)
					                    .intValue())
					    .append('\n');
				}
			    }
			    if (attribute.getAttributeLabel() != null) {
				dql.append('\t');
				if (hasChanges) {
				    dql.append(", "); //$NON-NLS-1$
				}
				hasChanges = true;
				dql
				        .append("set label_text = '").append( //$NON-NLS-1$
				                attribute.getAttributeLabel()
				                        .trim().replace(
				                                "'", "''")).append("'\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				hasChanges = true;
			    }
			    hasChanges = handleIntAttibuteField(hasChanges,
				    dql, attribute.isSearchable(),
				    "is_searchable"); //$NON-NLS-1$
			    hasChanges = handleIntAttibuteField(hasChanges,
				    dql, attribute.isHidden(), "is_hidden"); //$NON-NLS-1$
			    hasChanges = handleIntAttibuteField(hasChanges,
				    dql, attribute.isRequired(), "is_required"); //$NON-NLS-1$
			    hasChanges = handleIntAttibuteField(hasChanges,
				    dql, attribute.isReadOnly(), "read_only"); //$NON-NLS-1$
			    if (attribute.getDefault() != null) {
				dql.append('\t');
				if (hasChanges) {
				    dql.append(", "); //$NON-NLS-1$
				}
				dql.append("set default = "); //$NON-NLS-1$
				if (Type.RESERVED_DEFAULTS.contains(attribute
				        .getDefault())) {
				    dql.append(attribute.getDefault());
				} else {
				    if (Attribute.STRING.equals(attribute
					    .getDataType())
					    || Attribute.ID.equals(attribute
					            .getDataType())) {
					dql.append('\'').append(
					        attribute.getDefault()).append(
					        '\'');
				    } else {
					dql.append(attribute.getDefault());
				    }
				}
				dql.append('\n');
				hasChanges = true;
			    }
			    if (attribute.getValueAssistanceQuery() != null) {
				dql.append('\t');
				if (hasChanges) {
				    dql.append(", "); //$NON-NLS-1$
				}
				try {
				    close(new DfQuery(attribute
					    .getValueAssistanceQuery())
					    .execute(getSession(),
					            IDfQuery.READ_QUERY));
				    log(String
					    .format(
					            "Tested value assistance query %s.", //$NON-NLS-1$
					            new Object[] { attribute
					                    .getValueAssistanceQuery() }));
				} catch (final DfException dex) {
				    printStack(dex);
				    log(String
					    .format(
					            "Failed to test value assistance query %s.", //$NON-NLS-1$
					            new Object[] { attribute
					                    .getValueAssistanceQuery() }));
				}
				dql
				        .append("value assistance is qry '").append( //$NON-NLS-1$
				                attribute
				                        .getValueAssistanceQuery()
				                        .replace("'", "''")).append("' is "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if (attribute.isComplete() == 0) {
				    dql.append("not "); //$NON-NLS-1$
				}
				dql.append("complete\n"); //$NON-NLS-1$
				hasChanges = true;
			    }
			    dql.append(") publish"); //$NON-NLS-1$
			    if (hasChanges) {
				close(new DfQuery(dql.toString()).execute(
				        getSession(), IDfQuery.EXEC_QUERY));
				log(String
				        .format(
				                "Executed alter attribute statement:\n%s",//$NON-NLS-1$
				                new Object[] { dql.toString() }));
			    } else {
				log(String
				        .format(
				                "There was no changes to be made with the statement:\n%s", //$NON-NLS-1$
				                new Object[] { dql.toString() }));
			    }
			    if (attribute.getIllegalCharacters() != null) {
				for (final char character : attribute
				        .getIllegalCharacters()) {
				    dql = new StringBuilder();
				    dql.append("alter type ").append(getName());
				    dql.append(" modify ");
				    dql.append(attribute.getName());
				    dql.append("(\n\tadd check('instr(1, ");
				    dql.append(attribute.getName());
				    dql.append(", \"");
				    if (character == '"') {
					dql.append("\"");
				    }
				    dql.append(character).append("\", 1) <=0'");
				    dql.append(" language docbasic)\n\treport");
				    dql.append(" 'The attribute ");
				    dql.append(attribute.getName());
				    dql.append(" cannot contain the ''");
				    dql.append(character).append("'' ");
				    dql.append("character. Please re-enter.'");
				    dql.append("\n) publish");
				    close(new DfQuery(dql.toString()).execute(
					    getSession(), IDfQuery.EXEC_QUERY));
				    log(String
					    .format(
					            "Executed attribute constraint statement:\n%s",//$NON-NLS-1$
					            new Object[] { dql
					                    .toString() }));
				}
			    }
			} catch (final DfException dex) {
			    printStack(dex);
			    throw new BuildException(
				    String
				            .format(
				                    "Failure executing attribute alter statement:\n%s", //$NON-NLS-1$
				                    new Object[] { dql
				                            .toString() }));
			}
		    }
		}
	    } catch (final DfException dex) {
		printStack(dex);
		throw new BuildException("Failure obtaining repository name."); //$NON-NLS-1$
	    }
	}
    }

    protected IDfType executeCreate(final IDfType superType) {
	final StringBuilder dql = new StringBuilder(500).append("create type ") //$NON-NLS-1$
	        .append(getName());
	final List<String> addedAttributes = new ArrayList<String>();
	try {
	    boolean hasAttr = false;
	    for (final Attribute attribute : getAttributes()) {
		if ((superType.findTypeAttrIndex(attribute.getName()) == -1)
		        && !addedAttributes.contains(attribute.getName())
		        && attribute.isForThisRepository(getSession()
		                .getDocbaseName())) {
		    addedAttributes.add(attribute.getName());
		    if (attribute.getDataType() == null) {
			log(String
			        .format(
			                "Could not add attribute %s since it's datatype is null.", //$NON-NLS-1$
			                new Object[] { attribute.getName() }));
		    } else {
			if (hasAttr) {
			    dql.append("\t, "); //$NON-NLS-1$
			} else {
			    dql.append("(\n\t"); //$NON-NLS-1$
			    hasAttr = true;
			}
			dql.append(attribute.getName()).append(' ').append(
			        attribute.getDataType());
			if (Attribute.STRING.equals(attribute.getDataType())) {
			    dql.append('(').append(attribute.getLength())
				    .append(')');
			}
			if (attribute.isRepeating() == 1) {
			    dql.append(" repeating"); //$NON-NLS-1$
			}
			dql.append('\n');
		    }
		}
	    }
	    if (hasAttr) {
		dql.append(')');
	    }
	    dql.append(" with supertype ").append(getSuperType()); //$NON-NLS-1$
	    close(new DfQuery(dql.toString()).execute(getSession(),
		    IDfQuery.EXEC_QUERY));
	    log(String.format("Executed create statement:\n%s",//$NON-NLS-1$
		    new Object[] { dql.toString() }));
	    return getSession().getType(getName());
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException(String.format(
		    "Failure executing type create statement:\n%s", //$NON-NLS-1$
		    new Object[] { dql.toString() }));
	}
    }

    protected void executeTypeAlter(final IDfType type) {
	StringBuilder dql = new StringBuilder(150)
	        .append("alter type ").append(getName()); //$NON-NLS-1$
	final StringBuilder addDql = new StringBuilder();
	boolean hasChanges = false;
	if (getTypeLabel() != null) {
	    dql.append("\n\t set label_text = '").append(getTypeLabel()) //$NON-NLS-1$
		    .append('\'');
	    hasChanges = true;
	}
	if (getLifecycleState() > -1) {
	    dql.append("\n\t"); //$NON-NLS-1$
	    if (hasChanges) {
		dql.append(',');
	    }
	    dql.append(" set life_cycle = ").append(getLifecycleState()); //$NON-NLS-1$
	    hasChanges = true;
	}
	if (hasChanges) {
	    dql.append("\npublish"); //$NON-NLS-1$
	    try {
		close(new DfQuery(dql.toString()).execute(getSession(),
		        IDfQuery.EXEC_QUERY));
		log(String.format("Executed type alter statement:\n%s",//$NON-NLS-1$
		        new Object[] { dql.toString() }));
	    } catch (final DfException dex) {
		printStack(dex);
		throw new BuildException(String.format(
		        "Failure executing type alter statement:\n%s", //$NON-NLS-1$
		        new Object[] { dql.toString() }));
	    }
	}
	if (modifyType() && !isSystemType() && (type != null)) {
	    List<String> handledAttributes = new ArrayList<String>();
	    try {
		type.fetch(type.getName());
		final IDfType superIDfType = type.getSuperType();
		hasChanges = false;
		dql = new StringBuilder();
		for (int index = 0, count = type.getTypeAttrCount(); index < count; index++) {
		    final String attribute = type.getTypeAttr(index).getName();
		    if (!hasAttribute(attribute)
			    && !handledAttributes.contains(attribute)
			    && ((superIDfType == null) || (superIDfType
			            .findTypeAttrIndex(attribute) == -1))) {
			handledAttributes.add(attribute);
			if (hasChanges) {
			    dql.append("\t, "); //$NON-NLS-1$
			} else {
			    dql.append("alter type ").append(getName()).append( //$NON-NLS-1$
				    " drop \n\t"); //$NON-NLS-1$
			    hasChanges = true;
			}
			dql.append(attribute).append('\n');
		    }
		}
		if (hasChanges) {
		    close(new DfQuery(dql.toString()).execute(getSession(),
			    IDfQuery.EXEC_QUERY));
		    log(String.format("Executed type alter statement:\n%s",//$NON-NLS-1$
			    new Object[] { dql.toString() }));
		    type.fetch(type.getName());
		    hasChanges = false;
		}
		dql = new StringBuilder();
		handledAttributes = new ArrayList<String>();
		for (final Attribute attribute : getAttributes()) {
		    if (attribute.isForThisRepository(getSession()
			    .getDocbaseName())
			    && (type.findTypeAttrIndex(attribute.getName()) == -1)
			    && !handledAttributes.contains(attribute.getName())) {
			handledAttributes.add(attribute.getName());
			if (attribute.getDataType() == null) {
			    log(String
				    .format(
				            "Could not add attribute %s since it's datatype is null.", //$NON-NLS-1$
				            new Object[] { attribute.getName() }));
			} else {
			    if (hasChanges) {
				dql.append("\t, "); //$NON-NLS-1$
			    } else {
				dql
				        .append("alter type ").append(getName()).append( //$NON-NLS-1$
				                " add (\n\t"); //$NON-NLS-1$
				hasChanges = true;
			    }
			    dql.append(attribute.getName()).append(' ').append(
				    attribute.getDataType());
			    if (Attribute.STRING
				    .equals(attribute.getDataType())) {
				dql.append('(').append(attribute.getLength())
				        .append(')');
			    }
			    if (attribute.isRepeating() == 1) {
				dql.append(" repeating"); //$NON-NLS-1$
			    }
			    dql.append('\n');
			}
		    }
		}
		if (hasChanges) {
		    dql.append(')');
		    close(new DfQuery(dql.toString()).execute(getSession(),
			    IDfQuery.EXEC_QUERY));
		    log(String.format("Executed type alter statement:\n%s",//$NON-NLS-1$
			    new Object[] { dql.toString() }));
		    type.fetch(type.getName());
		    hasChanges = false;
		}
	    } catch (final DfException dex) {
		printStack(dex);
		throw new BuildException(String.format(
		        "Failed to alter %s type attributes with dql:\n%s%s", //$NON-NLS-1$
		        new Object[] { getName(), dql.toString(),
		                addDql.toString() }), dex);
	    }
	}
    }

    protected List<Attribute> getAttributes() {
	return getTypeAttributes().getAttributes();
    }

    protected int getLifecycleState() {
	return this.state;
    }

    protected String getName() {
	return this.name;
    }

    protected List<Type> getSubTypes() {
	return this.subTypes;
    }

    protected String getSuperType() {
	return this.superType;
    }

    protected Attributes getTypeAttributes() {
	return this.attributes;
    }

    protected String getTypeLabel() {
	return this.label;
    }

    protected boolean hasAttribute(final String attribute) {
	return this.attributeNames.contains(attribute);
    }

    protected boolean isSystemType() {
	return this.isSystemType;
    }

    protected boolean modifyType() {
	return this.modify;
    }

    private boolean handleIntAttibuteField(final boolean hasChanges,
	    final StringBuilder dql, final int value, final String field) {
	if (-1 != value) {
	    dql.append('\t');
	    if (hasChanges) {
		dql.append(", "); //$NON-NLS-1$
	    }
	    dql.append("set ").append(field).append(" = ").append(value) //$NON-NLS-1$ //$NON-NLS-2$
		    .append('\n');
	    return true;
	}
	return hasChanges;
    }

    private void populateAttributeNames() {
	for (final Attribute attribute : getAttributes()) {
	    this.attributeNames.add(attribute.getName());
	}
    }
}
/*-
 * $Log$
 */
