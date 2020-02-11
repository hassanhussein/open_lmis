package org.openlmis.vaccine.repository.mapper.warehouse.location;


import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import org.openlmis.vaccine.domain.wms.Site;
import org.openlmis.vaccine.domain.wms.WareHouse;
import org.openlmis.vaccine.domain.wms.dto.WareHouseDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WareHouseMapper {

    @Insert("INSERT INTO public.warehouses(\n" +
            "            siteId, code, name,active)\n" +
            "    VALUES (#{siteId}, #{code}, #{name}, #{active});\n ")
    @Options(useGeneratedKeys = true)
    Integer insert(WareHouse house);

    @Update("UPDATE public.warehouses\n" +
            "   SET  siteId=#{siteId}, code=#{code}, name=#{name}, active=#{active} " +
            " WHERE id=#{id}")
    void update(WareHouse house);

    @Select({"SELECT COUNT(*) FROM public.warehouses h \n" ,
            "JOIN sites s ON h.siteId = s.Id\n" ,
            "WHERE LOWER(h.name) LIKE '%' || LOWER(#{searchParam} || '%') "})
    Integer getTotalSearchResultCount(String param);

    @Select({"SELECT h.id, h.name,h.code, h.ACTIVE  FROM warehouses h\n" ,
            "JOIN sites s ON h.siteId = s.Id\n" ,
            "WHERE LOWER(h.name) LIKE '%' || LOWER(#{searchParam} || '%') ",
            "ORDER BY LOWER(h.name)"})
    List<WareHouse> searchByName(@Param(value = "searchParam") String searchParam, RowBounds rowBounds);

    @Select(" SELECT * FROM warehouses where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "site", column = "siteId", javaType = Site.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.SiteMapper.getAllById"))

    })
    WareHouse getById(@Param("id") Long id);
}
