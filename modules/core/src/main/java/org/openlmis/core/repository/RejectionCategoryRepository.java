package org.openlmis.core.repository;

import org.openlmis.core.dto.RejectionCategoryDTO;
import org.openlmis.core.repository.mapper.RejectionCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RejectionCategoryRepository {

    @Autowired
    private RejectionCategoryMapper mapper;

    public Integer insert(RejectionCategoryDTO categoryDTO) {
      return mapper.insert(categoryDTO);
    }

    public void update(RejectionCategoryDTO categoryDTO) {
        mapper.update(categoryDTO);
    }

    public RejectionCategoryDTO getByCode(String code) {
        return mapper.getByCode(code);
    }

    public List<RejectionCategoryDTO> getAll(){
        return mapper.getAll();
    }
}
