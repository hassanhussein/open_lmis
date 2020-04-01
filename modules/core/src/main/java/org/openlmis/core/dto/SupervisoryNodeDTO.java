package org.openlmis.core.dto;

import lombok.Data;

@Data
public class SupervisoryNodeDTO {

    private String code;

    private Long id;

    private String name;

    private Long facilityId;
}
