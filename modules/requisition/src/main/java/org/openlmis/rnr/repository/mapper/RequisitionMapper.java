/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.*;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.domain.RnrRejection;
import org.openlmis.rnr.dto.RnrDTO;
import org.openlmis.rnr.service.RequisitionService;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * It maps the Rnr entity to corresponding representation in database.
 */

@Repository
public interface RequisitionMapper {

    @Insert("INSERT INTO requisitions(facilityId, programId, periodId, status, sourceApplication, emergency, allocatedBudget, modifiedBy, createdBy) " +
            "VALUES (#{facility.id}, #{program.id}, #{period.id}, #{status}, #{sourceApplication}, #{emergency}, #{allocatedBudget}, #{modifiedBy}, #{createdBy})")
    @Options(useGeneratedKeys = true)
    void insert(Rnr requisition);

    @Update({"UPDATE requisitions SET",
            "modifiedBy = #{modifiedBy}, modifiedDate = CURRENT_TIMESTAMP, status = #{status},",
            "fullSupplyItemsSubmittedCost = #{fullSupplyItemsSubmittedCost},",
            "nonFullSupplyItemsSubmittedCost = #{nonFullSupplyItemsSubmittedCost},",
            "supervisoryNodeId = #{supervisoryNodeId}",
            "WHERE id = #{id}"})
    void update(Rnr requisition);

