package org.openlmis.core.dto;

import lombok.Data;

@Data
public class ConflictDTO {

    String affectedObject;
    String affectedValue;
}
