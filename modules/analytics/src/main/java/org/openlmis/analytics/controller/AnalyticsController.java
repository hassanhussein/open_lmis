package org.openlmis.analytics.controller;

import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.openlmis.analytics.Repository.Mapper.DashboardPerformanceBasedMapper;
import org.openlmis.analytics.Repository.Mapper.PercentageWastageMapper;
import org.openlmis.analytics.Repository.Mapper.StockAvailabilityMapper;
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

    @RequestMapping(value = "/requisition-report", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> requisitionRepor() {
        return OpenLmisResponse.response("rnr_list", service.getAllUsers());
    }


    @RequestMapping(value = "/stock-status-summary", method = GET, headers = BaseController.ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getStockStatusSummaryByProduct(
            @Param("product") Long product,
            @Param("program") Long program,
            @Param("year") Long year,
            HttpServletRequest request
    ) {

        return OpenLmisResponse.response("stocks", service.getStockStatusSummary(loggedInUserId(request),product,program,year));
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


}