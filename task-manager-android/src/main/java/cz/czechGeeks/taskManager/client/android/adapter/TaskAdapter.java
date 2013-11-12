package cz.czechGeeks.taskManager.client.android.adapter;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.to.TaskTO;

public class TaskAdapter extends BaseAdapter {

	private final TaskTO[] taskTOs;
	private final Context context;
	private DateFormatSymbols locale;

	public TaskAdapter(TaskTO[] taskTOs, Context context) {
		super();
		this.taskTOs = taskTOs;
		this.context = context;
	}

	@Override
	public int getCount() {
		return (taskTOs != null) ? taskTOs.length : 0;
	}

	@Override
	public TaskTO getItem(int position) {
		return taskTOs[position];
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		View rowView = view;

		if (rowView == null) {
			LayoutInflater layoutinflate = LayoutInflater.from(context);
			rowView = layoutinflate.inflate(R.layout.item_task_list, parent, false);
		}

		TaskTO item = getItem(position);
		((TextView) rowView.findViewById(R.id.taskName)).setText(item.getName());
		((TextView) rowView.findViewById(R.id.taskDesc)).setText(item.getDesc());

		if (item.getFinishToDate() != null) {
			String showTime = SimpleDateFormat.getDateInstance().format(item.getFinishToDate());
			((TextView) rowView.findViewById(R.id.taskFinishToDate)).setText(showTime);
		}

		return rowView;
	}

}
