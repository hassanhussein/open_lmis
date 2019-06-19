package org.openlmis.analytics.Builder;

import java.util.Map;

public class StockStatusQueryBuilder {

    public static String getStockStatusSummary(Map params){

         Long userId = (Long)params.get("userId");
         Long product = (Long)params.get("product");
         Long program = (Long)params.get("program");
         Long year = (Long)params.get("year");


        return
                " SELECT periodId, processing_period_name::text as period,case when status::text = null then 'UN' else status::text end as status , COUNT(*) totalFacilities \n" +
                " from mv_stock_imbalance_by_facility_report WHERE  PROGRAMID = '"+program+"'::INT and productId = '"+product+"'::INT " +
                " AND emergency = false and stockinhand IS NOT NULL AND skipped = false AND year = '"+year+"'::INT\n" +
                " AND facility_Id in (select facility_Id from vw_user_facilities where user_id = '"+userId+"'::INT and program_id = '"+program+"'::INT)\n" +
                " group by processing_period_name ,status ,periodId " +
                " order by periodId, processing_period_name  ";

    }


}
