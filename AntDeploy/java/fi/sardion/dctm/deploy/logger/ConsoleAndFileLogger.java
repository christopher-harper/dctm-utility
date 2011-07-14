package fi.sardion.dctm.deploy.logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.DefaultLogger;

/** <p>
 * A simple logger that writes the same output as is written to the console to a temporary file.
 * <ul>
 * <li>Created: 24 Sep 2010 09:26:26</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.logger.ConsoleAndFileLogger</li>
 * </ul>
 * </p>
 * <p>
 * To take this logger into use in eclipse do the following:
 * <ul>
 * <li>Right click the ant target you want to log.</li>
 * <li>Select: <u>R</u>un As -> <u>E</u>xternal tools configuration...</li>
 * <li>In the opening dialog select the Main tab and add: <code>-logger fi.sardion.dctm.deploy.logger.ConsoleAndFileLogger -verbose</code>
 * to the Arguments text box.</li>
 * <li>Look for: <code>(Sending/Sent) console output to file {file_path}</code> in order to locate the created log file.</li>
 * </ul>
 * </p>
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since% */
public class ConsoleAndFileLogger extends DefaultLogger {

	/** The log file object.
	 * <ul>
	 * <li>Created: 29 Sep 2010 09:46:48</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private File		logFile	= null;

	/** The stream that writes to the log file.
	 * <ul>
	 * <li>Created: 29 Sep 2010 09:47:06</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	private PrintStream	out		= null;

	/** Sole constructor.
	 * <ul>
	 * <li>Created: 29 Sep 2010 09:47:40</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @since %since% */
	public ConsoleAndFileLogger() {
		super();
	}

	/** Event that is triggered when the buiºld ends. Writes a message to the console about the log file location.
	 * <ul>
	 * <li>Created: 29 Sep 2010 09:48:53</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param event
	 *            the build event.
	 * @since %since% */
	@Override
	public void buildFinished(final BuildEvent event) {
		try {
			System.out.println(String.format("Sent console output to file %s.", //$NON-NLS-1$
					new Object[] { getLogFile().getAbsolutePath() }));
			log(String.format("Sent console output to file %s.", //$NON-NLS-1$
					new Object[] { getLogFile().getAbsolutePath() }));
		} catch (final NullPointerException npex) {
			System.out.println("No log file created."); //$NON-NLS-1$

		}
		super.buildFinished(event);
		getOut().flush();
		getOut().close();
	}

	/** Event that is triggered when the build starts. Writes a message to the console about the log file location.
	 * <ul>
	 * <li>Created: 29 Sep 2010 09:52:33</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param event
	 *            the build event.
	 * @since %since% */
	@Override
	public void buildStarted(final BuildEvent event) {
		super.buildStarted(event);
		try {
			this.logFile = File.createTempFile("ant_build_", ".log"); //$NON-NLS-1$ //$NON-NLS-2$
			this.out = new PrintStream(new BufferedOutputStream(new FileOutputStream(getLogFile())));
			System.out.println(String.format("Sending console output to file %s.", //$NON-NLS-1$
					new Object[] { getLogFile().getAbsolutePath() }));
			log(String.format("Sending console output to file %s.", //$NON-NLS-1$
					new Object[] { getLogFile().getAbsolutePath() }));
		} catch (final IOException ioex) {
			this.out = new PrintStream(new BufferedOutputStream(new ByteArrayOutputStream()));
			System.out.println("Failed to redirect console output to a file."); //$NON-NLS-1$
		}
	}

	/** Get the log file.
	 * <ul>
	 * <li>Created: 28 Sep 2010 11:15:03</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the log file.
	 * @since %since% */
	protected File getLogFile() {
		return this.logFile;
	}

	/** Get the outpur print stream.
	 * <ul>
	 * <li>Created: 29 Sep 2010 09:52:11</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @return the output stream.
	 * @since %since% */
	protected PrintStream getOut() {
		return this.out;
	}

	/** Override the log method that normally outputs to the console.
	 * <ul>
	 * <li>Created: 29 Sep 2010 09:53:43</li>
	 * <li>Author: Christopher Harper, account: dmadmin</li>
	 * </ul>
	 * @param message
	 *            the message to print to the log file and console.
	 * @since %since% */
	@Override
	protected void log(final String message) {
		super.log(message);
		getOut().println(message);
	}
}

/*-
 * $Log$
 */