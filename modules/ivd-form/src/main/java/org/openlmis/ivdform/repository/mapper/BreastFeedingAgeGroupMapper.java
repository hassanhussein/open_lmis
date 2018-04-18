package org.openlmis.ivdform.repository.mapper;

import org.apache.ibatis.annotations.Select;
import org.openlmis.ivdform.domain.BreastFeedingAgeGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreastFeedingAgeGroupMapper {

    @Select("select * from breast_feeding_age_groups")
    List<BreastFeedingAgeGroup> getAll();
}
