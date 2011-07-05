package fi.sardion.dctm.deploy.util;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import com.documentum.fc.common.DfException;

public class BofJar extends Jar {
    public BofJar() {
	super();
    }

    @Override
    public void execute() {
	try {
	    final String moduleFolderId = getModule().getModuleFolder()
		    .getObjectId().getId();
	    getJarObject(moduleFolderId);
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