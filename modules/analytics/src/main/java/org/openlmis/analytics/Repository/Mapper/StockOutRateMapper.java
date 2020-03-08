package org.openlmis.analytics.Repository.Mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface  StockOutRateMapper {

    @Select("    SELECT  SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) as numerator, count(*) as denominator, \n" +
            "  ROUND(100.0 * (SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) )/ COUNT(product),2) AS percentage, pg.id, pg.name, a.processing_period_name\n" +
            " FROM ( SELECT * from mv_stock_imbalance_by_facility_report ) a\n" +
            " join products p on p.code=a.productcode\n" +
            " join product_groups pg on pg.id=p.productgroupid\n" +
            "  group by pg.id, pg.name, a.processing_period_name")
    List<HashMap<String,Object>> getStockOutRate(@Param("program") Long program,
                                                            @Param("period") Long period);


    @Select("SELECT  a.product, a.region_name,a.district_name,\n" +
            "   ROUND(100.0 * (SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) )/ COUNT(product),2) AS stockOutPercentage,\n" +
            "  ROUND(100.0 * (SUM(CASE WHEN status='OS' THEN 1 ELSE 0 END) )/ COUNT(product),2) AS overStockPercentage,\n" +
            "   ROUND(100.0 * (SUM(CASE WHEN status='US' THEN 1 ELSE 0 END) )/ COUNT(product),2) AS underStockPercentage,\n" +
            "    ROUND(100.0 * (SUM(CASE WHEN status='UK' THEN 1 ELSE 0 END) )/ COUNT(product),2) AS unknownStockPercentage,\n" +
            "    ROUND(100.0 * (SUM(CASE WHEN status='SP' THEN 1 ELSE 0 END) )/ COUNT(product),2) AS adequatelyStockPercentage\n" +
            " FROM ( SELECT * from mv_stock_imbalance_by_facility_report ) a\n" +
            " join products p on p.code=a.productcode\n" +
            " join processing_periods pp on pp.id=a.periodid\n" +
            " WHERE a.productid=#{product}  and pp.id=#{period}  and a.programid=#{program} \n" +
            "  group by a.productcode, a.product, a.region_name, a.district_name")
    List<HashMap<String,Object>> getStockStatusByLocation(@Param("program") Long program,
                                                 @Param("period") Long period, @Param("product") Long product);



    @Select("select sum(cs.quantityDispensed) as  totalConsumption, cs.periodname as name, cs.productid as id, cs.productcode, cs.periodid\n" +
            "             from mv_dashboard_consumption_summary cs\n" +
            "             join processing_periods pp on pp.id=cs.periodid  \n" +
            "             where productcode in  ('10010164AB' ,'10010022AB' ) and extract(year from pp.startdate) =  #{year} and cs.scheduleid= #{schedule} and pp.enableorder=true\n" +
            "             group by cs.periodname, cs.productid, cs.productcode, cs.periodid order by cs.periodid asc")
    List<HashMap<String,Object>> getTLEAndTLDConsumption(@Param("year") Long year,
                                                         @Param("schedule") Long schedule);




    @Select("SELECT  SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) as numerator, count(*) as denominator, \n" +
            "  ROUND(100.0 * (SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) )/ COUNT(product),2) AS percentage, p.code, p.primaryname, a.processing_period_name, pp.id\n" +
            " FROM ( SELECT * from mv_stock_imbalance_by_facility_report ) a\n" +
            " join products p on p.code=a.productcode\n" +
            " join processing_periods pp on pp.id=a.periodid\n" +
            "where extract(year from pp.startdate) = #{year}  and p.id =#{product}  and pp.scheduleid= #{schedule}\n" +
            "  group by p.code, p.primaryname, a.processing_period_name, pp.id order by pp.id asc")
    List<HashMap<String,Object>> getStockOutRateByProduct(@Param("year") Long year,
                                                 @Param("schedule") Long schedule,
                                                          @Param("product") Long product);


    @Select("  SELECT  district_name,region_name, SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) as stockoutIncidence, count(*) as totalIncidence, \n" +
            " ROUND(100.0 * (SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) )/ COUNT(product),2) AS percentage, a.processing_period_name, pp.enddate as reported \n" +
            "FROM ( SELECT * from mv_stock_imbalance_by_facility_report ) a\n" +
            " join processing_periods pp on pp.id=a.periodid\n" +
            "where extract(year from pp.enddate) = #{year} and tracer=true \n" +
            "  group by district_name,region_name, a.processing_period_name, pp.enddate order by district_name ")
    List<HashMap<String,Object>> getStockOutRateTrendOfTracerProducts(@Param("year") Long year);


    @Select("   SELECT  district_name,region_name, SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) as stockoutIncidence, count(*) as totalIncidence, \n" +
            " ROUND(100.0 * (SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) )/ COUNT(product),2) AS percentage, a.processing_period_name, pp.enddate as reported \n" +
            "FROM ( SELECT * from mv_stock_imbalance_by_facility_report ) a\n" +
            " join processing_periods pp on pp.id=a.periodid\n" +
            " join products p on p.code=a.productcode\n" +
            "where extract(year from pp.enddate) = #{year} and p.id =#{product} \n" +
            "  group by district_name,region_name, a.processing_period_name, pp.enddate order by district_name  ")
    List<HashMap<String,Object>> getStockOutRateTrendOfProducts(@Param("year") Long year, @Param("product") Long product);



    @Select("SELECT  a.product, a.region_name,a.district_name,\n" +
            "SUM(a.stockinhand) as stockinhand,\n" +
            "SUM(a.beginningbalance) as beginningbalance,\n" +
            "SUM(a.quantityreceived) as quantityreceived,\n" +
            "SUM(a.quantityin) as quantityin,\n" +
            "SUM(a.quantityout) as quantityout,\n" +
            "SUM(a.quantityexpiredloststolen) as quantityexpiredloststolen,\n" +
            "SUM(a.quantitydispensed) as consumption,\n" +
            "SUM(a.price * (a.quantityreceived/p.packsize) ) as totalCostOfReceived,\n" +
            "SUM(a.price * (a.quantityexpiredloststolen/p.packsize) ) as totalCostOfquantityexpiredloststolen,\n" +
            "SUM(a.price * (a.quantitydispensed/p.packsize) ) as totalCostOfquantitydispensed,\n" +
            "SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) as stockOutIncidence,\n" +
            "SUM(CASE WHEN status='OS' THEN 1 ELSE 0 END) as overStockIncidence,\n" +
            "COUNT(*) as totalIncidence \n" +
            " FROM  mv_commodities_details  a\n" +
            " join products p on p.code=a.productcode\n" +
            " join processing_periods pp on pp.id=a.periodid\n" +
            " join program_products pps on pps.programid=a.programid and pps.productid=p.id\n" +
            " WHERE p.id=#{product}  and pp.id=#{period}  and a.programid=#{program}\n" +
            "  group by a.productcode,a.product, a.region_name, a.district_name, a.periodid")
    List<HashMap<String,Object>> getAllCommoditiesDetailsByDistrict(@Param("program") Long program,
                                                          @Param("period") Long period, @Param("product") Long product);

    @Select(" select  district_name, region_name, SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) as stockOutIncidence," +
            "            SUM(CASE WHEN status='UK' THEN 1 ELSE 0 END) as unknownIncidence,\n" +
            "            SUM(CASE WHEN status='OS' THEN 1 ELSE 0 END) as overStockIncidence,\n" +
            "            SUM(CASE WHEN status='US' THEN 1 ELSE 0 END) as underStockIncidence,\n" +
            "            SUM(CASE WHEN status='SP' THEN 1 ELSE 0 END) as adeliquateStockIncidence," +
            " count(*) as totalIncidence,\n" +
            "            MAX(processing_period_name) || ' ' || Max(year) as reported  \n" +
            "             from mv_stock_imbalance_by_facility_report  msifr\n" +
            "             inner join (\n" +
            "       select productid, max(periodid) from mv_stock_imbalance_by_facility_report\n" +
            "       where tracer=true\n" +
            "             group by productid\n" +
            ") a on a.productid=msifr.productid\n" +
            "             where tracer=true\n" +
            "             group by district_name, region_name")
    List<HashMap<String,Object>> getLatestReportedStockOnHandForTracer();



    @Select(" select  district_name, region_name, SUM(CASE WHEN status='SO' THEN 1 ELSE 0 END) as stockOutIncidence," +
            "            SUM(CASE WHEN status='UK' THEN 1 ELSE 0 END) as unknownIncidence,\n" +
            "            SUM(CASE WHEN status='OS' THEN 1 ELSE 0 END) as overStockIncidence,\n" +
            "            SUM(CASE WHEN status='US' THEN 1 ELSE 0 END) as underStockIncidence,\n" +
            "            SUM(CASE WHEN status='SP' THEN 1 ELSE 0 END) as adeliquateStockIncidence," +
            " count(*) as totalIncidence,\n" +
            "            MAX(processing_period_name) || ' ' || Max(year) as reported  \n" +
            "             from mv_stock_imbalance_by_facility_report  msifr\n" +
            "             inner join (\n" +
            "       select productid, max(periodid) from mv_stock_imbalance_by_facility_report\n" +
            "       where productid=#{product}\n" +
            "             group by productid\n" +
            ") a on a.productid=msifr.productid\n" +
            "             where msifr.productid=#{product}\n" +
            "             group by district_name, region_name")
    List<HashMap<String,Object>> getLatestReportedStockOnHandForProductByDistrict( @Param("product") Long product);


    @Select("select district_name, region_name, status, count(*) as totalIncidence from mv_stock_imbalance_by_facility_report where periodid in (\n" +
            "select periodid from mv_latest_reported_stock_status\n" +
            "where tracer=true)\n" +
            "and tracer=true\n" +
            "group by status, district_name, region_name")
    List<HashMap<String,Object>> getLatestStockImbalanceReportByDistrictForTracer();


    @Select("select district_name, region_name, status, count(*) as totalIncidence  from mv_stock_imbalance_by_facility_report where periodid in (\n" +
            "select periodid from mv_latest_reported_stock_status\n" +
            "where productid=#{product})\n" +
            "and  productid=#{product}\n" +
            "group by status, district_name, region_name")
    List<HashMap<String,Object>> getLatestStockImbalanceReportByDistrictForProduct(@Param("product") Long product );


}
