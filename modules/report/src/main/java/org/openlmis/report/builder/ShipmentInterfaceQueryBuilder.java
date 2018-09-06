package org.openlmis.report.builder;

public class ShipmentInterfaceQueryBuilder {
    public static String getQuery(){
        String query="SELECT \n" +
                "       TO_CHAR(createddate :: DATE, 'YYYY-mm') order_date,\n" +
                "  SUM(\n" +
                "  CASE\n" +
                "       WHEN status = 'PACKED' THEN 1::INTEGER \n" +
                "\t   ELSE 0::INTEGER \n" +
                "  END  \n" +
                "      ) AS packed,\n" +
                "  SUM(\t  \n" +
                "  CASE\n" +
                "       WHEN status = 'RELEASED' THEN 1::INTEGER \n" +
                "\t   ELSE 0::INTEGER \n" +
                "  END  \n" +
                "       ) AS released,\n" +
                "  SUM(\t   \n" +
                "  CASE\n" +
                "       WHEN status = 'TRANSFER_FAILED' THEN 1::INTEGER \n" +
                "\t   ELSE 0::INTEGER \n" +
                "  END  \n" +
                "       ) AS failed  \n" +
                "  FROM public.orders \n" +
                " WHERE TO_CHAR(createddate :: DATE, 'YYYY') = '2018'\n" +
                "  GROUP BY \n" +
                "       TO_CHAR(createddate :: DATE, 'YYYY-mm')\n" +
                ";";
        return  query;
    }
}
