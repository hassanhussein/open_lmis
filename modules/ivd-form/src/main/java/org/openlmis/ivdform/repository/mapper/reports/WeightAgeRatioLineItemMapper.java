package org.openlmis.ivdform.repository.mapper.reports;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.openlmis.ivdform.domain.reports.WeightAgeRatioLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightAgeRatioLineItemMapper {


    @Insert("INSERT INTO vaccine_report_weight_ratio_line_items(\n" +
            "             reportid, ageGroupId,ageGroup,categoryId,category, maleValue, \n" +
            "            femaleValue, createdby, createddate, modifiedby, modifieddate, \n" +
            "            skipped)\n" +
            "    VALUES ( #{reportId}, #{ageGroupId},#{ageGroup}, #{categoryId},#{category}, #{maleValue}, #{femaleValue}, #{createdBy}, NOW(), #{modifiedBy}, NOW(),false);\n")
    void insert(WeightAgeRatioLineItem lineItem);

    @Update("UPDATE vaccine_report_weight_ratio_line_items " +
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
    void update(WeightAgeRatioLineItem lineItem);

    @Select("select li.* " +
            " from " +
            " vaccine_report_weight_ratio_line_items li " +
            " join weight_ratio_age_groups ag " +
            " on ag.id = li.ageGroupId " +
            " JOIN weight_ratio_categories c " +
            " ON c.id = li.categoryId " +
            " WHERE li.reportId = #{reportId} " +
            " order by id")
    List<WeightAgeRatioLineItem> getLineItems(@Param("reportId") Long reportId);
}
