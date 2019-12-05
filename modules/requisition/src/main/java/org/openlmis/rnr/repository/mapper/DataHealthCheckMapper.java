package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.rnr.domain.Comment;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface DataHealthCheckMapper {


    @Select("SELECT * FROM comments WHERE rnrId = #{rnrId} ORDER BY createdDate")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "author.id", column = "createdBy")
    })
    List<HashMap<String,Object>>  getDataHealthCheckRules(Long programId);


    @Select("select rule as rule, stockoutdaysrule as status, message as message from fn_rnr_line_item_health_check(#{facilityId}," +
            " #{rnrId}, #{lineItemId}, #{programId}, #{productCode}, #{skipped})")
    List<HashMap<String,Object>>  runDataHealthCheck(@Param("facilityId") int facilityId, @Param("rnrId") int rnrId,
                                                     @Param("lineItemId") int lineItemId,
                                                     @Param("productCode") String productCode,
                                                     @Param("programId") int programId,
                                                     @Param("skipped") boolean skipped);
}
