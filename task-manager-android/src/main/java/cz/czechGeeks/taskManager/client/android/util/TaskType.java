package cz.czechGeeks.taskManager.client.android.util;

/**
 * Typ ukolu
 * 
 * @author lukasb
 * 
 */
public enum TaskType {
	/**
	 * Ukoly me vlastni
	 */
	MAIN,

	/**
	 * Ukol je delegovan me
	 */
	DELEGATED_TO_ME,

	/**
	 * Ukol jsem delegoval na nejakeho uzivatele
	 */
	DELEGATED_TO_OTHERS
}