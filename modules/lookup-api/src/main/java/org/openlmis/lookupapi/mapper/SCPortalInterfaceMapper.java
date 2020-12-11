package org.openlmis.lookupapi.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface SCPortalInterfaceMapper {

    @Select(" SELECT 'COVID19' as \"program\", \n" +
            "p.code as \"product_code\", \n" +
            "p.primaryName as name\n, " +
            " p.description || p.dispensingunit as description, " +
            " case when pc.code = 'msus' then 'medical supplies' else 'pharmaceuticals' end as category  " +
            " FROM program_products pP\n" +
            "\n" +
            "JOIN products p ON p.id = pp.productId \n" +
            "join programs pr ON pr.id = pp.programId and pr.id =1\n" +
            "join product_categories pc ON pp.productcategoryid = pc.id\n" +
            "")
    List<HashMap<String, Object>> getAllProducts();

    @Select(
            "    SELECT f.hfrcode as facility_id, productcode as product_code\n" +
                    "            , 4 as level, \n" +
                    "            CASE WHEN stockinhand is null then 0::text else stockinhand::text END as quantity, to_char(r.modifieddate, 'yyyy-MM-dd') as updated_at\n" +
                    "           from requisitions r\n" +
                    "            JOIN requisition_line_items i on r.id = i.rnrid\n" +
                    "            JOIN facilities F on r.facilityId = F.ID\n" +
                    "             JOIN processing_periods per ON r.periodiD = per.id\n" +
                    "             where f.code::text is not null\n" +
                    "                           and  f.hfrcode::text not in ('.','-') \n" +
                    "             ORDER BY R.ID DESC LIMIT 1000"+
          //  " and per.startDate>= #{startDate}::DATE and per.endDate <=#{endDate}::DATE" +

            "")

    List<HashMap<String, Object>> getStockInHand(@Param("startDate") String startDate,
                                                 @Param("endDate") String endDate);

    @Select("   SELECT f.hfrcode as facility_id, productcode as product_code,\n" +
            "                     CASE WHEN quantityReceived IS NULL THEN 0::TEXT ELSE quantityReceived::text END as quantity_received,\n" +
            "                     CASE WHEN beginningBalance IS NULL THEN 0::TEXT ELSE beginningBalance::text END as begining_balance, \n" +
            "                       case when totallossesandadjustments < 0 then \n" +
            "                       (-1 * coalesce(totallossesandadjustments,0))::text  else\n" +
            "                       totallossesandadjustments::text end as adjustment_quantity,\n" +
            "                       to_char(pp.enddate,'YYYY-MM-dd') as period,\n" +
            "                       lower(ft.description) as reason\n" +
            "                        from requisitions r\n" +
            "                        JOIN requisition_line_items i on r.id = i.rnrid\n" +
            "                        JOIN facilities F on r.facilityId = F.ID\n" +
            "                        JOIN requisition_line_item_losses_adjustments L on l.requisitionlineitemid = i.id\n" +
            "                        JOIN processing_periods pp on r.periodId = pp.id\n" +
            "                        JOIN losses_adjustments_types ft ON l.type = ft.name \n" +
            "                      where f.hfrcode::text is not null  \n" +
            "               and  f.hfrcode::text not in ('.','-') \n" +
            "                and type in ('LOST', 'DAMAGED','EXPIRED' )\n" +
            " ORDER BY R.ID DESC LIMIT 1000" +
           // " and pp.startDate>= #{startDate}::DATE and pp.endDate <=#{endDate}::DATE" +

            "                        ")
    List<HashMap<String, Object>> getWastages(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select(" SELECT f.hfrcode as facility_id, productcode as \"product_code\",\n" +
            "          CASE WHEN amc is null THEN 0::TEXT ELSE amc::text END as \"actual_consumed\",\n" +
            "                      to_char(pp.enddate,'YYYY-MM-dd') as period,\n" +
            "           normalizedconsumption::text as \"forecast_consumed\"\n" +
            "            from requisitions r\n" +
            "            JOIN requisition_line_items i on r.id = i.rnrid\n" +
            "            JOIN facilities F on r.facilityId = F.ID\n" +
            "            JOIN requisition_line_item_losses_adjustments L on l.requisitionlineitemid = i.id\n" +
            "            JOIN processing_periods pp on r.periodId = pp.id\n" +
            "            JOIN losses_adjustments_types ft ON l.type = ft.name\n" +
            "            where f.hfrcode::text is not null and f.hfrcode::text not in ('.','-')\n" +
            "  ORDER BY R.ID DESC LIMIT 1000" +
           // " and pp.startDate::date >= #{startDate}::DATE and pp.endDate::date <=#{endDate}::DATE" +
            "            ")
    List<HashMap<String,Object>> getForeCastingData(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select(" SELECT i.amc as \"monthsOfStock\", i.quantityDispensed as \"consumedQuantity\",  pr.code as \"programCode\", f.code as \"facilityId\", productcode as \"productCode\"\n" +
            ", 4 as \"facilityLevel\", stockinhand::text as \"quantity\", to_char(r.modifieddate, 'yyyy-MM-dd') as \"period\" \n" +
            " , r.id as \"stockId\" " +
            "from requisitions r\n" +
            "JOIN requisition_line_items i on r.id = i.rnrid\n" +
            "JOIN facilities F on r.facilityId = F.ID" +
            " JOIN programs pr On r.programId = pr.id" +
            " where f.code::text is not null\n" +
            "               and  f.code::text not in ('.','-') \n" +
            "")
    List<HashMap<String, Object>> getThScpStockInHand();
}
