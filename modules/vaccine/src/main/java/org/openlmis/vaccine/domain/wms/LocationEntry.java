package org.openlmis.vaccine.domain.wms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.stockmanagement.domain.StockCardEntryKV;
import org.openlmis.stockmanagement.domain.StockCardEntryType;

import java.util.Date;
import java.util.List;

@Data
public class LocationEntry extends BaseModel {

   private Long lotOnHandId;
   private Long locationId;
   private StockCardEntryType type;
   private Integer quantity;
   private Date occurred;
   private Long vvmId;
   private Long stockCardId;
   private Long lotId;


   @JsonIgnore
   private List<StockCardEntryKV> keyValues;
}
