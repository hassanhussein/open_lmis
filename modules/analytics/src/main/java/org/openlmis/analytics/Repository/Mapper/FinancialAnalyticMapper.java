package org.openlmis.analytics.Repository.Mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface FinancialAnalyticMapper {

    @Select(" SELECT * FROM crosstab ( \n" +
            "$$SELECT d.district_name::TEXT district, f.name, sum(quantity)  quantity\n" +
            "\n" +
            " FROM requisition_source_of_funds f\n" +
            "\n" +
            "JOIN requisitions r on f.rnrid = r.id \n" +
            "\n" +
            "JOIN facilities FA ON r.facilityId = fa.id\n" +
            "\n" +
            "JOIN vw_districts d ON fa.geographiczoneid = d.district_id\n" +
            "group by district_name,f.name\n" +
            "order by 1,2$$\n" +
            ") AS (district TEXT, \"other\" BIGINT, \"userFees\" BIGINT \n" +
            "\n" +
            ")  ")
   List <HashMap <String, Object>> getDistrictFundUtilization(@Param("userId") Long userId, @Param("zone") Long zone, @Param("program") Long program, @Param("year") Long year , @Param("period") Long period);

}
