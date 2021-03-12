package org.openlmis.analytics.Builder;

import java.util.Map;

public class EmergencyRequisitionStatusTrendQueryBuilder {

    public static String getQuery (Map params) {

        return
                "SELECT  SUM(CASE WHEN emergency = true THEN 1 ELSE 0 END) as \"Emergency Requisitions\"\n" +
                "    , SUM(CASE WHEN emergency != true THEN 1 ELSE 0 END) as \"Regular Requisitions\"\n" +
                "    , to_char(createdDate, 'Mon') || '-' || extract(year from createdDate) as \"Month\" \n" +
                "    ,to_char(createdDate, 'yyyy-mm') ym\n" +
                "    from requisitions \n" +
                "    WHERE status != 'INITIATED' and createdDate >=\n" +
                "      date_trunc('month', CURRENT_DATE) - INTERVAL '1 year'\n" +
                "group by  to_char(createdDate, 'Mon') || '-' || extract(year from createdDate), to_char(createdDate, 'yyyy-mm')\n" +
                "    order by ym asc ";


    }

}
