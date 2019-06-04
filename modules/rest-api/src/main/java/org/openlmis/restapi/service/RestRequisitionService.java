/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.restapi.service;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.log4j.Logger;
import org.openlmis.core.domain.*;
import org.openlmis.core.dto.*;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.message.OpenLmisMessage;
import org.openlmis.core.service.*;
import org.openlmis.order.service.OrderService;
import org.openlmis.restapi.domain.ReplenishmentDTO;
import org.openlmis.restapi.domain.Report;
import org.openlmis.restapi.request.RequisitionSearchRequest;
import org.openlmis.rnr.domain.*;
import org.openlmis.rnr.search.criteria.RequisitionSearchCriteria;
import org.openlmis.rnr.service.RequisitionService;
import org.openlmis.rnr.service.RnrTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Long.valueOf;
import static java.util.Arrays.asList;
import static org.apache.commons.collections.CollectionUtils.find;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.openlmis.restapi.domain.ReplenishmentDTO.prepareForREST;
import static org.openlmis.rnr.domain.RnrStatus.SUBMITTED;

/**
 * This service exposes methods for creating, approving a requisition.
 */

@Service
@NoArgsConstructor
public class RestRequisitionService {

    public static final boolean EMERGENCY = false;
    public static final String ENTITY_NOT_MATCHED = "7003";
    public static final String ERROR_MISSED_VALUE = "7002";
    public static final String APP_SETTING_CODE = "GOTHOMIS-ELMIS-INTERFACE";
    public static final String INPUT_STATUS = "RECEIVED";
    public static final String SUCCESS_MESSAGE = "7000";
    private static final String SOURCE_APPLICATION_ELMIS_FE = "ELMIS_FE";
    private static final String SOURCE_APPLICATION_OTHER = "OTHER";
    private static final Logger logger = Logger.getLogger(RestRequisitionService.class);

    @Autowired
    private RequisitionService requisitionService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private ProgramService programService;
    @Autowired
    private RnrTemplateService rnrTemplateService;
    @Autowired
    private RestRequisitionCalculator restRequisitionCalculator;
    @Autowired
    private ProcessingPeriodService processingPeriodService;
    @Autowired
    private FacilityApprovedProductService facilityApprovedProductService;
    @Autowired
    private ELMISInterfaceService interfaceService;

    private List<FacilityTypeApprovedProduct> fullSupplyFacilityTypeApprovedProducts;

    private List<FacilityTypeApprovedProduct> nonFullSupplyFacilityApprovedProducts;

    @Autowired
    private ProcessingScheduleService processingScheduleService;

    @Transactional
    public Rnr submitReport(Report report, Long userId) {
        report.validate();

        Facility reportingFacility = facilityService.getOperativeFacilityByCode(report.getAgentCode());
        Program reportingProgram = programService.getValidatedProgramByCode(report.getProgramCode());

        restRequisitionCalculator.validatePeriod(reportingFacility, reportingProgram);

        Rnr rnr = requisitionService.initiate(reportingFacility, reportingProgram, userId, EMERGENCY, null, SOURCE_APPLICATION_OTHER);

        restRequisitionCalculator.validateProducts(report.getProducts(), rnr);

        markSkippedLineItems(rnr, report);

        if (reportingFacility.getVirtualFacility())
            restRequisitionCalculator.setDefaultValues(rnr);

        copyRegimens(rnr, report);

        requisitionService.save(rnr);

        updateClientFields(report, rnr);
        insertPatientQuantificationLineItems(report, rnr);

        insertRnrSignatures(report, rnr, userId);

        rnr = requisitionService.submit(rnr);

        return requisitionService.authorize(rnr);
    }

    private void updateClientFields(Report report, Rnr rnr) {
        Date clientSubmittedTime = report.getClientSubmittedTime();
        rnr.setClientSubmittedTime(clientSubmittedTime);

        String clientSubmittedNotes = report.getClientSubmittedNotes();
        rnr.setClientSubmittedNotes(clientSubmittedNotes);

        requisitionService.updateClientFields(rnr);
    }

