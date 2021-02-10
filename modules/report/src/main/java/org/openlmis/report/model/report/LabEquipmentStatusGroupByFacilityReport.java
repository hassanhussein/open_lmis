package org.openlmis.report.model.report;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.report.model.ResultRow;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabEquipmentStatusGroupByFacilityReport implements ResultRow {
    public LabEquipmentStatusGroupByFacilityReport(String facilityCode) {
        this.setFacilityCode(facilityCode);
    }

    private String facilityName;
    private String equipmentName;
    private String district;
    private String model;
    private String serialNumber;
    private String operationalStatus;
    private String equipmentType;
    private String facilityCode;
    private String facilityType;
    private String zone;
    private String region;
    private String serviceContract;
    private String vendorName;
    private String contractId;
    private int equipmentsCount;
    List<LabEquipmentStatusReport> equipmentStatusReportList;

}
