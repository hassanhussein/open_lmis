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

package org.openlmis.lookupapi.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.json.JSONObject;
import org.openlmis.core.domain.*;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.equipment.domain.ColdChainEquipment;
import org.openlmis.equipment.domain.Equipment;
import org.openlmis.equipment.domain.EquipmentType;
import org.openlmis.lookupapi.model.FacilityMsdCodeDTO;
import org.openlmis.lookupapi.model.HealthFacilityDTO;
import org.openlmis.lookupapi.model.MSDStockDTO;
import org.openlmis.lookupapi.model.ResponseMessage;
import org.openlmis.lookupapi.service.InterfaceService;
import org.openlmis.lookupapi.service.LookupService;
import org.openlmis.report.model.dto.Facility;
import org.openlmis.report.model.dto.FacilityType;
import org.openlmis.report.model.dto.Program;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.rnr.domain.LossesAndAdjustmentsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;
import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Controller
@NoArgsConstructor
@Api(value = "Lookups", description = "Returns shared Lookup data", position = 1)
public class LookupController {

    public static final String ACCEPT_JSON = "Accept=application/json";
    public static final String UNEXPECTED_EXCEPTION = "unexpected.exception";
    public static final String FORBIDDEN_EXCEPTION = "forbidden.exception";

    public static final String PRODUCT_CATEGORIES = "product-categories";
    public static final String PRODUCTS = "products";
    public static final String PRODUCT = "product";
    public static final String DOSAGE_UNITS = "dosage-units";
    public static final String FACILITY_TYPES = "facility-types";
    public static final String FACILITIES = "facilities";
    public static final String FACILITY = "facility";
    public static final String PROGRAMS = "programs";
    public static final String PROGRAM_PRODUCTS = "program-products";
    public static final String FACILITY_APPROVED_PRODUCTS = "facility-approved-products";
    public static final String PROGRAM = "program";
    public static final String LOSSES_ADJUSTMENTS_TYPES = "losses-adjustments-types";
    public static final String PROCESSING_PERIODS = "processing-periods";
    public static final String PROCESSING_SCHEDULES = "processing-schedules";
    public static final String GEOGRAPHIC_ZONES = "geographic-zones";
    public static final String GEOGRAPHIC_LEVELS = "geographic-levels";
    public static final String REGIMENS = "regimens";
    public static final String REGIMEN_CATEGORIES = "regimen-categories";
    public static final String DOSAGE_FREQUENCIES = "dosage-frequencies";
    public static final String REGIMEN_PRODUCT_COMBINATIONS = "regimen-product-combinations";
    public static final String REGIMEN_COMBINATION_CONSTITUENTS = "regimen-combination-constituents";
    public static final String REGIMEN_CONSTITUENT_DOSAGES = "regimen-constituent-dosages";
    private static final String PROGRAM_REFERENCE_DATA ="ProgramReferenceData" ;
    private static final String RECEIVED_MESSAGE = "Facility Received Successful";

    @Autowired
    private LookupService lookupService;
    @Autowired
    private InterfaceService interfaceService;

    @ApiOperation(value = "Product Categories", notes = "Returns a list of product categories", response = ProductCategory.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = ProductCategory.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/product-categories", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getProductCategories() {
        return RestResponse.response(PRODUCT_CATEGORIES, lookupService.getAllProductCategories());
    }

    @ApiOperation(value = "Products", notes = "Returns a list of products.", response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = Product.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/products", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getProducts(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "paging", defaultValue = "true") Boolean paging
    ) {
        RowBounds rowBounds = paging ? new RowBounds(page, pageSize) : new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        return RestResponse.response(PRODUCTS, lookupService.getFullProductList(rowBounds));
    }


    @ApiOperation(value = "Product Detail by Code", notes = "Returns details of a product", response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = Product.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/product/{code}", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getProductByCode(@PathVariable("code") String code) {
        return RestResponse.response(PRODUCT, lookupService.getProductByCode(code));
    }

