package fi.sardion.dctm.deploy.util;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.IDfId;

public class BofLibraryJar extends Jar {
    public BofLibraryJar() {
	super();
    }

    @Override
    public void execute() {
	try {
	    final String moduleFolderId = getModule().getModuleFolder()
		    .getObjectId().getId();
	    try {
		String qualification = new StringBuilder().append(
			DCTMTask.DMC_JAVA_LIBRARY).append(" where ").append( //$NON-NLS-1$
			DCTMTask.OBJECT_NAME).append(" = '").append(getName()) //$NON-NLS-1$
			.append("' and any ").append(DCTMTask.I_FOLDER_ID) //$NON-NLS-1$
			.append(" = id('").append(moduleFolderId).append("')") //$NON-NLS-1$ //$NON-NLS-2$
			.toString();
		IDfId libraryFolderId = getModule().getSession()
			.getIdByQualification(qualification);
		if ((libraryFolderId == null) || libraryFolderId.isNull()) {
		    log(String
			    .format(
				    "Library folder not found foud with qualification %s.", //$NON-NLS-1$
				    new Object[] { qualification }));
		    qualification = new StringBuilder().append(
			    DCTMTask.DMC_JAVA_LIBRARY).append(" where ") //$NON-NLS-1$
			    .append(DCTMTask.OBJECT_NAME).append(" = '") //$NON-NLS-1$
			    .append(getName()).append('\'').toString();
		    IDfFolder libraryFolder = (IDfFolder) getModule()
			    .getSession().getObjectByQualification(
				    qualification);
		    if (libraryFolder == null) {
			log(String
				.format(
					"Library folder not found foud with qualification %s. Creating a new %s with name %s and linked to module folder with id '%s'.", //$NON-NLS-1$
					new Object[] { qualification,
						DCTMTask.DMC_JAVA_LIBRARY,
						getName(), moduleFolderId }));
			libraryFolder = (IDfFolder) getModule().getSession()
				.newObject(DCTMTask.DMC_JAVA_LIBRARY);
			libraryFolder.setObjectName(getName());
			libraryFolder.setACLDomain(getModule().getSession()
				.getDocbaseOwnerName());
			libraryFolder.setACLName(DCTMTask.BOF_ACL);
		    } else {
			log(String
				.format(
					"Library folder found foud with qualification %s. Linking to module folder with id '%s'.", //$NON-NLS-1$
					new Object[] { qualification,
						moduleFolderId }));
		    }
		    libraryFolder.link(moduleFolderId);
		    libraryFolder.addChildRelative(
			    DCTMTask.DM_BOF_DEPENDENCIES, new DfId(
				    moduleFolderId), null, true, String.format(
				    "%s to %s relation.", //$NON-NLS-1$
				    new Object[] { DCTMTask.DMC_JAVA_LIBRARY,
					    DCTMTask.DMC_MODULE }));
		    libraryFolder.save();
		    libraryFolderId = libraryFolder.getObjectId();
		    if (getModule().isVerbose()) {
			log(
				String
					.format(
						"########## LIBRARY FOLDER DUMP START ##########\n%s\n########### LIBRARY FOLDER DUMP END ###########", //$NON-NLS-1$
						new Object[] { libraryFolder
							.dump() }),
				Project.MSG_INFO);
		    }
		}
		getJarObject(libraryFolderId.getId());
	    } catch (final DfException dex) {
		final String message = String
			.format(
				"Failed to get or create the library folder. Exception recieved:\n%s", //$NON-NLS-1$
				new Object[] { dex.getStackTraceAsString() });
		log(message, dex, Project.MSG_ERR);
		throw new BuildException(message, dex);
	    }
	} catch (final DfException dex) {
	    final String message = String.format(
		    "Failed to get the module folder. Exception recieved:\n%s", //$NON-NLS-1$
		    new Object[] { dex.getStackTraceAsString() });
	    log(message, dex, Project.MSG_ERR);
	    throw new BuildException(message, dex);
	}
    }
}

/*-
 * $Log$
 */