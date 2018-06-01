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
import org.omg.CORBA.NameValuePair;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.equipment.domain.ColdChainEquipmentTemperatureAlarm;
import org.openlmis.equipment.dto.*;
import org.openlmis.equipment.service.ColdChainEquipmentTemperatureAlarmService;
import org.openlmis.equipment.service.DailyColdTraceStatusService;
import org.openlmis.restapi.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@Api(value = "Cold Trace", description = "APIs to report cold trace status")
public class ColdTraceStatusController extends BaseController {

  private static final String ALARMS = "alarms";
  private static final String EQUIPMENTS = "equipments";
  private static final String STATUSES = "statuses";
  private static final String COLD_TRACE_STATUS = "cold_trace_status";

  @Autowired
  private DailyColdTraceStatusService dailyColdTraceStatusService;

  @Autowired
  private ColdChainEquipmentTemperatureAlarmService alarmService;

  @ApiOperation(value="submit monthly status", response = MultiValueMap.class)
  @RequestMapping(value = "/rest-api/equipment/cold-trace/monthly-status", method = RequestMethod.POST, headers = ACCEPT_JSON)
  public ResponseEntity<OpenLmisResponse> submit(@RequestBody ColdTraceMonthlyStatusDTO status, Principal principal) {
    status.validate();
    dailyColdTraceStatusService.saveDailyStatus(status.buildEntity(), loggedInUserId(principal));
    return OpenLmisResponse.success("Daily cold trace status submitted for" + status.getDate().toString());
  }

  @ApiOperation(value="submit temperature alarm.", response = MultiValueMap.class)
  @RequestMapping(value = "/rest-api/equipment/cold-trace/alarms", method = RequestMethod.POST, headers = ACCEPT_JSON)
  public ResponseEntity<OpenLmisResponse> submitAlarms(@RequestBody ColdChainTemperatureAlarmDTO alarm, Principal principal) {
    alarmService.save(alarm, loggedInUserId(principal));
    return OpenLmisResponse.success("Your submission has been accepted");
  }

  @ApiOperation(value="return list of alarm submissions for a serial number.",
      response = ColdChainEquipmentTemperatureAlarm.class, responseContainer = "List")
  @RequestMapping(value = "/rest-api/equipment/{serial}/cold-trace/alarms", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getAlarms(@PathVariable("serial") String serial) {
    List<ColdChainEquipmentTemperatureAlarm> alarms = alarmService.getAllAlarms(serial);
    return OpenLmisResponse.response(ALARMS, alarms);
  }

  @ApiOperation(value="return list of alarm submissions for a serial number in period.",
      response = ColdChainEquipmentTemperatureAlarm.class, responseContainer = "List")
  @RequestMapping(value = "/rest-api/equipment/{serial}/{period}/cold-trace/alarms", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getAlarms(@PathVariable("serial") String serial, @PathVariable("period") Long periodId) {
    List<ColdChainEquipmentTemperatureAlarm> alarms = alarmService.getAlarmsForPeriod(serial, periodId);
    return OpenLmisResponse.response(ALARMS, alarms);
  }

  @ApiOperation(value="return list of cold trace summary for region",
      response = ColdTraceSummaryDTO.class, responseContainer = "List")
  @RequestMapping(value = "/rest-api/equipment/cold-trace/regional-submission-status",
      method = RequestMethod.GET, headers = ACCEPT_JSON
  )
  public ResponseEntity<OpenLmisResponse> getLastSubmissions(@RequestParam("regionCode") String regionCode) {
    return OpenLmisResponse.response(STATUSES, dailyColdTraceStatusService.getLastSubmissionStatus(regionCode));
  }

  @ApiOperation(value = "returns list of equipments in geographic zone (region, district or the whole nation)",
      notes = "<p>accepts a region code as a parameter." +
          "<br />Valid geographic zone code (region or district code) can be passed as a regionCode to return list of equipments in respective region/district.</p>" +
          "<p><b>National equipment list:</b>" +
          " to retrieve the complete national equipments list, <b>*</b> should be passed as region code.",
      response = ColdChainEquipmentDTO.class, responseContainer = "List"

  )
  @RequestMapping(value = "/rest-api/equipment/cold-trace/equipments", method = RequestMethod.GET, headers = ACCEPT_JSON)
  public ResponseEntity<OpenLmisResponse> getEquipmentList(@RequestParam("regionCode") String regionCode) {
    return OpenLmisResponse.response(EQUIPMENTS, dailyColdTraceStatusService.getEquipmentList(regionCode));
  }

  @ApiOperation(value="return list of monthly status",
      response = ColdTraceMonthlyStatusDTO.class, responseContainer = "List")
  @RequestMapping(value = "/rest-api/equipment/cold-trace/submissions-for-equipment", method = RequestMethod.GET, headers = ACCEPT_JSON)
  public ResponseEntity<OpenLmisResponse> getListOfSubmissions(@RequestParam("serial") String serialNumber) {
    return OpenLmisResponse.response(STATUSES, dailyColdTraceStatusService.getStatusSubmittedFor(serialNumber));
  }

  @ApiOperation(value="return list of monthly status",
      response = ColdTraceMonthlyStatusDTO.class, responseContainer = "List")
  @RequestMapping(value = "/equipment/cold-trace/status", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> findStatusForPeriod(@RequestParam("facility") Long facilityId, @RequestParam("period") Long periodId) {
    return OpenLmisResponse.response(COLD_TRACE_STATUS, dailyColdTraceStatusService.findStatusForFacilityPeriod(facilityId, periodId));
  }

  @ApiOperation(value="return list of monthly status",
      response = ColdTraceMonthlyStatusDTO.class, responseContainer = "List")
  @RequestMapping(value = "/equipments/cold-trace/{facility}/{program}/{period}/alarms", method = RequestMethod.GET)
  public ResponseEntity<OpenLmisResponse> getAlarmsForFacility(@PathVariable("facility") Long facilityId, @PathVariable("program") Long program, @PathVariable("period") Long periodId) {
    List<ColdTraceAlarmDTO> alarms = alarmService.getAlarmsForFacilityPeriod(facilityId, program, periodId);
    return OpenLmisResponse.response(ALARMS, alarms);
  }

}
