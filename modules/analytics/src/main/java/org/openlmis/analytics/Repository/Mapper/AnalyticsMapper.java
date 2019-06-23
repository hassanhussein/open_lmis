package org.openlmis.analytics.Repository.Mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.openlmis.analytics.Builder.DashboardQueryBuilder;
import org.openlmis.analytics.Builder.DashboardStockAvailableForProgramQueryBuilder;
import org.openlmis.analytics.Builder.StockStatusQueryBuilder;
import org.openlmis.analytics.domain.Dashboard;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface AnalyticsMapper {

    @SelectProvider(type= DashboardQueryBuilder.class, method="getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10,timeout=0,useCache=true,flushCache=true)
    List<Dashboard> getUsers();


    @SelectProvider(type= StockStatusQueryBuilder.class, method="getStockStatusSummary")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10,timeout=0,useCache=true,flushCache=true)
    List<HashMap<String,Object>> getStockStatusSummary(@Param("userId") Long userId,
                                                       @Param("product") Long product,
                                                       @Param("program") Long program,
                                                       @Param("year") Long year);

    @SelectProvider(type= DashboardStockAvailableForProgramQueryBuilder.class, method="getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10,timeout=0,useCache=true,flushCache=true)
    List<HashMap<String,Object>> getStockAvailableForPeriod(@Param("userId") Long userId,
                                                            @Param("period") Long period);

    @SelectProvider(type= DashboardStockAvailableForProgramQueryBuilder.class, method="getStockForProductandProgram")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10,timeout=0,useCache=true,flushCache=true)
    List<HashMap<String,Object>> getStockForProductandProgram(@Param("userId") Long userId,
                                                            @Param("program") Long program,
                                                              @Param("period") Long period);



}