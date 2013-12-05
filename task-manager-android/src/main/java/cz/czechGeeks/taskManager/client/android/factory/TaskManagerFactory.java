package cz.czechGeeks.taskManager.client.android.factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.manager.RestServiceTaskManager;
import cz.czechGeeks.taskManager.client.android.model.manager.SQLiteServiceTaskManager;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskManager;

/**
 * Tovarna pro vytvareni manageru pro model ukolu
 * 
 * @author lukasb
 * 
 */
public class TaskManagerFactory {

	private TaskManagerFactory() {
	}

	public static TaskManager get(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean useLocalDatabase = preferences.getBoolean(context.getString(R.string.app_settings_localDatabase_key), true);

		if (useLocalDatabase) {
			return new SQLiteServiceTaskManager(context);
		} else {
			return new RestServiceTaskManager(context);
		}
	}

}
