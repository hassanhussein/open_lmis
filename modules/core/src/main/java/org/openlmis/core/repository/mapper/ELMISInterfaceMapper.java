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
import org.openlmis.core.dto.*;
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
            "       modifieddate= COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP) \n" +
            " WHERE id = #{id} ")
    Integer updateDataSet(ELMISInterfaceDataSet dataSet);

    @Insert("INSERT INTO interface_dataset(" +
            "             interfaceid, datasetname, datasetid, " +
            "            modifiedby, modifieddate, createdby, createddate)" +
            "    VALUES (#{interfaceId}, #{dataSetname}, #{dataSetId}, " +
            "     #{modifiedBy}, COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP), #{createdBy}, COALESCE(#{createdDate}, NOW()))")
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


    @Select("SELECT * FROM vw_bed_nets_data WHERE reporting_year >= 2017")
    List<ELMISInterfaceDataSetDTO>getMosquitoNetData();

    @Select("REFRESH MATERIALIZED VIEW  vw_bed_nets_data")
    void refreshMaterializedView();

    @Insert(" INSERT INTO public.interface_responses(\n" +
            "            responseType, status, description, imported, updated, ignored, \n" +
            "            deleted, affectedObject, affectedValue, datasetComplete)\n" +
            "    VALUES (#{responseType}, #{status}, #{description}, #{imported}, #{updated}, #{ignored}, \n" +
            "            #{deleted}, #{affectedObject}, #{affectedValue},#{dataSetComplete});\n")
    void InsertInterfaceResponse(ResponseDTO dto);

    @Select(" SELECT * FROM (\n" +
            "      WITH Q as(\n" +
            "    select gzz.id, gzz.name,app.datasetid,periodId, COALESCE(expected.count) expected,  COALESCE(period.count,0) as reported   \n" +
            "     from   \n" +
            "     geographic_zones gzz  \n" +
            "    LEFT JOIN (\n" +
            "      SELECT  datasetName,datasetid FROM public.interface_apps a\n" +
            "      JOIN interface_dataset s on s.interfaceid = a.id \n" +
            "      WHERE a.name = 'ELMIS_DHIS2_DISTRICT_MAPPING'\n" +
            "\n" +
            "    ) APP  ON GZz.CODE = APP.datasetName\n" +
            "     \n" +
            "     left join  \n" +
            "     (select geographicZoneId,pp.id periodId, count(*) from facilities   \n" +
            "     join programs_supported ps on ps.facilityId = facilities.id  \n" +
            "     join geographic_zones gz on gz.id = facilities.geographicZoneId  \n" +
            "     join requisition_group_members rgm on rgm.facilityId = facilities.id  \n" +
            "     join requisition_group_program_schedules rgps on rgps.requisitionGroupId = rgm.requisitionGroupId and rgps.programId = ps.programId   \n" +
            "     join processing_periods pp on pp.scheduleId = rgps.scheduleId and pp.id = 90  \n" +
            "     where gz.levelId = (select max(id) from geographic_levels) and ps.programId = 1 \n" +
            "     group by geographicZoneId ,pp.id\n" +
            "     ) expected  \n" +
            "     on gzz.id = expected.geographicZoneId  \n" +
            "     left join  \n" +
            "     (select geographicZoneId, count(*) from facilities   \n" +
            "     join programs_supported ps on ps.facilityId = facilities.id  \n" +
            "     join geographic_zones gz on gz.id = facilities.geographicZoneId  \n" +
            "     where  ps.programId = 1 and facilities.id in   \n" +
            "     (select facilityId from requisitions where periodId = 90 and programId = 1 and status not in ('INITIATED', 'SUBMITTED', 'SKIPPED') and emergency = false )  \n" +
            "     group by geographicZoneId \n" +
            "     ) period \n" +
            "     on gzz.id = period.geographicZoneId order by gzz.name\n" +
            "     )\n" +
            "\n" +
            "     SELECT datasetId ORGUNIT,\n" +
            "     (date_part('YEAR'::text, pp.startdate) || ''::text) || to_char(pp.enddate, 'MM'::text) AS period,COALESCE(q.expected, 0) AS value,\n" +
            "      'LMISRREXPCT'::text AS dataelement FROM Q\n" +
            "    JOIN processing_periods pp ON Q.periodId = PP.ID\n" +
            "     where expected is not null\n" +
            "\n" +
            "     UNION ALL \n" +
            "\n" +
            "  SELECT datasetId ORGUNIT, \n" +
            "     (date_part('YEAR'::text, pp.startdate) || ''::text) || to_char(pp.enddate, 'MM'::text) AS period,COALESCE(q.reported, 0) AS value,\n" +
            "    'LMISRRSUBM'::text AS dataelement FROM Q\n" +
            "    JOIN processing_periods pp ON Q.periodId = PP.ID\n" +
            "     where expected is not null\n" +
            "     ) Y\n")
    List<ELMISInterfaceDataSetDTO>getMosquitoNetReportingRateData();

     @Select("select * from  elmis_response_messages where code = #{code}")
     ELMISResponseMessageDTO getResponseMessageByCode(@Param("code") String code);



    @Insert("INSERT INTO public.facility_mappings(\n" +
            "            interfaceid, facilityId, mappedId, active, createdby, createddate, \n" +
            "            modifiedby, modifieddate)\n" +
            "    VALUES (#{interfaceId}, #{facilityId}, #{mappedId}, #{active}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, NOW());")
    @Options(useGeneratedKeys = true)
    Integer insertFacility(FacilityMappingDTO mapping);

    @Update(" UPDATE public.facility_mappings\n" +
            "   SET  interfaceId=#{interfaceId}, facilityId=#{facilityId}, mappedId= #{mappedId}, active=#{active}, \n" +
            "       modifiedBy=#{modifiedBy}, modifiedDate=NOW()\n" +
            " WHERE id = #{id};\n ")
    void updateFacility(FacilityMappingDTO mapping);

    @Select(" SELECT * FROM facility_mappings where  mappedId= #{mappedId} ")
    FacilityMappingDTO getByMappedId(@Param("mappedId") String mappedId);

    @Select("select * from interface_apps where name = #{name}")
    ELMISInterface getByName(@Param("name") String name);
}