    @Transactional
    public Rnr submitSdpReport(Report report, Long userId) {
        report.validate();

        Facility reportingFacility = facilityService.getOperativeFacilityByCode(report.getAgentCode());
        Program reportingProgram = programService.getValidatedProgramByCode(report.getProgramCode());
        ProcessingPeriod period = processingPeriodService.getById(valueOf(report.getPeriodId()));


        Rnr rnr;
        List<Rnr> rnrs = null;

        RequisitionSearchCriteria searchCriteria = new RequisitionSearchCriteria();
        searchCriteria.setProgramId(reportingProgram.getId());
        searchCriteria.setFacilityId(reportingFacility.getId());
        searchCriteria.setWithoutLineItems(true);
        searchCriteria.setUserId(userId);

        if (report.getPeriodId() != null) {
            //check if the requisition has already been initiated / submitted / authorized.
            restRequisitionCalculator.validateCustomPeriod(reportingFacility, reportingProgram, period, userId);
            rnrs = requisitionService.getRequisitionsFor(searchCriteria, asList(period));
        }


        if (rnrs != null && rnrs.size() > 0) {
            rnr = requisitionService.getFullRequisitionById(rnrs.get(0).getId());

        } else {
            //by default, this API is being called from ELMIS_FE
            //if not, the application would have specified it's name.
            String sourceApplication = Strings.isNullOrEmpty(report.getSourceApplication()) ? SOURCE_APPLICATION_ELMIS_FE : report.getSourceApplication();
            rnr = requisitionService.initiate(reportingFacility, reportingProgram, userId, report.getEmergency(), period, sourceApplication);
        }

        List<RnrLineItem> fullSupplyProducts = new ArrayList<>();
        List<RnrLineItem> nonFullSupplyProducts = new ArrayList<>();

        fullSupplyFacilityTypeApprovedProducts = facilityApprovedProductService.getFullSupplyFacilityApprovedProductByFacilityAndProgram(reportingFacility.getId(), reportingProgram.getId());
        nonFullSupplyFacilityApprovedProducts = facilityApprovedProductService.getNonFullSupplyFacilityApprovedProductByFacilityAndProgram(reportingFacility.getId(), reportingProgram.getId());

        Collection<String> fullSupplyProductCodes = (Collection<String>) CollectionUtils.collect(fullSupplyFacilityTypeApprovedProducts, input -> ((FacilityTypeApprovedProduct) input).getProgramProduct().getProduct().getCode());
        Collection<String> nonFullSupplyProductCodes = (Collection<String>) CollectionUtils.collect(nonFullSupplyFacilityApprovedProducts, input -> ((FacilityTypeApprovedProduct) input).getProgramProduct().getProduct().getCode());

        fullSupplyProducts = report.getProducts().stream()
                .filter(p -> fullSupplyProductCodes.contains(p.getProductCode()))
                .collect(Collectors.toList());

        nonFullSupplyProducts = report.getProducts().stream()
                .filter(p -> nonFullSupplyProductCodes.contains(p.getProductCode()))
                .collect(Collectors.toList());

        for (RnrLineItem li : nonFullSupplyProducts) {
            setNonFullSupplyCreatorFields(li);
        }

        report.setProducts(fullSupplyProducts);
        report.setNonFullSupplyProducts(nonFullSupplyProducts);

        restRequisitionCalculator.validateProducts(report.getProducts(), rnr);

        markSkippedLineItems(rnr, report);


        copyRegimens(rnr, report);
        // if you have come this far, then do it, it is your day. make the submission.
        // i cannot believe we do all of these three at the same time.
        // but then this is what zambia specifically asked.
        requisitionService.save(rnr);
        rnr = requisitionService.submit(rnr);
        return requisitionService.authorize(rnr);
    }

    private void setNonFullSupplyCreatorFields(final RnrLineItem lineItem) {

        FacilityTypeApprovedProduct facilityTypeApprovedProduct = (FacilityTypeApprovedProduct) find(nonFullSupplyFacilityApprovedProducts, new Predicate() {
            @Override
            public boolean evaluate(Object product) {
                return ((FacilityTypeApprovedProduct) product).getProgramProduct().getProduct().getCode().equals(lineItem.getProductCode());
            }
        });
        if (facilityTypeApprovedProduct == null) {
            return;
        }
        lineItem.populateFromProduct(facilityTypeApprovedProduct.getProgramProduct());
        lineItem.setMaxMonthsOfStock(facilityTypeApprovedProduct.getMaxMonthsOfStock());
    }


