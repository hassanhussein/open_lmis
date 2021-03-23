package org.openlmis.analytics.Builder;

import java.util.Map;

public class StockStatusQueryBuilder {

    public static String getStockStatusSummary(Map params){

         Long userId = (Long)params.get("userId");
         Long product = (Long)params.get("product");
         Long program = (Long)params.get("program");
         Long year = (Long)params.get("year");
         Long schedule = (Long)params.get("schedule");

         return " \n" +
                 "        SELECT periodId,product, " +
                 " case when (   ( select id from programs where id = '"+program+"'::int and enablemonthlyreporting = true limit 1) <> 0 ) THEN \n" +
                 "                ( select to_char(to_timestamp (date_part('month', ENDDATE::TIMESTAMP)::text, 'MM'), 'Month')\n" +
                 "                 from processing_periods where id = periodId)\n" +
                 "                ELSE periodName end as periodName, " +
                 " SUM (SO) as so, \n" +
                 "                              SUM(OS) AS OS , SUM( SP) SP , SUM(US) US ,\n" +
                 "                              SUM(UK) UK, \n" +
                 "                  SUM (SO)+ SUM(OS) + SUM( SP) + SUM(US) + SUM(UK) total  \n" +
                 "                 FROM (\n" +
                 "                                SELECT periodid,product,processing_period_name periodName,\n" +
                 "                                case when status = 'SO' THEN 1 ELSE 0 END AS SO,\n" +
                 "                                case when status = 'SP' THEN 1 ELSE 0 END AS SP,\n" +
                 "                                case when status = 'OS' THEN 1 ELSE 0 END AS OS,\n" +
                 "                \n" +
                 "                                case when status = 'US' THEN 1 ELSE 0 END AS US,\n" +
                 "                                case when status = 'UK' THEN 1 ELSE 0 END AS UK\n" +
                 "                                \n" +
                 "                                from mv_stock_imbalance_by_facility_report\n" +
                 "                \n" +
                 "                                 WHERE  programId = '"+program+"'::INT  AND YEAR = '"+year+"'::int\n" +
                 "                               and productId = '"+product+"'::int \n and scheduleId = '"+schedule+"'" +
                 "                                \n" +
                 "                                )L\n" +
                 "                                GROUP BY  periodid,product,periodName\n" +
                 "                                ORDER BY periodid,periodName,product  ";


      /*  return "               SELECT periodId, periodName, SUM (SO) as so, \n" +
                "              SUM(OS) AS OS , SUM( SP) SP , SUM(US) US ,\n" +
                "              SUM(UK) UK," +
                "  SUM (SO)+ SUM(OS) + SUM( SP) + SUM(US) + SUM(UK) total " +
                " FROM (\n" +
                "                SELECT periodid,periodName,\n" +
                "                case when status = 'SO' THEN 1 ELSE 0 END AS SO,\n" +
                "                case when status = 'SP' THEN 1 ELSE 0 END AS SP,\n" +
                "                case when status = 'OS' THEN 1 ELSE 0 END AS OS,\n" +
                "\n" +
                "                case when status = 'US' THEN 1 ELSE 0 END AS US,\n" +
                "                case when status = 'UK' THEN 1 ELSE 0 END AS UK\n" +
                "                \n" +
                "                from mv_dashboard_tracer_availability_summary\n" +
                "\n" +
                "                 WHERE productId = '"+product+"'::int and programId = '"+program+"'::INT  AND reportedYEAR = '"+year+"'\n" +
                "                \n" +
                "                )L\n" +
                "                GROUP BY  periodid,periodName\n" +
                "                ORDER BY periodid,periodName  ";*/







    }


    public static String getStockSummaryByYearAndProgram(Map params){

        return " SELECT Schedule, SCHEDULEID,PERIODid, PERIOD, SUM(SP) sp, sum(unknown) uk, sum(understock)us,SUM(overstock) os,\n" +
                "SUM(SO) SO, SUM(overstock)+SUM(SP)+sum(unknown)+ sum(understock)+SUM(SO) TOTAL\n" +
                "FROM (\n" +
                " select Schedule, SCHEDULEID, PERIODid, PROCESSING_PERIOD_NAME period,\n" +
                "\n" +
                " CASE WHEN Status ='SP' then 1 else 0 END as SP,\n" +
                "CASE WHEN Status ='UK' then  1 ELSE 0 END as unknown,\n" +
                "CASE WHEN Status ='US' then  1 else 0  end as understock,\n" +
                "\n" +
                "CASE WHEN Status ='OS' then 1 ELSE 0 end as overstock,\n" +
                "CASE WHEN Status ='SO' then 1 ELSE 0 end as SO \n" +
                "\n" +
                "from mv_stock_imbalance_by_facility_report r where  programID = 1 and year = 2018 and EMERGENCY = FALSE\n" +
                " AND STATUS NOT IN ('')\n" +
                " \n" +
                " )l\n" +
                "GROUP BY PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "order by periodid,PERIODid,PERIOD,Schedule, SCHEDULEID ";

    }

}
