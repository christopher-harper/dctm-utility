package fi.sardion.dctm.deploy.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfTime;
import com.documentum.fc.common.IDfTime;

import fi.sardion.dctm.deploy.persistent.Persistent;
import fi.sardion.dctm.deploy.util.DCTMTask;

/** This is an ant task for creating and updating a dm_job object. The job task
 * is nested in the {@link fi.sardion.dctm.deploy.session.Session session} and {@link fi.sardion.dctm.deploy.method.Method servermethod}
 * tasks and must be
 * called <b> <code>job</code></b>.
 * <p>
 * <ul>
 * <li>Created: 18 Sep 2010 18:54:08</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.job.Job</li>
 * </ul>
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since% */
public class Job extends Persistent {

	/** When does the job expire. Defaults to ten(10) years from now.
	 * <ul>
	 * <li>Created: 18 Sep 2010 15:52:46</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private IDfTime			expirationDate;
	/** Should the job be inactivated after a failure. Default is false.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:13:12</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private boolean			inactivaAfterFailure	= false;
	/** Name of the job object. <b>NOTE</b>: This uniquely identifies the job in
	 * the repository.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:14:12</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private String			jobName					= null;
	/** How many times can the job run. Default is zero(0) which means that there
	 * is not a limit.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:15:09</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private int				maxIterations			= 0;
	/** The arguments to be passed to the method. <b>NOTE</b>: The method is
	 * responsible of reading these values.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:16:06</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private List<String>	methodArguments			= new ArrayList<String>();
	/** Name of the method this job executes.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:17:11</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private String			methodName				= null;
	/** Should the job pass standard arguments to the method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:39:25</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private boolean			passStandardArguments	= true;
	/** How often to execute the job. Used in conjunction with run mode. Default
	 * value is one.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:39:54</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private int				runInterval				= 1;
	/** How often to execute the job. Used in conjunction with run interval.
	 * Default value is week (4).
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:40:00</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private int				runMode					= 4;
	/** When can the job start executing. Defaults to the current date.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:41:49</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private IDfTime			startDate;
	/** Which method server to use?
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:43:23</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private String			targetServer			= "";						//$NON-NLS-1$
	/** Title of the job which categorises the jobs in DA.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:43:52</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private String			title					= "Custom";				//$NON-NLS-1$

	/** Sole constructor that initialises the date field values.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:44:22</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	public Job() {
		super();
		Calendar now = Calendar.getInstance(Locale.getDefault());
		this.startDate = new DfTime(new Date(now.getTimeInMillis()));
		now.set(Calendar.YEAR, now.get(Calendar.YEAR) + 10);
		this.expirationDate = new DfTime(new Date(now.getTimeInMillis()));
		now = null;
	}

	/** Set the job category.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:46:08</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aCategory
	 *            the category value.
	 * @since %since% */
	public void setCategory(final String aCategory) {
		this.title = aCategory;
	}

	/** Set when the job expires. The date must be given in the format: <code>dd/mm/yyyy hh:mi:ss</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:46:29</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param anExpirationDate
	 *            the date to expire the job.
	 * @since %since% */
	public void setExpirationdate(final String anExpirationDate) {
		this.expirationDate = new DfTime(anExpirationDate, IDfTime.DF_TIME_PATTERN18);
	}

	/** Should the job be inactivated if it fails. <b>NOTE</b>: String value
	 * passed in is transformed to a boolean using <code>Boolean.parseBoolean(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:48:22</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param inactivate
	 *            whether to inactivate.
	 * @since %since% */
	public void setInactivateafterfailure(final String inactivate) {
		this.inactivaAfterFailure = Boolean.parseBoolean(inactivate);
	}

	/** Set the name of the job.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:50:17</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aJobname
	 *            the job name.
	 * @since %since% */
	public void setJobname(final String aJobname) {
		this.jobName = aJobname;
	}

	/** How many times can the job be executed. <b>NOTE</b>: The given value is
	 * parsed to an int using <code>Integer.parseInt(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:50:43</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param iterations
	 *            number of execution times.
	 * @since %since% */
	public void setMaxiterations(final String iterations) {
		this.maxIterations = Integer.parseInt(iterations);
	}

