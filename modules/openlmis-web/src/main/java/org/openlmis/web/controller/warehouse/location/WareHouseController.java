package org.openlmis.web.controller.warehouse.location;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.vaccine.domain.wms.Site;
import org.openlmis.vaccine.domain.wms.WareHouse;
import org.openlmis.vaccine.domain.wms.dto.WareHouseDTO;
import org.openlmis.vaccine.service.warehouse.WareHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.openlmis.web.controller.GeographicZoneController.GEO_ZONES;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/warehouse")
public class WareHouseController extends BaseController {

    @Autowired
   private WareHouseService service;

    @RequestMapping(value = "house", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> save(@RequestBody WareHouse house, HttpServletRequest request) {
        try {
            Long userId = loggedInUserId(request);
            house.setCreatedBy(userId);
            house.setModifiedBy(userId);
            service.save(house);
            return success("message.success.site.created");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }



    @RequestMapping(value = "house/{id}", method =PUT, headers = ACCEPT_JSON)
    public ResponseEntity update(@RequestBody WareHouse house, @PathVariable(value = "id") Long id, HttpServletRequest request) {

        try{
            house.setId(id);
            house.setModifiedBy(loggedInUserId(request));
            service.save(house);
            return success("message.success.warehouse.updated");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/house", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> search(@RequestParam(value = "searchParam") String searchParam,
                                                   @RequestParam(value = "columnName") String columnName,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer page) {
        Pagination pagination = service.getPagination(page);
        pagination.setTotalRecords(service.getTotalSearchResultCount(searchParam, columnName));
        List<WareHouse> wareHouses = service.searchBy(searchParam, columnName, page);
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("houses", wareHouses);
        response.getBody().addData("pagination", pagination);
        return response;
    }

    @RequestMapping(value = "/house/{id}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> get(@PathVariable Long id) {
        return OpenLmisResponse.response("house", service.getById(id));
    }



}
