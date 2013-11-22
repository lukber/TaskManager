package cz.czechGeeks.taskManager.client.android.fragment;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.adapter.TaskListAdapter;
import cz.czechGeeks.taskManager.client.android.factory.TaskManagerFactory;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskManager;

public class TaskListFragment extends ListFragment {

	public interface TaskListFragmentCallBack {
		public void onTaskListItemSelected(TaskModel model);
	}

	private TaskListAdapter listAdapter;
	private TaskListFragmentCallBack callBack;
	private List<TaskModel> data;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getResources().getString(R.string.noData));

		setHasOptionsMenu(false);

		listAdapter = new TaskListAdapter(getActivity());
		setListAdapter(listAdapter);

		TaskManager taskManager = TaskManagerFactory.createService(getActivity());
		taskManager.getAllDelegatedToMe(new AsyncTaskCallBack<TaskModel[]>() {

			@Override
			public void onSuccess(TaskModel[] resumeObject) {
				listAdapter.setData(Arrays.asList(resumeObject));
			}

			@Override
			public void onError(ErrorMessage message) {
				Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
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

}
