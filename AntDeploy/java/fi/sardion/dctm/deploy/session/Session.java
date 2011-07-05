package fi.sardion.dctm.deploy.session;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfLoginInfo;

import fi.sardion.dctm.deploy.method.Method;
import fi.sardion.dctm.deploy.module.Simple;
import fi.sardion.dctm.deploy.objectmodel.Type;
import fi.sardion.dctm.deploy.persistent.Acl;
import fi.sardion.dctm.deploy.persistent.AliasSet;
import fi.sardion.dctm.deploy.persistent.Document;
import fi.sardion.dctm.deploy.persistent.Folder;
import fi.sardion.dctm.deploy.persistent.Group;
import fi.sardion.dctm.deploy.persistent.RelationType;
import fi.sardion.dctm.deploy.persistent.Sys;
import fi.sardion.dctm.deploy.sbo.Sbo;
import fi.sardion.dctm.deploy.script.Api;
import fi.sardion.dctm.deploy.script.Dql;
import fi.sardion.dctm.deploy.tbo.Tbo;
import fi.sardion.dctm.deploy.util.DCTMTask;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 6 Jul 2010 09:05:20</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.Documentum</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public class Session extends Task {

    public static final String SESSION_PROPERTY = "session.id";
    private final String domain = null;
    private String password = null;
    private String repository = null;
    private final List<DCTMTask> tasks = new ArrayList<DCTMTask>();
    private String user = null;

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 09:05:20</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Session() {
	super();
    }

    public void addAcl(final Acl acl) {
	getTasks().add(acl);
    }

    public void addAliasset(final AliasSet aliasSet) {
	getTasks().add(aliasSet);
    }

    public void addApi(final Api api) {
	getTasks().add(api);
    }

    public void addDocugroup(final Group group) {
	getTasks().add(group);
    }

    public void addDocument(final Document document) {
	getTasks().add(document);
    }

    public void addDql(final Dql dql) {
	getTasks().add(dql);
    }

    public void addFolder(final Folder folder) {
	getTasks().add(folder);
    }

    public void addObjecttype(final Type aType) {
	getTasks().add(aType);
    }

    public void addRelationtype(final RelationType relationType) {
	getTasks().add(relationType);
    }

    public void addSbo(final Sbo sbo) {
	getTasks().add(sbo);
    }

    public void addServermethod(final Method method) {
	getTasks().add(method);
    }

    public void addSimplemodule(final Simple simpleModule) {
	getTasks().add(simpleModule);
    }

    public void addSysobject(final Sys sys) {
	getTasks().add(sys);
    }

    public void addTbo(final Tbo tbo) {
	getTasks().add(tbo);
    }

    @Override
    public void execute() {
	final IDfLoginInfo login = new DfLoginInfo(getUser(), getPassword());
	login.setDomain(getDomain());
	try {
	    log(String.format(
		    "Opening a session to repository %s for user %s.", //$NON-NLS-1$
		    new Object[] { getRepository(), getUser() }));
	    final IDfSession session = DfClient.getLocalClientEx().newSession(
		    getRepository(), login);
	    log("Session opened successfully"); //$NON-NLS-1$
	    log("Executing sub tasks."); //$NON-NLS-1$
	    for (final DCTMTask task : getTasks()) {
		if (task instanceof Type) {
		    log("Executing type task."); //$NON-NLS-1$		    
		} else if (task instanceof Dql) {
		    log("Executing dql task."); //$NON-NLS-1$
		} else if (task instanceof Api) {
		    log("Executing api task."); //$NON-NLS-1$
		} else if (task instanceof Group) {
		    log("Executing group task."); //$NON-NLS-1$
		} else if (task instanceof Acl) {
		    log("Executing ACL task."); //$NON-NLS-1$
		} else if (task instanceof Folder) {
		    log("Executing folder task."); //$NON-NLS-1$
		} else if (task instanceof Document) {
		    log("Executing document task."); //$NON-NLS-1$
		} else if (task instanceof Simple) {
		    log("Executing simple module task."); //$NON-NLS-1$
		} else if (task instanceof Method) {
		    log("Executing method task."); //$NON-NLS-1$
		} else if (task instanceof Sbo) {
		    log("Executing SBO task."); //$NON-NLS-1$
		} else if (task instanceof Tbo) {
		    log("Executing TBO task."); //$NON-NLS-1$
		} else if (task instanceof RelationType) {
		    log("Executing relation type task."); //$NON-NLS-1$
		} else if (task instanceof AliasSet) {
		    log("Executing alias set task."); //$NON-NLS-1$		    
		} else if (task instanceof Sys) {
		    log("Executing system object task."); //$NON-NLS-1$		    
		} else {
		    log(String.format("Executing %s task.", new Object[] { task //$NON-NLS-1$
			    .getClass().getName() }));
		}
		task.setSession(session);
		task.execute();
	    }
	} catch (final DfException dex) {
	    final String message = String
		    .format(
		            "Failed to open a session to repository %s for user %s. Exception returned:\n%s", //$NON-NLS-1$
		            new Object[] { getRepository(), getUser(),
		                    dex.getStackTraceAsString() });
	    log(message, dex, Project.MSG_ERR);
	    throw new BuildException(message, dex);
	}
    }

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 10:07:27</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public String getDomain() {
	return this.domain;
    }

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 10:07:38</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public String getRepository() {
	return this.repository;
    }

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 10:07:41</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public String getUser() {
	return this.user;
    }

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 10:07:14</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aPassword
     * @since %since%
     */
    public void setPassword(final String aPassword) {
	this.password = aPassword;
    }

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 10:07:18</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aRepository
     * @since %since%
     */
    public void setRepository(final String aRepository) {
	this.repository = aRepository;
    }

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 10:07:22</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aUser
     * @since %since%
     */
    public void setUser(final String aUser) {
	this.user = aUser;
    }

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 10:07:33</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    protected String getPassword() {
	return this.password;
    }

    protected List<DCTMTask> getTasks() {
	return this.tasks;
    }
}
/*-
 * $Log$
 */
