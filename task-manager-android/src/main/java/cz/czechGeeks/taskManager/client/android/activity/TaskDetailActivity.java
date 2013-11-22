package cz.czechGeeks.taskManager.client.android.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailEditFragment;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailEditFragment.TaskDetailEditFragmentListener;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailPreviewFragment;
import cz.czechGeeks.taskManager.client.android.fragment.TaskDetailPreviewFragment.TaskDetailPreviewFragmentListener;

public class TaskDetailActivity extends FragmentActivity implements TaskDetailPreviewFragmentListener, TaskDetailEditFragmentListener {
	public static final String TASK_ID = "taskId";

	private Long taskId;

	private TaskDetailPreviewFragment previewFragment;
	private TaskDetailEditFragment editFragment;

	@Override
	protected void onCreate(Bundle args) {
		super.onCreate(args);
		setContentView(R.layout.activity_task_detail);

		taskId = (Long) getIntent().getExtras().get(TASK_ID);

		if (taskId != null) {
			// uprava stavajiciho zaznamu
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
			bundle.putSerializable(TaskDetailPreviewFragment.TASK_ID, taskId);

			previewFragment = new TaskDetailPreviewFragment();
			previewFragment.setArguments(bundle);
		}
		return previewFragment;
	}

	private Fragment createOrGetUpdateFragment() {
		if (editFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(TaskDetailEditFragment.TASK_ID, taskId);

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
			finish();
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

	@Override
	public void onTaskDetailDeleted() {
	}
}
