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

public class TaskDetailActivity extends FragmentActivity implements TaskDetailPreviewFragmentCallBack, TaskDetailEditFragmentCallBack {

	public static final String TASK_MODEL_ACTION = "modelAction";
	public static final String TASK_MODEL = "model";
	public static final String TASK_TYPE = "modelType";

	private ModelActionType actionType;
	private TaskType taskType;
	private TaskModel taskModel;

	private TaskDetailPreviewFragment previewFragment;
	private TaskDetailEditFragment editFragment;

	@Override
	protected void onCreate(Bundle args) {
		super.onCreate(args);
		setContentView(R.layout.activity_task_detail);
		setResult(RESULT_CANCELED);

		taskType = (TaskType) getIntent().getExtras().get(TASK_TYPE);
		taskModel = (TaskModel) getIntent().getExtras().get(TASK_MODEL);

		if (taskModel != null && taskModel.getId() != null) {
			// uprava stavajiciho zaznamu - zobrazeni nahledu
			performShowPreviewFragment();
		} else {
			// vytvareni noveho zaznamu
			performShowEditFragment();
		}

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private Fragment createOrGetPreviewFragment() {
		if (previewFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(TASK_TYPE, taskType);
			bundle.putSerializable(TASK_MODEL, taskModel);

			previewFragment = new TaskDetailPreviewFragment();
			previewFragment.setArguments(bundle);
		}
		return previewFragment;
	}

	private Fragment createOrGetUpdateFragment() {
		if (editFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(TASK_TYPE, taskType);
			bundle.putSerializable(TASK_MODEL, taskModel);

			editFragment = new TaskDetailEditFragment();
			editFragment.setArguments(bundle);
		}
		return editFragment;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			Intent intent = new Intent();
			intent.putExtra(TASK_TYPE, taskType);
			intent.putExtra(TASK_MODEL_ACTION, actionType);
			intent.putExtra(TASK_MODEL, taskModel);
			setResult(RESULT_OK, intent);

			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void performShowEditFragment() {
		showFragment(createOrGetUpdateFragment());
	}

	@Override
	public void performShowPreviewFragment() {
		showFragment(createOrGetPreviewFragment());
	}

	private void showFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.taskDetailFragmentContainer, fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();
	}

	@Override
	public void onTaskUpdated(TaskModel updatedTask) {
		this.taskModel = updatedTask;
		actionType = ModelActionType.UPDATE;
	}

	@Override
	public void onTaskDeleted(TaskModel deletedTask) {
		Toast.makeText(this, R.string.valueDeleted, Toast.LENGTH_SHORT).show();

		Intent intent = new Intent();
		intent.putExtra(TASK_TYPE, taskType);
		intent.putExtra(TASK_MODEL_ACTION, ModelActionType.DELETE);
		intent.putExtra(TASK_MODEL, deletedTask);
		setResult(RESULT_OK, intent);

		finish();
	}
}
