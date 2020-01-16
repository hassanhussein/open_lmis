package org.openlmis.report.builder;


import org.openlmis.report.model.params.AggregateStockStatusReportParam;
import org.openlmis.report.model.params.FacilityConsumptionReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class AggregateStockStatusReportQueryBuilder {

    public static String getAggregateSelect(AggregateStockStatusReportParam filter) {

        BEGIN();
        SELECT("p.code");
        SELECT("pp.name periodName");
        SELECT("pp.startdate periodStart");
        SELECT("p.primaryName || ' '|| coalesce(p.strength,'') ||' '|| coalesce(ds.code,'') || ' (' || coalesce(p.dispensingunit, '-') || ')' as product");
        SELECT("sum(li.quantityDispensed) dispensed");
        SELECT("sum(li.stockInhand) soh");
        SELECT("sum(li.normalizedConsumption) consumption");
        SELECT("ceil(sum(li.quantityDispensed) / (sum(li.packsize)/count(li.productCode))::float) consumptionInPacks");
        SELECT("ceil(sum(li.normalizedConsumption) / (sum(li.packsize)/count(li.productCode))::float) adjustedConsumptionInPacks ");
        SELECT(" CASE WHEN COALESCE(SUM(li.amc), 0) = 0 THEN 0::numeric ELSE round((SUM(li.stockInhand::double precision) / SUM(li.amc::double precision))::numeric, 1) END AS mos");

        FROM("requisition_line_items li");
        INNER_JOIN("requisitions r on r.id = li.rnrid");
        INNER_JOIN("facilities f on r.facilityId = f.id ");
        INNER_JOIN("vw_districts d on d.district_id = f.geographicZoneId ");
        INNER_JOIN("processing_periods pp on pp.id = r.periodId");
        INNER_JOIN("products p on p.code::text = li.productCode::text");
        INNER_JOIN("program_products ppg on ppg.programId = r.programId and ppg.productId = p.id");
        INNER_JOIN("dosage_units ds ON ds.id = p.dosageunitid");

        writePredicates(filter);

        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate,pp.id");
        ORDER_BY("p.primaryName,pp.id");
        return SQL();
    }


    public static String getDisAggregateSelect(AggregateStockStatusReportParam filter) {

        BEGIN();
        SELECT(" SUM(facility_approved_products.minmonthsofstock) minMonthsOfStock");
        SELECT(" SUM(facility_approved_products.maxmonthsofstock) maxMonthsOfStock");
        SELECT("d.region_name region");
        SELECT("d.district_name district");
        SELECT("p.code");
        SELECT("pp.name periodName");
        SELECT("pp.startdate periodStart");
        SELECT("f.id facilityId");
        SELECT("f.name facility");
        SELECT("ft.name facilityType ");
        SELECT("p.primaryName || ' '|| coalesce(p.strength,'') ||' '|| coalesce(ds.code,'') || ' (' || coalesce(p.dispensingunit, '-') || ')' as product");
        SELECT("sum(li.quantityDispensed) dispensed");
        SELECT("sum(li.normalizedConsumption) consumption");
        SELECT("ceil(sum(li.quantityDispensed) / (sum(li.packsize)/count(li.productCode))::float) consumptionInPacks");
        SELECT("ceil(sum(li.normalizedConsumption) / (sum(li.packsize)/count(li.productCode))::float) adjustedConsumptionInPacks ");
        SELECT(" CASE WHEN COALESCE(SUM(li.amc), 0) = 0 THEN 0::numeric ELSE round((SUM(li.stockInhand::double precision) / SUM(li.amc::double precision))::numeric, 1) END AS mos");
        SELECT("sum(li.stockInhand) soh");
        SELECT("COALESCE(sum(li.amc),0) amc");
        FROM("requisition_line_items li");
        INNER_JOIN("requisitions r on r.id = li.rnrid");
        INNER_JOIN("facilities f on r.facilityId = f.id ");
        INNER_JOIN("vw_districts d on d.district_id = f.geographicZoneId ");
        INNER_JOIN("processing_periods pp on pp.id = r.periodId");
        INNER_JOIN("products p on p.code::text = li.productCode::text");
        INNER_JOIN("program_products ppg on ppg.programId = r.programId and ppg.productId = p.id");
        INNER_JOIN("facility_types ft ON ft.id =f.typeId");
        INNER_JOIN("dosage_units ds ON ds.id = p.dosageunitid");
        LEFT_OUTER_JOIN(" facility_approved_products ON ft.Id = facility_approved_products.facilitytypeid AND ppg.id = facility_approved_products.programproductid ");

        writePredicates(filter);

        GROUP_BY("f.id,f.name, ft.name,p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate,pp.id,d.district_name,d.region_name ");
        ORDER_BY("f.name,p.primaryName,pp.id");
        return SQL();

    }




    private static void writePredicates(AggregateStockStatusReportParam filter) {

        WHERE(programIsFilteredBy("r.programId"));
//        WHERE(userHasPermissionOnFacilityBy("r.facilityId"));
        WHERE(rnrStatusFilteredBy("r.status", filter.getAcceptedRnrStatuses()));
        WHERE(periodStartDateRangeFilteredBy("pp.startdate", filter.getPeriodStart().trim()));
        WHERE(periodEndDateRangeFilteredBy("pp.enddate", filter.getPeriodEnd().trim()));
        WHERE("li.skipped = false ");
        WHERE("r.emergency = false");
        /*  if(filter.getFacility()!=null&&filter.getFacility()!=0) {
            WHERE(facilityIsFilteredBy("f.id"));
        }*/

        if (filter.getZone() != 0) {
            WHERE(geoZoneIsFilteredBy("d"));
        }
        if(filter.getProduct() != 0){
            WHERE(productFilteredBy("p.id"));
        }

        if(filter.getSchedule() != 0){

            WHERE(scheduleFilteredBy("scheduleId"));
        }

      /*  if (filter.getAllReportType()) {
            WHERE("r.emergency in (true,false)");
        } else {
            WHERE(reportTypeFilteredBy("r.emergency"));
        }*/
    }



    public static String getQuery(Map params) {

        AggregateStockStatusReportParam filter = (AggregateStockStatusReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return getDisAggregateSelect(filter);
        else
            return getAggregateSelect(filter);

    }






}