    @ApiOperation(value = "Dosage Units", notes = "Returns a list of Dosage Units.", response = DosageUnit.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = DosageUnit.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/dosage-units", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getDosageUnits() {
        return RestResponse.response(DOSAGE_UNITS, lookupService.getDosageUnits());
    }

    @ApiOperation(value = "Facility Types", notes = "List of Facility Types.", response = FacilityType.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = FacilityType.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/facility-types", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getFacilityTypes() {
        return RestResponse.response(FACILITY_TYPES, lookupService.getAllFacilityTypes());
    }

    @ApiOperation(value = "Facilities", notes = "Returns a list of facilities.", response = Facility.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = Facility.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/facilities", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getFacilities(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "paging", defaultValue = "true") Boolean paging) {
        RowBounds rowBounds = paging ? new RowBounds(page, pageSize) : new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        return RestResponse.response(FACILITIES, lookupService.getAllFacilities(rowBounds));
    }

    @ApiOperation(value = "Facility Detail by Code", notes = "Returns Facility Detail by Code", response = Facility.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = Facility.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/facility/{code}", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getFacilityByCode(@PathVariable("code") String code) {
        return RestResponse.response(FACILITY, lookupService.getFacilityByCode(code));
    }


    @ApiOperation(value = "Programs", notes = "Returns a list of Programs.", response = Program.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = Program.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/programs", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getPrograms() {
        return RestResponse.response(PROGRAMS, lookupService.getAllPrograms());
    }

    @ApiOperation(value = "Program Products", notes = "Returns a complete list of Products supported by Program.", response = ProgramProduct.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = ProgramProduct.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/program-products", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getProgramProducts() {
        return RestResponse.response(PROGRAM_PRODUCTS, lookupService.getAllProgramProducts());
    }

    @ApiOperation(value = "Facility Type Approved Products", notes = "Returns a complete list of Facility type supported by Program.", response = FacilityTypeApprovedProduct.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = FacilityTypeApprovedProduct.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/facility-approved-products", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getFacilityApprovedProducts() {
        return RestResponse.response(FACILITY_APPROVED_PRODUCTS, lookupService.getAllFacilityTypeApprovedProducts());
    }

    @ApiOperation(value = "Program detail By Code", notes = "Returns program detail by code", response = Program.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = Program.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/program/{code}", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getProgramByCode(@PathVariable("code") String code) {
        return RestResponse.response(PROGRAM, lookupService.getProgramByCode(code));
    }

    @ApiOperation(value = "Loss and Adjustment Types", notes = "Returns loss and adjustment types", response = LossesAndAdjustmentsType.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = LossesAndAdjustmentsType.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/losses-adjustments-types", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getLossesAdjustmentsTypes() {
        return RestResponse.response(LOSSES_ADJUSTMENTS_TYPES, lookupService.getAllAdjustmentTypes());
    }

    @ApiOperation(value = "Processing Periods", notes = "Returns all processing periods", response = org.openlmis.report.model.dto.ProcessingPeriod.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = org.openlmis.report.model.dto.ProcessingPeriod.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/processing-periods", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getProcessingPeriods() {
        return RestResponse.response(PROCESSING_PERIODS, lookupService.getAllProcessingPeriods());
    }


    @ApiOperation(value = "Processing Schedules", notes = "Returns list of processing schedule groups", response = ProcessingSchedule.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = ProcessingSchedule.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/processing-schedules", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getProcessingSchedules() {
        return RestResponse.response(PROCESSING_SCHEDULES, lookupService.getAllProcessingSchedules());
    }

    @ApiOperation(value = "Geographic Zones", notes = "Returns list of geographic zones", response = GeographicZone.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = GeographicZone.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/geographic-zones", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getGeographicZones() {
        return RestResponse.response(GEOGRAPHIC_ZONES, lookupService.getAllZones());
    }


    @ApiOperation(value = "Geographic Levels", notes = "Returns list of geographic levels", response = GeographicLevel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = GeographicLevel.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/geographic-levels", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getGeographicLevels() {
        return RestResponse.response(GEOGRAPHIC_LEVELS, lookupService.getAllGeographicLevels());
    }


