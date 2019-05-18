package org.openlmis.core.repository;

import org.apache.ibatis.annotations.Param;
import org.openlmis.core.dto.SourceOfFundDTO;
import org.openlmis.core.repository.mapper.SourceOfFundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SourceOfFundRepository {

    @Autowired
    private SourceOfFundMapper mapper;

    public List<SourceOfFundDTO> getAllSourcesOfFunds(Long program) {
        return mapper.getAll(program);
    }

    public SourceOfFundDTO getByCode(String code,String name) {
        return mapper.getByCode(code,name);
    }

    public void update(SourceOfFundDTO fundDTO) {
        mapper.update(fundDTO);

    }

    public void insert(SourceOfFundDTO fundDTO) {
        mapper.Insert(fundDTO);
    }
}
