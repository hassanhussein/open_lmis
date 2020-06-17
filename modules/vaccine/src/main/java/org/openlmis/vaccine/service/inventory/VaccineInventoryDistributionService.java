/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.vaccine.service.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.ProcessingPeriod;
import org.openlmis.core.domain.Program;
import org.openlmis.core.repository.ProcessingPeriodRepository;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.MessageService;
import org.openlmis.core.service.ProcessingScheduleService;
import org.openlmis.core.service.ProgramService;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.vaccine.domain.VaccineOrderRequisition.VaccineOrderRequisition;
import org.openlmis.vaccine.domain.VaccineOrderRequisition.VaccineOrderRequisitionLineItem;
import org.openlmis.vaccine.domain.VaccineOrderRequisition.VaccineOrderStatus;
import org.openlmis.vaccine.domain.inventory.*;
import org.openlmis.vaccine.dto.BatchExpirationNotificationDTO;
import org.openlmis.vaccine.dto.VaccineDistributionAlertDTO;
import org.openlmis.vaccine.dto.VaccineDistributionNotification;
import org.openlmis.vaccine.repository.inventory.VaccineDistributionStatusChangeRepository;
import org.openlmis.vaccine.repository.inventory.VaccineInventoryDistributionRepository;
import org.openlmis.vaccine.service.VaccineOrderRequisitionServices.VaccineNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@NoArgsConstructor
public class VaccineInventoryDistributionService {

    public static final String cvsRegionCode = "label.vaccine.voucher.number.region.for.cvs";
    public static final String cvsDistrictCode = "label.vaccine.voucher.number.district.for.cvs";
    public static final String rvsDistrictCode = "label.vaccine.voucher.number.district.for.rvs";
    public static final Boolean consolidate = false;

    @Autowired
    VaccineInventoryDistributionRepository repository;
    @Autowired
    ProgramService programService;
    @Autowired
    FacilityService facilityService;
    @Autowired
    MessageService messageService;
    @Autowired
    ProcessingScheduleService processingScheduleService;
    @Autowired
    ProcessingPeriodRepository processingPeriodRepository;

    @Autowired
    private VaccineDistributionStatusChangeRepository statusChangeRepository;
    @Autowired
    private VaccineNotificationService notificationService;

 /*   @Autowired
    private SdpNotificationService sdpNotificationService;*/


    public List<Facility> getFacilities(Long userId) {
        Facility homeFacility = facilityService.getHomeFacility(userId);
        Long facilityId = homeFacility.getId();
        return getOneLevelSupervisedFacilities(facilityId);
    }

    public List<Facility> getSameLevelFacilities(Long userId) {
        Facility homeFacility = facilityService.getHomeFacility(userId);
        Facility parentFacility = facilityService.getParentFacility(homeFacility.getId());
        return getOneLevelSupervisedFacilities(parentFacility.getId());
    }

