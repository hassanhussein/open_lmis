package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.openlmis.core.domain.Patient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientMapper {


    @Select({"SELECT * FROM patients P INNER JOIN patient_categories RC ON P.categoryId = RC.id ",
            "WHERE P.programId=#{programId} ORDER BY RC.displayOrder,P.displayOrder"})
    @Results(value = {
            @Result(property = "category", column = "categoryId", javaType = Long.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.PatientCategoryMapper.getById"))})
    List<Patient> getByProgram(Long programId);
}
