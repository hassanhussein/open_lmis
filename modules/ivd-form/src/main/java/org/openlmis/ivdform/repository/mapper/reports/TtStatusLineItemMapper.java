package org.openlmis.ivdform.repository.mapper.reports;

import org.apache.ibatis.annotations.*;
import org.openlmis.ivdform.domain.reports.TtStatusLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TtStatusLineItemMapper {

    @Insert(" INSERT INTO tt_Status_Line_Items(\n" +
            "             reportId, categoryId,category,maleValue, femaleValue, createdBy, createddate, \n" +
            "            modifiedBy, modifieddate,skipped)\n" +
            "    VALUES (#{reportId},#{categoryId},#{category}, #{maleValue}, #{femaleValue}, #{createdBy}, NOW(), #{modifiedBy},NOW(),false);\n")
    @Options(useGeneratedKeys = true)
    void insert(TtStatusLineItem lineItem);

    @Update("UPDATE tt_Status_Line_Items\n" +
            "   SET malevalue=#{maleValue}, femalevalue=#{femaleValue}, categoryId = #{categoryId}," +
            "  category= #{category},\n" +
            "       modifiedby=#{modifiedBy}, modifieddate=NOW()\n" +
            " WHERE id = #{id}")
    void update(TtStatusLineItem item);

    @Select("select li.* " +
            " from " +
            " tt_Status_Line_Items li " +
            " JOIN tt_status_categories c " +
            " ON c.id = li.categoryId " +
            " WHERE li.reportId = #{reportId} " +
            " order by id")
    List<TtStatusLineItem> getLineItems(@Param("reportId") Long reportId);

}
