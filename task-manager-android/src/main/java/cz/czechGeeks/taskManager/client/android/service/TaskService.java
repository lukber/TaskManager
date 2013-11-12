package cz.czechGeeks.taskManager.client.android.service;

import org.springframework.http.HttpMethod;

import android.util.Log;
import cz.czechGeeks.taskManager.client.android.exception.UnauthorizedException;
import cz.czechGeeks.taskManager.client.android.to.TaskTO;

public class TaskService extends AbstractService {

	private static TaskService instance;

	private TaskService() {
	}

	public static TaskService get() {
		if (instance == null) {
			instance = new TaskService();
		}
		return instance;
	}

	public TaskTO get(Long taskId) {
		try {
			return (TaskTO) callRestService("/Task/{id}", HttpMethod.GET, TaskTO.class, taskId);
		} catch (UnauthorizedException e) {
			Log.e("Chyba nacitani dat", "Nastala chyba pri nacitani ukolu: " + e.getMessage());
		}
		return null;
	}

	public TaskTO[] getAll() {
		try {
			return (TaskTO[]) callRestService("/Task/all", HttpMethod.GET, TaskTO[].class);
		} catch (UnauthorizedException e) {
			Log.e("Chyba nacitani dat", "Nastala chyba pri nacitani ukolu: " + e.getMessage());
		}
		return null;
	}

}
