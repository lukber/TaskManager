package cz.czechGeeks.taskManager.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Login uzivatele
 * 
 * @author lukasb
 * 
 */
@Entity
@Table(name = "LOGIN", schema = "dbo")
public class Login implements TaskManagerEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LOGIN_ID")
	private Long id;

	@Column(name = "LOGIN_NAME")
	private String loginName;// nazev uzivatele - pouze pro zobrazeni v GUI

	@Column(name = "LOGIN_USR_NAME")
	private String userName;// prihlasovaci jmeno uzivatele

	@Column(name = "LOGIN_USR_PASS")
	private String userPassword;// heslo

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Nazev uzivatele - <B>nejedna se o prihlasovaci jmeno</B>
	 * 
	 * @return nazev uzivatele
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * Nastaveni nazvu uzivatele - <B>nejedna se o prihlasovaci jmeno</B>
	 * 
	 * @param loginName
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * Nazev uzivatele pro prihlaseni
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Nastaveni nazvu uzivatele pro prihlaseni
	 * 
	 * @param userName
	 *            prihlasovaci jmeno
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Heslo uzivatelskeho uctu
	 * 
	 * @return
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * Heslo uzivatelskeho uctu
	 * 
	 * @param userPassword
	 *            heslo
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
