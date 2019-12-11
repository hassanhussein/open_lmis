package org.openlmis.core.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

    Class<? extends BaseModel> entity;
    AuditAction action;
    AuditIdentityKey identityKey;
    String identityValue;
    String oldValue;
    String newValue;
    Long userId;
    String userFullName;

    public String getActionFullName() {
        return entity.getSimpleName().concat(".").concat(action.toString());
    }
}