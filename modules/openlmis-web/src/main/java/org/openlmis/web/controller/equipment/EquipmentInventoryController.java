/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.web.controller.equipment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.ProgramService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.equipment.domain.EquipmentInventory;
import org.openlmis.equipment.service.EquipmentInventoryNotificationService;
import org.openlmis.equipment.service.EquipmentInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.openlmis.core.domain.RightName.MANAGE_EQUIPMENT_INVENTORY;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
@RequestMapping(value = "/equipment/inventory/")
@Api("IVD Equipment Rest APIs ")
public class EquipmentInventoryController extends BaseController {

  public static final String PROGRAMS = "programs";
  public static final String INVENTORY = "inventory";
  public static final String FACILITIES = "facilities";
  public static final String PAGINATION = "pagination";

  @Autowired
  private EquipmentInventoryService service;

  @Autowired
  private FacilityService facilityService;

  @Autowired
  private ProgramService programService;

  @Autowired
  private EquipmentInventoryNotificationService notificationService;


  @RequestMapping(value = {"list", "/rest-api/EquipmentInventory/{typeId}/{programId}/{equipmentTypeId}"}, method = RequestMethod.GET)
  @ApiOperation(position = 2, value = "Get Equipments By type, Program and EquipmentType")
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_EQUIPMENT_INVENTORY')")
  public ResponseEntity<OpenLmisResponse> getInventory(@RequestParam("typeId") Long typeId,
                                                       @RequestParam("programId") Long programId,
                                                       @RequestParam("equipmentTypeId") Long equipmentTypeId,
                                                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                       @Value("${search.page.size}") String limit,
                                                       HttpServletRequest request) {
    Long userId = loggedInUserId(request);
    Pagination pagination = new Pagination(page, parseInt(limit));
    pagination.setTotalRecords(service.getInventoryCount(userId, typeId, programId, equipmentTypeId));
    List<EquipmentInventory> inventory = service.getInventory(userId, typeId, programId, equipmentTypeId, pagination);
    ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response(INVENTORY, inventory);
    response.getBody().addData(PAGINATION, pagination);
    return response;
  }

  @RequestMapping(value = "/search", method = GET, headers = ACCEPT_JSON)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_EQUIPMENT_INVENTORY')")
  public ResponseEntity<OpenLmisResponse> getFilteredFacilities(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                                @RequestParam(value = "typeId", required = false) Long typeId,
                                                                @RequestParam(value = "programId", required = false) Long programId,
                                                                @RequestParam(value = "equipmentTypeId", required = false) Long equipmentTypeId,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @Value("${search.results.limit}") String facilitySearchLimit, HttpServletRequest request) {
    Long userId = loggedInUserId(request);
    Pagination pagination = new Pagination(page, parseInt(facilitySearchLimit));
    Integer count = service.getInventoryCountBySearch(searchParam, userId, typeId, programId, equipmentTypeId);
    pagination.setTotalRecords(count);
    if (count <= Integer.parseInt(facilitySearchLimit)) {

      List<EquipmentInventory> inventory = service.searchInventory(searchParam, userId, typeId, programId, equipmentTypeId, pagination);
      ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("inventories", inventory);
      response.getBody().addData(PAGINATION, pagination);

      return response;
    } else {
      return OpenLmisResponse.response("message", "too.many.results.found");
    }
  }

