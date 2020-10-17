package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.rnr.domain.PatientLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientLineItemMapper {


    @Insert({"INSERT INTO patient_line_items(code, name, patientDisplayOrder, patientCategory,  " +
            "patientCategoryDisplayOrder, rnrId, modifiedBy, createdBy, skipped) values " +
            "(#{code}, #{name},  #{patientDisplayOrder}, #{category.name}, #{category" +
            ".displayOrder}, #{rnrId},  #{modifiedBy}, #{createdBy}, #{skipped})"})
    @Options(useGeneratedKeys = true)
    void insert(PatientLineItem patientLineItem);




    @Select("SELECT * FROM patient_line_items WHERE rnrId = #{rnrId} ORDER BY patientCategoryDisplayOrder, patientDisplayOrder")
    @Results(value = {

            @Result(property = "id", column = "id"),
            @Result(property = "code", column = "code"),
            @Result(property = "name", column = "name"),

            @Result(property = "patientDisplayOrder", column = "patientDisplayOrder"),
            @Result(property = "firstMonth", column = "firstMonth"),
            @Result(property = "secondMonth", column = "secondMonth"),

            @Result(property = "thirdMonth", column = "thirdMonth"),
            @Result(property = "fourthMonth", column = "fourthMonth"),
            @Result(property = "fifthMonth", column = "fifthMonth"),

            @Result(property = "sixthMonth", column = "sixthMonth"),
            @Result(property = "seventhMonth", column = "seventhMonth"),
            @Result(property = "eighthMonth", column = "eighthMonth"),

            @Result(property = "ninthMonth", column = "ninthMonth"),
            @Result(property = "tenthMonth", column = "tenthMonth"),
            @Result(property = "eleventhMonth", column = "eleventhMonth"),
            @Result(property = "twelfthMonth", column = "twelfthMonth"),
            @Result(property = "thirteenthMonth", column = "thirteenthMonth"),
            @Result(property = "fourteenthMonth", column = "fourteenthMonth"),
            @Result(property = "fifteenthMonth", column = "fifteenthMonth"),
            @Result(property = "sixteenthMonth", column = "sixteenthMonth"),
            @Result(property = "seventeenthMonth", column = "seventeenthMonth"),
            @Result(property = "eighteenthMonth", column = "eighteenthMonth"),
            @Result(property = "nineteenthMonth", column = "nineteenthMonth"),
            @Result(property = "twentiethMonth", column = "twentiethMonth"),

            @Result(property = "category.name", column = "patientCategory"),
            @Result(property = "category.displayOrder", column = "patientCategoryDisplayOrder"),
            @Result(property = "skipped", column = "skipped"),
    })
    List<PatientLineItem> getPatientLineItemsByRnrId(Long rnrId);


    @Update("UPDATE patient_line_items set firstMonth = #{firstMonth}, " +
            "secondMonth = #{secondMonth}, " +
            "thirdMonth = #{thirdMonth} ," +

            "fourthMonth = #{fourthMonth}, " +
            "fifthMonth = #{fifthMonth}, " +
            "sixthMonth = #{sixthMonth} ," +

            "seventhMonth = #{seventhMonth}, " +
            "eighthMonth = #{eighthMonth}, " +
            "ninthMonth = #{ninthMonth} ," +
            "tenthMonth = #{tenthMonth} ," +
            "eleventhMonth = #{eleventhMonth} ," +
            "thirteenthMonth = #{thirteenthMonth} ," +
            "fourteenthMonth = #{fourteenthMonth} ," +
            "fifteenthMonth = #{fifteenthMonth} ," +
            "sixteenthMonth = #{sixteenthMonth} ," +
            "seventeenthMonth = #{seventeenthMonth} ," +
            "eighteenthMonth = #{eighteenthMonth} ," +
            "nineteenthMonth = #{nineteenthMonth} ," +
            "twentiethMonth = #{twentiethMonth} ," +
            "skipped = #{skipped} ," +

            "modifiedBy = #{modifiedBy}, modifiedDate =CURRENT_TIMESTAMP where id=#{id}")
    void update(PatientLineItem patientLineItem);
}
