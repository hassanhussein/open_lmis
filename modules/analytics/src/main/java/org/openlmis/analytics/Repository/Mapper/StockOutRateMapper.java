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



    @Select("select sum(normalizedconsumption) as totalConsumption,sum(stockinhand) as totalsTockinhand, pp.name, pp.id, rli.productcode from requisition_line_items rli\n" +
            "join requisitions r on r.id=rli.rnrid\n" +
            "join processing_periods pp on pp.id=r.periodid\n" +
            "where extract(year from pp.startdate) = #{year}  and (rli.productcode='10010164AB' OR rli.productcode='10010022AB' ) and pp.scheduleid= #{schedule}\n" +
            "group by pp.name ,pp.id, rli.productcode order by pp.id asc")
    List<HashMap<String,Object>> getTLEAndTLDConsumption(@Param("year") Long year,
                                                         @Param("schedule") Long schedule);



}
