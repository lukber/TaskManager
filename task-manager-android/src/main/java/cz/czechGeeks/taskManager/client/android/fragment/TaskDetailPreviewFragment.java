package cz.czechGeeks.taskManager.client.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.factory.TaskManagerFactory;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.model.manager.AbstractAsyncTaskManager;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskWithResultCodeCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskManager;
import cz.czechGeeks.taskManager.client.android.util.LoginUtils;

public class TaskDetailPreviewFragment extends Fragment implements AsyncTaskCallBack<TaskModel> {

	public static final String TASK_ID = "taskId";

	public interface TaskDetailPreviewFragmentListener {
		void performShowEditFragment();

		void onTaskDetailDeleted();
	}

	private TaskDetailPreviewFragmentListener activityListener;

	private TextView categ;
	private TextView name;
	private TextView desc;
	private TextView finishToDate;
	private TextView finishedDate;

	private TextView executor;
	private TextView inserter;

	private Long taskId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main_task_detail_preview_activity, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit_task:
			activityListener.performShowEditFragment();
			return true;
		case R.id.action_delete_task:
			TaskManager taskManager = TaskManagerFactory.createService(getActivity());
			taskManager.delete(taskId, new AsyncTaskWithResultCodeCallBack() {

				@Override
				public void onSuccess(Integer responseCode) {
					if (responseCode.intValue() == AbstractAsyncTaskManager.STATUS_CODE_OK) {
						Toast.makeText(getActivity(), R.string.valueDeleted, Toast.LENGTH_SHORT).show();
						getActivity().finish();
					} else {
						Toast.makeText(getActivity(), R.string.valueNotDeleted, Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onError(ErrorMessage message) {
					Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_SHORT).show();
				}
			});
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_task_detail_preview, container, false);

		categ = (TextView) rootView.findViewById(R.id.taskCateg);
		name = (TextView) rootView.findViewById(R.id.taskName);
		desc = (TextView) rootView.findViewById(R.id.taskDesc);
		finishToDate = (TextView) rootView.findViewById(R.id.taskFinishToDate);
		finishedDate = (TextView) rootView.findViewById(R.id.taskFinishedDate);
		executor = (TextView) rootView.findViewById(R.id.taskExecutor);
		inserter = (TextView) rootView.findViewById(R.id.taskInserter);

		taskId = (Long) getArguments().get(TASK_ID);

		if (taskId == null) {
			throw new IllegalArgumentException("Argument " + TASK_ID + " musi byt zadan");
		}

		TaskManager taskManager = TaskManagerFactory.createService(getActivity());
		taskManager.get(taskId, this);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			activityListener = (TaskDetailPreviewFragmentListener) activity;
		} catch (Exception e) {
			throw new ClassCastException("Activity musi implementovat " + TaskDetailPreviewFragmentListener.class);
		}
	}

	private void showModelData(TaskModel taskModel) {
		categ.setText(taskModel.getCategName());
		name.setText(taskModel.getName());
		desc.setText(taskModel.getDesc());

		finishToDate.setText((taskModel.getFinishToDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(taskModel.getFinishToDate()) : "");
		finishedDate.setText((taskModel.getFinishedDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(taskModel.getFinishedDate()) : "");

		executor.setText(taskModel.getExecutorName());
		inserter.setText(taskModel.getInserterName());
	}

	@Override
	public void onSuccess(TaskModel resumeObject) {
		showModelData(resumeObject);

		if (LoginUtils.get().getLoggedUserId().equals(resumeObject.getExecutorId()) && resumeObject.isUnread()) {
			// Pokud jsem ten kdo ma ukol splnit a ukol je neprecteny tak ho oznacim jako precteny
			TaskManager taskManager = TaskManagerFactory.createService(getActivity());
			taskManager.markAsReaded(taskId, new AsyncTaskWithResultCodeCallBack() {

				@Override
				public void onSuccess(Integer responseCode) {
					Toast.makeText(getActivity(), R.string.taskMarkedAsReaded, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onError(ErrorMessage message) {
					String errorMessage = getActivity().getText(R.string.taskMarkedAsReaded_error) + message.getMessage();
					Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@Override
	public void onError(ErrorMessage message) {
		Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_LONG).show();
	}

}
