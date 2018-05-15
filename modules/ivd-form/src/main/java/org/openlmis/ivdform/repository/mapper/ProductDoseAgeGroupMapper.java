package org.openlmis.ivdform.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.ivdform.domain.VaccineDose;
import org.openlmis.ivdform.domain.VaccineProductDoseAgeGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDoseAgeGroupMapper {

    @Insert("INSERT INTO public.vaccine_product_dose_age_groups(\n" +
            "             ageGroupId, ageGroupName, doseId, programId, productId, displayOrder, \n" +
            "            createdBy, createddate, modifiedBy, modifieddate)\n" +
            "    VALUES (#{ageGroupId}, #{ageGroupName}, #{doseId}, #{programId}, #{productId}, #{displayOrder}, \n" +
            "            #{createdBy}, NOW(),#{modifiedBy} , NOW());")
    @Options(useGeneratedKeys = true)
    Integer insert(VaccineProductDoseAgeGroup ageGroup);

    @Update("UPDATE public.vaccine_product_dose_age_groups\n" +
            "   SET agegroupid=#{agegroupId}, agegroupname=#{ageGroupName}, doseid=#{doseId}, programid=#{programId}, productid=#{productId}, \n" +
            "       displayorder=#{displayOrder}, createdby=#{createdBy}, createddate=#{createdDate}, modifiedby=#{modifiedBy}, modifieddate=#{modifiedDate}\n" +
            " WHERE id = #{id}")
    void update(VaccineProductDoseAgeGroup ageGroup);


    @Select("select d.*, pr.primaryName as productName from vaccine_product_dose_age_groups d " +
            " join products pr on pr.id = d.productId " +
            "where d.programId = #{programId} " +
            " order by pr.id,d.displayOrder ASC" )
    List<VaccineProductDoseAgeGroup> getProgramProductDosesAgeGroups(@Param("programId") Long programId);

    @Select("select * from vaccine_doses order by displayOrder")
    List<VaccineDose> getAllDoses();

    @Select("select pd.* from vaccine_product_dose_age_groups pd " +
            " join vaccine_doses d on d.id = pd.doseId " +
            " where " +
            " productId = #{productId} and pd.programId = #{programId} " +
            " order by pd.displayOrder")
    List<VaccineProductDoseAgeGroup> getDoseSettingByProductForAgeGroup(@Param("programId") Long programId, @Param("productId") Long productId);

    @Delete("delete from vaccine_product_dose_age_groups where programId = #{programId}")
    void deleteAllByProgram(@Param("programId") Long programId);
}
