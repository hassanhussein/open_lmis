package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.vaccine.domain.wms.dto.InspectionDTO;
import org.openlmis.vaccine.service.warehouse.InspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static java.lang.Integer.parseInt;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/warehouse")
public class InspectionController extends BaseController {

    @Autowired
    InspectionService service;

    @RequestMapping(value = "inspection", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getInspectionByPagination(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                                   @RequestParam(value = "column") String column,
                                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @Value("${search.page.size}") String limit) {
        Pagination pagination = new Pagination(page, parseInt(limit));
        pagination.setTotalRecords(service.getTotalSearchResultCount(searchParam, column));
        List<InspectionDTO> inspectList = service.searchBy(searchParam, column, pagination);
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("inspections", inspectList);
        response.getBody().addData("pagination", pagination);
        return response;
    }

    @RequestMapping(value = "inspection/{id}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getById(@PathVariable Long id) {

        return OpenLmisResponse.response("inspection",service.getById(id) );
    }

}