	/** Set the method arguments. <b>NOTE</b>: This is a comma (,) separated list
	 * that is turned to a List using <code>Arrays.asList(aMethodArguments.split(","));</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:53:09</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aMethodArguments
	 *            the comma separated list of arguments.
	 * @since %since% */
	public void setMethodarguments(final String aMethodArguments) {
		this.methodArguments = Arrays.asList(aMethodArguments.split(",")); //$NON-NLS-1$
	}

	/** Set a method name.
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:55:28</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aMethodname
	 *            name of the method.
	 * @since %since% */
	public void setMethodname(final String aMethodname) {
		this.methodName = aMethodname;
	}

	/** Pass standard arguments to the method. String value passed in is
	 * transformed to a boolean using <code>Boolean.parseBoolean(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:55:51</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aPassStandardArguments
	 *            whether to pass in standard arguments.
	 * @since %since% */
	public void setPassstandardarguments(final String aPassStandardArguments) {
		this.passStandardArguments = Boolean.parseBoolean(aPassStandardArguments);
	}

	/** Set how often to execute the job. Used in conjunction with run mode.
	 * <b>NOTE</b>: The given value is parsed to an int using <code>Integer.parseInt(String);</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 16:57:26</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param interval
	 *            how often to execute the job.
	 * @since %since% */
	public void setRuninterval(final String interval) {
		this.runInterval = Integer.parseInt(interval);
	}

	/** Set how often to execute the job. Used in conjunction with run interval.
	 * <b>NOTE</b>: The given value is parsed to an int using <code>Integer.parseInt(String);</code> and must meet the following
	 * conditions. Mode must be >=1 and <=9 or one of the following: minute,
	 * hour, day, week, month, dayofweek, dayofmonth or dayofyear
	 * <ul>
	 * <li>Created: 18 Sep 2010 17:00:01</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param mode
	 *            how often to execute the job.
	 * @since %since% */
	public void setRunmode(final String mode) {
		try {
			this.runMode = Integer.parseInt(mode);
			if (getRunMode() < 1 || getRunMode() > 9) {
				this.runMode = 4;
			}
		} catch (final NumberFormatException nfex) {
			if ("minute".equals(mode)) { //$NON-NLS-1$
				this.runMode = 1;
			} else if ("hour".equals(mode)) { //$NON-NLS-1$
				this.runMode = 2;
			} else if ("day".equals(mode)) { //$NON-NLS-1$
				this.runMode = 3;
			} else if ("week".equals(mode)) { //$NON-NLS-1$
				this.runMode = 4;
			} else if ("month".equals(mode)) { //$NON-NLS-1$
				this.runMode = 5;
			} else if ("dayofweek".equals(mode)) { //$NON-NLS-1$
				this.runMode = 7;
			} else if ("dayofmonth".equals(mode)) { //$NON-NLS-1$
				this.runMode = 8;
			} else if ("dayofyear".equals(mode)) { //$NON-NLS-1$
				this.runMode = 9;
			} else {
				throw new BuildException(
						String.format(
								"Invalid run mode %s given. Mode must be >=1 and <=9 or one of the following: minute, hour, day, week, month, dayofweek, dayofmonth or dayofyear.", //$NON-NLS-1$
								new Object[] { mode }), nfex);
			}
		}
	}

	/** Set the starting date for the job. The date must be given in the format: <code>dd/mm/yyyy hh:mi:ss</code>
	 * <ul>
	 * <li>Created: 18 Sep 2010 17:08:15</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param aStartDate
	 *            when to start the job execution.
	 * @since %since% */
	public void setStartdate(final String aStartDate) {
		this.startDate = new DfTime(aStartDate, IDfTime.DF_TIME_PATTERN18);
	}

	/** Set the method server to execute the job method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 17:09:24</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param server
	 *            the server name.
	 * @since %since% */
	public void setTargetserver(final String server) {
		this.targetServer = server;
	}

