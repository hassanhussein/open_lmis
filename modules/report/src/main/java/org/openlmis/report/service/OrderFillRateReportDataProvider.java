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

package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.mapper.OrderFillRateReportMapper;
import org.openlmis.report.mapper.RnRFeedbackReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.OrderFillRateReportParam;
import org.openlmis.report.model.params.OrderReportParam;
import org.openlmis.report.model.report.MasterReport;
import org.openlmis.report.model.report.OrderFillRateReport;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class OrderFillRateReportDataProvider extends ReportDataProvider {

    public static final String ORDER_FILL_RATE = "ORDER_FILL_RATE";
    public static final String TOTAL_PRODUCTS_APPROVED = "TOTAL_PRODUCTS_APPROVED";
    public static final String TOTAL_PRODUCT_SHIPPED = "TOTAL_PRODUCT_SHIPPED";
    public static final String REPORT_STATUS = "REPORT_STATUS";
    @Autowired
    private OrderFillRateReportMapper reportMapper;

    @Autowired
    private SelectedFilterHelper selectedFilterHelper;

    @Autowired
    private RnRFeedbackReportMapper feedbackReportMapper;


    @Override
    public List<? extends ResultRow> getResultSet(Map<String, String[]> filterCriteria) {
        OrderFillRateReportParam reportParam = this.getFilterParam(filterCriteria);
        List<OrderFillRateReport> detail = this.getReport(reportParam);
        return detail;
    }

    public OrderFillRateReportParam getFilterParam(Map<String, String[]> filterCriteria) {

        OrderFillRateReportParam parameter = ParameterAdaptor.parse(filterCriteria, OrderFillRateReportParam.class);
        parameter.setUserId(this.getUserId());
        return parameter;
    }

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {

        final OrderFillRateReportParam reportParam = this.getFilterParam(filterCriteria);
        List<MasterReport> reportList = new ArrayList<>();
        MasterReport report = new MasterReport();
        List<OrderFillRateReport> detail = this.getReport(reportParam);
        report.setDetails(detail);
        Long approved = detail.stream().filter(row -> row.getApproved() != null && row.getApproved() > 0).count();
        Long shipped = detail.stream().filter(row -> (row.getReceipts() != null && row.getReceipts() > 0)
                || (row.getSubstitutedProductQuantityShipped() != null && row.getSubstitutedProductQuantityShipped() > 0)).count();
        Float orderFillRate = ((approved == 0L || approved == null) ? 0L : ((float) shipped / approved) * 100);
        String requistionStatus = reportMapper.getFillRateReportRequisitionStatus(reportParam);
        Map<String, Object> keyValues = new HashMap();
        keyValues.put(ORDER_FILL_RATE, orderFillRate);
        keyValues.put(TOTAL_PRODUCTS_APPROVED, approved);
        keyValues.put(TOTAL_PRODUCT_SHIPPED, shipped);
        keyValues.put(REPORT_STATUS, detail.size() == 0 ? requistionStatus : null);
        report.setKeyValueSummary(keyValues);
        reportList.add(report);
        return reportList;
    }

    private List<OrderFillRateReport> getReport(OrderFillRateReportParam reportParam) {
        List<OrderFillRateReport> detailList = reportMapper.getReport(reportParam, this.getUserId());
        detailList.stream().filter(row -> row.getSubstitutedquantityshipped()!=null && row.getSubstitutedquantityshipped() > 0).
                forEach((order) -> {
            List<OrderFillRateReport> substituteProReportList = null;
            reportParam.setProductCode(order.getProductcode());
            reportParam.setRnrId(order.getRnrid());
            substituteProReportList = reportMapper.getSubStitutProductReport(reportParam, this.getUserId());
            order.setSubstituteProductList(substituteProReportList);
        });
        return detailList;
    }

    @Override
    public HashMap<String, String> getExtendedHeader(Map params) {
        HashMap<String, String> result = new HashMap<String, String>();
        OrderFillRateReportParam parameter = ParameterAdaptor.parse(params, OrderFillRateReportParam.class);
        result.put("REPORT_FILTER_PARAM_VALUES", selectedFilterHelper.getProgramGeoZoneFacility(params));
        return result;

    }

    public int getReportTotalCount(Map<String, String[]> filter) {
        OrderFillRateReportParam reportParam = null;
        reportParam = this.getFilterParam(filter);
        return reportMapper.getReportTotalCount(reportParam, this.getUserId());
    }

    public void refresh(){
        reportMapper.refresh();
    }
}
