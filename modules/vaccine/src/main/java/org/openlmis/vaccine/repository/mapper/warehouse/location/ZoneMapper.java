package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.Select;
import org.openlmis.vaccine.domain.wms.Zone;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneMapper {

    @Select("select * from wms_zones")
    List<Zone> getAll();
}
