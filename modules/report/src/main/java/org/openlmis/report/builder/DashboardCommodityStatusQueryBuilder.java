package org.openlmis.report.builder;

public class DashboardCommodityStatusQueryBuilder {

    public static String getQuery(){
        String query="WITH commodity AS\n" +
                "  (SELECT gz.region_name AS supplyingfacility,\n" +
                "          gz.region_name,\n" +
                "          gz.district_name,\n" +
                "          gz.zone_name,\n" +
                "          f.code AS facilitycode,\n" +
                "          li.productcode,\n" +
                "          f.NAME AS facility,\n" +
                "          li.product AS product,\n" +
                "          ft.NAME facilitytypename,\n" +
                "          gz.district_name AS LOCATION,\n" +
                "          pp.NAME AS processing_period_name,\n" +
                "          li.stockinhand,\n" +
                "          li.stockoutdays stockoutdays,\n" +
                "          To_char(pp.startdate, 'Mon') asmonth,\n" +
                "          Extract(YEAR\n" +
                "                  FROM pp.startdate) AS YEAR,\n" +
                "          pg.code AS program,\n" +
                "          CASE -- stocked out when stockinhand\n" +
                "\n" +
                "              WHEN li.stockinhand = 0 THEN 'SO'::text\n" +
                "              ELSE CASE\n" +
                "                       WHEN li.amc > 0\n" +
                "                            AND li.stockinhand > 0 THEN CASE\n" +
                "                                                            WHEN round((li.stockinhand:: decimal / li.amc)::numeric, 2) < fap.minmonthsofstock THEN 'US'::text\n" +
                "                                                            WHEN round((li.stockinhand:: decimal / li.amc)::numeric, 2) >= fap.minmonthsofstock::numeric\n" +
                "                                                                 AND round((li.stockinhand::decimal / li.amc)::numeric, 2) <= fap.maxmonthsofstock::numeric THEN 'SP'::text\n" +
                "                                                            WHEN round((li.stockinhand:: decimal / li.amc)::numeric, 2) > fap.maxmonthsofstock THEN 'OS'::text\n" +
                "                                                        END\n" +
                "                       ELSE 'UK'::text\n" +
                "                   END\n" +
                "          END AS status,\n" +
                "          CASE\n" +
                "              WHEN COALESCE(li.amc, 0) = 0 THEN 0::numeric\n" +
                "              ELSE trunc(round((li.stockinhand::decimal / li.amc)::numeric, 2), 1)::numeric\n" +
                "          END AS mos,\n" +
                "          li.amc,\n" +
                "          COALESCE(CASE\n" +
                "                       WHEN (COALESCE(li.amc, 0) * ft.nominalmaxmonth - li.stockinhand) < 0 THEN 0\n" +
                "                       ELSE COALESCE(li.amc, 0) * ft.nominalmaxmonth - li.stockinhand\n" +
                "                   END, 0) AS required,\n" +
                "          li.quantityapproved AS ordered\n" +
                "   FROM processing_periods pp\n" +
                "   JOIN requisitions r ON pp. id = r.periodid\n" +
                "   JOIN requisition_line_items li ON li.rnrid = r. id\n" +
                "   JOIN facilities f ON f.id = r.facilityid\n" +
                "   JOIN facility_types ft ON ft.id = f.typeid\n" +
                "   JOIN products p ON p.code = li.productcode\n" +
                "   JOIN vw_districts gz ON gz.district_id = f.geographiczoneid\n" +
                "   JOIN programs pg ON pg.id = r.programid\n" +
                "   JOIN program_products pgp ON r.programid = pgp.programid\n" +
                "   AND p.id = pgp.productid\n" +
                "   JOIN facility_approved_products fap ON ft.id = fap.facilitytypeid\n" +
                "   AND fap.programproductid=pgp.id\n" +
                "   WHERE li.skipped = FALSE\n" +
                "     AND (li.beginningbalance > 0\n" +
                "          OR li.quantityreceived > 0\n" +
                "          OR li.quantitydispensed > 0\n" +
                "          OR abs(li.totallossesandadjustments) > 0\n" +
                "          OR li.amc > 0)\n" +
                "     AND r.periodid=114\n" +
                "     AND r.programid=3 ),\n" +
                "     over_stocked as\n" +
                "  (SELECT a.region_name AS regionname, row_number() OVER (PARTITION BY a.region_name\n" +
                "                                                          ORDER BY count(productcode) DESC) AS rownu, product, productcode, count(productcode)\n" +
                "   FROM commodity a\n" +
                "   WHERE status='OS'\n" +
                "   GROUP BY a.region_name, product, productcode\n" +
                "   ORDER BY a.region_name, count(productcode) DESC ),\n" +
                "                  under_stocked as\n" +
                "  ( SELECT a.region_name AS regionname, row_number() OVER (PARTITION BY a.region_name\n" +
                "                                                           ORDER BY count(productcode) DESC) AS rownu, product, productcode, count(productcode)\n" +
                "   FROM commodity a\n" +
                "   WHERE status='US'\n" +
                "   GROUP BY a.region_name, product, productcode\n" +
                "   ORDER BY a.region_name, count(productcode) DESC ),\n" +
                "                                compiled as(\n" +
                "                                              (SELECT us.regionname, us.product, us.productcode, us.count, 'US' AS status, rownu\n" +
                "                                               FROM under_stocked us\n" +
                "                                               WHERE rownu <=5\n" +
                "                                               ORDER BY us.regionname, us.rownu, us.productcode)\n" +
                "                                            UNION\n" +
                "                                              ( SELECT os.regionname, os.product, os.productcode, os.count, 'OS' AS status, rownu\n" +
                "                                               FROM over_stocked os\n" +
                "                                               WHERE os.rownu <=5\n" +
                "                                               ORDER BY os.regionname, os.rownu, os.productcode ) )\n" +
                "SELECT c.regionname,\n" +
                "       c.product,\n" +
                "       c.productcode,\n" +
                "       c.count,\n" +
                "       c.status,\n" +
                "       c.rownu\n" +
                "FROM compiled c\n" +
                "ORDER BY c.regionname,\n" +
                "         c.status,\n" +
                "         rownu,\n" +
                "         c.productcode ";
        return query;
    }
    public static String getFacilityQuery(){
        String query="with commodity as ( \n" +
                "                SELECT gz.region_name AS supplyingfacility, \n" +
                "                       gz.region_name, \n" +
                "                       gz.district_name, \n" +
                "                       gz.zone_name, \n" +
                "                       f.code AS facilitycode, \n" +
                "                       li.productcode, \n" +
                "                       f.NAME           AS facility, \n" +
                "                       li.product       AS product, \n" +
                "                       ft.NAME             facilitytypename, \n" +
                "                       gz.district_name AS location, \n" +
                "                       pp.NAME          AS processing_period_name, \n" +
                "                       li.stockinhand, \n" +
                "                       li.stockoutdays                    stockoutdays, \n" +
                "                       To_char(pp.startdate, 'Mon')       asmonth, \n" +
                "                       Extract(year FROM pp.startdate) AS year, \n" +
                "                       pg.code                         AS program, \n" +
                "                       CASE -- stocked out when stockinhand \n" +
                "                              WHEN li.stockinhand = 0 THEN 'SO'::text \n" +
                "                              ELSE \n" +
                "                                     CASE \n" +
                "                                            WHEN li.amc > 0 \n" +
                "                                            AND    li.stockinhand > 0 THEN \n" +
                "                                                   CASE \n" +
                "                                                          WHEN round((li.stockinhand::  decimal / li.amc)::numeric, 2) < fap.minmonthsofstock THEN 'US'::text\n" +
                "                                                          WHEN round((li.stockinhand::  decimal / li.amc)::numeric, 2) >= fap.minmonthsofstock::numeric\n" +
                "                                                          AND    round((li.stockinhand::decimal / li.amc)::numeric, 2) <= fap.maxmonthsofstock::numeric THEN 'SP'::text\n" +
                "                                                          WHEN round((li.stockinhand::  decimal / li.amc)::numeric, 2) > fap.maxmonthsofstock THEN 'OS'::text\n" +
                "                                                   END \n" +
                "                                            ELSE 'UK'::text \n" +
                "                                     END \n" +
                "                       END AS status, \n" +
                "                       CASE \n" +
                "                              WHEN COALESCE(li.amc, 0) = 0 THEN 0::numeric \n" +
                "                              ELSE trunc(round((li.stockinhand::decimal / li.amc)::numeric, 2), 1)::numeric\n" +
                "                       END AS mos, \n" +
                "                       li.amc, \n" +
                "                       COALESCE( \n" +
                "                       CASE \n" +
                "                              WHEN ( \n" +
                "                                            COALESCE(li.amc, 0) * ft.nominalmaxmonth - li.stockinhand) < 0 THEN 0\n" +
                "                              ELSE COALESCE(li.amc, 0) * ft.nominalmaxmonth - li.stockinhand \n" +
                "                       END, 0)             AS required, \n" +
                "                       li.quantityapproved AS ordered \n" +
                "                FROM   processing_periods pp \n" +
                "                JOIN   requisitions r     ON     pp. id = r.periodid \n" +
                "                JOIN   requisition_line_items li       ON     li.rnrid = r. id \n" +
                "                JOIN   facilities f      ON     f.id = r.facilityid \n" +
                "                JOIN   facility_types ft  ON     ft.id = f.typeid \n" +
                "                JOIN   products p         ON     p.code = li.productcode \n" +
                "                JOIN   vw_districts gz                 ON     gz.district_id = f.geographiczoneid \n" +
                "                JOIN   programs pg                 ON     pg.id = r.programid \n" +
                "                JOIN   program_products pgp ON     r.programid = pgp.programid   AND    p.id = pgp.productid   \n" +
                "\t\t\t\tJOIN   facility_approved_products fap ON     ft.id = fap.facilitytypeid AND    fap.programproductid=pgp.id \n" +
                "                WHERE  li.skipped = false \n" +
                "                AND    ( \n" +
                "                              li.beginningbalance > 0 \n" +
                "                       OR     li.quantityreceived > 0 \n" +
                "                       OR     li.quantitydispensed > 0 \n" +
                "                       OR     abs(li.totallossesandadjustments) > 0 \n" +
                "                       OR     li.amc > 0) \n" +
                "\t\t\t\t\t   and r.periodid=114\n" +
                "\t\t\t\t\t   and r.programid=3\n" +
                "\t\t\t\t\t   \n" +
                "\t\t\t\t\t  ),\n" +
                "over_stocked as(\t\t\t  \n" +
                "\n" +
                "\tSELECT  \n" +
                "        a.facility   AS facility, \n" +
                "        row_number() over (partition by a.facility order by  mos desc) as rownu,\n" +
                "         product, \n" +
                "         productcode,\n" +
                "\tmos,\n" +
                "\tstockinhand\n" +
                "FROM     commodity a \n" +
                "WHERE    status='OS'\n" +
                "\t\n" +
                "\t\torder by a.facility ,mos desc\n" +
                "\t),\n" +
                "\tunder_stocked as(\n" +
                "\t\n" +
                "\tSELECT  \n" +
                "        a.facility   AS facility, \n" +
                "        row_number() over (partition by a.facility order by  mos asc) as rownu,\n" +
                "         product, \n" +
                "         productcode,\n" +
                "\tmos,\n" +
                "\tstockinhand\n" +
                "FROM     commodity a \n" +
                "WHERE    status='US'\n" +
                "\t\n" +
                "\t\torder by a.facility ,mos asc\n" +
                "\t),\n" +
                "\tcompiled as(\n" +
                "\t\t\n" +
                "\t(select \n" +
                "\tus.facility,us.product,us.productcode,us.mos ,stockinhand, 'US' as status,\n" +
                "\t rownu\n" +
                "\t\n" +
                "\tfrom under_stocked us \n" +
                "\twhere rownu <=5\n" +
                "\torder by us.facility, us.rownu,us.productcode)\n" +
                "\tunion \n" +
                "\t(\n" +
                "\t\tselect \n" +
                "\tos.facility,os.product,os.productcode,os.mos ,stockinhand, 'OS' as status,\n" +
                "\t\trownu\n" +
                "\t\n" +
                "\tfrom over_stocked os \n" +
                "\twhere os.rownu <=5\n" +
                "\torder by os.facility, os.rownu,os.productcode\n" +
                "\t)\n" +
                "\t\t)\n" +
                "\t\tselect c.facility,c.product,c.productcode,c.mos , c.status,stockinhand,\n" +
                "\t\tc.rownu from compiled c order by c.facility,c.status,rownu, c.productcode\n" +
                "\t\n" +
                "\n" +
                "        ";
        return query;
    }
}