package org.openlmis.rnr.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatusChange extends BaseModel {

    private Long reportId;

    private RnrStatus status;

    private String userName;

    private String firstName;

    private String lastName;

    private String comment;

    private Date date;

    public ReportStatusChange(MonitoringReport report, RnrStatus statusToSave, Long userId) {
        reportId = report.getId();
        status = statusToSave;
        createdBy = userId;
        modifiedBy = userId;
    }

    public ReportStatusChange(MonitoringReport report, RnrStatus statusToSave, Long userId, String comment) {
        reportId = report.getId();
        status = statusToSave;
        createdBy = userId;
        modifiedBy = userId;
        this.comment = comment;
    }

}
