package org.openlmis.vaccine.repository.mapper.warehouse.location;


import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.Site;
import org.openlmis.vaccine.domain.wms.WareHouse;
import org.openlmis.vaccine.domain.wms.dto.WareHouseDTO;
import org.openlmis.vaccine.dto.LocationDTO;
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

    @Select("SELECT * FROM wms_locations WHERE code = #{code} and warehouseId= #{warehouseId} and typeId= #{typeId}")
    LocationDTO getBy(@Param("code") String code,@Param("warehouseId") Long warehouseCodeId, @Param("typeId") Long typeId);

    @Select(" SELECT * FROM warehouses where code = #{code}")
    WareHouse getByC(@Param("code") String code);

    @Update("UPDATE wms_locations SET code = #{code}, name=#{name}, warehouseId = #{warehouseId}, typeId= #{typeId}, active = #{active} WHERE ID = #{id}")
    void updateLocation(LocationDTO location);

    @Insert("INSERT INTO public.wms_locations(\n" +
            "             code, name, warehouseId, typeId,active)\n" +
            "    VALUES ( #{code}, #{name}, #{warehouseId}, #{typeId}, #{active});")
    Integer saveLocation(LocationDTO location);

    @Select(" \n" +
            "SELECT l.code, l.name, type.name locationType FROM public.wms_locations L\n" +
            "\n" +
            "JOIN wms_location_types type ON l.typeId = type.id\n" +
            "\n" +
            "JOIN WAREHOUSES H ON L.warehouseId = H.ID\n" +
            "\n" +
            "where warehouseId = #{warehouseId} ")
    List<LocationDTO> getAllLocations(@Param("warehouseId") Long warehouseId);


    @Select(" SELECT * FROM warehouses")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "site", column = "siteId", javaType = Site.class,
                    one = @One(select = "org.openlmis.vaccine.repository.mapper.warehouse.location.SiteMapper.getAllById"))

    })
    List<WareHouse>getAllWarehouses();

    @Select({"SELECT COUNT(*) FROM public.wms_locations h \n" ,
            " WHERE LOWER(h.code) LIKE '%' || LOWER(#{searchParam} || '%') "})
    Integer getTotalBinsSearchResultCount(@Param("searchParam") String searchParam);


    @Select({"     SELECT l.id, l.code, l.name, type.name locationType,displayOrder, l.active FROM public.wms_locations L\n" ,
            "            \n" +
            "            JOIN wms_location_types type ON l.typeId = type.id\n" ,
            "            \n" +
            "            JOIN WAREHOUSES H ON L.warehouseId = H.ID ",
            " WHERE LOWER(L.code) LIKE '%' || LOWER(#{searchParam} || '%') ",
            " ORDER BY L.id desc "})
    List<LocationDTO> searchBinBy(@Param("searchParam") String searchParam, RowBounds rowBounds);
}
