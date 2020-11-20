package org.openlmis.stockmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.openlmis.core.domain.BaseModel;

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
   private String inputType,transferLogs;
   private Integer packSize;
   private Lot lot;



   @JsonIgnore
   private List<StockCardEntryKV> keyValues;
}