    public List<Facility> getOneLevelSupervisedFacilities(Long facilityId) {
        return repository.getOneLevelSupervisedFacilities(facilityId);
    }

@Transactional
    public Long save(VaccineDistribution distribution, Long userId) {
        //Get supervised facility period

    System.out.println(userId);
        Facility homeFacility = facilityService.getHomeFacility(userId);
    System.out.println("fac");
    System.out.println(homeFacility);
        Long homeFacilityId = homeFacility.getId();
    System.out.println("home");
    System.out.println(homeFacilityId);
        ProcessingPeriod period = null;
        if (null != distribution.getToFacilityId() && null != distribution.getProgramId()) {
            period = getCurrentPeriod_new(distribution.getToFacilityId(), distribution.getProgramId(), distribution.getDistributionDate());
        }
        if (period != null) {
            distribution.setPeriodId(period.getId());
        }

        if (null == distribution.getVoucherNumber())
         distribution.setVoucherNumber(generateVoucherNumber(homeFacilityId, distribution.getProgramId()));

         VaccineDistribution dbDistribution = null;

        if (distribution.getOrderId() != null) {

             dbDistribution = repository.getDistributionByOrderId(distribution.getOrderId());
             distribution.setId(dbDistribution.getId());
        }
        if (distribution.getId() != null || dbDistribution != null) {

            distribution.setModifiedBy(userId);
            repository.updateDistribution(distribution);

            //Update status changes to keep distribution log
            VaccineDistributionStatusChange statusChange = new VaccineDistributionStatusChange(distribution,userId);
            statusChangeRepository.insert(statusChange);
        } else {
            distribution.setPickListId(generatePickList());
            distribution.setCreatedBy(userId);
            ObjectMapper mapper = new ObjectMapper();
            try {
                System.out.println("Distribution");
                System.out.println(mapper.writeValueAsString(distribution));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            repository.saveDistribution(distribution);
            System.out.println("Distribution Id");

            //Update status changes to keep distribution log
            VaccineDistributionStatusChange statusChange = new VaccineDistributionStatusChange(distribution,userId);
            statusChangeRepository.insert(statusChange);

        }

       List<VaccineDistributionLineItem> itemList = repository.getItemByDistributionId(distribution.getId());

        if(!itemList.isEmpty()) {

            for (VaccineDistributionLineItem item : itemList) {
                repository.deleteLotsByLineItem(item.getId());
            }
          repository.deleteLineItemByDistributionId(distribution.getId());
        }

        for (VaccineDistributionLineItem lineItem : distribution.getLineItems()) {
            lineItem.setDistributionId(distribution.getId());
            lineItem.setCreatedBy(userId);
            lineItem.setModifiedBy(userId);
            if (lineItem.getId() != null) {
                repository.updateDistributionLineItem(lineItem);
            } else {
                repository.saveDistributionLineItem(lineItem);
            }

            repository.deleteLotsByLineItem(lineItem.getId());
            if (lineItem.getLots() != null) {
                for (VaccineDistributionLineItemLot lot : lineItem.getLots()) {
                    lot.setModifiedBy(userId);
                    lot.setCreatedBy(userId);
                    lot.setDistributionLineItemId(lineItem.getId());
                    lot.setQuantity(lot.getQty());
                    System.out.println(lot.getQty());
                    if (lot.getId() != null) {
                        repository.updateDistributionLineItemLot(lot);
                   } else {
                        repository.saveDistributionLineItemLot(lot);
                    }
                }
            }
        }

        return distribution.getId();
    }

    private String generateVoucherNumber(Long facilityId, Long programId) {

        VoucherNumberCode voucherNumberCode = repository.getFacilityVoucherNumberCode(facilityId);

        String lastVoucherNumber = repository.getLastVoucherNumber();
        String newVoucherNumber;
        Long newSerial;

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);

        if (lastVoucherNumber != null) {
            String serialString = lastVoucherNumber.substring(lastVoucherNumber.lastIndexOf('/') + 1);
            Long serial = Long.parseLong(serialString);
            newSerial = serial + 1;
        } else {
            newSerial = 1L;
        }

        String nationalCode = "";
        if (null != voucherNumberCode.getNational()) {
            String[] nationalCodes = voucherNumberCode.getNational().split("\\s+");
            nationalCode = (nationalCodes.length > 1) ? (nationalCodes[0].substring(0, 2).toUpperCase() + nationalCodes[1].substring(0, 1).toUpperCase()) : (nationalCodes[0].substring(0, 3).toUpperCase());
        }
        String regionCode = "";
        if (null != voucherNumberCode.getRegion()) {
            if (voucherNumberCode.getRegion().equalsIgnoreCase("cvs-region-code")) {
                regionCode = messageService.message(cvsRegionCode);
            } else {
                String[] regionCodes = voucherNumberCode.getRegion().split("\\s+");
                regionCode = (regionCodes.length > 1) ? (regionCodes[0].substring(0, 2).toUpperCase() + regionCodes[1].substring(0, 1).toUpperCase()) : (regionCodes[0].substring(0, 3).toUpperCase());
            }
        }
        String districtCode = "";
        if (null != voucherNumberCode.getDistrict()) {
            if (voucherNumberCode.getDistrict().equalsIgnoreCase("cvs-district-code")) {

                districtCode = messageService.message(cvsDistrictCode);
            } else if (voucherNumberCode.getDistrict().equalsIgnoreCase("rvs-district-code")) {
                districtCode = messageService.message(rvsDistrictCode);
            } else {
                String[] districtCodes = voucherNumberCode.getDistrict().split("\\s+");
                districtCode = (districtCodes.length > 1) ? (districtCodes[0].substring(0, 2).toUpperCase() + districtCodes[1].substring(0, 1).toUpperCase()) : (districtCodes[0].substring(0, 3).toUpperCase());
            }
        }
        newVoucherNumber = nationalCode + "/" + regionCode + "/" + districtCode + "/" + year + "/" + newSerial;
        return newVoucherNumber;
    }

