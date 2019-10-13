package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.DocumentType;
import org.openlmis.vaccine.repository.mapper.warehouse.asn.DocumentTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentTypeRepository {

    @Autowired
    DocumentTypeMapper mapper;

    public Integer insert(DocumentType documentType) {
        return mapper.insert(documentType);
    }

    public void update(DocumentType documentType) {
        mapper.update(documentType);
    }

    public DocumentType getById (Long id){
        return mapper.getById(id);
    }

    public List<DocumentType> getAll(){
        return mapper.getAll();
    }

    public DocumentType getByName(String documentType) {
        return mapper.getByName(documentType);
    }
}
