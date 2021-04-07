/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.report.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.serializer.DateDeserializer;
import org.openlmis.upload.annotation.ImportField;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentInventory extends BaseDtoModel {

    private Long facilityId;
    private Long programId;
    private Long equipmentId;


    private Equipment equipment;


    private Facility facility;

    private Long operationalStatusId;
    private Long notFunctionalStatusId;


    private String serialNumber;


    private Integer yearOfInstallation;


    private Float purchasePrice;


    private String sourceOfFund;


    private Boolean replacementRecommended;


    private String reasonForReplacement;


    private String nameOfAssessor;

    private Long primaryDonorId;


    private Boolean isActive;


    @JsonDeserialize(using = DateDeserializer.class)
    private Date dateDecommissioned;


    @JsonDeserialize(using = DateDeserializer.class)
    private Date dateLastAssessed;

    private Boolean hasStabilizer;

    private String nameOfSparePart;
    private Long equipmentInventoryId;
    private Long isObsolete;


    private String remark;


    private Long equipmentModelId;


    private Long equipmentOperationalStatusId;


}
