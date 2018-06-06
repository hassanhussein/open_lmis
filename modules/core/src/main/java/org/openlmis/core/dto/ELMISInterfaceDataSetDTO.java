package org.openlmis.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ELMISInterfaceDataSetDTO{
    private String dataElement;
    private String categoryOptionCombo;
    private Long period;
    private String orgUnit;
    private Long value;
}
