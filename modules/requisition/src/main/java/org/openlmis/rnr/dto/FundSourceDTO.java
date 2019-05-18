package org.openlmis.rnr.dto;

import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.rnr.domain.SourceOfFunds;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FundSourceDTO extends BaseModel {

    List<SourceOfFunds> sourceOfFunds;

}
