package org.openlmis.analytics.Repository.Mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.openlmis.analytics.Builder.EmergencyRequisitionStatusTrendQueryBuilder;
import org.openlmis.analytics.Builder.OnTimeDeliveryQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface RequisitionStatusMapper {

    @SelectProvider(type= EmergencyRequisitionStatusTrendQueryBuilder.class, method="getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize=10,timeout=0,useCache=true,flushCache=true)
    List<HashMap<String,Object>> getRequisitionStatusTrend(@Param("userId") Long userId,
                                                   @Param("program") Long program,
                                                   @Param("period") Long period);



    //mappe


    @Select("select  to_char(createdDate, 'yyyy_mm') as \"Month\", count(*) \"Rejected Count\" from ( \n" +
            "    select count(*), max(createdDate) as createdDate, rnrid from requisition_status_changes c where c.status = 'INITIATED' " +
            " and  c.createdDate>'2017-06-01'::date group by rnrid having count(*) > 1\n" +
            ") a\n" +
            "group by to_char(createdDate, 'yyyy_mm')\n" +
            "order by to_char(createdDate, 'yyyy_mm')")
    List<HashMap<String,Object>>getRnRRejectionCount();

    @Select("SELECT d.region_name as \"Province\", SUM(1) \"Number Of EOs\"\n" +
            "    FROM requisitions r\n" +
            "         JOIN facilities f on f.id = r.facilityId \n" +
            "         JOIN vw_districts d on d.district_id = f.geographiczoneid\n" +
            "    WHERE r.emergency = true \n" +
            "        \n" +
            "        AND r.createdDate >= date_trunc('month', CURRENT_DATE) - INTERVAL '3 month'\n" +
            "    GROUP BY d.region_name\n" +
            "    ORDER BY SUM(1) DESC\n" +
            "    LIMIT 100;")
    List<HashMap<String,Object>>getNumberOfEmergency();

    @Select("SELECT  p.name as \"Program Name\",\n" +
            "        100.0 * SUM(CASE WHEN emergency = true THEN 1 ELSE 0 END) / SUM(1) as \"Emergency\", \n" +
            "        100.0 * SUM(CASE WHEN emergency != true THEN 1 ELSE 0 END) / SUM(1) as \"Regular\", \n" +
            "        SUM(1) as \"Total\" \n" +
            "        FROM requisitions r join programs p \n" +
            "            ON r.programId = p.id\n" +
            "        WHERE p.active = true and r.status = 'RELEASED'\n" +
            "        GROUP BY p.name\n" +
            "        ")
    List<HashMap<String,Object>>getPercentageOfEmergencyOrderByProgram();


    @Select(" SELECT  p.name as \"Program Name\",\n" +
            "        SUM(CASE WHEN emergency = true THEN 1 ELSE 0 END) as \"Emergency\", \n" +
            "        SUM(CASE WHEN emergency != true THEN 1 ELSE 0 END) as \"Regular\", \n" +
            "        SUM(1) as \"Total\" \n" +
            "       \n" +
            "        FROM requisitions r join programs p \n" +
            "            ON r.programId = p.id\n" +
            "           and r.createdDate >= date_trunc('month', CURRENT_DATE) - INTERVAL '20 month'\n" +
            "        WHERE p.active = true\n" +
            "            AND r.status = 'RELEASED'\n" +
            "        GROUP BY p.name\n" +
            "        ")
    List<HashMap<String,Object>>getEmergencyOrderByProgram();

    @Select("SELECT  SUM(CASE WHEN emergency = true THEN 1 ELSE 0 END) as \"Emergency Requisitions\"\n" +
            "    , SUM(CASE WHEN emergency != true THEN 1 ELSE 0 END) as \"Regular Requisitions\"\n" +
            "    , to_char(createdDate, 'Mon') || '-' || extract(year from createdDate) as \"Month\" \n" +
            "    ,to_char(createdDate, 'yyyy-mm') ym\n" +
            "    from requisitions \n" +
            "    WHERE status != 'INITIATED' and createdDate >=\n" +
            "      date_trunc('month', CURRENT_DATE) - 2 * INTERVAL '1 year'\n" +
            "group by  to_char(createdDate, 'Mon') || '-' || extract(year from createdDate), to_char(createdDate, 'yyyy-mm')\n" +
            "    order by ym asc")
    List<HashMap<String,Object>>getTrendOfEmergencyOrdersSubmittedPerMonth();

    @Select(" SELECT  SUM(CASE WHEN emergency = true THEN 1 ELSE 0 END) as \"Emergency Requisitions\"\n" +
            "    , SUM(CASE WHEN emergency != true THEN 1 ELSE 0 END) as \"Regular Requisitions\"\n" +
            "    , to_char(createdDate, 'Mon') || '-' || extract(year from createdDate) as \"Month\" \n" +
            "    ,to_char(createdDate, 'yyyy-mm') ym\n" +
            "    from requisitions \n" +
            "    WHERE status != 'INITIATED' and createdDate >=\n" +
            "      date_trunc('month', CURRENT_DATE) - INTERVAL '2 year'\n" +
            "group by  to_char(createdDate, 'Mon') || '-' || extract(year from createdDate), to_char(createdDate, 'yyyy-mm')\n" +
            "    order by ym asc")
    List<HashMap<String,Object>> emergencyOrderTrends();

    @Select("SELECT  ln.productcode, ln.product, count(ln.productcode) order_frequency\n" +
            "from requisitions  r\n" +
            "join requisition_line_items ln on r.id = ln.rnrid\n" +
            " WHERE r.status != 'INITIATED' and r.createdDate >=  date_trunc('month', CURRENT_DATE) - 2 * INTERVAL '1 year'  and r.emergency = true\n" +
            " group by ln.productcode, ln.product\n" +
            " order by  1,2\n" +
            " limit 10")
    List<HashMap<String, Object>> getEmergencyOrderFrequentAppearingProducts();


}
