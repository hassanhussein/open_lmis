package org.openlmis.core.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.openlmis.core.domain.BaseModel;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "invoiceNumber",
        "zone",
        "soldTo",
        "soldToCustomerName",
        "shipTo",
        "shipToCustomerName",
        "msdOrderNumber",
        "elmisOrderNumber",
        "invoiceDate",
        "shipVia",
        "salesCategory",
        "paymentTerms",
        "salesPerson",
        "comment",
        "invoiceLineTotal",
        "invoicelineDiscount",
        "invoiceMiscellanousCharges",
        "invoiceTotal",
        "legalNumber",
        "fullFilledItems",
        "stockOutItems",
        "inSufficientFundingItems",
        "rationingItems",
        "closeToExpireItems",
        "phasedOutItems"
})
public class StockOutNotificationDTO extends BaseModel {

    @JsonProperty("invoiceNumber")
    public String invoiceNumber;
    @JsonProperty("zone")
    public String zone;
    @JsonProperty("soldTo")
    public String soldTo;
    @JsonProperty("soldToCustomerName")
    public String soldToCustomerName;
    @JsonProperty("shipTo")
    public String shipTo;
    @JsonProperty("shipToCustomerName")
    public String shipToCustomerName;
    @JsonProperty("msdOrderNumber")
    public String msdOrderNumber;
    @JsonProperty("elmisOrderNumber")
    public String elmisOrderNumber;
    @JsonProperty("invoiceDate")
    public String invoiceDate;
    @JsonProperty("shipVia")
    public String shipVia;
    @JsonProperty("salesCategory")
    public String salesCategory;
    @JsonProperty("paymentTerms")
    public String paymentTerms;
    @JsonProperty("salesPerson")
    public String salesPerson;
    @JsonProperty("comment")
    public String comment;
    @JsonProperty("invoiceLineTotal")
    public String invoiceLineTotal;
    @JsonProperty("invoicelineDiscount")
    public String invoiceLineDiscount;
    @JsonProperty("invoiceMiscellanousCharges")
    public String invoiceMiscellanousCharges;
    @JsonProperty("invoiceTotal")
    public String invoiceTotal;
    @JsonProperty("legalNumber")
    public String legalNumber;
    @JsonProperty("fullFilledItems")
    public List<FullFilledItem> fullFilledItems = null;
    @JsonProperty("stockOutItems")
    public List<StockOutItem> stockOutItems = null;
    @JsonProperty("inSufficientFundingItems")
    public List<InSufficientFundingItem> inSufficientFundingItems = null;
    @JsonProperty("rationingItems")
    public List<RationingItem> rationingItems = null;
    @JsonProperty("closeToExpireItems")
    public List<CloseToExpireItem> closeToExpireItems = null;
    @JsonProperty("phasedOutItems")
    public List<PhasedOutItem> phasedOutItems = null;

}
