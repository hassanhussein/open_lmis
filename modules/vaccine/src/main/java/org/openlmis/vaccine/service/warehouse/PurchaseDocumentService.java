package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.PurchaseDocument;
import org.openlmis.vaccine.repository.warehouse.PurchaseDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseDocumentService {

    @Autowired
    PurchaseDocumentRepository repository;

    public void save(PurchaseDocument purchaseDocument) {

        if (purchaseDocument.getId() == null) {

            repository.insert(purchaseDocument);
        }else {
            repository.update(purchaseDocument);
        }

    }

    public PurchaseDocument getById (Long id) {

        return repository.getById(id);

    }

    public List<PurchaseDocument> getByAsnId (Long id) {

        return repository.getByAsnId(id);

    }
    public List<PurchaseDocument> getAll(){

        return  repository.getAll();
    }
}
