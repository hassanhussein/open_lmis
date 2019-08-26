package org.openlmis.vaccine.service.wms;

import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.AsnLineItem;
import org.openlmis.vaccine.domain.wms.AsnLot;
import org.openlmis.vaccine.domain.wms.PurchaseDocument;
import org.openlmis.vaccine.repository.wms.AsnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AsnService {

    @Autowired
    AsnRepository repository;

    @Autowired
    AsnLineItemService asnLineItemService;

    @Autowired
    AsnLotService asnLotService;

    @Autowired
    PurchaseDocumentService purchaseDocumentService;

    @Transactional
    public void save(Asn asn, Long userId) {

        if (asn.getId() == null) {

            repository.insert(asn);

        }else {
            repository.update(asn);
        }

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
        }

    }

    public Asn getById (Long id) {

        return repository.getById(id);

    }
    public List<Asn> getAll(){

        return  repository.getAll();
    }
}
