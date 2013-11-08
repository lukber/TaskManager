package cz.czechGeeks.taskManager.server.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import cz.czechGeeks.taskManager.server.model.Task;
import cz.czechGeeks.taskManager.server.rest.to.ErrorMessageTO;
import cz.czechGeeks.taskManager.server.rest.to.TaskTO;
import cz.czechGeeks.taskManager.server.rest.to.TaskTOList;
import cz.czechGeeks.taskManager.server.rest.toBuilder.TaskTOBuilder;
import cz.czechGeeks.taskManager.server.service.TaskService;

@Stateless
@Path("/Task")
public class TaskResource {

	@Context
	UriInfo uriInfo;

	@EJB
	TaskService taskService;

	@GET
	@Path("{id}")
	@Produces("application/xml")
	public Response getTask(@PathParam("id") String id) {
		try {
			Long entityId = Long.valueOf(id);
			Task task = taskService.get(entityId);
			TaskTO taskTO = TaskTOBuilder.build(task);
			return Response.ok(taskTO).build();
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("all")
	@Produces("application/xml")
	public Response getTasks() {
		TaskTOList toList = new TaskTOList();
		try {
			List<Task> entityList = taskService.getAll();
			toList.setToList(TaskTOBuilder.build(entityList));
			return Response.ok(toList).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}

//	@POST
//	@Produces("application/xml")
//	public Response insertTask(@FormParam("name") String name) {
//		if (name == null || name.isEmpty()) {
//			return Response.noContent().build();
//		}
//		try {
//			Task task = taskService.insert(name);
//			URI requestUri = uriInfo.getRequestUri();
//			URI uriWithId = UriHelper.createUriWithId(requestUri, task.getId());
//			return Response.created(uriWithId).build();
//		} catch (Exception e) {
//			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
//		}
//	}
//
//	@PUT
//	@Path("{id}")
//	@Produces("application/xml")
//	public Response updateTask(@PathParam("id") Long id, @FormParam("name") String name) {
//		if (name == null || name.isEmpty()) {
//			return Response.noContent().build();
//		}
//		try {
//			taskService.update(id, name);
//		} catch (EntityNotFoundException e) {
//			return Response.status(Status.NOT_FOUND).build();
//		} catch (Exception e) {
//			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
//		}
//
//		return Response.ok().build();
//	}

	@DELETE
	@Path("{id}")
	@Produces("application/xml")
	public Response deleteTask(@PathParam("id") Long id) {
		try {
			taskService.delete(id);
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok().build();
	}

}
