/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.rnr.repository;

import org.openlmis.core.domain.*;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.repository.SourceOfFundRepository;
import org.openlmis.core.repository.helper.CommaSeparator;
import org.openlmis.core.repository.mapper.SignatureMapper;
import org.openlmis.core.repository.mapper.SourceOfFundMapper;
import org.openlmis.equipment.domain.EquipmentInventoryStatus;
import org.openlmis.equipment.repository.mapper.EquipmentInventoryStatusMapper;
import org.openlmis.equipment.service.EquipmentOperationalStatusService;
import org.openlmis.rnr.domain.*;
import org.openlmis.rnr.dto.RnrDTO;
import org.openlmis.core.dto.SourceOfFundDTO;
import org.openlmis.rnr.dto.SourceOfFundLineItemDTO;
import org.openlmis.rnr.repository.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openlmis.rnr.domain.RnrStatus.*;

/**
 * Repository class for Requisition related database operations.
 */

@Repository
public class RequisitionRepository {

  public static final String FUNCTIONAL = "FUNCTIONAL";
  @Autowired
  private RequisitionMapper requisitionMapper;

  @Autowired
  private RnrLineItemMapper rnrLineItemMapper;

  @Autowired
  private LossesAndAdjustmentsMapper lossesAndAdjustmentsMapper;

  @Autowired
  private CommentMapper commentMapper;

  @Autowired
  private CommaSeparator commaSeparator;

  @Autowired
  private RequisitionStatusChangeMapper requisitionStatusChangeMapper;

  @Autowired
  private RegimenLineItemMapper regimenLineItemMapper;

  @Autowired
  private EquipmentLineItemMapper equipmentLineItemMapper;

  @Autowired
  private EquipmentInventoryStatusMapper equipmentInventoryStatusMapper;

  @Autowired
  private PatientQuantificationLineItemMapper patientQuantificationLineItemMapper;

  @Autowired
  private SignatureMapper signatureMapper;

  @Autowired
  private ManualTestsLineItemMapper manualTestMapper;

  @Autowired
  private EquipmentOperationalStatusService operationalStatusService;

  @Autowired
  private SourceOfFundRepository sourceOfFundRepository;

  @Autowired
  private SourceOfFundLineItemMapper fundLineItemMapper;

  @Autowired
  private RejectionReasonMapper rejectionReasonMapper;

  public void insert(Rnr requisition) {
    requisition.setStatus(INITIATED);
    requisitionMapper.insert(requisition);
    insertLineItems(requisition, requisition.getFullSupplyLineItems());
    insertLineItems(requisition, requisition.getNonFullSupplyLineItems());
    insertRegimenLineItems(requisition, requisition.getRegimenLineItems());
    insertEquipmentStatus(requisition, requisition.getEquipmentLineItems());
    insertManualTestsLineItems(requisition, requisition.getManualTestLineItems());
  }

  public void insertPatientQuantificationLineItems(Rnr rnr) {
    for (PatientQuantificationLineItem patientQuantificationLineItem : rnr.getPatientQuantifications()) {
      patientQuantificationLineItem.setRnrId(rnr.getId());
      patientQuantificationLineItem.setModifiedBy(rnr.getModifiedBy());
      patientQuantificationLineItem.setCreatedBy(rnr.getCreatedBy());
      patientQuantificationLineItemMapper.insert(patientQuantificationLineItem);
    }
  }

