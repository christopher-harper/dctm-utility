package fi.sardion.dctm.deploy.module;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.method.Method;
import fi.sardion.dctm.deploy.util.DCTMTask;
import fi.sardion.dctm.deploy.util.Module;

/**
 * This is an ant task for creating and updating a dmc_module object. The
 * simplemmodule task is nested in the
 * {@link fi.sardion.dctm.deploy.session.Session session} task and must be
 * called <code><b>simplemodule</b></code>.
 * <p>
 * <ul>
 * <li>Created: 25 Jul 2010 13:37:19</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.module.Simple</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public class Simple extends Module {

    /**
     * The modules folder in the repository
     * <code>MODULE_FOLDER = "/System/Modules";</code>
     * <ul>
     * <li>Created: 19 Sep 2010 11:20:21</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public static final String MODULE_FOLDER = "/System/Modules"; //$NON-NLS-1$
    /**
     * Any methods defined for this module.
     * <ul>
     * <li>Created: 19 Sep 2010 11:21:04</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private final List<Method> methods = new ArrayList<Method>();
    /**
     * Name of the module.
     * <ul>
     * <li>Created: 19 Sep 2010 11:21:37</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private String moduleName = null;

    /**
     * Sole constructor.
     * <ul>
     * <li>Created: 19 Sep 2010 11:22:03</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Simple() {
	super();
    }

    /**
     * Add nested method tasks.
     * <ul>
     * <li>Created: 19 Sep 2010 11:22:19</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param method the nested method task.
     * @since %since%
     */
    public void addServermethod(final Method method) {
	this.methods.add(method);
    }

    /**
     * Execute the task work.
     * <ul>
     * <li>Created: 29 Sep 2010 10:20:39</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     * @see fi.sardion.dctm.deploy.util.Module#execute()
     */
    @Override
    public void execute() {
	final IDfSession session = getSession();
	try {
	    if (isForThisRepository(session.getDocbaseName())) {
		super.execute();
		for (final Method method : getServerMethods()) {
		    method.setSession(getSession());
		    method.setMethodverb(getModuleName());
		    method.setMethodtype(DCTMTask.JAVA);
		    method.execute();
		}
	    } else {
		log(String
		        .format(
		                "This simple module %s was only defined for repositories %s and the current repository is %s.", //$NON-NLS-1$
		                new Object[] { getModuleName(),
		                        getRepositories(),
		                        session.getDocbaseName() }));
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException("Failed to return repository name.", dex); //$NON-NLS-1$
	} catch (final Throwable t) {
	    printStack(t);
	    log(String.format("Shit!:\n%s\n%s", new Object[] { t.getMessage(), //$NON-NLS-1$
		    t.getStackTrace()[0].toString() }));
	}
    }

    /**
     * Get the module path in the repository.
     * <ul>
     * <li>Created: 29 Sep 2010 10:20:39</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     * @see fi.sardion.dctm.deploy.util.Module#getModuleFolderPath()
     */
    @Override
    public String getModuleFolderPath() {
	return Simple.MODULE_FOLDER;
    }

    /**
     * Get the name of the module.
     * <ul>
     * <li>Created: 29 Sep 2010 10:20:39</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     * @see fi.sardion.dctm.deploy.util.Module#getModuleName()
     */
    @Override
    public String getModuleName() {
	return this.moduleName;
    }

    /**
     * Get the type of the module.
     * <ul>
     * <li>Created: 29 Sep 2010 10:20:39</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     * @see fi.sardion.dctm.deploy.util.Module#getModuleType()
     */
    @Override
    public String getModuleType() {
	return Module.MODULE;
    }

    /**
     * Set the module name.
     * <ul>
     * <li>Created: 19 Sep 2010 11:24:05</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aModuleName the module name.
     * @since %since%
     */
    public void setModulename(final String aModuleName) {
	this.moduleName = aModuleName;
    }

    /**
     * Get the list of servermethod sub tasks.
     * <ul>
     * <li>Created: 19 Sep 2010 11:24:23</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the server method sub tasks.
     * @since %since%
     */
    protected List<Method> getServerMethods() {
	return this.methods;
    }
}
/*-
 * $Log$
 */
