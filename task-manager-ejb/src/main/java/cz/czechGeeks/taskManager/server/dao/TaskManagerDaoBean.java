package cz.czechGeeks.taskManager.server.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.TaskManagerEntity;

@Stateless
public class TaskManagerDaoBean implements TaskManagerDao {

	@PersistenceContext(unitName = "TaskManager")
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public <T extends TaskManagerEntity> T find(Class<T> entityType, Object primaryKey) {
		if (primaryKey == null) {
			throw new IllegalArgumentException("Primarni klic entity je povinny parametr");
		}

		return em.find(entityType, primaryKey);
	}

	@Override
	public <T extends TaskManagerEntity> T findNonNull(Class<T> entityType, Object primaryKey) {
		T entity = find(entityType, primaryKey);
		if (entity == null) {
			throw new EntityNotFoundException(entityType, primaryKey);
		}
		return entity;
	}

	@Override
	public <T extends TaskManagerEntity> List<T> getAll(Class<T> entityType) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(entityType);
		Root<T> entity = query.from(entityType);

		query.select(entity);
		query.orderBy(builder.asc(entity));

		return em.createQuery(query).getResultList();
	}

	@Override
	public void persist(TaskManagerEntity entity) {
		em.persist(entity);
		em.flush();
	}

	@Override
	public void merge(TaskManagerEntity entity) {
		em.merge(entity);
		em.flush();
	}

	@Override
	public void refresh(TaskManagerEntity entity) {
		em.refresh(entity);
	}

}
