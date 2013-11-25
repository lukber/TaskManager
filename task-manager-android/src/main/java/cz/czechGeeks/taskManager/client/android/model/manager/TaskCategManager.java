package cz.czechGeeks.taskManager.client.android.model.manager;

import cz.czechGeeks.taskManager.client.android.model.TaskCategModel;

/**
 * Service pro nacitani kategorii
 * 
 * @author lukasb
 * 
 */
public interface TaskCategManager {

	/**
	 * Vrati vsechny dostupne ktegorie
	 */
	void getAll(AsyncTaskCallBack<TaskCategModel[]> callBack);

	/**
	 * Vrati kategorii dle ID
	 * 
	 * @param id
	 * @param callBack
	 */
	void get(Long id, AsyncTaskCallBack<TaskCategModel> callBack);

	/**
	 * Zalozi novou kategorii
	 * 
	 * @param categ
	 * @param callBack
	 */
	void insert(TaskCategModel categ, AsyncTaskCallBack<TaskCategModel> callBack);

	/**
	 * Aktualizuje stavajici kategorii
	 * 
	 * @param categ
	 * @param callBack
	 */
	void update(TaskCategModel categ, AsyncTaskCallBack<TaskCategModel> callBack);

	/**
	 * Odstraneni kategorie
	 * 
	 * @param categ
	 * @param callBack
	 */
	void delete(TaskCategModel categ, AsyncTaskCallBack<TaskCategModel> callBack);

}
