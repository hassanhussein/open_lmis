package org.openlmis.vaccine.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openlmis.core.domain.BaseModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class VaccineDistributionNotification extends BaseModel {

    private Long facilityId;
    private String toFacility;
    private String fromFacility;
    private Boolean sent;
    private Long geographicZoneId;
    private Boolean allowedToSend;
    private Long distributionId;

}
