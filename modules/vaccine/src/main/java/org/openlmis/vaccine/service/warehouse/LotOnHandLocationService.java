package org.openlmis.vaccine.service.warehouse;

import org.openlmis.stockmanagement.service.StockCardService;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.dto.PutAwayLineItemDTO;
import org.openlmis.vaccine.repository.warehouse.LotOnHandLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotOnHandLocationService {

    @Autowired
    private LotOnHandLocationRepository repository;

    @Autowired
    private StockCardService stockCardService;


    public void save(LotOnHandLocation location){

        if(location.getId() == null){
           repository.insert(location);
        } else {
            repository.update(location);
        }
    }


    public PutAwayLineItemDTO savePutAwayDetails(List<PutAwayLineItemDTO> items,Long userId) {

       repository.deleteExistingPutAway(items.get(0).getInspectionId());

       for(PutAwayLineItemDTO dto :items) {
           dto.setCreatedBy(userId);
           repository.insertPutAwayDetails(dto);

       }

       return items.get(0);
    }
}
