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
	 */
	void get(Long id, AsyncTaskCallBack<TaskModel> callBack);

	/**
	 * Odstraneni ukolu
	 */
	void delete(TaskModel task, AsyncTaskCallBack<TaskModel> callBack);

	/**
	 * Aktualizace ukolu
	 */
	void update(TaskModel task, AsyncTaskCallBack<TaskModel> callBack);

	/**
	 * Zalozeni ukolu
	 */
	void insert(TaskModel task, AsyncTaskCallBack<TaskModel> callBack);

	/**
	 * Oznaceni ukolu jako precteneho
	 */
	void markAsReaded(TaskModel task, AsyncTaskCallBack<TaskModel> callBack);

	/**
	 * Uzavreni tasku
	 */
	void close(TaskModel task, AsyncTaskCallBack<TaskModel> callBack);

}
