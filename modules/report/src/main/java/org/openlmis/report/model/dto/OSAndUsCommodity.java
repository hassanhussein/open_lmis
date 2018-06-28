package org.openlmis.report.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OSAndUsCommodity {
    private String regionName;
    private String product;
    private String productCode;
    private Long count;
    private String status;
    private String rowNum;
}
