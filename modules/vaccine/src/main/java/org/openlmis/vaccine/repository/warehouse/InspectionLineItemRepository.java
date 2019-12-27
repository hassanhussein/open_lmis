package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.InspectionLineItem;
import org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionLineItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InspectionLineItemRepository {

    @Autowired
    private InspectionLineItemMapper mapper;


    public void update(InspectionLineItem lineItem){
        mapper.update(lineItem);
    }



}
