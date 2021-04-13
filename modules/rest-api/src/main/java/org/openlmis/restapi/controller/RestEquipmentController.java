
/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 *
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.restapi.controller;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.exception.DataException;
import org.openlmis.equipment.domain.EquipmentInventory;
import org.openlmis.restapi.converter.ModelConversionProcessor;
import org.openlmis.restapi.domain.FacilityEquipmentStatusReport;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.restapi.service.RestAgentService;
import org.openlmis.restapi.service.RestEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.response;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@NoArgsConstructor
public class RestEquipmentController extends BaseController {

    @Autowired
    private RestEquipmentService restEquipmentService;

    @RequestMapping(value = "/rest-api/equipment-inventories", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> addEquipmentInventory(@RequestBody EquipmentInventory agent, Principal principal) {
        try {
            restEquipmentService.addEquipmentInventory(agent, loggedInUserId(principal));
            return success("message.success.agent.created");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/equipment-inventoryList", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> addEquipmentInventoryList(@RequestBody List<EquipmentInventory> equipmentList, Principal principal) {
        try {
            if (equipmentList != null && !equipmentList.isEmpty()) {
                equipmentList.stream().forEach((agent) -> {
                    restEquipmentService.addEquipmentInventory(agent, loggedInUserId(principal));
                });

            }
            return success("message.success.agent.created");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/rest-api/equipment-inventory-statuses", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> submitEquipmentStatus(@RequestBody FacilityEquipmentStatusReport report, Principal principal) {


        try {
            restEquipmentService.addEquipmentStatus(report, loggedInUserId(principal));
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
        return success("message.success.agent.created");
    }

}
