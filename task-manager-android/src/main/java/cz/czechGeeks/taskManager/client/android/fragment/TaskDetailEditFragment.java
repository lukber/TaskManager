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
import android.widget.EditText;
import android.widget.Spinner;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.activity.TaskDetailActivity;
import cz.czechGeeks.taskManager.client.android.activity.TaskModelActionsCallBack;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;

public class TaskDetailEditFragment extends Fragment {

	public interface TaskDetailEditFragmentCallBack extends TaskModelActionsCallBack {
		void performShowPreviewFragment();
	}

	private TaskDetailEditFragmentCallBack callBack;
	private TaskModel taskModel;

	private Spinner categ;
	private EditText name;
	private EditText desc;
	private EditText finishToDate;
	private EditText finishedDate;
	private Spinner executor;
	private Spinner inserter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main_task_detail_edit_activity, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save:
			callBack.onTaskUpdated(taskModel);
			return true;
		case R.id.action_storno:
			callBack.performShowPreviewFragment();
			return true;
		default:
			break;
		}
		throw new IllegalArgumentException("Nedefinovana akce:" + item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_task_detail_edit, container, false);

		categ = (Spinner) rootView.findViewById(R.id.taskCateg);
		name = (EditText) rootView.findViewById(R.id.taskName);
		desc = (EditText) rootView.findViewById(R.id.taskDesc);
		finishToDate = (EditText) rootView.findViewById(R.id.taskFinishToDate);
		finishedDate = (EditText) rootView.findViewById(R.id.taskFinishedDate);
		executor = (Spinner) rootView.findViewById(R.id.taskExecutor);
		inserter = (Spinner) rootView.findViewById(R.id.taskInserter);
		binding();

		final TaskModel taskModel = (TaskModel) getArguments().get(TaskDetailActivity.TASK_MODEL);

		if (taskModel != null) {
			setModelData(taskModel);
		}

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			callBack = (TaskDetailEditFragmentCallBack) activity;
		} catch (Exception e) {
			throw new ClassCastException("Activity musi implementovat " + TaskDetailEditFragmentCallBack.class);
		}
	}

	private void binding() {
	}

	private void setModelData(TaskModel taskModel) {
		// categ.setText(taskModel.getCategName());
		name.setText(taskModel.getName());
		desc.setText(taskModel.getDesc());

		finishToDate.setText((taskModel.getFinishToDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(taskModel.getFinishToDate()) : "");
		finishedDate.setText((taskModel.getFinishedDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(taskModel.getFinishedDate()) : "");

		// executor.setText(taskModel.getExecutorName());
		// inserter.setText(taskModel.getInserterName());

		// Pokud prihlaseny uzivatel je ten co ma ukol udelat tak nezobrazuji
		// if (!UserUtil.get().getLoggedUser().getId().equals(taskModel.getExecutorId())) {
		// executorRow.setVisibility(TableRow.VISIBLE);
		// } else {
		// executorRow.setVisibility(TableRow.GONE);
		// }

		// Pokud prihlaseny uzivatel je ten co ukol zalozil tak nezobrazuji
		// if (!UserUtil.get().getLoggedUser().getId().equals(taskModel.getInserterId())) {
		// inserterRow.setVisibility(TableRow.VISIBLE);
		// } else {
		// inserterRow.setVisibility(TableRow.GONE);
		// }
	}

}
