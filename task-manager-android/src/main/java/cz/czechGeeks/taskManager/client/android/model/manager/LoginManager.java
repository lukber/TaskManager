package cz.czechGeeks.taskManager.client.android.model.manager;

import java.util.List;

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
	 * @return ID prihlaseneho uzivatele
	 */
	public void signIn(String userName, String password, SignInCallBack callBack);

	/**
	 * Vrati uzivatele
	 * 
	 * @param userId
	 *            ID uzivatele
	 * @return
	 */
	public LoginModel get(long userId);

	/**
	 * Vrati vsechny uzivatele
	 * 
	 * @return
	 */
	public List<LoginModel> getAll();

}
