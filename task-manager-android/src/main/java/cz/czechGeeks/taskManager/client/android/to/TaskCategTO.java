package cz.czechGeeks.taskManager.client.android.to;


public class TaskCategTO {

	private Long id;
	private String name;

	private Boolean deletable;
	private Boolean updatable;

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

	public Boolean getDeletable() {
		return deletable;
	}

	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
	}

	public Boolean getUpdatable() {
		return updatable;
	}

	public void setUpdatable(Boolean updatable) {
		this.updatable = updatable;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
