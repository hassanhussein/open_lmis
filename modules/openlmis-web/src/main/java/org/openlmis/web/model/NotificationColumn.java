package org.openlmis.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class NotificationColumn {
    private String name;
    private String label;
    private Boolean visible;
    private Integer columnWidth;

    public NotificationColumn(String name, String label, Boolean visible, Integer columnWidth) {
       this.name=name;
       this.label =label;
       this.visible =visible;
       this.columnWidth=columnWidth;
    }



}
