package org.openlmis.vaccine.service.warehouse;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mock;
import org.openlmis.core.builder.FacilityBuilder;
import org.openlmis.core.builder.ProductBuilder;
import org.openlmis.core.builder.ProgramBuilder;
import org.openlmis.core.builder.ProgramProductBuilder;
import org.openlmis.core.domain.*;
import org.openlmis.core.repository.FacilityRepository;
import org.openlmis.core.service.ProductService;
import org.openlmis.core.service.ProgramService;
import org.openlmis.db.categories.UnitTests;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.domain.StockCard;
import org.openlmis.stockmanagement.domain.StockCardEntry;
import org.openlmis.stockmanagement.domain.StockCardEntryType;
import org.openlmis.stockmanagement.dto.StockEvent;
import org.openlmis.stockmanagement.dto.StockEventType;
import org.openlmis.stockmanagement.repository.LotRepository;
import org.openlmis.stockmanagement.repository.StockCardRepository;
import org.openlmis.stockmanagement.service.StockCardService;
import org.openlmis.vaccine.domain.wms.dto.StockEventDTO;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;


import java.util.*;


import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Category(UnitTests.class)
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(BlockJUnit4ClassRunner.class)
public class ReceiveServiceTest {

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ProgramService programService;

    @Mock
    private StockCardService stockCardService;
    @Mock
    private StockCardRepository stockCardRepository;

    @Mock
    private LotRepository lotRepository;




    private static final long USER_ID = 1L;
    private static final Facility defaultFacility;
    private static final Product defaultProduct;
    private static final ProgramProduct defaultProgramProduct;
    private static final Program defaultProgram;
    private static final StockCard dummyCard;
    private static final MockHttpServletRequest request = new MockHttpServletRequest();
    private static final MockHttpSession session = new MockHttpSession();


    private long fId;
    private String pCode;
    private String reasonName;
    private StockAdjustmentReason reason;
    private StockEventDTO event;

    private ReceiveService receiveService;



    static  {
        defaultFacility = make(a(FacilityBuilder.defaultFacility, with(FacilityBuilder.facilityId, 1L)));
        defaultProduct = make(a(ProductBuilder.defaultProduct, with(ProductBuilder.code, "valid_code")));
        defaultProgramProduct = make(a(ProgramProductBuilder.defaultProgramProduct));
        defaultProgram = make(a(ProgramBuilder.defaultProgram, with(ProgramBuilder.programId, 1L)));
        dummyCard = StockCard.createZeroedStockCard(defaultFacility, defaultProduct);
    }

    public void setupEvent() {
        fId = 1L;
        pCode ="valid_code";
        reasonName = "dummyReason";

        reason = new StockAdjustmentReason();
        reason.setAdditive(false);
        reason.setName(reasonName);

        event = new StockEventDTO();
        event.setProductCode(pCode);
        event.setType(StockEventType.RECEIPT);
//    event.setFacilityId(fId);
        event.setReasonName(reasonName);
        event.setQuantity(10L);
    }


    @Before
    public void setup() {
        receiveService =  new ReceiveService (facilityRepository,
                productService,
                lotRepository,
                programService,
                stockCardService);
    }



    public Lot setupLot(Long id)
    {
        Lot lot = new Lot();
        lot.setId(id);
        lot.setProduct(defaultProduct);
        lot.setLotCode("code_" + id);
        lot.setManufacturerName("Manufacturer_of_" + id);
        lot.setManufactureDate(new Date());
        lot.setExpirationDate(new Date());
        //event.setLot(lot);
        return lot;
    }
    public void setupGetStockCardCalls() {
        when(stockCardRepository.getStockCardByFacilityAndProduct(any(Long.class), any(String.class))).thenReturn(dummyCard);
        when(stockCardService.getStockCardById(any(Long.class), any(Long.class))).thenReturn(dummyCard);
        when(stockCardService.getStockCards(any(Long.class))).thenReturn(new LinkedList<>(Collections.singletonList(dummyCard)));
    }

    @Test
    public void shouldReturnMessageWhenStockEventIsNotSpecified() {
        List<StockEventDTO> events = Collections.emptyList();
        long facilityId = 1;
        long userId = 1L;

        String stock = receiveService.processStockCard(facilityId, events,userId);

        assertThat(stock, not(isEmptyString()));

        //assertFalse(stock.isEmpty());
    }

    @Test
    public void shouldSucceedWithValidStockEvent() {
        setupEvent();
        Long  fId = 1L;
        // test
        when(facilityRepository.getById(fId)).thenReturn(defaultFacility);
        when(productService.getByCode(pCode)).thenReturn(defaultProduct);
        when(lotRepository.getLotOnHandByStockCardAndLot(eq(dummyCard.getId()), any(Long.class))).thenReturn(null);
        List<StockEventDTO> actual = new ArrayList();
        StockEventDTO dto= new StockEventDTO();
        dto.setFacilityId(fId);
        dto.setQuantity(100L);
        dto.setProductCode(pCode);
        dto.setType(StockEventType.RECEIPT);
        actual.add(dto);
        String stock =  receiveService.processStockCard(fId, actual, 1L);
        assertFalse(stock.isEmpty());

        /*ResponseEntity response = controller.processStock(fId, Collections.singletonList(event), request);

        // verify
        StockCardEntry entry = new StockCardEntry(dummyCard, StockCardEntryType.CREDIT, event.getQuantity() * -1, null, null);
        entry.setAdjustmentReason(reason);
        verify(stockCardService).addStockCardEntries(Collections.singletonList(entry));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));*/
    }

}
