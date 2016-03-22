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

package org.openlmis.web.controller.vaccine;

import org.apache.log4j.Logger;
import org.openlmis.core.web.OpenLmisResponse;

import org.openlmis.vaccine.domain.reports.DropoutProduct;
import org.openlmis.vaccine.domain.reports.PerformanceByDisrictReport;

import org.openlmis.vaccine.service.reports.PerformanceByDropoutRateByDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/vaccine/report/")
public class PerformanceByDropoutRateController {
    public static final  String PERFORMANCE_BY_DROPOUT_RATE_LIST="PerformanceByDropoutRateList";
    public static final  String DROPOUT_PRODUCT_LIST="dropoutProductsList";
    private static final Logger LOGGER = Logger.getLogger(PerformanceByDropoutRateController.class);
  @Autowired
    private PerformanceByDropoutRateByDistrictService service;
    @RequestMapping(value = "performanceByDropoutRateByDistrict", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<OpenLmisResponse> getPerformanceByDropoutRateList( HttpServletRequest request){
        PerformanceByDisrictReport performanceByDropoutRateByDistrictList=null;

        try {
            performanceByDropoutRateByDistrictList=service.loadPerformanceByDropoutRateDistrictReports(request.getParameterMap());
        }catch (Exception ex){
            LOGGER.warn(" Exception is :" ,ex);
        }

       return OpenLmisResponse.response(PERFORMANCE_BY_DROPOUT_RATE_LIST, performanceByDropoutRateByDistrictList);

    }
    @RequestMapping(value = "dropoutProducts", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<OpenLmisResponse> loadDropoutProductList(){
        List<DropoutProduct> productList=service.loadDropoutProductList();
        return OpenLmisResponse.response(DROPOUT_PRODUCT_LIST, productList);

    }
}
