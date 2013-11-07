package cz.czechGeeks.taskManager.server.exception;

import javax.ejb.ApplicationException;

import cz.czechGeeks.taskManager.server.model.TaskManagerEntity;

@ApplicationException(rollback = true)
public class EntityNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	private final Class<? extends TaskManagerEntity> entityType;
	private final Object primaryKey;

	public <T extends TaskManagerEntity> EntityNotFoundException(Class<T> entityType, Object primaryKey) {
		this.entityType = entityType;
		this.primaryKey = primaryKey;
	}

	public Class<? extends TaskManagerEntity> getEntityType() {
		return entityType;
	}

	public Object getPrimaryKey() {
		return primaryKey;
	}

}
