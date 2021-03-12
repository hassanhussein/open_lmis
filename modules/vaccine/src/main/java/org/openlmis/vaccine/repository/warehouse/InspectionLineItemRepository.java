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
            System.out.println("ID:"+lineItem.getId());
            inspectionLotRepository.deleteLotByInspectionLineItem(lineItem.getId());

            for (InspectionLot lot : lineItem.getLots()) {


                if(lot.getPassQuantity()==null){
                    lot.setPassQuantity(lot.getReceivedQuantity());
                }

               // System.out.println("passed: "+lot);
                if(lot.getVvm()!=null&&lot.getVvm().size()>0) {
                    if(lot.getVvm().size()>0&&lot.getVvm().get(0).getQuantity()!=null&&lot.getVvm().get(0).getQuantity()>0) {
                        for (VVMLots lotVVm : lot.getVvm()) {

                            lotVVm.setInspectionLineItemId(lot.getInspectionLineItemId());
                            lotVVm.setLotNumber(lot.getLotNumber());
                            lotVVm.setPassLocationId(lot.getPassLocationId());
                            lotVVm.setExpiryDate(lot.getExpiryDate());
                            lotVVm.setBoxNumber(lot.getBoxNumber());
                            lotVVm.setReceivedQuantity(lot.getReceivedQuantity());

                            try {
                                InspectionFailProblem failed = lotVVm.getFailed();
                                lotVVm.setFailQuantity(failed.getQuantity());
                                lotVVm.setFailReason(failed.getReasonId());
                                lotVVm.setFailLocationId(failed.getLocationId());
                                lotVVm.setFailVvmId(failed.getVvmId());
                            }catch (Exception e){
                                //e.printStackTrace();
                            }

                            // if(failed.getQuantity()!=null){
                            //    lotVVm.setQuantity(lotVVm.getQuantity());
                            //}
                            // System.out.println("Failed:"+failed.getQuantity());
                            //lotVVm.set
                            if (lotVVm.getQuantity() != null) {
                                lotVVm.setPassQuantity(lotVVm.getQuantity());
                                inspectionLotRepository.updateOrSave(lotVVm);
                            } /*else {
                                inspectionLotRepository.save(lot);

                            }
*/
                            //System.out.println("passed: "+lotVVm.getQuantity());
                        }
                    }else{
                        inspectionLotRepository.save(lot);
                    }
                }else{
                    inspectionLotRepository.save(lot);

                }
            }
        }
    }


}
