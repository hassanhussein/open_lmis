package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.service.MessageService;
import org.openlmis.report.mapper.EmergencyRnrAggregateByGeozoneMapper;
import org.openlmis.report.model.ReportParameter;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.EmergencyRnrAggregateByGeozoneReportParam;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class EmergencyRnrAggregateByGeozoneDataProvider extends ReportDataProvider {

    @Autowired
    private EmergencyRnrAggregateByGeozoneMapper reportMapper;

    @Autowired
    private SelectedFilterHelper filterHelper;

    @Autowired
    private MessageService messageService;

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria,
                                                   Map<String, String[]> sortCriteria,
                                                   int page, int pageSize) {
        RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
        return reportMapper.getEmergencyRnrAggregate(getReportFilterData(filterCriteria), this.getUserId());
    }

    public ReportParameter getReportFilterData(Map<String, String[]> filterCriteria) {
        EmergencyRnrAggregateByGeozoneReportParam reportParam = ParameterAdaptor.parse(filterCriteria,
                EmergencyRnrAggregateByGeozoneReportParam.class);
        return reportParam;
    }

    @Override
    public int getReportTotalCount(Map<String, String[]> filterCriteria) {
        return reportMapper.getEmergencyRnrAggregateCount(getReportFilterData(filterCriteria));
    }

//    @Override
//    public String getFilterSummary(Map<String, String[]> params) {
//        return (new StringBuffer()).append(messageService.message("label.audit.actions"))
//                .append(" : ")
//                .append((params.get("action")[0]).isEmpty() ? messageService.message("label.all") :
//                        messageService.message("audit.label."+params.get("action")[0]))
//                .append("\n")
//                .append(filterHelper.getSelectedPeriodRange(params)).toString();
//    }
}
