package org.openlmis.report.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.model.ResultRow;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemFillRateReport implements ResultRow {

    private String zoneName;
    private String  district;
    private String region;
    private String facilityName;
    private String productCode;
    private String product;
    private String orderedDate;
    private String shippedDate;
    private String receivedDate;
    private Integer orderedQuantity;
    private Integer quantityReceived;
    private Integer itemFillRate;

    private String msdProductCode;

    private String eLMISProductCode;

    private Integer quantityApproved;

    private Integer quantityShipped;

    private String msdZone;

    private String orderNumber;

    private boolean emergency;

    private Integer rnrId;

    private Integer approvedQuantity;

    private Integer receivedQuantity;

    private String invoiceNumber;

    private Date invoiceDate;

    private String periodName;


}
