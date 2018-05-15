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
public class CoverageLineItemAgeGroup extends BaseModel {

    String name;
    String description;
    Integer displayOrder;
    Long ageCategoryId;

}
