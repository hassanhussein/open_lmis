package org.openlmis.analytics.Builder;

import java.util.Map;

public class StockAvailabilityByLevelQueryBuilder {

    public static String getAvailableStockByLevel(Map params){

        Long userId = (Long)params.get("userId");
        Long period = (Long)params.get("period");
        Long year = (Long)params.get("year");
        Long program = (Long)params.get("program");

        return "                                select tracerItems,\n" +
                "                               Schedule,PERIODid,\n" +
                "\n" +
                "                               ROUnd(( sp::numeric / all_incidences ) * 100) percentage_of_sp,\n" +
                "                                ROUnd(( uk::numeric / all_incidences ) * 100) percentage_of_uk,\n" +
                "                                ROUnd(( us::numeric / all_incidences ) * 100) percentage_of_us,\n" +
                "                                ROUnd(( os::numeric / all_incidences ) * 100) percentage_of_os,\n" +
                "                \n" +
                "                               ROUnd(( available_incidencies::numeric / all_incidences ) * 100) percentage_of_total,\n" +
                "                               100 - ROUnd(( available_incidencies::numeric / all_incidences ) * 100) percentage_of_stock_out\n" +
                "                \n" +
                "                                 from (\n" +
                "                                 \n" +
                "                              SELECT * FROM ( \n" +
                "                                \n" +
                "                               SELECT 'tracerItems'::TEXT as tracerItems,Schedule, SCHEDULEID,PERIODid, PERIOD, SUM(SP) sp, sum(un) uk, sum(understock)us,SUM(overstock) os,\n" +
                "                               SUM(overstock)+SUM(SP)+sum(un)+ sum(understock)+SUM(SO) all_incidences, \n" +
                "                                SUM(SO) SO, SUM(overstock)+SUM(SP)+sum(un)+ sum(understock) available_incidencies\n" +
                "                                \n" +
                "\n" +
                "\n" +
                "                                FROM (\n" +
                "                                 select Schedule, SCHEDULEID, PERIODid, PROCESSING_PERIOD_NAME period,\n" +
                "                                \n" +
                "                                 CASE WHEN Status ='SP' then 1 else 0 END as SP,\n" +
                "                                CASE WHEN Status ='UK' then  1 ELSE 0 END as un,\n" +
                "                                CASE WHEN Status ='US' then  1 else 0  end as understock,\n" +
                "                                \n" +
                "                                CASE WHEN Status ='OS' then 1 ELSE 0 end as overstock,\n" +
                "                                CASE WHEN Status ='SO' then 1 ELSE 0 end as SO \n" +
                "                                \n" +
                "                                from mv_stock_imbalance_by_facility_report r where  programID = '"+program+"'::int and year = '"+year+"'::int and EMERGENCY = FALSE \n" +
                "                                 and periodid = '"+period+"'::int and tracer = true\n" +
                "                                 AND STATUS NOT IN ('')\n" +
                "                                 \n" +
                "                                 )l\n" +
                "                                GROUP BY PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "                                order by periodid,PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "                                ) Z \n" +
                "\n" +
                "\t\t\t\tUNION \n" +
                "\n" +
                "\n" +
                "\n" +
                "\t\t\t\t  SELECT * FROM ( \n" +
                "                                \n" +
                "                               SELECT 'allItems'::TEXT as tracerItems, Schedule, SCHEDULEID,PERIODid, PERIOD, SUM(SP) sp, sum(un) uk, sum(understock)us,SUM(overstock) os,\n" +
                "                               SUM(overstock)+SUM(SP)+sum(un)+ sum(understock)+SUM(SO) all_incidences, \n" +
                "                                SUM(SO) SO, SUM(overstock)+SUM(SP)+sum(un)+ sum(understock) available_incidencies\n" +
                "                                \n" +
                "\n" +
                "\n" +
                "                                FROM (\n" +
                "                                 select Schedule, SCHEDULEID, PERIODid, PROCESSING_PERIOD_NAME period,\n" +
                "                                \n" +
                "                                 CASE WHEN Status ='SP' then 1 else 0 END as SP,\n" +
                "                                CASE WHEN Status ='UK' then  1 ELSE 0 END as un,\n" +
                "                                CASE WHEN Status ='US' then  1 else 0  end as understock,\n" +
                "                                \n" +
                "                                CASE WHEN Status ='OS' then 1 ELSE 0 end as overstock,\n" +
                "                                CASE WHEN Status ='SO' then 1 ELSE 0 end as SO \n" +
                "                                \n" +
                "                                from mv_stock_imbalance_by_facility_report r where  programID = '"+program+"'::int and year = '"+year+"'::int and EMERGENCY = FALSE \n" +
                "                                 and periodid = '"+period+"'::int\n" +
                "                                 AND STATUS NOT IN ('')\n" +
                "                                 \n" +
                "                                 )l\n" +
                "                                GROUP BY PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "                                order by periodid,PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "                                ) M\n" +
                "                                \n" +
                "\n" +
                "\t\t\t       ) X\n ";


/*

        return  " \n" +
                "               select case when isprimaryhealthcare = true then 'Primary Health Care' ELSE 'HOSPITAL' END AS facilityType,Schedule,PERIODid,\n" +
                "               ROUnd(( sp::numeric / all_incidences ) * 100) percentage_of_sp,\n" +
                "                ROUnd(( uk::numeric / all_incidences ) * 100) percentage_of_uk,\n" +
                "                ROUnd(( us::numeric / all_incidences ) * 100) percentage_of_us,\n" +
                "                ROUnd(( os::numeric / all_incidences ) * 100) percentage_of_os,\n" +
                "\n" +
                "               ROUnd(( available_incidencies::numeric / all_incidences ) * 100) percentage_of_total,\n" +
                "               100 - ROUnd(( available_incidencies::numeric / all_incidences ) * 100) percentage_of_stock_out\n" +
                "\n" +
                "                 from (\n" +
                "               SELECT isprimaryhealthcare,Schedule, SCHEDULEID,PERIODid, PERIOD, SUM(SP) sp, sum(unknown) uk, sum(understock)us,SUM(overstock) os,\n" +
                "               SUM(overstock)+SUM(SP)+sum(unknown)+ sum(understock)+SUM(SO) all_incidences, \n" +
                "                SUM(SO) SO, SUM(overstock)+SUM(SP)+sum(unknown)+ sum(understock) available_incidencies\n" +
                "                \n" +
                "                FROM (\n" +
                "                 select isprimaryhealthcare,Schedule, SCHEDULEID, PERIODid, PROCESSING_PERIOD_NAME period,\n" +
                "                \n" +
                "                 CASE WHEN Status ='SP' then 1 else 0 END as SP,\n" +
                "                CASE WHEN Status ='UK' then  1 ELSE 0 END as unknown,\n" +
                "                CASE WHEN Status ='US' then  1 else 0  end as understock,\n" +
                "                \n" +
                "                CASE WHEN Status ='OS' then 1 ELSE 0 end as overstock,\n" +
                "                CASE WHEN Status ='SO' then 1 ELSE 0 end as SO \n" +
                "                \n" +
                "                from mv_stock_imbalance_by_facility_report r where  programID = '"+program+"'::int and year = '"+year+"'::int and EMERGENCY = FALSE " +
                "                 and periodiD = '"+period+"'::int\n" +
                "                 AND STATUS NOT IN ('')\n" +
                "                 \n" +
                "                 )l\n" +
                "                GROUP BY isprimaryhealthcare,PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "                order by periodid,PERIODid,PERIOD,Schedule, SCHEDULEID\n" +
                "                ) X\n";
*/

    }


}
