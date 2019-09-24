package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.*;
import org.openlmis.vaccine.repository.warehouse.ReceiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReceiveService {

    @Autowired
    private ReceiveRepository repository;

    @Autowired
    private ReceiveLineItemService lineItemService;

    @Autowired
    private ReceiveLotService lotService;

    @Autowired
    private PurchaseDocumentService purchaseDocumentService;

    @Transactional
    public void save(Receive receive, Long userId) {

        if (receive.getId() == null) {

            repository.insert(receive);

        }else {
            repository.update(receive);
        }
        List<ReceiveLineItem> receiveLineItems = receive.getReceiveLineItems();

        if(!receiveLineItems.isEmpty()) {

            for (ReceiveLineItem lineItem : receiveLineItems) {

                lineItem.setReceive(receive);
                lineItem.setCreatedBy(userId);
                lineItem.setModifiedBy(userId);
                lineItemService.save(lineItem);
                if (lineItem.isLotFlag()) {
                    if(!lineItem.getReceiveLots().isEmpty())  {

                        for (ReceiveLot lot : lineItem.getReceiveLots()) {
                        lot.setReceiveLineItem(lineItem);
                        lot.setCreatedBy(userId);
                        lot.setModifiedBy(userId);
                        lotService.save(lot);
                    }

                    }
                }
            }
        }
   /*     List<PurchaseDocument> purchaseDocuments = receive.getPurchaseDocuments();
        for(PurchaseDocument document : purchaseDocuments) {
            document.setReceive(receive);
            document.setCreatedBy(userId);
            document.setModifiedBy(userId);
            purchaseDocumentService.save(document);
        }*/

    }

    public List<Receive> getAll() {
       return repository.getAll();
    }

    public Receive getById(Long id) {
        return repository.getById(id);
    }
}
