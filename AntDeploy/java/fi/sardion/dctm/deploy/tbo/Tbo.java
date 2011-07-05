package fi.sardion.dctm.deploy.tbo;

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
 */
public class Tbo extends Module {

    public static final String TBO = "TBO";
    public static final String TBO_FOLDER = "/System/Modules/TBO";
    private String typename;

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 11:03:04</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Tbo() {
	super();
    }

    @Override
    public String getModuleFolderPath() {
	return Tbo.TBO_FOLDER;
    }

    @Override
    public String getModuleName() {
	return getTypename();
    }

    @Override
    public String getModuleType() {
	return Tbo.TBO;
    }

    public String getTypename() {
	return this.typename;
    }

    public void setTypename(final String aTypename) {
	this.typename = aTypename;
    }
}
/*-
 * $Log$
 */
