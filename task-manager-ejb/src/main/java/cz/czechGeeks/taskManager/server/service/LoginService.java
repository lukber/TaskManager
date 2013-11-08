package cz.czechGeeks.taskManager.server.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.czechGeeks.taskManager.server.dao.TaskManagerDao;
import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.Login;

@Singleton
public class LoginService {

	@EJB(mappedName = TaskManagerDao.JNDI)
	private TaskManagerDao dao;

	public List<Login> getAll() {
		return dao.getAll(Login.class);
	}

	public Login get(Long id) throws EntityNotFoundException {
		return dao.findNonNull(Login.class, id);
	}

	public Long getId(String userName) throws EntityNotFoundException {
		EntityManager entityManager = dao.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<Login> root = query.from(Login.class);

		query.select(root.<Long> get("id"));
		query.where(builder.equal(root.get("userName"), userName));

		return entityManager.createQuery(query).getSingleResult();
	}
}
