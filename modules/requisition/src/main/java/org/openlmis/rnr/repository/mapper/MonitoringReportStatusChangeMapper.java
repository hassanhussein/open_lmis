package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.rnr.domain.ReportStatusChange;
import org.openlmis.rnr.domain.RnrStatus;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MonitoringReportStatusChangeMapper {

    @Insert("INSERT into monitoring_report_status_changes (reportId, status, comment, createdBy, createdDate, modifiedBy, modifiedDate) " +
            " values " +
            " (#{reportId}, #{status}, #{comment}, #{createdBy}, NOW(), #{modifiedBy}, NOW())")
    @Options(useGeneratedKeys = true)
    Integer insert(ReportStatusChange change);

    @Select("SELECT sc.status, sc.reportId, sc.createdDate as date,  u.username, u.firstName, u.lastName, sc.comment  from monitoring_report_status_changes sc join users u on u.id = sc.createdBy where reportId = #{reportId}")
    List<ReportStatusChange> getChangeLogByReportId(@Param("reportId") Long reportId);

    @Select("SELECT sc.status, sc.reportId, sc.createdDate as date, u.username, u.firstName, u.lastName, sc.comment " +
            "from monitoring_report_status_changes sc join users u on u.id = sc.createdBy " +
            "where reportId = #{reportId} and status = #{operation} " +
            "order by sc.createdDate desc limit 1")
    ReportStatusChange getOperationLog(@Param("reportId") Long reportId, @Param("operation") RnrStatus status);


}
