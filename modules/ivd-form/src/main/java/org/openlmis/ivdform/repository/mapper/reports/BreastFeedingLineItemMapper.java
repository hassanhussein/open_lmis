package org.openlmis.ivdform.repository.mapper.reports;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.openlmis.ivdform.domain.reports.BreastFeedingLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BreastFeedingLineItemMapper {
    @Insert("INSERT INTO breast_feeding_line_items(\n" +
            "             reportid,categoryId,category, ageGroupId,ageGroup, maleValue, \n" +
            "            femaleValue, createdby, createddate, modifiedby, modifieddate, \n" +
            "            skipped)\n" +
            "    VALUES ( #{reportId},#{categoryId},#{category}, #{ageGroupId},#{ageGroup}, #{maleValue}, #{femaleValue}, #{createdBy}, NOW(), #{modifiedBy}, NOW(),false);\n")
    void insert(BreastFeedingLineItem lineItem);

    @Update("UPDATE breast_feeding_line_items " +
            " SET" +
            " maleValue = #{maleValue} " +
            " , skipped = #{skipped}" +
            " , femaleValue = #{femaleValue} " +
            " , ageGroup = #{ageGroup}"+
            ", ageGroupId=#{ageGroupId}" +
            ", categoryId = #{categoryId}"+
            ",category = #{category}"+
            " , modifiedBy = #{modifiedBy} " +
            " , modifiedDate = NOW()" +
            " WHERE id = #{id}")
    void update(BreastFeedingLineItem lineItem);

    @Select("select li.* " +
            " from " +
            " breast_feeding_line_items li " +
            " join breast_feeding_age_groups ag " +
            " on ag.id = li.ageGroupId " +
            " JOIN breast_feeding_categories c " +
            " ON c.id = li.categoryId " +
            " WHERE li.reportId = #{reportId} " +
            " order by id")
    List<BreastFeedingLineItem> getLineItems(@Param("reportId") Long reportId);


}