    private void copyRegimens(Rnr rnr, Report report) {
        if (report.getRegimens() != null) {
            for (RegimenLineItem regimenLineItem : report.getRegimens()) {
                RegimenLineItem correspondingRegimenLineItem = rnr.findCorrespondingRegimenLineItem(regimenLineItem);
                if (correspondingRegimenLineItem == null)
                    throw new DataException("error.invalid.regimen");
                correspondingRegimenLineItem.populate(regimenLineItem);
            }
        }
    }

    private void insertPatientQuantificationLineItems(Report report, Rnr rnr) {
        if (report.getPatientQuantifications() != null) {
            rnr.setPatientQuantifications(report.getPatientQuantifications());
            requisitionService.insertPatientQuantificationLineItems(rnr);
        }
    }

    private void insertRnrSignatures(Report report, Rnr rnr, final Long userId) {
        if (report.getRnrSignatures() != null) {

            List<Signature> rnrSignatures = new ArrayList(CollectionUtils.collect(report.getRnrSignatures(), new Transformer() {
                @Override
                public Object transform(Object input) {
                    ((Signature) input).setCreatedBy(userId);
                    ((Signature) input).setModifiedBy(userId);
                    return input;
                }
            }));
            rnr.setRnrSignatures(rnrSignatures);
            requisitionService.insertRnrSignatures(rnr);
        }
    }

    @Transactional
    public void approve(Report report, Long requisitionId, Long userId) {
        Rnr requisition = report.getRequisition(requisitionId, userId);

        Rnr savedRequisition = requisitionService.getFullRequisitionById(requisition.getId());

        if (!savedRequisition.getFacility().getVirtualFacility()) {
            throw new DataException("error.approval.not.allowed");
        }

        if (savedRequisition.getNonSkippedLineItems().size() != report.getProducts().size()) {
            throw new DataException("error.number.of.line.items.mismatch");
        }

        restRequisitionCalculator.validateProducts(report.getProducts(), savedRequisition);

        requisitionService.save(requisition);
        requisitionService.approve(requisition, report.getApproverName());
    }

    public ReplenishmentDTO getReplenishmentDetails(Long id) {
        Rnr requisition = requisitionService.getFullRequisitionById(id);
        return prepareForREST(requisition, orderService.getOrder(id));
    }


    private void markSkippedLineItems(Rnr rnr, Report report) {

        ProgramRnrTemplate rnrTemplate = rnrTemplateService.fetchProgramTemplateForRequisition(rnr.getProgram().getId());

        List<RnrLineItem> savedLineItems = rnr.getFullSupplyLineItems();
        List<RnrLineItem> reportedProducts = report.getProducts();

        for (final RnrLineItem savedLineItem : savedLineItems) {
            RnrLineItem reportedLineItem = (RnrLineItem) find(reportedProducts, new Predicate() {
                @Override
                public boolean evaluate(Object product) {
                    return ((RnrLineItem) product).getProductCode().equals(savedLineItem.getProductCode());
                }
            });

            copyInto(savedLineItem, reportedLineItem, rnrTemplate);
        }


        savedLineItems = rnr.getNonFullSupplyLineItems();
        reportedProducts = report.getNonFullSupplyProducts();
        if (reportedProducts != null) {
            for (final RnrLineItem reportedLineItem : reportedProducts) {
                RnrLineItem savedLineItem = (RnrLineItem) find(savedLineItems, new Predicate() {
                    @Override
                    public boolean evaluate(Object product) {
                        return ((RnrLineItem) product).getProductCode().equals(reportedLineItem.getProductCode());
                    }
                });
                if (savedLineItem == null && reportedLineItem != null) {
                    rnr.getNonFullSupplyLineItems().add(reportedLineItem);
                } else {
                    copyInto(savedLineItem, reportedLineItem, rnrTemplate);
                }
            }
        }

    }

