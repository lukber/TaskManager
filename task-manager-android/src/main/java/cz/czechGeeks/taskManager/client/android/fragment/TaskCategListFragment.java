package cz.czechGeeks.taskManager.client.android.fragment;

import java.util.Arrays;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.Toast;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.adapter.TaskCategListAdapter;
import cz.czechGeeks.taskManager.client.android.adapter.TaskCategListAdapter.TaskCategListAdapterCallBack;
import cz.czechGeeks.taskManager.client.android.factory.TaskCategManagerFactory;
import cz.czechGeeks.taskManager.client.android.fragment.TaskCategEditDialogFragment.TaskCategEditDialogFragmentCallBack;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.TaskCategModel;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskCategManager;

public class TaskCategListFragment extends ListFragment implements TaskCategListAdapterCallBack, TaskCategEditDialogFragmentCallBack {

	private TaskCategListAdapter listAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getResources().getString(R.string.noData));

		setHasOptionsMenu(false);

		listAdapter = new TaskCategListAdapter(getActivity(), this);
		setListAdapter(listAdapter);

		loadData();
	}

	public void loadData() {
		TaskCategManager taskCategManager = TaskCategManagerFactory.get(getActivity());
		taskCategManager.getAll(new AsyncTaskCallBack<TaskCategModel[]>() {

			@Override
			public void onSuccess(TaskCategModel[] resumeObject) {
				setEmptyText(getResources().getString(R.string.noData));
				listAdapter.setData(Arrays.asList(resumeObject));
				listAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(ErrorMessage message) {
				setEmptyText(message.getMessage());
				listAdapter.setData(null);
				listAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onTaskCategDeleteButtonClick(TaskCategModel taskCategModel) {
		TaskCategManager taskCategManager = TaskCategManagerFactory.get(getActivity());
		taskCategManager.delete(taskCategModel, new AsyncTaskCallBack<TaskCategModel>() {

			@Override
			public void onSuccess(TaskCategModel resumeObject) {
				listAdapter.remove(resumeObject);
				listAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(ErrorMessage message) {
				String error = getActivity().getString(R.string.error_taskCategDelete) + message.getMessage();
				Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onTaskCategSave(final int position, String newCategName) {
		final TaskCategModel taskCategModel = listAdapter.getItem(position);
		taskCategModel.setName(newCategName);
		TaskCategManager taskCategManager = TaskCategManagerFactory.get(getActivity());
		taskCategManager.update(taskCategModel, new AsyncTaskCallBack<TaskCategModel>() {

			@Override
			public void onSuccess(TaskCategModel resumeObject) {
				listAdapter.remove(taskCategModel);
				listAdapter.insert(taskCategModel, position);
				listAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(ErrorMessage message) {
				Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onTaskCategEditButtonClick(TaskCategModel taskCategModel) {
		int position = listAdapter.getPosition(taskCategModel);
		Bundle bundle = new Bundle();
		bundle.putInt(TaskCategEditDialogFragment.TASK_CATEG_POSITION, position);
		bundle.putString(TaskCategEditDialogFragment.TASK_CATEG_NAME, taskCategModel.getName());

		TaskCategEditDialogFragment editDialog = new TaskCategEditDialogFragment();
		editDialog.setArguments(bundle);
		editDialog.setTargetFragment(this, 0);
		editDialog.show(getFragmentManager(), "editCategDialog");
	}

}
