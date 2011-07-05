package fi.sardion.dctm.deploy.persistent;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfRelationType;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.util.DCTMTask;

public class RelationType extends Persistent {

    private String childParentLabel = "";
    private String childType = DCTMTask.DM_SYSOBJECT;
    private String controllingKind = "TSCC";
    private int copyChild = 0;
    private int directionKind = 0;
    private int integrityKind = 0;
    private String name = null;
    private String parentChildLabel = "";
    private String parentType = DCTMTask.DM_SYSOBJECT;
    private boolean permanentLink = false;
    private String securityType = "PARENT"; //$NON-NLS-1$

    public void setChildParentLabel(final String label) {
	this.childParentLabel = label;
    }

    public void setChildtype(final String type) {
	this.childType = type;
    }

    public void setControllingkind(final String kind) {
	this.controllingKind = kind;
    }

    public void setCopyChild(final String copy) {
	this.copyChild = Integer.parseInt(copy);
	if ((getCopyChild() > 1) || (getCopyChild() < 0)) {
	    this.copyChild = 0;
	}
    }

    public void setDirectionkind(final String aKind) {
	this.directionKind = validateKind(aKind);
    }

    public void setIntegritykind(final String aKind) {
	this.integrityKind = validateKind(aKind);
    }

    public void setParentChildLabel(final String label) {
	this.parentChildLabel = label;
    }

    public void setParenttype(final String type) {
	this.parentType = type;
    }

    public void setPermanentlink(final String permanent) {
	this.permanentLink = Boolean.parseBoolean(permanent);
    }

    public void setRelationname(final String aName) {
	this.name = aName;
    }

    public void setSecuritytype(final String aSecurityType) {
	if ("CHILD".equalsIgnoreCase(aSecurityType)
		|| "SYSTEM".equalsIgnoreCase(aSecurityType)
		|| "NONE".equalsIgnoreCase(aSecurityType)) {
	    this.securityType = aSecurityType.toUpperCase();
	} else {
	    this.securityType = "PARENT";
	}
    }

    protected String getChildParentLabel() {
	return this.childParentLabel;
    }

    protected String getChildType() {
	return this.childType;
    }

    protected String getControllinKind() {
	return this.controllingKind;
    }

    protected int getCopyChild() {
	return this.copyChild;
    }

    protected int getDirectionKind() {
	return this.directionKind;
    }

    @Override
    protected String getExistingQualification() {
	return new StringBuilder().append(DCTMTask.DM_RELATION_TYPE).append(
		" where ").append(DCTMTask.RELATION_NAME).append(" = '")
		.append(getRelationName()).append('\'').toString();
    }

    protected int getIntegrityKind() {
	return this.integrityKind;
    }

    protected String getParentChildLabel() {
	return this.parentChildLabel;
    }

    protected String getParentType() {
	return this.parentType;
    }

    protected boolean getPermanentLink() {
	return this.permanentLink;
    }

    protected String getRelationName() {
	return this.name;
    }

    protected String getSecurityType() {
	return this.securityType;
    }

    @Override
    protected String getType() {
	return DCTMTask.DM_RELATION_TYPE;
    }

    @Override
    protected void typeSpesific(final IDfPersistentObject tempObject) {
	typeSpesific((IDfRelationType) tempObject);
    }

    protected void typeSpesific(final IDfRelationType relationType) {
	try {
	    if (relationType.isNew() || relationType.isDirty()
		    || reaplySettings()) {
		relationType.setRelationName(getRelationName());
		relationType.setSecurityType(getSecurityType());
		relationType.setParentType(getParentType());
		relationType.setChildType(getChildType());
		relationType.setDescription(getDescription());
		relationType
			.setInt(DCTMTask.DIRECTION_KIND, getDirectionKind());
		relationType
			.setInt(DCTMTask.INTEGRITY_KIND, getIntegrityKind());
		relationType.setString(DCTMTask.PARENT_CHILD_LABEL,
			getParentChildLabel());
		relationType.setString(DCTMTask.CHILD_PARENT_LABEL,
			getChildParentLabel());
		relationType.setPermanentLink(getPermanentLink());
		relationType.setCopyBehavior(getCopyChild());
		relationType.setControllingKind(getControllinKind());
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException("Failure modifying object.", dex); //$NON-NLS-1$
	}
    }

    private int validateKind(final String aKind) {
	int kind = Integer.parseInt(aKind);
	if ((kind < 0) || (kind > 2)) {
	    kind = 0;
	}
	return kind;
    }
}

/*-
 * $Log$
 */