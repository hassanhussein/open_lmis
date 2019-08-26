package org.openlmis.vaccine.service.asn;

import org.openlmis.vaccine.domain.asn.Port;
import org.openlmis.vaccine.repository.asn.PortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortService {
    @Autowired
    private PortRepository repository;

    public void save(Port port) {

        if (port.getId() == null) {

            repository.insert(port);
        }else {
            repository.update(port);
        }

    }

    public Port getById (Long id) {

        return repository.getById(id);

    }
    public List<Port> getAll(){

        return  repository.getAll();
    }
}
