package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.WareHouseLineItem;
import org.openlmis.vaccine.domain.wms.Zone;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WareHouseLineItemMapper {

    @Insert("INSERT INTO public.wms_warehouse_line_items(\n" +
            "            wareHouseId, zoneId, aisleCode, binLocationFrom, binLocationTo, \n" +
            "            beamLevelFrom, beamLevelTo, createdBy, createdDate, modifiedBy, \n" +
            "            modifiedDate)\n" +
            "    VALUES (#{wareHouse.id}, #{zone.id}, #{aisleCode}, #{binLocationFrom}, #{binLocationTo}, \n" +
            "            #{beamLevelFrom}, #{beamLevelTo}, #{createdBy}, NOW(), #{modifiedBy}, \n" +
            "            NOW());")
    @Options(useGeneratedKeys = true)
    Integer insert(WareHouseLineItem lineItem);

    @Delete("DELETE FROM public.wms_warehouse_line_items\n" +
            " WHERE warehouseId = #{warehouseId};")
    void deleteByWareHouseId(@Param("warehouseId") Long warehouseId);

    @Select(" SELECT * from wms_warehouse_line_items WHERE warehouseId = #{warehouseId} ")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "zone", column = "zoneId", javaType = Zone.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.ZoneMapper.getById"))
    })
    List<WareHouseLineItem>getByWareHouseId(@Param("warehouseId") Long warehouseId);

}
