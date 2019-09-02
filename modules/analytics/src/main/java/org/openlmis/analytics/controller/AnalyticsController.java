package org.openlmis.analytics.controller;

import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.openlmis.analytics.Repository.Mapper.*;
import org.openlmis.analytics.service.DashboardService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/api/dashboard")
public class AnalyticsController extends BaseController {

    @Autowired
    private DashboardService service;

    @Autowired
    private DashboardPerformanceBasedMapper performanceBasedMapper;

    @Autowired
    private StockAvailabilityMapper stockAvailabilityMapper;

     @Autowired
    private PercentageWastageMapper percentageWastageMapper;

    @Autowired
    private AnalyticsMapper analyticsMapper;

    @Autowired
    private DashboardTimelinessReportingMapper reportingMapper;

    @Autowired
    private OnTimeDeliveryMapper deliveryMapper;

    @Autowired
    private RequisitionStatusMapper requisitionStatusMapper;
    @Autowired
    private DashboardConsumptionMapper consumptionMapper;

    @Autowired
    private GeoFacilityStockStatusMapper stockStatusMapper;

    @RequestMapping(value = "/requisition-report", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> requisitionRepor() {
        return OpenLmisResponse.response("rnr_list", service.getAllUsers());
    }


    @RequestMapping(value = "/stock-status-summary", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockStatusSummaryByProduct(
            @Param("product") Long product,
            @Param("program") Long program,
            @Param("year") Long year,
            @Param("schedule") Long schedule,

            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", service.getStockStatusSummary(loggedInUserId(request),product,program,year,schedule));
    }

  @RequestMapping(value = "/stock-available-for-period", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockAvailableForPeriod(
            @Param("period") Long period,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", service.getStockAvailableForPeriod(loggedInUserId(request),period));
    }

    @RequestMapping(value = "/stock-available-for-program-period", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockForProductandProgram(
            @Param("period") Long period,
            @Param("program") Long program,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", service.getStockForProductandProgram(loggedInUserId(request),program,period));
    }

    @RequestMapping(value = "/consumption-trend-year", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getConsumptioTrends(
            @Param("year") Long year,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", service.getConsumptioTrends(loggedInUserId(request),year));
    }

  @RequestMapping(value = "/rnr-passed-quality-check", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getConsumptioTrends(
            @Param("program") Long program,
            @Param("period") Long period,
            @Param("year") Long year,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", performanceBasedMapper.getStockForProductandProgram(loggedInUserId(request),program,period));
    }

    @RequestMapping(value = "/index-stock-availability", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getIndexOfStockAvailability(
            @Param("program") Long program,
            @Param("period") Long period,
            @Param("year") Long year,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", stockAvailabilityMapper.getIndexOfStockAvailability(loggedInUserId(request),program,period));
    }

   @RequestMapping(value = "/percentage-wastage", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getPercentageWastage(
            @Param("program") Long program,
            @Param("period") Long period,
            @Param("year") Long year,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", percentageWastageMapper.getPercentageWastage(loggedInUserId(request),program,period));
    }


    @RequestMapping(value = "/stock-status-by-program-and-year", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockSummaryByYearAndProgram(
            @Param("program") Long program,
            @Param("year") Long year,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", analyticsMapper.getStockSummaryByYearAndProgram(loggedInUserId(request),program,year));
    }


    @RequestMapping(value = "/stock-availability-summary", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getSummary(
            @Param("program") Long program,
            @Param("period") Long period,
            @Param("year") Long year,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", stockAvailabilityMapper.getStockSummary(loggedInUserId(request),program,period,year));
    }


    @RequestMapping(value = "/stock-availability-trends-by-program-and-year", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockAvailabilityTrends(
            @Param("program") Long program,
            @Param("period") Long period,
            @Param("year") Long year,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", stockAvailabilityMapper.getStockAvailabilityTrends(loggedInUserId(request),program,period,year));
    }

    @RequestMapping(value = "/stock-availability-by-level", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockAvailableByLevel(
            @Param("program") Long program,
            @Param("period") Long period,
            @Param("year") Long year,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", stockAvailabilityMapper.getStockAvailabilityByLevel(loggedInUserId(request),program,period,year));
    }

    @RequestMapping(value = "/dashboard-timeliness-reporting", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getTimelinessReporting(
            @Param("program") Long program,
            @Param("period") Long period,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", reportingMapper.getTimelinessReporting(loggedInUserId(request),program,period));
    }

    @RequestMapping(value = "/on-time-delivery", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getOnTimeDelivery(
            @Param("program") Long program,
            @Param("period") Long period,
            HttpServletRequest request
    ) {
        return OpenLmisResponse.response("stocks", deliveryMapper.getOnTimeDelivery(loggedInUserId(request),program,period));
    }

    //ZAAMBIA CONTROLLET


    @RequestMapping(value = "/getRejectionCount.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getRejectionStatus() {
        return OpenLmisResponse.response("rejections", this.requisitionStatusMapper.getRnRRejectionCount());
    }
    @RequestMapping(value = "/getNumberOfEmergency.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getNumberOfEmergency() {
        return OpenLmisResponse.response("emergency", this.requisitionStatusMapper.getNumberOfEmergency());
    }

    @RequestMapping(value = "/getPercentageOfEmergencyOrderByProgram.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getPercentageOfEmergencyOrderByProgram() {
        return OpenLmisResponse.response("emergency", this.requisitionStatusMapper.getPercentageOfEmergencyOrderByProgram());
    }
    @RequestMapping(value = "/getEmergencyOrderByProgram.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getEmergencyOrderByProgram() {
        return OpenLmisResponse.response("emergency", this.requisitionStatusMapper.getEmergencyOrderByProgram());
    }
    @RequestMapping(value = "/getTrendOfEmergencyOrdersSubmittedPerMonth.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getTrendOfEmergencyOrdersSubmittedPerMonth() {
        return OpenLmisResponse.response("emergency", this.requisitionStatusMapper.getTrendOfEmergencyOrdersSubmittedPerMonth());
    }
    @RequestMapping(value = "/emergencyOrderTrends.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> emergencyOrderTrends() {
        return OpenLmisResponse.response("emergency", this.requisitionStatusMapper.emergencyOrderTrends());
    }

    @RequestMapping(value = "/getEmergencyOrderFrequentAppearingProducts.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getEmergencyOrderFrequentAppearingProducts() {
        return OpenLmisResponse.response("products", this.requisitionStatusMapper.getEmergencyOrderFrequentAppearingProducts());
    }

    @RequestMapping(value = "/getConsumptionSummaryTrends.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getConsumptionSummaryTrends(               @Param("program") Long program,
                                                                                       @Param("product") String product,
                                                                                       @Param("year") Long year,
                                                                                       @Param("schedule") Long schedule,
                                                                                       HttpServletRequest request) {
        return OpenLmisResponse.response("stocks", this.consumptionMapper.getConsumptionSummary(loggedInUserId(request), product ,program,schedule,year));
    }

    @RequestMapping(value = "/getEmergencyAndRegularRnRTrends.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getConsumptionSummaryTrends(               @Param("program") Long program,
                                                                                       HttpServletRequest request) {
        return OpenLmisResponse.response("stocks", this.requisitionStatusMapper.getEmergencyAndRegularRnRTrend(loggedInUserId(request),program));
    }


    @RequestMapping(value = "/getGeoStockStatusForMap.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getGeoStockStatusForMap(               @Param("program") Long program,
                                                                                       @Param("product") Long product,
                                                                                       @Param("year") Long year,
                                                                                       @Param("period") Long period,
                                                                                       HttpServletRequest request) {
        return OpenLmisResponse.response("stocks", this.stockStatusMapper.getGeoFacilityStockStatus(loggedInUserId(request),product,program,year,period));
    }

}