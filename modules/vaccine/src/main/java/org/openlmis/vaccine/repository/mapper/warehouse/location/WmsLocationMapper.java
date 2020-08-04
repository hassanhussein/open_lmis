package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.Location;
import org.openlmis.vaccine.dto.LocationDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WmsLocationMapper {

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




    @Select("SELECT * FROM wms_locations WHERE id=#{id}")
    @Results(value = {
            @Result(property = "type", column = "typeId", javaType = Integer.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.LocationTypeMapper.getById")),
            @Result(property = "house", column = "warehouseId", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.WareHouseMapper.getById"))
    })
    LocationDTO getByLocationId(Long id);

    @Select(" SELECT * FROM wms_locations WHERE warehouseId=#{id} " )
            //" and id IN"
           // " ( SELECT Distinct locationId FROM lot_on_hand_locations )"
    @Results(value = {
            @Result(property = "type", column = "typeId", javaType = Integer.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.LocationTypeMapper.getById")),
            @Result(property = "house", column = "warehouseId", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.WareHouseMapper.getById"))
    })
    List<LocationDTO> getByWarehouseId(Long id);

    @Select("select l.* from public.wms_locations l JOIN wms_location_types t ON l.typeID = t.id where l.wareHouseId = #{id} and (lower(t.code)='storage' or lower(t.code)='quarantine')")

    @Results(value = {
            @Result(property = "type", column = "typeId", javaType = Integer.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.LocationTypeMapper.getById")),
            @Result(property = "house", column = "warehouseId", javaType = Integer.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.WareHouseMapper.getById"))
    })
    List<LocationDTO> getByWareHouseStorageQuarantine(Long id);
}
