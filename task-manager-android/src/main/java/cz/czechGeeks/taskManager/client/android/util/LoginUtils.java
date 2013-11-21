package cz.czechGeeks.taskManager.client.android.util;

import cz.czechGeeks.taskManager.client.android.model.LoginModel;

public class LoginUtils {

	public static void copyAttributes(LoginModel from, LoginModel to) {
		to.setId(from.getId());
		to.setName(from.getName());
	}
}
