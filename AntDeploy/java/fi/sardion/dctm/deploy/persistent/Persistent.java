package fi.sardion.dctm.deploy.persistent;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.util.DCTMTask;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 6 Sep 2010 16:26:35</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.sysobject.SysObject</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public abstract class Persistent extends DCTMTask {

    /**
     * <ul>
     * <li>Created: 7 Sep 2010 16:28:03</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private final List<Attribute> attributes = new ArrayList<Attribute>();
    /**
     * <ul>
     * <li>Created: 7 Sep 2010 16:28:00</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String type;

    /**
     * <ul>
     * <li>Created: 6 Sep 2010 16:26:35</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Persistent() {
	super();
    }

    public void addAttribute(final Attribute anAttribute) {
	this.attributes.add(anAttribute);
    }

    @Override
    public void execute() {
	final IDfSession session = getSession();
	try {
	    if (isForThisRepository(session.getDocbaseName())) {
		IDfPersistentObject tempObject = checkExixting();
		if (tempObject == null) {
		    try {
			tempObject = getSession().newObject(getType());
		    } catch (final DfException dex) {
			printStack(dex);
			throw new BuildException(String.format(
			        "Failure creating object with type '%s'", //$NON-NLS-1$
			        new Object[] { getType() }), dex);
		    }
		}
		for (final Attribute attribute : getAttributes()) {
		    try {
			if (tempObject.hasAttr(attribute.getName())) {
			    if (tempObject.isAttrRepeating(attribute.getName())) {
				tempObject.removeAll(attribute.getName());
				for (final String value : attribute.getValues()) {
				    tempObject.appendString(
					    attribute.getName(), value);
				}
			    } else {
				tempObject.setString(attribute.getName(),
				        attribute.getValue());
			    }
			} else {
			    log(String.format(
				    "Attribute %s not present on object.", //$NON-NLS-1$
				    new Object[] { attribute.getName() }));
			}
		    } catch (final DfException dex) {
			printStack(dex);
			throw new BuildException(String.format(
			        "Failure setting value '%s' to attribute '%s'", //$NON-NLS-1$
			        new Object[] { attribute.getValue(),
			                attribute.getName() }), dex);
		    }
		}
		typeSpesific(tempObject);
		try {
		    if (tempObject.isNew() || tempObject.isDirty()) {
			if (tempObject instanceof IDfSysObject) {
			    final IDfSysObject sysobject = (IDfSysObject) tempObject;
			    final IDfUser user = getSession().getUser(null);
			    if ((IDfACL.DF_PERMIT_WRITE > sysobject.getPermit())
				    && (IDfUser.DF_PRIVILEGE_SUPERUSER == user
				            .getUserPrivileges())) {
				log(String
				        .format(
				                "Granting %s permit to %s since old %s is insufficient for saving changes.", //$NON-NLS-1$
				                new Object[] {
				                        new Integer(
				                                IDfACL.DF_PERMIT_WRITE),
				                        user.getUserName(),
				                        new Integer(sysobject
				                                .getPermit()) }));
				final IDfACL acl = getSession().getACL(
				        sysobject.getACLDomain(),
				        sysobject.getACLName());
				acl.grant(user.getUserName(),
				        IDfACL.DF_PERMIT_WRITE, ""); //$NON-NLS-1$
				acl.save();
			    }
			    if (sysobject.isCheckedOut()) {
				tempObject = getSession().getObject(
				        sysobject.checkin(false, "")); //$NON-NLS-1$
			    } else {
				sysobject.save();
			    }
			} else {
			    tempObject.save();
			}
		    }
		    if (isVerbose()) {
			log(String
			        .format(
			                "#################### Object type %s dump start ####################\n%s\n##################### Object type %s dump end #####################", //$NON-NLS-1$
			                new Object[] { getType(),
			                        tempObject.dump(),
			                        tempObject.getType().getName() }));
		    }
		} catch (final DfException dex) {
		    printStack(dex);
		    throw new BuildException(String.format(
			    "Failure saving object with type '%s'", //$NON-NLS-1$
			    new Object[] { getType() }), dex);
		}
	    } else {
		log(String
		        .format(
		                "This persistent object %s was only defined for repositories %s and the current repository is %s.", //$NON-NLS-1$
		                new Object[] { getType(), getRepositories(),
		                        session.getDocbaseName() }));
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException("Failed to return repository name.", dex); //$NON-NLS-1$
	} catch (final Throwable t) {
	    printStack(t);
	    throw new BuildException("Unknown.", t); //$NON-NLS-1$
	}
    }

    /**
     * <ul>
     * <li>Created: 7 Sep 2010 16:27:25</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aType
     * @since %since%
     */
    public void setType(final String aType) {
	this.type = aType;
    }

    /**
     * <ul>
     * <li>Created: 7 Sep 2010 16:27:31</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected IDfPersistentObject checkExixting() {
	final String qualification = getExistingQualification();
	try {
	    final IDfPersistentObject tempObject = getSession()
		    .getObjectByQualification(qualification);
	    if (tempObject == null) {
		log(String.format(
		        "Failed to return object with qualificaiton: '%s'.", //$NON-NLS-1$
		        new Object[] { qualification }));
	    } else {
		log(String.format("Returned object with qualificaiton: '%s'.", //$NON-NLS-1$
		        new Object[] { qualification }));
	    }
	    return tempObject;
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException(String.format(
		    "Failure returning object with qualification '%s'.", //$NON-NLS-1$
		    new Object[] { qualification }), dex);
	}
    }

    /**
     * <ul>
     * <li>Created: 7 Sep 2010 16:27:38</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected List<Attribute> getAttributes() {
	return this.attributes;
    }

    /**
     * <ul>
     * <li>Created: 7 Sep 2010 16:27:40</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected abstract String getExistingQualification();

    /**
     * <ul>
     * <li>Created: 7 Sep 2010 16:27:50</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected String getType() {
	return this.type;
    }

    /**
     * <ul>
     * <li>Created: 7 Sep 2010 16:27:44</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param tempObject
     * @since %since%
     */
    protected abstract void typeSpesific(IDfPersistentObject tempObject);
}
/*-
 * $Log$
 */
