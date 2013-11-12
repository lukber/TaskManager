package cz.czechGeeks.taskManager.client.android.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;

import android.util.Log;
import cz.czechGeeks.taskManager.client.android.exception.UnauthorizedException;
import cz.czechGeeks.taskManager.client.android.to.LoginTO;

public class LoginService extends AbstractService {

	private static LoginService instance;

	private static Map<Long, LoginTO> loginsMap;
	private static LoginTO loggedLogin;

	private LoginService() {
		loginsMap = new HashMap<Long, LoginTO>();
	}

	public static LoginService get() {
		if (instance == null) {
			instance = new LoginService();
		}
		return instance;
	}

	public boolean signIn(String userName, String password) throws UnauthorizedException {
		setUserName(userName);
		setPassword(password);
		loggedLogin = (LoginTO) callRestService("/Login", HttpMethod.GET, LoginTO.class);
		return true;
	}

	public boolean isAuthorized() {
		return loggedLogin != null;
	}

	public LoginTO getLoggedLogin() {
		return loggedLogin;
	}

	public Collection<LoginTO> getAll() {
		if (!loginsMap.isEmpty()) {
			return loginsMap.values();
		}
		try {
			LoginTO[] tos = (LoginTO[]) callRestService("/Login/all", HttpMethod.GET, LoginTO[].class);
			for (LoginTO loginTO : tos) {
				loginsMap.put(loginTO.getId(), loginTO);
			}
		} catch (Exception e) {
			Log.e("Chyba nacitani dat", "Nastala chyba pri nacitani vsech uzivatelu: " + e.getMessage());
		}
		return loginsMap.values();
	}

	public void clearData() {
		loginsMap.clear();
	}

}
