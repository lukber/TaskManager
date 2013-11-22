package cz.czechGeeks.taskManager.client.android.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;

public class TaskListAdapter extends ArrayAdapter<TaskModel> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final LayoutInflater mInflater;

	public TaskListAdapter(Context context) {
		super(context, R.layout.task_list_item);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<TaskModel> data) {
		clear();
		if (data != null) {
			addAll(data);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.task_list_item, parent, false);
		} else {
			view = convertView;
		}

		TaskModel item = getItem(position);

		((TextView) view.findViewById(R.id.taskName)).setText(item.getName());
		((TextView) view.findViewById(R.id.taskDesc)).setText(item.getDesc());
		((TextView) view.findViewById(R.id.taskCateg)).setText(item.getCategName());
		((TextView) view.findViewById(R.id.taskFinishToDate)).setText((item.getFinishToDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(item.getFinishToDate()) : "");

		return view;
	}

}
