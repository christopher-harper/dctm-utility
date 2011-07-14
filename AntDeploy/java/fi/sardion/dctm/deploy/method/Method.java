package fi.sardion.dctm.deploy.method;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.Project;

import com.documentum.fc.client.IDfACL;
import com.documentum.fc.client.IDfMethodObject;
import com.documentum.fc.client.IDfSession;

import fi.sardion.dctm.deploy.job.Job;
import fi.sardion.dctm.deploy.util.DCTMTask;

/** This is an ant task for creating and updating a dm_method object. The method task is nested in the
 * {@link fi.sardion.dctm.deploy.session.Session session} task and must be called <code><b>servermethod</b></code>.
 * <p>
 * <ul>
 * <li>Created: 18 Sep 2010 18:53:36</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.method.Method</li>
 * </ul>
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since% */
public class Method extends DCTMTask {

	/** Extended method properties. Defaults to just one entry: <code>JMS_LOCATION=ANY</code>.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:54:50</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private List<String>		extendedProperties	= Arrays.asList(new String[] { "JMS_LOCATION=ANY" });	//$NON-NLS-1$
	/** Can the method be restarted. Defaults to false.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:55:42</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private boolean				isRestartable		= false;
	/** Add possible job objects for this method. A single method can have multiple jobs that execute the same method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:56:08</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private final List<Job>		jobs				= new ArrayList<Job>();
	/** Launch the method asynchronously. Defaults to false.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:59:30</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private boolean				launchAsync			= false;
	/** Launch the method immediately. Defaults to true.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:00:08</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private boolean				launchDirect		= true;
	/** A list of arguments passed to the method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:00:43</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private List<String>		methodArguments		= new ArrayList<String>();
	/** Name of the method that uniquely identifies the method in the repository.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:01:09</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private String				methodName			= null;
	/** Type of the method. Defaults to java.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:01:31</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private String				methodType			= DCTMTask.JAVA;
	/** Command to launch the method or the primary class name if a java method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:01:56</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private String				methodVerb			= null;
	/** Will the job be executed using the system account (super user).
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:02:31</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private boolean				runAsServer			= true;
	/** List of success return codes. <b>NOTE</b>: Values are integers.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:04:28</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private final List<Integer>	successReturnCodes	= new ArrayList<Integer>();
	/** Success status.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:05:14</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private String				successStatus		= null;
	/** Default timeout. Defaults to 150.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:05:25</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private int					timeoutDefault		= 150;
	/** Maximum timeout. Defaults to 300.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:05:57</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private int					timeoutMax			= 300;
	/** Minimum timeout. Defaults to 75.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:06:19</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private int					timeoutMin			= 75;
	/** Should the launch be traced in the server log. Defaults to false.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:07:11</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private boolean				traceLaunch			= false;
	/** Use the method object content as the file to execute. Defaults to false.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:07:37</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private boolean				useMethodContent	= false;
	/** Whether to use the method server for execution. Defaults to true.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:08:20</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private boolean				useMethodServer		= true;

	/** Sole constructor.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:08:51</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	public Method() {
		super();
	}

	/** Add a job that uses this method as its execution method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:09:02</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aJob
	 *            the job ant task definition.
	 * @since %since% */
	public void addJob(final Job aJob) {
		this.jobs.add(aJob);
	}

