package cz.czechGeeks.taskManager.client.android.model.manager;

import java.util.ArrayList;
import java.util.List;

import cz.czechGeeks.taskManager.client.android.model.LoginModel;

public class FakeLoginManager implements LoginManager {

	private static List<LoginModel> mainData = new ArrayList<LoginModel>();

	public FakeLoginManager() {
		if (mainData.size() == 0) {
			int dataCounts = 20;
			for (int i = 1; i <= dataCounts; i++) {
				Long id = Long.valueOf(i);
				String name = "Uzivatel " + i;
				mainData.add(new LoginModel(id, name));
			}
		}
	}

	@Override
	public List<LoginModel> getAll() {
		return mainData;
	}

	@Override
	public LoginModel get(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void signIn(String userName, String password, SignInCallBack callBack) {
		// TODO Auto-generated method stub
		
	}

}
