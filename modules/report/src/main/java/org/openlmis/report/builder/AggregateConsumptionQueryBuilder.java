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

import org.openlmis.report.model.params.AggregateConsumptionReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class AggregateConsumptionQueryBuilder extends ConsumptionBuilder{

    public static String getOrderString(AggregateConsumptionReportParam filter) {
        String sortString = "";
        sortString = (filter.getSortBy() != null && filter.getSortBy().trim().length() > 0) ? filter.getSortBy() : " p.primaryName ";
        sortString = sortString+" " + (filter.getSortDirection() != null && filter.getSortDirection().trim().length() > 0 ? filter.getSortDirection() : " asc ");
        return sortString;
    }

    public static String getAggregateSelectCount(AggregateConsumptionReportParam filter) {

        BEGIN();
        SELECT("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code, li.packsize");
        writeCommonJoinStatment();
        writePredicates(filter);
        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code, li.packsize");
        String query = SQL();
        query = "select count(*) from ( " +query+ " ) as c";
        return query;

    }

    public static String getDisAggregateSelectCount(AggregateConsumptionReportParam filter) {

        BEGIN();
        SELECT(" p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,f.Code,f.name,ft.name , li.packsize ");

        writeCommonJoinStatment();
        INNER_JOIN("facility_types ft ON ft.id =f.typeId");
        writePredicates(filter);
        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,f.Code,f.name,ft.name , li.packsize");
        String query = SQL();
        query = "select count(*) from ( " +query+ " ) as c";
        return query;

    }

    public static void writeCommonSelect() {
        SELECT("p.code");
        SELECT("p.primaryName || ' '|| coalesce(p.strength,'') ||' '|| coalesce(ds.code,'') || ' (' || coalesce(p.dispensingunit, '-') || ')' as product");
        SELECT("sum(li.quantityDispensed) dispensed");
        SELECT("sum(li.normalizedConsumption) consumption");
        SELECT("ceil(sum(li.quantityDispensed /li.packsize)) consumptionInPacks");
        SELECT("ceil(sum(li.normalizedConsumption/ li.packsize)) adjustedConsumptionInPacks ");
        writeCommonJoinStatment();
    }


    public static String getAggregateSelect(AggregateConsumptionReportParam filter) {
        BEGIN();
        writeCommonSelect();
        writePredicates(filter);
        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code, li.packsize");
        String query = SQL();
        query = query + " order by " + getOrderString(filter) + " " +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;
    }

    public static String getDisAggregateSelect(AggregateConsumptionReportParam filter) {

        BEGIN();
        SELECT("f.code facilityCode");
        SELECT("f.name facility");
        SELECT("ft.name facilityType");
        SELECT("f.code+'_'+p.code as facProdCode");
        writeCommonSelect();
        INNER_JOIN("facility_types ft ON ft.id =f.typeId");
        writePredicates(filter);
        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,f.Code,f.name,ft.name , li.packsize");
        String query = SQL();
        query = query + " order by " + getOrderString(filter) + " " +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;

    }

    private static void writePredicates(AggregateConsumptionReportParam filter) {

        WHERE(programIsFilteredBy("r.programId"));
        WHERE(periodIsFilteredBy("r.periodId"));
        WHERE(rnrStatusFilteredBy("r.status", filter.getAcceptedRnrStatuses()));
        if (filter.getProductCategory() != 0) {
            WHERE(productCategoryIsFilteredBy("ppg.productCategoryId"));
        }

        if (multiProductFilterBy(filter.getProducts(), "p.id", "p.tracer") != null) {
            WHERE(multiProductFilterBy(filter.getProducts(), "p.id", "p.tracer"));
        }

        if (filter.getZone() != 0) {
            WHERE(geoZoneIsFilteredBy("d"));
        }

        if (filter.getAllReportType()) {
            WHERE("r.emergency in (true,false)");
        } else {
            WHERE(reportTypeFilteredBy("r.emergency"));
        }
    }

    public static String getQuery(Map params) {

        AggregateConsumptionReportParam filter = (AggregateConsumptionReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return getDisAggregateSelect(filter);
        else
            return getAggregateSelect(filter);

    }

    public static String getCountQuery(Map params) {

        AggregateConsumptionReportParam filter = (AggregateConsumptionReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return getDisAggregateSelectCount(filter);
        else
            return getAggregateSelectCount(filter);

    }

}
