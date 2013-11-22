package cz.czechGeeks.taskManager.server.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TSK", schema = "dbo")
public class Task implements TaskManagerEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TSK_ID")
	private Long id;

	@Column(name = "TSK_CATEG_ID")
	private Long categId;// kategorie tasku

	@Column(name = "TSK_LOGIN_ID_EXECUTOR")
	private Long executorId;// ID uzivatele ktery ma task udelat

	@Column(name = "TSK_LOGIN_ID_INS")
	private Long inserterId;// ID uzivatele ktery task zalozil

	@Column(name = "TSK_NAME")
	private String name;// nazev tasku

	@Column(name = "TSK_DESK")
	private String desc;// popis tasku

	@Column(name = "TSK_FINISH_TO_DATE")
	private Timestamp finishToDate;// datum a cas do kdy ma byt dokonceno

	@Column(name = "TSK_FINISHED_DATE")
	private Timestamp finishedDate;// datum a cas dokonceni

	@Column(name = "TSK_INS_DATE")
	private Timestamp insDate;// systemova informace - datum a cas vlozeni zaznamu

	@Column(name = "TSK_UPD_DATE")
	private Timestamp updDate;// systemova informace - datum a cas upravy zaznamu

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TSK_CATEG_ID", referencedColumnName = "TSK_CATEG_ID", insertable = false, updatable = false)
	private TaskCateg categ;// entita kategorie

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "TSK_LOGIN_ID_EXECUTOR", referencedColumnName = "LOGIN_ID", insertable = false, updatable = false)
	private Login executor;// entita login ktery ma task udelat

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TSK_LOGIN_ID_INS", referencedColumnName = "LOGIN_ID", insertable = false, updatable = false)
	private Login inserter;// entita login ktery ma task udelat

	@Column(name = "TSK_UNREAD")
	private Boolean unread;// priznak jestli task byl precten executorem

	public Boolean getUnread() {
		return unread;
	}

	public void setUnread(Boolean unread) {
		this.unread = unread;
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

	/**
	 * ID uzivatele, ktery ma ukol udelat
	 * 
	 * @return
	 */
	public Long getExecutorId() {
		return executorId;
	}

	public void setExecutorId(Long executorId) {
		this.executorId = executorId;
	}

	/**
	 * ID uzivatele ktery ukol zalozil
	 * 
	 * @return
	 */
	public Long getInserterId() {
		return inserterId;
	}

	public void setInserterId(Long inserterId) {
		this.inserterId = inserterId;
	}

	/**
	 * Nazev ukolu
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Popis ukolu
	 * 
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Datum do kdy ma byt ukol dokoncen
	 * 
	 * @return
	 */
	public Timestamp getFinishToDate() {
		return finishToDate;
	}

	public void setFinishToDate(Timestamp finishToDate) {
		this.finishToDate = finishToDate;
	}

	/**
	 * Datum kdy byl ukol ukoncen
	 * 
	 * @return
	 */
	public Timestamp getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Timestamp finishedDate) {
		this.finishedDate = finishedDate;
	}

	/**
	 * Systemova informace - datum vlozeni zaznamu
	 * 
	 * @return
	 */
	public Timestamp getInsDate() {
		return insDate;
	}

	public void setInsDate(Timestamp insDate) {
		this.insDate = insDate;
	}

	/**
	 * Systemova informace - datum upravy zaznamu
	 * 
	 * @return
	 */
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
