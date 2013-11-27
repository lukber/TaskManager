package cz.czechGeeks.taskManager.client.android.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailEditFragment;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailEditFragment.TaskDetailEditFragmentCallBack;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailPreviewFragment;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailPreviewFragment.TaskDetailPreviewFragmentCallBack;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.util.ModelActionType;
import cz.czechGeeks.taskManager.client.android.util.TaskType;

/**
 * Repreyentace detail tasku
 * 
 * @author lukasb
 * 
 */
public class TaskDetailActivity extends FragmentActivity implements TaskDetailPreviewFragmentCallBack, TaskDetailEditFragmentCallBack {

	// Extras parametry
	public static final String TASK_MODEL_ACTION_TYPE = "cz.czechGeeks.taskManager.client.android.activity.ModelActionType";// Vystupni priznak - co se s ukolem udalo
	public static final String TASK_TYPE = "cz.czechGeeks.taskManager.client.android.activity.ModelType";
	public static final String TASK_MODEL = "cz.czechGeeks.taskManager.client.android.activity.Model";

	private ModelActionType actionType;// typ akce nad provedenym taskem
	private TaskType taskType;// typ tasku
	private TaskModel taskModel;// zdroj dat

	@Override
	protected void onCreate(Bundle args) {
		super.onCreate(args);
		setContentView(R.layout.activity_task_detail);

		taskType = (TaskType) getIntent().getExtras().get(TASK_TYPE);
		if (taskType == null) {
			throw new IllegalArgumentException("Argument " + TASK_TYPE + " musi byt zadan");
		}

		taskModel = (TaskModel) getIntent().getExtras().get(TASK_MODEL);
		if (taskModel == null) {
			throw new IllegalArgumentException("Argument " + TASK_MODEL + " musi byt zadan");
		}

		if (taskModel != null && taskModel.getId() != null) {
			// mam zdroj - zobrazim nahled
			showPreviewFragment();
		} else {
			// nemam zdroj - jedna se o zakladani
			showUpdateFragment();
		}

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Vytvoreni fragmentu pro nahled
	 * 
	 * @return
	 */
	private Fragment createPreviewFragment() {
		Bundle bundle = new Bundle();
		bundle.putSerializable(TASK_TYPE, taskType);
		bundle.putSerializable(TASK_MODEL, taskModel);

		TaskDetailPreviewFragment previewFragment = new TaskDetailPreviewFragment();
		previewFragment.setArguments(bundle);
		return previewFragment;
	}

	/**
	 * Vytvoreni fragmentu pro editaci
	 * 
	 * @return
	 */
	private Fragment createUpdateFragment() {
		Bundle bundle = new Bundle();
		bundle.putSerializable(TASK_TYPE, taskType);
		bundle.putSerializable(TASK_MODEL, taskModel);

		TaskDetailEditFragment editFragment = new TaskDetailEditFragment();
		editFragment.setArguments(bundle);
		return editFragment;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			// klik na tlacitko zpet
			Intent intent = new Intent();
			intent.putExtra(TASK_MODEL_ACTION_TYPE, actionType);
			intent.putExtra(TASK_TYPE, taskType);
			intent.putExtra(TASK_MODEL, taskModel);
			setResult(RESULT_OK, intent);

			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Zobrazeni fragmentu
	 * 
	 * @param fragment
	 */
	private void showFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.taskDetailFragmentContainer, fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();
	}

	@Override
	public void onTaskDeleted(TaskModel deletedTask) {
		Toast.makeText(this, R.string.valueDeleted, Toast.LENGTH_SHORT).show();

		Intent intent = new Intent();
		intent.putExtra(TASK_MODEL_ACTION_TYPE, ModelActionType.DELETE);
		intent.putExtra(TASK_TYPE, taskType);
		intent.putExtra(TASK_MODEL, deletedTask);
		setResult(RESULT_OK, intent);

		finish();
	}

	@Override
	public void onTaskSaved(TaskModel model) {
		this.taskModel = model;
		actionType = ModelActionType.UPDATE;
		showPreviewFragment();
	}

	@Override
	public void onTaskStornoEditing() {
		if (taskModel.getId() == null) {
			// Je zakladan novy ukol - storno ukonci detail activity
			setResult(RESULT_CANCELED);
			finish();
			return;
		}

		showPreviewFragment();
	}

	private void showPreviewFragment() {
		setTitle(R.string.task_detail);
		showFragment(createPreviewFragment());
	}

	@Override
	public void onTaskEditButtonClick() {
		showUpdateFragment();
	}

	private void showUpdateFragment() {
		if (taskModel.getId() == null) {
			setTitle(R.string.task_new);
		} else {
			setTitle(R.string.task_edit);
		}
		showFragment(createUpdateFragment());
	}

	@Override
	public void onTaskClosed(TaskModel closedModel) {
		this.taskModel = closedModel;
		actionType = ModelActionType.UPDATE;
	}

	@Override
	public void onTaskReaded(TaskModel readedModel) {
		this.taskModel = readedModel;
		actionType = ModelActionType.UPDATE;
	}
}
