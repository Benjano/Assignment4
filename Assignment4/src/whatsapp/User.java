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
	private Map<String, List<MessageWhatsApp>> _MessagesRead;
	private Map<String, List<MessageWhatsApp>> _MessagesRecieved;

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
		_MessagesRead = new ConcurrentHashMap<String, List<MessageWhatsApp>>();
		_MessagesRecieved = new ConcurrentHashMap<String, List<MessageWhatsApp>>();
		_Messages = new ConcurrentLinkedQueue<MessageWhatsApp>();
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
			Group group = _UserGroups.get(name);
			synchronized (group) {
				group.removeUser(this);
			}
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
	// public void addMessageFromUser(Message message) {
	//
	// String sourcePhone = message.getSource();
	// String targetPhone = message.getTarget();
	//
	// if (sourcePhone.equals(_Phone)) {
	// if (!_MessagesRecieved.containsKey(targetPhone)
	// && !_Phone.equals(targetPhone)) {
	// List<Message> messageList = new Vector<Message>();
	// _MessagesRead.put(targetPhone, messageList);
	// List<Message> messageList2 = new Vector<Message>();
	// messageList2.add(message);
	// _MessagesRecieved.put(targetPhone, messageList2);
	// } else {
	// List<Message> messageList = _MessagesRecieved.get(targetPhone);
	// messageList.add(message);
	// }
	// } else {
	// if (!_MessagesRecieved.containsKey(sourcePhone)
	// && !_Phone.equals(sourcePhone)) {
	// List<Message> messageList = new Vector<Message>();
	// _MessagesRead.put(sourcePhone, messageList);
	// List<Message> messageList2 = new Vector<Message>();
	// messageList2.add(message);
	// _MessagesRecieved.put(sourcePhone, messageList2);
	// } else {
	// List<Message> messageList = _MessagesRecieved.get(sourcePhone);
	// messageList.add(message);
	// }
	// }
	// }
	//
	// public void addMessageFromGroup(Message message) {
	//
	// String groupName = message.getTarget();
	//
	// if (!_MessagesRecieved.containsKey(groupName)) {
	// List<Message> messageList = new Vector<Message>();
	// _MessagesRead.put(groupName, messageList);
	// messageList.add(message);
	// _MessagesRecieved.put(groupName, messageList);
	// } else {
	// List<Message> messageList = _MessagesRecieved.get(groupName);
	// messageList.add(message);
	// }
	//
	// }

	public void addMessage(MessageWhatsApp message) {
		_Messages.add(message);
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

	public synchronized List<MessageWhatsApp> getNewMessages() {
		List<MessageWhatsApp> result = new Vector<MessageWhatsApp>();
		while (!_Messages.isEmpty()) {
			result.add(_Messages.poll());
		}
		// for (Map.Entry<String, List<Message>> it :
		// _MessagesRecieved.entrySet()) {
		// for (Message message : it.getValue()) {
		// result.add(message);
		// }
		// _MessagesRead.get(it.getKey()).addAll(it.getValue());
		// it.getValue().clear();
		// }

		// for (Map.Entry<String, Group> it : _UserGroups.entrySet()) {
		// result.addAll(it.getValue().getNewMessages(_Phone));
		// }
		return result;
	}

	public String getMessages(String target) {
		StringBuilder builder = new StringBuilder();
		if (_MessagesRead.containsKey(target)) {
			for (MessageWhatsApp message : _MessagesRead.get(target)) {
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
