package cz.czechGeeks.taskManager.client.android.model;

import java.sql.Timestamp;

/**
 * Reprezentace tasku
 * 
 * @author lukasb
 * 
 */
public class TaskModel {

	private Long id;
	private Long categId;

	private String name;
	private String desc;

	private Timestamp finishToDate;// do kdy ma byt ukol splnen
	private Timestamp finishedDate;// kdy byl ukol splnen

	private Long executorId;// ID uzivatele ktery ma ukol udelat
	private Long inserterId;// kdo ukol zalozil

	private Timestamp insDate;
	private Timestamp updDate;

	// priznaky pro upravy
	private boolean updatable;
	private boolean deletable;
	private boolean closeable;

	public TaskModel() {
	}

	public TaskModel(Long id, Long categId, String name, String desc, Timestamp finishToDate, Timestamp finishedDate, Long executorId, Long inserterId, Timestamp insDate, Timestamp updDate, boolean updatable, boolean deletable, boolean closeable) {
		this.id = id;
		this.categId = categId;
		this.name = name;
		this.desc = desc;
		this.finishToDate = finishToDate;
		this.finishedDate = finishedDate;
		this.executorId = executorId;
		this.inserterId = inserterId;
		this.insDate = insDate;
		this.updDate = updDate;
		this.updatable = updatable;
		this.deletable = deletable;
		this.closeable = closeable;
	}

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
