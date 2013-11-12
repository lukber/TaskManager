package cz.czechGeeks.taskManager.server.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.czechGeeks.taskManager.server.dao.TaskManagerDao;
import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.Task;

@Singleton
public class TaskService {

	public class Filter {
		public Long loginId;
		public Long categId;
		public Timestamp finishToDate;
	}

	@EJB(mappedName = TaskManagerDao.JNDI)
	private TaskManagerDao dao;

	public List<Task> getAll(Long loginId, Long categId, Timestamp finishToDate) {
		EntityManager entityManager = dao.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Task> query = builder.createQuery(Task.class);
		Root<Task> root = query.from(Task.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (loginId != null) {
			predicates.add(builder.or(builder.equal(root.get("executorId"), loginId), builder.equal(root.get("inserterId"), loginId)));
		}
		if (categId != null) {
			predicates.add(builder.equal(root.get("categId"), categId));
		}
		if (finishToDate != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.<Timestamp> get("finishToDate"), finishToDate));
		}

		if (predicates.size() == 0) {
			throw new IllegalArgumentException("Je potreba zadat alespon jeden parametr");
		}

		query.select(root);
		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

	public Task get(Long id) throws EntityNotFoundException {
		return dao.findNonNull(Task.class, id);
	}

	public Task insert(Long categId, Long executorId, Long inserterId, String name, String desc, Timestamp finishToDate, Timestamp finishedDate) {
		Task categ = new Task();

		categ.setCategId(categId);
		categ.setExecutorId(executorId);
		categ.setInserterId(inserterId);
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

	public boolean isUpdatable(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDeleteable(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

}
