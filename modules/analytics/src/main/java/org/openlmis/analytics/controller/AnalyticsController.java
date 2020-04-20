package org.openlmis.analytics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.openlmis.analytics.Repository.Mapper.*;
import org.openlmis.analytics.service.DashboardService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;

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
    private FinancialAnalyticMapper financialAnalyticMapper;

    @Autowired
    private GeoFacilityStockStatusMapper stockStatusMapper;


    @Autowired
    private StockOutRateMapper stockOutRateMapper;

    @Autowired
    private COVIDMapper covidMapper;


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

    @RequestMapping(value = "/getDistrictFundUtilization.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getDistrictFundUtilization(               @Param("program") Long program,
                                                                                       @Param("zone") Long zone,
                                                                                       @Param("year") Long year,
                                                                                       @Param("period") Long period,
                                                                                       HttpServletRequest request) {
        return OpenLmisResponse.response("financies", this.financialAnalyticMapper.getDistrictFundUtilization(loggedInUserId(request),zone, program,year,period));
    }


    @RequestMapping(value = "/getSourceOfFundsByLocation.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getSourceOfFundsByLocation(@Param("program") Long program,
                                                                       @Param("period") Long period) {
        return OpenLmisResponse.response("sourceOfFunds", this.requisitionStatusMapper.getSourceOfFundsByLocation(program, period));
    }

    @RequestMapping(value = "/getGeoStockStatusDetails.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getGeoStockStatusDetails(               @Param("program") Long program,
                                                                                   @Param("product") Long product,
                                                                                   @Param("year") Long year,
                                                                                   @Param("period") Long period,
                                                                                   @Param("facility") Long facility,
                                                                                   HttpServletRequest request) {
        return OpenLmisResponse.response("stocks", this.stockStatusMapper.GeoFacilityStockStatusDetails(loggedInUserId(request),product,program,year,period, facility));
    }


    @RequestMapping(value = "/getStockOutRate.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockOutRate(@Param("program") Long program,
                                                            @Param("period") Long period) {
        return OpenLmisResponse.response("stockOutRates", this.stockOutRateMapper.getStockOutRate(program, period));
    }

    @RequestMapping(value = "/getStockStatusByLocation.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockStatusByLocation(@Param("program") Long program,
                                                                     @Param("product") Long product,
                                                                     @Param("period") Long period) {
        return OpenLmisResponse.response("getStockStatusByLocation", this.stockOutRateMapper.getStockStatusByLocation(program, period, product));
    }


    @RequestMapping(value = "/getTLEAndTLDConsumption.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getTLEAndTLDConsumption(@Param("year") Long year,
                                                                     @Param("schedule") Long schedule) {
        return OpenLmisResponse.response("TLEAndTLDConsumption", this.stockOutRateMapper.getTLEAndTLDConsumption(year, schedule));
    }

    @RequestMapping(value = "/getStockOutRateByProduct.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockOutRateByProduct(@Param("year") Long year,
                                                                    @Param("schedule") Long schedule,
                                                                     @Param("product") Long product) {
        return OpenLmisResponse.response("StockOutRateByProduct", this.stockOutRateMapper.getStockOutRateByProduct(year, schedule, product));
    }


    @RequestMapping(value = "/getStockOutRateTrendOfTracerProducts.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockOutRateTrendOfTracerProducts(@Param("year") Long year) {
        return OpenLmisResponse.response("StockOutRateTrendOfTracerProducts", this.stockOutRateMapper.getStockOutRateTrendOfTracerProducts(year));
    }

    @RequestMapping(value = "/getStockOutRateTrendOfProducts.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockOutRateTrendOfProducts(@Param("year") Long year, @Param("product") Long product) {
        return OpenLmisResponse.response("StockOutRateTrendOfProducts", this.stockOutRateMapper.getStockOutRateTrendOfProducts(year, product));
    }

    @RequestMapping(value = "/tz-reg.json", method = GET, headers = ACCEPT_JSON)
    public @ResponseBody Object getRegionMapJSON() {
        ClassPathResource resource = new ClassPathResource("/static/tz-reg.json");
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/tz-district.json", method = GET, headers = ACCEPT_JSON)
    public @ResponseBody Object getDistrictMapJSON() {
        ClassPathResource resource = new ClassPathResource("/static/tz-district.json");
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequestMapping(value = "/getAllCommoditiesDetailsByDistrict.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAllCommoditiesDetailsByDistrict(@Param("program") Long program,
                                                                     @Param("product") Long product,
                                                                     @Param("period") Long period) {
       return OpenLmisResponse.response("commoditiesDetailsByDistrict", this.stockOutRateMapper.getAllCommoditiesDetailsByDistrict(program, period, product));

    }


    @RequestMapping(value = "/getLatestReportedStockStatusForAllTracerByDistrict.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getLatestReportedStockStatusByDistrict() {
        return OpenLmisResponse.response("commoditiesDetailsByDistrict", this.stockOutRateMapper.getLatestReportedStockOnHandForTracer());


    }

    @RequestMapping(value = "/getLatestReportedStockStatusForProductByDistrict.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getLatestReportedStockOnHandForProductByDistrict(@Param("product") Long product) {
        return OpenLmisResponse.response("commoditiesDetailsByDistrict", this.stockOutRateMapper.getLatestReportedStockOnHandForProductByDistrict(product));
    }

    @RequestMapping(value = "/getLatestStockImbalanceReportByDistrictForTracer.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getLatestStockImbalanceReportByDistrictForTracer() {
        return OpenLmisResponse.response("stockImbalanceByDistrict", this.stockOutRateMapper.getLatestStockImbalanceReportByDistrictForTracer());
    }


    @RequestMapping(value = "/getLatestStockImbalanceReportByDistrictForProduct.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getLatestStockImbalanceReportByDistrictForProduct(@Param("product") Long product) {
        return OpenLmisResponse.response("stockImbalanceByDistrict", this.stockOutRateMapper.getLatestStockImbalanceReportByDistrictForProduct(product));
    }

    @RequestMapping(value = "/getStockImbalanceSummary.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockImbalanceSummary() {
        return OpenLmisResponse.response("imbalances", this.stockOutRateMapper.getStockImbalanceSummary());
    }


    @RequestMapping(value = "/getCOVIDStockStatus.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getCOVIDStockStatus(@Param("product") Long product,
                                                                               @Param("startdate") String startDate, @Param("enddate") String endDate )  {
       if(product==0)
        return OpenLmisResponse.response("COVIDStockStatus", this.covidMapper.getAllStockStatus(startDate, endDate));
     else
         return OpenLmisResponse.response("COVIDStockStatus", this.covidMapper.getStockStatusperProduct(product, startDate, endDate));
    }

    @RequestMapping(value = "/getCOVIDReportByFacility.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getCOVIDStockStatus(@Param("facility") Long facility ) {
        if(facility==0)
            return OpenLmisResponse.response("COVIDReportByFacility", this.covidMapper.getCOVIDReportForAllFacilities());
      else
            return OpenLmisResponse.response("COVIDReportByFacility", this.covidMapper.getCOVIDReportByFacility(facility));


    }

    @RequestMapping(value = "/getCOVIDDesignatedFacilities.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getCOVIDDesignatedFacilities() {
        return OpenLmisResponse.response("getCOVIDDesignatedFacilities", this.covidMapper.getCOVIDDesignatedFacilities());
    }


    @RequestMapping(value = "/getInboundReports.json", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getInboundReports(@Param("product") Long product) {
        if(product==0)
            return OpenLmisResponse.response("inboundReports", this.covidMapper.getInboundReports());
        else
            return OpenLmisResponse.response("inboundReports", this.covidMapper.getInboundByProductReports(product));
    }
}