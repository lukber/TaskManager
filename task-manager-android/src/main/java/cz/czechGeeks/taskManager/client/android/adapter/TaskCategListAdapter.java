package cz.czechGeeks.taskManager.client.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.model.TaskCategModel;

/**
 * List adapter pro kategorie
 * 
 * @author lukasb
 * 
 */
public class TaskCategListAdapter extends ArrayAdapter<TaskCategModel> {

	public interface TaskCategListAdapterCallBack {
		void onTaskCategEditButtonClick(TaskCategModel taskCategModel);

		void onTaskCategDeleteButtonClick(TaskCategModel taskCategModel);
	}

	private final LayoutInflater layoutInflater;
	private TaskCategListAdapterCallBack callBack;

	public TaskCategListAdapter(Context context, TaskCategListAdapterCallBack callBack) {
		super(context, R.layout.task_categ_list_item);
		this.callBack = callBack;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<TaskCategModel> data) {
		clear();
		if (data != null) {
			addAll(data);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.task_categ_list_item, parent, false);
		} else {
			view = convertView;
		}

		final TaskCategModel item = getItem(position);

		((TextView) view.findViewById(R.id.taskCateg)).setText(item.getName());

		// tlacitko pro editaci kategorie
		ImageButton editButton = (ImageButton) view.findViewById(R.id.taskCategEditButton);
		if (item.isUpdatable()) {
			editButton.setVisibility(Button.VISIBLE);
			editButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					callBack.onTaskCategEditButtonClick(item);
				}
			});
		} else {
			editButton.setOnClickListener(null);
			editButton.setVisibility(Button.GONE);
		}

		// tlacitko pro odstraneni kategorie
		ImageButton deleteButton = (ImageButton) view.findViewById(R.id.taskCategDeleteButton);
		if (item.isDeletable()) {
			deleteButton.setVisibility(Button.VISIBLE);
			deleteButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					callBack.onTaskCategDeleteButtonClick(item);
				}
			});
		} else {
			deleteButton.setOnClickListener(null);
			deleteButton.setVisibility(Button.GONE);
		}

		return view;
	}

}
