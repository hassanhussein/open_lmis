package org.openlmis.ivdform.repository.mapper;

import org.apache.ibatis.annotations.Select;
import org.openlmis.ivdform.domain.WeightCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightCategoryMapper {

    @Select("select * from weight_ratio_categories")
    List<WeightCategory> getAll();
}
