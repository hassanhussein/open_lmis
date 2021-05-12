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

package org.openlmis.report.service;

import javafx.util.Pair;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.User;
import org.openlmis.report.mapper.AggregateConsumptionReportMapper;
import org.openlmis.report.mapper.UserPermissionMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.AggregateConsumptionReportParam;
import org.openlmis.report.model.report.DistrictConsumptionReport;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.ReportPaginationHelper;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;

@Component
@NoArgsConstructor
public class AggregateConsumptionReportDataProvider extends ReportDataProvider {

  @Value("${report.status.considered.accepted}")
  private String configuredAcceptedRnrStatuses;

  @Autowired
  private SelectedFilterHelper filterHelper;

  @Autowired
  private AggregateConsumptionReportMapper reportMapper;

  @Autowired
  private ReportPaginationHelper paginationHelper;

  @Autowired
  private UserPermissionMapper userPermissionMapper;

  Boolean canViewNationalReport = false;

  @Override
  public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
    paginationHelper.setPageSize(pageSize + "");
    List<User> userPermission = userPermissionMapper.getPermissionToViewNationalReport(this.getUserId());

    if (userPermission.isEmpty()){
      canViewNationalReport = false;
    } else {
      canViewNationalReport = true;
    }
    return reportMapper.getAggregateConsumptionReport(getReportFilterData(filterCriteria), sortCriteria, paginationHelper.getPagination(page), this.getUserId(),
            canViewNationalReport);
  }

  public AggregateConsumptionReportParam getReportFilterData(Map<String, String[]> filterCriteria) {
    AggregateConsumptionReportParam param = ParameterAdaptor.parse(filterCriteria, AggregateConsumptionReportParam.class);
    param.setAcceptedRnrStatuses(configuredAcceptedRnrStatuses);
    return param;
  }

  @Override
  public String getFilterSummary(Map<String, String[]> params) {

    Map<String, String[]> modifiableParams = new HashMap<String, String[]>();
    modifiableParams.putAll(params);
    modifiableParams.put("userId", new String[]{String.valueOf(this.getUserId())});
    return filterHelper.getProgramPeriodGeoZone(modifiableParams);
  }


  public List<? extends ResultRow> downloadReport(Map<String, String[]> filterCriteria,
                                                Map<String, String[]> sortCriteria) {
    List<User> userPermission = userPermissionMapper.getPermissionToViewNationalReport(this.getUserId());

    if (userPermission.isEmpty()){
      canViewNationalReport = false;
    } else {
      canViewNationalReport = true;
    }
    return reportMapper.downloadAggregateConsumptionReport(getReportFilterData(filterCriteria),
            sortCriteria,  this.getUserId(),
            canViewNationalReport);
  }

  public ByteArrayInputStream getCSV(List<DistrictConsumptionReport> districtConsumptionReportList) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL)
            .withHeader("REPORT");

    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format.withHeader("Line",
              "Product Code", "Product Name", "Reported Consumption", "Consumption(in Packs)", "Adjusted Consumption"));

      int position = 1;
      for (DistrictConsumptionReport line : districtConsumptionReportList) {
        csvPrinter.printRecord(position, line.getCode(), line.getProduct(),
                (line.getConsumption() == null) ? 0 : line.getConsumption(),
                (line.getConsumptionInPacks() == null) ? 0 : line.getConsumptionInPacks(),
                (line.getAdjustedConsumptionInPacks() == null) ? 0 : line.getAdjustedConsumptionInPacks());
        position++;
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {

    }
    return null;

  }

}
