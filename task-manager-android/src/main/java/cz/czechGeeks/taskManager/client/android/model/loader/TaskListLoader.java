package cz.czechGeeks.taskManager.client.android.model.loader;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import cz.czechGeeks.taskManager.client.android.factory.TaskServiceFactory;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;

public class TaskListLoader extends AsyncTaskLoader<List<TaskModel>> {

	List<TaskModel> taskList;

	public TaskListLoader(Context context) {
		super(context);
	}

	@Override
	public List<TaskModel> loadInBackground() {
		// nacteni dat
		return TaskServiceFactory.createService().getAllDelegatedToMe();
	}

	/**
	 * Nastaveni nactenich dat
	 */
	@Override
	public void deliverResult(List<TaskModel> apps) {
		super.deliverResult(apps);
		taskList = apps;
	}

	/**
	 * Zachyceni pozadavku na nacteni dat
	 */
	@Override
	protected void onStartLoading() {
		if (taskList != null) {
			// data uz jsou nactena - nastavim jiz nactena
			deliverResult(taskList);
		}

		if (takeContentChanged() || taskList == null) {
			// data se nejak zmenila nebo jeste nebyla vubec nactena
			forceLoad();// nacti data
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		if (taskList != null) {
			taskList = null;
		}
	}

}
