package org.openlmis.analytics.Repository.Mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public interface COVIDMapper {

    @Select("select r.facilityid , f.name,\n" +
            "ROUND(100.0 * (SUM(CASE WHEN rli.stockinhand<1 THEN 1 ELSE 0 END) )/ COUNT(*),2) AS stockOutPercentage ,\n" +
            "ROUND(100.0 * (SUM(CASE WHEN rli.stockinhand>0 THEN 1 ELSE 0 END) )/ COUNT(*),2) AS availabilityPercentage \n" +
            "from requisition_line_items rli\n" +
            "join requisitions r on r.id=rli.rnrid\n" +
            "join products pr on pr.code=rli.productcode\n" +
            "join programs p on p.id=r.programid\n" +
            "join facilities f on f.id=r.facilityid\n" +
            "join processing_periods pp on pp.id=r.periodid\n" +
            "where p.code='COVID-19' and r.status='APPROVED'\n" +
           "and pr.id=#{product}  and  pp.enddate between #{startDate}::DATE  and #{endDate}::DATE  \n" +
            "group by r.facilityid, f.name ")
    List<HashMap<String,Object>> getStockStatusperProduct(@Param("product") Long product,
                                                 @Param("startDate") String startDate,
                                                 @Param("endDate") String endDate
                                                );



    @Select("select r.facilityid , f.name,\n" +
            "ROUND(100.0 * (SUM(CASE WHEN rli.stockinhand<1 THEN 1 ELSE 0 END) )/ COUNT(*),2) AS stockOutPercentage ,\n" +
            "ROUND(100.0 * (SUM(CASE WHEN rli.stockinhand>0 THEN 1 ELSE 0 END) )/ COUNT(*),2) AS availabilityPercentage \n" +
            "from requisition_line_items rli\n" +
            "join requisitions r on r.id=rli.rnrid\n" +
            "join programs p on p.id=r.programid\n" +
            "join facilities f on f.id=r.facilityid\n" +
            "join processing_periods pp on pp.id=r.periodid\n" +
            "where p.code='COVID-19' and r.status='APPROVED'\n" +
            "  and pp.enddate between #{startDate}::DATE  and #{endDate}::DATE  \n" +
            "group by r.facilityid, f.name ")
    List<HashMap<String,Object>> getAllStockStatus( @Param("startDate") String startDate,
                                                          @Param("endDate") String endDate
    );
}
