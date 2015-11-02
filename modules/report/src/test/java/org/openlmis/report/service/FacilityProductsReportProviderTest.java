package org.openlmis.report.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.GeographicLevel;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.domain.Product;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.GeographicZoneService;
import org.openlmis.db.categories.UnitTests;
import org.openlmis.report.model.dto.FacilityProductReportEntry;
import org.openlmis.stockmanagement.domain.StockCard;
import org.openlmis.stockmanagement.domain.StockCardEntry;
import org.openlmis.stockmanagement.service.StockCardService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@Category(UnitTests.class)
@RunWith(MockitoJUnitRunner.class)
public class FacilityProductsReportProviderTest {

    @Mock
    FacilityService facilityService;

    @Mock
    StockCardService stockCardService;

    @Mock
    GeographicZoneService geographicZoneService;

    @InjectMocks
    FacilityProductsReportDataProvider facilityProductsReportDataProvider;

    @Before()
    public void setup(){
    }

    @Test
    public void shouldGetSpecificProductDataForAllFacilityInGeographicZone(){

        List<Facility> facilities = new ArrayList<>();
        Facility facility = new Facility();
        facility.setId(1L);
        facilities.add(facility);

        List<StockCard> stockCards = new ArrayList<>();
        StockCard stockCard = new StockCard();

        Product product = new Product();
        product.setId(1L);
        product.setPrimaryName("Product Test Name");
        stockCard.setProduct(product);
        List<StockCardEntry> entries = new ArrayList<>();
        StockCardEntry stockCardEntry = new StockCardEntry();
        stockCardEntry.setQuantity(100L);
        stockCardEntry.setCreatedDate(new Date());

        entries.add(stockCardEntry);
        stockCard.setEntries(entries);
        stockCards.add(stockCard);

        when(stockCardService.getStockCards(1L)).thenReturn(stockCards);
        List<FacilityProductReportEntry> entryList = facilityProductsReportDataProvider.fillReportEntryList(1L, null, facilities);

        assertThat(entryList.size(), is(1));
        assertThat(entryList.get(0).getProductQuantity(), is(100L));
        assertThat(entryList.get(0).getProductName(), containsString("Product Test Name"));
    }

    @Test
    public void shouldReturnTrueIfFacilityInGeographicZone() {
        GeographicZone district = new GeographicZone();
        district.setCode("District");
        district.setLevel(new GeographicLevel(1L, FacilityProductsReportDataProvider.DISTRICT_CODE, "District", 5));

        GeographicZone province = new GeographicZone();
        province.setCode("Maputo");
        province.setLevel(new GeographicLevel(2L, FacilityProductsReportDataProvider.PROVINCE_CODE, "Province", 6));

        district.setParent(province);

        Facility facility = new Facility();
        facility.setGeographicZone(district);

        assertThat(FacilityProductsReportDataProvider.inGeographicZone(district, facility), is(true));
        assertThat(FacilityProductsReportDataProvider.inGeographicZone(province, facility), is(true));
    }
}