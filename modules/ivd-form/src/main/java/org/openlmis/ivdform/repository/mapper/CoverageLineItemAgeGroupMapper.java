package org.openlmis.ivdform.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Product;
import org.openlmis.ivdform.domain.CoverageLineItemAgeGroup;
import org.openlmis.ivdform.domain.VaccineProductDose;
import org.openlmis.ivdform.domain.reports.VaccineCoverageAgeGroupLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CoverageLineItemAgeGroupMapper {

    @Select("select * from vaccine_report_coverage_line_items_age_groups")
    List<CoverageLineItemAgeGroup> getAll();

    @Select("Select * from vaccine_report_coverage_line_items_age_groups where id = #{id}")
    CoverageLineItemAgeGroup getBy(@Param("id") Long id);


    @Insert("INSERT into vaccine_report_coverage_age_group_line_items " +
            " (reportId, productId, doseId, displayName, displayOrder , trackMale, trackFemale, regularMale, regularFemale, outreachMale, outreachFemale, campaignMale, campaignFemale, createdBy, createdDate, modifiedBy, modifiedDate,ageGroupId,ageGroupName) " +
            " values " +
            " (#{reportId}, #{productId}, #{doseId}, #{displayName}, #{displayOrder}, #{trackMale}, #{trackFemale}, #{regularMale}, #{regularFemale}, #{outreachMale}, #{outreachFemale}, #{campaignMale}, #{campaignFemale}, #{createdBy}, NOW(), #{modifiedBy}, NOW(), #{ageGroupId},#{ageGroupName})")
    @Options(useGeneratedKeys = true)
    Integer insert(VaccineCoverageAgeGroupLineItem item);

    @Update("UPDATE vaccine_report_coverage_age_group_line_items " +
            " SET " +
            " reportId = #{reportId} " +
            " , skipped = #{skipped}" +
            " , productId = #{productId} " +
            " , doseId  = #{doseId} " +
            " , displayName = #{displayName}  " +
            " , displayOrder = #{displayOrder}  " +
            " , trackMale = #{trackMale}  " +
            " , trackFemale = #{trackFemale}  " +
            " , regularMale = #{regularMale} " +
            " , regularFemale = #{regularFemale} " +
            " , outreachMale = #{outreachMale} " +
            " , outreachFemale = #{outreachFemale} " +
            " , campaignMale = #{campaignMale} " +
            " , campaignFemale = #{campaignFemale}" +
            " ,ageGroupId = #{ageGroupId} " +
            " ,ageGroupName = #{ageGroupName}   " +
            " , modifiedBy = #{modifiedBy} " +
            " , modifiedDate = NOW()" +
            " WHERE id = #{id} ")
    void update(VaccineCoverageAgeGroupLineItem item);

    @SuppressWarnings("unused")
    @Select("SELECT id, code, primaryName FROM products where id = #{id}")
    Product getProductDetails(Long id);

    @Select("SELECT * from vaccine_report_coverage_age_group_line_items WHERE id = #{id}")
    VaccineCoverageAgeGroupLineItem getById(@Param("id") Long id);

    @Select("SELECT * from vaccine_report_coverage_age_group_line_items " +
            "WHERE reportId = #{reportId} " +
            "   and productId = #{productId} " +
            "   and doseId = #{doseId} " +
            "order by id")
    @Results(value = {
            @Result(property = "productId", column = "productId"),
            @Result(property = "product", javaType = Product.class, column = "productId",
                    many = @Many(select = "org.openlmis.ivdform.repository.mapper.reports.CoverageMapper.getProductDetails")),
    })
    VaccineCoverageAgeGroupLineItem getCoverageByReportProductDosage(@Param("reportId") Long reportId, @Param("productId") Long productId, @Param("doseId") Long doseId);

    @Select("select d.* from vaccine_reports r " +
            " join vaccine_report_coverage_age_group_line_items cli on cli.reportId = r.id " +
            " join vaccine_product_doses d on d.doseId = cli.doseId and cli.productId = d.productId and d.programId = r.programId" +
            " where cli.id = #{id} " +
            " order by cli.id")
    VaccineProductDose getVaccineDoseDetail(@Param("id") Long id);

    @Select("SELECT " +
            "li.*, " +
            "(select sum(regularMale + regularFemale) from vaccine_report_coverage_age_group_line_items ili join vaccine_reports ir on ir.id = ili.reportId join processing_periods ipps on ipps.id = ir.periodId where ir.facilityId = r.facilityId and ili.productId = li.productId and li.doseId = ili.doseId and pps.startDate > ipps.startDate and extract(year from ipps.startDate) = extract(year from pps.startDate) ) as previousRegular , " +
            "(select sum(outreachMale + outreachFemale) from vaccine_report_coverage_age_group_line_items ili join vaccine_reports ir on ir.id = ili.reportId join processing_periods ipps on ipps.id = ir.periodId where ir.facilityId = r.facilityId and ili.productId = li.productId and li.doseId = ili.doseId and pps.startDate > ipps.startDate and extract(year from ipps.startDate) = extract(year from pps.startDate) ) as previousOutreach " +
            " from " +
            "   vaccine_report_coverage_age_group_line_items li join vaccine_reports r on r.id = li.reportId " +
            "     join processing_periods pps on pps.id = r.periodId " +
            "WHERE " +
            "     reportId = #{reportId} " +
            "order by li.id ASC")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "productId", column = "productId"),
            @Result(property = "product", javaType = Product.class, column = "productId",
                    many = @Many(select = "org.openlmis.ivdform.repository.mapper.reports.CoverageMapper.getProductDetails")),
            @Result(property = "vaccineProductDose", column = "id", one = @One(select = "org.openlmis.ivdform.repository.mapper.CoverageLineItemAgeGroupMapper.getVaccineDoseDetail"))
    })
    List<VaccineCoverageAgeGroupLineItem> getLineItems(@Param("reportId") Long reportId);
}
