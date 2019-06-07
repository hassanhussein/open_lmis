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

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.Pagination;
import org.openlmis.report.mapper.AdjustmentSummaryReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.AdjustmentSummaryReportParam;
import org.openlmis.report.model.report.AdjustmentSummaryReport;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.ReportPaginationHelper;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class AdjustmentSummaryReportDataProvider extends ReportDataProvider {

  @Value("${report.status.considered.accepted}")
  private String configuredAcceptedRnrStatuses;

  @Autowired
  private AdjustmentSummaryReportMapper reportMapper;

  @Autowired
  private SelectedFilterHelper filterHelper;

  @Autowired
  private ReportPaginationHelper paginationHelper;

  @Override
  public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
    /*RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
    Pagination pagination = new Pagination(page,pageSize);
    pagination.setTotalRecords(reportMapper.getTotalFilteredSortedPagedAdjustmentSummaryReport(getReportFilterData(filterCriteria), sortCriteria ,this.getUserId()).getTotalRecords());
    AdjustmentSummaryReport report = new AdjustmentSummaryReport();
    report.setPagination(pagination);
    List<AdjustmentSummaryReport> all = reportMapper.getFilteredSortedPagedAdjustmentSummaryReport(getReportFilterData(filterCriteria), sortCriteria, rowBounds, this.getUserId());
    all.add(report);
    return all;
    */

    return reportMapper.getFilteredSortedPagedAdjustmentSummaryReport(getReportFilterData(filterCriteria), sortCriteria, paginationHelper.getPagination(page), this.getUserId());

  }

  private AdjustmentSummaryReportParam getReportFilterData(Map<String, String[]> filterCriteria) {
    AdjustmentSummaryReportParam param = ParameterAdaptor.parse(filterCriteria, AdjustmentSummaryReportParam.class);
    param.setAcceptedRnrStatuses(configuredAcceptedRnrStatuses);
    return param;
  }

  @Override
  public String getFilterSummary(Map<String, String[]> params) {
    return filterHelper.getProgramPeriodGeoZone(params);
  }

}
