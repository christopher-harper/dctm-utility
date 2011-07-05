package fi.sardion.dctm.deploy.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.util.DCTMTask;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 15 Sep 2010 09:16:36</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.script.Script</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public abstract class Script extends DCTMTask {

    private boolean failFast = false;
    private String script = null;

    @Override
    public void execute() {
	final IDfSession session = getSession();
	try {
	    if (isForThisRepository(session.getDocbaseName())) {
		final File execution = new File(getScript());
		if (execution.exists()) {
		    if (execution.isDirectory()) {
			executeDirectory(execution);
		    } else {
			executeFile(execution);
		    }
		} else {
		    executeScript(getScript());
		}
	    } else {
		log(String
		        .format(
		                "This script %s was only defined for repositories %s and the current repository is %s.", //$NON-NLS-1$
		                new Object[] { getScript(), getRepositories(),
		                        session.getDocbaseName() }));
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException("Failed to return repository name.", dex); //$NON-NLS-1$
	}
    }

    public void setFailfast(final String fail) {
	this.failFast = Boolean.parseBoolean(fail);
    }

    /**
     * <ul>
     * <li>Created: 5 Sep 2010 21:07:05</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aStatement
     * @since %since%
     */
    protected abstract void executeScript(final String aStatement);

    /**
     * <ul>
     * <li>Created: 5 Sep 2010 21:27:42</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected String getScript() {
	return this.script;
    }

    /**
     * <ul>
     * <li>Created: 5 Sep 2010 21:37:31</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected boolean isFailFast() {
	return this.failFast;
    }

    protected abstract boolean isTerminated(final String line);

    protected void setScript(final String aScript) {
	this.script = aScript;
    }

    /**
     * <ul>
     * <li>Created: 5 Sep 2010 21:13:58</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param execution
     * @since %since%
     */
    private void executeDirectory(final File execution) {
	if ("CVS".equals(execution.getName())) {
	    log(String
		    .format(
		            "Not executing directories with the name '%s' in path '%s'.", //$NON-NLS-1$
		            new Object[] { execution.getName(),
		                    execution.getParentFile().getAbsolutePath() }));
	} else {
	    log(String.format("Executing directory with name '%s' in path.", //$NON-NLS-1$
		    new Object[] { execution.getName(),
		            execution.getParentFile().getAbsolutePath() }));
	    for (final File file : execution.listFiles()) {
		if (file.isDirectory()) {
		    executeDirectory(file);
		} else {
		    executeFile(file);
		}
	    }
	}
    }

    /**
     * <ul>
     * <li>Created: 5 Sep 2010 21:27:31</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param execution
     * @since %since%
     */
    private void executeFile(final File execution) {
	log(String.format("Handling file: '%s' in path '%s'.", new Object[] { //$NON-NLS-1$
	        execution.getName(),
	                execution.getParentFile().getAbsolutePath() }));
	try {
	    final BufferedReader in = new BufferedReader(new FileReader(
		    execution));
	    String line;
	    StringBuilder builtDQL = new StringBuilder();
	    int counter = 0;
	    while ((line = in.readLine()) != null) {
		line = line.trim();
		if (!line.startsWith("#")) { //$NON-NLS-1$
		    if (isTerminated(line)) {
			executeScript(builtDQL.append(
			        System.getProperty("line.separator")).append( //$NON-NLS-1$
			        line).toString());
			counter++;
			builtDQL = new StringBuilder();
		    } else {
			builtDQL.append(line).append(' ');
		    }
		}
	    }
	    in.close();
	    log(String.format("Found %s DQL statements in file '%s'.", //$NON-NLS-1$
		    new Object[] { String.valueOf(counter),
		            execution.getAbsolutePath() }));
	} catch (final IOException ioex) {
	    printStack(ioex);
	    final String message = String.format("Failure reading file '%s'", //$NON-NLS-1$
		    new Object[] { execution.getAbsolutePath() });
	    if (isFailFast()) {
		throw new BuildException(message, ioex);
	    }
	    log(message, ioex, Project.MSG_WARN);
	}
    }
}
/*-
 * $Log$
 */
