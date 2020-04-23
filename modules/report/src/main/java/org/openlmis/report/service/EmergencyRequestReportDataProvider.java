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
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.report.mapper.EmergencyRequestMapper;
import org.openlmis.report.mapper.NonReportingFacilityReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.dto.NameCount;
import org.openlmis.report.model.dto.ProcessingPeriod;
import org.openlmis.report.model.params.NonReportingFacilityParam;
import org.openlmis.report.model.report.EmergencyRequistionReport;
import org.openlmis.report.model.report.NonReportingFacilityDetail;
import org.openlmis.report.model.report.ReportingMasterReport;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class EmergencyRequestReportDataProvider extends ReportDataProvider {

    private static final String REPORT_FILTER_PARAM_VALUES = "REPORT_FILTER_PARAM_VALUES";
    private static final String TOTAL_NON_REPORTING = "TOTAL_NON_REPORTING";
    private static final String TOTAL_FACILITIES = "TOTAL_FACILITIES";
    private static final String REPORTING_FACILITIES = "REPORTING_FACILITIES";
    public static final String REPORTING_STATUS = "reportingStatus";
    public static final String PERIODS_FOR_CHART = "periodsForChart";

    @Autowired
    private EmergencyRequestMapper reportMapper;

    @Autowired
    private SelectedFilterHelper filterHelper;

    List<NameCount> summary = new ArrayList<>();

    @Override
    public List<? extends ResultRow> getResultSet(Map<String, String[]> filterCriteria) {
        RowBounds rowBounds = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        NonReportingFacilityParam nonReportingFacilityParam = getFilterParameters(filterCriteria);
        List<EmergencyRequistionReport> requistionReports=reportMapper.getEmergencyRequisitions(nonReportingFacilityParam, rowBounds, this.getUserId());
        return requistionReports;
    }



    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        RowBounds rowBounds = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        NonReportingFacilityParam nonReportingFacilityParam = getFilterParameters(filterCriteria);
        List<EmergencyRequistionReport> requistionReports=reportMapper.getEmergencyRequisitions(nonReportingFacilityParam, rowBounds, this.getUserId());
        return requistionReports;
    }

    private NonReportingFacilityParam getFilterParameters(Map params) {
        return ParameterAdaptor.parse(params, NonReportingFacilityParam.class);
    }

    @Override
    public String getFilterSummary(Map<String, String[]> params) {
        return filterHelper.getProgramPeriodGeoZone(params);
    }


}
