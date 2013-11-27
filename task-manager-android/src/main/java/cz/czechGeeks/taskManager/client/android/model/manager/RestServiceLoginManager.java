package cz.czechGeeks.taskManager.client.android.model.manager;

import android.content.Context;
import cz.czechGeeks.taskManager.client.android.model.LoginModel;
import cz.czechGeeks.taskManager.client.android.util.StorageAndPreferencesUtils;
import cz.czechGeeks.taskManager.client.android.util.StorageAndPreferencesUtils.ConnectionItems;

/**
 * Implementace pro REST sluzny pro LOGIN
 * 
 * @author lukasb
 * 
 */
public class RestServiceLoginManager extends AbstractAsyncTaskManager implements LoginManager {

	public RestServiceLoginManager(Context context) {
		super(context);
	}

	@Override
	public void signIn(String userName, String password, AsyncTaskCallBack<LoginModel> callBack) {
		ConnectionItems connectionItems = StorageAndPreferencesUtils.getConnectionItems(getContext());
		run(connectionItems.BASE_URL + "/Login", userName, password, RequestMethod.GET, LoginModel.class, callBack);
	}

	@Override
	public void get(long userId, AsyncTaskCallBack<LoginModel> callBack) {
		run("/Login/" + userId, RequestMethod.GET, LoginModel.class, callBack);
	}

	@Override
	public void getAll(AsyncTaskCallBack<LoginModel[]> callBack) {
		run("/Login/all", RequestMethod.GET, LoginModel[].class, callBack);
	}

}
