
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

import org.openlmis.report.mapper.FacilityConsumptionReportMapper;
import org.openlmis.report.mapper.lookup.ProcessingPeriodReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.dto.ProcessingPeriod;
import org.openlmis.report.model.params.FacilityConsumptionReportParam;
import org.openlmis.report.model.report.ConsumptionColumn;
import org.openlmis.report.model.report.FacilityConsumptionRow;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by takepc on 5/26/2019.
 */
public abstract class ConsumptionReportDataProvider extends ReportDataProvider {
    @Value("${report.status.considered.accepted}")
    protected String configuredAcceptedRnrStatuses;

    @Autowired
    protected SelectedFilterHelper filterHelper;

    @Autowired
    protected FacilityConsumptionReportMapper reportMapper;
    protected List<ProcessingPeriod> periods;
    protected List<ConsumptionColumn> flatConsumptionList;
    @Autowired
    ProcessingPeriodReportMapper processingPeriodReportMapper;

    public List<FacilityConsumptionRow> convertToFacilityConsumptionRow(List<Map<String, Object>> consumptionHashMapList) {
        List<FacilityConsumptionRow> facilityConsumptionRows = null;
        flatConsumptionList = new ArrayList<>();
        if (consumptionHashMapList != null && !consumptionHashMapList.isEmpty()) {
            facilityConsumptionRows = new ArrayList<>();
            for (Map<String, Object> objectHashMap : consumptionHashMapList) {
                FacilityConsumptionRow consumptionRow = new FacilityConsumptionRow();
                Long facilityId = objectHashMap.get("facilityid") != null ? Long.parseLong(String.valueOf(objectHashMap.get("facilityid"))) : null;
                consumptionRow.setHeaderPeriods(this.periods);
                consumptionRow.setProductCode(String.valueOf(objectHashMap.get("productcode")));
                consumptionRow.setProduct(String.valueOf(objectHashMap.get("product")));
                consumptionRow.setFacilityId(facilityId);
                consumptionRow.setType(String.valueOf(objectHashMap.get("facilitytype")));
                consumptionRow.setFacility(String.valueOf(objectHashMap.get("facility")));
                consumptionRow.setFacilityCode(String.valueOf(objectHashMap.get("facilitycode")));
                consumptionRow.setFacProdCode(String.valueOf(objectHashMap.get("facprodcode")));
                List<ConsumptionColumn> consumptionColumns = new ArrayList<>();
                for (ProcessingPeriod period : this.periods) {
                    ConsumptionColumn consumptionColumn = new ConsumptionColumn();
                    consumptionColumn.setProductCode(String.valueOf(objectHashMap.get("productcode")));
                    consumptionColumn.setProduct(String.valueOf(objectHashMap.get("product")));
                    consumptionColumn.setFacilityId(facilityId);
                    consumptionColumn.setType(String.valueOf(objectHashMap.get("facilitytype")));
                    consumptionColumn.setFacility(String.valueOf(objectHashMap.get("facility")));
                    consumptionColumn.setFacilityCode(String.valueOf(objectHashMap.get("facilitycode")));
                    consumptionColumn.setFacProdCode(String.valueOf(objectHashMap.get("facprodcode")));
                    consumptionColumn.setHeader(period.getName());
                    Object objectValue = objectHashMap.get(period.getName());
                    if (objectValue != null) {
                        consumptionColumn.setValeu(objectValue);
                    } else {
                        consumptionColumn.setValeu(0L);
                    }
                    consumptionColumns.add(consumptionColumn);
                }
                flatConsumptionList.addAll(consumptionColumns);
                consumptionRow.setConsumptionColumnList(consumptionColumns);
                facilityConsumptionRows.add(consumptionRow);

            }
        }
        return facilityConsumptionRows;
    }

    public String constructTabColumnHeader(Long programId, String startDate, String endDate) {
        String tableColumnHeader = "";
        List<ProcessingPeriod> periods = processingPeriodReportMapper.getPeriodsForProgramBeetweeDates(programId, startDate, endDate);
        this.periods = periods;
        if (periods != null && !periods.isEmpty()) {
            for (ProcessingPeriod period : periods) {
                tableColumnHeader = tableColumnHeader + ", \"" + period.getName() + "\"";
            }


        } else {
            return null;
        }
        return tableColumnHeader;

    }

    public String constructCrossTabColumnSection(Long programId, String startDate, String endDate) {
        String crossTabColumn = " ( code text[]";

        List<ProcessingPeriod> periods = processingPeriodReportMapper.getPeriodsForProgramBeetweeDates(programId, startDate, endDate);
        this.periods = periods;
        if (periods != null && !periods.isEmpty()) {
            for (ProcessingPeriod period : periods) {
                crossTabColumn = crossTabColumn + ", \"" + period.getName() + "\" integer";
            }
            crossTabColumn = crossTabColumn + ")";

        } else {
            return null;
        }
        return crossTabColumn;

    }

    @Override
    public int getReportTotalCount(Map<String, String[]> param) {

        FacilityConsumptionReportParam filter = this.getReportFilterData(param);
        int totalcount = reportMapper.getAggregateConsumptionReportTotalCount(filter, this.getUserId());
        return totalcount;

    }

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        FacilityConsumptionReportParam reportParam = getReportFilterData(filterCriteria);
        List<Map<String, Object>> objectList = reportMapper.getAggregateConsumptionReport(reportParam, this.getUserId());
        List<FacilityConsumptionRow> facilityConsumptionRowList = this.convertToFacilityConsumptionRow(objectList);
        return flatConsumptionList;
    }

    @Override
    public List<? extends ResultRow> getReportHtmlBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        FacilityConsumptionReportParam reportParam = getReportFilterData(filterCriteria);
        List<Map<String, Object>> objectList = reportMapper.getAggregateConsumptionReport(reportParam, this.getUserId());
        List<FacilityConsumptionRow> facilityConsumptionRowList = this.convertToFacilityConsumptionRow(objectList);
        return facilityConsumptionRowList;
    }

    public FacilityConsumptionReportParam getReportFilterData(Map<String, String[]> filterCriteria) {
        FacilityConsumptionReportParam param = ParameterAdaptor.parse(filterCriteria, FacilityConsumptionReportParam.class);
        String crossTabColumn = this.constructCrossTabColumnSection(param.getProgram(), param.getPeriodStart(), param.getPeriodEnd());
        String crossColumnHeader = this.constructTabColumnHeader(param.getProgram(), param.getPeriodStart(), param.getPeriodEnd());
        param.setCrossTabColumn(crossTabColumn);
        param.setCrossColumnHeader(crossColumnHeader);
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
}
