package fi.sardion.dctm.deploy.objectmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.Task;

/**
 * Just a wrapper task for the <code>typeattribute</code> tasks.
 * <p>
 * <ul>
 * <li>Created: 17 Sep 2010 08:33:51</li>
 * <li>Project: Deploy</li>
 * <li>File: fi.sardion.dctm.deploy.objectmodel.Attributes</li>
 * </ul>
 * 
 * @author Christopher Harper, account: dmadmin
 * @version %version%
 * @since %since%
 */
public class Attributes extends Task {

    /**
     * <code>attributes = new ArrayList<Attribute>();</code>
     * <ul>
     * <li>Created: 29 Sep 2010 11:27:47</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    private final List<Attribute> attributes = new ArrayList<Attribute>();

    /**
     * Sole constructor.
     * <ul>
     * <li>Created: 17 Sep 2010 08:33:52</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @since %since%
     */
    public Attributes() {
	// TODO Auto-generated constructor stub
    }

    /**
     * Add a type attribute.
     * <ul>
     * <li>Created: 29 Sep 2010 11:28:19</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @param attribute
     *            the attribute.
     * @since %since%
     */
    public void addTypeattribute(final Attribute attribute) {
	this.attributes.add(attribute);
    }

    /**
     * Get all the defined type attributes.
     * <ul>
     * <li>Created: 29 Sep 2010 11:28:43</li>
     * <li>Author: Christopher Harper, account: dmadmin</li>
     * </ul>
     * 
     * @return the list of attributes.
     * @since %since%
     */
    protected List<Attribute> getAttributes() {
	return this.attributes;
    }
}

/*-
 * $Log$
 */