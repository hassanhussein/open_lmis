/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.builder.AggregateConsumptionQueryBuilder;
import org.openlmis.report.model.ReportParameter;
import org.openlmis.report.model.report.DistrictConsumptionReport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AggregateConsumptionReportMapper {
  /**
   *
   * @param filterCriteria
   * @param userId
   * @return
   *
   *  p.code, p.primaryName, p.dispensingUnit, p.strength, ds.code,f.Code,f.name,ft.name as facilityType, li.packsize
   *
   */
  @SelectProvider(type = AggregateConsumptionQueryBuilder.class, method = "getQuery")
  @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
  @Results({
          @Result(property = "facilityCode", column = "Code"),
          @Result(property = "facility", column = "name"),
          @Result(property = "facilityType", column = "facilityType"),
          @Result(property = "facProdCode", column = "facProdCode"),
          @Result(property = "product", column = "primaryName"),
          @Result(property = "code", column = "code"),
          @Result(property = "consumption", column = "consumption"),
          @Result(property = "consumptionrate", column = "consumptionrate"),
          @Result(property = "amc", column = "amc"),
          @Result(property = "flagcolor", column = "flagcolor"),
          @Result(property = "consumptionInPacks", column = "consumptionInPacks"),
          @Result(property = "adjustedConsumptionInPacks", column = "adjustedConsumptionInPacks"),
          @Result(property = "dispensed", column = "dispensed")
  })
  public List<DistrictConsumptionReport> getAggregateConsumptionReport(
      @Param("filterCriteria") ReportParameter filterCriteria,
      @Param("userId") Long userId
  );

  @SelectProvider(type = AggregateConsumptionQueryBuilder.class, method = "getTotalCountQuery")
  @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
  public int getAggregateConsumptionCountReport(
          @Param("filterCriteria") ReportParameter filterCriteria,
          @Param("userId") Long userId
  );

}
