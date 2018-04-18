package org.openlmis.ivdform.repository.mapper.reports;

import org.apache.ibatis.annotations.*;
import org.openlmis.ivdform.domain.reports.PmtctLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PmctcLineItemMapper {

    @Insert(" INSERT INTO pmtct_line_Items(\n" +
            "             reportId,transferredToCtc, categoryId,category,maleValue, femaleValue, createdBy, createddate, \n" +
            "            modifiedBy, modifieddate,skipped)\n" +
            "    VALUES (#{reportId},#{transferredToCtc},#{categoryId},#{category}, #{maleValue}, #{femaleValue}, #{createdBy}, NOW(), #{modifiedBy},NOW(),false);\n")
    @Options(useGeneratedKeys = true)
    void insert(PmtctLineItem lineItem);

    @Update("UPDATE pmtct_line_Items\n" +
            "   SET malevalue=#{maleValue}, femalevalue=#{femaleValue}, categoryId = #{categoryId},transferredToCtc = #{transferredToCtc}," +
            "  category= #{category},\n" +
            "       modifiedby=#{modifiedBy}, modifieddate=NOW()\n" +
            " WHERE id = #{id}")
    void update(PmtctLineItem item);

    @Select("select li.* " +
            " from " +
            " pmtct_line_Items li " +
            " JOIN pmtct_categories c " +
            " ON c.id = li.categoryId " +
            " WHERE li.reportId = #{reportId} " +
            " order by id")
    List<PmtctLineItem> getLineItems(@Param("reportId") Long reportId);
}
