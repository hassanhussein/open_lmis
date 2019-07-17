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

package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.joda.time.DateTime;
import org.openlmis.rnr.dto.RequisitionDetailDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequisitionDetailMapper {

  @Select("select program_id programId, " +
      "program_code programCode, " +
      "program_name programName, " +
      "processing_schedule_id processingScheduleId, " +
      "processing_schedule_name processingScheduleName, " +
      "period_id periodId, " +
      "period_name periodName, " +
      "period_start_date periodStartDate, " +
      "period_end_date periodEndDate, " +
      "geographic_zone_id geographicZoneId, " +
      "geographic_zone_name geographicZoneName, " +
      "geographic_zone_level geographicZoneLevel, " +
      "facility_type_id facilityTypeId, " +
      "facility_type_name facilityTypeName, " +
      "facility_type_nominaleop facilityTypeNominalEop, " +
      "facility_type_nominalmaxmonth facilityTypeNominalMaxMonth, " +
      "facility_id facilityId, " +
      "facility_code facilityCode, " +
      "facility_name facilityName, " +
      "facility_sdp facilitySdp, " +
      "facility_enabled facilityEnabled, " +
      "facility_approved_product_maxmonthsofstock facilityApprovedProductMaxMonthsOfStock, " +
      "facility_approved_product_minmonthsofstock facilityApprovedProductMinMonthsOfStock, " +
      "facility_approved_product_eop facilityApprovedProductEop, " +
      "requisition_id requisitionId, " +
      "requisition_status requisitionStatus, " +
      "requisition_emergency requisitionEmergency, " +
      "product_category_id productCategoryId, " +
      "product_category_name productCategoryName, " +
      "product_group_id productGroupId, " +
      "product_id productId, " +
      "product_code productCode, " +
      "product_description productDescription, " +
      "product_dispensingunit productDispensingUnit, " +
      "product_primaryname productPrimaryName, " +
      "product_fullname productFullName, " +
      "product_tracer productTracer, " +
      "amc,  beginningbalance, " +
      "calculatedorderquantity calculatedOrderQuantity, " +
      "createddate createdDate, " +
      "fullsupply fullSupply, " +
      "line_item_id lineItemId, " +
      "maxmonthsofstock maxMonthsOfStock, " +
      "maxstockquantity maxStockQuantity, " +
      "modifieddate modifiedDate, " +
      "newpatientcount newPatientCount, " +
      "normalizedconsumption normalizedConsumption, " +
      "packsize packSize, " +
      "packstoship packsToShip, " +
      "previousstockinhand previousStockInHand, " +
      "quantityapproved quantityApproved, " +
      "quantitydispensed quantityDispensed, " +
      "quantityreceived quantityReceived, " +
      "quantityrequested quantityRequested, " +
      "skipped skipped, " +
      "stockinhand stockInHand, " +
      "stockoutdays stockOutDays, " +
      "totallossesandadjustments totalLossesAndAdjustments, " +
      "quantityordered quantityOrdered, " +
      "quantityshipped quantityShipped, " +
      "ordereddate orderedDate, " +
      "shippeddate shippedDate from vw_requisition_detail_dw where facility_id = #{facilityId} and period_id = #{periodId} " +
      " OFFSET (#{page} * #{pageSize}) LIMIT #{pageSize}")
  List<RequisitionDetailDto> getRequisitionDetailsByFacilityPeriod(@Param("facilityId") Long facilityId,
                                                                   @Param("periodId") Long periodId,
                                                                   @Param("page") Long page,
                                                                   @Param("pageSize") Long pageSize);

  @Select("select count(*) from vw_requisition_detail_dw where facility_id = #{facilityId} and period_id = #{periodId}")
  Long getRequisitionDetailsByFacilityPeriodCount(@Param("facilityId") Long facilityId,
                                                  @Param("periodId") Long periodId);

  @Select("select program_id programId, " +
      "program_code programCode, " +
      "program_name programName, " +
      "processing_schedule_id processingScheduleId, " +
      "processing_schedule_name processingScheduleName, " +
      "period_id periodId, " +
      "period_name periodName, " +
      "period_start_date periodStartDate, " +
      "period_end_date periodEndDate, " +
      "geographic_zone_id geographicZoneId, " +
      "geographic_zone_name geographicZoneName, " +
      "geographic_zone_level geographicZoneLevel, " +
      "facility_type_id facilityTypeId, " +
      "facility_type_name facilityTypeName, " +
      "facility_type_nominaleop facilityTypeNominalEop, " +
      "facility_type_nominalmaxmonth facilityTypeNominalMaxMonth, " +
      "facility_id facilityId, " +
      "facility_code facilityCode, " +
      "facility_name facilityName, " +
      "facility_sdp facilitySdp, " +
      "facility_enabled facilityEnabled, " +
      "facility_approved_product_maxmonthsofstock facilityApprovedProductMaxMonthsOfStock, " +
      "facility_approved_product_minmonthsofstock facilityApprovedProductMinMonthsOfStock, " +
      "facility_approved_product_eop facilityApprovedProductEop, " +
      "requisition_id requisitionId, " +
      "requisition_status requisitionStatus, " +
      "requisition_emergency requisitionEmergency, " +
      "product_category_id productCategoryId, " +
      "product_category_name productCategoryName, " +
      "product_group_id productGroupId, " +
      "product_id productId, " +
      "product_code productCode, " +
      "product_description productDescription, " +
      "product_dispensingunit productDispensingUnit, " +
      "product_primaryname productPrimaryName, " +
      "product_fullname productFullName, " +
      "product_tracer productTracer, " +
      "amc,  beginningbalance, " +
      "calculatedorderquantity calculatedOrderQuantity, " +
      "createddate createdDate, " +
      "fullsupply fullSupply, " +
      "line_item_id lineItemId, " +
      "maxmonthsofstock maxMonthsOfStock, " +
      "maxstockquantity maxStockQuantity, " +
      "modifieddate modifiedDate, " +
      "newpatientcount newPatientCount, " +
      "normalizedconsumption normalizedConsumption, " +
      "packsize packSize, " +
      "packstoship packsToShip, " +
      "previousstockinhand previousStockInHand, " +
      "quantityapproved quantityApproved, " +
      "quantitydispensed quantityDispensed, " +
      "quantityreceived quantityReceived, " +
      "quantityrequested quantityRequested, " +
      "skipped skipped, " +
      "stockinhand stockInHand, " +
      "stockoutdays stockOutDays, " +
      "totallossesandadjustments totalLossesAndAdjustments, " +
      "quantityordered quantityOrdered, " +
      "quantityshipped quantityShipped, " +
      "ordereddate orderedDate, " +
      "shippeddate shippedDate from vw_requisition_detail_dw where period_id = #{periodId} " +
      "OFFSET (#{page} * #{pageSize}) LIMIT #{pageSize}")
  List<RequisitionDetailDto> getRequisitionDetailsByPeriod(@Param("periodId") Long periodId,
                                                           @Param("page") Long page,
                                                           @Param("pageSize") Long pageSize);

  @Select("select count(*) from vw_requisition_detail_dw where period_id = #{periodId}")
  Long getRequisitionDetailsByPeriodCount(@Param("periodId") Long periodId);
}





