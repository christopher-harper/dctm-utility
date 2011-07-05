package fi.sardion.dctm.deploy.persistent;

import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.Task;

/**
 * TODO: write a type description.
 * <p>
 * <ul>
 * <li>Created: 6.10.2010 15.35.04</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.persistent.Attribute</li>
 * </ul>
 * 
 * @author Christopher Harper, account: ebf1231
 * @version %version%
 * @since %since%
 */
public class Attribute extends Task {

    private boolean isRepeating = false;
    private String name = null;
    private String value = null;
    private List<String> values = null;

    /**
     * <ul>
     * <li>Created: 6.10.2010 15.35.04</li>
     * <li>Author: Christopher Harper, account: ebf1231</li>
     * </ul>
     * 
     * @since %since%
     */
    public Attribute() {
	super();
    }

    public void setName(final String aName) {
	this.name = aName;
    }

    public void setRepeating(final String repeating) {
	this.isRepeating = Boolean.parseBoolean(repeating);
	if (isRepeating() && (getValues() == null) && (getValue() != null)) {
	    this.values = Arrays.asList(getValue().split(",")); //$NON-NLS-1$
	    this.value = this.values.get(0);
	}
    }

    public void setValue(final String aValue) {
	if (isRepeating()) {
	    this.values = Arrays.asList(aValue.split(",")); //$NON-NLS-1$
	    this.value = this.values.get(0);
	} else {
	    this.value = aValue;
	}
    }

    protected String getName() {
	return this.name;
    }

    protected String getValue() {
	return this.value;
    }

    protected List<String> getValues() {
	return this.values;
    }

    protected boolean isRepeating() {
	return this.isRepeating;
    }
}
/*-
 * $Log$
 */
