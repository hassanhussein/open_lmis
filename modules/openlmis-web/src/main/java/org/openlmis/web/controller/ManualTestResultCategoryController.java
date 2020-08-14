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

import org.openlmis.core.domain.ManualTestResultCategory;
import org.openlmis.core.domain.ManualTestResultType;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.ManualTestResultCategoryService;
import org.openlmis.core.service.ManualTestResultTypeService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.openlmis.core.web.OpenLmisResponse.error;
import static org.openlmis.core.web.OpenLmisResponse.success;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping(value="/manualTestResultCategories/")
public class ManualTestResultCategoryController extends BaseController {

    public static final String MANUAL_TEST_TYPE = "manualTestResultCategory";

    @Autowired
    ManualTestResultCategoryService manualTestService;


    @RequestMapping(value="types",method= GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_MANUAL_TEST_TYPES')")
    public List<ManualTestResultCategory> getAll(){
        return manualTestService.getAll();
    }


    @RequestMapping(value="types/{id}",method = GET,headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_MANUAL_TEST_TYPES')")
    public ManualTestResultCategory getManualTestTypeById(@PathVariable(value="id") Long id){
        return manualTestService.getById(id);
    }

    @RequestMapping(value="types",method=POST, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_MANUAL_TEST_TYPES')")
    public ResponseEntity<OpenLmisResponse> save(@RequestBody ManualTestResultCategory testType, HttpServletRequest request){

        ResponseEntity<OpenLmisResponse> successResponse;

        testType.setModifiedBy(loggedInUserId(request));
        testType.setCreatedBy(loggedInUserId(request));

        manualTestService.save(testType);

        successResponse = success(String.format("Test Result category '%s' has been successfully saved", testType.getName()));
        successResponse.getBody().addData(MANUAL_TEST_TYPE, testType);
        return successResponse;
    }

    @RequestMapping(value="types/{tid}",method = DELETE,headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_MANUAL_TEST_TYPES')")
    public ResponseEntity<OpenLmisResponse> remove(@PathVariable(value="tid") Long id, HttpServletRequest request){
        ResponseEntity<OpenLmisResponse> successResponse;
        try {
            manualTestService.remove(id);
        } catch (DataIntegrityViolationException ex){
            return error("Can not delete test result category. Its already in use", HttpStatus.BAD_REQUEST);
        } catch (DataException e) {
            return error(e, HttpStatus.BAD_REQUEST);
        }
        successResponse = success(String.format("Test category has been successfully removed"));
        return successResponse;
    }


}
