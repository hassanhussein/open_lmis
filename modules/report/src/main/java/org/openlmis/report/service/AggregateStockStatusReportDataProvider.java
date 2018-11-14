package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;

import org.openlmis.report.mapper.AggregateStockStatusReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.AggregateStockStatusReportParam;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class AggregateStockStatusReportDataProvider extends ReportDataProvider {


    @Value("${report.status.considered.accepted}")
    private String configuredAcceptedRnrStatuses;

    @Autowired
    private SelectedFilterHelper filterHelper;

    @Autowired
    private AggregateStockStatusReportMapper reportMapper;

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
        return reportMapper.getAggregateStockStatusReport(getReportFilterData(filterCriteria), sortCriteria, rowBounds, this.getUserId());
    }

    public AggregateStockStatusReportParam getReportFilterData(Map<String, String[]> filterCriteria) {
        AggregateStockStatusReportParam param = ParameterAdaptor.parse(filterCriteria, AggregateStockStatusReportParam.class);
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
