package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.InspectionLot;
import org.openlmis.vaccine.domain.wms.VVMLots;
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
    public void deleteLotByInspectionLineItem(Long inspectionLotId){
        mapper.deleteLotByInspectionLineItem(inspectionLotId);

    }

    public void updateOrSave(VVMLots lots){
        mapper.updateOrSave(lots);
    }
    public void save(InspectionLot lots){
        mapper.save(lots);
    }


}
