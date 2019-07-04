package org.openlmis.analytics.Builder;

import java.util.Map;

public class DashboardRnRPassedQualityCheckQueryBuilder {


    public static String getQuery(Map params){
        Long userId = (Long)params.get("userId");
        Long period = (Long)params.get("period");
        Long program = (Long)params.get("program");

        return " SELECT * from (\n" +
                "\n" +
                "select COUNT(*) passed_total from requisitions WHERE ID NOT IN (\n" +
                "select  rnrid  from ( \n" +
                "\n" +
                "select rnrid from requisition_status_changes c where c.status = 'INITIATED'  and RNRid IN (SELECT ID FROM requisitions where programId = '"+program+"' AND PERIODID = '"+period+"' ) group by rnrid having count(*) > 1\n" +
                "   \n" +
                ") a\n" +
                "\n" +
                ")\n" +
                "AND programId = '"+program+"' AND periodId = '"+period+"' AND STATUS IN('APPROVED','RELEASED') group BY PERIODid \n" +
                ")total,(select count (*) total from requisitions where programId = '"+program+"' AND PERIODID = '"+period+"' AND STATUS IN('APPROVED','RELEASED') group BY PERIODid\n" +
                ")totalRnR\n" +
                "\n  ";
    }

}
