package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.DocumentType;
import org.openlmis.vaccine.repository.warehouse.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTypeService {

    @Autowired
    private DocumentTypeRepository repository;

    public void save(DocumentType documentType) {

        if (documentType.getId() == null) {

            repository.insert(documentType);
        }else {
            repository.update(documentType);
        }

    }

    public DocumentType getById (Long id) {

       return repository.getById(id);

    }
    public List<DocumentType> getAll(){

        return  repository.getAll();
    }

    public DocumentType getByName(String documentType) {
        return repository.getByName(documentType);
    }
}
