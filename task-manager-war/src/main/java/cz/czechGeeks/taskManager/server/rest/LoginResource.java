package cz.czechGeeks.taskManager.server.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import cz.czechGeeks.taskManager.server.model.Login;
import cz.czechGeeks.taskManager.server.rest.to.ErrorMessageTO;
import cz.czechGeeks.taskManager.server.rest.to.LoginTO;
import cz.czechGeeks.taskManager.server.rest.toBuilder.LoginTOBuilder;
import cz.czechGeeks.taskManager.server.service.LoginService;

@Stateful
@Path("/Login")
public class LoginResource {

	@EJB
	LoginService loginService;

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getLogin(@Context SecurityContext securityContext) {
		String userName = securityContext.getUserPrincipal().getName();
		try {
			Long loginId = loginService.getId(userName);
			Login login = loginService.get(loginId);
			LoginTO loginTO = LoginTOBuilder.build(login);
			return Response.ok(loginTO).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}
	
	@GET
	@Path("all")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllLogins() {
		try {
			List<Login> loginList = loginService.getAll();
			List<LoginTO> loginTOList = LoginTOBuilder.build(loginList);
			return Response.ok(loginTOList).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}

}
