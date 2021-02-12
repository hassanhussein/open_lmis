package org.openlmis.core.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Facility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockOutNotificationDTO extends BaseModel {
    //start new
    public String quoteNumber;
    public String customerId;
    public String customerName;
    public String hfrCode;
    public String elmisOrderNumber;
    public Date noticationDate;
    public String zone;
    public String comment;

    @JsonProperty("rationingItems")
    public List<RationingItem> rationingItems = new ArrayList<>();
    @JsonProperty("stockOutItems")
    public List<StockOutItem> stockOutItems = new ArrayList<>();
    @JsonProperty("inSufficientFundingItems")
    public List<InSufficientFundingItem> inSufficientFundingItems = new ArrayList<>();

    //end new
    @JsonProperty("fullFilledItems")
    public List<FullFilledItem> fullFilledItems = new ArrayList<>();
    @JsonProperty("closeToExpireItems")
    public List<CloseToExpireItem> closeToExpireItems = new ArrayList<>();
    @JsonProperty("phasedOutItems")
    public List<PhasedOutItem> phasedOutItems = new ArrayList<>();

}
