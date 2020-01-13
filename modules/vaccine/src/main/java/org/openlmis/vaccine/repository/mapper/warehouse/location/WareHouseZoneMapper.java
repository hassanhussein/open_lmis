package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.WareHouseZone;
import org.openlmis.vaccine.domain.wms.Zone;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WareHouseZoneMapper {

    @Insert("INSERT INTO public.wms_warehouse_zones(\n" +
            "             warehouseId, zoneId, createdBy, createdDate, modifiedBy, \n" +
            "            modifiedDate)\n" +
            "    VALUES (#{warehouse.id}, #{zone.id}, #{createdBy}, NOW(), #{modifiedby}, \n" +
            "            NOW());")
    @Options(useGeneratedKeys = true)
    Integer insert(WareHouseZone wareHouseZone);

    @Select(" SELECT * FROM wms_warehouse_zones WHERE warehouseId = #{warehouseId}")
    @Result(property = "zone", column = "zoneId", javaType = Zone.class,
            one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.ZoneMapper.getById"))
    List<WareHouseZone>getByWareHouseId(@Param("warehouseId") Long warehouseId);


}
