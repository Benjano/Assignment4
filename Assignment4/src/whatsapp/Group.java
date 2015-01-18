package whatsapp;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Group {

	// ********** Members **********
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
		addUser(manager);
	}

	/**
	 * Add a new user to group if user does not exist already
	 * 
	 * @param user
	 * @return boolean true if user added else false
	 */
	public boolean addUser(User user) {
		synchronized (_UsersInGroup) {
			if (_UsersInGroup.containsKey(user.getPhone())) {
				return false;
			}
			_UsersInGroup.put(user.getPhone(), user);
			return true;
		}
	}

	/**
	 * Remove the user from group if user is in the group.
	 * 
	 * @param user
	 * @return boolean true if user removed else false
	 */
	public boolean removeUser(User user) {
		synchronized (_UsersInGroup) {
			if (_UsersInGroup.containsKey(user.getPhone())
					&& !_GroupManager.equals(user)) {
				_UsersInGroup.remove(user.getPhone());
				return true;
			}
			return false;
		}
	}

	/**
	 * Add a new message to group messages
	 * 
	 * @param message
	 */
	public void addMessage(Message message) {
		for (Map.Entry<String, User> it : _UsersInGroup.entrySet()) {
			it.getValue().addMessage(message);
		}
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(_GroupManager.getPhone());
		for (Map.Entry<String, User> it : _UsersInGroup.entrySet()) {
			if (!it.getValue().equals(_GroupManager))
				builder.append(",").append(it.getKey());
		}
		return builder.toString();
	}

	public Collection<? extends Message> getNewMessages(String _Phone) {
		// TODO Auto-generated method stub
		return null;
	}
}
