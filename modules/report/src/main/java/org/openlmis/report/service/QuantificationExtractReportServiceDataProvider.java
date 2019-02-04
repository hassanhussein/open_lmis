package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.Pagination;
import org.openlmis.report.mapper.QuantificationExtractReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.QuantificationExtractReportParam;
import org.openlmis.report.model.report.QuantificationExtractReport;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class QuantificationExtractReportServiceDataProvider extends ReportDataProvider {

    @Value("${report.status.considered.accepted}")
    private String configuredAcceptedRnrStatuses;

    @Autowired
    private QuantificationExtractReportMapper reportMapper;

    @Autowired
    private SelectedFilterHelper filterHelper;

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
        Pagination pagination = new Pagination(page,pageSize);
        pagination.setTotalRecords(reportMapper.getTotalFilteredSortedPagedQuantificationReport(getReportFilterData(filterCriteria), sortCriteria ,this.getUserId()).getTotalRecords());
        QuantificationExtractReport report = new QuantificationExtractReport();
        report.setPagination(pagination);
        List<QuantificationExtractReport> all = reportMapper.getReport(getReportFilterData(filterCriteria), sortCriteria, rowBounds, this.getUserId());
        all.add(report);
        return all;
    }

    private QuantificationExtractReportParam getReportFilterData(Map<String, String[]> filterCriteria) {
        QuantificationExtractReportParam param = ParameterAdaptor.parse(filterCriteria, QuantificationExtractReportParam.class);
        param.setAcceptedRnrStatuses(configuredAcceptedRnrStatuses);
        return param;
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {
        return filterHelper.getProgramPeriodGeoZone(params);
    }

}
