package org.openlmis.ivdform.repository;

import org.openlmis.ivdform.domain.PmtctCategory;
import org.openlmis.ivdform.repository.mapper.PmtctCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PmtctCategoryRepository {
    @Autowired
    PmtctCategoryMapper mapper;

    public List<PmtctCategory> getAll() {
        return mapper.getAll();
    }
}
