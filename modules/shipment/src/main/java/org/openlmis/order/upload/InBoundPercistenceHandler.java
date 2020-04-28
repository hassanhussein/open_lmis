package org.openlmis.order.upload;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.order.dto.InBoundDTO;
import org.openlmis.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InBoundPercistenceHandler extends AbstractModelPersistenceHandler {
    @Autowired
    private OrderService orderService;



    @Override
    protected BaseModel getExisting(BaseModel record) {
        return orderService.getByProductAndExpectedDate(((InBoundDTO) record).getProductCode(),((InBoundDTO) record).getTrackingNumber());
    }

    @Override
    protected void save(BaseModel record) {
        orderService.saveInBounds((InBoundDTO) record);
    }

    @Override
    public String getMessageKey() {
        return "error.duplicate.product.code";
    }
}
