package org.openlmis.vaccine.domain.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.stockmanagement.domain.Lot;

/**
 * Created by chrispinus on 10/22/15.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VaccineDistributionLineItemLot extends BaseModel {

    private Long distributionLineItemId;

    private Long lotId;
    private Long locationId;

    private Lot lot;
    private Long stockCardId;

    private Integer vvmId;

    private Long quantity;

    private String facilityName;

    private Integer vvmStatus;
    private Long packSize;

    private Long qty;
}
