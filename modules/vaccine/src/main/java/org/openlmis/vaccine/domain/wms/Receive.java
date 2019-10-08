package org.openlmis.vaccine.domain.wms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.core.serializer.DateDeserializer;

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

    private SupplyPartner supplyPartner;
    private SupplyPartner supplier;

    private List<ReceiveLineItem> receiveLineItems;

    private Port port;

    public void copyAsnValues(Asn asn) {

      Receive res = new Receive();
      res.setId(null);
      res.setAsnId(asn.getId());
      res.setPoNumber(asn.getPonumber());
      res.setPoDate(asn.getAsndate());
      res.setSupplierId(asn.getSupplier().getId());
      res.setReceiveDate(asn.getAsndate());
      res.setBlawBnumber(asn.getBlawbnumber());
      res.setCountry("Tanzania");
      res.setFlightVesselNumber(asn.getFlightvesselnumber());
      res.setPortOfArrival(asn.getPortofarrival());
      res.setExpectedArrivalDate(asn.getExpectedarrivaldate());
      res.setActualArrivalDate(asn.getExpecteddeliverydate());
      res.setClearingAgent(asn.getClearingagent());
      res.setShippingAgent(null);
      res.setStatus("DRAFT");
      res.setNote(asn.getNote());
      res.setNoteToSupplier(asn.getNote());
      res.setDescription(null);
      res.setCreatedBy(asn.getCreatedBy());
      res.setModifiedBy(asn.getModifiedBy());
      res.setIsForeignProcurement(true);
      res.setSupplyPartner(asn.getSupplier());
      res.setPurchaseOrderId(null);

    /*  for(AsnLineItem asnLineItem: asn.getAsnLineItems()){

          ReceiveLineItem receiveLineItem = new ReceiveLineItem();
          receiveLineItem.setReceive(res);
          receiveLineItem.set

      }
*/

    }

    // private PurchaseDocument purchaseDocuments;

}
