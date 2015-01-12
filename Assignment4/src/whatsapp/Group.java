package whatsapp;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Group {

	// ********** Members **********
	private String _GroupName;
	private User _GroupManager;
	private Map<String, User> _UsersInGroup;
	private List<Message> _GroupMessages;

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
		_GroupMessages = new Vector<Message>();
	}

	/**
	 * Add a new user to group if user does not exist already
	 * 
	 * @param user
	 * @return boolean true if user added else false
	 */
	public boolean addUser(User user) {
		// TODO Add user if not exists
		return false;
	}

	/**
	 * Remove the user from group if user is in the group.
	 * 
	 * @param user
	 * @return boolean true if user removed else false
	 */
	public boolean removeUser(User user) {
		// TODO Remove user if exists
		// TODO Cannot remove manager
		return true;
	}

	/**
	 * Add a new message to group messages
	 * 
	 * @param message
	 */
	public void addMessage(Message message) {
		_GroupMessages.add(message);
	}

	/**
	 * Return the name of the group
	 * 
	 * @return String name
	 */
	public String getGroupName() {
		return _GroupName;
	}

	/**
	 * Return the name of the group manager
	 * 
	 * @return String name
	 */
	public String getGroupManagerName() {
		return _GroupManager.getName();
	}

	/**
	 * Return the phone of the group manager
	 * 
	 * @return String phone
	 */
	public String getGroupManagerPhone() {
		return _GroupManager.getPhone();
	}

	/**
	 * Check if the user exists in group.
	 * 
	 * @param user
	 * @return boolean true if the user exists else false
	 */
	public boolean isUserExistsInGroup(User user) {
		return _UsersInGroup.get(user.getPhone()) != null;
	}
}
