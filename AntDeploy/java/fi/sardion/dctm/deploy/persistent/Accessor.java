package fi.sardion.dctm.deploy.persistent;

import org.apache.tools.ant.Task;

/** <p>
 * Accessor entry for the ACL task.
 * <ul>
 * <li>Created: Jul 19, 2011 12:18:20 PM</li>
 * <li>Project: AntDeploy</li>
 * <li>File: fi.sardion.dctm.deploy.persistent.Accessor</li>
 * </ul>
 * </p>
 * @author Christopher Harper, account: admin
 * @version %version%
 * @since %since% */
public class Accessor extends Task {

	/** Accessors application permit.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:18:57 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * @since %since% */
	private String	applicationPermit	= "";					//$NON-NLS-1$
	/** Accessors extended permit.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:19:48 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * @since %since% */
	private String	extendedPermit		= null;
	/** The accessor name.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:20:19 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * @since %since% */
	private String	name;
	/** The accessor permit.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:20:41 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * @since %since% */
	private String	permit				= String.valueOf(1);
	/** Type of the permit given to the accessor.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:21:22 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * <li><code>permitType = ;</code></li>
	 * </ul>
	 * @since %since% */
	private int		permitType			= 0;

	/** <p>
	 * Set the application permit.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:22:02 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param anApplicationPermit
	 *            the application permit to grant.
	 * @since %since% */
	public void setApplicationpermit(final String anApplicationPermit) {
		this.applicationPermit = anApplicationPermit;
	}

	/** <p>
	 * Set extended permit.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:24:48 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param anExtendedPermit
	 *            the extended permit.
	 * @since %since% */
	public void setExtendedpermit(final String anExtendedPermit) {
		this.extendedPermit = anExtendedPermit;
	}

	/** <p>
	 * Set the accessor name.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:25:23 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param aName
	 *            the name.
	 * @since %since% */
	public void setName(final String aName) {
		this.name = aName;
	}

	/** <p>
	 * Set the accessor permit.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:25:59 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param aPermit
	 *            the permit.
	 * @since %since% */
	public void setPermit(final String aPermit) {
		this.permit = String.valueOf(Integer.parseInt(aPermit));
	}

	/** <p>
	 * Set the permit type.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:26:27 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @param aPermitType
	 *            the permit type.
	 * @since %since% */
	public void setPermittype(final String aPermitType) {
		this.permitType = Integer.parseInt(aPermitType);
	}

	/** <p>
	 * Get the application permit.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:26:48 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return the application permit.
	 * @since %since% */
	protected String getApplicationPermit() {
		return this.applicationPermit;
	}

	/** <p>
	 * Get the extended permit.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:27:31 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return extended permit.
	 * @since %since% */
	protected String getExtendedPermit() {
		return this.extendedPermit;
	}

	/** <p>
	 * Get the accessor name.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:27:52 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return the accessor name.
	 * @since %since% */
	protected String getName() {
		return this.name;
	}

	/** <p>
	 * Get the permit.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:28:38 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return the permit.
	 * @since %since% */
	protected String getPermit() {
		return this.permit;
	}

	/** <p>
	 * Get the permit type.
	 * <ul>
	 * <li>Created: Jul 19, 2011 12:28:26 PM</li>
	 * <li>Author: Christopher Harper, account: admin</li>
	 * </ul>
	 * </p>
	 * @return the permit type.
	 * @since %since% */
	protected int getPermitType() {
		return this.permitType;
	}
}
/*-
 * $Log$
 */
