package cz.czechGeeks.taskManager.client.android.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import cz.czechGeeks.taskManager.client.android.R;
import cz.czechGeeks.taskManager.client.android.adapter.TaskListAdapter;
import cz.czechGeeks.taskManager.client.android.model.TaskModel;
import cz.czechGeeks.taskManager.client.android.model.loader.TaskListLoader;

public class TaskListFragment extends ListFragment implements LoaderCallbacks<List<TaskModel>> {

	private TaskListAdapter listAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getResources().getString(R.string.noData));

		setHasOptionsMenu(false);

		listAdapter = new TaskListAdapter(getActivity());
		setListAdapter(listAdapter);

		// Start out with a progress indicator.
		setListShown(false);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.i("CLICK", "Byla vybrana polozka" + position);
	}

	@Override
	public Loader<List<TaskModel>> onCreateLoader(int arg0, Bundle arg1) {
		return new TaskListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<TaskModel>> loader, List<TaskModel> data) {
		listAdapter.setData(data);

		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<TaskModel>> loader) {
		 listAdapter.setData(null);
	}

}
