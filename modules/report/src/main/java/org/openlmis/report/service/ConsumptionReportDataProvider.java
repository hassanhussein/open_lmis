
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

import org.apache.log4j.Logger;
import org.openlmis.report.mapper.FacilityConsumptionReportMapper;
import org.openlmis.report.mapper.lookup.ProcessingPeriodReportMapper;
import org.openlmis.report.model.ResultRow;
import org.openlmis.report.model.params.FacilityConsumptionReportParam;
import org.openlmis.report.model.report.ConsumptionColumn;
import org.openlmis.report.model.report.FacilityConsumptionRow;
import org.openlmis.report.util.ParameterAdaptor;
import org.openlmis.report.util.SelectedFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public abstract class ConsumptionReportDataProvider extends ReportDataProvider {
    @Value("${report.status.considered.accepted}")
    protected String configuredAcceptedRnrStatuses;

    @Autowired
    protected SelectedFilterHelper filterHelper;

    @Autowired
    protected FacilityConsumptionReportMapper reportMapper;

    protected List<ConsumptionColumn> flatConsumptionList;
    @Autowired
    ProcessingPeriodReportMapper processingPeriodReportMapper;
    private static final Logger logger = Logger.getLogger(ConsumptionReportDataProvider.class);
    private List<String> periodList;

    public List<FacilityConsumptionRow> convertToFacilityConsumptionRow(List<Map<String, Object>> consumptionHashMapList) {
        List<FacilityConsumptionRow> facilityConsumptionRows = null;
        flatConsumptionList = new ArrayList<>();
        if (consumptionHashMapList != null && !consumptionHashMapList.isEmpty()) {
            facilityConsumptionRows = new ArrayList<>();
            for (Map<String, Object> objectHashMap : consumptionHashMapList) {
                FacilityConsumptionRow consumptionRow = new FacilityConsumptionRow();
                Long facilityId = objectHashMap.get("facilityid") != null ? Long.parseLong(String.valueOf(objectHashMap.get("facilityid"))) : null;
                Float amc = objectHashMap.get("amc") != null ? Float.parseFloat(String.valueOf(objectHashMap.get("amc"))) : null;
                consumptionRow.setHeaderPeriods(this.periodList);
                consumptionRow.setProductCode(String.valueOf(objectHashMap.get("productcode")));
                consumptionRow.setProduct(String.valueOf(objectHashMap.get("product")));
                consumptionRow.setFacilityId(facilityId);
                consumptionRow.setType(String.valueOf(objectHashMap.get("facilitytype")));
                consumptionRow.setFacility(String.valueOf(objectHashMap.get("facility")));
                consumptionRow.setFacilityCode(String.valueOf(objectHashMap.get("facilitycode")));
                consumptionRow.setFacProdCode(String.valueOf(objectHashMap.get("facprodcode")));
                consumptionRow.setAmc(amc);
                consumptionRow.setFlagcolor(String.valueOf(objectHashMap.get("flagcolor")));
                List<ConsumptionColumn> consumptionColumns = new ArrayList<>();
                for (String period : this.periodList) {
                    ConsumptionColumn consumptionColumn = new ConsumptionColumn();
                    consumptionColumn.setProductCode(String.valueOf(objectHashMap.get("productcode")));
                    consumptionColumn.setProduct(String.valueOf(objectHashMap.get("product")));
                    consumptionColumn.setFacilityId(facilityId);
                    consumptionColumn.setType(String.valueOf(objectHashMap.get("facilitytype")));
                    consumptionColumn.setFacility(String.valueOf(objectHashMap.get("facility")));
                    consumptionColumn.setFacilityCode(String.valueOf(objectHashMap.get("facilitycode")));
                    consumptionColumn.setFacProdCode(String.valueOf(objectHashMap.get("facprodcode")));
                    consumptionColumn.setAmc(amc);
                    consumptionColumn.setFlagcolor(String.valueOf(objectHashMap.get("flagcolor")));
                    consumptionColumn.setHeader(period);
                    Object objectValue = objectHashMap.get(period);
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

    public String constructTabColumnHeader() {
        String tableColumnHeader = "";

        if (this.periodList != null && !this.periodList.isEmpty()) {
            for (String period : periodList) {
                tableColumnHeader = tableColumnHeader + ", \"" + period + "\"";
            }
        } else {
            return null;
        }
        return tableColumnHeader;
    }

    public String constructCrossTabColumnSection() {
        String crossTabColumn = " ( code text[],val float4[]";

        if (this.periodList != null && !periodList.isEmpty()) {
            for (String period : periodList) {
                crossTabColumn = crossTabColumn + ", \"" + period + "\" integer";
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
        int totalcount = 0;
        try {
            totalcount = reportMapper.getAggregateConsumptionReportTotalCount(filter, this.getUserId());
        } catch (Exception ex) {
            logger.error(ex);
        }
        return totalcount;

    }

    @Override
    public List<? extends ResultRow> getReportBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        FacilityConsumptionReportParam reportParam = getReportFilterData(filterCriteria);
        try {
            List<Map<String, Object>> objectList = reportMapper.getAggregateConsumptionReport(reportParam, this.getUserId());
            this.convertToFacilityConsumptionRow(objectList);
        } catch (Exception ex) {
            logger.error(ex);
        }
        return flatConsumptionList;
    }

    @Override
    public List<? extends ResultRow> getReportHtmlBody(Map<String, String[]> filterCriteria, Map<String, String[]> sortCriteria, int page, int pageSize) {
        FacilityConsumptionReportParam reportParam = getReportFilterData(filterCriteria);
        List<FacilityConsumptionRow> facilityConsumptionRowList = null;
        try {
            List<Map<String, Object>> objectList = reportMapper.getAggregateConsumptionReport(reportParam, this.getUserId());
            facilityConsumptionRowList = this.convertToFacilityConsumptionRow(objectList);
        } catch (Exception ex) {
            logger.error(ex);
        }

        return facilityConsumptionRowList;
    }

    public FacilityConsumptionReportParam getReportFilterData(Map<String, String[]> filterCriteria) {
        FacilityConsumptionReportParam param = ParameterAdaptor.parse(filterCriteria, FacilityConsumptionReportParam.class);
        param.setAcceptedRnrStatuses(configuredAcceptedRnrStatuses);
        List<Object> periodObjectList=reportMapper.getPeriods(param, this.getUserId());
        this.periodList= periodObjectList.stream().map(o->String.valueOf(o)).collect(Collectors.toList());
//        this.periodList = reportMapper.getPeriods(param, this.getUserId());
        String crossTabColumn = this.constructCrossTabColumnSection();
        String crossColumnHeader = this.constructTabColumnHeader();
        param.setCrossTabColumn(crossTabColumn);
        param.setCrossColumnHeader(crossColumnHeader);


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
