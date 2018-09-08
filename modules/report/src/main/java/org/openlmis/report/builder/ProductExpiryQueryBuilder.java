package org.openlmis.report.builder;

public class ProductExpiryQueryBuilder {
    public  static String getQuery(){
        String query = "select * from  mv_expired_tracer_products" +
                " ep " +
                " where " +
                " ep.periodid=154 and programid=1 ";
        return query;
    }
}
