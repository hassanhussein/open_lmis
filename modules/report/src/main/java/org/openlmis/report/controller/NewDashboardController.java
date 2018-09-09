package org.openlmis.report.controller;

import lombok.NoArgsConstructor;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.report.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@NoArgsConstructor
@RequestMapping(value = "/dashboard")
@Controller
public class NewDashboardController extends BaseController {
private static final String REPORTING_RATE="reportingRate";
    private static final String STOCK_STATUS="stockStatus";
    private static final String ITEM_FILL_RATE="itemFillRate";
    private static final String DAILY_STOCK_STATUS="dailyStockStatus";
    private static final String COMMODITY_STATUS="commodity";
    private static final String FACILITY_COMMODITY_STATUS="facilityCommodity";
    private static final String EXPIRED_PRODUCTS="expiredProducts";
     private static  final  String RNR_EMERGENCY_REGULAR_TYPE="rnrTypes";
    private static  final  String SHIPMENT_INTERFACES="shipmentInterfaces";
    private static final String VITAL_STATUSES="vitalStatuses";
    private static final String PROUDCT_SUPPLY_STATUSES="supplyStatuses";

    @Autowired
private DashboardService dashboardService;
    @RequestMapping(value = "/reporting-rate", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getOrderFillRate(@RequestParam("zoneId") Long zoneId,
                                                             @RequestParam("periodId") Long periodId,
                                                             @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(REPORTING_RATE, this.dashboardService.getReportingRate(zoneId, periodId, programId));
    }

    @RequestMapping(value = "/stock-staus-availablity", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockStatusAvailablity(@RequestParam("zoneId") Long zoneId,
                                                             @RequestParam("periodId") Long periodId,
                                                             @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(STOCK_STATUS, this.dashboardService.getStockStaus(zoneId, periodId, programId));
    }
    @RequestMapping(value = "/item-fill-rate", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getItemFillRate(@RequestParam("zoneId") Long zoneId,
                                                                      @RequestParam("periodId") Long periodId,
                                                                      @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(ITEM_FILL_RATE, this.dashboardService.getItemFillRate(zoneId, periodId, programId));
    }
    @RequestMapping(value = "/daily-stock-status", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getDailyStockStatus(@RequestParam("zoneId") Long zoneId,
                                                                @RequestParam("periodId") Long periodId,
                                                                @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(DAILY_STOCK_STATUS, this.dashboardService.getDailyStockStatus(zoneId, periodId, programId));
    }
    @RequestMapping(value = "/commodity-status", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getCommodityStaus(@RequestParam("zoneId") Long zoneId,
                                                                @RequestParam("periodId") Long periodId,
                                                                @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(COMMODITY_STATUS, this.dashboardService.getCommodityMostOSandUSList(zoneId, periodId, programId));
    }
    @RequestMapping(value = "/facility-commodity-status", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getFacilityCommodityStaus(@RequestParam("zoneId") Long zoneId,
                                                              @RequestParam("periodId") Long periodId,
                                                              @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(FACILITY_COMMODITY_STATUS, this.dashboardService.getFacilityCommodityMostOSandUSList(zoneId, periodId, programId));
    }
    @RequestMapping(value = "/expired_products", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getExpiredProducts(@RequestParam("zoneId") Long zoneId,
                                                                      @RequestParam("periodId") Long periodId,
                                                                      @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(EXPIRED_PRODUCTS, this.dashboardService.getExpiredProducts(zoneId, periodId, programId));
    }
    @RequestMapping(value = "/rnr-emergency-regular-types", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getRnrWithTypeCount(@RequestParam("zoneId") Long zoneId,
                                                               @RequestParam("periodId") Long periodId,
                                                               @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(RNR_EMERGENCY_REGULAR_TYPE, this.dashboardService.getRnrWithTypeCount(zoneId, periodId, programId));
    }
    @RequestMapping(value = "/shipment-interface", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getInterfacesStatusReport(@RequestParam("zoneId") Long zoneId,
                                                                      @RequestParam("periodId") Long periodId,
                                                                      @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(SHIPMENT_INTERFACES, this.dashboardService.getInterfacesStatusReport(zoneId, periodId, programId));
    }

    @RequestMapping(value = "/vital_status", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getVitalStatuees(@RequestParam("zoneId") Long zoneId,
                                                                      @RequestParam("periodId") Long periodId,
                                                                      @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(VITAL_STATUSES, this.dashboardService.getVitalStatuees(zoneId, periodId, programId));
    }
    @RequestMapping(value = "/supply_status", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getProductSupplyStatus(@RequestParam("zoneId") Long zoneId,
                                                             @RequestParam("periodId") Long periodId,
                                                             @RequestParam("programId") Long programId) {
        return OpenLmisResponse.response(PROUDCT_SUPPLY_STATUSES, this.dashboardService.getProductSupplyStatus(zoneId, periodId, programId));
    }
}
