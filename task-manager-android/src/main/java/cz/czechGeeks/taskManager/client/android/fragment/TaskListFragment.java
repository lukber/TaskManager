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
import cz.czechGeeks.taskManager.client.android.util.TaskType;

/**
 * Reprezentace seznamu ukolu
 * 
 * @author lukasb
 * 
 */
public class TaskListFragment extends ListFragment implements AsyncTaskCallBack<TaskModel[]> {

	public interface TaskListFragmentCallBack {
		/**
		 * Byla vybrana polozka v seznamu
		 * 
		 * @param model
		 *            vybrany ukol
		 */
		public void onTaskListItemSelected(TaskModel model);
	}

	private TaskListAdapter listAdapter;
	private TaskListFragmentCallBack callBack;
	private TaskType taskType;// typ ukolu - rozlisuje jaka data se budou nacitat

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

		loadData();
	}

	/**
	 * Nacteni dat pro zalozku
	 */
	public void loadData() {
		TaskManager taskManager = TaskManagerFactory.get(getActivity());
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
		// byla vybrana polozka seznamu
		super.onListItemClick(l, v, position, id);
		callBack.onTaskListItemSelected(listAdapter.getItem(position));
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	@Override
	public void onSuccess(TaskModel[] resumeObject) {
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

}
