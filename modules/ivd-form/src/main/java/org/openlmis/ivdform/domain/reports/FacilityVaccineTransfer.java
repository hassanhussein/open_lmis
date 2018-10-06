package org.openlmis.ivdform.domain.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacilityVaccineTransfer extends BaseModel {

    private Long lineItemId;
    private Long quantity;
    private Long facilityId;

}
