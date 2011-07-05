package fi.sardion.dctm.deploy.persistent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;

import com.documentum.fc.client.IDfGroup;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

import fi.sardion.dctm.deploy.util.DCTMTask;

public class Group extends Persistent {

	private String	          groupAddress	= "info@sardion.fi";	   //$NON-NLS-1$
	private String	          groupClass	= "group";	               //$NON-NLS-1$
	private final List<Group>	groups	   = new ArrayList<Group>();
	private List<String>	  groupsNames	= new ArrayList<String>();
	private String	          name	       = null;
	private String	          owner	       = null;
	private List<String>	  usersNames	= new ArrayList<String>();

	public void addDocugroup(final Group group) {
		this.groups.add(group);
	}

	@Override
	public void execute() {
		final IDfSession session = getSession();
		try {
			if (isForThisRepository(session.getDocbaseName())) {
				for (final Group group : getGroups()) {
					group.setSession(getSession());
					group.execute();
					if (!getGroupsNames().contains(group.getGroupName())) {
						getGroupsNames().add(group.getGroupName());
					}
				}
				super.execute();
			} else {
				log(String.format("This group %s was only defined for repositories %s and the current repository is %s.", //$NON-NLS-1$
				        new Object[] { getGroupName(), getRepositories(), session.getDocbaseName() }));
			}
		} catch (final DfException dex) {
			printStack(dex);
			throw new BuildException("Failed to return repository name.", dex); //$NON-NLS-1$
		}
	}

	public void setGroupaddress(final String aGroupAddress) {
		this.groupAddress = aGroupAddress;
	}

	public void setGroupclass(final String theClass) {
		if ("privilege group".equalsIgnoreCase(theClass) //$NON-NLS-1$
		        || "role".equalsIgnoreCase(theClass)) {
			this.groupClass = theClass.toLowerCase();
		} else {
			this.groupClass = "group";
		}
	}

	public void setGroupname(final String aName) {
		this.name = aName.trim().toLowerCase();
	}

	public void setGroupsnames(final String theGroupsNames) {
		this.groupsNames = Arrays.asList(theGroupsNames.split(","));
	}

	public void setOwnername(final String anOwner) {
		this.owner = anOwner;
	}

	public void setUsersnames(final String theUsersNames) {
		this.usersNames = Arrays.asList(theUsersNames.split(","));
	}

	@Override
	protected String getExistingQualification() {
		return new StringBuilder().append(DCTMTask.DM_GROUP).append(" where ") //$NON-NLS-1$
		        .append(DCTMTask.GROUP_NAME).append(" = '").append( //$NON-NLS-1$
		                getGroupName()).append('\'').toString();
	}

	protected String getGroupAddress() {
		return this.groupAddress;
	}

	protected String getGroupClass() {
		return this.groupClass;
	}

	protected String getGroupName() {
		return this.name;
	}

	protected List<Group> getGroups() {
		return this.groups;
	}

	protected List<String> getGroupsNames() {
		return this.groupsNames;
	}

	protected String getOwnerName() {
		return this.owner;
	}

	@Override
	protected String getType() {
		return DCTMTask.DM_GROUP;
	}

	protected List<String> getUsersNames() {
		return this.usersNames;
	}

	protected void typeSpesific(final IDfGroup group) {
		try {
			if (group.isNew() || group.isDirty() || reaplySettings()) {
				group.setDescription(getDescription());
				group.setGroupName(getGroupName());
				group.setGroupClass(getGroupClass());
				if (getOwnerName() == null) {
					setOwnername(getSession().getDocbaseOwnerName());
				}
				group.setOwnerName(getOwnerName());
				group.setGroupAddress(getGroupAddress());
				group.removeAllGroups();
				for (final String groupName : getGroupsNames()) {
					group.addGroup(groupName.trim().toLowerCase());
				}
				group.removeAllUsers();
				for (final String userName : getUsersNames()) {
					group.addUser(userName.trim());
				}
			}
		} catch (final DfException dex) {
			printStack(dex);
			throw new BuildException("Failure modifying object.", dex); //$NON-NLS-1$
		}
	}

	@Override
	protected void typeSpesific(final IDfPersistentObject tempObject) {
		typeSpesific((IDfGroup) tempObject);
	}
}
/*-
 * $Log$
 */
