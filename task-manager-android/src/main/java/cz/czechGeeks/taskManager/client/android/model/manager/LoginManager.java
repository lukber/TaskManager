package cz.czechGeeks.taskManager.client.android.model.manager;

import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.LoginModel;

/**
 * Service pro nacitani loginu
 * 
 * @author lukasb
 * 
 */
public interface LoginManager {

	public interface SignInCallBack {
		void onUserSigned(LoginModel signedUser);

		void onSignInProcessError(ErrorMessage errorMessage);
	}

	/**
	 * Prihlaseni uzivatele
	 * 
	 * @param userName
	 * @param password
	 */
	void signIn(String userName, String password, AsyncTaskCallBack<LoginModel> callBack);

	/**
	 * Vrati uzivatele
	 * 
	 * @param userId
	 *            ID uzivatele
	 */
	void get(long userId, AsyncTaskCallBack<LoginModel> callBack);

	/**
	 * Vrati vsechny uzivatele
	 * 
	 */
	void getAll(AsyncTaskCallBack<LoginModel[]> callBack);
}
