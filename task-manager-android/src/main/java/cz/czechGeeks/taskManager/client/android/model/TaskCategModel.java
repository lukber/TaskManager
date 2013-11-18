package cz.czechGeeks.taskManager.client.android.model;

/**
 * Reprezentace kategorie
 * 
 * @author lukasb
 * 
 */
public class TaskCategModel {

	private Long id;
	private String name;

	// priznaky uprav
	private boolean deletable;
	private boolean updatable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeletable() {
		return deletable;
	}

	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

}
