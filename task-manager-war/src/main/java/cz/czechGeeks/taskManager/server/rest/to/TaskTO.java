package cz.czechGeeks.taskManager.server.rest.to;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import cz.czechGeeks.taskManager.server.rest.adapter.TimestampAdapter;

@XmlRootElement(name = "TaskCateg")
public class TaskTO {

	private Long id;
	private String name;
	private String desc;

	private Timestamp finishToDate;
	private Timestamp finishedDate;

	private TaskCategTO categ;
	private LoginTO executor;
	private LoginTO inserter;

	private Timestamp insDate;
	private Timestamp updDate;

	private boolean updatable;
	private boolean deletable;

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

	@XmlAttribute
	public boolean isUpdatable() {
		return updatable;
	}

	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}

	@XmlAttribute
	public boolean isDeletable() {
		return deletable;
	}

	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public Timestamp getFinishToDate() {
		return finishToDate;
	}

	public void setFinishToDate(Timestamp finishToDate) {
		this.finishToDate = finishToDate;
	}

	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public Timestamp getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(Timestamp finishedDate) {
		this.finishedDate = finishedDate;
	}

	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public Timestamp getInsDate() {
		return insDate;
	}

	public void setInsDate(Timestamp insDate) {
		this.insDate = insDate;
	}

	@XmlJavaTypeAdapter(TimestampAdapter.class)
	public Timestamp getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public TaskCategTO getCateg() {
		return categ;
	}

	public void setCateg(TaskCategTO categ) {
		this.categ = categ;
	}

	public LoginTO getExecutor() {
		return executor;
	}

	public void setExecutor(LoginTO executor) {
		this.executor = executor;
	}

	public LoginTO getInserter() {
		return inserter;
	}

	public void setInserter(LoginTO inserter) {
		this.inserter = inserter;
	}

}