  /*private void insertEquipmentStatus(Rnr requisition, List<EquipmentLineItem> equipmentLineItems) {

    List<EquipmentLineItemBioChemistryTests> generatedNonFunctionalTestes =
            equipmentLineItemMapper.getEmptyBioChemistryEquipmentTestWithProducts();

    for (EquipmentLineItem equipmentLineItem : equipmentLineItems) {
      EquipmentInventoryStatus status = getStatusFromEquipmentLineItem(equipmentLineItem);
      equipmentInventoryStatusMapper.insert(status);
      equipmentLineItem.setInventoryStatusId(status.getId());

      equipmentLineItem.setRnrId(requisition.getId());
      equipmentLineItem.setModifiedBy(requisition.getModifiedBy());
      equipmentLineItemMapper.insert(equipmentLineItem);

      List<EquipmentLineItemBioChemistryTests> newNonFunctionalTestes;
      newNonFunctionalTestes = generatedNonFunctionalTestes;

      if(equipmentLineItem.getIsBioChemistryEquipment() != null && equipmentLineItem.getIsBioChemistryEquipment()) {
          newNonFunctionalTestes.stream().forEach(test -> {
              test.setEquipmentLineItemId(equipmentLineItem.getId().intValue());
              test.setModifiedBy(requisition.getModifiedBy());
              test.setCreatedBy(requisition.getModifiedBy());
              test.setCreatedDate(requisition.getCreatedDate());
              test.setModifiedDate(requisition.getModifiedDate());
              equipmentLineItemMapper.insertEquipmentLineItemBioChemistryTests(test);
          });
      }
    }
  }*/

  private void insertEquipmentStatus(Rnr requisition, List<EquipmentLineItem> equipmentLineItems) {

    for (EquipmentLineItem equipmentLineItem : equipmentLineItems) {
      EquipmentInventoryStatus status = getStatusFromEquipmentLineItem(equipmentLineItem);
      equipmentInventoryStatusMapper.insert(status);
      equipmentLineItem.setInventoryStatusId(status.getId());

      equipmentLineItem.setRnrId(requisition.getId());
      equipmentLineItem.setModifiedBy(requisition.getModifiedBy());
      equipmentLineItemMapper.insert(equipmentLineItem);
    }

    equipmentLineItemMapper.generateOperationalStatus(requisition.getId(), requisition.getModifiedBy());

    equipmentLineItemMapper.generateTestCounts(requisition.getId(), requisition.getModifiedBy());

     /* List<EquipmentLineItemBioChemistryTests> generatedNonFunctionalTestes =
            equipmentLineItemMapper.getEmptyBioChemistryEquipmentTestWithProducts();


      List<EquipmentLineItemBioChemistryTests> newNonFunctionalTestes;
      newNonFunctionalTestes = generatedNonFunctionalTestes;

      if(equipmentLineItem.getIsBioChemistryEquipment() != null && equipmentLineItem.getIsBioChemistryEquipment()) {
        newNonFunctionalTestes.stream().forEach(test -> {
          test.setEquipmentLineItemId(equipmentLineItem.getId().intValue());
          test.setModifiedBy(requisition.getModifiedBy());
          test.setCreatedBy(requisition.getModifiedBy());
          test.setCreatedDate(requisition.getCreatedDate());
          test.setModifiedDate(requisition.getModifiedDate());
          equipmentLineItemMapper.insertEquipmentLineItemBioChemistryTests(test);
        });
      }
      */

  }

  private void insertManualTestsLineItems(Rnr requisition, List<ManualTestesLineItem> manualTestLineItems) {
    manualTestLineItems.stream().forEach(item -> {
          item.setRnrId(requisition.getId());
          item.setModifiedBy(requisition.getModifiedBy());
          item.setCreatedBy(requisition.getCreatedBy());
          manualTestMapper.insertManualTestLineItem(item);
    });
  }

  private EquipmentInventoryStatus getStatusFromEquipmentLineItem(EquipmentLineItem equipmentLineItem) {
    EquipmentInventoryStatus status = new EquipmentInventoryStatus();
    status.setId(equipmentLineItem.getInventoryStatusId());
    status.setInventoryId(equipmentLineItem.getEquipmentInventoryId());

    if(equipmentLineItem.getOperationalStatusId() == null)
      status.setStatusId(operationalStatusService.getByCode(FUNCTIONAL).getId());
    else status.setStatusId(equipmentLineItem.getOperationalStatusId());

    return status;
  }

  private void insertRegimenLineItems(Rnr requisition, List<RegimenLineItem> regimenLineItems) {
    for (RegimenLineItem regimenLineItem : regimenLineItems) {
      regimenLineItem.setRnrId(requisition.getId());
      regimenLineItem.setModifiedBy(requisition.getModifiedBy());
      regimenLineItemMapper.insert(regimenLineItem);
    }
  }

