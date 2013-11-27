package cz.czechGeeks.taskManager.client.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;

/**
 * List adapter pro ukoly
 * 
 * @author lukasb
 * 
 */
public class TaskListAdapter extends ArrayAdapter<TaskModel> {

	private final LayoutInflater layoutInflater;

	public TaskListAdapter(Context context) {
		super(context, R.layout.task_list_item);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			view = layoutInflater.inflate(R.layout.task_list_item, parent, false);
		} else {
			view = convertView;
		}

		TaskModel item = getItem(position);

		((TextView) view.findViewById(R.id.taskName)).setText(item.getName());
		((TextView) view.findViewById(R.id.taskCateg)).setText(item.getCategName());
		((TextView) view.findViewById(R.id.taskFinishToDate)).setText((item.getFinishToDate() != null) ? java.text.DateFormat.getDateTimeInstance().format(item.getFinishToDate()) : "");

		// Zobrazeni ikony pokud je ukol uzavren
		ImageView taskImageClosed = (ImageView) view.findViewById(R.id.taskImageClosed);
		taskImageClosed.setVisibility(item.isClosed() ? ImageView.VISIBLE : ImageView.GONE);

		// zobrazeni ikonkty pokud je ukol neprecteny
		ImageView taskImageUnread = (ImageView) view.findViewById(R.id.taskImageUnread);
		taskImageUnread.setVisibility(item.isUnread() ? ImageView.VISIBLE : ImageView.GONE);

		return view;
	}

}
