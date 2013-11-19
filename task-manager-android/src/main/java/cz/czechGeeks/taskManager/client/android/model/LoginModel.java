package cz.czechGeeks.taskManager.client.android.model;

import java.io.Serializable;

/**
 * Reprezentace uzivatele
 * 
 * @author lukasb
 * 
 */
public class LoginModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;// nazev uzivatele

	public LoginModel() {
	}

	public LoginModel(Long id, String name) {
		this.id = id;
		this.name = name;
	}

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
}
