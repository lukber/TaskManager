package cz.czechGeeks.taskManager.client.android.util;

import cz.czechGeeks.taskManager.client.android.model.LoginModel;

public class UserUtil {

	private static UserUtil instance;
	private LoginModel loggedUser = new LoginModel(Long.valueOf(1), "Lukas");

	public UserUtil() {
	}

	public static UserUtil get() {
		if (instance == null) {
			instance = new UserUtil();
		}

		return instance;
	}

	public LoginModel getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(LoginModel loggedUser) {
		this.loggedUser = loggedUser;
	}
}
