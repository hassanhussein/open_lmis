package org.openlmis.report.builder;

public class DashboardSupplyStatusQueryBuilder {
    public  String getQuery(){
        String query= "with tmp as ( \n" +
                "                select  \n" +
                "                productcode, \n" +
                "\t\t\t\tproduct,\n" +
                "                case when item_fill_rate = 0 then 1 else 0 END fill_0, \n" +
                "                case when item_fill_rate > 0  and item_fill_rate < 25 then 1 else 0 END less_25, \n" +
                "                case when item_fill_rate >= 25  and item_fill_rate < 50 then 1 else 0 END less_50, \n" +
                "                case when item_fill_rate >= 50  and item_fill_rate < 75 then 1 else 0 END less_75 \n" +
                "                from mv_item_fill_rate  \n" +
                "                where programid = 2 and periodid = 106 and productcode = 'HTK0002') \n" +
                "                select  \n" +
                "                 productcode, \n" +
                "\t\t\t\t product,\n" +
                "                 sum(fill_0) fill_0, \n" +
                "                 sum(less_25) less_25, \n" +
                "                 sum(less_50) less_50, \n" +
                "                 sum(less_75) less_75 \n" +
                "                from tmp  \n" +
                "                where productcode in ('HTK0002', 'ARV0018', 'ARV0005') \n" +
                "                group by productcode,product";
        return  query;
    }
}
