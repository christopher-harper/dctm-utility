package fi.sardion.dctm.deploy.persistent;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 24 Sep 2010 15:16:54</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.persistent.Alias</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */

public class Alias extends Task {

    private int category;
    private String name;
    private int userCategory;
    private String value;

    /**
     * <ul>
     * <li>Created: 24 Sep 2010 15:16:54</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Alias() {
	super();
    }

    public void setCategory(final String aCategory) {
	try {
	    this.category = Integer.parseInt(aCategory);
	    if ((this.category > 6) || (this.category < 0)) {
		throw new BuildException(
			String
				.format(
					"Invalid category %s given. Valid value are between 0 and 6.", //$NON-NLS-1$
					new Object[] { aCategory }));
	    }
	} catch (final NumberFormatException nfex) {
	    throw new BuildException(String.format(
		    "Invalid category %s given. Value must be an integer.", //$NON-NLS-1$
		    new Object[] { aCategory }));

	}
    }

    public void setName(final String aName) {
	this.name = aName;
    }

    /**
     * User defined category for the alias value
     * <ul>
     * <li>Created: 24 Sep 2010 15:34:54</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param aUserCategory
     * @since %since%
     */
    public void setUsercategory(final String aUserCategory) {
	try {
	    this.userCategory = Integer.parseInt(aUserCategory);
	} catch (final NumberFormatException nfex) {
	    throw new BuildException(String.format(
		    "Invalid category %s given. Value must be an integer.", //$NON-NLS-1$
		    new Object[] { aUserCategory }));
	}
    }

    public void setValue(final String aValue) {
	this.value = aValue;
    }

    protected int getCategory() {
	return this.category;
    }

    protected String getName() {
	return this.name;
    }

    protected int getUserCategory() {
	return this.userCategory;
    }

    protected String getValue() {
	return this.value;
    }

}

/*-
 * $Log$
 */