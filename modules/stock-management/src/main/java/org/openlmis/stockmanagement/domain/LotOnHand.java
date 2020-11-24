package org.openlmis.stockmanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.serializer.DateDeserializer;
import org.openlmis.stockmanagement.util.LatestSyncedStrategy;
import org.openlmis.stockmanagement.util.StockCardEntryKVReduceStrategy;
import org.openlmis.stockmanagement.util.StockManagementUtils;

import java.util.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LotOnHand extends BaseModel {

  private String gap;
  @JsonIgnore
  private StockCard stockCard;

  private Lot lot;
  private Long id;

  private Long lotId;
  private Long locationId;
  private Long quantityOnHand,totalQuantity;
  private Long stockCardId;
  private Long packSize;

  private Long quantity;
  private Long distributionLineItemId;

  private Long qty;

  private Long vvmId;

  private String transferLogs;

  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
  @JsonDeserialize(using=DateDeserializer.class)
  private Date effectiveDate;

  @JsonIgnore
  private List<StockCardEntryKV> keyValues;

  @JsonIgnore
  private StockCardEntryKVReduceStrategy strategy;

  private LotOnHand(Lot lot, StockCard stockCard) {
    Objects.requireNonNull(lot);
    Objects.requireNonNull(stockCard);
    this.lot = lot;
    this.lotId = lot.getId();
    this.stockCard = stockCard;
    this.quantityOnHand = 0L;
    this.effectiveDate = new Date();
    this.keyValues = new ArrayList<>();
    this.strategy = null;
    this.vvmId = lot.getVvmId();
  }

  /**
   * This method creates a zeroed lot on hand. If lot or stockCard are null, it will throw an exception, rather than
   * returning null.
   *
   * @param lot
   * @param stockCard
   * @return
   */
  public static final LotOnHand createZeroedLotOnHand(Lot lot, StockCard stockCard) {
    return new LotOnHand(lot, stockCard);
  }

  public Map<String, String> getCustomProps() {
    try {
      if (null == strategy) strategy = new LatestSyncedStrategy();

      Map<String, String> customProps = StockManagementUtils.getKeyValueAggregate(keyValues, strategy);

      if (customProps.isEmpty()) {
        customProps.put("vvmstatus", "1");
      }

      return customProps.isEmpty() ? null : customProps;
    }catch (Exception e){
      e.printStackTrace();
      return null;
    }
  }

  //public Map<String, String> customProps;

  public void addToQuantityOnHand(long quantity) {
    this.quantityOnHand += quantity;
  }
}
