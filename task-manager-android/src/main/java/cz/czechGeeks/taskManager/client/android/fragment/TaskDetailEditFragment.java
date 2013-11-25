package cz.czechGeeks.taskManager.client.android.fragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.activity.TaskDetailActivity;
import cz.czechGeeks.taskManager.client.android.activity.TaskModelActionsCallBack;
import cz.czechGeeks.taskManager.client.android.factory.LoginManagerFactory;
import cz.czechGeeks.taskManager.client.android.factory.TaskCategManagerFactory;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.LoginModel;
import cz.czechGeeks.taskManager.client.android.model.TaskCategModel;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.LoginManager;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskCategManager;
import cz.czechGeeks.taskManager.client.android.util.LoginUtils;
import cz.czechGeeks.taskManager.client.android.util.TaskType;

public class TaskDetailEditFragment extends Fragment {

	public interface TaskDetailEditFragmentCallBack extends TaskModelActionsCallBack {
		void performShowPreviewFragment();
	}

	private Map<Long, TaskCategModel> categMap = new HashMap<Long, TaskCategModel>();
	private Map<Long, LoginModel> loginMap = new HashMap<Long, LoginModel>();

	private TaskDetailEditFragmentCallBack callBack;
	private TaskType taskType;
	private TaskModel taskModel;

	private ArrayAdapter<TaskCategModel> categAdapter;
	private Spinner categ;
	private EditText name;
	private EditText desc;
	private EditText finishToDate;

	private ArrayAdapter<LoginModel> executorAdapter;
	private Spinner executor;

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

		taskType = (TaskType) getArguments().get(TaskDetailActivity.TASK_TYPE);
		taskModel = (TaskModel) getArguments().get(TaskDetailActivity.TASK_MODEL);

		categAdapter = new ArrayAdapter<TaskCategModel>(getActivity(), android.R.layout.simple_spinner_item);
		categ = (Spinner) rootView.findViewById(R.id.taskCateg);
		categ.setAdapter(categAdapter);

		name = (EditText) rootView.findViewById(R.id.taskName);
		desc = (EditText) rootView.findViewById(R.id.taskDesc);

		finishToDate = (EditText) rootView.findViewById(R.id.taskFinishToDate);
		if (taskType == TaskType.DELEGATED_TO_ME) {
			// Neni mozno menit datum do kdy ma byt dokonceno tak policko schovam
			((TextView) rootView.findViewById(R.id.taskFinishToDateTitle)).setVisibility(Spinner.GONE);
			finishToDate.setVisibility(Spinner.GONE);
		}

		executorAdapter = new ArrayAdapter<LoginModel>(getActivity(), android.R.layout.simple_spinner_item);
		executor = (Spinner) rootView.findViewById(R.id.taskExecutor);
		executor.setAdapter(executorAdapter);
		if (taskType == TaskType.MAIN || taskType == TaskType.DELEGATED_TO_ME) {
			// Neni mozno menit executora tak se policko schova
			((TextView) rootView.findViewById(R.id.taskExecutorTitle)).setVisibility(Spinner.GONE);
			executor.setVisibility(Spinner.GONE);
		}

		if (taskModel != null) {
			setModelData(taskModel);
		}

		TaskCategManager taskCategManager = TaskCategManagerFactory.get(getActivity());
		taskCategManager.getAll(new AsyncTaskCallBack<TaskCategModel[]>() {

			@Override
			public void onSuccess(TaskCategModel[] resumeObject) {
				categAdapter.clear();
				categMap.clear();
				if (resumeObject != null) {
					for (TaskCategModel item : resumeObject) {
						categMap.put(item.getId(), item);
					}

					categAdapter.addAll(Arrays.asList(resumeObject));
					categAdapter.notifyDataSetChanged();

					setModelData(taskModel);
				}
			}

			@Override
			public void onError(ErrorMessage message) {
				Toast.makeText(getActivity(), R.string.error_taskCategLoading, Toast.LENGTH_SHORT).show();
			}
		});

		LoginManager loginManager = LoginManagerFactory.get(getActivity());
		loginManager.getAll(new AsyncTaskCallBack<LoginModel[]>() {

			@Override
			public void onSuccess(LoginModel[] resumeObject) {
				executorAdapter.clear();
				loginMap.clear();

				if (resumeObject != null) {
					for (LoginModel item : resumeObject) {
						if (LoginUtils.get().getLoggedUserId().equals(item.getId())) {
							// Nebudu vkladat sam sebe
							continue;
						}

						loginMap.put(item.getId(), item);
					}

					executorAdapter.addAll(loginMap.values());
					executorAdapter.notifyDataSetChanged();

					setModelData(taskModel);
				}
			}

			@Override
			public void onError(ErrorMessage message) {
				Toast.makeText(getActivity(), R.string.error_loginsLoading, Toast.LENGTH_SHORT).show();
			}
		});

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

	private void setModelData(TaskModel taskModel) {
		Long categId = taskModel.getCategId();
		if (categId != null) {
			categ.setSelection(categAdapter.getPosition(categMap.get(categId)));
		}

		name.setText(taskModel.getName());
		desc.setText(taskModel.getDesc());

		finishToDate.setText((taskModel.getFinishToDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(taskModel.getFinishToDate()) : "");

		Long executorId = taskModel.getExecutorId();
		if (executorId != null) {
			executor.setSelection(executorAdapter.getPosition(loginMap.get(executorId)));
		}
	}

}
