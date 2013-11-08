package cz.czechGeeks.taskManager.server.rest;

import java.net.URI;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.TaskCateg;
import cz.czechGeeks.taskManager.server.rest.to.ErrorMessageTO;
import cz.czechGeeks.taskManager.server.rest.to.TaskCategTO;
import cz.czechGeeks.taskManager.server.rest.to.TaskCategTOList;
import cz.czechGeeks.taskManager.server.rest.toBuilder.TaskCategTOBuilder;
import cz.czechGeeks.taskManager.server.rest.util.UriHelper;
import cz.czechGeeks.taskManager.server.service.TaskCategService;
import cz.czechGeeks.taskManager.server.util.ServiceLocator;

@Path("/TaskCateg")
public class TaskCategResource {

	@Context
	UriInfo uriInfo;

	TaskCategService categService;

	public TaskCategResource() {
		categService = ServiceLocator.INSTANCE.getService(TaskCategService.class);
	}

	@GET
	@Path("{id}")
	@Produces("application/xml")
	public Response getTaskCateg(@PathParam("id") String id) {
		try {
			Long entityId = Long.valueOf(id);
			TaskCateg categ = categService.get(entityId);
			TaskCategTO categTO = TaskCategTOBuilder.build(categ);
			return Response.ok(categTO).build();
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("all")
	@Produces("application/xml")
	public Response getTaskCategs() {
		TaskCategTOList toList = new TaskCategTOList();
		try {
			List<TaskCateg> entityList = categService.getAll();
			toList.setToList(TaskCategTOBuilder.build(entityList));
			return Response.ok(toList).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}

	@POST
	@Produces("application/xml")
	public Response insertTaskCateg(@FormParam("name") String name) {
		if (name == null || name.isEmpty()) {
			return Response.noContent().build();
		}
		try {
			TaskCateg taskCateg = categService.insert(name);
			URI requestUri = uriInfo.getRequestUri();
			URI uriWithId = UriHelper.createUriWithId(requestUri, taskCateg.getId());
			return Response.created(uriWithId).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}

	@PUT
	@Path("{id}")
	@Produces("application/xml")
	public Response updateTaskCateg(@PathParam("id") Long id, @FormParam("name") String name) {
		if (name == null || name.isEmpty()) {
			return Response.noContent().build();
		}
		try {
			categService.update(id, name);
		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}

		return Response.ok().build();
	}

	@DELETE
	@Path("{id}")
	@Produces("application/xml")
	public Response deleteTaskCateg(@PathParam("id") Long id) {
		try {
			categService.delete(id);
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok().build();
	}
}
