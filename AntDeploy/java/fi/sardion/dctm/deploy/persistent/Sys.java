package fi.sardion.dctm.deploy.persistent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.util.DCTMTask;

public class Sys extends Persistent {

    private Acl acl = null;
    private String aclDomain = null;
    private String aclName = null;
    private String objectName = null;
    private String owner = null;
    private List<String> paths = null;

    public void addAcl(final Acl theAcl) {
	this.acl = theAcl;
    }

    public void setAcldomain(final String aDomain) {
	this.aclDomain = aDomain;
    }

    public void setAclname(final String aName) {
	this.aclName = aName;
    }

    public void setFolders(final String aPaths) {
	this.paths = Arrays.asList(aPaths.split(",")); //$NON-NLS-1$
    }

    public void setObjectname(final String anObjectName) {
	this.objectName = anObjectName;
    }

    public void setOwnername(final String anOwner) {
	this.owner = anOwner;
    }

    protected void addPath(final String aPath) {
	if (getFolders() == null) {
	    this.paths = new ArrayList<String>();
	}
	if (!getFolders().contains(aPath)) {
	    getFolders().add(aPath);
	}
    }

    protected boolean containsPath(final String aPath) {
	if (getFolders() == null) {
	    return false;
	}
	return getFolders().contains(aPath);
    }

    protected Acl getAcl() {
	return this.acl;
    }

    protected String getACLDomain() {
	return this.aclDomain;
    }

    protected String getACLName() {
	return this.aclName;
    }

    @Override
    protected String getExistingQualification() {
	return new StringBuilder().append(getType()).append(" where ").append( //$NON-NLS-1$
	        DCTMTask.OBJECT_NAME).append(" = '").append(getObjectName()) //$NON-NLS-1$
	        .append('\'').toString();
    }

    protected List<String> getFolders() {
	return this.paths;
    }

    protected String getObjectName() {
	return this.objectName;
    }

    protected String getOwnerName() {
	return this.owner;
    }

    @Override
    protected void typeSpesific(final IDfPersistentObject tempObject) {
	typeSpesific((IDfSysObject) tempObject);
    }

    protected void typeSpesific(final IDfSysObject sysobject) {
	if (getAcl() != null) {
	    getAcl().setSession(getSession());
	    getAcl().execute();
	    setAcldomain(getAcl().getOwnerName());
	    setAclname(getAcl().getAclName());
	}
	if ((getACLName() != null) && ((getACLDomain() == null) || "dm_dbo" //$NON-NLS-1$
	        .equalsIgnoreCase(getACLDomain()))) {
	    try {
		setAcldomain(getSession().getDocbaseOwnerName());
	    } catch (final DfException dex) {
		printStack(dex);
		throw new BuildException(
		        "Failed to return the repository owner name.", dex); //$NON-NLS-1$
	    }
	}
	if ((getACLDomain() != null) && (getACLName() != null)) {
	    try {
		sysobject.setACLDomain(getACLDomain());
		sysobject.setACLName(getACLName());
	    } catch (final DfException dex) {
		printStack(dex);
		throw new BuildException(String.format(
		        "Failure setting ACL %s/%s.", new Object[] { //$NON-NLS-1$
		        getACLDomain(), getACLName() }), dex);
	    }
	}
	if (getFolders() != null) {
	    for (final String path : getFolders()) {
		try {
		    final IDfFolder folder = getSession().getFolderByPath(path);
		    if ((folder != null)
			    && (sysobject.findId(DCTMTask.I_FOLDER_ID, folder
			            .getObjectId()) <= -1)) {
			sysobject.link(folder.getObjectId().getId());
		    }
		} catch (final DfException dex) {
		    printStack(dex);
		    throw new BuildException(String.format(
			    "Failure linkin object to %s.", //$NON-NLS-1$
			    new Object[] { path }), dex);
		}
	    }
	}
	if (getObjectName() != null) {
	    try {
		sysobject.setObjectName(getObjectName());
	    } catch (final DfException dex) {
		printStack(dex);
		throw new BuildException(String.format(
		        "Failure setting object name to %s.", //$NON-NLS-1$
		        new Object[] { getObjectName() }), dex);
	    }
	}
	if (getOwnerName() != null) {
	    try {
		if ("dm_dbo".equalsIgnoreCase(getOwnerName())) { //$NON-NLS-1$
		    setOwnername(getSession().getDocbaseOwnerName());
		}
		sysobject.setOwnerName(getOwnerName());
	    } catch (final DfException dex) {
		printStack(dex);
		throw new BuildException(String.format(
		        "Failure setting owner name to %s.", //$NON-NLS-1$
		        new Object[] { getOwnerName() }), dex);
	    }
	}
    }
}
/*-
 * $Log$
 */
