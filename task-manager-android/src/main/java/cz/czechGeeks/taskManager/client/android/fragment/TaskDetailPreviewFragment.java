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
import cz.czechGeeks.taskManager.client.android.activity.TaskDetailActivity;
import cz.czechGeeks.taskManager.client.android.factory.TaskManagerFactory;
import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.model.manager.AsyncTaskCallBack;
import cz.czechGeeks.taskManager.client.android.model.manager.TaskManager;
import cz.czechGeeks.taskManager.client.android.util.LoginUtils;

/**
 * Reprezentace nahledu tasku
 * 
 * @author lukasb
 * 
 */
public class TaskDetailPreviewFragment extends Fragment {

	public interface TaskDetailPreviewFragmentCallBack {
		/**
		 * Uzivatel klepnul na tlacitko editovat
		 */
		void onTaskEditButtonClick();

		/**
		 * Ukol byl uzavren
		 * 
		 * @param model
		 */
		void onTaskClosed(TaskModel closedModel);

		/**
		 * Ukol byl smazan
		 * 
		 * @param model
		 */
		void onTaskDeleted(TaskModel deletedModel);

		/**
		 * Ukol byl oznacen jako precteny
		 * 
		 * @param model
		 */
		void onTaskReaded(TaskModel readedModel);
	}

	private TaskDetailPreviewFragmentCallBack callBack;
	private TaskModel taskModel;// data

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
		TaskManager taskManager = TaskManagerFactory.get(getActivity());
		switch (item.getItemId()) {

		case R.id.action_edit_task:// editace tasku

			if (taskModel.isUpdatable()) {// kontrola jestli je mozne ukol upravit
				callBack.onTaskEditButtonClick();

			} else {
				// ukol neni mozne editovat
				if (taskModel.isClosed()) {
					// ukol je jiz uzavren
					Toast.makeText(getActivity(), R.string.error_taskCantEditIsClosed, Toast.LENGTH_SHORT).show();
				} else {
					// nejaky jiny duvod
					Toast.makeText(getActivity(), R.string.task_isNotUpdatable, Toast.LENGTH_SHORT).show();
				}
			}

			return true;

		case R.id.action_delete_task:// odstraneni tasku

			if (taskModel.isDeletable()) {// kontrola jestli mohu smazat
				taskManager.delete(taskModel, new AsyncTaskCallBack<TaskModel>() {

					@Override
					public void onSuccess(TaskModel resumeObject) {
						callBack.onTaskDeleted(resumeObject);
					}

					@Override
					public void onError(ErrorMessage message) {
						Toast.makeText(getActivity(), getActivity().getString(R.string.error_taskNotDeleted) + message.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});

			} else {
				// neni mozne smazat
				if (!LoginUtils.get().getLoggedUserId().equals(taskModel.getInserterId())) {
					// smazat muze jen zakladatel
					Toast.makeText(getActivity(), R.string.error_taskCantDeleteYouAreNotInserter, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), R.string.task_isNotDeletable, Toast.LENGTH_SHORT).show();
				}
			}
			return true;

		case R.id.action_close_task: // uzavreni tasku

			if (taskModel.isCloseable()) {// kontrola jestli je mozne task uzavrit
				taskManager.close(taskModel, new AsyncTaskCallBack<TaskModel>() {

					@Override
					public void onSuccess(TaskModel resumeObject) {
						Toast.makeText(getActivity(), R.string.taskMarkedAsClosed, Toast.LENGTH_SHORT).show();
						setTaskModel(resumeObject);
						callBack.onTaskClosed(resumeObject.createCopy());
					}

					@Override
					public void onError(ErrorMessage message) {
						Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				if (taskModel.isClosed()) {
					// task je jiz uzavren
					Toast.makeText(getActivity(), R.string.error_taskCantCloseIsClosed, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), R.string.task_isNotCloseable, Toast.LENGTH_SHORT).show();
				}
			}
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

		TaskModel taskModel = (TaskModel) getArguments().get(TaskDetailActivity.TASK_MODEL);

		if (taskModel == null) {
			throw new IllegalArgumentException("Argument " + TaskDetailActivity.TASK_MODEL + " musi byt zadan");
		}

		setTaskModel(taskModel);
		markModelAsReaded(taskModel);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callBack = (TaskDetailPreviewFragmentCallBack) activity;
		} catch (Exception e) {
			throw new ClassCastException("Activity musi implementovat " + TaskDetailPreviewFragmentCallBack.class);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		callBack = null;
	}

	/**
	 * nastaveni modelu do view
	 * 
	 * @param taskModel
	 */
	private void setTaskModel(TaskModel taskModel) {
		this.taskModel = taskModel;

		categ.setText(taskModel.getCategName());
		name.setText(taskModel.getName());
		desc.setText(taskModel.getDesc());

		finishToDate.setText((taskModel.getFinishToDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(taskModel.getFinishToDate()) : "");
		finishedDate.setText((taskModel.getFinishedDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(taskModel.getFinishedDate()) : "");

		executor.setText(taskModel.getExecutorName());
		inserter.setText(taskModel.getInserterName());
	}

	/**
	 * kontrola jestli je task delegovan na me a pokud ano tak je oznacen jako precteny
	 * 
	 * @param taskModel
	 */
	public void markModelAsReaded(TaskModel taskModel) {
		if (LoginUtils.get().getLoggedUserId().equals(taskModel.getExecutorId()) && taskModel.isUnread()) {
			// Pokud jsem ten kdo ma ukol splnit a ukol je neprecteny tak ho oznacim jako precteny
			TaskManager taskManager = TaskManagerFactory.get(getActivity());
			taskManager.markAsReaded(taskModel, new AsyncTaskCallBack<TaskModel>() {

				@Override
				public void onSuccess(TaskModel resumeObject) {
					Toast.makeText(getActivity(), R.string.taskMarkedAsReaded, Toast.LENGTH_SHORT).show();
					setTaskModel(resumeObject);
					callBack.onTaskReaded(resumeObject.createCopy());
				}

				@Override
				public void onError(ErrorMessage message) {
					String errorMessage = getActivity().getText(R.string.error_taskMarkingAsReaded) + message.getMessage();
					Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

}
