package org.openlmis.vaccine.dto;

import lombok.Data;
import org.openlmis.stockmanagement.domain.LotOnHand;

import java.util.ArrayList;
import java.util.List;

@Data
public class PendingRequestDTO {

    private Long productId;

    private String product;

    private Long amount;
    private Long id;

    private String gap;

    private List<LotOnHand> given = new ArrayList<>();

    private String productCode;

}
