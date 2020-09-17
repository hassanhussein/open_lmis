package org.openlmis.vaccine.domain.wms;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InspectionFailProblem extends BaseModel {

    private Long vvmId;
    private Long locationId;
    private Long quantity;
    private Long reasonId;
}
