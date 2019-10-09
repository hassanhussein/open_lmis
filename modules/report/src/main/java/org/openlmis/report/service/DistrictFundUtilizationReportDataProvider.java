package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.mapper.DistrictFundUtilizationMapper;
import org.openlmis.report.model.ReportParameter;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.DistrictFundUtilizationParam;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class DistrictFundUtilizationReportDataProvider extends ReportDataProvider {

    @Autowired
    private DistrictFundUtilizationMapper reportMapper;

    @Autowired
    private SelectedFilterHelper filterHelper;

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
        return reportMapper.getDistrictFundUtilization(getReportFilterData(filterCriteria), sortCriteria, rowBounds, this.getUserId());
    }

    public ReportParameter getReportFilterData(Map<String, String[]> filterCriteria) {
        return ParameterAdaptor.parse(filterCriteria, DistrictFundUtilizationParam.class);
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {
        return filterHelper.getProgramPeriodGeoZone(params);
    }


}