  private void insertLineItems(Rnr requisition, List<RnrLineItem> lineItems) {
    for (RnrLineItem lineItem : lineItems) {
      lineItem.setRnrId(requisition.getId());
      lineItem.setModifiedBy(requisition.getModifiedBy());
      rnrLineItemMapper.insert(lineItem, lineItem.getPreviousNormalizedConsumptions().toString());
    }
  }

  public void update(Rnr rnr) {
    requisitionMapper.update(rnr);
    updateFullSupplyLineItems(rnr);
    updateNonFullSupplyLineItems(rnr);
    if (!(rnr.getStatus() == AUTHORIZED || rnr.getStatus() == IN_APPROVAL)) {
      updateRegimenLineItems(rnr);
      updateEquipmentLineItems(rnr);
      updateManualTestsLineItems(rnr);
    }
  }

  private void updateManualTestsLineItems(Rnr rnr) {
    if(rnr.getManualTestLineItems() == null) return;

    rnr.getManualTestLineItems().stream().forEach(item ->{
      item.setModifiedBy(rnr.getModifiedBy());
      item.setModifiedDate(rnr.getModifiedDate());
      manualTestMapper.updateManualTestLineItem(item);
    });
  }

  private void updateEquipmentLineItems(Rnr rnr) {
    for(EquipmentLineItem item : rnr.getEquipmentLineItems()){
      equipmentLineItemMapper.update(item);

      if(item.getNonFunctionalDaysOutOfUse() != null)
        item.getNonFunctionalDaysOutOfUse().stream().forEach( operationalStatus -> {
              operationalStatus.setModifiedBy(rnr.getModifiedBy());
              operationalStatus.setModifiedDate(rnr.getModifiedDate());
              equipmentLineItemMapper.updateNonFunctionalDaysOutOfUse(operationalStatus);
            }
        );

      if(item.getTestDone() != null)
        item.getTestDone().stream().forEach(test -> {
            test.setModifiedBy(rnr.getModifiedBy());
            test.setModifiedDate(rnr.getModifiedDate());
            equipmentLineItemMapper.updateTestDone(test);
          }
        );

      EquipmentInventoryStatus status = getStatusFromEquipmentLineItem(item);
      equipmentInventoryStatusMapper.update(status);
    }
  }

  private void updateRegimenLineItems(Rnr rnr) {
    for (RegimenLineItem regimenLineItem : rnr.getRegimenLineItems()) {
      regimenLineItemMapper.update(regimenLineItem);
    }
  }

  public void approve(Rnr rnr) {
    requisitionMapper.update(rnr);
    for (RnrLineItem lineItem : rnr.getFullSupplyLineItems()) {
      updateLineItem(rnr, lineItem);
    }
    for (RnrLineItem lineItem : rnr.getNonFullSupplyLineItems()) {
      updateLineItem(rnr, lineItem);
    }
  }

  private void updateNonFullSupplyLineItems(Rnr rnr) {
    for (RnrLineItem lineItem : rnr.getNonFullSupplyLineItems()) {
      RnrLineItem savedLineItem = rnrLineItemMapper.getExistingNonFullSupplyItemByRnrIdAndProductCode(rnr.getId(), lineItem.getProductCode());
      if (savedLineItem != null) {
        lineItem.setId(savedLineItem.getId());
        updateLineItem(rnr, lineItem);
        continue;
      }
      lineItem.setRnrId(rnr.getId());
      rnrLineItemMapper.insertNonFullSupply(lineItem);
    }
  }

  private void updateFullSupplyLineItems(Rnr requisition) {
    for (RnrLineItem lineItem : requisition.getFullSupplyLineItems()) {
      updateLineItem(requisition, lineItem);
      lossesAndAdjustmentsMapper.deleteByLineItemId(lineItem.getId());
      insertLossesAndAdjustmentsForLineItem(lineItem);
    }
  }

