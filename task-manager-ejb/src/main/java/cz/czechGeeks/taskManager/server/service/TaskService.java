package cz.czechGeeks.taskManager.server.service;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import cz.czechGeeks.taskManager.server.dao.TaskManagerDao;
import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.Task;

@Singleton
public class TaskService {

	@EJB(mappedName = TaskManagerDao.JNDI)
	private TaskManagerDao dao;

	public List<Task> getAll() {
		return dao.getAll(Task.class);
	}

	public Task get(Long id) throws EntityNotFoundException {
		return dao.findNonNull(Task.class, id);
	}

	public Task insert(Long categId, Long executorId, String name, String desc, Timestamp finishToDate, Timestamp finishedDate) {
		Task categ = new Task();

		categ.setCategId(categId);
		categ.setExecutorId(executorId);
		categ.setName(name);
		categ.setDesc(desc);
		categ.setFinishToDate(finishToDate);
		categ.setFinishedDate(finishedDate);

		dao.persist(categ);
		return categ;
	}

	public Task update(Long id, Long categId, Long executorId, String name, String desc, Timestamp finishToDate, Timestamp finishedDate) throws EntityNotFoundException {
		Task categ = dao.findNonNull(Task.class, id);

		categ.setCategId(categId);
		categ.setExecutorId(executorId);
		categ.setName(name);
		categ.setDesc(desc);
		categ.setFinishToDate(finishToDate);
		categ.setFinishedDate(finishedDate);

		dao.merge(categ);
		dao.refresh(categ);
		return categ;
	}

}
