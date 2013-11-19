package cz.czechGeeks.taskManager.client.android.model.loader;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class AbstractListLoader<T> extends AsyncTaskLoader<List<T>>{

	List<T> dataList;
	
	public AbstractListLoader(Context context) {
		super(context);
	}

	/**
	 * Nastaveni nactenich dat
	 */
	@Override
	public void deliverResult(List<T> apps) {
		super.deliverResult(apps);
		dataList = apps;
	}

	/**
	 * Zachyceni pozadavku na nacteni dat
	 */
	@Override
	protected void onStartLoading() {
		if (dataList != null) {
			// data uz jsou nactena - nastavim jiz nactena
			deliverResult(dataList);
		}

		if (takeContentChanged() || dataList == null) {
			// data se nejak zmenila nebo jeste nebyla vubec nactena
			forceLoad();// nacti data
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		if (dataList != null) {
			dataList = null;
		}
	}

}
