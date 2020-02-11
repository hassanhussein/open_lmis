package org.openlmis.vaccine.repository.mapper.warehouse.location;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.Site;
import org.openlmis.vaccine.domain.wms.dto.WareHouseDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteMapper {

 @Insert(" INSERT INTO public.sites(\n" +
         "             code, name, geographicZoneId, longitude,latitude, active)\n" +
         "    VALUES ( #{code}, #{name}, #{geographicZoneId},#{longitude},#{latitude}, #{active});")
 @Options(useGeneratedKeys = true)
 Integer insert(Site site);

 @Update(" UPDATE public.sites\n" +
         "   SET  code=#{code}, name=#{name}, geographicZoneId=#{geographicZoneId}, longitude=#{longitude},latitude=#{latitude}, active=#{active}\n" +
         " WHERE id= #{id};\n ")
 void update(Site site);

 @Select("SELECT * FROM public.sites where geographicZoneId= #{zoneId}")
 @Results(value = {
         @Result(property = "geographicZone", column = "geographicZoneId", javaType = GeographicZone.class,
                 one = @One(select = "org.openlmis.core.repository.mapper.GeographicZoneMapper.getWithParentById"))
 })
 List<Site> getAllBy(@Param("zoneId") Long zoneId);

 @Select("SELECT * FROM public.sites where id = #{id}")
 @Results(value = {
         @Result(property = "region", column = "geographicZone.name", javaType = String.class),
         @Result(property = "geographicZoneId", column = "geographicZone.id", javaType = Integer.class),
         @Result(property = "geographicZone", column = "geographicZoneId", javaType = GeographicZone.class,
                 one = @One(select = "org.openlmis.core.repository.mapper.GeographicZoneMapper.getWithParentById"))
 })
 Site getAllById(@Param("id") Long id);


 @Select({"SELECT COUNT(*) FROM sites s \n" ,
         "JOIN geographic_zones  z ON s.geographicZoneId = z.id\n" ,
         "WHERE LOWER(s.name) LIKE '%' || LOWER(#{searchParam} || '%') "})
 Integer getTotalSearchResultCount(String param);

 @Select({"SELECT h.id,h.code, h.name,gz.name region FROM sites h\n" ,
         "JOIN GEOGRAPHIC_ZONES gz ON  gz.id = h.geographicZoneId\n" ,
         "WHERE LOWER(h.name) LIKE '%' || LOWER(#{searchParam} || '%') ",
         "ORDER BY LOWER(h.name)"})
 List<Site> searchByName(@Param(value = "searchParam") String searchParam, RowBounds rowBounds);
}
