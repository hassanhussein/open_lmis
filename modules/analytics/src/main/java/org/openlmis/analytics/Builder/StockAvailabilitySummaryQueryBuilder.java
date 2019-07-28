package org.openlmis.analytics.Builder;

import java.util.Map;

public class StockAvailabilitySummaryQueryBuilder {


    public static String getSummary (Map params){

        Long userId = (Long)params.get("userId");
        Long period = (Long)params.get("period");
        Long program = (Long)params.get("program");
        Long year = (Long)params.get("year");


        return  "  select Schedule,PERIODid,\n" +
                "                ROUnd(( sp::numeric / all_incidences ) * 100) percentage_of_sp,\n" +
                "                ROUnd(( uk::numeric / all_incidences ) * 100) percentage_of_uk,\n" +
                "                ROUnd(( us::numeric / all_incidences ) * 100) percentage_of_us,\n" +
                "                ROUnd(( os::numeric / all_incidences ) * 100) percentage_of_os,\n" +
                "\n" +
                "\n" +
                "                ROUnd(( available_incidencies::numeric / all_incidences ) * 100) percentage_of_total,\n" +
                "               100 - ROUnd(( available_incidencies::numeric / all_incidences ) * 100) percentage_of_stock_out\n" +
                "\n" +
                "                 from (\n" +
                "               SELECT Schedule, SCHEDULEID,PERIODid, PERIOD, SUM(SP) sp, sum(unknown) uk, sum(understock)us,SUM(overstock) os,\n" +
                "               SUM(overstock)+SUM(SP)+sum(unknown)+ sum(understock)+SUM(SO) all_incidences, \n" +
                "                SUM(SO) SO, SUM(overstock)+SUM(SP)+sum(unknown)+ sum(understock) available_incidencies\n" +
                "                \n" +
                "                FROM (\n" +
                "                 select Schedule, SCHEDULEID, PERIODid, PROCESSING_PERIOD_NAME period,\n" +
                "                \n" +
                "                 CASE WHEN Status ='SP' then 1 else 0 END as SP,\n" +
                "                CASE WHEN Status ='UK' then  1 ELSE 0 END as unknown,\n" +
                "                CASE WHEN Status ='US' then  1 else 0  end as understock,\n" +
                "                \n" +
                "                CASE WHEN Status ='OS' then 1 ELSE 0 end as overstock,\n" +
                "                CASE WHEN Status ='SO' then 1 ELSE 0 end as SO \n" +
                "                \n" +
                "                from mv_stock_imbalance_by_facility_report r where  programID = '"+program+"'::int and year = '"+year+"'::int and EMERGENCY = FALSE and periodId = '"+period+"'::int\n" +
                "                 AND STATUS NOT IN ('')\n" +
                "                 \n" +
                "                 )l\n" +
                "                GROUP BY PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "                order by periodid,PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "                ) X";

    }

    public static String getStockAvailabilityTrends (Map params){

                    Long program = (Long)params.get("program");
                    Long year = (Long)params.get("year");

        return  " \n" +
                "                SELECT Schedule, SCHEDULEID,PERIODid, PERIOD, SUM(SP) sp, sum(unknown) uk, sum(understock)us,SUM(overstock) os,\n" +
                "                SUM(SO) SO, SUM(overstock)+SUM(SP)+sum(unknown)+ sum(understock)+SUM(SO) TOTAL\n" +
                "                FROM (\n" +
                "                 select Schedule, SCHEDULEID, PERIODid, PROCESSING_PERIOD_NAME period,\n" +
                "                \n" +
                "                 CASE WHEN Status ='SP' then 1 else 0 END as SP,\n" +
                "                CASE WHEN Status ='UK' then  1 ELSE 0 END as unknown,\n" +
                "                CASE WHEN Status ='US' then  1 else 0  end as understock,\n" +
                "                \n" +
                "                CASE WHEN Status ='OS' then 1 ELSE 0 end as overstock,\n" +
                "                CASE WHEN Status ='SO' then 1 ELSE 0 end as SO \n" +
                "                \n" +
                "                from mv_stock_imbalance_by_facility_report r where  programID ='"+program+"'::int  and year = '"+year+"'::int and EMERGENCY = FALSE\n" +
                "                 AND STATUS NOT IN ('')\n" +
                "                 \n" +
                "                 )l\n" +
                "                GROUP BY PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "                order by periodid,PERIODid,PERIOD,Schedule, SCHEDULEID";

    }

}