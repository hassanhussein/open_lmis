package org.openlmis.vaccine.repository.mapper.warehouse.asn;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.Port;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortMapper {
    @Insert(" INSERT INTO ports (code, name, description, active, createdBy, createdDate,modifiedBy,modifiedDate) " +
            " VALUES(#{name}, #{description}, #{createdBy}, NOW(),#{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Integer insert(Port port);

    @Update(" update ports set  code = #{code}, name = #{name}, description = #{description}, " +
            " active = #{active}, modifiedDate = now(), " +
            " modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(Port port);
    @Select("select * from ports where id = #{id}")
    Port getById(@Param("id") Long id);

    @Select(" select * from ports")
    List<Port> getAll();
}
