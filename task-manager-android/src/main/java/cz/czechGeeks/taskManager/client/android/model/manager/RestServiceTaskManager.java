package cz.czechGeeks.taskManager.client.android.model.manager;

import android.content.Context;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
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
	public void insert(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		run("/Task", RequestMethod.POST, task, TaskModel.class, callBack);
	}

	@Override
	public void update(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		run("/Task/" + task.getId(), RequestMethod.PUT, task, TaskModel.class, callBack);
	}

	@Override
	public void delete(final TaskModel task, final AsyncTaskCallBack<TaskModel> callBack) {
		run("/Task/" + task.getId(), RequestMethod.DELETE, TaskModel.class, new AsyncTaskCallBack<TaskModel>() {

			@Override
			public void onSuccess(TaskModel resumeObject) {
				// metoda delete posle jen OK odpoved jako smazano proto musim naplnit sam
				callBack.onSuccess(task);
			}

			@Override
			public void onError(ErrorMessage message) {
				callBack.onError(message);
			}
		});
	}

	@Override
	public void markAsReaded(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		run("/Task/markReaded/" + task.getId(), RequestMethod.PUT, TaskModel.class, callBack);
	}

	@Override
	public void close(TaskModel task, AsyncTaskCallBack<TaskModel> callBack) {
		run("/Task/close/" + task.getId(), RequestMethod.PUT, TaskModel.class, callBack);
	}

}
