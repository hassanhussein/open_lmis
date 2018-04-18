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
public class BreastFeedingLineItem extends BaseModel {


    private Boolean skipped = false;

    Long reportId;

    Long displayOrder;
    Long maleValue;
    Long femaleValue;
    Long AgeGroupId;
    String ageGroup;
    Long categoryId;
    String category;

    public void copyValuesFrom(BreastFeedingLineItem source){
        this.setCategory(source.getCategory());
        this.setCategoryId(source.getCategoryId());
        this.setAgeGroupId(source.getAgeGroupId());
        this.setAgeGroup(source.getAgeGroup());
        this.setMaleValue(source.getMaleValue());
        this.setFemaleValue(source.getFemaleValue());
    }
}
