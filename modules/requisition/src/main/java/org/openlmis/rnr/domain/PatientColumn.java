package org.openlmis.rnr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PatientColumn extends Column{

    private Long programId;

    private String dataType;

    private Integer displayOrder;

    private Boolean fixed;


    public PatientColumn(Long programId, String name, String label, String dataType, Boolean visible, Boolean fixed, Integer dispOrder, Long createdBy) {
        super(name, label, visible);
        this.programId = programId;
        this.dataType = dataType;
        this.createdBy = createdBy;
        this.displayOrder = dispOrder;
        this.fixed = fixed;
    }

    @Override
    public Integer getColumnWidth() {
        if (this.name.equals("remarks")) {
            return 80;
        }
        return 40;
    }

    @Override
    public ColumnType getColumnType() {
        if (this.getName().equals("name") || this.getName().equals("code") || this.getName().equals("remarks")) {
            return ColumnType.TEXT;
        } else if(this.getName().equals("skipped")) {
            return ColumnType.BOOLEAN;
        }else {
            return ColumnType.NUMERIC;
        }
    }
}
