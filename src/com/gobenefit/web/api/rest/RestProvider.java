package com.gobenefit.web.api.rest;

import static com.gobenefit.util.MessageConstant.METHOD_NOT_SUPPORTED;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface RestProvider {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	default public Response createEntity(@Context UriInfo ui, @Context HttpHeaders hh, String data)
			throws WebApplicationException {
		throw new WebApplicationException(METHOD_NOT_SUPPORTED);
	}

	@PUT
	@Path("/{id:[0-9]}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	default public Response updateEntity(@Context UriInfo ui, @Context HttpHeaders hh, @PathParam("id") Long id,
			String data) throws WebApplicationException {
		throw new WebApplicationException(METHOD_NOT_SUPPORTED);
	}

	@DELETE
	@Path("/{id:[0-9]}")
	default public Response deleteEntity(@Context UriInfo uriInfo, @Context HttpHeaders header,
			@PathParam("id") Long id) throws WebApplicationException {
		throw new WebApplicationException(METHOD_NOT_SUPPORTED);
	}

	@GET
	@Path("/{id:[0-9]}")
	@Produces(MediaType.APPLICATION_JSON)
	default public Response getEntity(@Context UriInfo uriInfo, @Context HttpHeaders header, @PathParam("id") Long id)
			throws WebApplicationException {
		throw new WebApplicationException(METHOD_NOT_SUPPORTED);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	default public Response getEntities(@Context UriInfo uriInfo, @Context HttpHeaders header)
			throws WebApplicationException {
		throw new WebApplicationException(METHOD_NOT_SUPPORTED);
	}
}
