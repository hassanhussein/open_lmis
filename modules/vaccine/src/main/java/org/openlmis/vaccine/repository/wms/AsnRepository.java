package org.openlmis.vaccine.repository.wms;

import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.repository.mapper.asn.AsnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsnRepository {

    @Autowired
    AsnMapper mapper;


    public Long insert(Asn asn) {
        return mapper.insert(asn);
    }

    public void update(Asn asn) {
        mapper.update(asn);
    }

    public Asn getById (Long id){
        return mapper.getById(id);
    }

    public List<Asn> getAll(){
        return mapper.getAll();
    }
}
