package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.*;
import org.openlmis.rnr.domain.MonitoringReport;
import org.openlmis.rnr.dto.MonitoringReportDTO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonitoringMapper {

    @Insert(" INSERT INTO public.monitoring_reports(\n" +
            "            facilityId,programId,supervisoryNodeId, status,nameOfHidTu,numberOfHidTu, numberOfCumulativeCases, \n" +
            "            patientOnTreatment, numberOfStaff, reportedDate, createdBy, createdDate, \n" +
            "            modifiedBy, modifiedDate)\n" +
            "    VALUES (#{facilityId},#{programId},#{supervisoryNodeId},#{status}, #{nameOfHidTu}, #{numberOfHidTu}, #{numberOfCumulativeCases}, \n" +
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

    @Select("select * from monitoring_reports where programId = #{programId} and facilityId = #{facilityId} and status='INITIATED'")
    MonitoringReport getReportByProgramAndFacility(@Param("facilityId") Long facilityId,@Param("programId") Long programId);

    @Select("select * from monitoring_reports where programId = #{programId} and facilityId = #{facilityId} and status = 'INITIATED'")
    MonitoringReport getDraftReport(@Param("facilityId") Long facilityId,@Param("programId") Long programId);

    @Select("select * from monitoring_reports where reportedDate = #{reportedDate}::date and  programId = #{programId} and facilityId = #{facilityId}")
    MonitoringReport getAlreadySubmittedReport(@Param("facilityId") Long facilityId,@Param("programId") Long programId, @Param("reportedDate") String reportDate);

    @Select("select * from monitoring_reports where  programId = #{programId} and facilityId = #{facilityId} order by id desc limit 1")
    MonitoringReport getPreviousReport(@Param("facilityId") Long facilityId,@Param("programId") Long programId,@Param("reportedDate") String reportedDate);


    @Select("SELECT " +
            "   r.id, f.name as facilityName, f.code as facilityCode, z.name as districtName, r.status, r.modifiedDate submissionDate, r.reportedDate " +
            "from monitoring_reports r " +
            "join facilities f on f.id = r.facilityId " +
            "join geographic_zones z on z.id = f.geographicZoneId " +
            "where " +
            "r.status = 'SUBMITTED' " +
            "and facilityId = ANY( #{facilityIds}::INT[] )")
    List<MonitoringReportDTO> pendingForApproval(@Param("facilityIds") String facilityIds);

}
