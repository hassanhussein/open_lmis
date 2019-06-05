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

package org.openlmis.equipment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Program;
import org.openlmis.core.serializer.DateDeserializer;
import org.openlmis.core.utils.DateUtil;
import org.openlmis.equipment.dto.ColdChainEquipmentTemperatureStatusDTO;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentInventory extends BaseModel implements Importable{

  private Long facilityId;
  private Long programId;
  private Long equipmentId;

  @ImportField(mandatory = true, name = "Equipment Name", nested = "name")
  private Equipment equipment;

  @ImportField(mandatory = true, name = "Facility Code", nested = "code")
  private Facility facility;

  private Long operationalStatusId;
  private Long notFunctionalStatusId;

  @ImportField(name = "Serial Number")
  private String serialNumber;

  @ImportField(name = "Year Of Installation")
  private Integer yearOfInstallation;

    @ImportField(name = "Purchase Price")
  private Float purchasePrice;

  @ImportField(name = "Source of Fund")
  private String sourceOfFund;

  @ImportField( name = "Replacement Recommended", type = "boolean")
  private Boolean replacementRecommended;

  @ImportField( name = "Reason For Replacement")
  private String reasonForReplacement;

  @ImportField(name = "Name of Assessor")
  private String nameOfAssessor;

  private Long primaryDonorId;

  @ImportField(name = "Active", type = "boolean")
  private Boolean isActive;

  @ImportField(name = "Date Decommissioned", type = "Date")
  @JsonDeserialize(using = DateDeserializer.class)
  private Date dateDecommissioned;

  @ImportField(name = "Date Last Assessed", type = "Date")
  @JsonDeserialize(using = DateDeserializer.class)
  private Date dateLastAssessed;

  private Boolean hasStabilizer;
  @ImportField(name = "Name of SparePart")
  private String nameOfSparePart;
  private Long equipmentInventoryId;
  private Long isObsolete;

  @ImportField(name = "Remark")
  private String remark;

  @ImportField(mandatory = true, name = "Program Code", nested = "code")
  private Program program;

  @ImportField(mandatory = true, name = "Equipment Model", nested = "code")
  private EquipmentModel equipmentModel;

  @ImportField(mandatory = true, name = "Equipment Operational Status", nested = "code")
  private EquipmentOperationalStatus  equipmentOperationalStatus;

  public String getDateLastAssessedString() {
    return DateUtil.getFormattedDate(this.dateLastAssessed, "yyyy-MM-dd");
  }

  public String getDateDecommissionedString() {
    return DateUtil.getFormattedDate(this.dateDecommissioned, "yyyy-MM-dd");
  }

  private List<ColdChainEquipmentTemperatureStatusDTO> coldChainLineItems;
}
