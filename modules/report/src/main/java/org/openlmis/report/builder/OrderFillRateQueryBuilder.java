/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.openlmis.report.builder;

import org.openlmis.report.model.params.FacilityConsumptionReportParam;
import org.openlmis.report.model.params.OrderFillRateReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class OrderFillRateQueryBuilder {
    public static final String EMERGENCY = "EM";
    public static final String REGULAR = "RE";

    private static String getEmergencyPredictate(OrderFillRateReportParam param) {
        String predicate = "";
        if (param.getReportType() != null) {
            if (EMERGENCY.equalsIgnoreCase(param.getReportType())) {
                predicate += " and r.emergency =true ";
            } else if (REGULAR.equalsIgnoreCase(param.getReportType())) {
                predicate += "and r.emergency = false ";
            }
        }
        return predicate;
    }

    public static String getQuery(Map params) {
        OrderFillRateReportParam queryParam = (OrderFillRateReportParam) params.get("filterCriteria");
        return getQueryStringV2(queryParam, queryParam.getUserId());
    }

    public static String getSubProdQuery(Map params) {
        OrderFillRateReportParam queryParam = (OrderFillRateReportParam) params.get("filterCriteria");
        return getQuerySubstituteProduct(queryParam, queryParam.getUserId());
    }

    private static String writePredicates(OrderFillRateReportParam param) {

        String predicate = " and r.status= 'RELEASED' and r.approved >0 " +
                "and  r.programId=" + param.getProgram() +
                " and r.periodid = " + param.getPeriod() + " ";
        if (param.getZone() != null && param.getZone() != 0) {
            predicate = predicate + " and (r.zoneid = " + param.getZone() + " or r.districtid = " + param.getZone() + " or r.parent = " + param.getZone() + " " +

                    " or r.provinceid = " + param.getZone() + ")";
        }
        predicate = predicate + getEmergencyPredictate(param);
        if(param.getProductCategory()!=null&& !param.getProductCategory().equals(0L)){
           predicate=predicate+ " and r.categoryid="+param.getProductCategory() +" ";

        }
        if (multiProductFilterBy(param.getProducts(), "r.productid", "r.tracer") != null) {
            predicate = predicate + " and " + multiProductFilterBy(param.getProducts(), "r.productid", "r.tracer");
        }


        if (param.getFacility() != 0) {
            predicate = predicate + " and r.facilityId=" + param.getFacility();
        }


        return predicate;
    }

    private static String getQueryStringV2(OrderFillRateReportParam param, Long userId) {

        String query = " SELECT r.district AS district,\n" +
                "    r.districtid AS districtid,\n" +
                "    r.provinceid AS provinceid,\n" +
                "    r.province AS province,\n" +
                "    r.facility AS facility,\n" +
                "    r.program AS program,\n" +
                "    r.programid AS programid,\n" +
                "    r.periodid AS periodid,\n" +
                "    r.period AS period,\n" +
                "    r.facilitycode AS facilitycode,\n" +
                "    r.productid AS productid,\n" +
                "    r.tracer,\n" +
                "    r.productcode,\n" +
                "    r.product,\n" +
                "    r.order AS \"order\",\n" +
                "    r.approved AS approved,\n" +
                "    r.rnrid,\n" +
                "    o.quantityshipped AS receipts,    \n" +
                "    o.substitutedquantityshipped,\n" +
                "\to.shippeddate,\n" +
                "\t o.packeddate,\n" +
                "        CASE\n" +
                "            WHEN COALESCE(r.approved::numeric, 0::numeric) = 0::numeric THEN 0::numeric\n" +
                "            ELSE round(COALESCE(o.quantityshipped, 0::bigint)::numeric / COALESCE(r.approved, 0)::numeric * 100::numeric, 2)\n" +
                "        END AS item_fill_rate\n" +
                "   FROM mv_requisition r \n" +
                "     LEFT JOIN mv_order_fulfillment o on o.orderid= r.rnrid and  o.productcode = r.productcode \n" +
                "  WHERE r.status::text = 'RELEASED'::text AND r.approved > 0\n" +
                writePredicates(param);
        query = query + " order by " + getOrderString(param) + " " +
                "   OFFSET " + (param.getPage() - 1) * param.getPageSize() + " LIMIT " + param.getPageSize();

        return query;
    }

    private static String getQuerySubstituteProduct(OrderFillRateReportParam param, Long userId) {

        String query = " select sli.id ,\n" +
                "sli.orderid as rnrid,\n" +
                "sli.productcode,\n" +
                "sli.substitutedproductcode as substitutedProductCode,\n" +
                "sli.substitutedproductname as substitutedProductName,\n" +
                "sli.substitutedproductquantityshipped as substitutedProductQuantityShipped,\n" +
                "sli.packsize\n" +
                "from shipment_line_items sli\n" +
                "where sli.substitutedproductquantityshipped>0 \n" +
                "and sli.productcode ='" + param.getProductCode() + "' and sli.orderid=" + param.getRnrId();

        return query;
    }

    public static String getQueryCount(Map params) {

        OrderFillRateReportParam param = (OrderFillRateReportParam) params.get("filterCriteria");
        String query = "  with query as (select count(*) AS totalcount,\n" +
                "    count(*) FILTER (WHERE r.approved>0) AS approved,\n" +
                "\tcount(*) FILTER (WHERE o.quantityshipped>0) AS shipped FROM mv_requisition r \n" +
                "     LEFT JOIN mv_order_fulfillment o on o.orderid= r.rnrid and  o.productcode = r.productcode \n" +
                "  WHERE r.status::text = 'RELEASED'::text AND r.approved > 0\n" +
                writePredicates(param) +
                ")\n" +
                " select q.totalcount as totalcount," +
                "q.approved as approved , q.shipped as shipped , \n" +
                "  CASE\n" +
                "  WHEN COALESCE(q.approved::numeric, 0::numeric) = 0::numeric THEN 0::numeric\n" +
                "  ELSE round(COALESCE(q.shipped, 0::bigint)::numeric / COALESCE(q.approved, 0)::numeric * 100::numeric, 2)\n" +
                "    END AS itemfillrate" +
                " from query q";

        return query;
    }

    public static String getOrderString(OrderFillRateReportParam filter) {
        String sortString = "";
        sortString = (filter.getSortBy() != null && filter.getSortBy().trim().length() > 0) ? filter.getSortBy() : " facility, productCode ";
        sortString = sortString + " " + (filter.getSortDirection() != null && filter.getSortDirection().trim().length() > 0 ? filter.getSortDirection() : " asc ");
        return sortString;
    }

    public static String getFillRateReportRequisitionStatus(Map params) {
        BEGIN();
        SELECT("distinct status\n");
        FROM("requisitions r");
        WHERE("  r.id = #{filterCriteria.rnrId}");


        String query = SQL();
        return query.concat(" LIMIT 1");
    }

    @Deprecated
    private static String getQueryString(OrderFillRateReportParam param, Long userId) {
        BEGIN();
        SELECT_DISTINCT("facilityname facility,quantityapproved as Approved,quantityreceived receipts ,productcode, product, " +
                " CASE WHEN COALESCE(quantityapproved, 0::numeric) = 0::numeric THEN 0::numeric\n" +
                "    ELSE COALESCE(quantityreceived,0 )/ COALESCE(quantityapproved,0) * 100::numeric\n" +
                "                                     END AS item_fill_rate ");
        FROM("vw_order_fill_rate join vw_districts gz on gz.district_id = vw_order_fill_rate.zoneId");
        WHERE("facilityid in (select facility_id from vw_user_facilities where user_id = #{userId} and program_id = #{filterCriteria.program} )");
        WHERE(" status in ('RELEASED') and totalproductsapproved > 0 ");
        writePredicates(param);
        GROUP_BY("product, approved, " +
                "  quantityreceived,  productcode, " +
                "  facilityname ");
        ORDER_BY("facilityname");
        String query = SQL();
        return query;
    }

    public static String getTotalProductsReceived(Map param) {
        OrderFillRateReportParam queryParam = (OrderFillRateReportParam) param.get("filterCriteria");
        BEGIN();
        SELECT("count(totalproductsreceived) quantityreceived");
        FROM("vw_order_fill_rate join vw_districts gz on gz.district_id = zoneId");
        WHERE("facilityid in (select facility_id from vw_user_facilities where user_id = #{userId} and program_id = #{filterCriteria.program})");
        WHERE("totalproductsreceived>0 and totalproductsapproved >0  and status in ('RELEASED') and periodId = #{filterCriteria.period} and programId= #{filterCriteria.program} and facilityId = #{filterCriteria.facility}");
        writePredicates(queryParam);
        GROUP_BY("totalproductsreceived");
        return SQL();
    }


    public static String getTotalProductsOrdered(Map params) {
        OrderFillRateReportParam queryParam = (OrderFillRateReportParam) params.get("filterCriteria");

        BEGIN();
        SELECT("count(totalproductsapproved) quantityapproved");
        FROM("vw_order_fill_rate join vw_districts gz on gz.district_id = zoneId");
        WHERE("facilityid in (select facility_id from vw_user_facilities where user_id = #{userId} and program_id = #{filterCriteria.program})");
        WHERE("totalproductsapproved > 0  and status in ('RELEASED') and periodId= #{filterCriteria.period} and programId= #{filterCriteria.program} and facilityId= #{filterCriteria.facility} ");
        writePredicates(queryParam);
        return SQL();

    }


    public static String getSummaryQuery(Map params) {
        OrderFillRateReportParam queryParam = (OrderFillRateReportParam) params.get("filterCriteria");
        BEGIN();
        SELECT("count(totalproductsreceived) quantityreceived");
        FROM("vw_order_fill_rate join vw_districts gz on gz.district_id = zoneId");
        WHERE("facilityid in (select facility_id from vw_user_facilities where user_id = #{userId} and program_id = #{filterCriteria.program})");
        WHERE("totalproductsreceived>0 and totalproductsapproved > 0 and  status in ('RELEASED') and periodId= #{filterCriteria.period} and programId= #{filterCriteria.program} and facilityId= #{filterCriteria.facility}");
        writePredicates(queryParam);
        GROUP_BY("totalproductsreceived");
        String query = SQL();
        RESET();
        BEGIN();
        SELECT("count(totalproductsapproved) quantityapproved");
        FROM("vw_order_fill_rate join vw_districts gz on gz.district_id = zoneId");
        WHERE("status in ('RELEASED') and totalproductsapproved > 0 and periodId= #{filterCriteria.period} and programId= #{filterCriteria.program} and facilityId= #{filterCriteria.facility}");
        writePredicates(queryParam);
        query += " UNION " + SQL();
        return query;
    }


}
