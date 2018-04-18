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
public class PmtctLineItem extends BaseModel {

    private Boolean skipped = false;
    private Boolean transferredToCtc = false;

    Long reportId;
    Long categoryId;
    String category;
    Long maleValue;
    Long femaleValue;

    public void copyValuesFrom(PmtctLineItem source) {
        this.setTransferredToCtc(source.getTransferredToCtc());
        this.setCategory(source.getCategory());
        this.setCategoryId(source.getCategoryId());
        this.setMaleValue(source.getMaleValue());
        this.setFemaleValue(source.getFemaleValue());

    }


}
