package cz.czechGeeks.taskManager.client.android.factory;

import android.content.Context;
import cz.czechGeeks.taskManager.client.android.model.manager.RestServiceTaskCategManager;
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
		return new RestServiceTaskCategManager(context);
	}

}
