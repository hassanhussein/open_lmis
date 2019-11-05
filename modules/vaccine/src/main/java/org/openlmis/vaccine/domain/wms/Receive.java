package org.openlmis.vaccine.domain.wms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.core.serializer.DateDeserializer;
import org.openlmis.vaccine.dto.CurrencyDTO;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Receive extends BaseModel {

    private Integer purchaseOrderId;
    private String poNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date poDate;

    private Long asnId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date receiveDate;
    private String blawBnumber;
    private String country;
    private String flightVesselNumber;
    private Integer portOfArrival;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date expectedArrivalDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date actualArrivalDate;
    private String clearingAgent;
    private String shippingAgent;
    private String status;
    private String note;
    private String noteToSupplier;
    private String description;
    private Boolean isForeignProcurement;

    private Long supplierId;

   // private SupplyPartner supplyPartner;

    private SupplyPartner supplier;

    private List<ReceiveLineItem> receiveLineItems;

    private String asnNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date asnReceiveDate;

    private Port port;

    private Asn asn;

    private Long facilityId;

    private  Long currencyId;

    private CurrencyDTO currency;

    // private PurchaseDocument purchaseDocuments;

}
