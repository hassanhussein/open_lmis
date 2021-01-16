/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.builder.DailyConsumptionQueryBuilder;
import org.openlmis.report.builder.RequistionStatusReportsBuilder;
import org.openlmis.report.model.ReportParameter;
import org.openlmis.report.model.dto.RequisitionDTO;
import org.openlmis.report.model.report.RnRDetailReport;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Repository
public interface RequisitionStatusReportsMapper {

    @SelectProvider(type = RequistionStatusReportsBuilder.class, method = "getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
    @Results(value = {
            @Result(property = "rnrid", column = "rnrid"),
            @Result(property = "staus", column = "staus"),
            @Result(property = "emergency", column = "emergency"),
            @Result(property = "zoneid", column = "zoneid"),
            @Result(property = "district", column = "district"),
            @Result(property = "districtid", column = "districtid"),
            @Result(property = "provinceid", column = "provinceid"),
            @Result(property = "province", column = "province"),
            @Result(property = "facility", column = "facility"),
            @Result(property = "facilityid", column = "facilityid"),
            @Result(property = "facilitycode", column = "facilitycode"),
            @Result(property = "feconfigured", column = "feconfigured"),
            @Result(property = "facilitytypeid", column = "facilitytypeid"),
            @Result(property = "facilitytype", column = "facilitytype"),
            @Result(property = "program", column = "program"),
            @Result(property = "programid", column = "programid"),
            @Result(property = "perioidid", column = "perioidid"),
            @Result(property = "period", column = "period"),
            @Result(property = "startdate", column = "startdate"),
            @Result(property = "enddate", column = "enddate"),
            @Result(property = "categoryid", column = "categoryid"),
            @Result(property = "productcategory", column = "productcategory")})
    List<RnRDetailReport> getRequisitionList(@Param("filterCriteria") ReportParameter filterCriteria,
                                             @Param("userId") Long userId);
    @SelectProvider(type = RequistionStatusReportsBuilder.class, method = "getRequisitionListCountQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)

    int getRequisitionListCount(@Param("filterCriteria") ReportParameter filterCriteria,
                                             @Param("userId") Long userId);

}

