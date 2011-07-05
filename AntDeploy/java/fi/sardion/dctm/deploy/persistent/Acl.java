package fi.sardion.dctm.deploy.persistent;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.DfPermit;
import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfPermit;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.util.DCTMTask;

public class Acl extends Persistent {

    private final List<Accessor> accessors = new ArrayList<Accessor>();
    private int aclClass = 3;
    private String name = null;
    private String owner = null;

    public void addAccessor(final Accessor anAccessor) {
	this.accessors.add(anAccessor);
    }

    public void setAclclass(final String theClass) {
	this.aclClass = Integer.parseInt(theClass);
    }

    public void setAclname(final String aName) {
	this.name = aName;
    }

    public void setOwnername(final String anOwner) {
	this.owner = anOwner;
    }

    protected List<Accessor> getAccessors() {
	return this.accessors;
    }

    protected int getAclClass() {
	return this.aclClass;
    }

    protected String getAclName() {
	return this.name;
    }

    @Override
    protected String getExistingQualification() {
	if ((getOwnerName() == null) && (getAclName() == null)) {
	    throw new BuildException(
		    "Both acl name and acl owner must be provided.");
	} else if ("dm_dbo".equalsIgnoreCase(getOwnerName())) {
	    try {
		setOwnername(getSession().getDocbaseOwnerName());
	    } catch (final DfException dex) {
		printStack(dex);
		throw new BuildException(
		        "Failure returning repository owner name.");
	    }
	}
	return new StringBuilder().append(DCTMTask.DM_ACL).append(" where ")
	        .append(DCTMTask.OBJECT_NAME).append(" = '").append(
	                getAclName()).append("' and ").append(
	                DCTMTask.OWNER_NAME).append(" = '").append(
	                getOwnerName()).append('\'').toString();
    }

    protected String getOwnerName() {
	return this.owner;
    }

    @Override
    protected String getType() {
	return DCTMTask.DM_ACL;
    }

    protected void typeSpesific(final IDfACL acl) {
	try {
	    if (acl.isNew() || acl.isDirty() || reaplySettings()) {
		acl.setDescription(getDescription());
		acl.setACLClass(getAclClass());
		acl.setObjectName(getAclName());
		if ((getOwnerName() == null)
		        || "dm_dbo".equalsIgnoreCase(getOwnerName())) { //$NON-NLS-1$
		    setOwnername(getSession().getDocbaseOwnerName());
		}
		acl.setDomain(getOwnerName());
		for (final Accessor accessor : getAccessors()) {
		    IDfPermit permit = new DfPermit();
		    permit.setAccessorName(accessor.getName());
		    permit.setPermitType(accessor.getPermitType());
		    permit.setPermitValue(accessor.getPermit());
		    acl.grantPermit(permit);
		    if (accessor.getExtendedPermit() != null) {
			permit = new DfPermit();
			permit.setAccessorName(accessor.getName());
			permit.setPermitType(IDfPermit.DF_EXTENDED_PERMIT);
			permit.setPermitValue(accessor.getExtendedPermit());
			acl.grantPermit(permit);
		    }
		}
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException("Failure modifying acl object.", dex); //$NON-NLS-1$
	}
    }

    @Override
    protected void typeSpesific(final IDfPersistentObject tempObject) {
	typeSpesific((IDfACL) tempObject);
    }
}
/*-
 * $Log$
 */