	/** Execute the method task and all defined {@link fi.sardion.dctm.deploy.job.Job job} subtasks.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:10:07</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	@Override
	public void execute() {
		final IDfSession session = getSession();
		try {
			if (isForThisRepository(session.getDocbaseName())) {
				final String qualification = new StringBuilder().append(DCTMTask.DM_METHOD).append(" where ").append( //$NON-NLS-1$
						DCTMTask.OBJECT_NAME).append(" = '") //$NON-NLS-1$
						.append(getMethodName()).append('\'').toString();
				IDfMethodObject method = (IDfMethodObject) session.getObjectByQualification(qualification);
				if (method == null) {
					log(String.format("Qualification %s din't return a method. Creating a new one.", //$NON-NLS-1$
							new Object[] { qualification }));
					method = (IDfMethodObject) session.newObject(DCTMTask.DM_METHOD);
					method.setObjectName(getMethodName());
					method.grant(DCTMTask.DM_WORLD, IDfACL.DF_PERMIT_READ, ""); //$NON-NLS-1$
					// method.link(Method.METHOD_FOLDER);
					setReaplySettings(String.valueOf(true));
				} else {
					log(String.format("Found method with qualification %s.", //$NON-NLS-1$
							new Object[] { qualification }));
				}
				if (reaplySettings()) {
					if (!method.isDirty()) {
						method.checkout();
						log(String.format("Modifying existing method %s values.", //$NON-NLS-1$
								new Object[] { getMethodName() }));
					}
					method.setMethodVerb(getMethodVerb());
					method.setTimeoutMin(getTimeoutMin());
					method.setTimeoutMax(getTimeoutMax());
					method.setTimeoutDefault(getTimeoutDefault());
					method.setLaunchDirect(getLaunchDirect());
					method.setLaunchAsync(getLaunchAsync());
					method.setTraceLaunch(getTraceLaunch());
					method.setRunAsServer(getRunAsServer());
					method.setUseMethodContent(getUseMethodContent());
					method.setMethodType(getMethodType());
					method.setUseMethodServer(getUseMethodServer());
					method.setSuccessStatus(getSuccessStatus());
					method.setBoolean(DCTMTask.IS_RESTARTABLE, getIsRestartable());
					method.removeAll(DCTMTask.SUCCESS_RETURN_CODES);
					for (final Integer successReturnCode : getSuccessReturnCodes()) {
						method.appendInt(DCTMTask.SUCCESS_RETURN_CODES, successReturnCode.intValue());
					}
					method.removeAll(DCTMTask.METHOD_ARGS);
					for (final String methodArgument : getMethodArguments()) {
						method.appendString(DCTMTask.METHOD_ARGS, methodArgument);
					}
					method.removeAll(DCTMTask.A_EXTENDED_PROPERTIES);
					for (final String extededProperty : getExtendedProperties()) {
						method.appendString(DCTMTask.A_EXTENDED_PROPERTIES, extededProperty);
					}
				}
				if (method.isDirty()) {
					if (method.isCheckedOut()) {
						method = (IDfMethodObject) session.getObject(method.checkin(false, "")); //$NON-NLS-1$
					} else {
						method.save();
					}
				}
				if (isVerbose()) {
					log(String.format("########## METHOD OBJECT DUMP START ##########\n%s\n########### METHOD OBJECT DUMP END ###########", //$NON-NLS-1$
							new Object[] { method.dump() }), Project.MSG_INFO);
				}
			} else {
				log(String.format("This method %s was only defined for repositories %s and the current repository is %s.", //$NON-NLS-1$
						new Object[] { getMethodName(), getRepositories(), session.getDocbaseName() }));
			}
		} catch (final Throwable t) {
			printStack(t);
			log(String.format("Shit!:\nclass: %s\nmessage: %s\nstack: %s", //$NON-NLS-1$
					new Object[] { t.getClass().getName(), t.getMessage(), t.getStackTrace()[0].toString() }));
		}
		for (final Job job : getJobs()) {
			job.setSession(session);
			job.setMethodname(getMethodName());
			job.execute();
		}
	}

	/** Set the extended properties. <b>NOTE</b>: This is a comma (,) separated list that is turned to a List using
	 * <code>Arrays.asList(String.split(","));</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:13:17</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param theExtendedProperties
	 *            the extended properties.
	 * @since %since% */
	public void setExtendedproperties(final String theExtendedProperties) {
		this.extendedProperties = Arrays.asList(theExtendedProperties.split(",")); //$NON-NLS-1$
	}

	/** Set whether the job is restartable. <b>NOTE</b>: String value passed in is transformed to a boolean using
	 * <code>Boolean.parseBoolean(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:14:51</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param restartable
	 *            can the job be restarted.
	 * @since %since% */
	public void setIsrestartable(final String restartable) {
		this.isRestartable = Boolean.parseBoolean(restartable);
	}

	/** Set whether the method should be launched asynchronously. <b>NOTE</b>: String value passed in is transformed to a boolean using
	 * <code>Boolean.parseBoolean(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:26:53</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param whetherToLaunchAsync1
	 *            should the method should be launched asynchronously.
	 * @since %since% */
	public void setLaunchasync(final String whetherToLaunchAsync1) {
		this.launchAsync = Boolean.parseBoolean(whetherToLaunchAsync1);
	}

