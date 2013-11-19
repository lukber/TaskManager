package cz.czechGeeks.taskManager.client.android.factory;

import cz.czechGeeks.taskManager.client.android.model.manager.FakeLoginManager;
import cz.czechGeeks.taskManager.client.android.model.manager.LoginManager;

/**
 * Tovarna pro vytvareni manageru pro model loginu
 * 
 * @author lukasb
 * 
 */
public class LoginServiceFactory {

	private LoginServiceFactory() {
	}

	public static LoginManager createService() {
		return new FakeLoginManager();
	}

}
