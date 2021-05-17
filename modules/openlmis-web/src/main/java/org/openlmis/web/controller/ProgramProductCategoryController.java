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

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.FacilityTypeApprovedProduct;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.FacilityApprovedProductService;
import org.openlmis.core.service.ProgramProductService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.web.form.FacilityTypeApprovedProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.openlmis.core.web.OpenLmisResponse.response;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * This controller handles endpoint related to get non full supply products for a given facility, program combination,
 * GET, POST, PUT, DELETE of facilityTypeApprovedProduct.
 */

@Controller
@NoArgsConstructor
@RequestMapping(value = "/program-product-categories")
public class ProgramProductCategoryController extends BaseController {


    public static final String PROGRAM_PRODUCTS = "programProducts";
    public static final String PRODUCTS = "products";
    public static final String PROGRAM_PRODUCT = "programProduct";
    public static final String PAGINATION = "pagination";


    @Autowired
    private ProgramProductService service;

    @RequestMapping(value = "/category/{categoryId}/program/{programId}", method = GET,
            headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_PRODUCT')")
    public ResponseEntity<OpenLmisResponse> getAllProductsByCategoryAndProgram(@PathVariable("categoryId") Long categoryId,
                                                                               @PathVariable("programId") Long programId) {
        return response(PROGRAM_PRODUCTS, service.getByProgramAndCategory(programId, categoryId));
    }

    @RequestMapping(value = "/filter/category/{categoryId}/program/{programId}", method = GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_PRODUCT')")
    public ResponseEntity<OpenLmisResponse> getUnapprovedProgramProducts(@PathVariable(value = "categoryId") Long categoryId,
                                                      @PathVariable(value = "programId") Long programId) {
        return response(PRODUCTS, service.getUnCategorizedProducts(categoryId, programId));
    }

    @RequestMapping(method = GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_PRODUCT')")
    public ResponseEntity<OpenLmisResponse> getAllBy(@RequestParam("categoryId") Long facilityTypeId,
                                                     @RequestParam("programId") Long programId,
                                                     @RequestParam(value = "searchParam", defaultValue = "", required = false) String searchParam,
                                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                     @Value("${search.page.size}") String limit) {
        Pagination pagination = new Pagination(page, parseInt(limit));
        pagination.setTotalRecords(service.getTotalSearchResultCount(searchParam, "Program"));
        List<ProgramProduct> programProducts = service.search(searchParam, pagination, "Program");
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response(PROGRAM_PRODUCTS, programProducts);
        response.getBody().addData(PAGINATION, pagination);
        return response;
    }

    @RequestMapping(method = POST, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_PRODUCT')")
    public ResponseEntity<OpenLmisResponse> insert(@RequestBody List<ProgramProduct> programProducts,
                                                   HttpServletRequest request) {
        try {
            service.saveAllProgramProducts(programProducts);
        } catch (DataException e) {
            return OpenLmisResponse.error(e, BAD_REQUEST);
        }
        return OpenLmisResponse.success(messageService.message("message.facility.type.approved.products.added.successfully", programProducts.size()));
    }

    @RequestMapping(value = "/{id}", method = PUT, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_PRODUCT')")
    public ResponseEntity<OpenLmisResponse> update(@PathVariable("id") Long id,
                                                   @RequestBody ProgramProduct programProduct,
                                                   HttpServletRequest request) {
        programProduct.setId(id);
        programProduct.setModifiedBy(loggedInUserId(request));
        try {
            service.save(programProduct);
        } catch (DataException e) {
            return OpenLmisResponse.error(e, BAD_REQUEST);
        }

        OpenLmisResponse openLmisResponse = new OpenLmisResponse(PROGRAM_PRODUCT, programProduct);
        String successMessage = messageService.message("message.facility.approved.product.updated.success", programProduct.getProduct().getPrimaryName());
        return openLmisResponse.successEntity(successMessage);
    }


    @RequestMapping(value = "/{id}", method = POST, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_PRODUCT')")
    public ResponseEntity<OpenLmisResponse> delete(@PathVariable("id") Long id,
                                                   @RequestBody ProgramProduct programProduct,
                                                   HttpServletRequest request) {
        programProduct.setId(id);
        programProduct.setModifiedBy(loggedInUserId(request));
        try {
            service.deleteProgramProductId(programProduct);
        } catch (DataException e) {
            return OpenLmisResponse.error(e, BAD_REQUEST);
        }

        OpenLmisResponse openLmisResponse = new OpenLmisResponse(PROGRAM_PRODUCT, programProduct);
        String successMessage = messageService.message("message.facility.approved.product.updated.success", programProduct.getProduct().getPrimaryName());
        return openLmisResponse.successEntity(successMessage);
    }

}
