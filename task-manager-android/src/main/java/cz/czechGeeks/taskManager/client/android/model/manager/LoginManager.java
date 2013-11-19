package cz.czechGeeks.taskManager.client.android.model.manager;

import java.util.List;

import cz.czechGeeks.taskManager.client.android.model.LoginModel;

/**
 * Service pro nacitani loginu
 * 
 * @author lukasb
 * 
 */
public interface LoginManager {

	/**
	 * Vrati vsechny uzivatele
	 * 
	 * @return
	 */
	public List<LoginModel> getAll();

}