    private void copyInto(RnrLineItem savedLineItem, RnrLineItem reportedLineItem, ProgramRnrTemplate rnrTemplate) {
        if (reportedLineItem == null) {
            savedLineItem.setSkipped(true);
            return;
        }

        for (Column column : rnrTemplate.getColumns()) {
            if (!column.getVisible() || !rnrTemplate.columnsUserInput(column.getName()))
                continue;
            try {
                Field field = RnrLineItem.class.getDeclaredField(column.getName());
                field.setAccessible(true);

                Object reportedValue = field.get(reportedLineItem);
                Object toBeSavedValue = (reportedValue != null ? reportedValue : field.get(savedLineItem));
                field.set(savedLineItem, toBeSavedValue);
            } catch (Exception e) {
                logger.error("could not copy field: " + column.getName());
            }
        }
    }

    public List<Report> getRequisitionsByFacility(String facilityCode) {
        Facility facility = facilityService.getFacilityByCode(facilityCode);
        if (facility == null) {
            throw new DataException("error.facility.unknown");
        }

        List<Rnr> rnrList = requisitionService.getRequisitionsByFacility(facility);

        return FluentIterable.from(rnrList).transform(new Function<Rnr, Report>() {
            @Override
            public Report apply(Rnr input) {
                return Report.prepareForREST(input);
            }
        }).toList();
    }

    public Rnr initiateRnr(Long facilityId, Long programId, Long userId, Boolean emergency, Long periodId, String sourceApplication) {
        Facility reportingFacility = facilityService.getFacilityById(facilityId);
        Program reportingProgram = programService.getById(programId);
        ProcessingPeriod period = processingPeriodService.getById(periodId);

        Rnr rnr;
        List<Rnr> rnrs = null;

        RequisitionSearchCriteria searchCriteria = new RequisitionSearchCriteria();
        searchCriteria.setProgramId(reportingProgram.getId());
        searchCriteria.setFacilityId(reportingFacility.getId());
        searchCriteria.setWithoutLineItems(true);
        searchCriteria.setUserId(userId);

        if (periodId != null) {
            //check if the requisition has already been initiated / submitted / authorized.
            restRequisitionCalculator.validateCustomPeriod(reportingFacility, reportingProgram, period, userId);
            rnrs = requisitionService.getRequisitionsFor(searchCriteria, asList(period));
        }


        if (rnrs != null && rnrs.size() > 0) {
            rnr = requisitionService.getFullRequisitionById(rnrs.get(0).getId());

        } else {
            //by default, this API is being called from ELMIS_FE
            //if not, the application would have specified it's name.
            sourceApplication = Strings.isNullOrEmpty(sourceApplication) ? SOURCE_APPLICATION_ELMIS_FE : sourceApplication;
            rnr = requisitionService.initiate(reportingFacility, reportingProgram, userId, emergency, period, sourceApplication);
        }
        return rnr;
    }

    public List<Rnr> searchRnrs(RequisitionSearchRequest requisitionSearchRequest, Long userId) {
        List<Rnr> rnrs = new ArrayList<>();

        Facility reportingFacility = facilityService.getFacilityById(requisitionSearchRequest.getFacilityId());
        Program reportingProgram = programService.getById(requisitionSearchRequest.getProgramId());

        if (reportingProgram == null || reportingFacility == null) {
            return Collections.emptyList();
        }

        RequisitionSearchCriteria searchCriteria = new RequisitionSearchCriteria();
        searchCriteria.setProgramId(reportingProgram.getId());
        searchCriteria.setFacilityId(reportingFacility.getId());
        searchCriteria.setWithoutLineItems(false);
        searchCriteria.setUserId(userId);
        searchCriteria.setEmergency(requisitionSearchRequest.getEmergency());

        ProcessingPeriod period;
        Rnr fullRnR;
        for (Long periodId : requisitionSearchRequest.getPeriodIds()) {
            period = processingPeriodService.getById(periodId);
            if (period != null) {
                searchCriteria.setPeriodId(period.getId());
                List<Rnr> searchResult = requisitionService.getRequisitionsFor(searchCriteria, asList(period));
                if (searchResult != null && !searchResult.isEmpty()) {
                    fullRnR = requisitionService.getFullRequisitionById(searchResult.get(0).getId());
                    rnrs.add(fullRnR);
                }
            }
        }

        return rnrs;
    }


