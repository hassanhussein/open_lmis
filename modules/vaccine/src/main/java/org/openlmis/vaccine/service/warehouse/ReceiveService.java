package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.*;
import org.openlmis.vaccine.repository.warehouse.ReceiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceiveService {

    @Autowired
    private ReceiveRepository repository;

    @Transactional
    public void save(Receive receive, Long userId) {

        if (receive.getId() == null) {

            repository.insert(receive);

        }else {
            repository.update(receive);
        }
   /*
        List<AsnLineItem> asnLineItems = asn.getAsnLineItems();

        for(AsnLineItem lineItem : asnLineItems){

            lineItem.setAsn(asn);
            lineItem.setCreatedBy(userId);
            lineItem.setModifiedBy(userId);
            asnLineItemService.save(lineItem);
            if(lineItem.isLotflag()) {
                for (AsnLot asnLot : lineItem.getAsnLots()) {
                    asnLot.setAsnLineItem(lineItem);
                    asnLot.setCreatedBy(userId);
                    asnLot.setModifiedBy(userId);
                    asnLotService.save(asnLot);
                }
            }
        }

        List<PurchaseDocument> purchaseDocuments = asn.getPurchaseDocuments();
        for(PurchaseDocument document : purchaseDocuments){
            document.setAsn(asn);
            document.setCreatedBy(userId);
            document.setModifiedBy(userId);
            purchaseDocumentService.save(document);
        }*/

    }
}
