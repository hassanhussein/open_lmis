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
public class ChildVisitLineItem extends BaseModel {

    private Boolean skipped = false;

    Long reportId;
    Long childVisitAgeGroupId;

    Long maleValue;
    Long femaleValue;
    String ageGroup;

    public void copyValuesFrom(ChildVisitLineItem source){
        this.setAgeGroup(source.getAgeGroup());
        this.setMaleValue(source.getMaleValue());
        this.setFemaleValue(source.getFemaleValue());
    }

}