    public List<HashMap<String, Object>> getRequisitionsByFacilityAndProgram(String facilityCode, String programCode) {
        Facility facility = facilityService.getFacilityByCode(facilityCode);
        if (facility == null) {
            throw new DataException("error.facility.unknown");
        }

        return requisitionService.getRequisitionsByFacilityAndProgram(facility.getCode(), programCode);

    }

    public String formatDate(Date requestDate) {
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String date = "";
        date = outputFormat.format(requestDate);
        return date;
    }

    @Transactional
    public Map<String, OpenLmisMessage> saveSDPReport(Report report, Long userId) {
        if (report != null) {
            saveReceivedRnrBeforeProcessing(report);
        }

        Map<String, OpenLmisMessage> errors = null;

        report.setResponseMessage(getResponseMessage(ERROR_MISSED_VALUE));
        errors = report.validateReportFields();
        if (!errors.isEmpty()) return errors;

        //FacilityMappingDTO facility = facilityService.getByAgentCode(report.getAgentCode(), APP_SETTING_CODE);

        Facility facility = facilityService.getByCodeFor(report.getAgentCode());
        Program reportingProgram = programService.getByCode(report.getProgramCode().toLowerCase());
        ProcessingPeriod reportingPeriod = processingPeriodService.getById(report.getPeriodId());

        if (facility == null || reportingProgram == null || reportingPeriod == null || report.getRnrId() == null) {
            report.setResponseMessage(getResponseMessage(ENTITY_NOT_MATCHED));
            errors = report.validateMappedReportFields();
            if (!errors.isEmpty()) return errors;

        }
       // Date reportingDate = report.getDateFromPeriod(report.getPeriodId());

        Facility reportingFacility = facilityService.getFacilityById(facility.getId());

        ProcessingPeriod period = processingScheduleService.getPeriodForDate(reportingFacility, reportingProgram, reportingPeriod.getStartDate());

        Rnr rnr;
        List<Rnr> rnrs = null;

        RequisitionSearchCriteria searchCriteria = new RequisitionSearchCriteria();
        searchCriteria.setProgramId(reportingProgram.getId());
        searchCriteria.setFacilityId(facility.getId());
        searchCriteria.setWithoutLineItems(true);
        searchCriteria.setUserId(userId);


        rnr = requisitionService.getFullRequisitionById(report.getRnrId());
        // check if the requisition has already been initiated / submitted / authorized.
        if(SUBMITTED.equals(rnr.getStatus())) {
                throw new DataException("error.rnr.already.submitted.for.this.period");
        }
     /*   else {
            //by default, this API is being called from ELMIS_FE
            //if not, the application would have specified it's name.
            String sourceApplication = Strings.isNullOrEmpty(report.getSourceApplication()) ? SOURCE_APPLICATION_ELMIS_FE : report.getSourceApplication();
            rnr = requisitionService.initiate(reportingFacility, reportingProgram, userId, report.getEmergency(), period, sourceApplication);
        }*/
        List<RnrLineItem> fullSupplyProducts = new ArrayList<>();
        List<RnrLineItem> nonFullSupplyProducts = new ArrayList<>();

        fullSupplyFacilityTypeApprovedProducts = facilityApprovedProductService.getFullSupplyFacilityApprovedProductByFacilityAndProgram(reportingFacility.getId(), reportingProgram.getId());
        nonFullSupplyFacilityApprovedProducts = facilityApprovedProductService.getNonFullSupplyFacilityApprovedProductByFacilityAndProgram(reportingFacility.getId(), reportingProgram.getId());

        Collection<String> fullSupplyProductCodes = (Collection<String>) CollectionUtils.collect(fullSupplyFacilityTypeApprovedProducts, input -> ((FacilityTypeApprovedProduct) input).getProgramProduct().getProduct().getCode());
        Collection<String> nonFullSupplyProductCodes = (Collection<String>) CollectionUtils.collect(nonFullSupplyFacilityApprovedProducts, input -> ((FacilityTypeApprovedProduct) input).getProgramProduct().getProduct().getCode());

        fullSupplyProducts = report.getProducts().stream()
                .filter(p -> fullSupplyProductCodes.contains(p.getProductCode()))
                .collect(Collectors.toList());

        nonFullSupplyProducts = report.getProducts().stream()
                .filter(p -> nonFullSupplyProductCodes.contains(p.getProductCode()))
                .collect(Collectors.toList());

        for (RnrLineItem li : nonFullSupplyProducts) {
            setNonFullSupplyCreatorFields(li);
        }

        report.setProducts(fullSupplyProducts);
        report.setNonFullSupplyProducts(nonFullSupplyProducts);

        restRequisitionCalculator.validateProducts(report.getProducts(), rnr);

        markSkippedLineItems(rnr, report);

        copyRegimens(rnr, report);
        // if you have come this far, then do it, it is your day. make the submission.
        // i cannot believe we do all of these three at the same time.
        // but then this is what zambia specifically asked.
        rnr = requisitionService.save(rnr);
        report.setRnrId(rnr.getId());
        report.setResponseMessage(getResponseMessage(SUCCESS_MESSAGE));
        report.addSuccessMessage();
        requisitionService.submit(rnr);

        return errors;
    }

