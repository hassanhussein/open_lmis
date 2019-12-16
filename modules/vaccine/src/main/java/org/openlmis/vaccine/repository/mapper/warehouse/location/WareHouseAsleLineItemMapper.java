package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.WareHouseAsleLineItem;
import org.openlmis.vaccine.domain.wms.WareHouseLineItem;
import org.openlmis.vaccine.domain.wms.Zone;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WareHouseAsleLineItemMapper {

    @Insert("INSERT INTO public.wms_aisle_line_items(\n" +
            "             zoneId, aisleCode, binLocationFrom, binLocationTo, \n" +
            "            beamLevelFrom, beamLevelTo, createdBy, createdDate, modifiedBy, \n" +
            "            modifiedDate)\n" +
            "    VALUES (#{zone.id}, #{aisleCode}, #{binLocationFrom}, #{binLocationTo}, \n" +
            "            #{beamLevelFrom}, #{beamLevelTo}, #{createdBy}, NOW(), #{modifiedBy}, \n" +
            "            NOW());")
    @Options(useGeneratedKeys = true)
    Integer insert(WareHouseAsleLineItem lineItem);

    @Delete("DELETE FROM public.wms_aisle_line_items\n" +
            " WHERE zoneId = #{zoneId};")
    void deleteByZoneId(@Param("zoneId") Long zoneId);

    @Select(" SELECT * from wms_aisle_line_items WHERE zoneId = #{zoneId} ")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "zone", column = "zoneId", javaType = Zone.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.ZoneMapper.getById"))
    })
    List<WareHouseLineItem> getByzoneId(@Param("zoneId") Long zoneId);


}
