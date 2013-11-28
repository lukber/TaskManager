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
import android.view.MotionEvent;
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

/**
 * Editace ukolu
 * 
 * @author lukasb
 * 
 */
public class TaskDetailEditFragment extends Fragment {

	public interface TaskDetailEditFragmentCallBack {
		/**
		 * Volano po ulozeni zmen
		 * 
		 * @param model
		 */
		void onTaskSaved(TaskModel model);

		/**
		 * Uzivatel stornuje zmeny
		 */
		void onTaskStornoEditing();
	}

	// seznam vsech kategorii
	private Map<Long, TaskCategModel> categMap = new HashMap<Long, TaskCategModel>();

	// seznam vsech uzivatelu
	private Map<Long, LoginModel> loginMap = new HashMap<Long, LoginModel>();

	private TaskDetailEditFragmentCallBack callBack;

	private TaskType taskType;// typ ukolu
	private TaskModel taskModel;// data

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
		case R.id.action_save:// ulozeni zmen
			boolean isValid = checkInputData();
			if (!isValid) {
				// data nejsou validni - nebude se provadet update
				return true;
			}

			AsyncTaskCallBack<TaskModel> putDataCallBack = new AsyncTaskCallBack<TaskModel>() {

				@Override
				public void onSuccess(TaskModel resumeObject) {
					// data byla ulozena
					taskModel = resumeObject;
					callBack.onTaskSaved(resumeObject.createCopy());
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
			return true;

		case R.id.action_storno:// storno uprav
			callBack.onTaskStornoEditing();
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
		if (taskType == null) {
			throw new IllegalArgumentException("TaskType musi byt zadan");
		}

		taskModel = (TaskModel) getArguments().get(TaskDetailActivity.TASK_MODEL);
		if (taskModel == null) {
			throw new IllegalArgumentException("TaskModel musi byt zadan");
		}

		categAdapter = new ArrayAdapter<TaskCategModel>(getActivity(), android.R.layout.simple_spinner_item);
		categ = (Spinner) rootView.findViewById(R.id.taskCateg);
		categ.setAdapter(categAdapter);

		name = (EditText) rootView.findViewById(R.id.taskName);
		desc = (EditText) rootView.findViewById(R.id.taskDesc);

		finishToDate_date = (EditText) rootView.findViewById(R.id.taskFinishToDate_date);
		finishToDate_time = (EditText) rootView.findViewById(R.id.taskFinishToDate_time);
		finishToDateCalendar = Calendar.getInstance();

		finishToDate_date.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// je potreba kontrolovat jaky typ akce je volan protoze metoda je volana dvakrat
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
					return true;
				}
				return false;
			}
		});

		finishToDate_time.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {// je potreba kontrolovat jaky typ akce je volan protoze metoda je volana dvakrat
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
					return true;
				}
				return false;
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

		// nacteni vsech dostupnych kategorii
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

					updateViewFromTaskModel();
				}
			}

			@Override
			public void onError(ErrorMessage message) {
				Toast.makeText(getActivity(), R.string.error_taskCategLoading, Toast.LENGTH_SHORT).show();
			}
		});

		// nacteni vsech uzivatelu
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

					updateViewFromTaskModel();
				}
			}

			@Override
			public void onError(ErrorMessage message) {
				Toast.makeText(getActivity(), R.string.error_loginsLoading, Toast.LENGTH_SHORT).show();
			}
		});

		updateViewFromTaskModel();

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

	@Override
	public void onDetach() {
		super.onDetach();
		callBack = null;
	}

	/**
	 * Kontrola vstupnich dat od uzivatele
	 * 
	 * @return
	 */
	private boolean checkInputData() {
		if (categ.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
			// Neni zadana kategorie
			Toast.makeText(getActivity(), R.string.error_taskCategNotSelected, Toast.LENGTH_SHORT).show();
			return false;
		}

		if (name.getEditableText().toString() == null || name.getEditableText().toString().isEmpty()) {
			// Neni zadan nazev ukolu
			Toast.makeText(getActivity(), R.string.error_taskNameIsNotSet, Toast.LENGTH_SHORT).show();
			return false;
		}

		if (executor.getVisibility() == Spinner.VISIBLE && executor.getSelectedItemPosition() == Spinner.INVALID_POSITION) {
			// Neni nastaven exekutor
			Toast.makeText(getActivity(), R.string.error_taskExecutorNotSelected, Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	/**
	 * Aktualizace dat z formulare do modelu
	 */
	private void updateTaskModelFromView() {
		TaskCategModel selectedCateg = categAdapter.getItem(categ.getSelectedItemPosition());
		taskModel.setCategId(selectedCateg.getId());
		taskModel.setCategName(selectedCateg.getName());

		taskModel.setName(name.getEditableText().toString());
		taskModel.setDesc(desc.getEditableText().toString());

		if (finishToDate_date.getVisibility() == EditText.VISIBLE && !finishToDate_date.getEditableText().toString().isEmpty()) {
			// mam nastaveny nejaky datum
			taskModel.setFinishToDate(new Timestamp(finishToDateCalendar.getTimeInMillis()));
		}

		if (executor.getVisibility() == Spinner.VISIBLE) {
			LoginModel executorModel = executorAdapter.getItem(executor.getSelectedItemPosition());
			taskModel.setExecutorId(executorModel.getId());
			taskModel.setExecutorName(executorModel.getName());
		}
	}

	/**
	 * Aktualizace View z modelu
	 */
	private void updateViewFromTaskModel() {
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

		Long executorId = taskModel.getExecutorId();
		if (executorId != null) {
			executor.setSelection(executorAdapter.getPosition(loginMap.get(executorId)));
		}
	}

}
