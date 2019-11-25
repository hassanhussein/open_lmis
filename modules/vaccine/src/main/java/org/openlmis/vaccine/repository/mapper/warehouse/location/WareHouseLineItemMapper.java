package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.openlmis.vaccine.domain.wms.WareHouseLineItem;
import org.springframework.stereotype.Repository;

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

}
