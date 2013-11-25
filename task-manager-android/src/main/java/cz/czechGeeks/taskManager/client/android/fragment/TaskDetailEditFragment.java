package cz.czechGeeks.taskManager.client.android.fragment;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.activity.TaskDetailActivity;
import cz.czechGeeks.taskManager.client.android.activity.TaskModelActionsCallBack;
import cz.czechGeeks.taskManager.client.android.factory.LoginManagerFactory;
import cz.czechGeeks.taskManager.client.android.factory.TaskCategManagerFactory;
import cz.czechGeeks.taskManager.client.android.factory.TaskManagerFactory;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.LoginModel;
import cz.czechGeeks.taskManager.client.android.model.TaskCategModel;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.LoginManager;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskCategManager;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskManager;
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
	private EditText finishToDate_date;
	private EditText finishToDate_time;

	private Calendar finishToDateCalendar;

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
			AsyncTaskCallBack<TaskModel> putDataCallBack = new AsyncTaskCallBack<TaskModel>() {

				@Override
				public void onSuccess(TaskModel resumeObject) {
					callBack.onTaskUpdated(resumeObject);
					callBack.performShowPreviewFragment();
				}

				@Override
				public void onError(ErrorMessage message) {
					Toast.makeText(getActivity(), R.string.error_taskSaving, Toast.LENGTH_SHORT).show();
				}
			};

			updateTaskModelFromView();
			TaskManager taskManager = TaskManagerFactory.get(getActivity());
			if (taskModel.getId() == null) {
				// INSERT
				taskManager.insert(taskModel, putDataCallBack);
			} else {
				// UPDATE
				taskManager.update(taskModel, putDataCallBack);
			}
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

	private void updateTaskModelFromView() {
		TaskCategModel selectedCateg = categAdapter.getItem(categ.getSelectedItemPosition());
		taskModel.setCategId(selectedCateg.getId());
		taskModel.setCategName(selectedCateg.getName());

		taskModel.setName(name.getEditableText().toString());
		taskModel.setDesc(desc.getEditableText().toString());

		if (finishToDate_date.getVisibility() != EditText.GONE && !finishToDate_date.getEditableText().toString().isEmpty()) {
			// mam nastaveny nejaky datum
			taskModel.setFinishToDate(new Timestamp(finishToDateCalendar.getTimeInMillis()));
		}

		if (executor.getVisibility() != Spinner.GONE) {
			LoginModel executorModel = executorAdapter.getItem(executor.getSelectedItemPosition());
			taskModel.setExecutorId(executorModel.getId());
			taskModel.setExecutorName(executorModel.getName());
		}
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

		finishToDate_date = (EditText) rootView.findViewById(R.id.taskFinishToDate_date);
		finishToDate_time = (EditText) rootView.findViewById(R.id.taskFinishToDate_time);
		finishToDateCalendar = Calendar.getInstance();

		finishToDate_date.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int year = finishToDateCalendar.get(Calendar.YEAR);
				int monthOfYear = finishToDateCalendar.get(Calendar.MONTH);
				int dayOfMonth = finishToDateCalendar.get(Calendar.DAY_OF_MONTH);

				OnDateSetListener dateCallBack = new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						finishToDateCalendar.set(year, monthOfYear, dayOfMonth);
						finishToDate_date.setText(java.text.DateFormat.getDateInstance().format(finishToDateCalendar.getTime()));
					}
				};

				DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateCallBack, year, monthOfYear, dayOfMonth);
				dialog.show();
			}
		});

		finishToDate_time.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int hourOfDay = finishToDateCalendar.get(Calendar.HOUR_OF_DAY);
				int minute = finishToDateCalendar.get(Calendar.MINUTE);

				OnTimeSetListener timeCallBack = new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						finishToDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						finishToDateCalendar.set(Calendar.MINUTE, minute);
						finishToDate_time.setText(hourOfDay + ":" + minute);
					}
				};

				TimePickerDialog dialog = new TimePickerDialog(getActivity(), timeCallBack, hourOfDay, minute, true);
				dialog.show();
			}
		});

		if (taskType == TaskType.DELEGATED_TO_ME) {
			// Neni mozno menit datum do kdy ma byt dokonceno tak policko schovam
			((TextView) rootView.findViewById(R.id.taskFinishToDateTitle)).setVisibility(Spinner.GONE);
			finishToDate_date.setVisibility(EditText.GONE);
			finishToDate_time.setVisibility(EditText.GONE);
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

		if (taskModel.getFinishToDate() != null) {
			finishToDateCalendar.setTimeInMillis(taskModel.getFinishToDate().getTime());
			finishToDate_date.setText(java.text.DateFormat.getDateInstance().format(finishToDateCalendar.getTime()));
			finishToDate_time.setText(java.text.DateFormat.getTimeInstance().format(finishToDateCalendar.getTime()));
		} else {
			finishToDate_date.setText("");
			finishToDate_time.setText("");
		}

		// finishToDate.setText((taskModel.getFinishToDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(taskModel.getFinishToDate()) : "");

		Long executorId = taskModel.getExecutorId();
		if (executorId != null) {
			executor.setSelection(executorAdapter.getPosition(loginMap.get(executorId)));
		}
	}

}
