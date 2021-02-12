package org.openlmis.core.repository;

import org.apache.ibatis.annotations.Param;
import org.openlmis.core.dto.SourceOfFundDTO;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.repository.mapper.SourceOfFundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SourceOfFundRepository {

    @Autowired
    private SourceOfFundMapper mapper;

    public List<SourceOfFundDTO> getAllSourcesOfFunds(Long program) {
        return mapper.getAll(program);
    }

    public SourceOfFundDTO getByCode(String code) {
        return mapper.getByCode(code);
    }

    public void update(SourceOfFundDTO fundDTO) {
        try {
            mapper.update(fundDTO);
        }  catch (DuplicateKeyException duplicateKeyException) {
            throw new DataException("Code or Name arleady exists");
        }
    }

    public void insert(SourceOfFundDTO fundDTO) {
        try {
            mapper.Insert(fundDTO);
        }  catch (DuplicateKeyException duplicateKeyException) {
            throw new DataException("Code or Name Arleady exists");
        }
    }
}
