package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.Location;
import org.openlmis.core.domain.LocationType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LocationMapper {
    @Insert("insert into locations(code, name, active, typeid, zoneid, size, capacity, aisleid, parentid, scrap, createdDate,createdBy, modifiedBy, modifiedDate) values(" +
            "#{code}, #{name},#{active},#{locationType.id},#{zone},#{size},#{capacity},#{aisle},#{parent.id}, #{scrap}, COALESCE(#{createdDate}, NOW()), #{createdBy}, #{modifiedBy}," +
            "COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP))")
    @Options(useGeneratedKeys = true)
     void insert(Location location);

    @Update("update  locations set code = #{code}, name = #{name},active = #{active},typeid = #{locationType.id},zoneid = #{zone}, size = #{size}," +
            "capacity = #{capacity},aisleid = #{aisle}, parentid = #{parent.id}, scrap = #{scrap}, modifiedBy = #{modifiedBy}, modifiedDate = (COALESCE(#{modifiedDate}, NOW())) WHERE id = #{id}")
    void update(Location location);

    @Select("SELECT * FROM locations")
    @Results(value = {
            @Result(property = "locationType", column = "typeid", javaType = Integer.class,
                    one = @One(select = "getLocationTypeById"))
        })
    List<Location> getAllLocations();

    @Select("SELECT * FROM locations WHERE id=#{id}")
    @Results(value = {
            @Result(property = "locationType", column = "typeid", javaType = Integer.class,
                    one = @One(select = "getLocationTypeById")),
            @Result(property = "zone", column = "zoneid", javaType = String.class),
            @Result(property = "parent.id", column = "parentid"),
            @Result(property = "aisle", column = "aisleid", javaType = String.class)
    })
     Location getById(Long id);

    @Select("SELECT * FROM locations WHERE code=#{code}")
    @Results(value = {
            @Result(property = "locationType", column = "typeid", javaType = Integer.class,
                    one = @One(select = "getLocationTypeById"))
    })
     Location getByCode(String code);

    @Select("delete FROM locations WHERE id=#{id}")
    void deleteById(Long id);

    @Select("SELECT * FROM location_types")
     List<LocationType> getAllLocationTypes();

    @Select("SELECT COUNT(*) FROM locations L INNER JOIN location_types LT on LT.id = L.typeId " +
            "WHERE (LOWER(L.name) LIKE '%' || LOWER(#{searchParam}) || '%')")
    Integer getTotalSearchResultCountByLocation(String searchParam);
    @Select("SELECT COUNT(*) FROM locations L INNER JOIN location_types LT on LT.id = L.typeId " +
            " WHERE (LOWER(LT.name) LIKE '%' || LOWER(#{searchParam}) || '%')")
    Integer getTotalSearchResultCountByLocationType(String searchParam);

    @Select("SELECT * FROM location_types WHERE id = #{id}")
    LocationType getLocationTypeById(Long id);

    @SelectProvider(type = SelectLocations.class, method = "getLocationsBySearchParam")
    @Results(value = {
            @Result(property = "locationType.name", column = "locationTypeName"),
            @Result(property = "locationType.code", column = "locationTypeCode"),
            @Result(property = "locationType.id", column = "locationTypeId"),
    })
    List<Location> search(@Param(value = "searchParam") String searchParam, @Param(value = "column") String column,
                          RowBounds rowBounds);

     class SelectLocations {
        @SuppressWarnings(value = "unused")
        public static String getLocationsCountBy(Map<String, Object> params) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(*) FROM locations L WHERE ");
            return createQuery(sql, params).toString();
        }

        @SuppressWarnings(value = "unused")
        public static String getLocationsBySearchParam(Map<String, Object> params){
            StringBuilder sql = new StringBuilder();
            String searchParam = (String) params.get("searchParam");
            String column = (String) params.get("column");
            sql.append("SELECT L.id as id, L.code as code, L.name as name, L.zoneid as zone, L.parentid as parent, LT.name as locationTypeName, LT.code as locationTypeCode, LT.id as locationTypeId, L.active FROM locations L ");
            sql.append("INNER JOIN location_types LT on LT.id = L.typeId WHERE ");
            if(column.equalsIgnoreCase("name")) {
                sql.append("(LOWER(L.name) LIKE '%' || LOWER(#{searchParam}) || '%')");

            }
            else if(column.equalsIgnoreCase("type")) {
                sql.append(" (LOWER(LT.name) LIKE '%' || LOWER(#{searchParam}) || '%') ");
            }
            sql.append("ORDER BY LOWER(L.name), LOWER(L.code)");
            return sql.toString();
        }

        @SuppressWarnings(value = "unused")
        public static String searchLocationsBy(Map<String, Object> params) {
            StringBuilder sql = new StringBuilder();
            sql.append(
                    "SELECT L.*, LT.id AS locationTypeId, LT.name AS locationTypeName FROM locations L INNER JOIN location_types LT ON L.typeId = LT.id " +
                            "WHERE ");
            sql = createQuery(sql, params);
            sql.append(" ORDER BY LOWER(L.code)");
            return sql.toString();
        }

        private static StringBuilder createQuery(StringBuilder sql, Map<String, Object> params) {
            String searchParam = (String) params.get("searchParam");
            String column = (String) params.get("column");
            if(column.equalsIgnoreCase("name")) {
                sql.append("(LOWER(L.name) LIKE LOWER('%" + searchParam + "%') ");
            }
            else if(column.equalsIgnoreCase("type")) {
                sql.append("LOWER(LT.name) LIKE LOWER('%" + searchParam + "%'))");
            }
            return sql;
        }
    }
}
