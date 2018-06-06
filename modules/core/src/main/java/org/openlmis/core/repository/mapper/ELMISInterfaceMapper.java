/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 *   Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 *   This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.core.repository.mapper;


import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.DosageUnit;
import org.openlmis.core.domain.ELMISInterface;
import org.openlmis.core.domain.ELMISInterfaceDataSet;
import org.openlmis.core.domain.ELMISInterfaceFacilityMapping;
import org.openlmis.core.dto.ELMISInterfaceDataSetDTO;
import org.openlmis.core.dto.ELMISInterfaceFacilityMappingDTO;
import org.openlmis.core.dto.ResponseDTO;
import org.openlmis.core.repository.ELMISInterfaceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ELMISInterfaceMapper {

    @Select({"select * from interface_apps where id = #{interfaceId}"})
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "dataSets",  column = "id", javaType = List.class,  many = @Many(select = "getInterfaceDatasetById"))
    })
    ELMISInterface get(Long interfaceId);

    @Select({"select * from interface_dataset where interfaceid = #{interfaceId}"})
    List<ELMISInterfaceDataSet> getInterfaceDatasetById(Long interfaceId);

    @Insert({
            "INSERT INTO interface_apps (name, active, createddate, createdby, modifiedby, modifieddate ) " +
                    "VALUES (#{name}, #{active}, COALESCE(#{createdDate}, NOW()), #{createdBy}, #{modifiedBy}," +
                    " COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP))"
    })
    @Options(useGeneratedKeys = true)
    Integer insert(ELMISInterface elmisInterface);

    @Update({
            "UPDATE interface_apps \n" +
                    " SET  name=#{name}, active=#{active}, modifiedBy = #{modifiedBy}, modifiedDate = (COALESCE(#{modifiedDate}, NOW())) "+
                    " WHERE id = #{id}"

    })
    Integer update(ELMISInterface elmisInterface);

    @Update("UPDATE interface_dataset\n" +
            "   SET datasetname=#{dataSetname}, interfaceid=#{interfaceId}, datasetid=#{dataSetId}, createdby=#{createdBy}, createddate=COALESCE(#{createdDate}, NOW())," +
            " modifiedby= #{modifiedBy}, \n" +
            "       modifieddate= COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP),elmisCode = #{elmisCode}  \n" +
            " WHERE id = #{id} ")
    Integer updateDataSet(ELMISInterfaceDataSet dataSet);

    @Insert("INSERT INTO interface_dataset(" +
            "             interfaceid, datasetname, datasetid, " +
            "            modifiedby, modifieddate, createdby, createddate,elmisCode)" +
            "    VALUES (#{interfaceId}, #{dataSetname}, #{dataSetId}, " +
            "     #{modifiedBy}, COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP), #{createdBy}, COALESCE(#{createdDate}, NOW()),#{elmisCode})")
    Integer insertDataSet(ELMISInterfaceDataSet dataSet);

    @Delete("DELETE from interface_dataset where id = #{id}")
    void deleteDataset(ELMISInterfaceDataSet previous);

    @Select("select * from interface_apps")
    List<ELMISInterface> getAllInterfaces();

    @Select("select * from facility_mappings")
    @Results(value = {
            @Result(property = "interfaceId", column = "interfaceid", javaType = Long.class, one = @One(select = "get"))
           })
    List<ELMISInterfaceFacilityMapping> getInterfaceFacilityMappings();

    @Select("select * from facility_mappings where facilityid = #{facilityId}")
    @Results(value = {
            @Result(property = "interfaceId", column = "interfaceid", javaType = Long.class, one = @One(select = "get"))
    })
    List<ELMISInterfaceFacilityMapping> getFacilityInterfaceMappingById(Long facilityId);

    @Select("select * from interface_apps where active = true")
    List<ELMISInterface> getAllActiveInterfaces();

    @Delete("Delete from facility_mappings where id = #{id}")
    Integer deleteFacilityMapping(ELMISInterfaceFacilityMapping previous);

    @Insert("INSERT INTO facility_mappings( " +
            "  interfaceid, facilityid, mappedid, active, createdby, createddate, modifiedby, modifieddate)\n" +
            "     VALUES (#{interfaceId.id}, #{facilityId}, #{mappedId}, #{active}, " +
            " #{modifiedBy}, COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP), #{createdBy}, COALESCE(#{createdDate}, NOW()))")
    @Options(useGeneratedKeys = true)
    Integer insertFacilityMapping(ELMISInterfaceFacilityMapping mapping);

    @Update("UPDATE facility_mappings" +
            "   SET interfaceid=#{interfaceId.id}, facilityid=#{facilityId}, mappedid= #{mappedId}, active=#{active}, createdby= #{createdBy}, \n" +
            "    createddate=COALESCE(#{createdDate}, NOW()), modifiedby=#{modifiedBy}, modifieddate=COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP)\n" +
            "   WHERE id = #{id}")
    Integer updateFacilityMapping(ELMISInterfaceFacilityMapping mapping);

    @Update("UPDATE interface_dataset\n" +
            "   SET datasetname=#{dataSetname}, interfaceid=#{interfaceId}, datasetid=#{dataSetId}, createdby=#{createdBy}, createddate=COALESCE(#{createdDate}, NOW())," +
            " modifiedby= #{modifiedBy}, \n" +
            "       modifieddate= COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP),elmisCode = #{elmisCode}  \n" +
            " WHERE id = #{id} ")
    Integer updateElmisInterFaceData(ELMISInterfaceFacilityMappingDTO dataSet);

     @Select("SELECT * FROM interface_dataset where dataSetId= #{dataSetId}")
    ELMISInterfaceFacilityMappingDTO getByDataset(@Param("dataSetId") String dataSetId);


    @Insert("INSERT INTO interface_dataset( interfaceid, datasetname, datasetid, " +
            "            modifiedby, modifieddate, createdby, createddate,elmisCode)" +
            "    VALUES (#{interfaceId}, #{dataSetname}, #{dataSetId}, " +
            "     #{modifiedBy}, COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP), #{createdBy}, COALESCE(#{createdDate}, NOW()),#{elmisCode})")
    Integer insertData(ELMISInterfaceFacilityMappingDTO dataSet);


    @Select("SELECT PERIOD::int, ORGUNIT, split_part(dataSetId,'.',1) dataElement,split_part(dataSetId,'.',2) categoryOptionCombo, value from (\n" +
            "SELECT extract(year from pp.startdate) ||''|| to_char(pp.startdate,'MM') period,f.code AS orgUnit,\n" +
            "(select dataSetId FROM interface_dataset s\n" +
            "JOIN INTERFACE_APPS A on s.INTERFACEID = A.ID\n" +
            "WHERE A.NAME = 'VIMS_DHIS_DATA_ELEMENTS' AND s.elmiscode= 'FE') dataSetId, sum(femaleValue) as value\n" +
            "from vaccine_reports r\n" +
            "--JOIN vaccine_report_logistics_line_items li ON li.reportId=r.id \n" +
            "--JOIN vaccine_report_coverage_line_items c ON r.id = c.reportId\n" +
            "JOIN vaccine_report_vitamin_supplementation_line_items v ON r.id = v.reportId\n" +
            "AND vitaminagegroupid = (SELECT ID FROM vaccine_vitamin_supplementation_age_groups where displayOrder=2 LIMIT 1)\n" +
            "JOIN processing_periods pp ON r.periodId = PP.ID and numberofmonths =1\n" +
            "JOIN facilities f ON f.id= r.facilityId\n" +
            "WHERE R.STATUS in('APPROVED','RELEASED') AND PERIODid = 119\n" +
            "group by pp.startdate,f.code)y\n" +
            "where value >0\n" +
            "\n" +
            "UNION ALL \n" +
            "\n" +
            "SELECT PERIOD::int, ORGUNIT, split_part(dataSetId,'.',1) dataElement,split_part(dataSetId,'.',2) categoryOptionCombo, value from (\n" +
            "\n" +
            "SELECT extract(year from pp.startdate) ||''|| to_char(pp.startdate,'MM') period,f.code AS orgUnit,\n" +
            "(select dataSetId FROM interface_dataset s\n" +
            "JOIN INTERFACE_APPS A on s.INTERFACEID = A.ID\n" +
            "WHERE A.NAME = 'VIMS_DHIS_DATA_ELEMENTS' AND s.elmiscode= 'ME') dataSetId, sum(maleValue) as value\n" +
            "from vaccine_reports r\n" +
            "--JOIN vaccine_report_logistics_line_items li ON li.reportId=r.id \n" +
            "--JOIN vaccine_report_coverage_line_items c ON r.id = c.reportId\n" +
            "JOIN vaccine_report_vitamin_supplementation_line_items v ON r.id = v.reportId\n" +
            "AND vitaminagegroupid = (SELECT ID FROM vaccine_vitamin_supplementation_age_groups where displayOrder=2 LIMIT 1)\n" +
            "JOIN processing_periods pp ON r.periodId = PP.ID and numberofmonths =1\n" +
            "JOIN facilities f ON f.id= r.facilityId\n" +
            "WHERE R.STATUS in('APPROVED','RELEASED') AND PERIODid = 119\n" +
            "group by pp.startdate,f.code) X\n" +
            "where value >0\n")
    List<ELMISInterfaceDataSetDTO>getImmunizationData();

    @Select("REFRESH MATERIALIZED VIEW  vw_bed_nets_data")
    void refreshMaterializedView();

    @Insert(" INSERT INTO public.interface_responses(\n" +
            "            responseType, status, description, imported, updated, ignored, \n" +
            "            deleted, affectedObject, affectedValue, datasetComplete)\n" +
            "    VALUES (#{responseType}, #{status}, #{description}, #{imported}, #{updated}, #{ignored}, \n" +
            "            #{deleted}, #{affectedObject}, #{affectedValue},#{dataSetComplete});\n")
    void InsertInterfaceResponse(ResponseDTO dto);

}
