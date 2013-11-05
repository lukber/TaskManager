package cz.czechGeeks.taskManager.server.dao;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.TaskManagerEntity;

@Local
public interface TaskManagerDao {

	public static final String JNDI = "java:app/task-manager-ejb/TaskManagerDaoBean";

	public EntityManager getEntityManager();

	/**
	 * Vyhleda entitu dle zadaneho primarniho klice
	 * 
	 * @param entityType
	 * @param primaryKey
	 * @return NULL - pokud entita nebyla nalezena
	 */
	public <T extends TaskManagerEntity> T find(Class<T> entityType, Object primaryKey);

	/**
	 * Vyhleda entitu dle zadaneho primarniho klice. Pokud entita neni nalezena
	 * tak {@link EntityNotFoundException}
	 * 
	 * @param entityType
	 * @param primaryKey
	 * @return
	 */
	public <T extends TaskManagerEntity> T findNonNull(Class<T> entityType, Object primaryKey);

	/**
	 * Vrati vsechny entity
	 * 
	 * @param entityType
	 * @return
	 */
	public <T extends TaskManagerEntity> List<T> getAll(Class<T> entityType);

	/**
	 * Ulozi entitu jako novy zaznam
	 * 
	 * @param entity
	 */
	public void persist(TaskManagerEntity entity);

	/**
	 * Update entity
	 * 
	 * @param entity
	 */
	public void merge(TaskManagerEntity entity);

	/**
	 * Znovunacteni hodnot entity
	 * 
	 * @param entity
	 */
	public void refresh(TaskManagerEntity entity);

}
