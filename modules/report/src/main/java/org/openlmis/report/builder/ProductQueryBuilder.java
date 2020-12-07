
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


import org.apache.ibatis.annotations.Select;
import org.openlmis.report.model.params.ProductReportParam;

import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.productCategoryIsFilteredBy;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.productStatusFilteredBy;
import static org.openlmis.report.builder.helpers.RequisitionPredicateHelper.programIsFilteredBy;

public class ProductQueryBuilder {

    public static String getQuery(Map params) {
        String query = "";
        ProductReportParam filter = (ProductReportParam) params.get("filterCriteria");
        ;
        String reportType = filter.getProductStatus() != null && !filter.getProductStatus().isEmpty() ?
                filter.getProductStatus().replaceAll(",", "','").replaceAll("AC", "t").replaceAll("IN", "f") : "f";
        BEGIN();
        SELECT("p.id, p.code, p.fullname,   p.primaryName As primaryName, \n" +
                "            p.strength, \n" +
                "            p.dispensingUnit AS dispensingUnit, \n" +
                "            du.code as dosageCode, \n" +
                "            du.id AS dosageUnitId,           \n" +
                "            p.packSize, \n" +
                "            p.packroundingthreshold AS packRoundingThreshold, \n" +
                "            p.dosesperdispensingunit AS dosesPerDispensingUnit, \n" +
                "            p.fullsupply AS fullSupply, \n" +
                "            p.active AS active, \n" +
                "pm.id as programid,pm.name as program, pm.code as programcode");
        FROM(" products p");
        INNER_JOIN(" program_products prp on prp.productid=p.id");
        INNER_JOIN("programs pm on pm.id = prp.programid");
        INNER_JOIN(" product_categories pc on pc.id=prp.productcategoryid");
        LEFT_OUTER_JOIN(" dosage_units du ON  p.dosageunitid =  du.id");
        predicate(filter, reportType);
        query = SQL();
        return query;
    }

    public static void predicate(ProductReportParam reportParam, String reportType) {

        WHERE(productStatusFilteredBy("p.active", reportType));
        if (reportParam.getProgram() != null && !reportParam.getProgram().equals(0l)) {
            WHERE(programIsFilteredBy("prp.programId"));
        }
        if (reportParam.getProductCategory() != null && !reportParam.getProductCategory().equals(0l)) {
            WHERE(productCategoryIsFilteredBy("pc.id"));
        }
    }
}
