package cz.czechGeeks.taskManager.client.android.model.manager;

import android.content.Context;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;

public class RestServiceTaskManager extends AbstractAsyncTaskManager implements TaskManager {

	public RestServiceTaskManager(Context context) {
		super(context);
	}

	@Override
	public void getAllMain(AsyncTaskCallBack<TaskModel[]> callBack) {
		run("/Task/all?mainTasks=true", RequestMethod.GET, TaskModel[].class, callBack);
	}

	@Override
	public void getAllDelegatedToMe(AsyncTaskCallBack<TaskModel[]> callBack) {
		run("/Task/all?delegatedToMe=true", RequestMethod.GET, TaskModel[].class, callBack);
	}

	@Override
	public void getAllDelegatedToOthers(AsyncTaskCallBack<TaskModel[]> callBack) {
		run("/Task/all?delegatedToOthers=true", RequestMethod.GET, TaskModel[].class, callBack);
	}

	@Override
	public void get(Long id, AsyncTaskCallBack<TaskModel> callBack) {
		run("/Task/" + id, RequestMethod.GET, TaskModel.class, callBack);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		// TODO Auto-generated method stub

	}

}
