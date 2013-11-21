package cz.czechGeeks.taskManager.client.android.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailEditFragment;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailEditFragment.TaskDetailEditFragmentListener;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailPreviewFragment;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailPreviewFragment.TaskDetailPreviewFragmentListener;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;

public class TaskDetailActivity extends FragmentActivity implements TaskDetailPreviewFragmentListener, TaskDetailEditFragmentListener {
	public static final String TASK_MODEL = "model";

	private TaskModel taskModel;

	private TaskDetailPreviewFragment previewFragment;
	private TaskDetailEditFragment editFragment;

	@Override
	protected void onCreate(Bundle args) {
		super.onCreate(args);
		setContentView(R.layout.activity_task_detail);

		taskModel = (TaskModel) getIntent().getExtras().get(TASK_MODEL);

		if (taskModel == null) {
			// vytvareni noveho zaznamu
			performShowEditFragment();
		} else {
			// uprava stavajiciho zaznamu
			performShowPreviewFragment();
		}

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private Fragment createOrGetPreviewFragment() {
		if (previewFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(TaskDetailEditFragment.TASK_MODEL, taskModel);

			previewFragment = new TaskDetailPreviewFragment();
			previewFragment.setArguments(bundle);
		}
		return previewFragment;
	}

	private Fragment createOrGetUpdateFragment() {
		if (editFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(TaskDetailEditFragment.TASK_MODEL, taskModel);

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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void performShowEditFragment() {
		showFragment(createOrGetUpdateFragment());
	}

	private void showFragment(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.taskDetailFragmentContainer, fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();
	}

	@Override
	public void performShowPreviewFragment() {
		showFragment(createOrGetPreviewFragment());
	}
}
