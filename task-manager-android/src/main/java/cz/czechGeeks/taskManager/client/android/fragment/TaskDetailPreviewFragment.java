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
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;

public class TaskDetailPreviewFragment extends Fragment {

	public static final String TASK_MODEL = "model";

	public interface TaskDetailPreviewFragmentListener {
		void performShowEditFragment();
	}

	private TaskDetailPreviewFragmentListener activityListener;

	private TextView categ;
	private TextView name;
	private TextView desc;
	private TextView finishToDate;
	private TextView finishedDate;

	private TextView executor;
	private TextView inserter;

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

		final TaskModel taskModel = (TaskModel) getArguments().get(TASK_MODEL);
		if (taskModel == null) {
			throw new IllegalArgumentException("Argument " + TASK_MODEL + " musi byt zadan");
		}

		showModelData(taskModel);
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

}
