package org.openlmis.restapi.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openlmis.core.dto.BudgetDTO;
import org.openlmis.db.categories.UnitTests;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.restapi.service.RestBudgetService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.openlmis.restapi.response.RestResponse.SUCCESS;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.springframework.http.HttpStatus.OK;

@Category(UnitTests.class)
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(BlockJUnit4ClassRunner.class)
@PrepareForTest(RestResponse.class)
public class RestBudgetControllerTest {

    @Mock
    RestBudgetService restBudgetService;


    @Mock
    Principal principal;

    @InjectMocks
    private RestBudgetController controller;

    @Before
    public void setUp() throws Exception {
        mockStatic(RestResponse.class);
    }

    @Test
    public void shouldSaveBudget() throws Exception {
        BudgetDTO budgetDTO = new BudgetDTO();
        String facilityCode ="123";
        when(principal.getName()).thenReturn("2");
        when(restBudgetService.saveBudget(budgetDTO, 2L,facilityCode)).thenReturn(new BudgetDTO());
        ResponseEntity<RestResponse> response = new ResponseEntity<>(new RestResponse(SUCCESS, "success"), OK);
        PowerMockito.when(success("message.success.budget.updated")).thenReturn(response);

    }

}
