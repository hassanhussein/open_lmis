package org.openlmis.web.controller;


import lombok.NoArgsConstructor;
import org.openlmis.core.domain.DataRangeFlagConfiguration;
import org.openlmis.core.domain.SupplyLine;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.repository.DataRangeFlagConfigurationRepository;
import org.openlmis.core.service.DataRangeFlagConfigurationService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
@NoArgsConstructor
public class DataRangeFlagConfigurationController extends BaseController {
    @Autowired
    private DataRangeFlagConfigurationService service;
    public final static String DATA_RANGE_CONFIGURATION="dataRangeConfiguration";
    public final static String DATA_RANGE_CONFIGURATIONS="dataRangeConfigurations";

    @RequestMapping(value = "/data_configuration", method = POST, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_SETTING')")
    public ResponseEntity<OpenLmisResponse> addDataRangeFlagConfigurationRepository(@RequestBody DataRangeFlagConfiguration flagConfiguration,
                                                        HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> response;
        Long userId = loggedInUserId(request);
        flagConfiguration.setCreatedBy(userId);
        flagConfiguration.setModifiedBy(userId);
        try {
            service.addDataRangeFlagConfigurationRepository(flagConfiguration);
        } catch (DataException de) {
            response = OpenLmisResponse.error(de, BAD_REQUEST);
            return response;
        }
        response = OpenLmisResponse.success(messageService.message("message.supply.line.created.success"));
        response.getBody().addData(DATA_RANGE_CONFIGURATION, flagConfiguration);
        return response;

    }

    @RequestMapping(value = "/data_configuration/{id}", method = PUT, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_SETTING')")
    public ResponseEntity<OpenLmisResponse>  updateDataRangeFlagConfigurationRepository(@RequestBody DataRangeFlagConfiguration flagConfiguration,
                                                           @PathVariable(value = "id") Long supervisoryNodeId,
                                                           HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> response;
        Long userId = loggedInUserId(request);
        flagConfiguration.setModifiedBy(userId);
        flagConfiguration.setId(supervisoryNodeId);
        try {
            service.updateDataRangeFlagConfigurationRepository(flagConfiguration);
        } catch (DataException de) {
            response = OpenLmisResponse.error(de, BAD_REQUEST);
            return response;
        }
        response = OpenLmisResponse.success(messageService.message("message.supply.line.updated.success"));
        response.getBody().addData(DATA_RANGE_CONFIGURATION, flagConfiguration);
        return response;
    }

    @RequestMapping(value = "/data_configuration/{id}", method = GET)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_SETTING')")
    public ResponseEntity<OpenLmisResponse> getById(@PathVariable(value = "id") Long id) {

        return OpenLmisResponse.response(DATA_RANGE_CONFIGURATION, service.getById(id));
    }

    @RequestMapping(value = "/data_configuration", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getSupplyingDepots(HttpServletRequest request){

        return OpenLmisResponse.response(DATA_RANGE_CONFIGURATIONS, service.loadAll());
    }
}