	/** Should the method should be launched immediately. <b>NOTE</b>: String value passed in is transformed to a boolean using
	 * <code>Boolean.parseBoolean(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:28:31</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aLaunchDirect
	 *            should the method should be launched immediately.
	 * @since %since% */
	public void setLaunchdirect(final String aLaunchDirect) {
		this.launchDirect = Boolean.parseBoolean(aLaunchDirect);
	}

	/** Set the method arguments. <b>NOTE</b>: This is a comma (,) separated list that is turned to a List using
	 * <code>Arrays.asList(String.split(","));</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:30:02</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param theMethodArguments
	 *            arguments save on the method object.
	 * @since %since% */
	public void setMethodarguments(final String theMethodArguments) {
		this.methodArguments = Arrays.asList(theMethodArguments.split(",")); //$NON-NLS-1$
	}

	/** Set the name of the method. The name uniquely identifies the method in the repository.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:12:42</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aMethodName
	 *            name of the method.
	 * @since %since% */
	public void setMethodname(final String aMethodName) {
		this.methodName = aMethodName;
	}

	/** Sets the method type.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:31:08</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param theMethodType
	 *            type of the method.
	 * @since %since% */
	public void setMethodtype(final String theMethodType) {
		this.methodType = theMethodType;
	}

	/** Sets the method verb.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:31:36</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aMethodVerb
	 *            the method verb.
	 * @since %since% */
	public void setMethodverb(final String aMethodVerb) {
		this.methodVerb = aMethodVerb;
	}

	/** Set whether to run as super user. <b>NOTE</b>: String value passed in is transformed to a boolean using
	 * <code>Boolean.parseBoolean(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:32:05</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aRunAsServer
	 *            whether to run as server.
	 * @since %since% */
	public void setRunasserver(final String aRunAsServer) {
		this.runAsServer = Boolean.parseBoolean(aRunAsServer);
	}

	/** Sets the success return codes. <b>NOTE</b>: This is a comma (,) separated list that is turned to a List using
	 * <code>Arrays.asList(String.split(","));</code> and then each element is converted to an Integer using <code>new Integer(code)</code>.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:33:11</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param theSuccessReturnCodes
	 *            the success return codes.
	 * @since %since% */
	public void setSuccessreturncodes(final String theSuccessReturnCodes) {
		final List<String> codes = Arrays.asList(theSuccessReturnCodes.split(",")); //$NON-NLS-1$
		for (final String code : codes) {
			this.successReturnCodes.add(new Integer(code));
		}
	}

	/** Sets the success status.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:35:26</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aSuccessStatus
	 *            the success status.
	 * @since %since% */
	public void setSuccessstatus(final String aSuccessStatus) {
		this.successStatus = aSuccessStatus;
	}

	/** Sets the default timeout. <b>NOTE</b>: String value passed in is transformed to a int using <code>Integer.parseInt(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:35:58</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param timeOut
	 *            default timeout.
	 * @since %since% */
	public void setTimeoutdefault(final String timeOut) {
		this.timeoutDefault = Integer.parseInt(timeOut);
	}

	/** Sets the maximum timeout. <b>NOTE</b>: String value passed in is transformed to a int using <code>Integer.parseInt(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:36:23</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param timeOut
	 *            maximum timeout.
	 * @since %since% */
	public void setTimeoutmax(final String timeOut) {
		this.timeoutMax = Integer.parseInt(timeOut);
	}

	/** Sets the minimum timeout. <b>NOTE</b>: String value passed in is transformed to a int using <code>Integer.parseInt(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:36:48</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param timeOut
	 *            the minimum timeout.
	 * @since %since% */
	public void setTimeoutmin(final String timeOut) {
		this.timeoutMin = Integer.parseInt(timeOut);
	}

	/** Sets whether to trace the launch. <b>NOTE</b>: String value passed in is transformed to a boolean using
	 * <code>Boolean.parseBoolean(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:38:13</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aTraceLaunch
	 *            whether to trace the launch.
	 * @since %since% */
	public void setTracelaunch(final String aTraceLaunch) {
		this.traceLaunch = Boolean.parseBoolean(aTraceLaunch);
	}

