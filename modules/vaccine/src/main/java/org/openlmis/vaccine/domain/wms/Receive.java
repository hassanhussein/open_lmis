package org.openlmis.vaccine.domain.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.SupplyPartner;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receive extends BaseModel {

    private Integer purchaseOrderId;
    private String poNumber;
    private Date poDate;
    private Integer supplierId;
    private Integer asnId;
    private Date receiveDate;
    private String blawBnumber;
    private String country;
    private String flightVesselNumber;
    private Integer portOfArrival;
    private Date expectedArrivalDate;
    private Date actualArrivalDate;
    private String clearingAgent;
    private String shippingAgent;
    private String status;
    private String note;
    private String noteToSupplier;
    private String description;
    private Boolean isForeignProcurement;

    private SupplyPartner supplyPartner;

}
