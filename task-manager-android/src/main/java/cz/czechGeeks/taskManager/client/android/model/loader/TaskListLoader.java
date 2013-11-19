package cz.czechGeeks.taskManager.client.android.model.loader;

import java.util.List;

import android.content.Context;
import cz.czechGeeks.taskManager.client.android.factory.TaskServiceFactory;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;

public class TaskListLoader extends AbstractListLoader<TaskModel> {

	public TaskListLoader(Context context) {
		super(context);
	}

	@Override
	public List<TaskModel> loadInBackground() {
		// nacteni dat
		return TaskServiceFactory.createService().getAllMain();
	}

}
