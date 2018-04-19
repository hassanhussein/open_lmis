package org.openlmis.ivdform.repository.mapper;

import org.apache.ibatis.annotations.Select;
import org.openlmis.ivdform.domain.ChildVisitAgeGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildVisitAgeGroupMapper {

    @Select("select * from vaccine_child_visit_age_groups")
    List<ChildVisitAgeGroup> getAll();
}
