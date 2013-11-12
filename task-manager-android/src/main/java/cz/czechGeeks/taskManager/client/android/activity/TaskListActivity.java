package cz.czechGeeks.taskManager.client.android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.adapter.TaskAdapter;
import cz.czechGeeks.taskManager.client.android.service.TaskService;
import cz.czechGeeks.taskManager.client.android.to.TaskTO;

public class TaskListActivity extends Activity {

	private ListView taskList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);

		TaskTO[] taskTOs = TaskService.get().getAll();

		taskList = (ListView) findViewById(R.id.taskListView);
		taskList.setAdapter(new TaskAdapter(taskTOs, this));
	}

}