    @Select("SELECT * FROM requisitions WHERE id = #{rnrId}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "period.id", column = "periodId"),
            @Result(property = "fullSupplyLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RnrLineItemMapper.getRnrLineItemsByRnrId")),
            @Result(property = "nonFullSupplyLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RnrLineItemMapper.getNonFullSupplyRnrLineItemsByRnrId")),
            @Result(property = "regimenLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RegimenLineItemMapper.getRegimenLineItemsByRnrId")),
            @Result(property = "equipmentLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.EquipmentLineItemMapper.getEquipmentLineItemsByRnrId")),
            @Result(property = "patientQuantifications", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.PatientQuantificationLineItemMapper.getPatientQuantificationLineItemsByRnrId")),
            @Result(property = "rnrSignatures", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RequisitionMapper.getRnrSignaturesByRnrId")),
            @Result(property = "manualTestLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.ManualTestsLineItemMapper.getManualTestLineItemsByRnrId"))
    })
    Rnr getById(Long rnrId);

    @Deprecated
    @Select({"SELECT id, emergency, programId, facilityId, periodId, modifiedDate",
            "FROM requisitions ",
            "WHERE programId =  #{programId}",
            "AND supervisoryNodeId =  #{supervisoryNode.id} AND status IN ('AUTHORIZED', 'IN_APPROVAL')"})
    @Results({@Result(property = "program.id", column = "programId"),
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "period.id", column = "periodId")})
    List<Rnr> getAuthorizedRequisitions(RoleAssignment roleAssignment);

    @Select({"SELECT r.id, r.emergency, r.programId, r.facilityId, r.periodId, r.modifiedDate\n" +
            "           , p.id as programId, p.code as programCode, p.name as programName  \n" +
            "           , f.id as facilityId, f.code as facilityCode, f.name as facilityName  \n" +
            "           , ft.name as facilityType  \n" +
            "           , gz.name as districtName  \n" +
            "           , pr.StartDate as periodStartDate, pr.endDate as periodEndDate, pr.name as periodName\n" +
            "           , (select max(createdDate) from requisition_status_changes rsc where rsc.rnrId = r.id and rsc.status = 'SUBMITTED') as submittedDate\n" +
            "       FROM requisitions r  \n" +
            "           join programs p on p.id = r.programId  \n" +
            "           join facilities f on f.id = r.facilityId  \n" +
            "           join processing_periods pr on pr.id = r.periodId  \n" +
            "           join facility_types ft on ft.id = f.typeId  \n" +
            "           join geographic_zones gz on gz.id = f.geographicZoneId\n" +
            "          join (SELECT   r.emergency, r.programId, r.facilityId, max(r.periodId) periodId\n" +
            "             from requisitions r\n" +
            "             WHERE programId =   #{programId}\n" +
            "             AND supervisoryNodeId =  #{supervisoryNode.id} AND status IN ('AUTHORIZED', 'IN_APPROVAL')\n" +
            "             GROUP BY 1,2,3 ORDER BY r.facilityid) t\n" +
            "           on t.programid =r.programid and t.periodId=r.periodid and t.facilityid=r.facilityid and t.emergency=r.emergency\n" +
            "      WHERE r.programId =   #{programId}\n" +
            "      AND supervisoryNodeId =  #{supervisoryNode.id} AND status IN ('AUTHORIZED', 'IN_APPROVAL')\n" +
            "order by r.facilityid"})
    List<RnrDTO> getAuthorizedRequisitionsDTO(RoleAssignment roleAssignment);

    @Select("SELECT * FROM requisitions WHERE facilityId = #{facility.id} AND programId= #{program.id} AND periodId = #{period.id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "period.id", column = "periodId"),
            @Result(property = "fullSupplyLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RnrLineItemMapper.getRnrLineItemsByRnrId")),
            @Result(property = "nonFullSupplyLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RnrLineItemMapper.getNonFullSupplyRnrLineItemsByRnrId")),
            @Result(property = "regimenLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RegimenLineItemMapper.getRegimenLineItemsByRnrId")),
            @Result(property = "equipmentLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.EquipmentLineItemMapper.getEquipmentLineItemsByRnrId")),
    })
    Rnr getRequisitionWithLineItems(@Param("facility") Facility facility, @Param("program") Program program, @Param("period") ProcessingPeriod period);

    @Select({"SELECT * FROM requisitions r",
            "WHERE facilityId = #{facility.id}"}
    )
    @Results(value = {
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "program", javaType = Program.class, column = "programId",
                    one = @One(select = "org.openlmis.core.repository.mapper.ProgramMapper.getById")),
            @Result(property = "period.id", column = "periodId"),
            @Result(property = "fullSupplyLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RnrLineItemMapper.getNonSkippedRnrLineItemsByRnrId")),
            @Result(property = "nonFullSupplyLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RnrLineItemMapper.getNonSkippedNonFullSupplyRnrLineItemsByRnrId")),
            @Result(property = "regimenLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RegimenLineItemMapper.getRegimenLineItemsByRnrId")),
            @Result(property = "equipmentLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.EquipmentLineItemMapper.getEquipmentLineItemsByRnrId")),
            @Result(property = "patientQuantifications", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.PatientQuantificationLineItemMapper.getPatientQuantificationLineItemsByRnrId")),
            @Result(property = "period", column = "periodId", javaType = ProcessingPeriod.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.ProcessingPeriodMapper.getById")),
            @Result(property = "clientSubmittedTime", column = "clientSubmittedTime"),
            @Result(property = "clientSubmittedNotes", column = "clientSubmittedNotes"),
            @Result(property = "rnrSignatures", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RequisitionMapper.getRnrSignaturesByRnrId")
            )
    })
    List<Rnr> getRequisitionsWithLineItemsByFacility(@Param("facility") Facility facility);

    @Select({"SELECT * FROM requisitions R",
            "WHERE facilityId = #{facilityId}",
            "AND programId = #{programId} ",
            "AND status NOT IN ('INITIATED', 'SUBMITTED')",
            "AND emergency = false",
            "ORDER BY (select startDate from processing_periods where id=R.periodId) DESC LIMIT 1"})
    @Results(value = {
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "period.id", column = "periodId")
    })
    Rnr getLastRegularRequisitionToEnterThePostSubmitFlow(@Param(value = "facilityId") Long facilityId, @Param(value = "programId") Long programId);

    @Select({"SELECT * FROM requisitions WHERE",
            "facilityId = #{facility.id} AND",
            "programId = #{program.id} AND ",
            "periodId = ANY (#{periods}::INTEGER[]) AND ",
            "status NOT IN ('INITIATED', 'SUBMITTED')"})
    @Results(value = {
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "period.id", column = "periodId")
    })
    List<Rnr> getPostSubmitRequisitions(@Param("facility") Facility facility, @Param("program") Program program, @Param("periods") String periodIds);

    @Select({"SELECT * FROM requisitions WHERE",
            "facilityId = #{facilityId} AND",
            "programId = #{programId} AND ",
            "periodId = #{periodId} AND ",
            "emergency = false"
    })
    @Results(value = {
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "period.id", column = "periodId")
    })
    Rnr getRequisitionWithoutLineItems(@Param("facilityId") Long facilityId, @Param("programId") Long programId, @Param("periodId") Long periodId);

    @Select("SELECT * FROM requisitions WHERE id = #{rnrId}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "period.id", column = "periodId"),
            @Result(property = "supplyingFacility.id", column = "supplyingFacilityId")
    })
    Rnr getLWById(Long rnrId);

    @Select("SELECT * FROM requisitions WHERE facilityId = #{facility.id} AND programId= #{program.id} AND periodId = #{period.id} AND emergency = FALSE")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "period.id", column = "periodId"),
            @Result(property = "fullSupplyLineItems", javaType = List.class, column = "id",
                    many = @Many(select = "org.openlmis.rnr.repository.mapper.RnrLineItemMapper.getRnrLineItemsByRnrId")),
    })
    Rnr getRegularRequisitionWithLineItems(@Param("facility") Facility facility, @Param("program") Program program, @Param("period") ProcessingPeriod period);

    @Select({"SELECT * FROM requisitions WHERE",
            "facilityId = #{facilityId} AND",
            "programId = #{programId} AND",
            "emergency = TRUE AND",
            "status IN ('INITIATED', 'SUBMITTED') ORDER BY createdDate DESC"
    })
    @Results(value = {
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "period", column = "periodId", javaType = ProcessingPeriod.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.ProcessingPeriodMapper.getById"))
    })
    List<Rnr> getInitiatedOrSubmittedEmergencyRequisitions(@Param("facilityId") Long facilityId,
                                                           @Param("programId") Long programId);

    @SelectProvider(type = ApprovedRequisitionSearch.class, method = "getApprovedRequisitionsByCriteria")
    @Results(value = {
            @Result(property = "program.id", column = "programId"),
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "period.id", column = "periodId"),
            @Result(property = "supplyingFacility.id", column = "supplyingFacilityId")
    })
    List<Rnr> getApprovedRequisitionsForCriteriaAndPageNumber(@Param("searchType") String searchType, @Param("searchVal") String searchVal,
                                                              @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize,
                                                              @Param("userId") Long userId, @Param("right") String rightName,
                                                              @Param("sortBy") String sortBy, @Param("sortDirection") String sortDirection);


    @SelectProvider(type = RequisitionPendingApproval.class, method = "getCountingRequisitionPendingApproval")
    Integer getCountOfRequisitionsPendingApprovalForCriteria(
            @Param("programId") Long programId,
            @Param("scheduleId") Long scheduleId,
            @Param("year") String year,
            @Param("periodId") Long periodId,
            @Param("zoneId") Long zoneId,
            @Param("searchType") String searchType,
            @Param("searchVal") String searchVal,
            @Param("userId") Long userId,
            @Param("right") String rightName);


    @SelectProvider(type = RequisitionPendingApproval.class, method = "getRequisitionPendingApproval")
    @Results(value = {
            @Result(property = "program.id", column = "programId"),
            @Result(property = "program.name", column = "program"),
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "facility.code", column = "facilityCode"),
            @Result(property = "facility.name", column = "facility"),
            @Result(property = "period.id", column = "periodId"),
            @Result(property = "period.startDate", column = "startdate"),
            @Result(property = "period.endDate", column = "enddate")
    })
    List<Rnr> getPendingApprovalRequisitions(
            @Param("programId") Long programId,
            @Param("scheduleId") Long scheduleId,
            @Param("year") String year,
            @Param("periodId") Long periodId,
            @Param("zoneId") Long zoneId,
            @Param("searchType") String searchType, @Param("searchVal") String searchVal,
            @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize,
            @Param("userId") Long userId, @Param("right") String rightName,
            @Param("sortBy") String sortBy, @Param("sortDirection") String sortDirection);

    @SelectProvider(type = ApprovedRequisitionSearch.class, method = "getCountOfApprovedRequisitionsForCriteria")
    Integer getCountOfApprovedRequisitionsForCriteria(@Param("searchType") String searchType, @Param("searchVal") String searchVal,
                                                      @Param("userId") Long userId, @Param("right") String rightName);

    @Select({"SELECT facilityId FROM requisitions WHERE id = #{id}"})
    Long getFacilityId(Long id);

    @Select({"SELECT r.* FROM requisitions r join processing_periods pr on pr.id = r.periodId WHERE r.facilityId = #{facility.id} AND r.programId = #{program.id} AND r.emergency = false",
            "ORDER BY pr.startDate DESC LIMIT 1"})
    @Results(value = {
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "period.id", column = "periodId")
    })
    Rnr getLastRegularRequisition(@Param("facility") Facility facility, @Param("program") Program program);

    @Select("SELECT programId FROM requisitions WHERE id = #{rnrId}")
    Long getProgramId(Long rnrId);

    @Select("select * from fn_delete_rnr( #{rnrId} )")
    String deleteRnR(@Param("rnrId") Integer rnrId);

    @Update({"UPDATE requisitions SET",
            "clientSubmittedNotes = COALESCE(#{clientSubmittedNotes}, clientSubmittedNotes),",
            "clientSubmittedTime = COALESCE(#{clientSubmittedTime}, clientSubmittedTime)",
            "WHERE id = #{id}"})
    void updateClientFields(Rnr rnr);

    @Insert("INSERT INTO requisition_signatures(signatureId, rnrId) VALUES " +
            "(#{signature.id}, #{rnr.id})")
    void insertRnrSignature(@Param("rnr") Rnr rnr, @Param("signature") Signature signature);

    @Select("SELECT * FROM requisition_signatures " +
            "JOIN signatures " +
            "ON signatures.id = requisition_signatures.signatureId " +
            "WHERE requisition_signatures.rnrId = #{rnrId} ")
    List<Signature> getRnrSignaturesByRnrId(Long rnrId);

    @Select("SELECT * FROM requisitions " +
            "WHERE " +
            " facilityId = #{facilityId} " +
            "and periodId = #{periodId} " +
            "and programId = #{programId} " +
            "and emergency = #{emergency}")
    Rnr getRnrBy(@Param("facilityId") Long facilityId, @Param("periodId") Long periodId, @Param("programId") Long programId, @Param("emergency") boolean emergency);

    @Select({"SELECT r.* from requisitions r",
            "JOIN processing_periods p " +
                    "on p.id = r.periodid " +
                    "and p.startDate < (select max(pp.startDate) from processing_periods pp where pp.id = #{rnr.period.id})",
            "where ",
            "r.facilityid = #{rnr.facility.id} ",
            "and r.programid = #{rnr.program.id} ",
            "and r.emergency = false ",
            "and r.status = 'APPROVED'"})
    List<Rnr> getUnreleasedPreviousRequisitions(@Param("rnr") Rnr rnr);

    @Select({"SELECT * FROM requisitions WHERE ",
            "facilityId = ANY (#{commaSeparatedFacilityIds}::INTEGER[]) AND ",
            "programId = ANY (#{commaSeparatedProgramIds}::INTEGER[]) AND ",
            "periodId = ANY (#{commaSeparatedPeriodIds}::INTEGER[]) AND ",
            "status NOT IN ('INITIATED', 'SUBMITTED') " +
                    "AND emergency = true "})
    @Results(value = {
            @Result(property = "facility.id", column = "facilityId"),
            @Result(property = "program.id", column = "programId"),
            @Result(property = "period.id", column = "periodId")
    })
    List<Rnr> getPostSubmitEmergencyOnlyRequisitions(@Param("commaSeparatedFacilityIds") String commaSeparatedFacilityIds,
                                                     @Param("commaSeparatedProgramIds") String commaSeparatedProgramIds,
                                                     @Param("commaSeparatedPeriodIds") String commaSeparatedPeriodIds);

    @Insert("INSERT INTO requisition_rejection(rnr_id, rnr_status_from,rnr_status_to,  description) VALUES " +
            " (#{rnr.id}, #{statusFrom}, #{statusTo},#{reasons})")
    void insertRnrRejection(RnrRejection rnrRejection);


    public class ApprovedRequisitionSearch {

        @SuppressWarnings("UnusedDeclaration")
        public static String getApprovedRequisitionsByCriteria(Map<String, Object> params) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT R.id, R.emergency, R.programId, R.facilityId, R.periodId, R.status, R.supervisoryNodeId," +
                    " R.modifiedDate as modifiedDate, RSC.createdDate as submittedDate, P.name AS programName, F.name AS facilityName," +
                    " F.code AS facilityCode, SF.name AS supplyingDepotName, PP.startDate as periodStartDate, PP.endDate as periodEndDate" +
                    " FROM Requisitions R INNER JOIN  (select status, rnrId, max(createdDate) createdDate from requisition_status_changes group by rnrId, status) RSC ON R.id = RSC.rnrId AND RSC.status = 'SUBMITTED' " +
                    " INNER JOIN processing_periods PP ON PP.id = R.periodId ");

            appendQueryClausesBySearchType(sql, params);

            Integer pageNumber = (Integer) params.get("pageNumber");
            Integer pageSize = (Integer) params.get("pageSize");
            String sortBy = (String) params.get("sortBy");
            String sortDirection = (String) params.get("sortDirection");

            return sql.append("ORDER BY " + sortBy + " " + sortDirection).append(" LIMIT ").append(pageSize)
                    .append(" OFFSET ").append((pageNumber - 1) * pageSize).toString();
        }

        @SuppressWarnings("UnusedDeclaration")
        public static String getCountOfApprovedRequisitionsForCriteria(Map params) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COUNT(DISTINCT R.id) FROM Requisitions R ");

            appendQueryClausesBySearchType(sql, params);
            return sql.toString();
        }

        private static void appendQueryClausesBySearchType(StringBuilder sql, Map<String, Object> params) {
            String searchType = (String) params.get("searchType");
            String searchVal = ((String) params.get("searchVal")).toLowerCase();
            Long userId = (Long) params.get("userId");
            String right = (String) params.get("right");

            if (userId != null && right != null) {
                sql.append("INNER JOIN supply_lines S ON R.supervisoryNodeId = S.supervisoryNodeId " +
                        "INNER JOIN fulfillment_role_assignments FRA ON S.supplyingFacilityId = FRA.facilityId " +
                        "INNER JOIN role_rights RR ON FRA.roleId = RR.roleId " +
                        "INNER JOIN Programs P ON P.id = R.programId " +
                        "INNER JOIN Facilities F ON F.id = R.facilityId " +
                        "LEFT JOIN Supply_lines SL ON (SL.supervisoryNodeId = R.supervisoryNodeId AND SL.programId = R.programId) " +
                        "LEFT JOIN Facilities SF ON SL.supplyingFacilityId = SF.id ");
            }

            if (searchVal.isEmpty()) {
                sql.append("WHERE ");
            } else if (searchType.isEmpty() || searchType.equalsIgnoreCase(RequisitionService.SEARCH_ALL)) {
                sql.append("WHERE (LOWER(P.name) LIKE '%" + searchVal + "%' OR LOWER(F.name) LIKE '%" +
                        searchVal + "%' OR LOWER(F.code) LIKE '%" + searchVal + "%' OR LOWER(SF.name) LIKE '%" + searchVal + "%') AND ");
            } else if (searchType.equalsIgnoreCase(RequisitionService.SEARCH_FACILITY_CODE)) {
                sql.append("WHERE LOWER(F.code) LIKE '%" + searchVal + "%' AND ");
            } else if (searchType.equalsIgnoreCase(RequisitionService.SEARCH_FACILITY_NAME)) {
                sql.append("WHERE LOWER(F.name) LIKE '%" + searchVal + "%' AND ");
            } else if (searchType.equalsIgnoreCase(RequisitionService.SEARCH_PROGRAM_NAME)) {
                sql.append("WHERE LOWER(P.name) LIKE '%" + searchVal + "%' AND ");
            } else if (searchType.equalsIgnoreCase(RequisitionService.SEARCH_SUPPLYING_DEPOT_NAME)) {
                sql.append("WHERE LOWER(SF.name) LIKE '%" + searchVal + "%' AND ");
            }
            sql.append("FRA.userId = " + userId + " AND RR.rightName = '" + right + "' AND ");
            sql.append("R.status = 'APPROVED'");
        }
    }


    public class RequisitionPendingApproval {


        private static final String PROGRAM_ID = "programId";
        private static final String SCHEDULE_ID = "scheduleId";
        private static final String YEAR = "year";
        private static final String PERIOD_ID = "periodId";
        private static final String ZONE_ID = "zoneId";
        private static final String SEARCH_TYPE = "searchType";
        private static final String SEARCH_VAL = "searchVal";
        private static final String USER_ID = "userId";
        private static final String RIGHT_NAME = "rightName";


        public static String getRequisitionPendingApproval(Map params) {
            Long programId = (Long) params.get(PROGRAM_ID);
            Long scheduleId = (Long) params.get(SCHEDULE_ID);
            String year = (String) params.get(YEAR);
            Long periodId = (Long) params.get(PERIOD_ID);
            Long zoneId = (Long) params.get(ZONE_ID);
            String searchType = (String) params.get(SEARCH_TYPE);
            String searchVal = params.get(SEARCH_VAL) != null ? (String) params.get(SEARCH_VAL) : " ";
            Long userId = (Long) params.get(USER_ID);
            String rightName = "";

            return getQuery(programId, scheduleId, year, periodId, zoneId,
                    searchType, searchVal, userId, rightName);
        }

        public static String getCountingRequisitionPendingApproval(Map params) {
            Long programId = (Long) params.get(PROGRAM_ID);
            Long scheduleId = (Long) params.get(SCHEDULE_ID);
            String year = (String) params.get(YEAR);
            Long periodId = (Long) params.get(PERIOD_ID);
            Long zoneId = (Long) params.get(ZONE_ID);
            String searchType = (String) params.get(SEARCH_TYPE);
            String searchVal = params.get(SEARCH_VAL) != null ? (String) params.get(SEARCH_VAL) : " ";
            Long userId = (Long) params.get(USER_ID);
            String rightName = "";

            return getCountQuery(programId, scheduleId, year, periodId, zoneId,
                    searchType, searchVal, userId, rightName);
        }

        public static String getQuery(Long programId, Long scheduleId, String year, Long periodId, Long zoneId,
                                      String searchType, String searchVal, Long userId, String rightName) {

            BEGIN();
            SELECT_DISTINCT("r.id AS rnrid");
            SELECT("r.emergency");
            SELECT("r.status");
            SELECT("r.createddate as submittedDate");
            SELECT("r.modifieddate");
            SELECT(" d.zone_id AS zoneid");
            SELECT(" d.parent");
            SELECT(" d.district_name AS district");
            SELECT(" d.district_id AS districtid");
            SELECT(" d.region_id AS provinceid");
            SELECT(" d.region_name AS province");
            SELECT("f.name AS facility");
            SELECT("f.feconfigured");
            SELECT("ft.id AS facilitytypeid");
            SELECT("ft.name AS facilitytype");
            SELECT("ft.nominalmaxmonth");
            SELECT("ft.nominaleop");
            SELECT(" ft.code AS facilitytypecode");
            SELECT("pr.name AS program");
            SELECT("pr.id AS programid");
            SELECT("pp.id AS periodid");
            SELECT("pp.name AS period");
            SELECT("pp.startdate");
            SELECT("pp.enddate");
            SELECT("pp.scheduleid");
            SELECT(" f.code AS facilitycode");
            SELECT("r.id AS facilityid");
            FROM("requisitions r");
            INNER_JOIN(" processing_periods pp ON r.periodid = pp.id");
            INNER_JOIN(" programs pr ON pr.id = r.programid    ");
            INNER_JOIN("  facilities f ON f.id = r.facilityid");
            INNER_JOIN(" facility_types ft ON ft.id = f.typeid");
            INNER_JOIN("  vw_districts d ON f.geographiczoneid = d.district_id  ");
            WHERE("r.status::text = ANY (ARRAY['IN_APPROVAL'::text, 'APPROVED'::text])");
            writePredicates(programId, scheduleId, year, periodId, zoneId,
                    searchType, searchVal, userId, rightName);
            String query = SQL();
            return query;
        }

        public static String getCountQuery(Long programId, Long scheduleId, String year, Long periodId, Long zoneId,
                                           String searchType, String searchVal, Long userId, String rightName) {

            BEGIN();
            SELECT_DISTINCT("count(*)");
            FROM("requisitions r");
            INNER_JOIN(" processing_periods pp ON r.periodid = pp.id");
            INNER_JOIN(" programs pr ON pr.id = r.programid    ");
            INNER_JOIN("  facilities f ON f.id = r.facilityid");
            INNER_JOIN(" facility_types ft ON ft.id = f.typeid");
            INNER_JOIN("  vw_districts d ON f.geographiczoneid = d.district_id  ");
            WHERE("r.status::text = ANY (ARRAY['IN_APPROVAL'::text, 'APPROVED'::text])");
            writePredicates(programId, scheduleId, year, periodId, zoneId,
                    searchType, searchVal, userId, rightName);
            String query = SQL();
            return query;
        }

        private static void writePredicates(Long programId, Long scheduleId, String year, Long periodId, Long zoneId,
                                            String searchType, String searchVal, Long userId, String rightName) {

            WHERE("r.programid= " + programId);
            if (zoneId != 0) {
                WHERE("(d.zone_id =" + zoneId +
                        " or d.district_id=" + zoneId +
                        " or d.region_id=" + zoneId + ")");
            }
            if (periodId != 0) {
                WHERE("r.periodid=" + periodId);
            }

        }
    }
}

