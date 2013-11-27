package cz.czechGeeks.taskManager.client.android.util;

import cz.czechGeeks.taskManager.client.android.model.LoginModel;

public class LoginUtils {

	private static LoginUtils instance;

	// prihlaseny uzivatel
	private LoginModel loggedUser = null;

	public LoginUtils() {
	}

	public static LoginUtils get() {
		if (instance == null) {
			instance = new LoginUtils();
		}

		return instance;
	}

	/**
	 * Okopirovani hodnot
	 * 
	 * @param from
	 * @param to
	 */
	public void copyAttributes(LoginModel from, LoginModel to) {
		to.setId(from.getId());
		to.setName(from.getName());
	}

	/**
	 * ID prihlaseneho uzivatele
	 * 
	 * @return
	 */
	public Long getLoggedUserId() {
		return loggedUser.getId();
	}

	/**
	 * Nazev prihlaseneho uzivatele
	 * 
	 * @return
	 */
	public String getLoggedUserName() {
		return loggedUser.getName();
	}

	/**
	 * Nastaveni prihlaseneho uzivatele
	 * 
	 * @param loggedUser
	 */
	public void setLoggedUser(LoginModel loggedUser) {
		this.loggedUser = new LoginModel();
		copyAttributes(loggedUser, this.loggedUser);
	}
}
