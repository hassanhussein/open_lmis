package org.openlmis.core.service;


import org.openlmis.core.dto.RejectionCategoryDTO;
import org.openlmis.core.repository.RejectionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RejectionCategoryService {

    @Autowired
    private RejectionCategoryRepository repository;

    public void save(RejectionCategoryDTO dto) {

        if(dto.getId() == null) {
            repository.insert(dto);
        }else {
            repository.update(dto);
        }
    }

    public RejectionCategoryDTO getByCode(RejectionCategoryDTO code) {

        if(code.getCode() != null) {
            return repository.getByCode(code.getCode());
        }

        return null;
    }

    public List<RejectionCategoryDTO> getAll(){
        return repository.getAll();
    }

}
