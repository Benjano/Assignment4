package whatsapp;

import java.util.List;
import java.util.Map;

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
	}

	/**
	 * Add group to user's groups and update group to add the user
	 * 
	 * @param name
	 * @return true if group added else false
	 */
	public boolean addGroup(Group group) {
		// TODO Add group to groups if not exists
		return true;
	}

	/**
	 * Remove group from user's groups and update the group to remove the user
	 * 
	 * @param name
	 * @return true if group removed else false
	 */
	public boolean removeGroup(String name) {
		// TODO group from user if group exists
		return true;
	}

	/**
	 * Add a new message to messages
	 * 
	 * @param message
	 */
	public void addMessage(Message message) {

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

}
