package org.openlmis.ivdform.repository.mapper.reports;

import org.apache.ibatis.annotations.*;
import org.openlmis.ivdform.domain.reports.HEIDLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HEIDLineItemMapper {

    @Insert(" INSERT INTO heid_line_items(\n" +
            "             reportId, maleValue, femaleValue, createdBy, createddate, \n" +
            "            modifiedBy, modifieddate,skipped)\n" +
            "    VALUES (#{reportId}, #{maleValue}, #{femaleValue}, #{createdBy}, NOW(), #{modifiedBy},NOW(),false);\n")
    @Options(useGeneratedKeys = true)
    void insert(HEIDLineItem lineItem);

    @Update("UPDATE heid_line_items\n" +
            "   SET malevalue=#{maleValue}, femalevalue=#{femaleValue}, \n" +
            "       modifiedby=#{modifiedBy}, modifieddate=NOW()\n" +
            " WHERE id = #{id}")
    void update(HEIDLineItem item);

    @Select("select li.* " +
            " from " +
            " heid_line_items li "+
            " WHERE li.reportId = #{reportId} " +
            " order by id")
    List<HEIDLineItem> getLineItems(@Param("reportId") Long reportId);

}
