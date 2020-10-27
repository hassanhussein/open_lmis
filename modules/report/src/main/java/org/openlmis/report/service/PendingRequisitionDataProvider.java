package org.openlmis.report.service;


import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.mapper.PendingRequisitionsMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.NonReportingFacilityParam;
import org.openlmis.report.model.report.PendingRequistionReport;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class PendingRequisitionDataProvider extends ReportDataProvider {
    @Autowired
    private PendingRequisitionsMapper reportMapper;
    @Autowired
    private SelectedFilterHelper filterHelper;

    @Override
    public List<? extends ResultRow> getResultSet(Map<String, String[]> filterCriteria) {
        RowBounds rowBounds = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        NonReportingFacilityParam nonReportingFacilityParam = getFilterParameters(filterCriteria);
        List<PendingRequistionReport> requistionReports = reportMapper.getPendingRequisitions(nonReportingFacilityParam, rowBounds, this.getUserId());
        return requistionReports;
    }


    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        RowBounds rowBounds = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        NonReportingFacilityParam nonReportingFacilityParam = getFilterParameters(filterCriteria);
        List<PendingRequistionReport> requistionReports = reportMapper.getPendingRequisitions(nonReportingFacilityParam, rowBounds, this.getUserId());
        return requistionReports;
    }

    private NonReportingFacilityParam getFilterParameters(Map params) {
        return ParameterAdaptor.parse(params, NonReportingFacilityParam.class);
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {
        return filterHelper.getProgramPeriodGeoZone(params);
    }

}
