package fi.sardion.dctm.deploy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

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
public abstract class DCTMTask extends Task {

    public static final String A_BOF_VERSION = "a_bof_version";
    public static final String A_EXTENDED_PROPERTIES = "a_extended_properties";
    public static final String A_INTERFACES = "a_interfaces";
    public static final String A_MODULE_TYPE = "a_module_type";
    public static final String A_NEXT_INVOCATION = "a_next_invocation";
    public static final String A_REQ_MODULE_INTERFACES = "a_req_module_interfaces";
    public static final String ALIAS_CATEGORY = "alias_category";
    public static final String ALIAS_DESCRIPTION = "alias_description";
    public static final String ALIAS_NAME = "alias_name";
    public static final String ALIAS_USR_CATEGORY = "alias_usr_category";
    public static final String ALIAS_VALUE = "alias_value";
    public static final String BOF_ACL = "BOF_acl";
    public static final String CHILD_PARENT_LABEL = "child_parent_label";
    public static final String CONTACT_INFO = "contact_info";
    public static final String DESCRIPTION = "description";
    public static final String DIRECTION_KIND = "direction_kind";
    public static final String DM_ACL = "dm_acl";
    public static final String DM_ALIAS_SET = "dm_alias_set";
    public static final String DM_BOF_DEPENDENCIES = "dm_bof_dependencies";
    public static final Object DM_CABINET = "dm_cabinet";
    public static final String DM_DOCUMENT = "dm_document";
    public static final String DM_FOLDER = "dm_folder";
    public static final String DM_GROUP = "dm_group";
    public static final String DM_JOB = "dm_job";
    public static final String DM_METHOD = "dm_method";
    public static final String DM_RELATION_TYPE = "dm_relation_type";
    public static final String DM_SYSOBJECT = "dm_sysobject";
    public static final String DM_WORLD = "dm_world";
    public static final String DMC_JAR = "dmc_jar";
    public static final String DMC_JAVA_LIBRARY = "dmc_java_library";
    public static final String DMC_MODULE = "dmc_module";
    public static final String EXPIRATION_DATE = "expiration_date";
    public static final String GROUP_NAME = "group_name";
    public static final String I_FOLDER_ID = "i_folder_id";
    public static final String IMPLEMENTATION_TECHNOLOGY = "implementation_technology";
    public static final String INACTIVATE_AFTER_FAILURE = "inactivate_after_failure";
    public static final String INTEGRITY_KIND = "integrity_kind";
    public static final String IS_INACTIVE = "is_inactive";
    public static final String IS_RESTARTABLE = "is_restartable";
    public static final String JAR = "jar";
    public static final String JAR_TYPE = "jar_type";
    public static final String JAVA = "java";
    public static final String LAUNCH_ASYNC = "launch_async";
    public static final String LAUNCH_DIRECT = "launch_direct";
    public static final String MAX_ITERATIONS = "max_iterations";
    public static final String METHOD_ARGS = "method_args";
    public static final String METHOD_ARGUMENTS = "method_arguments";
    public static final String METHOD_NAME = "method_name";
    public static final String METHOD_TYPE = "method_type";
    public static final String METHOD_VERB = "method_verb";
    public static final String MIN_DFC_VERSION = "min_dfc_version";
    public static final String MODULE_CHANGE_MONITOR = "com.documentum.fc.bof.bootstrap.DfModuleItemChangeMonitor";
    public static final String MODULE_DESCRIPTION = "module_description";
    public static final String OBJECT_DESCRIPTION = "object_description";
    public static final String OBJECT_NAME = "object_name";
    public static final String OWNER_NAME = "owner_name";
    public static final String PARENT_CHILD_LABEL = "parent_child_label";
    public static final String PASS_STANDARD_ARGUMENTS = "pass_standard_arguments";
    public static final String PRIMARY_CLASS = "primary_class";
    public static final String R_ACCESSOR_NAME = "r_accessor_name";
    public static final String R_ASPECT_NAME = "r_aspect_name";
    public static final String R_FOLDER_PATH = "r_folder_path";
    public static final String RELATION_NAME = "relation_name";
    public static final String REQ_MODULE_NAMES = "req_module_names";
    public static final String RUN_AS_SERVER = "run_as_server";
    public static final String RUN_INTERVAL = "run_interval";
    public static final String RUN_MODE = "run_mode";
    public static final String START_DATE = "start_date";
    public static final String SUCCESS_RETURN_CODES = "success_return_codes";
    public static final String SUCCESS_STATUS = "success_status";
    public static final String TARGET_SERVER = "target_server";
    public static final String TIMEOUT_DEFAULT = "timeout_default";
    public static final String TIMEOUT_MAX = "timeout_max";
    public static final String TIMEOUT_MIN = "timeout_min";
    public static final String TITLE = "title";
    public static final String TRACE_LAUNCH = "trace_launch";
    public static final String USE_METHOD_CONTENT = "use_method_content";
    public static final String USE_METHOD_SERVER = "use_method_server";
    /**
     * <ul>
     * <li>Created: 23 Jul 2010 16:48:18</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private boolean reaplySettings;
    private List<String> repositories = new ArrayList<String>();
    /**
     * A repository session.
     * <ul>
     * <li>Created: 6 Jul 2010 10:23:11</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private IDfSession session;
    private boolean verbose;

    /**
     * <ul>
     * <li>Created: 6 Jul 2010 09:05:20</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public DCTMTask() {
	super();
    }

    /**
     * Adopt a DMCL session.
     * <ul>
     * <li>Created: 6 Jul 2010 10:11:36</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the adopted session.
     * @since %since%
     */
    @SuppressWarnings("deprecation")
    public IDfSession getSession() {
	if (this.session == null) {
	    throw new BuildException("Session is null.");
	}
	return this.session;
    }

    /**
     * <ul>
     * <li>Created: 24 Jul 2010 12:18:41</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public boolean isVerbose() {
	return this.verbose;
    }

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:19:04</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return
     * @since %since%
     */
    public boolean reaplySettings() {
	return this.reaplySettings;
    }

    /**
     * <ul>
     * <li>Created: 23 Jul 2010 17:19:23</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param reaply
     * @since %since%
     */
    public void setReaplySettings(final String reaply) {
	this.reaplySettings = Boolean.parseBoolean(reaply);
    }

    public void setRepositories(final String theRepositories) {
	this.repositories = Arrays.asList(theRepositories.split(","));
    }

    public void setSession(final IDfSession aSession) {
	this.session = aSession;
    }

    /**
     * <ul>
     * <li>Created: 24 Jul 2010 12:18:44</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param isVerbose
     * @since %since%
     */
    public void setVerbose(final String isVerbose) {
	this.verbose = Boolean.parseBoolean(isVerbose);
    }

    protected void close(final IDfCollection collection) {
	if ((collection != null)
	        && (IDfCollection.DF_CLOSED_STATE != collection.getState())) {
	    try {
		collection.close();
	    } catch (final DfException dex) {
		log("Failed to close collection", dex, Project.MSG_WARN);
	    }
	}
    }

    protected List<String> getRepositories() {
	return this.repositories;
    }

    protected boolean isForThisRepository(final String aRepositoryName) {
	return (getRepositories().size() == 0)
	        || getRepositories().contains(aRepositoryName);
    }

    protected void printStack(final Throwable t) {
	log(t.getMessage());
	for (final StackTraceElement e : t.getStackTrace()) {
	    if (e != null) {
		log(e.toString());
	    } else {
		log("e null."); //$NON-NLS-1$
	    }
	}
    }
}
/*-
 * $Log$
 */
