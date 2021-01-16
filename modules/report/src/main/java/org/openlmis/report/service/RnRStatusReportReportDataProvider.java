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
import org.apache.log4j.Logger;
import org.openlmis.report.mapper.AggregateConsumptionReportMapper;
import org.openlmis.report.mapper.RequisitionStatusReportsMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.FacilityConsumptionReportParam;
import org.openlmis.report.model.report.DistrictConsumptionReport;
import org.openlmis.report.model.report.RnRDetailReport;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class RnRStatusReportReportDataProvider extends ReportDataProvider {


    @Value("${report.status.considered.accepted}")
    private String configuredAcceptedRnrStatuses;

    @Autowired
    private SelectedFilterHelper filterHelper;

    @Autowired
    private RequisitionStatusReportsMapper reportMapper;
    private static final Logger logger = Logger.getLogger(RnRStatusReportReportDataProvider.class);

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        List<RnRDetailReport> districtConsumptionReports = new ArrayList<>();
        try {
            districtConsumptionReports = reportMapper.getRequisitionList(
                    getReportFilterData(filterCriteria, (long) page, (long) pageSize), this.getUserId());
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            return districtConsumptionReports;
        }
    }

    public FacilityConsumptionReportParam getReportFilterData(Map<String, String[]> filterCriteria, Long page, Long pagSize) {
        FacilityConsumptionReportParam param = ParameterAdaptor.parse(filterCriteria, FacilityConsumptionReportParam.class);
        param.setAcceptedRnrStatuses(configuredAcceptedRnrStatuses);
        return param;
    }

    @Override
    public int getReportTotalCount(Map<String, String[]> filterCriteria) {

        return reportMapper.getRequisitionListCount(getReportFilterData(filterCriteria, 0l, 0l), this.getUserId());
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {

        Map<String, String[]> modifiableParams = new HashMap<String, String[]>();
        modifiableParams.putAll(params);
        modifiableParams.put("userId", new String[]{String.valueOf(this.getUserId())});
        return filterHelper.getProgramPeriodGeoZone(modifiableParams);
    }

}
