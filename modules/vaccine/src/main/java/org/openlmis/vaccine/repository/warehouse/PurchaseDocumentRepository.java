package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.PurchaseDocument;
import org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseDocumentRepository {

    @Autowired
    PurchaseDocumentMapper mapper;

    public Integer insert(PurchaseDocument purchaseDocument) {
        return mapper.insert(purchaseDocument);
    }

    public void update(PurchaseDocument purchaseDocument) {
        mapper.update(purchaseDocument);
    }

    public PurchaseDocument getById (Long id){
        return mapper.getById(id);
    }

    public List<PurchaseDocument> getAll(){
        return mapper.getAllPurchaseDocuments();
    }
}
