package com.gobenefit.web.api.rest.impl;

import static com.gobenefit.util.AppConstant.ALL_AUTHETICATED_USER_ROLE;
import static com.gobenefit.util.AppConstant.APARTMENT_ID;
import static com.gobenefit.util.AppConstant.RWA_ADMIN_ROLE;
import static com.gobenefit.util.AppConstant.TOWER_ID;
import static com.gobenefit.util.AppConstant.USER_ID;
import static com.gobenefit.util.ApplicationUtils.getQueryParameterMap;
import static com.gobenefit.util.ApplicationUtils.throwWebApplicationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gobenefit.entity.impl.Apartment;
import com.gobenefit.entity.impl.Flat;
import com.gobenefit.entity.impl.Tower;
import com.gobenefit.exception.FlatException;
import com.gobenefit.service.FlatGenerator;
import com.gobenefit.service.FlatService;
import com.gobenefit.util.SerializationUtils;
import com.gobenefit.vo.PagingSearchResult;
import com.gobenefit.web.RequestScope;
import com.gobenefit.web.api.rest.RestProvider;
import com.gobenefit.web.api.rest.impl.mixin.FlatMixin;

@Path("/flats")
public class FlatRestProcessor implements RestProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlatRestProcessor.class);

	@Autowired
	FlatService flatService;

	@RolesAllowed(RWA_ADMIN_ROLE)
	@POST
	@Path("/{id:[0-9]}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createEntities(@Context UriInfo uriInfo, @Context HttpHeaders header, @PathParam("id") Long towerId,
			String data) {
		LOGGER.info("Request recieved to create the Flats.");
		Tower tower = null;
		List<Flat> newFlatList = new ArrayList<>();
		List<Flat> updateFlatList = new ArrayList<>();
		try {
			ObjectMapper mapper = SerializationUtils.getJsonObjectMapper(false, false);
			LOGGER.debug("Deserializing the JSON to flats.");
			List<Flat> flats = mapper.readValue(data, new TypeReference<List<Flat>>() {
			});
			if (towerId != null) {
				tower = new Tower();
				tower.setId(towerId);
			}
			for (Flat flat : flats) {
				if (flat.getId() == null) {
					flat.setApartment(RequestScope.getCurrentApartment());
					flat.setTower(tower);
					newFlatList.add(flat);
				} else {
					updateFlatList.add(flat);
				}
			}
			flatService.createEntities(newFlatList);
			LOGGER.debug("Flats created successfully.");
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Flat.class, FlatMixin.class);
			return Response.ok(SerializationUtils.serialize(newFlatList, mixinMap, false)).build();
		} catch (Exception e) {
			LOGGER.error("Error occured while create the club: " + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
	}

	@Override
	@PermitAll
	public Response getEntities(UriInfo uriInfo, HttpHeaders header) throws WebApplicationException {
		String response = null;

		PagingSearchResult<Flat> flatList = null;
		Long apartmentId = null;
		Map<String, Object> criteriaMap = getQueryParameterMap(uriInfo);

		if (!criteriaMap.isEmpty()) {
			apartmentId = Long.parseLong((String) criteriaMap.get(APARTMENT_ID));
		} else {
			apartmentId = RequestScope.getCurrentApartmentId();
		}

		criteriaMap.put(APARTMENT_ID, apartmentId);
		criteriaMap.put(TOWER_ID, Long.parseLong((String) criteriaMap.get(TOWER_ID)));
		try {
			flatList = flatService.getEntities(criteriaMap);
			LOGGER.debug(flatList.getEntityList().size() + " flat(s) found for apartment Id: " + apartmentId);
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Flat.class, FlatMixin.class);
			response = SerializationUtils.serialize(flatList, mixinMap, false);
		} catch (Exception e) {
			throw throwWebApplicationException(e);
		}
		return Response.ok(response).build();
	}

	@Override
	@RolesAllowed(RWA_ADMIN_ROLE)
	public Response deleteEntity(UriInfo uriInfo, HttpHeaders header, Long id) throws WebApplicationException {
		try {
			flatService.deleteEntityById(id);
			return Response.status(Response.Status.ACCEPTED).build();
		} catch (Exception e) {
			LOGGER.error("Error occured while deleting the flat. Error:" + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}
	}

	@RolesAllowed(ALL_AUTHETICATED_USER_ROLE)
	@GET
	@Path("/current")
	public Response getUserFlats(@Context UriInfo uriInfo, @Context HttpHeaders header) throws WebApplicationException {
		try {
			Map<String, Object> criteriaMap = new HashMap<>();
			criteriaMap.put(APARTMENT_ID, RequestScope.getCurrentApartmentId());
			criteriaMap.put(USER_ID, RequestScope.getCurrentUser().getId());
			Map<Class<?>, Class<?>> mixinMap = new HashMap<>();
			mixinMap.put(Flat.class, FlatMixin.class);
			return Response
					.ok(SerializationUtils.serialize(flatService.getUsersApartmentFlats(criteriaMap), mixinMap, false))
					.build();
		} catch (Exception e) {
			LOGGER.error("Error occured while retrieving the user flats. Error:" + e.getMessage(), e);
			throw throwWebApplicationException(e);
		}

	}

	@POST
	@Path("/upload/{id: [0-9]+}")
	public void uploadEntity(@Context HttpServletRequest request, @Context HttpServletResponse response,
			@PathParam("id") Long towerId) {
		InputStream dataStream = null;
		FileItem item = null;
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload fileUpload = new ServletFileUpload(factory);
				List<FileItem> items = fileUpload.parseRequest(request);
				if (items != null) {
					Iterator<FileItem> iter = items.iterator();
					while (iter.hasNext()) {
						item = iter.next();
						if (!item.isFormField()) {
							dataStream = item.getInputStream();
							break;
						}
					}
				}
				if (dataStream != null) {
					generateFlats(dataStream, towerId);
				}
			}
		} catch (Exception e) {
			throw throwWebApplicationException(e);
		} finally {
			if (item != null)
				item.delete();
		}
	}

	@SuppressWarnings({ "resource", "unused" })
	private Map<String, List<Flat>> generateFlats(InputStream dataStream, Long towerId)
			throws IOException, InterruptedException {

		List<FlatGenerator> flatGeneratorList = new ArrayList<>();
		List<FlatException> flatErrorList = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook(dataStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		Tower tower = new Tower();
		Apartment apartment = new Apartment();
		apartment.setId(RequestScope.getCurrentApartmentId());
		tower.setId(towerId);
		tower.setApartment(apartment);
		while (rowIterator.hasNext()) {
			flatGeneratorList.add(new FlatGenerator(rowIterator.next(), tower));
		}
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		List<Future<Flat>> resultList = executor.invokeAll(flatGeneratorList);

		for (Future<Flat> future : resultList) {
			try {
				Flat flat = future.get();
			} catch (InterruptedException | ExecutionException e) {
				if (e.getCause() instanceof FlatException) {
					flatErrorList.add((FlatException) e.getCause());
				}
			}
		}
		executor.shutdown();
		dataStream.close();
		return null;
	}

}
