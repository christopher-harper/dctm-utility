package fi.sardion.dctm.deploy.sbo;

import fi.sardion.dctm.deploy.util.Module;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 6 Jul 2010 11:03:03</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.tbo.Tbo</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 * 
 */
public class Sbo extends Module {

    public static final String SBO = "SBO"; //$NON-NLS-1$
    public static final String SBO_FOLDER = "/System/Modules/SBO"; //$NON-NLS-1$

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:20:54</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String servicename;

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 11:03:04</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Sbo() {
	super();
    }

    @Override
    public String getModuleFolderPath() {
	return Sbo.SBO_FOLDER;
    }

    @Override
    public String getModuleName() {
	return getServicename();
    }

    @Override
    public String getModuleType() {
	return Sbo.SBO;
    }

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:20:19</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public String getServicename() {
	return this.servicename;
    }

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:20:16</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aServicename
     * @since %since%
     */
    public void setServicename(final String aServicename) {
	this.servicename = aServicename;
    }
}

/*-
 * $Log$
 */