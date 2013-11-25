package cz.czechGeeks.taskManager.client.android.activity;

import cz.czechGeeks.taskManager.client.android.model.TaskModel;

/**
 * Definice callback procedur k zajisteni zpetneho volani pri upravach tasku
 * 
 * @author lukasb
 * 
 */
public interface TaskModelActionsCallBack {

	/**
	 * Metoda volana po uprave stavajiciho tasku nebo po zalozeni noveho
	 * 
	 * @param updatedTask
	 */
	void onTaskUpdated(TaskModel updatedTask);

	/**
	 * Metaoda volana po odstraneni tasku
	 * 
	 * @param deletedTask
	 */
	void onTaskDeleted(TaskModel deletedTask);

}
