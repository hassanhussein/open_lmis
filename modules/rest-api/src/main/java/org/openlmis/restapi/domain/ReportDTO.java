package org.openlmis.restapi.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Signature;
import org.openlmis.core.dto.ELMISResponseMessageDTO;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.message.OpenLmisMessage;
import org.openlmis.core.utils.DateUtil;
import org.openlmis.rnr.domain.*;
import org.openlmis.rnr.dto.LineItemDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;
import static java.util.Collections.addAll;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class ReportDTO {

    private List<LineItemDTO> nonFullSupplyProducts;
    private List<LineItemDTO> fullSupplyProducts;

    private String facilityCode;
    private String programCode;
    private String approverName;

    // added for the sdp version
    private Long periodId;
    private Boolean emergency;

    private String clientSubmittedTime;
    private String clientSubmittedNotes;
    private String sourceApplication = null;
    private String status;

    //Added only for GotHOMIS integration
    private String sourceOrderId;

    private Long rnrId;

    public void initializeLineItems(Rnr rnr) {

        fullSupplyProducts = new  ArrayList<>();
        nonFullSupplyProducts = new  ArrayList<>();

        for(RnrLineItem r: rnr.getFullSupplyLineItems()){

         LineItemDTO item = new LineItemDTO();
         item.setRnrId(r.getRnrId());
         item.setBeginningBalance((r.getBeginningBalance()==null)?0:r.getBeginningBalance());
         item.setQuantityReceived(r.getQuantityReceived());
         item.setStockInHand(r.getStockInHand());
         item.setSkipped(r.getSkipped());
         item.setProductCode(r.getProductCode());
         item.setFullSupply(true);
         item.setStockOutDays(r.getStockOutDays());
         item.setLossesAndAdjustments(r.getLossesAndAdjustments());
         item.setProduct(r.getProduct());
         item.setDosesPerDispensingUnit(r.getDosesPerDispensingUnit());

         fullSupplyProducts.add(item);

        }

        for(RnrLineItem r: rnr.getNonFullSupplyLineItems()){

            LineItemDTO item = new LineItemDTO();
            item.setRnrId(r.getRnrId());
            item.setBeginningBalance((r.getBeginningBalance()==null)?0:r.getBeginningBalance());
            item.setQuantityReceived(r.getQuantityReceived());
            item.setStockInHand(r.getStockInHand());
            item.setSkipped(r.getSkipped());
            item.setProductCode(r.getProductCode());
            item.setFullSupply(false);
            item.setStockOutDays(r.getStockOutDays());
            item.setLossesAndAdjustments(r.getLossesAndAdjustments());
            item.setProduct(r.getProduct());
            item.setDosesPerDispensingUnit(r.getDosesPerDispensingUnit());

            nonFullSupplyProducts.add(item);

        }


    }

    public static ReportDTO prepareFeedBack(final Rnr rnr,String sourceApplication) {

        ReportDTO report = new ReportDTO();

        report.setFacilityCode(rnr.getFacility().getCode());
        report.setProgramCode(rnr.getProgram().getCode());
        report.setRnrId(rnr.getId());
        report.setApproverName(null);
        report.setClientSubmittedNotes(null);

        report.setEmergency(rnr.isEmergency());
        report.setSourceApplication(sourceApplication);
        report.setSourceApplication(null);
        report.setPeriodId(rnr.getPeriod().getId());
        report.setSourceOrderId(null);
        report.setStatus(String.valueOf(rnr.getStatus()));

        report.initializeLineItems(rnr);

        return report;

    }

    public void validate() {
        if (isEmpty(facilityCode) || isEmpty(programCode)) {
            throw new DataException("error.mandatory.fields.missing");
        }
    }

//    @JsonIgnore
//    public Rnr getRequisition(Long requisitionId, Long modifiedBy) {
//        Rnr requisition = new Rnr(requisitionId);
//        requisition.setModifiedBy(modifiedBy);
//        requisition.setFullSupplyLineItems(products);
//
//        return requisition;
//    }

}
