package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.Receive;
import org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReceiveRepository {

    @Autowired
    private ReceiveMapper mapper;

    public Integer insert(Receive receive) {
        return mapper.insert(receive);
    }

    public void update(Receive receive) {
        mapper.update(receive);
    }


    public List<Receive> getAll() {
        return mapper.getAll();
    }

    public Receive getById(Long id) {
        return mapper.getById(id);
    }
}
