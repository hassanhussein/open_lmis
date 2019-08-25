package org.openlmis.vaccine.repository.asn;

import org.openlmis.vaccine.domain.asn.Asn;
import org.openlmis.vaccine.domain.asn.PurchaseDocument;
import org.openlmis.vaccine.repository.mapper.asn.PurchaseDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

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
