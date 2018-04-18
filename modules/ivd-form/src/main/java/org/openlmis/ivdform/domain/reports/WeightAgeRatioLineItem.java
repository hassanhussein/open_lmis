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
public class WeightAgeRatioLineItem extends BaseModel {


    private Boolean skipped = false;

    Long reportId;
    Long ageGroupId;

    Long displayOrder;
    Long maleValue;
    Long femaleValue;
    String ageGroup;
    String category;
    Long categoryId;

    public void copyValuesFrom(WeightAgeRatioLineItem source){
        this.setCategory(source.getCategory());
        this.setAgeGroup(source.getAgeGroup());
        this.setMaleValue(source.getMaleValue());
        this.setFemaleValue(source.getFemaleValue());
        this.setCategoryId(source.getCategoryId());
        this.setAgeGroupId(source.getAgeGroupId());
    }

}
