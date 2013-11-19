package cz.czechGeeks.taskManager.client.android.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.util.UserUtil;

public class TaskPreviewDetailActivity extends FragmentActivity {
	public static final String TASK_MODEL = "model";

	private TextView categ;
	private TextView name;
	private TextView desc;
	private TextView finishToDate;
	private TextView finishedDate;

	private TableRow executorRow;
	private TextView executor;

	private TableRow inserterRow;
	private TextView inserter;

	private Button closeButton;
	private Button updateButton;
	private Button deleteButton;

	@Override
	protected void onCreate(Bundle args) {
		super.onCreate(args);
		setContentView(R.layout.fragment_task_detail_preview);

		categ = (TextView) findViewById(R.id.taskCateg);
		name = (TextView) findViewById(R.id.taskName);
		desc = (TextView) findViewById(R.id.taskDesc);
		finishToDate = (TextView) findViewById(R.id.taskFinishToDate);
		finishedDate = (TextView) findViewById(R.id.taskFinishedDate);

		executorRow = (TableRow) findViewById(R.id.taskExecutorRow);
		executor = (TextView) findViewById(R.id.taskExecutor);

		inserterRow = (TableRow) findViewById(R.id.taskInserterRow);
		inserter = (TextView) findViewById(R.id.taskInserter);

		closeButton = (Button) findViewById(R.id.closeButton);
		updateButton = (Button) findViewById(R.id.updateButton);
		deleteButton = (Button) findViewById(R.id.deleteButton);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		setModel((TaskModel) getIntent().getExtras().get(TASK_MODEL));
	}

	public void setModel(TaskModel task) {
		if (task == null) {
			task = new TaskModel();
		}

		categ.setText(task.getCategName());
		name.setText(task.getName());
		desc.setText(task.getDesc());

		finishToDate.setText((task.getFinishToDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(task.getFinishToDate()) : "");
		finishedDate.setText((task.getFinishedDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(task.getFinishedDate()) : "");

		executor.setText(task.getExecutorName());
		inserter.setText(task.getInserterName());

		// Pokud prihlaseny uzivatel je ten co ma ukol udelat tak nezobrazuji
		if (!UserUtil.get().getLoggedUser().getId().equals(task.getExecutorId())) {
			executorRow.setVisibility(TableRow.VISIBLE);
		} else {
			executorRow.setVisibility(TableRow.GONE);
		}

		// Pokud prihlaseny uzivatel je ten co ukol zalozil tak nezobrazuji
		if (!UserUtil.get().getLoggedUser().getId().equals(task.getInserterId())) {
			inserterRow.setVisibility(TableRow.VISIBLE);
		} else {
			inserterRow.setVisibility(TableRow.GONE);
		}
		
		closeButton.setEnabled(task.isCloseable());
		updateButton.setEnabled(task.isUpdatable());
		deleteButton.setEnabled(task.isDeletable());
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
}
