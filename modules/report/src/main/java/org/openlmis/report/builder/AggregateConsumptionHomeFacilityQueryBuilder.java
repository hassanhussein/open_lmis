package org.openlmis.report.builder;

import org.openlmis.report.model.params.AggregateConsumptionReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.reportTypeFilteredBy;

public class AggregateConsumptionHomeFacilityQueryBuilder {
    public static String getAggregateSelect(AggregateConsumptionReportParam filter) {

        BEGIN();
        SELECT("p.code");
        SELECT("p.primaryName || ' '|| coalesce(p.strength,'') ||' '|| coalesce(ds.code,'') || ' (' || coalesce(p.dispensingunit, '-') || ')' as product");
        SELECT("sum(li.quantityDispensed) dispensed");
        SELECT("sum(li.normalizedConsumption) consumption");
        SELECT("ceil(sum(li.quantityDispensed) / (sum(li.packsize)/count(li.productCode))::float) consumptionInPacks");
        SELECT("ceil(sum(li.normalizedConsumption) / (sum(li.packsize)/count(li.productCode))::float) adjustedConsumptionInPacks ");
        FROM("requisition_line_items li");
        INNER_JOIN("requisitions r on r.id = li.rnrid");
        INNER_JOIN("facilities f on r.facilityId = f.id ");
        INNER_JOIN("vw_districts d on d.district_id = f.geographicZoneId ");
        INNER_JOIN("processing_periods pp on pp.id = r.periodId");
        INNER_JOIN("products p on p.code::text = li.productCode::text");
        INNER_JOIN("program_products ppg on ppg.programId = r.programId and ppg.productId = p.id");
        INNER_JOIN("dosage_units ds ON ds.id = p.dosageunitid");

        writePredicates(filter);

        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code");
        ORDER_BY("p.primaryName");
        return SQL();

    }


    public static String getDisAggregateSelect(AggregateConsumptionReportParam filter) {

        BEGIN();
        SELECT("f.code facilityCode");
        SELECT("f.name facility");
        SELECT("ft.name facilityType ");
        SELECT("p.code");
        SELECT("p.primaryName || ' '|| coalesce(p.strength,'') ||' '|| coalesce(ds.code,'') || ' (' || coalesce(p.dispensingunit, '-') || ')' as product");
        SELECT("sum(li.quantityDispensed) dispensed");
        SELECT("sum(li.normalizedConsumption) consumption");
        SELECT("ceil(sum(li.quantityDispensed) / (sum(li.packsize)/count(li.productCode))::float) consumptionInPacks");
        SELECT("ceil(sum(li.normalizedConsumption) / (sum(li.packsize)/count(li.productCode))::float) adjustedConsumptionInPacks ");// FROM("vw_materialized_aggregate_consumption vw");
        FROM("requisition_line_items li");
        INNER_JOIN("requisitions r on r.id = li.rnrid");
        INNER_JOIN("facilities f on r.facilityId = f.id ");
        INNER_JOIN("vw_districts d on d.district_id = f.geographicZoneId ");
        INNER_JOIN("processing_periods pp on pp.id = r.periodId");
        INNER_JOIN("products p on p.code::text = li.productCode::text");
        INNER_JOIN("program_products ppg on ppg.programId = r.programId and ppg.productId = p.id");
        INNER_JOIN("facility_types ft ON ft.id =f.typeId");
        INNER_JOIN("dosage_units ds ON ds.id = p.dosageunitid");
        writePredicates(filter);
        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,f.Code,f.name,ft.name ");
        ORDER_BY("p.primaryName");
        return SQL();

    }

    private static void writePredicates(AggregateConsumptionReportParam filter) {

        WHERE(programIsFilteredBy("r.programId"));
        WHERE(multiPeriodFilterBy(filter.getMultiPeriods(), "r.periodId"));

        WHERE(userHasPermissionOnFacilityBy("r.facilityId"));
        WHERE(rnrStatusFilteredBy("r.status", filter.getAcceptedRnrStatuses()));/*
    WHERE(periodStartDateRangeFilteredBy("pp.startdate", filter.getPeriodStart().trim()));
    WHERE(periodEndDateRangeFilteredBy("pp.enddate", filter.getPeriodEnd().trim()));*/



        if(filter.getProductCategory() != 0){
            WHERE( productCategoryIsFilteredBy("ppg.productCategoryId"));
        }

        if (multiProductFilterBy(filter.getProducts(), "p.id", "p.tracer") != null) {
            WHERE(multiProductFilterBy(filter.getProducts(), "p.id", "p.tracer"));
        }

        if (filter.getZone() != 0) {
            WHERE( geoZoneIsFilteredBy("d") );
        }

        if (filter.getAllReportType()) {
            WHERE("r.emergency in (true,false)");
        } else {
            WHERE(reportTypeFilteredBy("r.emergency"));
        }
    }

    public static String getQuery(Map params){

        AggregateConsumptionReportParam filter = (AggregateConsumptionReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return getDisAggregateSelect(filter);
        else
            return getAggregateSelect(filter);

    }

}
