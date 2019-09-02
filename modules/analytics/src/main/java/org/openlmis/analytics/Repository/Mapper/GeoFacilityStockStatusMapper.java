package org.openlmis.analytics.Repository.Mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
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




}
