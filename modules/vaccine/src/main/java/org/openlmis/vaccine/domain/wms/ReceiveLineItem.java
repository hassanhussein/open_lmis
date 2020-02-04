package org.openlmis.vaccine.domain.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Product;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveLineItem extends BaseModel {

    private Long receiveId;
    private Long productId;
    private Date expiryDate;
    private Date manufacturingDate;
    private Integer quantityCounted;
    private Double unitPrice;
    private Integer boxCounted;
    private boolean lotFlag;
    private Receive receive;
    private List<ReceiveLot> receiveLots;
    private List<Product> productList;

    private Product product;

    private String receiveNumber;

}
