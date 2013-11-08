package cz.czechGeeks.taskManager.server.rest.toBuilder;

import java.util.ArrayList;
import java.util.List;

import cz.czechGeeks.taskManager.server.model.Task;
import cz.czechGeeks.taskManager.server.model.TaskCateg;
import cz.czechGeeks.taskManager.server.rest.to.TaskTO;
import cz.czechGeeks.taskManager.server.service.TaskService;
import cz.czechGeeks.taskManager.server.util.ServiceLocator;

public class TaskTOBuilder {

	public static TaskTO build(Task task) {
		TaskService service = ServiceLocator.INSTANCE.getService(TaskService.class);

		TaskTO to = new TaskTO();

		to.setId(task.getId());
		to.setName(task.getName());
		to.setDesc(task.getDesc());

		to.setFinishToDate(task.getFinishToDate());
		to.setFinishedDate(task.getFinishedDate());

		TaskCateg categ = task.getCateg();
		to.setCateg(TaskCategTOBuilder.build(categ));
		to.setExecutor((task.getExecutorId() != null) ? LoginTOBuilder.build(task.getExecutor()) : null);
		to.setInserter((task.getInserterId() != null) ? LoginTOBuilder.build(task.getInserter()) : null);

		to.setUpdatable(service.isUpdatable(task.getId()));
		to.setDeletable(service.isDeleteable(task.getId()));
		
		to.setInsDate(task.getInsDate());
		to.setUpdDate(task.getUpdDate());

		return to;
	}

	public static List<TaskTO> build(List<Task> taskList) {
		List<TaskTO> toList = new ArrayList<TaskTO>();
		for (Task categ : taskList) {
			toList.add(build(categ));
		}
		return toList;
	}

}