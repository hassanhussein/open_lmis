package org.openlmis.stockmanagement.service.upload;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.upload.AbstractModelPersistenceHandler;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.service.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class LotPersistenceHandler extends AbstractModelPersistenceHandler {

    private LotService lotService;

    @Autowired
    LotPersistenceHandler(LotService lotService){

        this.lotService = lotService;
    }
    @Override
    protected BaseModel getExisting(BaseModel record) {

        return this.lotService.getByCode((Lot) record);
    }

    @Override
    protected void save(BaseModel record) {
     this.lotService.insertLot((Lot) record);
    }
    @Override
    public String getMessageKey() {
        return "error.duplicate.lot.code";
    }
}