	@Override
	protected String getExistingQualification() {
		return new StringBuilder().append(DCTMTask.DM_JOB).append(" where ") //$NON-NLS-1$
				.append(DCTMTask.OBJECT_NAME).append(" = '").append( //$NON-NLS-1$
						getJobName()).append('\'').toString();
	}

	/** Get the expiration time.
	 * <ul>
	 * <li>Created: 18 Sep 2010 17:10:00</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the expiration time.
	 * @since %since% */
	protected IDfTime getExpirationDate() {
		return this.expirationDate;
	}

	/** Get whether to inactivate the job after a failure.
	 * <ul>
	 * <li>Created: 18 Sep 2010 17:10:19</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return whether to inactivate the job after a failure.
	 * @since %since% */
	protected boolean getInactivateAfterFailure() {
		return this.inactivaAfterFailure;
	}

	/** Get the name of the job.
	 * <ul>
	 * <li>Created: 18 Sep 2010 17:11:08</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the job name.
	 * @since %since% */
	protected String getJobName() {
		return this.jobName;
	}

	/** Get the maximum iterations the job can do.
	 * <ul>
	 * <li>Created: 18 Sep 2010 17:11:24</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the max iterations.
	 * @since %since% */
	protected int getMaxIterations() {
		return this.maxIterations;
	}

	/** Get the method arguments list.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:48:21</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the method arguments.
	 * @since %since% */
	protected List<String> getMethodArguments() {
		return this.methodArguments;
	}

	/** Get the method name for this job.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:48:44</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the method name.
	 * @since %since% */
	protected String getMethodName() {
		return this.methodName;
	}

	/** Should standard arguments be passed to the method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:49:06</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return whether to pass standard arguments.
	 * @since %since% */
	protected boolean getPassStandardArguments() {
		return this.passStandardArguments;
	}

	/** How often to execute the job.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:49:40</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return run interval.
	 * @since %since% */
	protected int getRunInterval() {
		return this.runInterval;
	}

	/** How often should the job be executed.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:50:07</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return run mode.
	 * @since %since% */
	protected int getRunMode() {
		return this.runMode;
	}

	/** Get the jobs start date.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:50:35</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return start date.
	 * @since %since% */
	protected IDfTime getStartDate() {
		return this.startDate;
	}

	/** Get the target execution server of the method.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:51:00</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return target server.
	 * @since %since% */
	protected String getTargetServer() {
		return this.targetServer;
	}

	/** Get the job title.
	 * <ul>
	 * <li>Created: 18 Sep 2010 18:51:34</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the title.
	 * @since %since% */
	protected String getTitle() {
		return this.title;
	}

	@Override
	protected void typeSpesific(final IDfPersistentObject tempObject) {
		try {
			tempObject.setString(DCTMTask.OBJECT_NAME, getJobName());
			tempObject.setString(DCTMTask.METHOD_NAME, getMethodName());
			tempObject.setTime(DCTMTask.EXPIRATION_DATE, getExpirationDate());
			tempObject.setBoolean(DCTMTask.INACTIVATE_AFTER_FAILURE, getInactivateAfterFailure());
			tempObject.setInt(DCTMTask.MAX_ITERATIONS, getMaxIterations());
			tempObject.setBoolean(DCTMTask.PASS_STANDARD_ARGUMENTS, getPassStandardArguments());
			tempObject.setInt(DCTMTask.RUN_INTERVAL, getRunInterval());
			tempObject.setInt(DCTMTask.RUN_MODE, getRunMode());
			tempObject.setTime(DCTMTask.START_DATE, getStartDate());
			tempObject.setTime(DCTMTask.A_NEXT_INVOCATION, getStartDate());
			tempObject.setString(DCTMTask.TARGET_SERVER, getTargetServer());
			tempObject.setString(DCTMTask.TITLE, getTitle());
			tempObject.setBoolean(DCTMTask.IS_INACTIVE, false);
		} catch (final DfException dex) {
			printStack(dex);
			throw new BuildException("Failure modifying job object.", dex); //$NON-NLS-1$
		}
	}
}
/*-
 * $Log$
 */
