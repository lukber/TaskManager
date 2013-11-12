package cz.czechGeeks.taskManager.client.android.to;

import java.sql.Timestamp;

public class TaskTO {

	private Long id;
	private Long categId;

	private String name;
	private String desc;

	private Timestamp finishToDate;
	private Timestamp finishedDate;

	private Long executorId;
	private Long inserterId;

	private Timestamp insDate;
	private Timestamp updDate;

	private boolean updatable;
	private boolean deletable;
	private boolean closeable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategId() {
		return categId;
	}

	public void setCategId(Long categId) {
		this.categId = categId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Timestamp getFinishToDate() {
		return finishToDate;
	}

	public void setFinishToDate(Timestamp finishToDate) {
		this.finishToDate = finishToDate;
	}

	public Timestamp getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Timestamp finishedDate) {
		this.finishedDate = finishedDate;
	}

	public Long getExecutorId() {
		return executorId;
	}

	public void setExecutorId(Long executorId) {
		this.executorId = executorId;
	}

	public Long getInserterId() {
		return inserterId;
	}

	public void setInserterId(Long inserterId) {
		this.inserterId = inserterId;
	}

	public Timestamp getInsDate() {
		return insDate;
	}

	public void setInsDate(Timestamp insDate) {
		this.insDate = insDate;
	}

	public Timestamp getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	public boolean isDeletable() {
		return deletable;
	}

	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

}
