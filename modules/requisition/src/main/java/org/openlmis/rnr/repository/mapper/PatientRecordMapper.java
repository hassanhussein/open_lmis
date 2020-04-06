package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.rnr.domain.Comment;
import org.openlmis.rnr.domain.PatientRecord;
import org.springframework.stereotype.Repository;



@Repository
public interface PatientRecordMapper {


    @Insert("INSERT INTO patient_records(rnrId, newConfirmedCase,totalRecovered,totalDeath,transfer,numberOfCumulativeCases,patientOnTreatment) " +
            "VALUES (#{rnrId}, #{newConfirmedCase}, #{totalRecovered}, #{totalDeath},#{transfer}, #{numberOfCumulativeCases},#{patientOnTreatment})")
    @Options(useGeneratedKeys = true)
    int insert(PatientRecord record);

    @Select("SELECT * FROM patient_records WHERE rnrId = #{rnrId}")
    PatientRecord getByRnrId(Long rnrId);

    @Update("UPDATE public.patient_records\n" +
            "   SET  rnrId=#{rnrId}, newConfirmedCase=#{newConfirmedCase}, totalRecovered=#{totalRecovered}, totalDeath=#{totalDeath}, \n" +
            "       transfer=#{transfer}, numberOfCumulativeCases=#{numberOfCumulativeCases}, patientOnTreatment=#{patientOnTreatment}\n" +
            " WHERE id=#{id};")
    void update(PatientRecord patientRecord);

}
