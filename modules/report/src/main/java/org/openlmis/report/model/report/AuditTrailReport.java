package org.openlmis.report.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.service.MessageService;
import org.openlmis.report.model.ResultRow;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditTrailReport implements ResultRow {

    String action;
    String actionPerformedBy;
    String identity;
    String identityValue;
    String oldValue;
    String newValue;
    Date createdDate;

    public String getAction() {
        return MessageService.getRequestInstance().message(action);
    }
}
