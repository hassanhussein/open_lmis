package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.Zone;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneMapper {

    @Select("select * from wms_zones")
    List<Zone> getAll();

    @Select("select * from wms_zones where code=#{code}")
    Zone getByCode(@Param("code") String code);

    @Insert("INSERT INTO public.wms_zones(\n" +
            "             code, name, description, createdby, createdDate, modifiedby, \n" +
            "            modifieddate)\n" +
            "    VALUES (#{code}, #{name}, #{description}, #{createdBy}, NOW(), #{modifiedBy}, \n" +
            "            NOW());")
    @Options(useGeneratedKeys = true)
    Integer insert(Zone zone);

    @Update("UPDATE public.wms_zones\n" +
            "   SET  code=#{code}, name=#{name}, description=#{description}, \n" +
            "       modifiedBy=#{modifiedBy}, modifiedDate=NOW()\n" +
            " WHERE id= #{id}\n")
    void update(Zone zone);

    @Select("select * from wms_zones where id = #{id}")
    Zone getById(@Param("id") Long id);

    @Select("select * from wms_zones where id = #{id}")
    Zone getByIdFor(@Param("id") Long id);
}
