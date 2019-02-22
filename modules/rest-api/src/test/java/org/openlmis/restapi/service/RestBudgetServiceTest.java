package org.openlmis.restapi.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.dto.BudgetDTO;
import org.openlmis.core.dto.BudgetLineItemDTO;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.FacilityService;
import org.openlmis.db.categories.UnitTests;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;

import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.spy;



@Category(UnitTests.class)
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(BlockJUnit4ClassRunner.class)
public class RestBudgetServiceTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private Facility FACILITY = new Facility(1L);
    @InjectMocks
    RestBudgetService budgetService;

    @InjectMocks
    FacilityService facilityService;

    @Test
    public void shouldSaveBudget() throws Exception{
        FACILITY.setCode("30D");
        String facilityCode = "100L";


        BudgetLineItemDTO lineItemDTO = new BudgetLineItemDTO("", "P10", "10-12-2013", "32.67", "Notes","Test12",true,
                "123",1L,2L,3L,4L,5L);

        expectedEx.expect(DataException.class);
        expectedEx.expectMessage("error.mandatory.fields.missing");


        lineItemDTO.checkMandatoryFields();

        BudgetDTO budgetDTO = new BudgetDTO();
        budgetDTO.setFacilityCode("F19");
        budgetDTO.setPeriodStartDate("2018-02-01");
        budgetDTO.setProgramCode("ILS");
        budgetDTO.setLineItem(asList(lineItemDTO));

        BudgetDTO spyBudgetDTO = spy(budgetDTO);

        doNothing().when(spyBudgetDTO).validate();
        budgetService.saveBudget(budgetDTO,1L,facilityCode);

    }

    @Test
    public void shouldUpdateBudget(){


    }

}
