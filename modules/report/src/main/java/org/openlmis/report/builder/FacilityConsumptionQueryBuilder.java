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
import org.openlmis.report.model.params.FacilityConsumptionReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.*;

public class FacilityConsumptionQueryBuilder extends ConsumptionBuilder {

    public static void writeBasicQuery() {
        SELECT("p.code");
        SELECT("pp.name periodName");
        SELECT("pp.startdate periodStart");
        SELECT("p.primaryName || ' '|| coalesce(p.strength,'') ||' '|| coalesce(ds.code,'') || ' (' || coalesce(p.dispensingunit, '-') || ')' as product");
        SELECT("sum(li.quantityDispensed) dispensed");
        SELECT("sum(li.normalizedConsumption) consumption");
        SELECT("ceil(sum(li.quantityDispensed) / (sum(li.packsize)/count(li.productCode))::float) consumptionInPacks");
        SELECT("ceil(sum(li.normalizedConsumption) / (sum(li.packsize)/count(li.productCode))::float) adjustedConsumptionInPacks ");
    }

    public static String getAggregateSelect(FacilityConsumptionReportParam filter) {

        BEGIN();
        writeBasicQuery();
        writeCommonJoinStatment();
        writePredicates(filter);
        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate");
        String query = SQL();
        query = query + " order by " + getOrderString(filter) + " " +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;

    }

    public static String getDisAggregateSelect(FacilityConsumptionReportParam filter) {

        BEGIN();
        SELECT("f.id facilityId");
        SELECT("f.code facilityCode");
        SELECT("f.name facility");
        SELECT("ft.name facilityType ");
        SELECT("f.code||p.code facProdCode");
        writeBasicQuery();
        writeCommonJoinStatment();
        INNER_JOIN("facility_types ft ON ft.id =f.typeId");


        writePredicates(filter);

        GROUP_BY("f.id,f.name,f.code, ft.name,p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate");
        String query = SQL();
        query = query + " order by " + getOrderString(filter) + " " +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;

    }


    public static String getQuery(Map params) {

        FacilityConsumptionReportParam filter = (FacilityConsumptionReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return getDisAggregateSelect(filter);
        else
            return getAggregateSelect(filter);

    }

}
