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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

@Data
@NoArgsConstructor
@JsonSerialize(include = NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
public class RegimenLineItem  extends BaseDtoModel {
    private Long rnrId;

    private String code;
    private String name;
    private Integer patientsOnTreatment;
    private Integer patientsToInitiateTreatment;
    private Integer patientsStoppedTreatment;

    private Integer patientsOnTreatmentAdult;
    private Integer patientsToInitiateTreatmentAdult;
    private Integer patientsStoppedTreatmentAdult;

    private Integer patientsOnTreatmentChildren;
    private Integer patientsToInitiateTreatmentChildren;
    private Integer patientsStoppedTreatmentChildren;

    private String remarks;
    private Long categoryId;
    private Integer regimenDisplayOrder;

    private Boolean skipped = false;


}
