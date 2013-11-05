package cz.czechGeeks.taskManager.server.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import cz.czechGeeks.taskManager.server.dao.TaskManagerDao;
import cz.czechGeeks.taskManager.server.model.TaskCateg;

@Singleton
public class TaskCategService {

	@EJB(mappedName = TaskManagerDao.JNDI)
	private TaskManagerDao dao;

	public List<TaskCateg> getAll() {
		return dao.getAll(TaskCateg.class);
	}

	public TaskCateg get(Long id) {
		return dao.find(TaskCateg.class, id);
	}

	public TaskCateg insert(String categName) {
		TaskCateg categ = new TaskCateg();
		categ.setName(categName);
		dao.persist(categ);
		return categ;
	}

}
