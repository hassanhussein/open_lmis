
/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.web.controller;


import org.openlmis.core.domain.MaterializedView;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.MaterilizedViewConfService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class MaterilizedViewConfController extends BaseController {
    public static final String VIEW = "View";
@Autowired
    MaterilizedViewConfService service;
    @RequestMapping(value = "/unpopulated-materialized-views", method = GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_FACILITY, MANAGE_SUPERVISORY_NODE, MANAGE_REQUISITION_GROUP, MANAGE_SUPPLY_LINE, MANAGE_FACILITY_APPROVED_PRODUCT, MANAGE_USER')")
    public List<MaterializedView> loadUnpopulatedViews() {
        return service.loadUnPopulatedViews();
    }

    @RequestMapping(value = "/materialized-views/{viewName}",  method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<OpenLmisResponse> getByKey(@PathVariable(value = "viewName") String viewName ,HttpServletRequest request) {
        service.refreshView(viewName);
        return OpenLmisResponse.response(VIEW, "success");
    }


}
