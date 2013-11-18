package cz.czechGeeks.taskManager.client.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cz.czechGeeks.taskManager.client.android.R;

public class TaskDetailFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_task_detail, container, false);
		Bundle args = getArguments();
		((TextView) rootView.findViewById(R.id.textView1)).setText("Detail");
		return rootView;
	}
	
}
