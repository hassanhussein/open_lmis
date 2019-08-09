package org.openlmis.analytics.Repository.Mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.openlmis.analytics.Builder.DashboardConsumptionSummaryQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface DashboardConsumptionMapper {


    @SelectProvider(type= DashboardConsumptionSummaryQueryBuilder.class, method="getSummary")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10,timeout=0,useCache=true,flushCache=true)
    List<HashMap<String,Object>> getConsumptionSummary(@Param("userId") Long userId,
                                                       @Param("product") Long product,
                                                       @Param("program") Long program,
                                                       @Param("period") Long period,
                                                       @Param("year") Long year);



}
