package cz.czechGeeks.taskManager.client.android.model.manager;

import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;

/**
 * Call back pro dotazy na server
 * 
 * @author lukasb
 * 
 * @param <T>
 */
public interface AsyncTaskWithResultCodeCallBack {

	/**
	 * Vraceni odpovedi
	 * 
	 * @param resumeObject
	 */
	void onSuccess(Integer responseCode);

	/**
	 * Pri volani nastala chyba
	 * 
	 * @param message
	 */
	void onError(ErrorMessage message);

}
