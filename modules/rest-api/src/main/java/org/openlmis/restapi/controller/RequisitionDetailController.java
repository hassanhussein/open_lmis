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

package org.openlmis.restapi.controller;

import org.openlmis.core.exception.DataException;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.rnr.dto.RequisitionDetailDto;
import org.openlmis.rnr.repository.RequisitionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RequisitionDetailController extends BaseController {

  @Autowired
  private RequisitionDetailRepository repository;

  @RequestMapping(value = "/rest-api/requisitions-by-facility-period", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getByFacilityAndPeriod(
      @RequestParam("facilityId") Long facilityId,
      @RequestParam("periodId") Long periodId,
      @RequestParam("page") Long page,
      @RequestParam("pageSize") Long pageSize
  ) {
    checkIfPageSizeIsValid(pageSize);
    List<RequisitionDetailDto> dtos = repository.getDetailsByFacilityPeriod(facilityId, periodId, page, pageSize);
    Long totalRecords = repository.getDetailsByFaciiltyPeriodCount(facilityId, periodId);
    return composeResponse(page, pageSize, dtos, totalRecords);
  }

  @RequestMapping(value = "/rest-api/requisitions-by-period", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getByPeriod(
      @RequestParam("periodId") Long periodId,
      @RequestParam("page") Long page,
      @RequestParam("pageSize") Long pageSize
  ) {
    checkIfPageSizeIsValid(pageSize);
    List<RequisitionDetailDto> dtos = repository.getDetailsByPeriod(periodId, page, pageSize);
    Long totalRecords = repository.getDetailByPeriodCount(periodId);
    return composeResponse(page, pageSize, dtos, totalRecords);
  }

  private ResponseEntity<OpenLmisResponse> composeResponse(Long page, Long pageSize, List<RequisitionDetailDto> dtos, Long totalRecords) {
    Map<String, Object> map = new HashMap<>();
    map.put("rows", dtos);
    map.put("page", page);
    map.put("pageSize", pageSize);
    map.put("totalRecords", totalRecords);
    return OpenLmisResponse.response("data", map);
  }

  private void checkIfPageSizeIsValid(Long pageSize) {
    if (pageSize < 1 || pageSize > 10000) {
      throw new DataException("pageSize can only be a positive integer less than or equal to 10,000");
    }
  }
}
