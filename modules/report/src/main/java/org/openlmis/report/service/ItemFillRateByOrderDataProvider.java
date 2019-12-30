package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.openlmis.report.mapper.ItemFillRateReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.ItemFillRateReportParam;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.ReportPaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class ItemFillRateByOrderDataProvider extends ReportDataProvider {

    @Autowired
    private ItemFillRateReportMapper mapper;

    @Autowired
    private ReportPaginationHelper helper;

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sorterCriteria, int page, int pageSize) {
        // RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
        return mapper.getItemByRnr(getReportFilterData(filterCriteria), helper.getPagination(page), this.getUserId());
    }

    public ItemFillRateReportParam getReportFilterData(Map<String, String[]> filterCriteria) {
        return ParameterAdaptor.parse(filterCriteria, ItemFillRateReportParam.class);
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {
        return getReportFilterData(params).toString();
    }


}
