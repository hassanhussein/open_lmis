
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

package org.openlmis.web.controller;


import org.openlmis.core.domain.MetabaseMenu;
import org.openlmis.core.domain.MetabasePage;
import org.openlmis.core.service.MetabaseIntegraionService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static org.openlmis.core.web.OpenLmisResponse.success;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping(value = "/metabase-integration/")
public class MetabaseIntegraionController extends BaseController {
    @Autowired
    private MetabaseIntegraionService service;

    private static String MENUS = "menus";
    private static String MENU = "menu";
    private static String PAGES = "pages";
    private static String PAGE = "page";

    @RequestMapping(value = "menus", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> save(@RequestBody MetabaseMenu metabaseMenu, HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> successResponse;
        try {
            Long userId = loggedInUserId(request);
            metabaseMenu.setModifiedBy(userId);
            metabaseMenu.setCreatedBy(userId);
            service.addMetabaseMenu(metabaseMenu);
        } catch (DuplicateKeyException exp) {
            return OpenLmisResponse.error("Duplicate Menu Name Exists in DB.", HttpStatus.BAD_REQUEST);
        }

        successResponse = success(String.format("Menu '%s' has been successfully saved", metabaseMenu.getName()));
        successResponse.getBody().addData(MENU, metabaseMenu);
        return successResponse;
    }

    @RequestMapping(value = "menus", method = PUT, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> update(@RequestBody MetabaseMenu metabaseMenu, HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> successResponse;
        try {
            Long userId = loggedInUserId(request);
            metabaseMenu.setModifiedBy(userId);
            service.updateMetabaseMenu(metabaseMenu);
        } catch (DuplicateKeyException exp) {
            return OpenLmisResponse.error("Duplicate Menu Name Exists in DB.", HttpStatus.BAD_REQUEST);
        }

        successResponse = success(String.format("Menu '%s' has been successfully updated", metabaseMenu.getName()));
        successResponse.getBody().addData(MENU, metabaseMenu);
        return successResponse;
    }

    @RequestMapping(value = "menus", method = DELETE, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> remove(@RequestBody MetabaseMenu metabaseMenu, HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> successResponse;
        try {
            Long userId = loggedInUserId(request);
            metabaseMenu.setModifiedBy(userId);
            service.removeMetabaseMenu(metabaseMenu);
        } catch (DuplicateKeyException exp) {
            return OpenLmisResponse.error("Duplicate Menu Name Exists in DB.", HttpStatus.BAD_REQUEST);
        }

        successResponse = success(String.format("Menu '%s' has been successfully updated", metabaseMenu.getName()));
        successResponse.getBody().addData(MENU, metabaseMenu);
        return successResponse;
    }

    @RequestMapping(method = GET, value = "menus")
    public ResponseEntity<OpenLmisResponse> getMenus() {
        return OpenLmisResponse.response(MENUS, service.loadMetabaseMenuList());
    }
    @RequestMapping(method = GET, value = "flat/menus")
    public ResponseEntity<OpenLmisResponse> getFlatMenus() {
        return OpenLmisResponse.response(MENUS, service.loadFlatMetabaseMenuList());
    }
    @RequestMapping(value = "pages", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> savePage(@RequestBody MetabasePage metabasePage, HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> successResponse;
        try {
            Long userId = loggedInUserId(request);
            metabasePage.setModifiedBy(userId);
            metabasePage.setCreatedBy(userId);
            service.addMetabasePage(metabasePage);
        } catch (DuplicateKeyException exp) {
            return OpenLmisResponse.error("Duplicate Page Name Exists in DB.", HttpStatus.BAD_REQUEST);
        }

        successResponse = success(String.format("Page '%s' has been successfully saved", metabasePage.getName()));
        successResponse.getBody().addData(PAGE, metabasePage);
        return successResponse;
    }

    @RequestMapping(value = "pages", method = PUT, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> updatePage(@RequestBody MetabasePage metabasePage, HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> successResponse;
        try {
            Long userId = loggedInUserId(request);
            metabasePage.setModifiedBy(userId);
            service.modifyMetabasePage(metabasePage);
        } catch (DuplicateKeyException exp) {
            return OpenLmisResponse.error("Duplicate Page Name Exists in DB.", HttpStatus.BAD_REQUEST);
        }

        successResponse = success(String.format("Page '%s' has been successfully updated", metabasePage.getName()));
        successResponse.getBody().addData(PAGE, metabasePage);
        return successResponse;
    }

    @RequestMapping(value = "pages", method = DELETE, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> removePage(@RequestBody MetabasePage metabasePage, HttpServletRequest request) {
        ResponseEntity<OpenLmisResponse> successResponse;
        try {
            Long userId = loggedInUserId(request);
            metabasePage.setModifiedBy(userId);
            service.removeMetabasePage(metabasePage);
        } catch (DuplicateKeyException exp) {
            return OpenLmisResponse.error("Duplicate Page Name Exists in DB.", HttpStatus.BAD_REQUEST);
        }

        successResponse = success(String.format("Page '%s' has been successfully Deleted", metabasePage.getName()));
        successResponse.getBody().addData(PAGE, metabasePage);
        return successResponse;
    }

    @RequestMapping(method = GET, value = "pages")
    public ResponseEntity<OpenLmisResponse> getPages() {
        return OpenLmisResponse.response(PAGES, service.loadMetabasePageList());
    }


}
