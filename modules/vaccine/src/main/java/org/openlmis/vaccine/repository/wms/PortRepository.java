package org.openlmis.vaccine.repository.wms;

import org.openlmis.vaccine.domain.wms.Port;
import org.openlmis.vaccine.repository.mapper.asn.PortMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PortRepository {

    @Autowired
    PortMapper mapper;

    public Integer insert(Port port) {
        return mapper.insert(port);
    }

    public void update(Port port) {
        mapper.update(port);
    }

    public Port getById (Long id){
        return mapper.getById(id);
    }

    public List<Port> getAll(){
        return mapper.getAll();
    }
}
