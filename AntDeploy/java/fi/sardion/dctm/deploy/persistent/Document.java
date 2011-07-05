package fi.sardion.dctm.deploy.persistent;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.util.DCTMTask;

public class Document extends Sys {

    private String path = null;
    private String type = "unknown";

    public void setContent(final String aPath) {
	this.path = aPath;
    }

    public void setContenttype(final String aType) {
	this.type = aType;
    }

    public void typeSpesific(final IDfDocument document) {
	super.typeSpesific(document);
	try {
	    if (!document.isNew() && reaplySettings() && !document.isCheckedOut()) {
		document.checkout();
	    }
	    if (document.isNew() || document.isDirty() || reaplySettings()) {
		try {
		    if (getContent() != null) {
			document.setContentType(getContentType());
			document.setFile(getContent());
		    }
		} catch (final DfException dex) {
		    printStack(dex);
		    throw new BuildException(String.format("Failure setting content file %s with type %s.", //$NON-NLS-1$
			    new Object[] { getContent(), getContentType() }), dex);
		}
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException("Failure saving object.", dex); //$NON-NLS-1$
	}
    }

    protected String getContent() {
	return this.path;
    }

    protected String getContentType() {
	return this.type;
    }

    @Override
    protected String getExistingQualification() {
	return new StringBuilder().append(DCTMTask.DM_DOCUMENT).append(" where folder('").append(getFolders().get(0)).append("') and ") //$NON-NLS-1$ //$NON-NLS-2$
	        .append(DCTMTask.OBJECT_NAME).append(" = '").append( //$NON-NLS-1$
	                getObjectName()).append('\'').toString();
    }

    @Override
    protected void typeSpesific(final IDfPersistentObject tempObject) {
	typeSpesific((IDfDocument) tempObject);
    }
}
/*-
 * $Log$
 */
