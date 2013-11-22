package cz.czechGeeks.taskManager.client.android.fragment;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.adapter.TaskListAdapter;
import cz.czechGeeks.taskManager.client.android.factory.TaskManagerFactory;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskManager;

public class TaskListFragment extends ListFragment implements AsyncTaskCallBack<TaskModel[]> {

	public enum TaskType {
		MAIN, DELEGATED_TO_ME, DELEGATED_TO_OTHERS
	}

	public interface TaskListFragmentCallBack {
		public void onTaskListItemSelected(TaskModel model);
	}

	private TaskListAdapter listAdapter;
	private TaskListFragmentCallBack callBack;
	private TaskType taskType;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getResources().getString(R.string.noData));

		setHasOptionsMenu(false);

		listAdapter = new TaskListAdapter(getActivity());
		setListAdapter(listAdapter);

		if (taskType == null) {
			throw new IllegalArgumentException("Typ tasku musi byt definovan");
		}

		TaskManager taskManager = TaskManagerFactory.createService(getActivity());
		switch (taskType) {
		case MAIN:
			taskManager.getAllMain(this);
			break;
		case DELEGATED_TO_ME:
			taskManager.getAllDelegatedToMe(this);
			break;
		case DELEGATED_TO_OTHERS:
			taskManager.getAllDelegatedToOthers(this);
			break;
		default:
			throw new IllegalArgumentException("Nedefinovana metoda");
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof TaskListFragmentCallBack) {
			callBack = (TaskListFragmentCallBack) activity;
		} else {
			throw new ClassCastException("View musi implementovat " + TaskListFragmentCallBack.class);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		callBack = null;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		callBack.onTaskListItemSelected(listAdapter.getItem(position));
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	@Override
	public void onSuccess(TaskModel[] resumeObject) {
		setEmptyText(getResources().getString(R.string.noData));
		listAdapter.setData(Arrays.asList(resumeObject));
	}

	@Override
	public void onError(ErrorMessage message) {
		setEmptyText(message.getMessage());
	}

}
