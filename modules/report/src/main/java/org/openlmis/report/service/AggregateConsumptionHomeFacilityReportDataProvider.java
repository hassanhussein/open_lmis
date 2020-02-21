package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.openlmis.report.mapper.AggregateConsumptionHomeFacilityReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.AggregateConsumptionReportParam;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.ReportPaginationHelper;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class AggregateConsumptionHomeFacilityReportDataProvider extends ReportDataProvider {


    @Value("${report.status.considered.accepted}")
    private String configuredAcceptedRnrStatuses;

    @Autowired
    private SelectedFilterHelper filterHelper;

    @Autowired
    private AggregateConsumptionHomeFacilityReportMapper reportMapper;

    @Autowired
    private ReportPaginationHelper paginationHelper;

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        paginationHelper.setPageSize(pageSize + "");
        return reportMapper.getAggregateConsumptionReport(getReportFilterData(filterCriteria), sortCriteria, paginationHelper.getPagination(page), this.getUserId());
    }

    public AggregateConsumptionReportParam getReportFilterData(Map<String, String[]> filterCriteria) {
        AggregateConsumptionReportParam param = ParameterAdaptor.parse(filterCriteria, AggregateConsumptionReportParam.class);
        param.setAcceptedRnrStatuses(configuredAcceptedRnrStatuses);
        return param;
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {

        Map<String, String[]> modifiableParams = new HashMap<String, String[]>();
        modifiableParams.putAll(params);
        modifiableParams.put("userId", new String[]{String.valueOf(this.getUserId())});
        return filterHelper.getProgramPeriodGeoZone(modifiableParams);
    }

}
