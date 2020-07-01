package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.repository.warehouse.VvmStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VvmStatusService {

    @Autowired
    private VvmStatusRepository repository;

    private List<VvmStatus>getAll() {
        return repository.getAll();
    }


    private VvmStatus getVvmStatus(Long statusId){
        return repository.getVvmStatus(statusId);
    }
}
