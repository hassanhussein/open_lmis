package org.openlmis.rnr.domain;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.PatientCategory;
import org.openlmis.core.domain.RegimenCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

/**
 * PatientLineItem represents a patientLineItem belonging to a Rnr.
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(include = NON_EMPTY)
public class PatientLineItem extends LineItem {


    private Long rnrId;
    private String code;
    private String name;
    private Integer fi;
    private Integer firstMonth;
    private Integer secondMonth;

    private Integer thirdMonth;
    private Integer fourthMonth;
    private Integer fifthMonth;

    private Integer sixthMonth;
    private Integer seventhMonth;
    private Integer eighthMonth;

    private Integer ninthMonth;
    private Integer tenthMonth;
    private Integer eleventhMonth;
    private Integer twelfthMonth;

    private PatientCategory category;
    private Integer patientDisplayOrder;


    private Long createdBy;
    private Long modifiedBy;


    private static Logger logger = LoggerFactory.getLogger(PatientLineItem.class);


    public PatientLineItem(Long rnrId, PatientCategory category, Long createdBy, Long modifiedBy) {
        this.rnrId = rnrId;
        this.category = category;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }


    @Override
    public boolean compareCategory(LineItem lineItem) {
        if (this.category.getName().equals(((PatientLineItem) lineItem).getCategory().getName())) return true;
        return false;
    }

    @Override
    public String getCategoryName() {
        return this.category.getName();
    }

    @Override
    public String getValue(String columnName) throws NoSuchFieldException, IllegalAccessException {
        Field field = PatientLineItem.class.getDeclaredField(columnName);
        field.setAccessible(true);
        Object fieldValue = field.get(this);
        String value = (fieldValue == null) ? "" : fieldValue.toString();
        return value;
    }

    @Override
    public boolean isRnrLineItem() {
        return false;
    }
}
