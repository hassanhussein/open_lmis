package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.Location;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationMapper {

    @Insert("INSERT INTO public.wms_locations(\n" +
            "            wareHouseId, code, active, createdBy, createddate, modifiedBy, \n" +
            "            modifieddate)\n" +
            "    VALUES (#{wareHouseId}, #{code}, #{active}, #{createdBy}, NOW(), #{modifiedBy}, \n" +
            "            NOW());")
    @Options(useGeneratedKeys = true)
    Integer insert(Location location);

    @Update("UPDATE public.wms_locations\n" +
            "   SET  wareHouseId=#{wareHouseId}, code=#{code}, active=#{active},\n" +
            "       modifiedBy=#{modifiedBy}, modifiedDate=NOW()\n" +
            " WHERE ID = #{id}\n")
    void update(Location location);

    @Select(" select * from public.wms_locations where id = #{id}")
    Location getById(@Param("id") Long id);

    @Select(" select * from public.wms_locations")
    List<Location> getAll();

    @Select(" select * from public.wms_locations where code = #{code}")
    Location getByCode(@Param("code") String code);

    @Delete("DELETE FROM public.wms_locations WERE warehouseId =#{wareHouseId}")
    void deleteBy(@Param("wareHouseId") Long wareHouseId);

    @Select("select * from public.wms_locations where wareHouseId = #{wareHouseId}")
    List<Location> getByWareHouse(@Param("wareHouseId") Long wareHouseId);
}
