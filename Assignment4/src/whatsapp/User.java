package whatsapp;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {

	// ********** Members **********
	private String _Name;
	private String _Phone;
	private Map<String, Group> _UserGroups;

	// <Target, List of messages> The messages of the user
	private Queue<MessageWhatsApp> _Messages;

	/**
	 * Constuctor
	 * 
	 * @param name
	 * @param phone
	 */
	public User(String name, String phone) {
		_Name = name;
		_Phone = phone;
		_UserGroups = new ConcurrentHashMap<String, Group>();
		_Messages = new ConcurrentLinkedQueue<MessageWhatsApp>();
	}

	/**
	 * Add group to user's groups and update group to add the user
	 * 
	 * @param name
	 * @return true if group added else false
	 */
	public synchronized boolean addGroup(Group group) {
		if (_UserGroups.containsKey(group)) {
			return false;
		}
		_UserGroups.put(group.getGroupName(), group);
		return true;
	}

	/**
	 * Remove group from user's groups and update the group to remove the user
	 * 
	 * @param name
	 * @return true if group removed else false
	 */
	public boolean removeGroup(String name) {
		synchronized (_UserGroups) {
			if (_UserGroups.containsKey(name)) {
				Group group = _UserGroups.get(name);
				synchronized (group) {
					group.removeUser(this);
				}
				_UserGroups.remove(name);
				return true;
			}
			return false;
		}
	}

	public void addMessage(MessageWhatsApp message) {
		synchronized (message) {
			_Messages.add(message);
		}
	}

	/**
	 * Return the name of the user
	 * 
	 * @return String name
	 */
	public String getName() {
		return _Name;
	}

	/**
	 * Return the phone of the user
	 * 
	 * @return String phone
	 */
	public String getPhone() {
		return _Phone;
	}

	public List<MessageWhatsApp> getNewMessages() {
		synchronized (_Messages) {
			List<MessageWhatsApp> result = new Vector<MessageWhatsApp>();
			while (!_Messages.isEmpty()) {
				MessageWhatsApp msg = _Messages.poll();
				result.add(msg);
			}
			return result;
		}
	}

	@Override
	public String toString() {
		return new StringBuilder().append(_Name).append("@").append(_Phone)
				.toString();
	}
}
