package org.openlmis.restapi.service;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.dto.BudgetDTO;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.message.OpenLmisMessage;
import org.openlmis.core.repository.BudgetLineItemRepository;
import org.openlmis.core.service.BudgetLineItemService;
import org.openlmis.core.service.ELMISInterfaceService;
import org.openlmis.core.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ELMISInterfaceService interfaceService;

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
        if (facility != null) {

        BudgetDTO budget = lineItemRepository.getExistingBudget(facility.getId());
        try{

        if(budget == null){
            budgetDTO.setCreatedBy(userId);
            budgetDTO.setModifiedBy(userId);
            budgetDTO.setFacilityId(facility.getId());
            lineItemRepository.insertBudget(budgetDTO);
            setResponseMessage(budgetDTO,SUCCESS_FIELD_VALUE);
            budgetDTO.setSuccess();

        }else {

            budgetDTO.setCreatedBy(userId);
            budgetDTO.setModifiedBy(userId);
            budgetDTO.setFacilityId(facility.getId());
            budgetDTO.setId(budget.getId());
            lineItemRepository.updateBudget(budgetDTO);
            setResponseMessage(budgetDTO,SUCCESS_FIELD_VALUE);
            errors = budgetDTO.setSuccess();
        }

        } catch (DataException e){
            setResponseMessage(budgetDTO,ERROR_MISSED_FIELD_VALUE);
           errors = budgetDTO.setFailure();
            //Catch an Exception
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
}
