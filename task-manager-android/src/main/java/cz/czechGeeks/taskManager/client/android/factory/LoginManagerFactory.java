package cz.czechGeeks.taskManager.client.android.factory;

import android.content.Context;
import cz.czechGeeks.taskManager.client.android.model.manager.LoginManager;
import cz.czechGeeks.taskManager.client.android.model.manager.RestServiceLoginManager;

/**
 * Tovarna pro vytvareni manageru pro model loginu
 * 
 * @author lukasb
 * 
 */
public class LoginManagerFactory {

	private LoginManagerFactory() {
	}

	public static LoginManager get(Context context) {
		return new RestServiceLoginManager(context);
	}

}
