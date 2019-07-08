package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.Select;
import org.openlmis.rnr.domain.PatientColumn;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientColumnMapper {


    @Select("SELECT * FROM program_patient_columns WHERE programId = #{programId} ORDER BY displayOrder")
    List<PatientColumn> getAllPatientColumnsByProgramId(Long programId);
}
