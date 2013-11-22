package cz.czechGeeks.taskManager.server.rest;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import cz.czechGeeks.taskManager.server.exception.EntityNotFoundException;
import cz.czechGeeks.taskManager.server.model.Task;
import cz.czechGeeks.taskManager.server.rest.to.ErrorMessageTO;
import cz.czechGeeks.taskManager.server.rest.to.TaskTO;
import cz.czechGeeks.taskManager.server.rest.toBuilder.TaskTOBuilder;
import cz.czechGeeks.taskManager.server.rest.util.UriHelper;
import cz.czechGeeks.taskManager.server.service.LoginService;
import cz.czechGeeks.taskManager.server.service.TaskService;

@Stateless
@Path("/Task")
public class TaskResource {

	@EJB
	TaskService taskService;

	@EJB
	LoginService loginService;

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getTask(@PathParam("id") Long id, @Context SecurityContext securityContext) {
		String userName = securityContext.getUserPrincipal().getName();
		try {
			Long loginId = loginService.getId(userName);
			Task task = taskService.get(id, loginId);
			TaskTO taskTO = TaskTOBuilder.build(task, loginId);
			return Response.ok(taskTO).build();
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@GET
	@Path("all")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllTasks(@QueryParam("categId") Long categId, @QueryParam("mainTasks") Boolean mainTasks, @QueryParam("delegatedToMe") Boolean delegatedToMe, @QueryParam("delegatedToOthers") Boolean delegatedToOthers, @QueryParam("finishToDate") Timestamp finishToDate, @Context SecurityContext securityContext) {
		String userName = securityContext.getUserPrincipal().getName();
		try {
			Long loginId = loginService.getId(userName);
			List<Task> entityList = taskService.getAll(loginId, categId, mainTasks, delegatedToMe, delegatedToOthers, finishToDate);
			List<TaskTO> toList = TaskTOBuilder.build(entityList, loginId);
			return Response.ok(toList).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addTask(TaskTO taskTO, @Context SecurityContext securityContext, @Context UriInfo uriInfo) {
		Long categId = taskTO.getCategId();
		String name = taskTO.getName();
		String desc = taskTO.getDesc();
		Timestamp finishToDate = taskTO.getFinishToDate();

		if (categId == null || name == null) {
			return Response.noContent().build();
		}

		String userName = securityContext.getUserPrincipal().getName();
		try {
			Long inserterId = loginService.getId(userName);
			Long executorId = (taskTO.getExecutorId() != null) ? taskTO.getExecutorId() : inserterId;
			Task task = taskService.insert(categId, executorId, inserterId, name, desc, finishToDate);
			URI requestUri = uriInfo.getRequestUri();
			URI uriWithId = UriHelper.createUriWithId(requestUri, task.getId());
			return Response.created(uriWithId).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateTask(@PathParam("id") Long id, TaskTO taskTO, @Context SecurityContext securityContext, @Context UriInfo uriInfo) {
		Long categId = taskTO.getCategId();
		String name = taskTO.getName();
		String desc = taskTO.getDesc();
		Timestamp finishToDate = taskTO.getFinishToDate();

		if (id == null || categId == null || name == null) {
			return Response.noContent().build();
		}

		String userName = securityContext.getUserPrincipal().getName();
		try {
			Long loginId = loginService.getId(userName);
			Long executorId = (taskTO.getExecutorId() != null) ? taskTO.getExecutorId() : loginId;
			taskService.update(id, loginId, categId, executorId, name, desc, finishToDate);
		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
		return Response.ok().build();
	}

	@PUT
	@Path("/close/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response closeTask(@PathParam("id") Long id, @Context SecurityContext securityContext, @Context UriInfo uriInfo) {
		String userName = securityContext.getUserPrincipal().getName();
		try {
			Long loginId = loginService.getId(userName);
			taskService.close(id, loginId);
		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
		return Response.ok().build();
	}

	@PUT
	@Path("/markReaded/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response markReadedTask(@PathParam("id") Long id, @Context SecurityContext securityContext, @Context UriInfo uriInfo) {
		String userName = securityContext.getUserPrincipal().getName();
		try {
			Long loginId = loginService.getId(userName);
			taskService.markReaded(id, loginId);
		} catch (EntityNotFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.serverError().entity(new ErrorMessageTO(e.getCause().getLocalizedMessage())).build();
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("{id}")
	public Response deleteTask(@PathParam("id") Long id, @Context SecurityContext securityContext) {
		String userName = securityContext.getUserPrincipal().getName();
		try {
			Long loginId = loginService.getId(userName);
			taskService.delete(id, loginId);
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok().build();
	}

}
