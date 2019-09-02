package org.openlmis.analytics.Builder;

import java.util.Map;

public class GeoFacilityStockStatusQueryBuilder {

    public static String getQuery(Map params) {

        Long userId = (Long)params.get("userId");
        Long period = (Long)params.get("period");
        Long program = (Long)params.get("program");
        Long product = (Long)params.get("product");
        Long year = (Long)params.get("year");


        return " \n" +
                "                                        select facilityid,facility,latitude,longitude, sum(SO) so, sum(SP) SP, sum(os) os, sum(us) us,sum(uk) uk  FROM  (\n" +
                "\n" +
                "                                          SELECT latitude, longitude, facility_id facilityid,facility,\n" +
                "                                             case when status = 'SO' THEN 1 ELSE 0 END AS SO,\n" +
                "                                                 case when status = 'SP' THEN 1 ELSE 0 END AS SP,\n" +
                "                                                 case when status = 'OS' THEN 1 ELSE 0 END AS OS,\n" +
                "             \n" +
                "                                               case when status = 'US' THEN 1 ELSE 0 END AS US,\n" +
                "                                            case when status = 'UK' THEN 1 ELSE 0 END AS UK\n" +
                "                           \n" +
                "                                               from mv_stock_imbalance_by_facility_report\n" +
                "                                 \n" +
                "                                                  WHERE  programId = '"+program+"'::INT  AND YEAR = '"+year+"'::int \n" +
                "                                                and productId = '"+product+"'::INT  and periodId = '"+period+"'::INT\n " +
                "   )L\n" +
                "                                           \n" +
                "                                                 GROUP BY  facilityid,facility,l.latitude,longitude\n" +
                "                                                 ORDER BY facilityid,facility";
    }

}
