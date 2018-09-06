package org.openlmis.report.builder;

public class DashboardVitalStatusQueryBuilder {

    public static String getQuery(){
        String query=" select * from mv_vital_states_query";
        return query;
    }
}
