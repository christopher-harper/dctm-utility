package fi.sardion.dctm.deploy.script;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 15 Sep 2010 09:42:47</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.script.Api</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public class Api extends Script {

    /**
     * <ul>
     * <li>Created: 15 Sep 2010 09:42:47</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Api() {
	super();
    }

    public void setApi(final String anApi) {
	setScript(anApi);
    }

    /*
     * (non-Javadoc)
     * @see fi.sardion.dctm.deploy.script.Script#executeScript(java.lang.String)
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void executeScript(final String aStatement) {
	final String[] lines = aStatement.trim().split(
	        System.getProperty("line.separator")); //$NON-NLS-1$
	String command = lines[0];
	String data = ""; //$NON-NLS-1$
	final int commaIndex = lines[0].indexOf(',');
	if (commaIndex > 2) {
	    command = lines[0].substring(0, commaIndex);
	    data = lines[0]
		    .substring(lines[0].indexOf(',', commaIndex + 1) + 1);
	}
	try {
	    log(aStatement);
	    switch (getSession().apiDesc(command).getInt(2)) {
		case IDfSession.DM_EXEC:
		    log(String.valueOf(getSession().apiExec(command, data)));
		    break;
		case IDfSession.DM_GET:
		    log(getSession().apiGet(command, data));
		    break;
		case IDfSession.DM_SET:
		    log(String.valueOf(getSession().apiSet(command, data,
			    lines[1])));
		    break;
		default:
		    log(String
			    .format(
			            "Unknow api command: '%s'. Skiping execution of:\n", //$NON-NLS-1$
			            new Object[] { command, aStatement }));
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    if (isFailFast()) {
		throw new BuildException(String.format(
		        "Failed to execute API: '%s'.", //$NON-NLS-1$
		        new Object[] { aStatement }), dex);
	    }
	    log(String.format("Failed to execute api command %s with data %s.", //$NON-NLS-1$
		    new Object[] { command, data }), dex, Project.MSG_WARN);
	}
    }

    /*
     * (non-Javadoc)
     * @see fi.sardion.dctm.deploy.script.Script#isTerminated(java.lang.String)
     */
    @Override
    protected boolean isTerminated(final String line) {
	return !line.startsWith("set,"); //$NON-NLS-1$
    }
}
/*-
 * $Log$
 */
