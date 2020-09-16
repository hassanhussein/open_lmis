package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.InspectionLineItem;
import org.openlmis.vaccine.domain.wms.InspectionLot;
import org.openlmis.vaccine.domain.wms.VVMLots;
import org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionLineItemMapper;
import org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionLotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InspectionLineItemRepository {

    @Autowired
    private InspectionLineItemMapper mapper;

    @Autowired
    private InspectionLotRepository inspectionLotRepository;


    public void update(InspectionLineItem lineItem) {

        mapper.update(lineItem);

        if (!lineItem.getLots().isEmpty()) {

            for (InspectionLot lot : lineItem.getLots()) {
                inspectionLotRepository.update(lot);
                //System.out.println("passed: "+lot.getVvm());
                for(VVMLots lotVVm : lot.getVvm()){
                    lotVVm.setInspectionLineItemId(lot.getInspectionLineItemId());
                    lotVVm.setLotNumber(lot.getLotNumber());
                    lotVVm.setPassLocationId(lot.getPassLocationId());
                    lotVVm.setExpiryDate(lot.getExpiryDate());


                    inspectionLotRepository.updateOrSave(lotVVm);

                    //System.out.println("passed: "+lotVVm.getQuantity());
                }
            }
        }
    }


}
