package whatsapp;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Group {

	// ********** Members **********
	private String _GroupName;
	private Map<String, User> _UsersInGroup;

	/**
	 * Constuctor
	 * 
	 * @param name
	 * @param creator
	 */
	public Group(String name, User creator) {
		_GroupName = name;
		_UsersInGroup = new ConcurrentHashMap<String, User>();
		addUser(creator);
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
			if (_UsersInGroup.containsKey(user.getPhone())) {
				_UsersInGroup.remove(user.getPhone());
				user.removeGroup(_GroupName);
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
	public void addMessage(MessageWhatsApp message) {
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
	 * Check if the user exists in group.
	 * 
	 * @param user
	 * @return boolean true if the user exists else false
	 */
	public boolean isUserExistsInGroup(User user) {
		return _UsersInGroup.containsKey(user.getPhone());
	}

	/**
	 * Check if the goup is empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return _UsersInGroup.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, User> it : _UsersInGroup.entrySet()) {
			builder.append(it.getKey()).append(",");
		}
		String result = builder.toString();
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

}
