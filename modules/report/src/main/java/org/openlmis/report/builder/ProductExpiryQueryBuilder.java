package org.openlmis.report.builder;

public class ProductExpiryQueryBuilder {
    public  static String getQuery(){
        String query = "select * from  mv_expired_tracer_products" +
                " ep " +
                " where " +
                " ep.periodid=154 and programid=1 and " +
                " ep.productcode in ('EM0003','EM1217','HTK0002','EM0166','ARV0028','ARV0041','ARV0031','ARV0063','HTK0001','ARV0035')";
        return query;
    }
}
