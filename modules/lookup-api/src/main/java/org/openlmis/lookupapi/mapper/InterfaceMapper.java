package org.openlmis.lookupapi.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.core.dto.ELMISInterfaceDataSetDTO;
import org.openlmis.core.dto.ZoneDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceMapper {

 @Select("\n" +
         "SELECT hfrcode as \"orgUnit\", period, datasetid as \"dataElement\", \n" +
         "SUM(overstock)+SUM(SP)+sum(unknown)+ sum(understock)+SUM(SO) as \"value\"\n" +
         "FROM (\n" +
         "select \n" +
         "hfrcode ,\n" +
         "\n" +
         "(date_part('YEAR'::text, pp.startdate) || ''::text) || to_char(pp.enddate, 'MM'::text) AS period,\n" +
         "se.datasetid ,\n" +
         "\n" +
         "\n" +
         "CASE WHEN Status ='SP' then 1 else 0 END as SP,\n" +
         "CASE WHEN Status ='UK' then 1 ELSE 0 END as unknown,\n" +
         "CASE WHEN Status ='US' then 1 else 0 end as understock,\n" +
         "\n" +
         "CASE WHEN Status ='OS' then 1 ELSE 0 end as overstock,\n" +
         "CASE WHEN Status ='SO' then 1 ELSE 0 end as SO \n" +
         "\n" +
         "from mv_stock_imbalance_by_facility_report r \n" +
         "JOIN interface_dataset se ON r.productcode::text = se.datasetname::text and interfaceid = \n" +
         "(select id from interface_apps where name = 'NumberofExpected' )\n" +
         "JOIN processing_periods pp ON r.periodid = pp.id\n" +
         "JOIN facilities f ON r.facility_id = F.id\n" +
         " where programID = 1 \n" +
         "\n" +
         " and EMERGENCY = FALSE  AND hfrcode IS NOT NULL and hfrcode not in('-', '.') and zone_id =#{zoneId}\n" +
         " AND r.periodId=#{id}::INT\n" +
         " AND STATUS NOT IN ('') \n" +
         " )l\n" +
         "GROUP BY hfrcode, period, l.datasetid\n" +
         "order by period,hfrcode ")
 List<ELMISInterfaceDataSetDTO> getNumberOfExpected(@Param("id") Long id, @Param("zoneId") Long zoneId);

 @Select("SELECT hfrcode as \"orgUnit\", period, datasetid as \"dataElement\", \n" +
         "\n" +
         "\n" +
         "\n" +
         "SUM(overstock)+SUM(SP)+ sum(understock) as \"value\"\n" +
         "\n" +
         "\n" +
         "FROM (\n" +
         "select \n" +
         "hfrcode ,\n" +
         "\n" +
         "(date_part('YEAR'::text, pp.startdate) || ''::text) || to_char(pp.enddate, 'MM'::text) AS period,\n" +
         "se.datasetid ,\n" +
         "\n" +
         "\n" +
         "CASE WHEN Status ='SP' then 1 else 0 END as SP,\n" +
         "CASE WHEN Status ='UK' then 1 ELSE 0 END as unknown,\n" +
         "CASE WHEN Status ='US' then 1 else 0 end as understock,\n" +
         "\n" +
         "CASE WHEN Status ='OS' then 1 ELSE 0 end as overstock,\n" +
         "CASE WHEN Status ='SO' then 1 ELSE 0 end as SO \n" +
         "\n" +
         "from mv_stock_imbalance_by_facility_report r \n" +
         "JOIN interface_dataset se ON r.productcode::text = se.datasetname::text and interfaceid = 7\n" +
         "JOIN processing_periods pp ON r.periodid = pp.id\n" +
         "JOIN facilities f ON r.facility_id = F.id\n" +
         "where programID = 1::int and year = 2021::int and EMERGENCY = FALSE  " +
         " and EMERGENCY = FALSE  AND hfrcode IS NOT NULL and hfrcode not in('-', '.') and\n" +
         "periodId = #{periodId}\n" +
         "and ZONE_ID=#{zoneId}\n" +
         "AND STATUS NOT IN ('') \n" +
         "\n" +
         ")l\n" +
         "GROUP BY hfrcode, period, l.datasetid\n" +
         "order by period,hfrcode")
 List<ELMISInterfaceDataSetDTO> getAvailability(@Param("periodId") Long id, @Param("zoneId") Long zoneId);

 @Select("select hfrcode as \"orgUnit\",\n" +
         "            (date_part('YEAR'::text, pp.startdate) || ''::text) || to_char(pp.enddate, 'MM'::text) AS period,\n" +
         "se.datasetid  \"dataElement\",\n" +
         "sum(stockinhand) as \"value\"\n" +
         "from requisitions r\n" +
         "JOIN requisition_line_items item ON r.id = item.rnrid\n" +
         "JOIN facilities f ON r.facilityId = F.id\n" +
         "JOIN vw_districts d on f.geographiczoneid = d.district_id\n" +
         "JOIN processing_periods pp ON r.periodid = pp.id\n" +
         "JOIN programs pr ON r.programId = PR.ID\n" +
         "JOIN interface_dataset se ON item.productcode::text = se.datasetname::text and interfaceid = 1\n" +
         "where\n" +
         " pr.id= 1 and extract(year from pp.startdate) = 2021 and pp.id = #{periodId} and scheduleid=#{scheduleId} and d.zone_id=#{zoneId}\n" +
         " and hfrcode is not null and hfrcode not in('-', '.') and stockinhand > 0\n" +
         "group by\n" +
         " hfrcode, pp.name, productcode,pp.startdate,pp.enddate,datasetid\n" +
         "order by  hfrcode, pp.name")
 List<ELMISInterfaceDataSetDTO> getMonthly(@Param("periodId") Long id, @Param("scheduleId") Long schedule,
                                                @Param("zoneId") Long zoneId);

 @Select("select hfrcode as \"orgUnit\",\n" +
         "            (date_part('YEAR'::text, pp.startdate) || ''::text) || to_char(pp.enddate, 'MM'::text) AS period,\n" +
         "se.datasetid  \"dataElement\",\n" +
         "sum(quantityapproved) as \"value\"\n" +
         "from requisitions r\n" +
         "JOIN requisition_line_items item ON r.id = item.rnrid\n" +
         "JOIN facilities f ON r.facilityId = F.id\n" +
         "JOIN vw_districts d on f.geographiczoneid = d.district_id\n" +
         "JOIN processing_periods pp ON r.periodid = pp.id\n" +
         "JOIN programs pr ON r.programId = PR.ID\n" +
         "JOIN interface_dataset se ON item.productcode::text = se.datasetname::text and interfaceid = 3\n" +
         "where\n" +
         " pr.id= 1 and extract(year from pp.startdate) = 2021 and \n" +
         " pp.id =#{periodId} AND pp.scheduleId = #{scheduleId} " +
         "and d.zone_id=#{zoneId}\n" +
         " and hfrcode is not null and hfrcode not in('-', '.') and quantityapproved > 0\n" +
         "group by\n" +
         " hfrcode, pp.name, productcode,pp.startdate,pp.enddate,datasetid\n" +
         "order by  hfrcode, pp.name")
 List<ELMISInterfaceDataSetDTO> getOrdered(@Param("periodId") Long id, @Param("scheduleId") Long schedule,
                                                @Param("zoneId") Long zoneId);

 @Select(" select hfrcode as \"orgUnit\",\n" +
         "            (date_part('YEAR'::text, pp.startdate) || ''::text) || to_char(pp.enddate, 'MM'::text) AS period,\n" +
         "se.datasetid  \"dataElement\",\n" +
         "sum(quantityreceived) as \"value\"\n" +
         "from requisitions r\n" +
         "JOIN requisition_line_items item ON r.id = item.rnrid\n" +
         "JOIN facilities f ON r.facilityId = F.id\n" +
         "JOIN vw_districts d on f.geographiczoneid = d.district_id\n" +
         "JOIN processing_periods pp ON r.periodid = pp.id\n" +
         "JOIN programs pr ON r.programId = PR.ID\n" +
         "JOIN interface_dataset se ON item.productcode::text = se.datasetname::text and interfaceid = 2\n" +
         "where\n" +
         " pr.id= 1 and extract(year from pp.startdate) = 2021 and pp.id = #{periodId} and scheduleid=#{scheduleId} and d.zone_id=#{zoneId}\n" +
         " and hfrcode is not null and hfrcode not in('-', '.') AND quantityreceived > 0\n" +
         "group by\n" +
         " hfrcode, pp.name, productcode,pp.startdate,pp.enddate,datasetid\n" +
         "order by  hfrcode, pp.name ")
 List<ELMISInterfaceDataSetDTO> getQuantityReceived(@Param("periodId") Long id, @Param("scheduleId") Long schedule,
                                                @Param("zoneId") Long zoneId);

 @Select(" select distinct zone_id zoneId from vw_districts where zone_id not in(437)\n ")
 List<ZoneDto> getZones();

}
