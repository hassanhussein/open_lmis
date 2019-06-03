package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.Pagination;
import org.openlmis.report.mapper.MonthlyStockStatusReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.MonthlyStockStatusParam;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.ReportPaginationHelper;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class MonthlyStockStatusReportDataProvider extends ReportDataProvider {

    @Autowired
    private MonthlyStockStatusReportMapper reportMapper;

    @Autowired
    private SelectedFilterHelper filterHelper;

    @Autowired
    private ReportPaginationHelper paginationHelper;

    private MonthlyStockStatusParam getParameter(Map params) {
        return ParameterAdaptor.parse(params, MonthlyStockStatusParam.class);
    }

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        Pagination pagination = paginationHelper.getPagination(page);
        return reportMapper.getReport(getParameter(filterCriteria), pagination);
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {
        return filterHelper.getProgramPeriodGeoZone(params);
    }
}
