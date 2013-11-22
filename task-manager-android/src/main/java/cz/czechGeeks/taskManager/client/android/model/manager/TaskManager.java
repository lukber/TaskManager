package cz.czechGeeks.taskManager.client.android.model.manager;

import cz.czechGeeks.taskManager.client.android.model.TaskModel;

/**
 * Service pro nacitani a upravu dat tasku
 * 
 * @author lukasb
 * 
 */
public interface TaskManager {

	/**
	 * Vrati vsechny ukoly ktere jsou mnou zalozene a ja je take mam splnit
	 */
	void getAllMain(AsyncTaskCallBack<TaskModel[]> callBack);

	/**
	 * Vrati vsechny ukoly ktere nekdo delegoval na me
	 */
	void getAllDelegatedToMe(AsyncTaskCallBack<TaskModel[]> callBack);

	/**
	 * Vrati vsechny ukoly ktere jsem nekomu delegoval
	 */
	void getAllDelegatedToOthers(AsyncTaskCallBack<TaskModel[]> callBack);

	/**
	 * Vrati ukol dle id
	 * 
	 * @param id
	 */
	void get(Long id, AsyncTaskCallBack<TaskModel> callBack);

	/**
	 * Odstraneni ukolu
	 * 
	 * @param id
	 */
	void delete(Long id, AsyncTaskWithResultCodeCallBack callBack);

	/**
	 * Aktualizace ukolu
	 * 
	 * @param task
	 */
	void update(TaskModel task, AsyncTaskCallBack<TaskModel> callBack);

	/**
	 * Oznaceni ukolu jako precteneho
	 * 
	 * @param id
	 * @param callBack
	 */
	void markAsReaded(Long id, AsyncTaskWithResultCodeCallBack callBack);

}
