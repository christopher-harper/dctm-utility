package fi.sardion.dctm.deploy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 6 Jul 2010 15:47:09</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.Module</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public abstract class Module extends DCTMTask {

    public static final String MODULE = "Module";
    private final List<BofJar> bofJars = new ArrayList<BofJar>();
    private final List<BofLibraryJar> bofLibraries = new ArrayList<BofLibraryJar>();
    /**
     * <ul>
     * <li>Created: 23 Jul 2010 16:48:03</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String contactInfo = null;
    /**
     * <ul>
     * <li>Created: 23 Jul 2010 20:34:02</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String dfcVersion = null;
    /**
     * <ul>
     * <li>Created: 23 Jul 2010 16:47:57</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private List<String> interfaces = null;
    /**
     * <ul>
     * <li>Created: 23 Jul 2010 16:48:10</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private IDfFolder module = null;
    private String moduleDescription = null;
    /**
     * <ul>
     * <li>Created: 23 Jul 2010 16:48:13</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String primaryClass = null;
    private List<String> requiredModuleInterfaces;
    private List<String> requiredModuleNames = null;
    private String version = null;

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 15:47:10</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Module() {
	super();
    }

    public void addBofjar(final BofJar jar) {
	this.bofJars.add(jar);
    }

    public void addBoflibrary(final BofLibraryJar library) {
	this.bofLibraries.add(library);
    }

    @Override
    public void execute() {
	final IDfSession session = getSession();
	try {
	    if (isForThisRepository(session.getDocbaseName())) {
		for (final BofJar bofJar : getBofjars()) {
		    bofJar.setModule(this);
		    bofJar.execute();
		}
		for (final BofLibraryJar bofLibrary : getBofLibraries()) {
		    bofLibrary.setModule(this);
		    bofLibrary.execute();
		}
	    } else {
		log(String
		        .format(
		                "This module %s was only defined for repositories %s and the current repository is %s.", //$NON-NLS-1$
		                new Object[] { getModuleName(),
		                        getRepositories(),
		                        session.getDocbaseName() }));
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException("Failed to return repository name.", dex); //$NON-NLS-1$
	}
    }

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 16:48:23</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public String getContactinfo() {
	return this.contactInfo;
    }

    public String getMindfcversion() {
	return this.dfcVersion;
    }

    public String getModuledescription() {
	return this.moduleDescription;
    }

    public IDfFolder getModuleFolder() {
	final IDfSession session = getSession();
	if (this.module == null) {
	    try {
		final String qualification = new StringBuilder().append(
		        DCTMTask.DMC_MODULE).append(" where ").append( //$NON-NLS-1$
		        DCTMTask.OBJECT_NAME).append(" = '").append( //$NON-NLS-1$
		        getModuleName()).append("' and ").append( //$NON-NLS-1$
		        DCTMTask.A_MODULE_TYPE).append(" = '").append( //$NON-NLS-1$
		        getModuleType()).append('\'').toString();
		this.module = (IDfFolder) session
		        .getObjectByQualification(qualification);
		if (this.module == null) {
		    this.module = (IDfFolder) session
			    .newObject(DCTMTask.DMC_MODULE);
		    this.module.setObjectName(getModuleName());
		    this.module.setString(DCTMTask.A_MODULE_TYPE,
			    getModuleType());
		    this.module.setString(DCTMTask.IMPLEMENTATION_TECHNOLOGY,
			    DCTMTask.JAVA);
		    this.module.setACLName(DCTMTask.BOF_ACL);
		    this.module.setACLDomain(session.getDocbaseOwnerName());
		    this.module.link(getModuleFolderPath());
		    setReaplySettings(String.valueOf(true));
		    log(String
			    .format(
			            "Created module with name '%s' and linked it to path %s.", //$NON-NLS-1$
			            new Object[] { getModuleName(),
			                    getModuleFolderPath() }));
		} else {
		    log(String
			    .format(
			            "Returned module with name '%s' using qualification %s.", //$NON-NLS-1$
			            new Object[] { getModuleName(),
			                    qualification }));
		}
		if (reaplySettings()) {
		    if (!this.module.isNew()) {
			log("Reaplying module values."); //$NON-NLS-1$
		    }
		    this.module.setString(DCTMTask.PRIMARY_CLASS,
			    getPrimaryclass());
		    this.module.setString(DCTMTask.CONTACT_INFO,
			    getContactinfo());
		    this.module.removeAll(DCTMTask.A_INTERFACES);
		    if (getInterfaces() != null) {
			for (final String interfaceName : getInterfaces()) {
			    this.module.appendString(DCTMTask.A_INTERFACES,
				    interfaceName);
			}
		    }
		    this.module.setString(DCTMTask.A_BOF_VERSION, getVersion());
		    this.module.setString(DCTMTask.MIN_DFC_VERSION,
			    getMindfcversion());
		    this.module.setString(DCTMTask.MODULE_DESCRIPTION,
			    getModuledescription());
		    this.module.removeAll(DCTMTask.REQ_MODULE_NAMES);
		    if (getRequiredmodulenames() != null) {
			for (final String moduleName : getRequiredmodulenames()) {
			    this.module.appendString(DCTMTask.REQ_MODULE_NAMES,
				    moduleName);
			}
		    }
		    this.module.removeAll(DCTMTask.A_REQ_MODULE_INTERFACES);
		    if (getRequiredmoduleinterfaces() != null) {
			for (final String moduleInterfaceName : getRequiredmoduleinterfaces()) {
			    this.module.appendString(
				    DCTMTask.A_REQ_MODULE_INTERFACES,
				    moduleInterfaceName);
			}
		    }
		}
		if (this.module.isDirty()) {
		    this.module.save();
		}
		if (isVerbose()) {
		    log(
			    String
			            .format(
			                    "########## MODULE DUMP START ##########\n%s\n########### MODULE DUMP END ###########", //$NON-NLS-1$
			                    new Object[] { this.module.dump() }),
			    Project.MSG_INFO);
		}
	    } catch (final DfException dex) {
		final String message = String.format(
		        "Failed to get module with name '%s'.", //$NON-NLS-1$
		        new Object[] { getModuleName() });
		log(message, dex, Project.MSG_ERR);
		throw new BuildException(message, dex);
	    }
	}
	return this.module;
    }

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:18:42</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public abstract String getModuleFolderPath();

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:18:45</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public abstract String getModuleName();

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:18:49</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public abstract String getModuleType();

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:18:58</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public String getPrimaryclass() {
	return this.primaryClass;
    }

    public List<String> getRequiredmoduleinterfaces() {
	return this.requiredModuleInterfaces;
    }

    public List<String> getRequiredmodulenames() {
	return this.requiredModuleNames;
    }

    public String getVersion() {
	return this.version;
    }

    public void setContactinfo(final String info) {
	this.contactInfo = info;
    }

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:19:09</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param interfaces
     * @since %since%
     */
    public void setInterfaces(final String interfaces) {
	this.interfaces = Arrays.asList(interfaces.split(","));
    }

    public void setMindfcversion(final String aDfcVersion) {
	this.dfcVersion = aDfcVersion;
    }

    public void setModuledescription(final String aDescription) {
	this.moduleDescription = aDescription;
    }

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:19:17</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param className
     * @since %since%
     */
    public void setPrimaryclass(final String className) {
	this.primaryClass = className;
    }

    /**
     * <ul>
     * <li>Created: 24 Jul 2010 12:51:30</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param theRequiredModuleInterfaces
     * @since %since%
     */
    public void setRequiredmoduleinterfaces(
	    final String theRequiredModuleInterfaces) {
	this.requiredModuleInterfaces = Arrays
	        .asList(theRequiredModuleInterfaces.split(","));
    }

    /**
     * <ul>
     * <li>Created: 24 Jul 2010 12:51:34</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param moduleNames
     * @since %since%
     */
    public void setRequiredmodulenames(final String moduleNames) {
	this.requiredModuleNames = Arrays.asList(moduleNames.split(","));
    }

    /**
     * <ul>
     * <li>Created: 24 Jul 2010 12:51:38</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aVersion
     * @since %since%
     */
    public void setVersion(final String aVersion) {
	this.version = aVersion;
    }

    /**
     * <ul>
     * <li>Created: 24 Jul 2010 12:51:41</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected List<BofJar> getBofjars() {
	return this.bofJars;
    }

    /**
     * <ul>
     * <li>Created: 24 Jul 2010 12:51:43</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected List<BofLibraryJar> getBofLibraries() {
	return this.bofLibraries;
    }

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 14:12:43</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected List<String> getInterfaces() {
	return this.interfaces;
    }
}
/*-
 * $Log$
 */