  private void updateLineItem(Rnr requisition, RnrLineItem lineItem) {
    lineItem.setModifiedBy(requisition.getModifiedBy());
    if (requisition.getStatus() == RnrStatus.IN_APPROVAL) {
      rnrLineItemMapper.updateOnApproval(lineItem);
      return;
    }
    rnrLineItemMapper.update(lineItem);
  }

  private void insertLossesAndAdjustmentsForLineItem(RnrLineItem lineItem) {
    for (LossesAndAdjustments lossAndAdjustment : lineItem.getLossesAndAdjustments()) {
      lossesAndAdjustmentsMapper.insert(lineItem, lossAndAdjustment);
    }
  }

  public Rnr getRequisitionWithLineItems(Facility facility, Program program, ProcessingPeriod period) {
    return requisitionMapper.getRequisitionWithLineItems(facility, program, period);
  }

  public Rnr getRequisitionWithoutLineItems(Long facilityId, Long programId, Long periodId) {
    return requisitionMapper.getRequisitionWithoutLineItems(facilityId, programId, periodId);
  }

  public Rnr getRegularRequisitionWithLineItems(Facility facility, Program program, ProcessingPeriod period) {
    return requisitionMapper.getRegularRequisitionWithLineItems(facility, program, period);
  }

  public List<Rnr> getInitiatedOrSubmittedEmergencyRequisitions(Long facilityId, Long programId) {
    return requisitionMapper.getInitiatedOrSubmittedEmergencyRequisitions(facilityId, programId);
  }

  public List<LossesAndAdjustmentsType> getLossesAndAdjustmentsTypes() {
    return lossesAndAdjustmentsMapper.getLossesAndAdjustmentsTypes();
  }

  public Rnr getById(Long rnrId) {
    Rnr requisition = requisitionMapper.getById(rnrId);
    if (requisition == null) throw new DataException("error.rnr.not.found");
    return requisition;
  }

  @Deprecated
  public List<Rnr> getAuthorizedRequisitions(RoleAssignment roleAssignment) {
    return requisitionMapper.getAuthorizedRequisitions(roleAssignment);
  }

  public List<RnrDTO> getAuthorizedRequisitionsDTOs(RoleAssignment roleAssignment){
    return requisitionMapper.getAuthorizedRequisitionsDTO(roleAssignment);
  }

  public Rnr getLastRegularRequisitionToEnterThePostSubmitFlow(Long facilityId, Long programId) {
    return requisitionMapper.getLastRegularRequisitionToEnterThePostSubmitFlow(facilityId, programId);
  }

  public List<Rnr> getPostSubmitRequisitions(Facility facility, Program program, List<ProcessingPeriod> periods) {
    return requisitionMapper.getPostSubmitRequisitions(facility, program, commaSeparator.commaSeparateIds(periods));
  }

  public Integer getCategoryCount(Rnr requisition, boolean fullSupply) {
    return rnrLineItemMapper.getCategoryCount(requisition, fullSupply);
  }

  public List<Comment> getCommentsByRnrID(Long rnrId) {
    return commentMapper.getByRnrId(rnrId);
  }

  public void insertComment(Comment comment) {
    commentMapper.insert(comment);
  }

  public void logStatusChange(Rnr requisition, String name) {
    RequisitionStatusChange statusChange = new RequisitionStatusChange(requisition, name);
    requisitionStatusChangeMapper.insert(statusChange);
  }

  public Date getOperationDateFor(Long rnrId, String status) {
    return requisitionStatusChangeMapper.getOperationDateFor(rnrId, status);
  }

  public Rnr getLWById(Long rnrId) {
    return requisitionMapper.getLWById(rnrId);
  }

  public List<Rnr> getApprovedRequisitionsForCriteriaAndPageNumber(String searchType, String searchVal, Integer pageNumber,
                                                                   Integer pageSize, Long userId, String rightName, String sortBy,
                                                                   String sortDirection) {
    return requisitionMapper.getApprovedRequisitionsForCriteriaAndPageNumber(searchType, searchVal, pageNumber, pageSize,
            userId, rightName, sortBy, sortDirection);
  }

