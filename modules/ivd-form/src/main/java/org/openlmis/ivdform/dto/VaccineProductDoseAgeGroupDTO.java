package org.openlmis.ivdform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.ivdform.domain.VaccineProductDoseAgeGroup;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineProductDoseAgeGroupDTO extends BaseModel {

    private Long productId;

    private String productName;

    private List<VaccineProductDoseAgeGroup> doses;
}
