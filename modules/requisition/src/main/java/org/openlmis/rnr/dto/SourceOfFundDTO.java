package org.openlmis.rnr.dto;

import lombok.*;
import org.openlmis.core.domain.BaseModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SourceOfFundDTO extends BaseModel {

    private String code;

    private String name;

    private Integer displayOrder;

}
