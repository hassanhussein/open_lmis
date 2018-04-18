package org.openlmis.ivdform.repository.mapper;

import org.apache.ibatis.annotations.Select;
import org.openlmis.ivdform.domain.WeightAgeRatioAgeGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightAgeRatioAgeGroupMapper {

    @Select("Select * from weight_ratio_age_groups")
    List<WeightAgeRatioAgeGroup>getAll();

}
