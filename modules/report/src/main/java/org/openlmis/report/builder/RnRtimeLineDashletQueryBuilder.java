package org.openlmis.report.builder;


public class RnRtimeLineDashletQueryBuilder {

    public static  String getQuery(){
        String query="with req as (\n" +
                "select r.id, r.facilityid,f.name facility from requisitions r " +
                " inner join facilities f on f.id=r.facilityid\n" +
                "),\n" +
                "submit as (\n" +
                "select rs.rnrid,rs.id, rs.createdDate from requisition_status_changes rs \n" +
                "inner join req r on  r.id=rs.rnrid\n" +
                "where rs.status='SUBMITTED'\n" +
                "),\n" +
                "approved as (\n" +
                "select rs.rnrid,rs.id, rs.createdDate from requisition_status_changes rs \n" +
                "inner join req r on  r.id=rs.rnrid\n" +
                "where rs.status='APPROVED'\n" +
                "),\n" +
                "convert as (\n" +
                "select rs.rnrid,rs.id, rs.createdDate from requisition_status_changes rs \n" +
                "inner join req r on  r.id=rs.rnrid\n" +
                "where rs.status='RELEASED'\n" +
                "),\n" +
                "ship as (\n" +
                "select o.id rnrid, o.createdDate from orders o \n" +
                "inner join req r on  r.id=o.id\n" +
                "where o.status='PACKED'\n" +
                ")\n" +
                "select r.id rnrId, r.facilityid," +
                " r.facility,  " +
                "s.createdDate submitted\n" +
                ", a.createdDate approved\n" +
                ", c.createdDate converted\n" +
                ", sh.createdDate shipped," +
                " date_part('day', age(sh.createdDate::date, s.createdDate::date) ) turnRoundDays \n" +
                "from req r\n" +
                "inner join submit s on r.id =s.rnrid\n" +
                "inner join approved a on  r.id=a.rnrid\n" +
                "inner join convert c on  r.id=c.rnrid\n" +
                "inner join ship sh on r.id =sh.rnrid\n" +
                "order by r.facilityid, r.id\n" +
                " ;";
        return query;
    }
}
