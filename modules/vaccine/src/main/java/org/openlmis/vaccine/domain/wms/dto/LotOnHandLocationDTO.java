package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.vaccine.dto.LocationDTO;

@Data
public class LotOnHandLocationDTO extends LotOnHand {

   private Long locationId;

   private LocationDTO location;

}
