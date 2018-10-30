package org.openlmis.report.builder;

public class DashboardVitalStatusQueryBuilder {

    public static String getQuery(){
        String query=" select * from mv_vital_states_query";
        return query;
    }
    public static String getUserInLastThreeMonths(){
        String query = " with months as(SELECT u.id,\n" +
                "            date_part('month'::text, age(now()::timestamp without time zone, u.createddate)) + 12::double precision * date_part('year'::text, age(now()::timestamp without time zone, u.createddate)) AS months\n" +
                "           FROM users u)\n" +
                "           \n" +
                "           \n" +
                "           select u.id userid, u.username ,u.email,u.firstname, u.lastname,u.officephone, u.cellphone,f.name facility,gz.name district from users u \n" +
                "           inner join months m on m.id=u.id\n" +
                "          left outer join facilities f on f.id=u.facilityid\n" +
                "           left outer join geographic_zones gz on f.geographiczoneid=gz.id\n" +
                "           where m.months<=6 order by u.username";
        return  query;
    }
}
