package org.openlmis.analytics.Repository.Mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.openlmis.analytics.Builder.GeoFacilityStockStatusQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface GeoFacilityStockStatusMapper {

    @SelectProvider(type= GeoFacilityStockStatusQueryBuilder.class, method="getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10,timeout=0,useCache=true,flushCache=true)
    List<HashMap<String,Object>> getGeoFacilityStockStatus(@Param("userId") Long userId,
                                                           @Param("product") Long product,
                                                           @Param("program") Long program,
                                                           @Param("year") Long year,
                                                           @Param("period") Long period);

    @Select(" Select facility, 0 as phoneNumber, amc, stockInHand, mos, product  from mv_stock_imbalance_by_facility_report where " +
            "  facility_id = #{facilityId} and periodId = #{period} and programId = #{program} and  year =#{year} and productId=#{product}")
    List<HashMap<String,Object>>GeoFacilityStockStatusDetails(@Param("userId") Long userId,
                                                          @Param("product") Long product,
                                                          @Param("program") Long program,
                                                          @Param("year") Long year,
                                                          @Param("period") Long period,
                                                           @Param("facilityId") Long facilityId);


    @Select(" SELECT * FROM geographic_zones gz \n" +
            "JOIN geographic_zone_geojson gjson on gz.id = gjson.zoneid\n" +
            "\n" +
            "where geojsonId = 0 ")
    List<HashMap<String,Object>> getGeoJSONInfo();



    @Select(" \n" +
            "   select region_id,region_name, sum(SO) so, sum(SP) SP, sum(os) os, sum(us) us,sum(uk) uk,\n" +
            "                          sum(soh) soh, sum(amc) amc,sum(MOS) MOS,  sum(currentPrice) currentPrice\n" +
            "                          ,productcode, product, sum(ordered) ordered, sum(required) required\n" +
            "                \n" +
            "                         FROM  (\n" +
            "                         \n" +
            "                       SELECT latitude,region_id , region_name ,district_id, district_name, longitude, facility_id facilityid,facility,\n" +
            "                      case when status = 'SO' THEN 1 ELSE 0 END AS SO,\n" +
            "                      case when status = 'SP' THEN 1 ELSE 0 END AS SP,\n" +
            "                      case when status = 'OS' THEN 1 ELSE 0 END AS OS,\n" +
            "                                                             \n" +
            "                      case when status = 'US' THEN 1 ELSE 0 END AS US,\n" +
            "                      case when status = 'UK' THEN 1 ELSE 0 END AS UK, (SELECT amc )AMC,  (SELECT stockinhand )soh, \n" +
            "                      (SELECT MOS) MOS, (SELECT CURRENTPRICE) currentPrice, (select mainphone) mainphone, (select productcode) productcode, \n" +
            "                \n" +
            "                      (select product) product, (select required) required, (select ordered) ordered\n" +
            "                                                                           \n" +
            "                     from mv_stock_imbalance_by_facility_report\n" +
            "                                                                                 \n" +
            "                     WHERE productId = #{product} AND  programId = #{program}::INT\n" +
            "                     AND YEAR = #{year}::int \n" +
            "                                                                                                 and periodId = #{period}::INT\n" +
            "                )L\n" +
            "                                                                                           \n" +
            "              GROUP BY  region_id,region_name,productcode, product\n" +
            "              ORDER BY region_id,region_name")

    List<HashMap<String, Object>> getRegionalStockStatusSummary(@Param("userId") Long userId,
                                                                @Param("product") Long product,
                                                                @Param("program") Long program,
                                                                @Param("year") Long year,
                                                                @Param("period") Long period
                                                              );




}
