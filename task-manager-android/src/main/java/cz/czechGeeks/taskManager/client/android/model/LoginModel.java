package cz.czechGeeks.taskManager.client.android.model;

/**
 * Reprezentace uzivatele
 * 
 * @author lukasb
 * 
 */
public class LoginModel {

	private Long id;
	private String name;// nazev uzivatele

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
