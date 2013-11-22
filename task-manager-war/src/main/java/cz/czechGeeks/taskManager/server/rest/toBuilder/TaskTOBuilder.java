package cz.czechGeeks.taskManager.server.rest.toBuilder;

import java.util.ArrayList;
import java.util.List;

import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.Task;
import cz.czechGeeks.taskManager.server.rest.to.TaskTO;
import cz.czechGeeks.taskManager.server.service.TaskService;
import cz.czechGeeks.taskManager.server.util.ServiceLocator;

public class TaskTOBuilder {

	public static TaskTO build(Task task, Long forLoginId) throws EntityNotFoundException {
		TaskService service = ServiceLocator.INSTANCE.getService(TaskService.class);

		TaskTO to = new TaskTO();

		to.setId(task.getId());
		to.setName(task.getName());
		to.setDesc(task.getDesc());

		to.setFinishToDate(task.getFinishToDate());
		to.setFinishedDate(task.getFinishedDate());

		to.setCategId(task.getCategId());
		to.setExecutorId(task.getExecutorId());
		to.setInserterId(task.getInserterId());
		
		to.setCategName(task.getCateg().getName());
		to.setExecutorName(task.getExecutor().getLoginName());
		to.setInserterName(task.getInserter().getLoginName());

		to.setUpdatable(service.isUpdatable(task.getId(), forLoginId));
		to.setDeletable(service.isDeleteable(task.getId(), forLoginId));
		to.setCloseable(service.isCloseable(task.getId(), forLoginId));

		to.setInsDate(task.getInsDate());
		to.setUpdDate(task.getUpdDate());

		return to;
	}

	public static List<TaskTO> build(List<Task> taskList, Long forLoginId) throws EntityNotFoundException {
		List<TaskTO> toList = new ArrayList<TaskTO>();
		for (Task categ : taskList) {
			toList.add(build(categ, forLoginId));
		}
		return toList;
	}

}