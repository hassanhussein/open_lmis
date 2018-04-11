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

import com.wordnik.swagger.annotations.Api;
import org.openlmis.core.domain.User;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.equipment.domain.ColdChainEquipmentTemperatureAlarm;
import org.openlmis.equipment.dto.ColdChainEquipmentTemperatureAlarmDTO;
import org.openlmis.equipment.dto.DailyColdTraceStatusDTO;
import org.openlmis.equipment.service.ColdChainEquipmentTemperatureAlarmService;
import org.openlmis.equipment.service.DailyColdTraceStatusService;
import org.openlmis.restapi.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@Api(value = "Cold Trace", description = "APIs to report cold trace status")
public class ColdTraceStatusController extends BaseController {

  @Autowired
  private DailyColdTraceStatusService dailyColdTraceStatusService;

  @Autowired
  private ColdChainEquipmentTemperatureAlarmService alarmService;

  @RequestMapping(value = "/equipment/cold-trace/status", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> findStatusForPeriod(@RequestParam("facility") Long facilityId, @RequestParam("period") Long periodId) {
    return OpenLmisResponse.response("cold_trace_status", dailyColdTraceStatusService.findStatusForFacilityPeriod(facilityId, periodId));
  }

  @RequestMapping(value = "/rest-api/equipment/cold-trace", method = RequestMethod.POST, headers = ACCEPT_JSON)
  public ResponseEntity<OpenLmisResponse> submit(@RequestBody DailyColdTraceStatusDTO status, Principal principal) {
    status.validate();
    dailyColdTraceStatusService.saveDailyStatus(status.buildEntity(), loggedInUserId(principal));
    return OpenLmisResponse.success("Daily cold trace status submitted for " + status.getDate().toString());
  }

  @RequestMapping(value = "/rest-api/equipment/cold-trace/operational-status-options", method = RequestMethod.GET, headers = ACCEPT_JSON)
  public ResponseEntity<OpenLmisResponse> findPossibleStatuses() {
    return OpenLmisResponse.response("statuses", dailyColdTraceStatusService.findPossibleStatuses());
  }

  @RequestMapping(value = "/rest-api/equipment/cold-trace/alarms", method = RequestMethod.POST)
  public ResponseEntity<OpenLmisResponse> submitAlarms(@RequestBody ColdChainEquipmentTemperatureAlarmDTO alarm, @AuthenticationPrincipal User user) {
    alarmService.save(alarm, user.getId());
    return OpenLmisResponse.success("Your submission has been accepted");
  }

  @RequestMapping(value = "/rest-api/equipment/{serial}/cold-trace/alarms", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getAlarms(@PathVariable("serial") String serial) {
    List<ColdChainEquipmentTemperatureAlarm> alarms = alarmService.getAllAlarms(serial);
    return OpenLmisResponse.response("ALARMS", alarms);
  }

  @RequestMapping(value = "/rest-api/equipment/{serial}/{period}/cold-trace/alarms", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getAlarms(@PathVariable("serial") String serial, @PathVariable("period") Long periodId) {
    List<ColdChainEquipmentTemperatureAlarm> alarms = alarmService.getAlarmsForPeriod(serial, periodId);
    return OpenLmisResponse.response("ALARMS", alarms);
  }

  @RequestMapping(value = "/rest-api/equipment/cold-trace/regional-submission-status", method = RequestMethod.GET, headers = ACCEPT_JSON)
  public ResponseEntity<OpenLmisResponse> getLastSubmissions(@RequestParam("regionCode") String regionCode) {
    return OpenLmisResponse.response("statuses", dailyColdTraceStatusService.getLastSubmissionStatus(regionCode));
  }

  @RequestMapping(value = "/rest-api/equipment/cold-trace/equipments", method = RequestMethod.GET, headers = ACCEPT_JSON)
  public ResponseEntity<OpenLmisResponse> getEquipmentList(@RequestParam("regionCode") String regionCode) {
    return OpenLmisResponse.response("statuses", dailyColdTraceStatusService.getEquipmentList(regionCode));
  }

  @RequestMapping(value = "/rest-api/equipment/cold-trace/submissions-for-equipment", method = RequestMethod.GET, headers = ACCEPT_JSON)
  public ResponseEntity<OpenLmisResponse> getListOfSubmissions(@RequestParam("serialNumber") String serialNumber) {
    return OpenLmisResponse.response("statuses", dailyColdTraceStatusService.getStatusSubmittedFor(serialNumber));
  }


}
