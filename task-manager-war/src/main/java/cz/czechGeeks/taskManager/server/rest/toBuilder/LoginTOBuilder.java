package cz.czechGeeks.taskManager.server.rest.toBuilder;

import java.util.ArrayList;
import java.util.List;

import cz.czechGeeks.taskManager.server.model.Login;
import cz.czechGeeks.taskManager.server.rest.to.LoginTO;

public class LoginTOBuilder {

	public static LoginTO build(Login login) {
		LoginTO to = new LoginTO();

		to.setId(login.getId());
		to.setName(login.getLoginName());
		
		return to;
	}

	public static List<LoginTO> build(List<Login> loginList) {
		List<LoginTO> toList = new ArrayList<LoginTO>();
		for (Login login : loginList) {
			toList.add(build(login));
		}
		return toList;
	}

}