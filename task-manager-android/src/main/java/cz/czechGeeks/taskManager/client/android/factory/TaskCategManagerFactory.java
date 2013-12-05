package cz.czechGeeks.taskManager.client.android.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.manager.RestServiceTaskCategManager;
import cz.czechGeeks.taskManager.client.android.model.manager.SQLiteServiceTaskCategManager;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskCategManager;

/**
 * Tovarna pro vytvareni manageru pro model kategorie ukolu
 * 
 * @author lukasb
 * 
 */
public class TaskCategManagerFactory {

	private TaskCategManagerFactory() {
	}

	public static TaskCategManager get(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean useLocalDatabase = preferences.getBoolean(context.getString(R.string.app_settings_localDatabase_key), true);

		if (useLocalDatabase) {
			return new SQLiteServiceTaskCategManager(context);
		} else {
			return new RestServiceTaskCategManager(context);
		}
	}

}
