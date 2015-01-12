package whatsapp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Group {

	private String _GroupName;
	private User _GroupManager;
	private Map<String, User> _UsersInGroup;

	/**
	 * Constuctor
	 * 
	 * @param name
	 * @param manager
	 */
	public Group(String name, User manager) {
		_GroupName = name;
		_GroupManager = manager;
		_UsersInGroup = new ConcurrentHashMap<String, User>();
	}

	/**
	 * Add a new user to group if user does not exist already
	 * 
	 * @param user
	 * @return true if user added else false
	 */
	public boolean addUser(User user) {
		// TODO Add user if not exists
		return false;
	}

	/**
	 * Remove the user from group if user is in the group.
	 * 
	 * @param user
	 * @return true if user removed else false
	 */
	public boolean removeUser(User user) {
		// TODO Remove user if exists
		// TODO Cannot remove manager
		return true;
	}

	/**
	 * Return the name of the group
	 * 
	 * @return name
	 */
	public String getGroupName() {
		return _GroupName;
	}

	/**
	 * Return the name of the group manager
	 * 
	 * @return name
	 */
	public String getGroupManagerName() {
		return _GroupManager.getName();
	}

	/**
	 * Return the phone of the group manager
	 * 
	 * @return phone
	 */
	public String getGroupManagerPhone() {
		return _GroupManager.getPhone();
	}

	/**
	 * Check if the user exists in group.
	 * 
	 * @param user
	 * @return true if the user exists else false
	 */
	public boolean isUserExistsInGroup(User user) {
		return _UsersInGroup.get(user.getPhone()) != null;
	}
}
