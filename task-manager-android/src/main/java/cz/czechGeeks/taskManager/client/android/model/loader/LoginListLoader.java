package cz.czechGeeks.taskManager.client.android.model.loader;

import java.util.List;

import android.content.Context;
import cz.czechGeeks.taskManager.client.android.factory.LoginManagerFactory;
import cz.czechGeeks.taskManager.client.android.model.LoginModel;

public class LoginListLoader extends AbstractListLoader<LoginModel> {

	public LoginListLoader(Context context) {
		super(context);
	}

	@Override
	public List<LoginModel> loadInBackground() {
		// nacteni dat
		return LoginManagerFactory.get(getContext()).getAll();
	}

}
