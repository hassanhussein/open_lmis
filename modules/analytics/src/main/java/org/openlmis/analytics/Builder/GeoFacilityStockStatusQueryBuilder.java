package org.openlmis.analytics.Builder;

import java.util.Map;

public class GeoFacilityStockStatusQueryBuilder {

    public static String getQuery(Map params) {

        Long userId = (Long)params.get("userId");
        Long period = (Long)params.get("period");
        Long program = (Long)params.get("program");
        Long product = (Long)params.get("product");
        Long year = (Long)params.get("year");


        return "   select facilityid,facility,latitude,longitude, sum(SO) so, sum(SP) SP, sum(os) os, sum(us) us,sum(uk) uk,\n" +
                "          sum(soh) soh, sum(amc) amc,sum(MOS) MOS,  sum(currentPrice) currentPrice\n" +
                "          ,productcode, mainphone, product, sum(ordered) ordered, sum(required) required\n" +
                "\n" +
                "         FROM  (\n" +
                "         \n" +
                "       SELECT latitude, longitude, facility_id facilityid,facility,\n" +
                "      case when status = 'SO' THEN 1 ELSE 0 END AS SO,\n" +
                "      case when status = 'SP' THEN 1 ELSE 0 END AS SP,\n" +
                "      case when status = 'OS' THEN 1 ELSE 0 END AS OS,\n" +
                "                                             \n" +
                "      case when status = 'US' THEN 1 ELSE 0 END AS US,\n" +
                "      case when status = 'UK' THEN 1 ELSE 0 END AS UK, (SELECT amc )AMC,  (SELECT stockinhand )soh, \n" +
                "      (SELECT MOS) MOS, (SELECT CURRENTPRICE) currentPrice, (select mainphone) mainphone, (select productcode) productcode, \n" +
                "\n" +
                "      (select product) product, (select required) required, (select ordered) ordered\n" +
                "                                                           \n" +
                "     from mv_stock_imbalance_by_facility_report\n" +
                "                                                                 \n" +
                "     WHERE productId = '"+product+"' AND  programId = '"+program+"'::INT and latitude is not null\n" +
                "                                                                                   AND YEAR = '"+year+"'::int \n" +
                "                                                                                 and periodId = '"+period+"'::INT\n" +

                ")L\n" +
                "                                                                           \n" +
                "                                                                                 GROUP BY  facilityid,facility,l.latitude,longitude,productcode, mainphone, product\n" +
                "                                                                                 ORDER BY facilityid,facility ;    \n";
    }

}
