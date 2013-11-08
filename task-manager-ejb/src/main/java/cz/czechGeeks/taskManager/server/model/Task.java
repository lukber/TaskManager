package cz.czechGeeks.taskManager.server.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TSK", schema = "dbo")
public class Task implements TaskManagerEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "TSK_ID")
	private Long id;

	@Column(name = "TSK_CATEG_ID")
	private Long categId;

	@Column(name = "TSK_LOGIN_ID_EXECUTOR")
	private Long executorId;

	@Column(name = "TSK_LOGIN_ID_INS")
	private Long inserterId;

	@Column(name = "TSK_NAME")
	private String name;

	@Column(name = "TSK_DESK")
	private String desc;

	@Column(name = "TSK_FINISH_TO_DATE")
	private Timestamp finishToDate;

	@Column(name = "TSK_FINISHED_DATE")
	private Timestamp finishedDate;

	@Column(name = "TSK_INS_DATE")
	private Timestamp insDate;

	@Column(name = "TSK_UPD_DATE")
	private Timestamp updDate;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TSK_CATEG_ID", referencedColumnName = "TSK_CATEG_ID", insertable = false, updatable = false)
	private TaskCateg categ;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "TSK_LOGIN_ID_EXECUTOR", referencedColumnName = "LOGIN_ID", insertable = false, updatable = false)
	private Login executor;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TSK_LOGIN_ID_INS", referencedColumnName = "LOGIN_ID", insertable = false, updatable = false)
	private Login inserter;

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

	public TaskCateg getCateg() {
		return categ;
	}

	public void setCateg(TaskCateg categ) {
		this.categ = categ;
	}

	public Login getExecutor() {
		return executor;
	}

	public void setExecutor(Login executor) {
		this.executor = executor;
	}

	public Login getInserter() {
		return inserter;
	}

	public void setInserter(Login inserter) {
		this.inserter = inserter;
	}

}
