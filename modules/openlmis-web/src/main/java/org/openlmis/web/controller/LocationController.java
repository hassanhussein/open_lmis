package org.openlmis.web.controller;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Location;
import org.openlmis.core.domain.LocationType;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.LocationService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static java.lang.Integer.parseInt;
import static org.openlmis.core.web.OpenLmisResponse.response;
import static org.openlmis.core.web.OpenLmisResponse.success;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@NoArgsConstructor
public class LocationController extends BaseController {

    @Autowired
    LocationService service;

    @RequestMapping(value = "/locations", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getLocations(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                         @RequestParam(value = "column") String column,
                                                         @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                         @Value("${search.page.size}") String limit) {
        Pagination pagination = new Pagination(page, parseInt(limit));
        pagination.setTotalRecords(service.getTotalSearchResultCount(searchParam, column));
        List<Location> locations = service.searchBy(searchParam, column, pagination);
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("locations", locations);
        response.getBody().addData("pagination", pagination);
        return response;
    }
    @RequestMapping(value = "/location-lookup", method = GET, headers = ACCEPT_JSON)
   // @PreAuthorize("@permissionEvaluator.hasPermission(principal, 'MANAGE_LOCATION')")
    public ResponseEntity<OpenLmisResponse> getLocationLookup() {
        List<Location> locations = service.getAllLocations();
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("locationList", locations);
        return response;
    }
    @RequestMapping(value = "/locations/{id}", method = GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal, 'MANAGE_LOCATION')")
    public ResponseEntity<OpenLmisResponse> getLocation(@PathVariable(value = "id") Long id) {
        return response("location", service.getById(id));
    }
    @RequestMapping(value = "/locations/{id}", method =PUT, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_LOCATION')")
    public ResponseEntity update(@RequestBody Location location, @PathVariable(value = "id") Long id,  HttpServletRequest request) {
        location.setId(id);
        location.setModifiedBy(loggedInUserId(request));
        ResponseEntity<OpenLmisResponse> response;
        try {
            service.save(location);
        } catch (DataException exception) {
            return createErrorResponse(location, exception);
        }
        response = success(messageService.message("message.location.updated.success", location.getName()));
        response.getBody().addData("location", location);
        return response;
    }
    @RequestMapping(value = "/locations", method = POST, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_LOCATION')")
    public ResponseEntity insert(@RequestBody Location location, HttpServletRequest request) {
        location.setCreatedBy(loggedInUserId(request));
        location.setModifiedBy(loggedInUserId(request));
        ResponseEntity<OpenLmisResponse> response;
        try {
            service.save(location);
        } catch (DataException exception) {
            return createErrorResponse(location, exception);
        }
        response = success(messageService.message("message.location.created.success", location.getName()));
        response.getBody().addData("location", location);
        return response;
    }

    @RequestMapping(value = "/location-types", method = GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_LOCATION')")
    public List<LocationType> getLocationTypes() {
        return service.getAllLocationTypes();
    }

    @RequestMapping(value = "/locations/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteLocation(@PathVariable("id") Long id) {

        try {
            service.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            return OpenLmisResponse.error("location.data.already.in.use", HttpStatus.BAD_REQUEST);
        }

        return OpenLmisResponse.success("message.location.deleted.success");
    }
    private ResponseEntity<OpenLmisResponse> createErrorResponse(Location location, DataException exception) {
        OpenLmisResponse openLmisResponse = new OpenLmisResponse("location", location);
        return openLmisResponse.errorEntity(exception, BAD_REQUEST);
    }
}