  public Integer getCountOfApprovedRequisitionsForCriteria(String searchType, String searchVal, Long userId, String rightName) {
    return requisitionMapper.getCountOfApprovedRequisitionsForCriteria(searchType, searchVal, userId, rightName);
  }

  public Long getFacilityId(Long id) {
    return requisitionMapper.getFacilityId(id);
  }

  public Rnr getLastRegularRequisition(Facility facility, Program program) {
    return requisitionMapper.getLastRegularRequisition(facility, program);
  }

  public Date getAuthorizedDateForPreviousLineItem(Rnr rnr, String productCode, Date periodStartDate) {
    return rnrLineItemMapper.getAuthorizedDateForPreviousLineItem(rnr, productCode, periodStartDate);
  }

  public List<RnrLineItem> getAuthorizedRegularUnSkippedLineItems(String productCode, Rnr rnr, Integer n, Date startDate) {
    return rnrLineItemMapper.getAuthorizedRegularUnSkippedLineItems(productCode, rnr, n, startDate);
  }

  public RnrLineItem getNonSkippedLineItem(Long rnrId, String productCode) {
    return rnrLineItemMapper.getNonSkippedLineItem(rnrId, productCode);
  }

  public Long getProgramId(Long rnrId) {
    return requisitionMapper.getProgramId(rnrId);
  }

  public String deleteRnR(Long rnrId) {
    return requisitionMapper.deleteRnR(rnrId.intValue());
  }

  public void updateClientFields(Rnr rnr) {
    requisitionMapper.updateClientFields(rnr);
  }

  public List<Rnr> getRequisitionDetailsByFacility(Facility facility) {
    return requisitionMapper.getRequisitionsWithLineItemsByFacility(facility);
  }

  public void insertRnrSignatures(Rnr rnr) {
    for (Signature signature: rnr.getRnrSignatures()) {
      signatureMapper.insertSignature(signature);
      requisitionMapper.insertRnrSignature(rnr, signature);
    }
  }

  public Rnr getRnrBy(Long facilityId, Long periodId, Long programId, boolean emergency) {
    return requisitionMapper.getRnrBy(facilityId, periodId, programId, emergency);
  }

  public List<Rnr> getUnreleasedPreviousRequisitions(Rnr rnr) {
    return requisitionMapper.getUnreleasedPreviousRequisitions(rnr);
  }

  public List<SourceOfFundDTO>getAllSourcesOfFund(){
    return sourceOfFundRepository.getAllSourcesOfFunds();
  }

  public List<SourceOfFundLineItemDTO> getFundingSourceByRnrID(Long rnrId) {
    return fundLineItemMapper.getLineItems(rnrId);
  }

  public void insertFundingSourceLineItem(SourceOfFundLineItemDTO lineItemDTO) {
    fundLineItemMapper.Insert(lineItemDTO);
  }

  public List<HashMap<String,Object>> getRequisitionDetailsByFacilityAndProgram(String facilityCode, String programCode) {
    return requisitionMapper.getRequisitionsWithLineItemsByFacilityAndProgram(facilityCode,programCode);

  }

  public List<RejectionReason> getRejectionsByRnrID(Long rnrId) {
    return rejectionReasonMapper.getByRnrId(rnrId);
  }

  public void insertRejectionReason(RejectionReason reason) {
    rejectionReasonMapper.insert(reason);
  }

  public List<Map<String,Object>>getAllRejections(){
    return rejectionReasonMapper.getAllRejections();
  }

  public Rnr getAllWithoutSkippedItemsById(Long rnrId) {
    Rnr requisition = requisitionMapper.getAllWithoutSkippedItemsById(rnrId);
    if (requisition == null) throw new DataException("error.rnr.not.found");
    return requisition;
  }

  public List<HashMap<String,Object>> getSupervisionCheckListReport(String facilityCode, String programCode) {
   return requisitionMapper.getSupervisionCheckListReport(facilityCode,programCode);
  }
}
