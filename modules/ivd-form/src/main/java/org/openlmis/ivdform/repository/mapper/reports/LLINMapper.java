package org.openlmis.ivdform.repository.mapper.reports;

import org.apache.ibatis.annotations.*;
import org.openlmis.ivdform.domain.reports.LLINLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LLINMapper {

    @Insert(" INSERT INTO mosquito_net_status_line_Items(\n" +
            "             reportId, maleValue, femaleValue, createdBy, createddate, \n" +
            "            modifiedBy, modifieddate,skipped)\n" +
            "    VALUES (#{reportId}, #{maleValue}, #{femaleValue}, #{createdBy}, NOW(), #{modifiedBy},NOW(),false);\n")
    @Options(useGeneratedKeys = true)
    void insert(LLINLineItem lineItem);

    @Update("UPDATE mosquito_net_status_line_Items\n" +
            "   SET malevalue=#{maleValue}, femalevalue=#{femaleValue}, \n" +
            "       modifiedby=#{modifiedBy}, modifieddate=NOW()\n" +
            " WHERE id = #{id}")
    void update(LLINLineItem item);

    @Select("select li.* " +
            " from " +
            " mosquito_net_status_line_Items li "+
            " WHERE li.reportId = #{reportId} " +
            " order by id")
    List<LLINLineItem> getLineItems(@Param("reportId") Long reportId);

}
