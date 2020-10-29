
package org.openlmis.report.service;


import lombok.NoArgsConstructor;
import org.openlmis.report.mapper.RequisitionGroupMemberReportQueryMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.RequisitionGroupParam;
import org.openlmis.report.model.report.RequisitionGroupMemberReport;
import org.openlmis.report.util.ParameterAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class RequisitionGroupMemberReportDataProvider extends ReportDataProvider {
    @Autowired
    private RequisitionGroupMemberReportQueryMapper reportsMapper;

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filter, Map<String, String[]> sorter, int page, int pageSize) {
        RequisitionGroupParam param = getReportFilterData(filter, page, pageSize);
        List<RequisitionGroupMemberReport> reportList = this.reportsMapper.loadRequisitionGroupMemberReport(param, this.getUserId());
        return reportList;
    }

    public RequisitionGroupParam getReportFilterData(Map<String, String[]> filterCriteria, int page, int pagSize) {
        RequisitionGroupParam param = ParameterAdaptor.parse(filterCriteria, RequisitionGroupParam.class);
        return param;
    }
}