    @ApiOperation(value = "Regimens", notes = "Returns list of regimens", response = Regimen.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = Regimen.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/regimens", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getRegimens() {
        return RestResponse.response(REGIMENS, lookupService.getAllRegimens());
    }


    @ApiOperation(value = "Regimen Categories", notes = "Returns list of regimen categories", response = RegimenCategory.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = RegimenCategory.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/regimen-categories", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getRegimenCategories() {
        return RestResponse.response(REGIMEN_CATEGORIES, lookupService.getAllRegimenCategories());
    }


    @ApiOperation(value = "Dosage Frequencies", notes = "Returns list of dosage frequencies", response = DosageFrequency.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = DosageFrequency.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/dosage-frequencies", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getDosageFrequencies() {
        return RestResponse.response(DOSAGE_FREQUENCIES, lookupService.getAllDosageFrequencies());
    }


    @ApiOperation(value = "Regimen Product Combinations", notes = "Returns list of regimen product combinations", response = RegimenProductCombination.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = RegimenProductCombination.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/regimen-product-combinations", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getRegimenProductCombinations() {
        return RestResponse.response(REGIMEN_PRODUCT_COMBINATIONS, lookupService.getAllRegimenProductCombinations());
    }


    @ApiOperation(value = "Regimen Combination Constituents", notes = "Returns list of regimen combination constituents", response = RegimenCombinationConstituent.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = RegimenCombinationConstituent.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/regimen-combination-constituents", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getRegimenCombinationConstituents() {
        return RestResponse.response(REGIMEN_COMBINATION_CONSTITUENTS, lookupService.getAllRegimenCombinationConstituents());
    }


