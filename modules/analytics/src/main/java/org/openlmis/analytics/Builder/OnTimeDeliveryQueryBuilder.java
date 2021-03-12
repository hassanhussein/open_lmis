package org.openlmis.analytics.Builder;

import java.util.Map;

public class OnTimeDeliveryQueryBuilder {

    public static String getQuery(Map params){
        Long period = (Long)params.get("period");
        Long program = (Long)params.get("program");
        Long userId = (Long)params.get("userId");


        return "     SELECT  * FROM (\n" +
                "\n" +
                "                         SELECT  COUNT(*) received FROM facilities f where f.id in (\n" +
                "                         select   facilityID  from pod  r\n" +
                "                          where R.periodid = '"+period+"'::int \n" +
                "                         and r.programId ='"+program+"'::int\n" +
                "                         ) )X,\n" +
                "                     \n" +
                "\t\t     (select  count(*) total from facilities   \n" +
                "\t\t     join programs_supported ps on ps.facilityId = facilities.id  \n" +
                "\t\t     join geographic_zones gz on gz.id = facilities.geographicZoneId  \n" +
                "\t\t     where  ps.programId = '"+program+"'::int and facilities.id in   \n" +
                "\t\t     (select facilityId from requisitions where periodId = '"+period+"'::int and programId = '"+program+"'::int and status not in ('INITIATED', 'SUBMITTED', 'SKIPPED') and emergency = false )  )L\n" +
                "\t\t    ";

    }



}
