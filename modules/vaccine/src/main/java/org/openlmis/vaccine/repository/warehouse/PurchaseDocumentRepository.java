package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.Document;
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

    public List<PurchaseDocument> getByAsnId (Long id){
        return mapper.getByAsnId(id);
    }

    public List<PurchaseDocument> getAll(){
        return mapper.getAllPurchaseDocuments();
    }


    public Integer insertDocument(Document document) {
        return mapper.insertDocument(document);
    }

    public void updateDocument(Document document) {
        mapper.updateDocument(document);
    }

    public List<Document> getByASNCode (String code){
        return mapper.getByASNCode(code);
    }

    public Document getByFileLocation (String code){
        return mapper.getByFileLocation(code);
    }

    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    public Document getDocumentById(Long id) {
        return mapper.getDocumentById(id);
    }

    public PurchaseDocument getDocumentByFileLocation(String code) {
        return mapper.getDocumentByFileLocation(code);
    }

    public void deleteByAsnNumber(String asnNumber) {
        mapper.deleteByAsnNumber(asnNumber);
    }
}
