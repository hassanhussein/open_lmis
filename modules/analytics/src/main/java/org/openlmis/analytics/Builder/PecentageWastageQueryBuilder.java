package org.openlmis.analytics.Builder;

import java.util.Map;

public class PecentageWastageQueryBuilder {

    public static String getQuery(Map params){

        Long period = (Long)params.get("period");
        Long program = (Long)params.get("program");
        Long userId = (Long)params.get("userId");


        return
                "     SELECT adjustmentName ,SUM(adjustment_qty) adjustment_qty,SUM(TOTAL) TOTAL FROM mv_dashboard_wastage_line_items \n" +
                "WHERE PROGRAMID = '"+program+"' AND PERIODID = '"+period+"'\n" +
                "     GROUP BY adjustmentName\n" ;



    }
}