    private void saveReceivedRnrBeforeProcessing(Report report) {
        InterfaceResponseDTO responseDTO = new InterfaceResponseDTO();
        responseDTO.setSourceOrderId(report.getSourceOrderId());
        responseDTO.setStatus(INPUT_STATUS);
        if (requisitionService.getResponseMessageBy(report.getSourceOrderId()) == null)
            requisitionService.insertResponseMessage(responseDTO);
        else
            requisitionService.updateResponseMessage(responseDTO);
    }


    private ELMISResponseMessageDTO getResponseMessage(String errorValue) {
        return interfaceService.getResponseMessageByCode(errorValue);
    }


    @Transactional
    public Rnr submitFacilityReport(Report report, Long userId) {

        return null;
    }


    public List<HashMap<String, Object>> getSupervisionCheckListReport(String facilityCode, String programCode) {
        return requisitionService.getSupervisionCheckListReport(facilityCode, programCode);
    }

    public RequisitionStatusDTO getRequisitionStatusByRnRId(Long rnrId) {
        return requisitionService.getRequisitionStatusByRnRId(rnrId);
    }

    public List<HashMap<String, Object>> getSavedRnrStatus() {
        return requisitionService.getSavedRnrStatus();
    }

    public void updateProcessedResponseMessage(InterfaceResponseDTO dto) {
        dto.setStatus("PROCESSED");
        requisitionService.updateResponseMessage(dto);

    }

    public List<HashMap<String, Object>> getAllResponseByStatus() {
        return requisitionService.getAllResponseByStatus();
    }

    public void updateBySourceId(String sourceId) {
        requisitionService.updateBySourceId(sourceId);
    }

    public void postFeedbackNotification(String sourceOrderId) {
        ResponseExtDTO dto = requisitionService.getAllResponseByStatusBySourceID(sourceOrderId);

    }

    public Report initiateSDPReport(String facilityCode, String programCode, Long userId, Boolean emergence, String sourceApplication) {

        if (isEmpty(facilityCode) || isEmpty(programCode)) {
            throw new DataException("error.mandatory.fields.missing");
        }

        //Check if Facility Code Exists
        Facility reportingFacility = facilityService.getOperativeSdpFacilityByCode(facilityCode);

        Program reportingProgram = programService.getValidatedProgramByCode(programCode);
        if (reportingFacility != null) {
            reportingFacility.setVirtualFacility(false);

        restRequisitionCalculator.validatePeriod(reportingFacility, reportingProgram);
        }
        String sourceApp = (sourceApplication == null)?SOURCE_APPLICATION_OTHER:sourceApplication;

        Rnr rnr = requisitionService.initiate(reportingFacility, reportingProgram, userId, emergence, null, sourceApp);


        return Report.prepareForREST(rnr);

    }


    public void deleteRnR(Long rnrId) {

        requisitionService.deleteRnR(rnrId);

    }

    public List<Rnr> getRequisitionsFor(RequisitionSearchCriteria criteria, List<ProcessingPeriod> periodList) {
        return  requisitionService.getRequisitionsFor(criteria,periodList);
    }

    public List<ProcessingPeriod> getProcessingPeriods(RequisitionSearchCriteria criteria) {
        return requisitionService.getProcessingPeriods(criteria);
    }
}
