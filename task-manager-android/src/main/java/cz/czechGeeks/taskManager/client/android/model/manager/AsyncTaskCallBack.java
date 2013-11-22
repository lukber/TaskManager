package cz.czechGeeks.taskManager.client.android.model.manager;

import cz.czechGeeks.taskManager.client.android.model.ErrorMessage;

/**
 * Call back pro dotazy na server
 * 
 * @author lukasb
 * 
 * @param <T>
 */
public interface AsyncTaskCallBack<T> {

	/**
	 * Vraceni odpovedi
	 * 
	 * @param resumeObject
	 */
	void onSuccess(T resumeObject);

	/**
	 * Pri volani nastala chyba
	 * 
	 * @param message
	 */
	void onError(ErrorMessage message);

}
