package fi.sardion.dctm.deploy.persistent;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.util.DCTMTask;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 24 Sep 2010 15:04:12</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.persistent.AliasSet</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */

public class AliasSet extends Persistent {

    private final List<Alias> aliases = new ArrayList<Alias>();
    private String aliasSetName;

    /**
     * <ul>
     * <li>Created: 24 Sep 2010 15:04:12</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public AliasSet() {
	super();
    }

    public void addAlias(final Alias alias) {
	this.aliases.add(alias);
    }

    public void setAliasSetName(final String aName) {
	this.aliasSetName = aName;
    }

    protected List<Alias> getAliases() {
	return this.aliases;
    }

    protected String getAliasSetName() {
	return this.aliasSetName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fi.sardion.dctm.deploy.persistent.Persistent#getExistingQualification()
     */
    @Override
    protected String getExistingQualification() {
	return new StringBuilder().append(DCTMTask.DM_ALIAS_SET).append(
		" where ").append(DCTMTask.OBJECT_NAME).append(" = '").append( //$NON-NLS-1$ //$NON-NLS-2$
		getAliasSetName()).append('\'').toString();
    }

    @Override
    protected String getType() {
	return DCTMTask.DM_ALIAS_SET;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fi.sardion.dctm.deploy.persistent.Persistent#typeSpesific(com.documentum
     * .fc.client.IDfPersistentObject)
     */
    @Override
    protected void typeSpesific(final IDfPersistentObject tempObject) {
	try {
	    if (tempObject.isDirty() || reaplySettings()) {
		tempObject.setString(DCTMTask.OBJECT_DESCRIPTION,
			getDescription());
		tempObject.setString(DCTMTask.OBJECT_NAME, getAliasSetName());
		tempObject.removeAll(DCTMTask.ALIAS_CATEGORY);
		tempObject.removeAll(DCTMTask.ALIAS_DESCRIPTION);
		tempObject.removeAll(DCTMTask.ALIAS_NAME);
		tempObject.removeAll(DCTMTask.ALIAS_USR_CATEGORY);
		tempObject.removeAll(DCTMTask.ALIAS_VALUE);
		for (final Alias alias : getAliases()) {
		    tempObject.appendInt(DCTMTask.ALIAS_CATEGORY, alias
			    .getCategory());
		    tempObject.appendString(DCTMTask.ALIAS_DESCRIPTION, alias
			    .getDescription());
		    tempObject.appendString(DCTMTask.ALIAS_NAME, alias
			    .getName());
		    tempObject.appendInt(DCTMTask.ALIAS_USR_CATEGORY, alias
			    .getUserCategory());
		    tempObject.appendString(DCTMTask.ALIAS_VALUE, alias
			    .getValue());
		}
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException(
		    "Failed to set alias set spesific values.", dex); //$NON-NLS-1$
	}
    }
}

/*-
 * $Log$
 */