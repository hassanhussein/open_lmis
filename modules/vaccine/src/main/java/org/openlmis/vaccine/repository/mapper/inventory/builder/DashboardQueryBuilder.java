package org.openlmis.vaccine.repository.mapper.inventory.builder;

import java.util.Map;

public class DashboardQueryBuilder {

    public static String getQuery (Map params) {

        Long product = (Long)params.get("product");
        Long year = (Long)params.get("year");
        Long userId = (Long)params.get("userId");

        return " select PERIODID,PERIOD_NAME,\n" +
                "\n" +
                "sum(total) total, sum(SAP) SAP,\n" +
                " SUM(OS) OS, SUM(US) US, SUM(UNDEFINED)UNDEFINED, SUM(SO)SO \n" +
                "\n" +
                " FROM (\n" +
                "\n" +
                "\n" +
                "\n" +
                "SELECT \n" +
                "\n" +
                "DISTRICT_ID,PERIODID,PERIOD_NAME,\n" +
                "sum(total) total, sum(SAP) SAP,\n" +
                " SUM(OS) OS, SUM(US) US, SUM(UNDEFINED)UNDEFINED, SUM(SO)SO ,\n" +
                "\n" +
                "ROUND(sum(SAP::numeric)/sum(total) * 100, 0) AS percentage_sap,\n" +
                "\n" +
                "ROUND(sum(OS::numeric)/sum(total) * 100, 0) AS percentage_OS,\n" +
                "ROUND(sum(UNDEFINED::numeric)/sum(total) * 100, 0) AS percentage_UNDEFINED,\n" +
                "ROUND(sum(SO::numeric)/sum(total) * 100, 0) AS percentage_SO\n" +
                "\n" +
                " FROM (\n" +
                "\n" +
                "SELECT b.*, \n" +
                "1 AS TOTAL,\n" +
                "CASE\n" +
                "  when coalesce(soh,0) = 0 \n" +
                "   THEN 1 ELSE 0 END AS SO,\n" +
                "  CASE WHEN coalesce(mos,0) IS NULL\n" +
                "    THEN 1 ELSE 0 END AS  undefined,\n" +
                "   CASE WHEN  coalesce(mos,0) between 1 and 1.5 \n" +
                "    THEN 1 else 0 end as sap,\n" +
                "  CASE WHEN coalesce(mos,0) > 1.5\n" +
                "    THEN 1 else 0 end as OS,\n" +
                "   CASE WHEN coalesce(mos,0) < 1\n" +
                "    THEN 1 ELSE 0 END AS US\n" +
                "from (\n" +
                "\n" +
                "select region_name || ' - ' || district_name district_name,district_ID, f.name facility_name,\n" +
                "periodID,facilityid facility_id, soh, consumption, amc, case when COALESCE(amc,0) > 0 then round(COALESCE(soh,0)/amc::numeric,2) else null end mos,\n" +
                "\t\tEXTRACT (YEAR FROM A .startdate) \"year\",\n" +
                "\t\tEXTRACT (MONTH FROM A .startdate) \"month\",\n" +
                "        a.period_name\n" +
                "from (\n" +
                "\n" +
                "with t as \n" +
                "(select pr.startdate::date, pr.name period_name, r.periodid, r.programid, r.facilityid, l.closingbalance soh, l.quantityissued consumption  \n" +
                "from vaccine_reports r \n" +
                "join vaccine_report_logistics_line_items l on r.id = l.reportid\n" +
                "join processing_periods pr on pr.id = r.periodid and pr.numberofmonths = 1\n" +
                "JOIN vw_user_facilities uf ON r.facilityId = uf.facility_id\n" +
                "where uf.user_id = '"+userId+"' and startdate::date  >= ('"+year+"'::text||'-01-01')::date - interval '3 months' and enddate::date <= ('"+year+"'::text||'-12-31')::date\n" +
                "and productid = '"+product+"'::INT\n" +
                "and status <> 'DRAFT'\n" +
                ") \n" +
                "SELECT facilityid, periodid, period_name, startdate,  consumption, soh, \n" +
                "    extract(month from startdate) AS dow,\n" +
                "    CASE WHEN count(consumption) OVER w = 3 THEN avg(consumption) OVER w END AS amc             \n" +
                "FROM t\n" +
                "WHERE extract(month from startdate) between 1 and 12 \n" +
                "WINDOW w AS (ORDER BY facilityid, startdate desc ROWS BETWEEN 0 FOLLOWING AND 2 FOLLOWING)\n" +
                "\n" +
                "\n" +
                " ) a\n" +
                "join facilities f on f.id = a.facilityid\n" +
                "\n" +
                "join vw_districts d on f.geographiczoneid = d.district_id\n" +
                "join processing_periods pr on pr.id = a.periodid\n" +
                ") b \n" +
                "\n" +
                "\n" +
                "order by periodid, district_name, facility_name\n" +
                "\n" +
                ")X \n" +
                "\n" +
                "GROUP BY X.DISTRICT_ID,PERIODID,PERIOD_NAME\n" +
                "ORDER BY PERIODID \n" +
                "\n" +
                ") Z GROUP BY PERIODID,PERIOD_NAME\n" +
                "ORDER BY PERIODID \n" ;


    }


}
