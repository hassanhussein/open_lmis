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

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class FacilityAggregateConsumptionQueryBuilder extends ConsumptionBuilder {

    public static String getAggregateSelect(FacilityConsumptionReportParam filter) {

        BEGIN();
        FacilityConsumptionQueryBuilder.writeBasicQuery();
        writeCommonJoinStatment();
        writePredicates(filter);
        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate ,li.packsize");
        String query = SQL();
        query = query + " order by " + getOrderString(filter) + " " +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;

    }

    public static String getAggregateTotalcount(FacilityConsumptionReportParam filter) {

        BEGIN();

        SELECT(" p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate ,li.packsize ");
        writeCommonJoinStatment();
        writePredicates(filter);
        GROUP_BY("p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate ,li.packsize");
        String query = SQL();
        query = "select count(*) from (" + query + ") as query";
        return query;

    }

    public static String getDisAggregateSelect(FacilityConsumptionReportParam filter) {

        BEGIN();
        FacilityConsumptionQueryBuilder.writeBasicQuery();
        SELECT("f.id facilityId");
        SELECT("f.name facility");
        SELECT("f.code facilityCode");
        SELECT("ft.name facilityType ");
        SELECT("f.code||'_'||p.code facProdCode");
        writeCommonJoinStatment();
        INNER_JOIN("facility_types ft ON ft.id =f.typeId");
        writePredicates(filter);
        GROUP_BY("f.id,f.name, ft.name,p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate,  li.packsize");
        ORDER_BY("f.name,p.primaryName,pp.startdate");
        String query = SQL();
        query = query + " order by " + getOrderString(filter) + " " +
                "   OFFSET " + (filter.getPage() - 1) * filter.getPageSize() + " LIMIT " + filter.getPageSize();
        return query;

    }

    public static String getDisAggregateTotalcount(FacilityConsumptionReportParam filter) {

        BEGIN();
        SELECT(" f.id,f.name, ft.name,p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,pp.name,pp.startdate,  li.packsize ");
        writeCommonJoinStatment();
        INNER_JOIN("facility_types ft ON ft.id =f.typeId");
        writePredicates(filter);
        GROUP_BY("f.id,f.name, ft.name,p.code, p.primaryName, p.dispensigUnit, p.strength, ds.code,pp.name,pp.startdate,  li.packsize");
        String query = SQL();
        query = "select count(*) from (" + query + ") as query";
        return query;

    }

    public static String getQuery(Map params) {

        FacilityConsumptionReportParam filter = (FacilityConsumptionReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return getDisAggregateSelect(filter);
        else
            return getAggregateSelect(filter);

    }

    public static String getTotalCountQuery(Map params) {

        FacilityConsumptionReportParam filter = (FacilityConsumptionReportParam) params.get("filterCriteria");
        if (filter.getDisaggregated())
            return getDisAggregateTotalcount(filter);
        else
            return getAggregateTotalcount(filter);

    }

}
