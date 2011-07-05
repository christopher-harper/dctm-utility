package fi.sardion.dctm.deploy.persistent;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.util.DCTMTask;

public class Folder extends Sys {

    private final List<Document> documents = new ArrayList<Document>();
    private final List<Folder> folders = new ArrayList<Folder>();

    public void addDocument(final Document document) {
	this.documents.add(document);
    }

    public void addFolder(final Folder folder) {
	this.folders.add(folder);
    }

    @Override
    public void execute() {
	final IDfSession session = getSession();
	try {
	    if (isForThisRepository(session.getDocbaseName())) {
		super.execute();
		final StringBuilder path = new StringBuilder();
		if (getFolders() != null) {
		    path.append(getFolders().get(0));
		}
		path.append('/').append(getObjectName());
		for (final Folder folder : this.folders) {
		    if (!folder.containsPath(path.toString())) {
			folder.addPath(path.toString());
		    }
		    folder.setSession(getSession());
		    folder.execute();
		}
		for (final Document document : getDocuments()) {
		    if (!document.containsPath(path.toString())) {
			document.addPath(path.toString());
		    }
		    document.setSession(getSession());
		    document.execute();
		}
	    } else {
		log(String
		        .format(
		                "This object %s was only defined for repositories %s and the current repository is %s.", //$NON-NLS-1$
		                new Object[] { getObjectName(),
		                        getRepositories(),
		                        session.getDocbaseName() }));
	    }
	} catch (final DfException dex) {
	    printStack(dex);
	    throw new BuildException("Failed to return repository name.", dex); //$NON-NLS-1$
	}
    }

    protected List<Document> getDocuments() {
	return this.documents;
    }

    @Override
    protected String getExistingQualification() {
	if (getFolders() == null) {
	    return new StringBuilder().append(DCTMTask.DM_CABINET).append(
		    " where ").append(DCTMTask.OBJECT_NAME).append(" = '")
		    .append(getObjectName()).append('\'').toString();
	}
	return new StringBuilder().append(DCTMTask.DM_FOLDER).append(
	        " where any ").append(DCTMTask.R_FOLDER_PATH).append(" = '")
	        .append(getFolders().get(0)).append('/')
	        .append(getObjectName()).append('\'').toString();
    }

    @Override
    protected void typeSpesific(final IDfPersistentObject tempObject) {
	super.typeSpesific((IDfFolder) tempObject);
    }
}
/*-
 * $Log$
 */
