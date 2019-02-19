package org.openlmis.core.service;

import org.openlmis.core.dto.RejectionReasonDTO;
import org.openlmis.core.repository.RejectionReasonDTORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RejectionReasonDTOService {
    @Autowired
    private RejectionReasonDTORepository repository;


    public void insert(RejectionReasonDTO rejection) {

        if (rejection.getId() == null) {
            repository.insert(rejection);
        } else
            repository.update(rejection);
    }

    public RejectionReasonDTO getByCode(RejectionReasonDTO record) {
        return repository.getByCode(record.getCode());
    }
}
