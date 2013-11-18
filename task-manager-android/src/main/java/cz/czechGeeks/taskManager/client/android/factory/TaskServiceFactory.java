package cz.czechGeeks.taskManager.client.android.factory;

import cz.czechGeeks.taskManager.client.android.model.manager.FakeTaskService;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskService;

/**
 * Tovarna pro vytvareni manageru pro model ukolu
 * 
 * @author lukasb
 * 
 */
public class TaskServiceFactory {

	private TaskServiceFactory() {
	}

	public static TaskService createService() {
		return new FakeTaskService();
	}

}
