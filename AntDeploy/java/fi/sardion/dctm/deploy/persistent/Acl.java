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

/** <p>
 * The ACL task.
 * <ul>
 * <li>Created: Jul 19, 2011 12:36:47 PM</li>
 * <li>Project: AntDeploy</li>
 * <li>File: fi.sardion.dctm.deploy.persistent.Acl</li>
 * </ul>
 * </p>
 * @author Christopher Harper, account: admin
 * @version %version%
 * @since %since% */
public class Acl extends Persistent {

	/** A list of accessors.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:36:31 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * <li><code>accessors = new ArrayList<Accessor>();</code></li>
	 * </ul>
	 * @since %since% */
	private final List<Accessor>	accessors	= new ArrayList<Accessor>();
	/** The class of the ACL.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:38:27 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * <li><code>aclClass = 3;</code></li>
	 * </ul>
	 * @since %since% */
	private int						aclClass	= 3;
	/** Name of the ACL.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:38:46 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * @since %since% */
	private String					name		= null;
	/** The owner of the ACL.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:39:06 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * @since %since% */
	private String					owner		= null;

	/** <p>
	 * Add an ACL accessor.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:39:46 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param anAccessor
	 *            the accessor to add.
	 * @since %since% */
	public void addAccessor(final Accessor anAccessor) {
		this.accessors.add(anAccessor);
	}

	/** <p>
	 * Set the ACL class.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:52:17 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param theClass
	 *            the class of the ACL.
	 * @since %since% */
	public void setAclclass(final String theClass) {
		this.aclClass = Integer.parseInt(theClass);
	}

	/** <p>
	 * Set the name of the ACL.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:51:50 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param aName
	 *            the name of the ACL.
	 * @since %since% */
	public void setAclname(final String aName) {
		this.name = aName;
	}

	/** <p>
	 * Set the ACL owner name.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:51:26 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param anOwner
	 *            the owner name.
	 * @since %since% */
	public void setOwnername(final String anOwner) {
		this.owner = anOwner;
	}

	/** <p>
	 * Get the list of accessors.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:50:38 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return the list of accessors.
	 * @since %since% */
	protected List<Accessor> getAccessors() {
		return this.accessors;
	}

	/** <p>
	 * Get the ACL class.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:50:19 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return the class.
	 * @since %since% */
	protected int getAclClass() {
		return this.aclClass;
	}

	/** <p>
	 * Get the ACL name.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:49:50 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return the name.
	 * @since %since% */
	protected String getAclName() {
		return this.name;
	}

	/** <p>
	 * Get the qualification for an existing ACL object.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:45:54 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return the qualification.<code>
	 * 		dm_acl
	 * 	where
	 * 		object_name = 'name' and
	 * 		owner_name = 'owner'</code>
	 * @since %since% */
	@Override
	protected String getExistingQualification() {
		if (getOwnerName() == null && getAclName() == null) {
			throw new BuildException("Both acl name and acl owner must be provided."); //$NON-NLS-1$
		} else if ("dm_dbo".equalsIgnoreCase(getOwnerName())) { //$NON-NLS-1$
			try {
				setOwnername(getSession().getDocbaseOwnerName());
			} catch (final DfException dex) {
				printStack(dex);
				throw new BuildException("Failure returning repository owner name."); //$NON-NLS-1$
			}
		}
		/*-
		 *		dm_acl
		 *	where
		 *		object_name = 'name' and
		 *		owner_name = 'owner'
		 */
		return new StringBuilder().append(DCTMTask.DM_ACL).append(" where ").append(DCTMTask.OBJECT_NAME).append(" = '") //$NON-NLS-1$ //$NON-NLS-2$
				.append(getAclName()).append("' and ").append(DCTMTask.OWNER_NAME).append(" = '").append(getOwnerName()).append('\'') //$NON-NLS-1$ //$NON-NLS-2$
				.toString();
	}

	/** <p>
	 * Get the ACL owner name.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:43:56 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return the ACL name.
	 * @since %since% */
	protected String getOwnerName() {
		return this.owner;
	}

	/** <p>
	 * Get the type.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:42:47 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return dm_acl.
	 * @since %since% */
	@Override
	protected String getType() {
		return DCTMTask.DM_ACL;
	}

	/** <p>
	 * Execute ACL specific actions.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:41:48 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param acl
	 *            the ACL to work with.
	 * @since %since% */
	protected void typeSpesific(final IDfACL acl) {
		try {
			if (acl.isNew() || acl.isDirty() || reaplySettings()) {
				acl.setDescription(getDescription());
				acl.setACLClass(getAclClass());
				acl.setObjectName(getAclName());
				if (getOwnerName() == null || "dm_dbo".equalsIgnoreCase(getOwnerName())) { //$NON-NLS-1$
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

	/** <p>
	 * Perform type specific actions.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:40:38 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param tempObject
	 *            the object to work with.
	 * @since %since% */
	@Override
	protected void typeSpesific(final IDfPersistentObject tempObject) {
		typeSpesific((IDfACL) tempObject);
	}
}
/*-
 * $Log$
 */
