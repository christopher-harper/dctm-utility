package fi.sardion.dctm.deploy.util;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;


public abstract class Jar extends Task {
    public static final int TYPE_BOTH = 3;
    public static final int TYPE_IMPLEMENTATION = 2;
    public static final int TYPE_INTERFACE = 1;
    public static final int TYPE_UNKNOWN = 0;

    private String jar = null;

    private Module module;

    private String name;
    private int type = Jar.TYPE_UNKNOWN;

    public Jar() {
	super();
    }

    public String getJar() {
	return this.jar;
    }

    public Module getModule() {
	return this.module;
    }

    public String getName() {
	return this.name;
    }

    public int getType() {
	return this.type;
    }

    public void setJar(final String aJar) {
	this.jar = aJar;
    }

    public void setModule(final Module aModule) {
	this.module = aModule;
    }

    public void setName(final String aName) {
	this.name = aName;
    }

    public void setType(final String aType) {
	if ("implementation".equalsIgnoreCase(aType)) { //$NON-NLS-1$
	    this.type = Jar.TYPE_IMPLEMENTATION;
	} else if ("interface".equalsIgnoreCase(aType)) { //$NON-NLS-1$
	    this.type = Jar.TYPE_INTERFACE;
	} else if ("both".equalsIgnoreCase(aType)) { //$NON-NLS-1$
	    this.type = Jar.TYPE_BOTH;
	}
    }

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 17:55:49</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param moduleFolderId
     * @param exact
     * @return
     * @since %since%
     */
    @SuppressWarnings("boxing")
    protected IDfDocument getJarObject(final String moduleFolderId) {
	final StringBuilder qualification = new StringBuilder().append(
		DCTMTask.DMC_JAR).append(" where ") //$NON-NLS-1$
		.append(DCTMTask.OBJECT_NAME).append(" = '").append(getName()) //$NON-NLS-1$
		.append("' and ").append(DCTMTask.JAR_TYPE).append(" = ") //$NON-NLS-1$//$NON-NLS-2$
		.append(getType());
	final IDfId moduleId = new DfId(moduleFolderId);
	if (moduleId.isObjectId()
		&& (IDfId.DM_FOLDER == moduleId.getTypePart())) {
	    qualification.append(" and any ").append(DCTMTask.I_FOLDER_ID) //$NON-NLS-1$
		    .append(" = id('").append(moduleFolderId).append("')"); //$NON-NLS-1$ //$NON-NLS-2$
	} else {
	    throw new BuildException(String.format(
		    "Provided (%s) jar folder id must be a valid folder id.", //$NON-NLS-1$
		    new Object[] { moduleFolderId }));
	}
	try {
	    IDfDocument jarObject = (IDfDocument) getModule().getSession()
		    .getObjectByQualification(qualification.toString());
	    if (jarObject != null) {
		log(String.format("Found jar with qualification %s.", //$NON-NLS-1$
			new Object[] { qualification }));
	    } else {
		log(String
			.format(
				"Creating new %s object with type %s and name %s.",//$NON-NLS-1$
				new Object[] { DCTMTask.DMC_JAR, getType(),
					getName() }));
		jarObject = (IDfDocument) getModule().getSession().newObject(
			DCTMTask.DMC_JAR);
		jarObject.setObjectName(getName());
		jarObject.setInt(DCTMTask.JAR_TYPE, getType());
		jarObject.setContentType(DCTMTask.JAR);
		jarObject.appendString(DCTMTask.R_ASPECT_NAME,
			DCTMTask.MODULE_CHANGE_MONITOR);
		jarObject.link(moduleFolderId);
	    }
	    if ((getModule().reaplySettings() || jarObject.isDirty())) {
		if (!jarObject.isDirty()) {
		    jarObject.checkout();
		}
		File jarFile = new File(getJar());
		if (!jarFile.exists() || !jarFile.isFile()) {
		    log(String
			    .format(
				    "Jar file %s wasn't a file. Checking for other files.", //$NON-NLS-1$
				    new Object[] { jarFile.getAbsolutePath() }));
		    for (final File tempFile : jarFile.getParentFile()
			    .listFiles()) {
			if (tempFile.isFile()
				&& tempFile.getName().startsWith(getName())) {
			    jarFile = tempFile;
			    log(String.format("Using file %s instead.", //$NON-NLS-1$
				    new Object[] { jarFile.getAbsolutePath() }));
			}
		    }
		}
		if (jarFile.exists() && jarFile.isFile()) {
		    log(String
			    .format(
				    "Creating a new version of the %s jar with new content file %s", //$NON-NLS-1$
				    new Object[] { jarObject.getObjectName(),
					    jarFile.getAbsolutePath() }));
		    jarObject.setFile(jarFile.getAbsolutePath());
		    jarObject.setTitle(jarFile.getName());
		} else {
		    log(
			    String
				    .format(
					    "Failed to locate a jar file starting with %s in the directory %s.", //$NON-NLS-1$
					    new Object[] {
						    getName(),
						    jarFile.getParentFile()
							    .getAbsolutePath() }),
			    Project.MSG_WARN);
		}
		if (jarObject.isCheckedOut()) {
		    jarObject = (IDfDocument) getModule().getSession()
			    .getObject(jarObject.checkin(false, "")); //$NON-NLS-1$
		} else {
		    jarObject.save();
		}
	    }
	    if (getModule().isVerbose()) {
		log(
			String
				.format(
					"########## JAR OBJECT DUMP START ##########\n%s\n########### JAR OBJECT DUMP END ###########", //$NON-NLS-1$
					new Object[] { jarObject.dump() }),
			Project.MSG_INFO);
	    }

	    return jarObject;
	} catch (final DfException dex) {
	    final String message = String.format(
		    "Failed to deploy jar %s. Exception message:\n%s", //$NON-NLS-1$
		    new Object[] { getName(), dex.getStackTraceAsString() });
	    log(message, dex, Project.MSG_ERR);
	    throw new BuildException(message, dex);
	}
    }
}

/*-
 * $Log$
 */