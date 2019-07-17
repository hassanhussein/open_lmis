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

package org.openlmis.rnr.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RequisitionDetailDto {


  Long programId;
  String programCode;
  String programName;
  Long processingScheduleId;
  String processingScheduleName;
  Long periodId;
  String periodName;
  Date periodStartDate;
  Date periodEndDate;
  Long geographicZoneId;
  String geographicZoneName;
  Long geographicZoneLevel;
  Long facilityTypeId;
  String facilityTypeName;
  Float facilityTypeNominalEop;
  Long facilityTypeNominalMaxMonth;
  Long facilityId;
  String facilityCode;
  String facilityName;
  Boolean facilitySdp;
  Boolean facilityEnabled;
  Long facilityApprovedProductMaxMonthsOfStock;
  Float facilityApprovedProductMinMonthsOfStock;
  Float facilityApprovedProductEop;
  Long requisitionId;
  String requisitionStatus;
  Boolean requisitionEmergency;
  Long productCategoryId;
  String productCategoryName;
  Long productGroupId;
  Long productId;
  String productCode;
  String productDescription;
  String productDispensingUnit;
  String productPrimaryName;
  String productFullName;
  Boolean productTracer;
  Long amc;
  Long beginningBalance;
  Long calculatedOrderQuantity;
  Date createdDate;
  Boolean fullSupply;
  Long lineItemId;
  Long maxMonthsOfStock;
  Long maxStockQuantity;
  Date modifiedDate;
  Long newPatientCount;
  Long normalizedConsumption;
  Long packSize;
  Long packsToShip;
  Long previousStockInHand;
  Long quantityApproved;
  Long quantityDispensed;
  Long quantityReceived;
  Long quantityRequested;
  Boolean skipped;
  Long stockInHand;
  Long stockOutDays;
  Long totalLossesAndAdjustments;
  Long quantityOrdered;
  Long quantityShipped;
  Date orderedDate;
  Date shippedDate;

}
