package org.openlmis.report.builder;

import java.util.Map;

public class ProductListReportQueryBuilder {

    public static String getQuery(Map params)  {

        Map filterCriteria = (Map) params.get("filterCriteria");

        return "    SELECT fap.*, pp.*, pgm.*, FT.name as facilityType, pgm.name as programName, pgm.active, p.code productCode, p.primaryName productName,\n" +
                "   pgm.code as program_code, \n" +
                "      p.*, p.code as product_code \n" +
                "      FROM facility_approved_products fap \n" +
                "      --INNER JOIN facilities f ON f.typeId = fap.facilityTypeId\n" +
                "      INNER JOIN FACILITY_TYPES FT ON FT.id = fap.facilityTypeId\n" +
                "      INNER JOIN program_products pp ON pp.id = fap.programProductId\n" +
                "      INNER JOIN products p ON p.id = pp.productId \n" +
                "      INNER JOIN product_categories pc ON pc.id = pp.productCategoryId \n" +
                "      INNER JOIN programs pgm ON pp.programId = pgm.id \n" +
                       writePredicates(filterCriteria,null) +
                "      AND pp.fullSupply = TRUE\n" +
                "      AND p.active = TRUE and fap.isActive = TRUE \n" +
                "      AND pp.active = TRUE\n" +
                "      ORDER BY pc.displayOrder, pc.name, pp.displayOrder NULLS LAST, p.code";

    }


    private static String writePredicates(Map params, Long userId) {

       String predicate = "  WHERE ";
       String facilityTypeId = params.get("facilityType") == null ? null : ((String[]) params.get("facilityType"))[0];
       String program = params.get("program") == null ? null : ((String[]) params.get("program"))[0];
       String productCategory = params.get("productCategory") == null ? null : ((String[]) params.get("productCategory"))[0];

       predicate += " fap.facilityTypeId = " + facilityTypeId;

       predicate += " and pp.programId = " + program;

       if(productCategory != null && !productCategory.isEmpty())
         predicate += " and pp.productCategoryId = " + productCategory;

       return predicate;
    }


}
