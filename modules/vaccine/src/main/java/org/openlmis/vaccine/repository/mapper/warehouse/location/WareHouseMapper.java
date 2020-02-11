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
            "            siteId, code, name)\n" +
            "    VALUES (#{site.id}, #{code}, #{name});\n ")
    @Options(useGeneratedKeys = true)
    Integer insert(WareHouse house);

    @Update("UPDATE public.warehouses\n" +
            "   SET  siteId=#{site.id}, code=#{code}, name=#{name} " +
            " WHERE id=#{id}")
    void update(WareHouse house);

    @Select({"SELECT COUNT(*) FROM wms_warehouse_line_items item\n" ,
            "JOIN wms_warehouses  h ON item.warehouseId = h.id\n" ,
            "JOIN wms_sites s ON h.siteId = s.Id\n" ,
            "JOIN wms_zones z ON item.zoneId = Z.ID\n" ,
            "WHERE LOWER(h.name) LIKE '%' || LOWER(#{searchParam} || '%') "})
    Integer getTotalSearchResultCount(String param);

    @Select({"SELECT h.id, h.name,gz.name region, s.name siteName, productTypeId,ACTIVE  FROM wms_warehouses h\n" ,
            "JOIN wms_warehouse_line_items  item ON item.warehouseId = h.id\n" ,
            "JOIN wms_sites s ON h.siteId = s.Id\n" ,
            "JOIN wms_zones z ON item.zoneId = Z.ID\n" ,
            "JOIN GEOGRAPHIC_ZONES gz ON  gz.id = S.regionId\n" ,
            "WHERE LOWER(h.name) LIKE '%' || LOWER(#{searchParam} || '%') ",
            "ORDER BY LOWER(h.name), LOWER(s.name)"})
    List<WareHouseDTO> searchByName(@Param(value = "searchParam") String searchParam, RowBounds rowBounds);

    @Select(" SELECT * FROM warehouses where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "site", column = "siteId", javaType = Site.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.SiteMapper.getAllById"))

    })
    WareHouse getById(@Param("id") Long id);
}