	/** Sets whether to use the method content. <b>NOTE</b>: String value passed in is transformed to a boolean using
	 * <code>Boolean.parseBoolean(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:38:48</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aUseMethodContent
	 *            should the method content be used.
	 * @since %since% */
	public void setUsemethodcontent(final String aUseMethodContent) {
		this.useMethodContent = Boolean.parseBoolean(aUseMethodContent);
	}

	/** Sets whether to use the method server. <b>NOTE</b>: String value passed in is transformed to a boolean using
	 * <code>Boolean.parseBoolean(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:40:54</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aUseMehtodServer
	 *            should a method server be used.
	 * @since %since% */
	public void setUsemethodserver(final String aUseMehtodServer) {
		this.useMethodServer = Boolean.parseBoolean(aUseMehtodServer);
	}

	/** Get the extended properties.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:41:53</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the extended properties.
	 * @since %since% */
	protected List<String> getExtendedProperties() {
		return this.extendedProperties;
	}

	/** Get whether the method can be restarted.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:42:15</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return whether the method can be restarted.
	 * @since %since% */
	protected boolean getIsRestartable() {
		return this.isRestartable;
	}

	/** Get should the method be started asynchronously.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:42:43</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return should the method be started asynchronously.
	 * @since %since% */
	protected boolean getLaunchAsync() {
		return this.launchAsync;
	}

	/** Should the method be started immediately.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:43:33</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return whether the method is started immediately.
	 * @since %since% */
	protected boolean getLaunchDirect() {
		return this.launchDirect;
	}

	/** Get the list of method arguments.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:44:33</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the list of method arguments.
	 * @since %since% */
	protected List<String> getMethodArguments() {
		return this.methodArguments;
	}

	/** Get the name of the method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:45:04</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the name of the method.
	 * @since %since% */
	protected String getMethodName() {
		return this.methodName;
	}

	/** Get the type of the method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:45:24</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the method type.
	 * @since %since% */
	protected String getMethodType() {
		return this.methodType;
	}

	/** Get the method verb.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:45:53</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the method verb.
	 * @since %since% */
	protected String getMethodVerb() {
		return this.methodVerb;
	}

	/** Get whether to run the method with a super user account.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:48:09</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return whether to run the method as the server.
	 * @since %since% */
	protected boolean getRunAsServer() {
		return this.runAsServer;
	}

	/** Get the success return codes.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:49:12</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the success return codes.
	 * @since %since% */
	protected List<Integer> getSuccessReturnCodes() {
		return this.successReturnCodes;
	}

	/** Set the success status.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:50:06</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the success status.
	 * @since %since% */
	protected String getSuccessStatus() {
		return this.successStatus;
	}

	/** Get the timeout default.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:50:35</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the timeout default.
	 * @since %since% */
	protected int getTimeoutDefault() {
		return this.timeoutDefault;
	}

	/** Get the maximum timeout.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:51:10</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the maximum timeout.
	 * @since %since% */
	protected int getTimeoutMax() {
		return this.timeoutMax;
	}

	/** Get the timeout minimum.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:51:55</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the timeout minimum.
	 * @since %since% */
	protected int getTimeoutMin() {
		return this.timeoutMin;
	}

	/** Get should the launch should be traced.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:52:29</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return whether the launch should be traced.
	 * @since %since% */
	protected boolean getTraceLaunch() {
		return this.traceLaunch;
	}

	/** Get whether to use the method content.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:53:12</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return should the method content be used.
	 * @since %since% */
	protected boolean getUseMethodContent() {
		return this.useMethodContent;
	}

	/** Get whether to use the method serve.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:54:06</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return whether to use the method serve.
	 * @since %since% */
	protected boolean getUseMethodServer() {
		return this.useMethodServer;
	}

	/** Get a list of {@link fi.sardion.dctm.deploy.job.Job job} tasks defined for this method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 19:54:49</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return a list of jobs defined for this method.
	 * @since %since% */
	private List<Job> getJobs() {
		return this.jobs;
	}
}
/*-
 * $Log$
 */
