/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.restapi.controller;

import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import org.openlmis.core.dto.InterfaceResponseDTO;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.message.OpenLmisMessage;
import org.openlmis.restapi.domain.Report;
import org.openlmis.restapi.request.RequisitionSearchRequest;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.restapi.service.RestRequisitionService;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.dto.RnRFeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openlmis.restapi.response.RestResponse.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * This controller is responsible for handling API endpoint to create/approve a requisition.
 * A user of any external integrated system can submit a request to this endpoint triggering actions like create or approve.
 * The system responds with the requisition Number on success and specific error messages on failure.
 * It also acts as an end point to get requisition details.
 */

@Controller
@NoArgsConstructor
@Api(value = "Requisitions", description = "Submit Requisitions", position = 5)
public class RestRequisitionController extends BaseController {

    public static final String RNR = "requisitionId";
    public static final String RNRS = "requisitions";
    public static final String RESPONSE_CODE = "code";
    public static final String RESPONSE_DESCRIPTION = "description";


    @Autowired
    private RestRequisitionService restRequisitionService;

    @RequestMapping(value = "/rest-api/requisitions", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> submitRequisition(@RequestBody Report report, Principal principal) {
        Rnr requisition;

        try {
            requisition = restRequisitionService.submitReport(report, loggedInUserId(principal));
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
        return response(RNR, requisition.getId(), CREATED);
    }

    @RequestMapping(value = "/rest-api/sdp-requisitions", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> submitSDPRequisition(@RequestBody Report report, Principal principal) {
        Rnr requisition;
        try {
            requisition = restRequisitionService.submitSdpReport(report, loggedInUserId(principal));
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
        return response(RNR, requisition.getId(), CREATED);
    }

    @RequestMapping(value = "/rest-api/requisitions/{requisitionId}/approve", method = PUT, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> approve(@PathVariable Long requisitionId, @RequestBody Report report, Principal principal) {
        try {
            report.validateForApproval();
            restRequisitionService.approve(report, requisitionId, loggedInUserId(principal));
            return success("msg.rnr.approved.success");
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/requisitions/{id}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getReplenishment(@PathVariable Long id) {
        try {
            return response("requisition", restRequisitionService.getReplenishmentDetails(id));
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/requisitions", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getRequisitionsByFacility(@RequestParam(value = "facilityCode") String facilityCode) {
        try {
            return response("requisitions", restRequisitionService.getRequisitionsByFacility(facilityCode), OK);
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/requisitions/initiate", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> initiateRnr(@RequestParam("facilityId") Long facilityId,
                                                    @RequestParam("programId") Long programId,
                                                    @RequestParam("periodId") Long periodId,
                                                    @RequestParam("sourceApplication") String sourceApplication,
                                                    @RequestParam("emergency") Boolean emergency,
                                                    Principal principal) {
        try {
            Rnr initiatedRnr = restRequisitionService.initiateRnr(facilityId, programId, loggedInUserId(principal), emergency, periodId, sourceApplication);
            ResponseEntity<RestResponse> response = response(RNR, initiatedRnr);
            return response;
        } catch (DataException e) {
            return error(e, BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/rest-api/requisitions/search", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> searchRnr(@RequestBody RequisitionSearchRequest requisitionSearchRequest, Principal principal) {
        try {
            List<Rnr> rnrs = restRequisitionService.searchRnrs(requisitionSearchRequest, loggedInUserId(principal));
            ResponseEntity<RestResponse> response = response(RNRS, rnrs);
            return response;
        } catch (DataException e) {
            return error(e, BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/requisitions/facility_programs", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getRequisitionsByFacilityProgram(@RequestParam(value = "facilityCode") String facilityCode,
                                                                         @RequestParam(value = "programCode") String programCode) {
        try {
            return response("requisitions", restRequisitionService.getRequisitionsByFacilityAndProgram(facilityCode, programCode), OK);
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/tz-sdp-requisition", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> submitTzSDPRequisition(@RequestBody Report report, Principal principal) {
        Rnr requisition;
        RnRFeedbackDTO rnRFeedbackDTO = new RnRFeedbackDTO();
        try {
            requisition = restRequisitionService.submitFacilityReport(report, loggedInUserId(principal));
            rnRFeedbackDTO.setMessage("RnR Saved Successfully");
            rnRFeedbackDTO.setSourceOrderId(report.getSourceOrderId());
            rnRFeedbackDTO.setRequisitionId(requisition.getId());

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
        return response(RNR, rnRFeedbackDTO, CREATED);
    }


    @RequestMapping(value = "/rest-api/supervision-checklist-report", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getSupervisionCheckListReport(
            @RequestParam(value = "facilityCode") String facilityCode,
            @RequestParam(value = "programCode") String programCode
    ) {
        try {
            return response("requisitions", restRequisitionService.getSupervisionCheckListReport(facilityCode, programCode), OK);
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/rest-api/tz-sdp-requisitions", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> saveSDPReport(@RequestBody Report report, Principal principal) {
        InterfaceResponseDTO dto = new InterfaceResponseDTO();
        Map<String, OpenLmisMessage> validationErrors = restRequisitionService.saveSDPReport(report, loggedInUserId(principal));
        dto.setCode(validationErrors.get(RESPONSE_CODE).toString());
        dto.setDescription(validationErrors.get(RESPONSE_DESCRIPTION).toString());
        dto.setSourceOrderId(report.getSourceOrderId());
        dto.setRnrId(report.getRnrId());
        if(report.getRnrId() != null ) {
            dto.setDescription("Success");
        }
        restRequisitionService.updateProcessedResponseMessage(dto);
        return null;
    }

    @RequestMapping(value = "/rest-api/tz-sdp-requisitions/status", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getRequisitionStatusByRnRId(@RequestParam(value = "rnrId") Long rnrId) {
        try {
            return response("requisitionStatus", restRequisitionService.getRequisitionStatusByRnRId(rnrId), OK);
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/tz-sdp-requisitions/feed-back", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getSavedRnrStatus() {
        try {
            return response("requisitionStatus", restRequisitionService.getSavedRnrStatus(), OK);
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/tz-sdp-requisitions/feedback-status", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getResponseByStatus() {
        try {
            return response("requisitionStatus", restRequisitionService.getAllResponseByStatus(), OK);
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/tz-sdp-requisitions/update-status", method = PUT, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> getSavedRnrStatus(@RequestParam("sourceOrderId") String sourceOrderId) {
        try {
            restRequisitionService.updateBySourceId(sourceOrderId);
            return response("requisitionStatus", "updated", OK);
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/rest-api/initiate-tz-sdp-requisitions", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> InitiateRequisition(@RequestParam("agentCode") String facilityCode,
                                                            @RequestParam("programCode") String programCode,
                                                            @RequestParam("sourceApplication") String sourceApplication,
                                                            Principal principal,
                                                            @RequestParam("emergence") boolean emergence) {
        Report requisition;

        try {
            requisition = restRequisitionService.initiateSDPReport(facilityCode,programCode, loggedInUserId(principal),emergence,sourceApplication);
        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
        return response(RNR, requisition, CREATED);
    }



}
