package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.Document;
import org.openlmis.vaccine.domain.wms.PurchaseDocument;
import org.openlmis.vaccine.repository.warehouse.PurchaseDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;

@Service
public class PurchaseDocumentService {

    @Autowired
    PurchaseDocumentRepository repository;

    public void save(Asn asn, Long userId) {
        deleteByAsnNumber(asn.getAsnnumber());
        for (Document document : getByASNCode(asn.getAsnnumber())) {

            PurchaseDocument doc = new PurchaseDocument();
            doc.setCreatedBy(userId);
            doc.setModifiedBy(userId);
            doc.setAsn(asn);
            doc.setDocumentType(document.getDocumentType());
            doc.setFileLocation(document.getFileLocation());
            doc.setReceive(null);
            doc.setAsnNumber(document.getAsnNumber());

            if (getDocumentByFileLocation(doc.getFileLocation()) == null) {

                repository.insert(doc);
            } else {

                repository.update(doc);
            }

        }


    }

    public PurchaseDocument getById(Long id) {

        return repository.getById(id);

    }

    public List<PurchaseDocument> getByAsnId(Long id) {

        return repository.getByAsnId(id);

    }

    public List<PurchaseDocument> getAll() {

        return repository.getAll();
    }

    public void saveDocument(Document document) {

        Document fileLocation = getByFileLocation(document.getFileLocation());

        if (fileLocation == null) {

            repository.insertDocument(document);
        } else {
            repository.updateDocument(document);
        }

    }

    public List<Document> getByASNCode(String code) {

        return repository.getByASNCode(code);

    }

    public Document getByFileLocation(String code) {

        return repository.getByFileLocation(code);

    }

    public List<Document> deleteById(Long id, String code) {
        repository.deleteById(id);
        return getByASNCode(code);

    }

    private PurchaseDocument getDocumentByFileLocation(String code) {

        return repository.getDocumentByFileLocation(code);

    }

    private void deleteByAsnNumber(String asnNumber) {

        repository.deleteByAsnNumber(asnNumber);

    }
}
