package cz.czechGeeks.taskManager.client.android.factory;

import cz.czechGeeks.taskManager.client.android.model.manager.FakeTaskManager;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskManager;

/**
 * Tovarna pro vytvareni manageru pro model ukolu
 * 
 * @author lukasb
 * 
 */
public class TaskServiceFactory {

	private TaskServiceFactory() {
	}

	public static TaskManager createService() {
		return new FakeTaskManager();
	}

}
