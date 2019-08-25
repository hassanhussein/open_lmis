package org.openlmis.vaccine.repository.asn;

import org.openlmis.vaccine.domain.asn.DocumentType;
import org.openlmis.vaccine.repository.mapper.asn.DocumentTypeMapper;
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
}
