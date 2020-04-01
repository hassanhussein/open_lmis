package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.*;
import org.openlmis.rnr.domain.MonitoringReport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonitoringMapper {

    @Insert(" INSERT INTO public.monitoring_reports(\n" +
            "            facilityId,programId,supervisoryNodeId, status,nameOfHidTu,numberOfHidTu, numberOfCumulativeCases, \n" +
            "            patientOnTreatment, numberOfStaff, reportedDate, createdBy, createddate, \n" +
            "            modifiedBy, modifiedDate)\n" +
            "    VALUES (#{districtId},#{programId},#{supervisoryNodeId},#{status}, #{nameOfHidTu}, #{numberOfHidTu}, #{numberOfCumulativeCases}, \n" +
            "            #{patientOnTreatment}, #{numberOfStaff}, #{reportedDate}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, NOW());\n ")
    @Options(useGeneratedKeys = true)
    Integer insert(MonitoringReport report);


    @Update("UPDATE public.monitoring_reports\n" +
            "   SET programId=#{programId}, supervisoryNodeId=#{supervisoryNodeId},status = #{status}, facilityId=#{facilityId}, nameofhidtu=#{nameOfHidTu}, numberOfHidTu=#{numberOfHidTu}, numberOfCumulativeCases=#{numberOfCumulativeCases}, \n" +
            "       patientOnTreatment=#{patientOnTreatment}, numberOfStaff=#{numberOfStaff}, reportedDate=#{reportedDate} \n" +
            "        ,modifiedBy=#{modifiedBy}, modifiedDate=NOW()\n" +
            " WHERE id = #{id}; ")
    void update(MonitoringReport report);

    @Select("SELECT * FROM monitoring_reports WHERE programId = #{programId} and status ='INITIATED' and facilityId = #{facilityId} ")
    MonitoringReport getBy(@Param("facilityId") Long facilityId,@Param("programId") Long programId, Long userId,@Param("reportedDate") String reportedDate);

    @Select(" select * from monitoring_reports where id = #{id} ")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "facilityId", column = "facilityId"),
            @Result(property = "programId", column = "programId"),
            @Result(property = "lineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.MonitoringLineItemMapper.getLineItems")),
            @Result(property = "reportStatusChanges", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.MonitoringReportStatusChangeMapper.getChangeLogByReportId")),
            @Result(property = "facility", javaType = Facility.class, column = "facilityId",
                    many = @Many(select = "org.openlmis.core.repository.mapper.FacilityMapper.getById")),
            @Result(property = "program", javaType = Program.class, column = "programId",
                    many = @Many(select = "org.openlmis.core.repository.mapper.ProgramMapper.getById"))

    })
    MonitoringReport getReportById(@Param("id") Long id);

}
