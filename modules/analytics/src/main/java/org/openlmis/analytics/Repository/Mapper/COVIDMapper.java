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
            "where p.code='COVID-19' and r.status IN ('APPROVED','IN_APPROVAL', 'RELEASED', 'AUTHORIZED')\n" +
           "and pr.id=#{product}  and  pp.enddate between #{startDate}::DATE  and #{endDate}::DATE  \n" +
            "group by r.facilityid, f.name ")
    List<HashMap<String,Object>> getStockStatusPerProduct(@Param("product") Long product,
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
            "where p.code='COVID-19' and r.status IN ('APPROVED','IN_APPROVAL', 'RELEASED', 'AUTHORIZED')\n" +
            "  and pp.enddate between #{startDate}::DATE  and #{endDate}::DATE  \n" +
            "group by r.facilityid, f.name ")
    List<HashMap<String,Object>> getAllStockStatus( @Param("startDate") String startDate,
                                                          @Param("endDate") String endDate
    );


    @Select("select product as product, a.enddate::DATE as last_update,  stockinhand as stockOnHand,\n" +
            "quantityrequested as ordered\n" +
            "from requisition_line_items rli\n" +
            "join ( select pp.id , pp.enddate, r.id as rnrid from  requisitions r \n" +
            "join programs p on p.id=r.programid\n" +
            "join facilities f on f.id=r.facilityid\n" +
            "join processing_periods pp on pp.id=r.periodid\n" +
            "where p.code='COVID-19' and r.status IN ('APPROVED','IN_APPROVAL', 'RELEASED', 'AUTHORIZED')  and r.facilityid =#{facility} \n" +
            "order by pp.enddate desc limit 1) a on a.rnrid=rli.rnrid ")
    List<HashMap<String,Object>> getCOVIDReportByFacility( @Param("facility") Long facility
    );

    @Select("select product as product, MAX(a.enddate::DATE) as last_update,  SUM(stockinhand) as stockOnHand,\n" +
            "SUM(quantityrequested) as ordered\n" +
            "from requisition_line_items rli\n" +
            "join ( select pp.id , pp.enddate, r.id as rnrid from  requisitions r \n" +
            "join programs p on p.id=r.programid\n" +
            "join facilities f on f.id=r.facilityid\n" +
            "join processing_periods pp on pp.id=r.periodid\n" +
            "where p.code='COVID-19' and r.status IN ('APPROVED','IN_APPROVAL', 'RELEASED', 'AUTHORIZED')  \n" +
            "order by pp.enddate desc limit 1) a on a.rnrid=rli.rnrid\n" +
            "group by product ")
    List<HashMap<String,Object>> getCOVIDReportForAllFacilities( );



    @Select("select f.name, f.id from programs_supported ps\n" +
            "join facilities f on f.id=ps.facilityid\n" +
            "join programs p on p.id=ps.programid\n" +
            "where p.code='COVID-19' ")
    List<HashMap<String,Object>> getCOVIDDesignatedFacilities( );


    @Select("select productname as product, uom, quantityordered, source, status, expectedarrivaldate::DATE, receivinglocationcode, trackingnumber, modifieddate::DATE\n" +
            " from in_bound_details where expectedarrivaldate > now()")
    List<HashMap<String,Object>> getInboundReports( );


    @Select("select productname as product, uom, quantityordered, source, status, expectedarrivaldate::DATE, receivinglocationcode, trackingnumber, bd.modifieddate::DATE\n" +
            " from in_bound_details  bd\n" +
            " join products p on p.code=bd.productcode\n" +
            " where expectedarrivaldate > now() and p.id=#{product}")
    List<HashMap<String,Object>> getInboundByProductReports(@Param("product") Long product );
}
