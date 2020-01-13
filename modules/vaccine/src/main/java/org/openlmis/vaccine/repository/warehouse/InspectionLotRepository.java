package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.InspectionLot;
import org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionLotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InspectionLotRepository {

    @Autowired
    private InspectionLotMapper mapper;

    public void update(InspectionLot lot){
        mapper.update(lot);

    }


}
