package cz.czechGeeks.taskManager.client.android.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.manager.LoginManager;
import cz.czechGeeks.taskManager.client.android.model.manager.RestServiceLoginManager;
import cz.czechGeeks.taskManager.client.android.model.manager.SQLiteServiceLoginManager;

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
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean useLocalDatabase = preferences.getBoolean(context.getString(R.string.app_settings_localDatabase_key), true);

		if (useLocalDatabase) {
			return new SQLiteServiceLoginManager(context);
		} else {
			return new RestServiceLoginManager(context);
		}
	}

}
