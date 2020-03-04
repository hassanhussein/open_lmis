package org.openlmis.vaccine.domain.wms;

import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.vaccine.dto.LocationDTO;

@Data
public class LotOnHandLocation extends BaseModel {

   private Long locationId;

   private LotOnHand lotOnHand;

   private Long lotOnHandId;

   private Integer quantityOnHand;

   private LocationDTO location;

}