    @ApiOperation(value = "Regimen Constituents' Dosages", notes = "Returns list of dosages for regimen constituents", response = RegimenConstituentDosage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = RegimenConstituentDosage.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(value = "/rest-api/lookup/regimen-constituent-dosages", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getRegimenConstituentDosages() {
        return RestResponse.response(REGIMEN_CONSTITUENT_DOSAGES, lookupService.getAllRegimenConstituentDosages());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse> handleException(Exception ex) {
        if (ex instanceof AccessDeniedException) {
            return RestResponse.error(FORBIDDEN_EXCEPTION, HttpStatus.FORBIDDEN);
        }
        return RestResponse.error(UNEXPECTED_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ApiOperation(value = "VimsFacilityList", notes = "Post Facility List from Interoperability Layer", response = HealthFacilityDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request", response = HealthFacilityDTO.class),
            @ApiResponse(code = 500, message = "Internal server error")}
    )


    @RequestMapping(value = "/rest-api/hfr-list", method = RequestMethod.POST, headers = ACCEPT_JSON)
    public ResponseEntity postTransaction(@RequestBody HealthFacilityDTO dto, HttpServletRequest request){

       try {
            interfaceService.sendResponse(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("RESPONSE BEFORE");
        return ResponseEntity.ok(OK);
    }
    @RequestMapping(value = "/rest-api/lookup/program-referece-data/{code}/{facility_code}", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getProgramReferenceData(@PathVariable("code") String code,@PathVariable("facility_code") String facilityCode) {
        return RestResponse.response(PROGRAM_REFERENCE_DATA, lookupService.getProgramReferenceData(code,facilityCode));
    }


    @RequestMapping(value = "/rest-api/lookup/hfr-facilities", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getAllFacilities() {
        return RestResponse.response("facilities", lookupService.getHFRFacilities());
    }


   @RequestMapping(value = "/rest-api/view/{view}", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getRefreshView(@PathVariable("view") String view) {
       lookupService.refreshViewsBy(view);
        return RestResponse.response("views", "refreshed");
    }


    @RequestMapping(value = "/rest-api/heath-facility-registry-list", method = RequestMethod.POST, headers = ACCEPT_JSON)
    public ResponseEntity saveHFRRecords(@RequestBody String jsonString, HttpServletRequest request){

        ObjectMapper objectMapper = new ObjectMapper();

           try {
               objectMapper.readTree(jsonString);
               objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

               if(jsonString != null && !jsonString.isEmpty() && !jsonString.equalsIgnoreCase("{}") &&  !jsonString.equalsIgnoreCase("{ }") ) {

                   HealthFacilityDTO dto = objectMapper.readValue(jsonString, HealthFacilityDTO.class);

                   interfaceService.receiveAndSendResponse(dto);

                   ResponseMessage message = new ResponseMessage();

                   message.setFacilityCode(dto.getFacIDNumber());
                   message.setFacilityName(dto.getName());
                   message.setOperatingStatus(dto.getOperatingStatus());
                   message.setMessage(RECEIVED_MESSAGE);

                   JSONObject jsonObject = new JSONObject(message);

                   return ResponseEntity.ok(jsonObject.toString());

               }else {
                   return ResponseEntity.status(NO_CONTENT).body("Empty Object");
               }

           } catch (DataException | IOException e) {

               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

           }

    }

    @RequestMapping(value = "/rest-api/activate-facility-by-msd-code", method = RequestMethod.POST, headers = ACCEPT_JSON)
    public ResponseEntity updateELMISFacility(@RequestBody FacilityMsdCodeDTO msd, HttpServletRequest request){

        try {
            interfaceService.activateByMSDFacilityCode(msd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(OK);
    }

    @RequestMapping(value = "/rest-api/msd-daily-stock-status", method = RequestMethod.POST, headers = ACCEPT_JSON)
    public ResponseEntity storeDailyStockStatusFromMSD(@RequestBody List<MSDStockDTO> msd, HttpServletRequest request){

        lookupService.insertDailyMSDStockStatus(msd);

        return ResponseEntity.ok(OK);
    }

   //C19



  /*  @RequestMapping(value = "/rest-api/sc-portal-stock-in-hand", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockInHand(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(value = "max", required = false, defaultValue = "10") int max
    ){

        Pagination pagination = new Pagination(page, max);

        Integer totalCount = lookupService.getTotalProducts();
        pagination.setTotalRecords(totalCount);

        ResponseEntity<OpenLmisResponse> response =
                OpenLmisResponse.response("data",lookupService.getStockInHand());
        response.getBody().addData("pagination", pagination);
        return response;
    }*/

 /*
 @RequestMapping(value = "/rest-api/sc-portal-stock-in-hand", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getStockInHand(HttpServletRequest request){

        try {
            lookupService.getStockInHand();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(OK);
    }*/

  /*  @RequestMapping(value = "/rest-api/sc-portal-hfr-facilities", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getHFRFacilities(HttpServletRequest request){

        try {
            lookupService.getAllHFRFacilities();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(OK);
    }
*/
/*    @RequestMapping(value = "/rest-api/sc-portal-order-delivery", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getOrderDelivery(HttpServletRequest request){

        try {

            lookupService.getOrderDelivery();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(OK);
    }*/

 /*   @RequestMapping(value = "/rest-api/sc-portal-emergency-commodities", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getEmergencystockpiles(HttpServletRequest request){

        try {
            lookupService.getEmergencyCommodites();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(OK);
    }
*/

    //THSP

    @RequestMapping(value = "/rest-api/thscp-portal-emergency-commodities", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getThScpEmergencyCommodites() {
        return RestResponse.response("data", lookupService.getThScpEmergencyCommodites());
    }

    @RequestMapping(value = "/rest-api/thscp-portal-order-delivery", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getThScpOrderDelivery() {
        return RestResponse.response("data", lookupService.getThScpOrderDelivery());
    }

    @RequestMapping(value = "/rest-api/thscp-portal-programs", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getThScpPrograms(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           @RequestParam(value = "max", required = false, defaultValue = "10") int max
                                           )
    {
        Pagination pagination = new Pagination(page,max);
        Integer totalProgram = lookupService.getTotalThScpPrograms();
        pagination.setTotalRecords(totalProgram);
        ResponseEntity<RestResponse> responseEntity =
                RestResponse.response("data", lookupService.getThScpPrograms(pagination));
        responseEntity.getBody().addData("pagination", pagination);

        return responseEntity;
    }

    @RequestMapping(value = "/rest-api/thscp-portal-stock-in-hand", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity getThScpStockInHand() {
        return RestResponse.response("data", lookupService.getThScpStockInHand());
    }


    //Start of COVID 19

    @RequestMapping(value = "/rest-api/sc-portal-emergency-commodities", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getEmergencystockpiles(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "max", required = false, defaultValue = "10") int max
    ){

        Pagination pagination = new Pagination(page, max);

       // Integer totalCount = lookupService.getTotalEmergencyCommodites(startDate);
     //   pagination.setTotalRecords(totalCount);

        ResponseEntity<OpenLmisResponse> response =
                OpenLmisResponse.response("data",lookupService.getEmergencyCommodites(startDate, pagination));
        response.getBody().addData("pagination", pagination);
        return response;
    }

    @RequestMapping(value = "/rest-api/sc-portal-order-delivery", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getOrderDelivery(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "max", required = false, defaultValue = "10") int max
    ){

        Pagination pagination = new Pagination(page, max);

      //  Integer totalCount = lookupService.getTotalOrderDelivery(startDate);
      //  pagination.setTotalRecords(totalCount);

        ResponseEntity<OpenLmisResponse> response =
                OpenLmisResponse.response("data",lookupService.getOrderDelivery(startDate, pagination));
        response.getBody().addData("pagination", pagination);
        return response;
    }

    @RequestMapping(value = "/rest-api/sc-portal-hfr-facilities", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAllHFRFacilities (
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "max", required = false, defaultValue = "10") int max
    ){

        Pagination pagination = new Pagination(page, max);

       // Integer totalCount = lookupService.getTotalHFRFacilities();
       // pagination.setTotalRecords(totalCount);

        ResponseEntity<OpenLmisResponse> response =
                OpenLmisResponse.response("data",lookupService.getAllHFRFacilities(pagination));
        response.getBody().addData("pagination", pagination);
        return response;
    }

    @RequestMapping(value = "/rest-api/sc-portal-stock-in-hand", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockInHand(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "max", required = false, defaultValue = "10") int max
    ){

        Pagination pagination = new Pagination(page, max);

        //Integer totalCount = lookupService.getTotalStockInHand(startDate);
       // pagination.setTotalRecords(totalCount);

        ResponseEntity<OpenLmisResponse> response =
                OpenLmisResponse.response("data",lookupService.getStockInHand(startDate, pagination));
        response.getBody().addData("pagination", pagination);
        return response;
    }

    @RequestMapping(value = "/rest-api/sc-portal-products", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAllProducts(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(value = "max", required = false, defaultValue = "10") int max
    ){

        Pagination pagination = new Pagination(page, max);

       // Integer totalCount = lookupService.getTotalProducts();
        // /pagination.setTotalRecords(totalCount);

        ResponseEntity<OpenLmisResponse> response =
                OpenLmisResponse.response("data",lookupService.getAllProducts(pagination));
        response.getBody().addData("pagination", pagination);
        return response;
    }

    @RequestMapping(value = "/rest-api/sc-portal-wastages", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getWastages(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "max", required = false, defaultValue = "10") int max
    ){

        Pagination pagination = new Pagination(page, max);

     //   Integer totalCount = lookupService.getTotalWastages(startDate);
      //  pagination.setTotalRecords(totalCount);

        ResponseEntity<OpenLmisResponse> response =
                OpenLmisResponse.response("data",lookupService.getWastages(startDate,pagination));
        response.getBody().addData("pagination", pagination);
        return response;
    }


    @RequestMapping(value = "/rest-api/sc-portal-forecasting", method = RequestMethod.GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getForeCastingData(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "max", required = false, defaultValue = "10") int max
    ){

        Pagination pagination = new Pagination(page, max);

      //  Integer totalCount = lookupService.getTotalForeCastingData(startDate);
      //  pagination.setTotalRecords(totalCount);

        ResponseEntity<OpenLmisResponse> response =
                OpenLmisResponse.response("data",lookupService.getForeCastingData(startDate,pagination));
        response.getBody().addData("pagination", pagination);
        return response;
    }

   //End Of covid APIs
}