    public VaccineDistribution getDistributionForFacilityByPeriod(Long facilityId, Long programId) {
        ProcessingPeriod period = getCurrentPeriod(facilityId, programId);
        if (period != null) {
            return repository.getDistributionForFacilityByPeriod(facilityId, period.getId());
        } else {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            return repository.getDistributionForFacilityByMonth(facilityId, month, year);
        }
    }

    public ProcessingPeriod getCurrentPeriod(Long facilityId, Long programId) {
        System.out.println("FCILITY");
        System.out.println(facilityId);
        Date programStartDate = programService.getProgramStartDate(facilityId, programId);
        return processingScheduleService.getCurrentPeriod(facilityId, programId, programStartDate);
    }

  public List<ProcessingPeriod> getALlBelowPeriod(Long facilityId, Long programId) {

        Date programStartDate = programService.getProgramStartDate(facilityId, programId);
        return processingScheduleService.getCurrentPeriodForDistribution(facilityId, programId,programStartDate);
    }

    public ProcessingPeriod getCurrentPeriod_new(Long facilityId, Long programId,Date distributionDate) {
        Date programStartDate = programService.getProgramStartDate(facilityId, programId);
        return processingScheduleService.getCurrentPeriodNew(facilityId, programId, distributionDate);
    }

    public ProcessingPeriod getSupervisedCurrentPeriod(Long userId) {
        Facility homeFacility = facilityService.getHomeFacility(userId);
        Long facilityId = homeFacility.getId();
        List<Program> programs = programService.getAllIvdPrograms();
        Long programId = (programs == null) ? null : programs.get(0).getId();
        return repository.getSupervisedCurrentPeriod(facilityId, programId, new Date());
    }

    public VaccineDistribution getById(Long id) {
        return repository.getById(id);
    }

    public List<Lot> getLotsByProductId(Long productId) {
        return repository.getLotsByProductId(productId);
    }

    public VaccineDistribution getDistributionByVoucherNumber(Long userId, String voucherNumber) {
        Facility homeFacility = facilityService.getHomeFacility(userId);
        Long facilityId = homeFacility.getId();
        return repository.getDistributionByVoucherNumber(facilityId, voucherNumber);
    }
    public VaccineDistribution getAllDistributionsByVoucherNumber(Long userId, String voucherNumber) {
        Facility homeFacility = facilityService.getHomeFacility(userId);
        Long facilityId = homeFacility.getId();
        return repository.getAllDistributionsByVoucherNumber(facilityId, voucherNumber);
    }

    public VoucherNumberCode getFacilityGeographicZone(Long userId) {
        Facility homeFacility = facilityService.getHomeFacility(userId);
        Long facilityId = homeFacility.getId();
        return repository.getFacilityVoucherNumberCode(facilityId);
    }


    public List<VaccineDistribution> saveConsolidatedList(List<VaccineDistribution> distributionList, Long userId) {

        for (VaccineDistribution distribute : distributionList) {
            save(distribute,userId);
            notificationService.sendConsolidationNotification(distribute,userId);

        }

        return distributionList;

    }

    public VaccineDistribution getAllDistributionsForNotification(Long facilityId) {
        return repository.getAllDistributionsForNotification(facilityId);
    }

    public Long UpdateNotification(Long Id){
        return repository.updateNotification(Id);
    }

    public List<ProcessingPeriod> getLastPeriod(Long facilityId, Long programId) {
        ProcessingPeriod currentPeriod = getCurrentPeriod(facilityId, programId);
        return processingPeriodRepository.getNPreviousPeriods(currentPeriod, 1);
    }

    public VaccineDistribution getDistributionByToFacility(Long facilityId) {
        return repository.getDistributionByToFacility(facilityId);
    }

    public Long getSupervisorFacilityId(Long facilityId) {
        return repository.getSupervisorFacilityId(facilityId);
    }

   public List<VaccineDistributionAlertDTO>getPendingReceivedAlert(Long facilityId){
       return repository.getPendingDistributionAlert(facilityId);
   }

   public List<VaccineDistributionAlertDTO>getPendingNotificationForLowerLevel(Long facilityId){
       return repository.getPendingNotificationFoLowerLevel(facilityId);
   }
    public List<Facility> getFacilitiesSameType(Long facilityId, String query) {
        return repository.getFacilitiesSameType(facilityId, query);
    }

