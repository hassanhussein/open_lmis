package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.service.MessageService;
import org.openlmis.report.mapper.AuditTrailReportMapper;
import org.openlmis.report.model.ReportParameter;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.AuditTrailReportParam;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class AuditTrailReportDataProvider extends ReportDataProvider {

    @Autowired
    private AuditTrailReportMapper reportMapper;

    @Autowired
    private SelectedFilterHelper filterHelper;

    @Autowired
    private MessageService messageService;

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria,
                                                   Map<String, String[]> sortCriteria,
                                                   int page, int pageSize) {
        RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
        return reportMapper.getAuditTrailReport(getReportFilterData(filterCriteria), this.getUserId());
    }

    public ReportParameter getReportFilterData(Map<String, String[]> filterCriteria) {
        AuditTrailReportParam auditTrailReportParam = ParameterAdaptor.parse(filterCriteria,
                AuditTrailReportParam.class);
        return auditTrailReportParam;
    }

    @Override
    public int getReportTotalCount(Map<String, String[]> filterCriteria) {
        return reportMapper.getAuditTrailReportCount(getReportFilterData(filterCriteria));
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {
        return (new StringBuffer()).append(messageService.message("label.audit.actions"))
                .append(" : ")
                .append((params.get("action")[0]).isEmpty() ? messageService.message("label.all") :
                        messageService.message("audit.label."+params.get("action")[0]))
                .append("\n")
                .append(filterHelper.getSelectedPeriodRange(params)).toString();
    }
}
