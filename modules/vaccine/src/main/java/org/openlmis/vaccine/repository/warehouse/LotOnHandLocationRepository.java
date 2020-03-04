package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.dto.PutAwayLineItemDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.LotOnHandLocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LotOnHandLocationRepository {

    @Autowired
    private LotOnHandLocationMapper mapper;

    public Integer insert(LotOnHandLocation location) {
       return mapper.insert(location);
    }

    public void update(LotOnHandLocation location) {
        mapper.update(location);
    }

    public Integer insertPutAwayDetails(PutAwayLineItemDTO item) {
       return mapper.savePutAwayDetails(item);
    }

    public void deleteExistingPutAway(Long inspectionId) {
        mapper.deleteExistingPutAway(inspectionId);
    }
}
