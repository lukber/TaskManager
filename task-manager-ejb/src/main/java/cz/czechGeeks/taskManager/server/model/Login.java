package cz.czechGeeks.taskManager.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOGIN", schema = "dbo")
public class Login implements TaskManagerEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LOGIN_ID")
	private Long id;

	@Column(name = "LOGIN_NAME")
	private String loginName;

	@Column(name = "LOGIN_USR_NAME")
	private String userName;

	@Column(name = "LOGIN_USR_PASS")
	private String userPassword;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
