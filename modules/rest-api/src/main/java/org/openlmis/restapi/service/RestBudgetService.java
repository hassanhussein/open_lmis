package org.openlmis.restapi.service;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.ProcessingPeriod;
import org.openlmis.core.domain.Program;
import org.openlmis.core.domain.ProgramSupported;
import org.openlmis.core.dto.BudgetDTO;
import org.openlmis.core.dto.ELMISResponseMessageDTO;
import org.openlmis.core.dto.SourceOfFundDTO;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.message.OpenLmisMessage;
import org.openlmis.core.repository.BudgetLineItemRepository;
import org.openlmis.core.service.*;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.search.criteria.RequisitionSearchCriteria;
import org.openlmis.rnr.service.RequisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RestBudgetService {

    public static final String ERROR_MISSED_FIELD_VALUE = "0";
    public static final String SUCCESS_FIELD_VALUE = "1";
    @Autowired
    private FacilityService facilityService;

    @Autowired
    private BudgetLineItemRepository lineItemRepository;

    @Autowired
    private ProcessingPeriodService periodService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private ProgramSupportedService supportedService;

    @Autowired
    private ELMISInterfaceService interfaceService;

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private ProcessingScheduleService processingScheduleService;

    private void setResponseMessage(BudgetDTO dto,String message){
        dto.setResponseMessage(interfaceService.getResponseMessageByCode(message));
    }
    @Transactional
    public Map<String, OpenLmisMessage> updateBudget(BudgetDTO budgetDTO, Long userId) {
        Map<String, OpenLmisMessage> errors = null;

        setResponseMessage(budgetDTO,ERROR_MISSED_FIELD_VALUE);
        errors = budgetDTO.validateBudgetFields();
        if(!errors.isEmpty()) return errors;

        Facility facility = facilityService.getByCodeFor(budgetDTO.getFacilityCode());

        ELMISResponseMessageDTO msg = new ELMISResponseMessageDTO();

        if (facility != null) {

            BudgetDTO budget = lineItemRepository.getExistingBudget(facility.getId());

            List<ProgramSupported> programs = supportedService.getAllByFacilityId(facility.getId());

            if(!programs.isEmpty()){

                Program p  = programService.getById(programs.get(0).getProgram().getId());

                ProcessingPeriod period = processingScheduleService.getCurrentPeriodBySchedule(facility,p,programs.get(0).getStartDate());

                if(period != null){

                    try {

                        if (budget == null) {

                            budgetDTO.setProgramId(programs.get(0).getProgram().getId());
                            budgetDTO.setPeriodId(period.getId());
                            budgetDTO.setCreatedBy(userId);
                            budgetDTO.setModifiedBy(userId);
                            budgetDTO.setFacilityId(facility.getId());
                            lineItemRepository.insertBudget(budgetDTO);
                            setResponseMessage(budgetDTO, SUCCESS_FIELD_VALUE);
                            budgetDTO.setSuccess();

                        } else {

                            budgetDTO.setProgramId(programs.get(0).getProgram().getId());
                            budgetDTO.setPeriodId(period.getId());
                            budgetDTO.setCreatedBy(userId);
                            budgetDTO.setModifiedBy(userId);
                            budgetDTO.setFacilityId(facility.getId());
                            budgetDTO.setId(budget.getId());
                            lineItemRepository.updateBudget(budgetDTO);
                            setResponseMessage(budgetDTO, SUCCESS_FIELD_VALUE);
                            errors = budgetDTO.setSuccess();
                        }

                    } catch (DataException e) {
                        setResponseMessage(budgetDTO, ERROR_MISSED_FIELD_VALUE);
                        errors = budgetDTO.setFailure();
                        //Catch an Exception
                    }

                } else {

                    msg.setDescription("Facility Code have does not have current period");
                    errors = budgetDTO.setFailure();
                }


        }

        }else {
            errors = budgetDTO.validateNull(null);
        }

      return errors;
    }

    @Transactional
    public BudgetDTO saveBudget(BudgetDTO budgetDTO, Long userId, String facilityCode){
        budgetDTO.validate();

       // Facility facility = facilityService.getByCodeFor(facilityCode);


        return budgetDTO;
    }

    public void sendResponse(BudgetDTO dto, SourceOfFundDTO fund) {
        interfaceService.postBudgetToHIM(dto,fund);

    }

}
