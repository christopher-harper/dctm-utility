package fi.sardion.dctm.deploy.script;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 20 Aug 2010 09:57:27</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.dql.Dql</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public class Dql extends Script {

    /**
     * <ul>
     * <li>Created: 20 Aug 2010 09:57:37</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Dql() {
	super();
    }

    public void setDql(final String aDQL) {
	super.setScript(aDQL);
    }

    @Override
    protected void executeScript(final String dqlStatement) {
	final IDfSession session = getSession();
	try {
	    close(new DfQuery(dqlStatement).execute(session,
		    IDfQuery.EXEC_QUERY));
	    log(dqlStatement);
	} catch (final DfException dex) {
	    printStack(dex);
	    final String message = String.format(
		    "Failure executing DQL statement: '%s'.", //$NON-NLS-1$
		    new Object[] { dqlStatement });
	    if (isFailFast()) {
		throw new BuildException(message, dex);
	    }
	    log(message, dex, Project.MSG_WARN);
	}
    }

    @Override
    protected boolean isTerminated(final String line) {
	return line.endsWith("go") || line.endsWith(";"); //$NON-NLS-1$ //$NON-NLS-2$
    }

}
/*-
 * $Log$
 */