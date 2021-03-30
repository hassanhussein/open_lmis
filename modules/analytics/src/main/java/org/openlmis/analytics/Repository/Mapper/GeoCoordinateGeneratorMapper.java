package org.openlmis.analytics.Repository.Mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.dto.GeographicZoneGeometry;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeoCoordinateGeneratorMapper {

    @Select("select * from geographic_zones where levelid = 2")
    List<GeographicZone> getAllProvinces();

    @Select("select * from geographic_zones where parentId = #{parentId} AND LEVELID = 3")
    List<GeographicZone> getDistrictByParentId(@Param("parentId") Long id);

    @Select("select geometry from geographic_zone_geojson where zoneId = #{id}\n")
    GeographicZoneGeometry getGeometryById(@Param("id") Long id);

}
