package cz.czechGeeks.taskManager.client.android.util;

import cz.czechGeeks.taskManager.client.android.model.LoginModel;

public class LoginUtil {

	private static LoginUtil instance;

	private LoginModel loggedUser = null;

	public LoginUtil() {
	}

	public static LoginUtil get() {
		if (instance == null) {
			instance = new LoginUtil();
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
