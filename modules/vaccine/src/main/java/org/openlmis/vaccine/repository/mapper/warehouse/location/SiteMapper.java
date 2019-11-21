package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.Site;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteMapper {

 @Insert(" INSERT INTO public.wms_sites(\n" +
         "             regionId, code, name, latitude, longitude, createdBy, createdDate, \n" +
         "            modifiedBy, modifiedDate)\n" +
         "    VALUES ( #{region.id}, #{code}, #{name}, #{latitude}, #{longitude}, #{createdBy}, NOW(), \n" +
         "            #{modifiedBy}, NOW());")
 @Options(useGeneratedKeys = true)
 Integer insert(Site site);

 @Update(" UPDATE public.wms_sites\n" +
         "   SET  regionId=#{region.id}, code=#{code}, name=#{name}, latitude=#{longitude}, longitude=#{latitude}, modifiedBy=#{modifiedBy}, modifiedDate=NOW()\n" +
         " WHERE id= #{id};\n ")
 void update(Site site);

 @Select(" SELECT * FROM public.wms_sites where regionId= #{regionId}")
 List<Site> getAllBy(@Param("regionId") Long regionId);

 @Select("SELECT * FROM public.wms_sites where id = #{id}")
 @Results(value = {
         @Result(property = "region", column = "regionId", javaType = Long.class,
                 one = @One(select = " org.openlmis.core.repository.mapper.GeographicZoneMapper.getWithParentById"))
 })
 Site getAllById(@Param("id") Long id);


}
