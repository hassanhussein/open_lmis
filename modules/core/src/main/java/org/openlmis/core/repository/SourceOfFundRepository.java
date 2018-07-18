package org.openlmis.core.repository;

import org.openlmis.core.dto.SourceOfFundDTO;
import org.openlmis.core.repository.mapper.SourceOfFundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SourceOfFundRepository {

    @Autowired
    private SourceOfFundMapper mapper;

    public List<SourceOfFundDTO> getAllSourcesOfFunds() {
        return mapper.getAll();
    }

    public SourceOfFundDTO getByCode(String code) {
        return mapper.getByCode(code);
    }

    public void update(SourceOfFundDTO fundDTO) {
        mapper.update(fundDTO);

    }

    public void insert(SourceOfFundDTO fundDTO) {
        mapper.Insert(fundDTO);
    }
}
