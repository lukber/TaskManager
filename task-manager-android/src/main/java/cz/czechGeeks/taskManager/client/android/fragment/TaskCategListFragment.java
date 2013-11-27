package cz.czechGeeks.taskManager.client.android.fragment;

import java.util.Arrays;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

/**
 * Seznam kategorii
 * 
 * @author lukasb
 * 
 */
public class TaskCategListFragment extends ListFragment implements TaskCategListAdapterCallBack, TaskCategEditDialogFragmentCallBack {

	private static final String LOG_TAG = "TaskCategListFragment";

	private TaskCategListAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main_task_categ_list_activity, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_newTaskCateg:
			// kliknuti na tlacitko pro zalozeni nove kategorie
			showEditDialog();
			return true;
		default:
			throw new IllegalArgumentException("Nedefinovana akce:" + item);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getResources().getString(R.string.noData));

		listAdapter = new TaskCategListAdapter(getActivity(), this);
		setListAdapter(listAdapter);

		loadData();
	}

	/**
	 * Nacteni vsech dostupnych kategorii
	 */
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
		// Klik na odstranit kategorii
		TaskCategManager taskCategManager = TaskCategManagerFactory.get(getActivity());
		taskCategManager.delete(taskCategModel, new AsyncTaskCallBack<TaskCategModel>() {

			@Override
			public void onSuccess(TaskCategModel resumeObject) {
				// kategorie bzla odstranena
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
	public void onTaskCategSave(final int position, String updatedCategName) {
		Log.i(LOG_TAG, "Kontrola zadane hodnoty");

		if (position != -1) {// byla provedena uprava existujici kategorie
			if (updatedCategName == null || updatedCategName.trim().isEmpty()) {
				Toast.makeText(getActivity(), R.string.error_taskCategNameNotValid, Toast.LENGTH_SHORT).show();
				showEditDialog(position, updatedCategName);
				return;
			}

			updatedCategName = updatedCategName.trim();

			Log.i(LOG_TAG, "Kontrola jestli se nazev zmenil");
			if (listAdapter.getItem(position).getName().equalsIgnoreCase(updatedCategName)) {
				Log.w(LOG_TAG, "Nazev se nezmenil");
				return;
			}
		}

		Log.i(LOG_TAG, "Kontrola jestli jsem nezadal nazev jiz existujici kategorie");
		for (int i = 0; i < listAdapter.getCount(); i++) {
			if (position == i) {
				// Stejna pozice jako chci zmenit
				continue;
			}

			if (listAdapter.getItem(i).getName().equalsIgnoreCase(updatedCategName)) {
				Log.e(LOG_TAG, "Kategorie s nazvem " + updatedCategName + " uz existuje");
				Toast.makeText(getActivity(), R.string.error_taskCategNameExists, Toast.LENGTH_SHORT).show();
				showEditDialog(position, updatedCategName);
				return;
			}
		}

		Log.i(LOG_TAG, "Provadim zmenu kategorie");

		AsyncTaskCallBack<TaskCategModel> asyncTaskCallBack = new AsyncTaskCallBack<TaskCategModel>() {

			@Override
			public void onSuccess(TaskCategModel resumeObject) {
				Log.i(LOG_TAG, "Nazev kategorie upraven");
				listAdapter.remove(resumeObject);
				listAdapter.insert(resumeObject, position == -1 ? 0 : position);
				listAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(ErrorMessage message) {
				Toast.makeText(getActivity(), message.getMessage(), Toast.LENGTH_SHORT).show();
			}
		};

		TaskCategManager taskCategManager = TaskCategManagerFactory.get(getActivity());
		if (position != -1) {
			// update
			final TaskCategModel taskCategModel = listAdapter.getItem(position);
			taskCategModel.setName(updatedCategName);
			taskCategManager.update(taskCategModel, asyncTaskCallBack);
		} else {
			// insert
			final TaskCategModel taskCategModel = new TaskCategModel();
			taskCategModel.setName(updatedCategName);
			taskCategManager.insert(taskCategModel, asyncTaskCallBack);
		}
	}

	@Override
	public void onTaskCategEditButtonClick(TaskCategModel taskCategModel) {
		int position = listAdapter.getPosition(taskCategModel);
		showEditDialog(position, taskCategModel.getName());
	}

	private void showEditDialog() {
		showEditDialog(-1, null);
	}

	/**
	 * Zobrazeni editacniho dialogu
	 * 
	 * @param position
	 * @param cantegName
	 */
	private void showEditDialog(int position, String cantegName) {
		Bundle bundle = new Bundle();
		bundle.putInt(TaskCategEditDialogFragment.TASK_CATEG_POSITION, position);
		bundle.putString(TaskCategEditDialogFragment.TASK_CATEG_NAME, cantegName);

		TaskCategEditDialogFragment editDialog = new TaskCategEditDialogFragment();
		editDialog.setArguments(bundle);
		editDialog.setTargetFragment(this, 0);
		editDialog.show(getFragmentManager(), "editCategDialog");
	}

}
