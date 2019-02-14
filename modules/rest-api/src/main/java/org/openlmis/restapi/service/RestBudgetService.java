package org.openlmis.restapi.service;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.dto.BudgetDTO;
import org.openlmis.core.service.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestBudgetService {

    @Autowired
    private FacilityService facilityService;

    @Transactional
    public void updateBudget(BudgetDTO budgetDTO, Long userId, String facilityCode) {

        Facility facility = facilityService.getByCodeFor(facilityCode);

        if(facility != null){

        }

    }

    @Transactional
    public BudgetDTO saveBudget(BudgetDTO budgetDTO, Long userId, String facilityCode){
        budgetDTO.validate();

       // Facility facility = facilityService.getByCodeFor(facilityCode);


        return budgetDTO;
    }
}
