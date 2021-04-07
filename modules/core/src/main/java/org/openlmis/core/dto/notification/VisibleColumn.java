package org.openlmis.core.dto.notification;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VisibleColumn {

    private String name;
    private String label;
    private Boolean visible;
    private Integer columnWidth;

    public VisibleColumn(String name, String label, Boolean visible, Integer columnWidth) {
        this.name=name;
        this.label =label;
        this.visible =visible;
        this.columnWidth=columnWidth;
    }

}
