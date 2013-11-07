package cz.czechGeeks.taskManager.server.rest.toBuilder;

import java.util.ArrayList;
import java.util.List;

import cz.czechGeeks.taskManager.server.model.TaskCateg;
import cz.czechGeeks.taskManager.server.rest.to.TaskCategTO;
import cz.czechGeeks.taskManager.server.service.TaskCategService;
import cz.czechGeeks.taskManager.server.util.ServiceLocator;

public class TaskCategTOBuilder {

	public static TaskCategTO build(TaskCateg categ) {
		TaskCategService service = ServiceLocator.INSTANCE.getService(TaskCategService.class);
		
		TaskCategTO to = new TaskCategTO();

		to.setId(categ.getId());
		to.setName(categ.getName());
		
		to.setUpdatable(service.isUpdatable(categ.getId()));
		to.setDeletable(service.isDeleteable(categ.getId()));
		
		return to;
	}

	public static List<TaskCategTO> build(List<TaskCateg> categList) {
		List<TaskCategTO> toList = new ArrayList<TaskCategTO>();
		for (TaskCateg categ : categList) {
			toList.add(build(categ));
		}
		return toList;
	}

}