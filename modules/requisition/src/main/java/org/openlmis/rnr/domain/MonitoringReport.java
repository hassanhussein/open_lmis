package org.openlmis.rnr.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.*;
import org.openlmis.core.serializer.DateDeserializer;

import java.util.*;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_NULL;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class MonitoringReport extends BaseModel {

    private Long programId;

    private Long districtId;

    private String nameOfHidTu;

    private Long numberOfHidTu;

    private Integer numberOfCumulativeCases;

    private Integer patientOnTreatment;

    private Integer numberOfStaff;

    @JsonDeserialize(using = DateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date reportedDate;

    private String status;

    private Long supervisoryNodeId;

    private List<MonitoringReportLineItem> lineItems;

    private List<ReportStatusChange> reportStatusChanges;

    private GeographicZone geographicZone;

    private Program program;

    public void initializeLineItems(List<ProgramProduct> programProducts, MonitoringReport previousReport, Boolean defaultFieldsToZero) {

        lineItems = new ArrayList<>();
   /*     Map<String, MonitoringReportLineItem> previousLineItemMap = new HashMap<>();
        if (previousReport != null) {
            for (MonitoringReportLineItem lineItem : previousReport.getLineItems()) {
                previousLineItemMap.put(lineItem.getProductCode(), lineItem);
            }
        }*/
        for (ProgramProduct pp : programProducts) {
            if (pp.getIsCovidIndicator()) {
                MonitoringReportLineItem item = new MonitoringReportLineItem();

                item.setReportId(id);

                item.setProductId(pp.getProduct().getId());
                item.setProductCode(pp.getProduct().getCode());
                item.setProduct(pp.getProduct().getPrimaryName());
                item.setProductCategory(pp.getProductCategory().getName());
                item.setDispensingUnit(pp.getProduct().getDosageUnit().getCode());
                item.setPackSize(pp.getProduct().getPackSize());
                item.setProductCategoryId(pp.getProductCategory().getId());

                item.setStockOnHand(null);
                item.setQuantityRequested(null);

                lineItems.add(item);
            }
        }

    }




}
