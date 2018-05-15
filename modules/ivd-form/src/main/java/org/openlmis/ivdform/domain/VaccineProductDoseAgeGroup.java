package org.openlmis.ivdform.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineProductDoseAgeGroup extends BaseModel {

    Long doseId;
    Long programId;
    Long productId;
    String productName;
    Long displayOrder;
    Long ageGroupId;
    String ageGroupName;


}
