package org.openlmis.vaccine.repository.asn;

import org.openlmis.vaccine.domain.asn.AsnLot;
import org.openlmis.vaccine.repository.mapper.asn.AsnLotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsnLotRepository {

    @Autowired
    AsnLotMapper mapper;

    public Integer insert(AsnLot asnLot) {
        return mapper.insert(asnLot);
    }

    public void update(AsnLot asnLot) {
        mapper.update(asnLot);
    }

    public AsnLot getById (Long id){
        return mapper.getById(id);
    }

    public List<AsnLot> getAll(){
        return mapper.getAll();
    }
}