  @RequestMapping(value = PROGRAMS, method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getPrograms(HttpServletRequest request) {
    Long userId = loggedInUserId(request);
    return OpenLmisResponse.response(PROGRAMS, programService.getProgramForSupervisedFacilities(userId, MANAGE_EQUIPMENT_INVENTORY));
  }

  @RequestMapping(value = "facility/programs", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getProgramsForFacility(@RequestParam("facilityId") Long facilityId, HttpServletRequest request) {
    Long userId = loggedInUserId(request);
    return OpenLmisResponse.response(PROGRAMS, programService.getProgramsForUserByFacilityAndRights(facilityId, userId, MANAGE_EQUIPMENT_INVENTORY));
  }

  @RequestMapping(value = "supervised/facilities", method = RequestMethod.GET)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_EQUIPMENT_INVENTORY')")
  public ResponseEntity<ModelMap> getFacilities(@RequestParam("programId") Long programId, HttpServletRequest request) {
    ModelMap modelMap = new ModelMap();
    Long userId = loggedInUserId(request);
    List<Facility> facilities = facilityService.getUserSupervisedFacilities(userId, programId, MANAGE_EQUIPMENT_INVENTORY);
    modelMap.put(FACILITIES, facilities);
    return new ResponseEntity<>(modelMap, HttpStatus.OK);
  }

  @RequestMapping(value = "by-id", method = RequestMethod.GET)
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_EQUIPMENT_INVENTORY')")
  public ResponseEntity<OpenLmisResponse> getInventory(@RequestParam("id") Long id) {
    return OpenLmisResponse.response(INVENTORY, service.getInventoryById(id));
  }

  @RequestMapping(value = {"save", "/rest-api/EquipmentInventory/save"}, method = {RequestMethod.PUT, RequestMethod.POST})
  @ApiOperation(position = 3, value = "Save Equipment Inventory Information")
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_EQUIPMENT_INVENTORY')")
  public ResponseEntity<OpenLmisResponse> save(@RequestBody EquipmentInventory inventory, HttpServletRequest request) {
    ResponseEntity<OpenLmisResponse> response;
    Long userId = loggedInUserId(request);
    inventory.setCreatedBy(userId);
    inventory.setModifiedBy(userId);
    try {
      service.save(inventory);
    } catch (DuplicateKeyException exception) {
      return OpenLmisResponse.error("Validated serial numbers have to be Unique.", HttpStatus.BAD_REQUEST);
    }
    service.updateNonFunctionalEquipments();
    response = OpenLmisResponse.success(messageService.message("message.equipment.inventory.saved"));
    response.getBody().addData(INVENTORY, inventory);
    return response;
  }

  @RequestMapping(value = {"status/update", "/rest-api/EquipmentInventory/update"}, method = {RequestMethod.POST, RequestMethod.PUT})
  @ApiOperation(position = 4, value = "Update Equipment Status")
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_EQUIPMENT_INVENTORY')")
  public ResponseEntity<OpenLmisResponse> updateStatus(@RequestBody EquipmentInventory inventory, HttpServletRequest request) {
    ResponseEntity<OpenLmisResponse> response;
    Long userId = loggedInUserId(request);
    inventory.setModifiedBy(userId);
    service.updateStatus(inventory);
    service.updateNonFunctionalEquipments();
    response = OpenLmisResponse.success(messageService.message("message.equipment.inventory.saved"));
    response.getBody().addData(INVENTORY, inventory);
    return response;
  }

  @RequestMapping(value = "/sendEmails", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> sendEmails() {
    notificationService.sendEmailNotificationsForNonFunctional();
    return OpenLmisResponse.response("Emails", "Sent");
  }

  @RequestMapping(value = "/delete", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> delete(@RequestParam("inventoryId") Long inventoryId) {
    if (null != inventoryId)
      service.deleteEquipmentInventory(inventoryId);
    return OpenLmisResponse.response("Equipment", "deleted");
  }


  @RequestMapping(value = "/rest-api/EquipmentInventory/{facilityId}/{programId}", method = RequestMethod.GET)
  @ApiOperation(position = 5, value = "GET Equipment Inventory by Facility and Program")
  @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_EQUIPMENT_INVENTORY')")
  public ResponseEntity<OpenLmisResponse> getInventoryByFacilityAndProgram(@RequestParam("facilityId") Long facilityId,
                                                                           @RequestParam("programId") Long programId) {

    return OpenLmisResponse.response("Equipments", service.getInventoryForFacility(facilityId, programId));
  }

  @RequestMapping(value = "toggleVerified/{id}", method = PUT, headers = ACCEPT_JSON)
  @Transactional
  public ResponseEntity<OpenLmisResponse> toggleVerified(@PathVariable("id") Long inventoryId) {
    EquipmentInventory inventory = service.getInventoryById(inventoryId);
    inventory.setIsSerialNumberVerified(!inventory.getIsSerialNumberVerified());
    service.save(inventory);
    return OpenLmisResponse.success(String.format("Serial is verified flag toggled for %s", inventory.getId()));
  }


  @RequestMapping(value = "move/{id}/{facilityId}", method = PUT, headers = ACCEPT_JSON)
  @Transactional
  public ResponseEntity<OpenLmisResponse> moveToOtherFacility(@PathVariable("id") Long inventoryId,
                                                              @PathVariable("facilityId") Long facilityId) {
    EquipmentInventory inventory = service.getInventoryById(inventoryId);
    inventory.setFacilityId(facilityId);
    service.save(inventory);
    return OpenLmisResponse.success("Equipment moved to new facility.");
  }

}
