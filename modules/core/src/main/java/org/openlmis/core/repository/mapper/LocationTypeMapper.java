package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.LocationType;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LocationTypeMapper {

    @Insert(" INSERT INTO location_types (code,name, description, active, createdBy, createDate,modifiedBy,modifiedDate) " +
            " VALUES(#{code},#{name}, #{description}, #{active}, #{createdBy}, NOW(),#{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Integer insert(LocationType locationType);

    @Update(" update location_types set code = #{code}, name = #{name}, " +
            " description = #{description}, active = #{active}, modifiedDate = now(), " +
            " modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(LocationType type);

    @Select(" select * from location_types where code = #{code}")
    public LocationType getByCode(@Param("code") String code);

    @Select("select * from location_types where id = #{id}")
    LocationType  getById(@Param("id") Long id);

    @Select(" select * from location_types")
    List<LocationType> getAll();


}
