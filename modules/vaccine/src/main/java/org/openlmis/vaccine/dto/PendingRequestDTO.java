package org.openlmis.vaccine.dto;

import lombok.Data;
import org.openlmis.stockmanagement.domain.LotOnHand;

import java.util.List;

@Data
public class PendingRequestDTO {

    private Long productId;

    private String product;

    private Integer amount;

    private String gap;

    List<LotOnHand> given;

}
