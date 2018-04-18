package org.openlmis.ivdform.repository.mapper.reports;

import org.apache.ibatis.annotations.*;
import org.openlmis.ivdform.domain.reports.ChildVisitLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildVisitLineItemMapper {

    @Insert("INSERT INTO vaccine_report_child_visit_line_items(\n" +
            "             reportid, childVisitAgeGroupId,ageGroup, maleValue, \n" +
            "            femaleValue, createdby, createddate, modifiedby, modifieddate, \n" +
            "            skipped)\n" +
            "    VALUES ( #{reportId}, #{childVisitAgeGroupId},#{ageGroup}, #{maleValue}, #{femaleValue}, #{createdBy}, NOW(), #{modifiedBy}, NOW(),false);\n")
   @Options(useGeneratedKeys = true)
    void insert(ChildVisitLineItem lineItem);

    @Update("UPDATE vaccine_report_child_visit_line_items " +
            " SET" +
            " maleValue = #{maleValue} " +
            " , skipped = #{skipped}" +
            " , femaleValue = #{femaleValue} " +
            " , ageGroup = #{ageGroup}"+
            " , modifiedBy = #{modifiedBy} " +
            " , modifiedDate = NOW()" +
            " WHERE id = #{id}")
    void update(ChildVisitLineItem lineItem);

    @Select("select li.* " +
            " from " +
            " vaccine_report_child_visit_line_items li " +
            " join vaccine_child_visit_age_groups ag " +
            " on ag.id = li.childVisitAgeGroupId " +
            " WHERE li.reportId = #{reportId} " +
            " order by id")
    List<ChildVisitLineItem> getLineItems(@Param("reportId") Long reportId);

}