    public List<VaccineDistribution> getDistributionsByDate(Long facilityId, String date) {
        return repository.getDistributionsByDate(facilityId, date);
    }

    public List<VaccineDistribution> getDistributionsByDateRange(Long facilityId, String date, String endDate,String distributionType) {
        return repository.getDistributionsByDateRange(facilityId, date, endDate,distributionType);
    }

    public VaccineDistribution getDistributionByVoucherNumberIfExist(Long userId, String voucherNumber) {
        Facility homeFacility = facilityService.getHomeFacility(userId);
        Long facilityId = homeFacility.getId();
        return repository.getDistributionByVoucherNumberIfExist(facilityId, voucherNumber);
    }
 public List<BatchExpirationNotificationDTO>getBatchExpiryNotifications(Long facilityId){
     return  repository.getBatchExpiryNotifications(facilityId);
 }

    public VaccineDistribution getDistributionById(Long id) {
        return repository.getDistributionById(id);
    }


    public List<VaccineDistribution> getDistributionsByDateRangeAndFacility(Long facilityId, String startDate, String endDate) {
        return repository.getDistributionsByDateRangeAndFacility(facilityId, startDate,endDate);
    }

    public List<VaccineDistribution> getDistributionsByDateRangeForFacility(Long facilityId, String startDate, String endDate) {
        return repository.getDistributionsByDateRangeForFacility(facilityId, startDate, endDate);
    }

    public List<VaccineDistribution> searchDistributionsByDateRangeAndFacility(Long facilityId, String startDate, String endDate,String distributionType, String searchParam) {
        return  repository.searchDistributionByDateRangeAndFacility(facilityId,startDate,endDate,distributionType,searchParam);
    }
    public List<VaccineDistribution>getReceiveNotification(Long facilityId){
        return repository.getReceiveNotification(facilityId);
    }

    public List<VaccineDistributionAlertDTO>getReceiveDistributionAlert(Long facilityId){
        return repository.getReceiveDistributionAlert(facilityId);
    }

    public List<Map<String,Object>>getMinimumStockNotification(Long userId,Long facilityId){
        return repository.getMinimumStockNotification(facilityId);
    }

    public List<HashMap<String,Object>> getLastDistributionForFacility(Long toFacilityId, String distributionType, String distributionDate, String status) {
        return repository.getLastDistributionForFacility(toFacilityId,distributionType,distributionDate,status);
    }

    public void saveDistributionNotification(VaccineDistribution distribution,Long userId) {
        VaccineDistributionNotification notification = new VaccineDistributionNotification();
        Facility facility = facilityService.getById(distribution.getFromFacilityId());
        Facility toFacility = facilityService.getById(distribution.getToFacilityId());

        notification.setDistributionId(distribution.getId());
        notification.setFacilityId(distribution.getFromFacilityId());
        notification.setGeographicZoneId(facility.getGeographicZone().getId());
        notification.setAllowedToSend(facility.getAllowedToSend());
        notification.setCreatedBy(userId);
        notification.setSent(false);
        notification.setFromFacility(facility.getName());
        notification.setToFacility(toFacility.getName());
        repository.saveDistributionNotification(notification);
    }

    public List<HashMap<String, Object>>getNotificationDistributionList(Long districtId,String startDate, String endDate){
        return repository.getNotificationDistributionList(districtId,startDate,endDate);
    }

    public Long generatePickList() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp.getTime());
        return timestamp.getTime();
    }

    public List<HashMap<String,Object>>  getPickList(String startDate, String endDate) {
        return repository.getPickList(startDate,endDate);
    }

    public static int generateRandomDigits(int n) {
        int m = (int) Math.pow(10, n - 1);
        return m + new Random().nextInt(9 * m);
    }

    public void saveDistribution(List<VaccineDistribution> distributions, Long userId) {

        for (VaccineDistribution distribution : distributions) {
            distribution.setCreatedBy(userId);
            distribution.setModifiedBy(userId);
            distribution.setStatus("UNDER_PICKING");
            save(distribution,userId);
      }

    }

    public void updateOrderStatus(List<VaccineDistribution> distribution) {

        for(VaccineDistribution distribution1 : distribution) {

            VaccineOrderRequisition requisition = new VaccineOrderRequisition();
            requisition.setStatus(VaccineOrderStatus.UNDER_PICKING);

            System.out.println(distribution1.getOrderId());
            requisition.setId(distribution1.getOrderId());

            notificationService.updateOrderStatus(requisition);

        }

    }
}
