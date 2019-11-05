package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.AsnLot;
import org.openlmis.vaccine.domain.wms.Receive;
import org.openlmis.vaccine.domain.wms.ReceiveLot;
import org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveLotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReceiveLotRepository {

    @Autowired
    ReceiveLotMapper mapper;

    public Integer insert(ReceiveLot receiveLot) {
        return mapper.insert(receiveLot);
    }

    public void update(ReceiveLot receiveLot) {
        mapper.update(receiveLot);
    }

    public ReceiveLot getById (Long id){
        return mapper.getById(id);
    }

    public List<ReceiveLot> getAll(){
        return mapper.getAll();
    }

    public List<ReceiveLot> getByLotNumber(Long id) {

       return mapper.getByLotNumber(id);

    }

    public void deleteByLineItem(Long id) {
        mapper.deleteByLineItem(id);
    }
}
