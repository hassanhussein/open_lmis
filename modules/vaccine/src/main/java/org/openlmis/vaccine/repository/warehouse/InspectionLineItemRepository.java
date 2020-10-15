package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.InspectionFailProblem;
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

                inspectionLotRepository.deleteLotByInspectionLineItem(lot.getInspectionLineItemId());

                if(lot.getPassQuantity()==null){
                    lot.setPassQuantity(lot.getReceivedQuantity());
                }

                inspectionLotRepository.update(lot);
                //System.out.println("passed: "+lot.getVvm());
                if(lot.getVvm()!=null) {
                    for (VVMLots lotVVm : lot.getVvm()) {

                        lotVVm.setInspectionLineItemId(lot.getInspectionLineItemId());
                        lotVVm.setLotNumber(lot.getLotNumber());
                        lotVVm.setPassLocationId(lot.getPassLocationId());
                        lotVVm.setExpiryDate(lot.getExpiryDate());

                        InspectionFailProblem failed = lotVVm.getFailed();
                        lotVVm.setFailQuantity(failed.getQuantity());
                        lotVVm.setFailReason(failed.getReasonId());
                        lotVVm.setFailLocationId(failed.getLocationId());
                        lotVVm.setFailVvmId(failed.getVvmId());

                        // if(failed.getQuantity()!=null){
                        //    lotVVm.setQuantity(lotVVm.getQuantity());
                        //}
                        // System.out.println("Failed:"+failed.getQuantity());
                        //lotVVm.set
                        if (lotVVm.getQuantity() != null) {
                            inspectionLotRepository.updateOrSave(lotVVm);
                        }

                        //System.out.println("passed: "+lotVVm.getQuantity());
                    }
                }
            }
        }
    }


}
