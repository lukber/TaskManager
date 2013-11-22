package cz.czechGeeks.taskManager.client.android.util;

import cz.czechGeeks.taskManager.client.android.model.LoginModel;

public class LoginUtils {

	private static LoginUtils instance;

	private LoginModel loggedUser = null;

	public LoginUtils() {
	}

	public static LoginUtils get() {
		if (instance == null) {
			instance = new LoginUtils();
		}

		return instance;
	}

	public void copyAttributes(LoginModel from, LoginModel to) {
		to.setId(from.getId());
		to.setName(from.getName());
	}

	public String getLoggedUserId() {
		return loggedUser.getName();
	}

	public Long getLoggedUserName() {
		return loggedUser.getId();
	}

	public void setLoggedUser(LoginModel loggedUser) {
		this.loggedUser = new LoginModel();
		copyAttributes(loggedUser, this.loggedUser);
	}
}
