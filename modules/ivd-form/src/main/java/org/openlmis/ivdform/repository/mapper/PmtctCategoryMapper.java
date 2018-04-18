package org.openlmis.ivdform.repository.mapper;

import org.apache.ibatis.annotations.Select;
import org.openlmis.ivdform.domain.PmtctCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PmtctCategoryMapper {

    @Select("select * from pmtct_categories")
    List<PmtctCategory> getAll();
}
