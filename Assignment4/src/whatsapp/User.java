package whatsapp;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class User {

	// ********** Members **********
	private String _Name;
	private String _Phone;
	private Map<String, Group> _UserGroups;

	// <Target, List of messages> The messages of the user
	private Map<String, List<Message>> _Messages;

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
		_Messages = new ConcurrentHashMap<String, List<Message>>();
	}

	/**
	 * Add group to user's groups and update group to add the user
	 * 
	 * @param name
	 * @return true if group added else false
	 */
	public boolean addGroup(Group group) {

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
		if (_UserGroups.containsKey(name)) {
			_UserGroups.remove(name);
			return true;
		}
		return false;
	}

	/**
	 * Add a new message to messages
	 * 
	 * @param message
	 */
	public void addMessage(Message message) {

		String sourcePhone = message.getSource();
		String targetPhone = message.getTarget();

		if (sourcePhone.equals(_Phone)) {
			if (!_Messages.containsKey(targetPhone)) {
				List<Message> messageList = new Vector<Message>();
				messageList.add(message);
				_Messages.put(targetPhone, messageList);
			} else {
				List<Message> messageList = _Messages.get(targetPhone);
				messageList.add(message);
			}
		} else {
			if (!_Messages.containsKey(sourcePhone)) {
				List<Message> messageList = new Vector<Message>();
				messageList.add(message);
				_Messages.put(sourcePhone, messageList);
			} else {
				List<Message> messageList = _Messages.get(targetPhone);
				messageList.add(message);
			}

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

	public String getMessages(String target) {
		StringBuilder builder = new StringBuilder();
		if (_Messages.containsKey(target)) {
			for (Message message : _Messages.get(target)) {
				builder.append(message);
			}
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		return new StringBuilder().append(_Name).append("@").append(_Phone)
				.toString();
	}
}
