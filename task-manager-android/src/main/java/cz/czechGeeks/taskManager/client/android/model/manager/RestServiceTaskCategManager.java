package cz.czechGeeks.taskManager.client.android.model.manager;

import android.content.Context;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.TaskCategModel;

public class RestServiceTaskCategManager extends AbstractAsyncTaskManager implements TaskCategManager {

	public RestServiceTaskCategManager(Context context) {
		super(context);
	}

	@Override
	public void getAll(AsyncTaskCallBack<TaskCategModel[]> callBack) {
		run("/TaskCateg/all", RequestMethod.GET, TaskCategModel[].class, callBack);
	}

	@Override
	public void get(Long id, AsyncTaskCallBack<TaskCategModel> callBack) {
		run("/TaskCateg/" + id, RequestMethod.GET, TaskCategModel.class, callBack);
	}

	@Override
	public void insert(TaskCategModel categ, AsyncTaskCallBack<TaskCategModel> callBack) {
		run("/TaskCateg", RequestMethod.POST, categ, TaskCategModel.class, callBack);
	}

	@Override
	public void update(TaskCategModel categ, AsyncTaskCallBack<TaskCategModel> callBack) {
		run("/TaskCateg/" + categ.getId(), RequestMethod.PUT, categ, TaskCategModel.class, callBack);
	}

	@Override
	public void delete(final TaskCategModel categ, final AsyncTaskCallBack<TaskCategModel> callBack) {
		run("/TaskCateg/" + categ.getId(), RequestMethod.DELETE, TaskCategModel.class, new AsyncTaskCallBack<TaskCategModel>() {

			@Override
			public void onSuccess(TaskCategModel resumeObject) {
				// metoda delete posle jen OK odpoved jako smazano proto musim naplnit sam
				callBack.onSuccess(categ);
			}

			@Override
			public void onError(ErrorMessage message) {
				callBack.onError(message);
			}
		});
	}

}
