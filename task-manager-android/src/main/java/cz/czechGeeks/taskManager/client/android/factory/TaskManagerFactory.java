package cz.czechGeeks.taskManager.client.android.factory;

import android.content.Context;
import cz.czechGeeks.taskManager.client.android.model.manager.RestServiceTaskManager;
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

	public static TaskManager createService(Context context) {
		return new RestServiceTaskManager(context);
	}

}
