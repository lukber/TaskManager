package cz.czechGeeks.taskManager.client.android.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Reprezentace tasku
 * 
 * @author lukasb
 * 
 */
public class TaskModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long categId;
	private String categName;

	private String name;
	private String desc;

	private Timestamp finishToDate;// do kdy ma byt ukol splnen
	private Timestamp finishedDate;// kdy byl ukol splnen

	private Long executorId;// ID uzivatele ktery ma ukol udelat
	private String executorName;// ID uzivatele ktery ma ukol udelat
	private Long inserterId;// kdo ukol zalozil
	private String inserterName;// kdo ukol zalozil

	private Timestamp insDate;
	private Timestamp updDate;

	// priznaky pro upravy
	private boolean updatable;
	private boolean deletable;
	private boolean closeable;
	private boolean unread;

	public TaskModel() {
	}

	public TaskModel(Long id, Long categId, String categName, String name, String desc, Timestamp finishToDate, Timestamp finishedDate, Long executorId, String executorName, Long inserterId, String inserterName, Timestamp insDate, Timestamp updDate, boolean updatable, boolean deletable, boolean closeable) {
		super();
		this.id = id;
		this.categId = categId;
		this.categName = categName;
		this.name = name;
		this.desc = desc;
		this.finishToDate = finishToDate;
		this.finishedDate = finishedDate;
		this.executorId = executorId;
		this.executorName = executorName;
		this.inserterId = inserterId;
		this.inserterName = inserterName;
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

	public String getCategName() {
		return categName;
	}

	public void setCategName(String categName) {
		this.categName = categName;
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

	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public Long getInserterId() {
		return inserterId;
	}

	public void setInserterId(Long inserterId) {
		this.inserterId = inserterId;
	}

	public String getInserterName() {
		return inserterName;
	}

	public void setInserterName(String inserterName) {
		this.inserterName = inserterName;
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

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

}
