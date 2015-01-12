package whatsapp;

public class User {

	private String _Name;
	private String _Phone;
	private String _Auth;

	public User(String name, String phone) {
		_Name = name;
		_Phone = phone;
	}

	public void setAuth(String auth) {
		_Auth = auth;
	}

	public String getName() {
		return _Name;
	}

	public String getPhone() {
		return _Phone;
	}

	public String getAuth() {
		return _Auth;
	}

}
