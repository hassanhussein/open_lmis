package org.openlmis.ivdform.repository.mapper;

import org.apache.ibatis.annotations.Select;
import org.openlmis.ivdform.domain.TtStatusCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TtStatusCategoryMapper {

    @Select("select * from tt_status_categories")
    List<TtStatusCategory> getAll();

}
