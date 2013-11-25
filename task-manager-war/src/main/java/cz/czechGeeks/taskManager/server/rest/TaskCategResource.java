package cz.czechGeeks.taskManager.server.rest;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.TaskCateg;
import cz.czechGeeks.taskManager.server.rest.to.ErrorMessageTO;
import cz.czechGeeks.taskManager.server.rest.to.TaskCategTO;
import cz.czechGeeks.taskManager.server.rest.toBuilder.TaskCategTOBuilder;
import cz.czechGeeks.taskManager.server.rest.util.UriHelper;
import cz.czechGeeks.taskManager.server.service.TaskCategService;

@Stateful
@Path("/TaskCateg")
public class TaskCategResource {

	@EJB
	TaskCategService categService;

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getTaskCateg(@PathParam("id") Long id) {
		try {
			TaskCateg categ = categService.get(id);
			TaskCategTO categTO = TaskCategTOBuilder.build(categ);
			return Response.ok(categTO).build();
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("all")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllTaskCategs() {
		try {
			List<TaskCateg> entityList = categService.getAll();
			List<TaskCategTO> toList = TaskCategTOBuilder.build(entityList);
			return Response.ok(toList).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addTaskCateg(String name, @Context UriInfo uriInfo) {
		if (name == null || name.isEmpty()) {
			return Response.noContent().build();
		}
		try {
			TaskCateg taskCateg = categService.insert(name);
			URI requestUri = uriInfo.getRequestUri();
			URI uriWithId = UriHelper.createUriWithId(requestUri, taskCateg.getId());
			return Response.created(uriWithId).entity(TaskCategTOBuilder.build(taskCateg)).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateTaskCateg(@PathParam("id") Long id, String name) {
		if (name == null || name.isEmpty()) {
			return Response.noContent().build();
		}
		try {
			TaskCateg taskCateg = categService.update(id, name);
			return Response.ok(TaskCategTOBuilder.build(taskCateg)).build();
		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}

	@DELETE
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteTaskCateg(@PathParam("id") Long id) {
		try {
			categService.delete(id);
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok().build();
	}
}
