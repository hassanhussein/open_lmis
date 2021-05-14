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

import org.apache.log4j.Logger;
import org.openlmis.core.domain.ProductCategory;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.ProductCategoryService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.openlmis.core.web.OpenLmisResponse.error;

@RequestMapping(value = "/product-category")
@Controller
public class ProductCategoryController extends BaseController {
    public static final String PRODUCT_CATEGORY = "productCategory";
    public static final String PRODUCT_CATEGORIES = "productCategories";
    private static final Logger LOGGER = Logger.getLogger(ProductCategoryController.class);
    @Autowired
    private ProductCategoryService service;

    @RequestMapping(value = "/product-categories", method = RequestMethod.POST, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_PRODUCT')")
    public ResponseEntity<OpenLmisResponse> save(@RequestBody ProductCategory productCategory, HttpServletRequest request) {

        productCategory.setCreatedBy(loggedInUserId(request));
        productCategory.setModifiedBy(loggedInUserId(request));
        productCategory.setModifiedDate(new Date());
        productCategory.setCreatedDate(new Date());
        return saveProductCategory(productCategory, true);
    }

    private ResponseEntity<OpenLmisResponse> saveProductCategory(ProductCategory productCategory, boolean createOperation) {
        try {
            this.service.save(productCategory);
            ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.success(("'" + productCategory.getName()) + "' " + (createOperation ? "created" : "updated") + " successfully");
            response.getBody().addData(PRODUCT_CATEGORY, this.service.getById(productCategory.getId()));
            return response;
        } catch (DuplicateKeyException exp) {
            LOGGER.warn("DuplicateKeyException exp", exp);

            return error("Duplicate Code Exists in DB.", HttpStatus.BAD_REQUEST);
        } catch (DataException e) {
            LOGGER.warn("DataException exp", e);
            return error(e, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.warn("Exception exp", e);
            return error("Duplicate Code Exists ", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/product-categories", method = RequestMethod.GET, headers = ACCEPT_JSON)
    @PreAuthorize("@permissionEvaluator.hasPermission(principal,'MANAGE_PRODUCT')")
    public ResponseEntity<OpenLmisResponse> getAll(HttpServletRequest request) {


        return OpenLmisResponse.response(PRODUCT_CATEGORIES, this.service.getAll());
    }
}
