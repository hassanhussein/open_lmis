package org.openlmis.ivdform.repository.mapper;

import org.apache.ibatis.annotations.Select;
import org.openlmis.ivdform.domain.BreastFeedingCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreastFeedingCategoryMapper {

    @Select("select * from breast_feeding_categories")
    List<BreastFeedingCategory> getAll();
}
