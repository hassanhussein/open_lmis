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
public class TtStatusLineItem extends BaseModel {

    private Boolean skipped = false;

    Long reportId;
    Long maleValue;
    Long femaleValue;
    Long categoryId;
    String category;

    public void copyValuesFrom(TtStatusLineItem source) {

        this.setMaleValue(source.getMaleValue());
        this.setFemaleValue(source.getFemaleValue());

    }


}
