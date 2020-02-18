package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.openlmis.report.mapper.StockImbalanceHomeFacilityReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.StockImbalanceReportParam;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.ReportPaginationHelper;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class StockImbalanceHomeFacilityReportDataProvider extends ReportDataProvider  {


    @Autowired
    private SelectedFilterHelper filterHelper;

    @Autowired
    private StockImbalanceHomeFacilityReportMapper reportMapper;

    @Autowired
    private ReportPaginationHelper paginationHelper;

    @Value("${report.status.considered.accepted}")
    private String configuredAcceptedRnrStatuses;

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        return  reportMapper.getReport(getReportFilterData(filterCriteria), sortCriteria, paginationHelper.getPagination(page), this.getUserId());
    }

    public StockImbalanceReportParam getReportFilterData(Map<String, String[]> filterCriteria) {
        StockImbalanceReportParam param = ParameterAdaptor.parse(filterCriteria, StockImbalanceReportParam.class);
        param.setAcceptedRnrStatuses(configuredAcceptedRnrStatuses);
        return param;
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {
        return filterHelper.getProgramPeriodGeoZone(params);
    }
}


