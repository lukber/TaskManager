package cz.czechGeeks.taskManager.server.exception;

import cz.czechGeeks.taskManager.server.model.TaskManagerEntity;

public class EntityNotFoundException extends RuntimeException {
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
