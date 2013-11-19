package cz.czechGeeks.taskManager.client.android.model.manager;

import java.util.List;

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
	 * 
	 * @return
	 */
	public List<TaskModel> getAllMain();

	/**
	 * Vrati vsechny ukoly ktere nekdo delegoval na me
	 * 
	 * @return
	 */
	public List<TaskModel> getAllDelegatedToMe();

	/**
	 * Vrati vsechny ukoly ktere jsem nekomu delegoval
	 * 
	 * @return
	 */
	public List<TaskModel> getAllDelegatedToOthers();

	/**
	 * Odstraneni ukolu
	 * 
	 * @param id
	 */
	public void delete(Long id);

	/**
	 * Aktualizace ukolu
	 * 
	 * @param task
	 */
	public void update(TaskModel task);

}
