package org.openlmis.vaccine.service.warehouse;

import org.apache.log4j.Logger;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Product;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.ProductService;
import org.openlmis.stockmanagement.domain.*;
import org.openlmis.stockmanagement.dto.StockEvent;
import org.openlmis.stockmanagement.dto.StockEventType;
import org.openlmis.stockmanagement.repository.LotRepository;
import org.openlmis.stockmanagement.service.StockCardService;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.dto.PutAwayLineItemDTO;
import org.openlmis.vaccine.domain.wms.dto.StockCardLocationDTO;
import org.openlmis.vaccine.domain.wms.dto.StockEventDTO;
import org.openlmis.vaccine.repository.warehouse.LotOnHandLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LotOnHandLocationService {
    private static Logger logger = Logger.getLogger(LotOnHandLocationService.class);
    private static final String CVS_CODE = "cvs";
    @Autowired
    private LotOnHandLocationRepository repository;

    @Autowired
    private StockCardService stockCardService;

    @Autowired
    private ProductService productService;

    @Autowired
    FacilityService facilityService;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private InspectionService inspectionService;

    public void save(LotOnHandLocation location){

        if(location.getId() == null){
           repository.insert(location);
        } else {
            repository.update(location);
        }
    }

    @Transactional
    public PutAwayLineItemDTO savePutAwayDetails(List<PutAwayLineItemDTO> items,Long userId) {

       repository.deleteExistingPutAway(items.get(0).getInspectionId());
       Long total = 0L;
       List<StockEventDTO> events = new ArrayList<>();
       Map<String, String> customProps = new HashMap<String, String>();


        Facility facility = facilityService.getAllByFacilityTypeCode(CVS_CODE).get(0);
        Product product = productService.getById(items.get(0).getProductId());

        StockCard stock = stockCardService.getStockCardByFacilityAndProduct(facility.getId(),product.getCode());

        for(PutAwayLineItemDTO dto :items) {
            total = total + dto.getQuantity();

/*
            SAVE PutAway
*/
            dto.setCreatedBy(userId);
            repository.insertPutAwayDetails(dto);

/*
            set stock events
*/
            StockEventDTO event = new StockEventDTO();
            Lot newLot = null;
            if(dto.getLotNumber() != null)  {
               newLot = lotRepository.getById(lotRepository.getByCode(dto.getLotNumber()).getId());
                event.setLotId(newLot.getId());
                event.setLot(newLot);
            } else {
                event.setLot(null);
            }

            event.setType(StockEventType.PUTAWAY);
            event.setFacilityId(facility.getId());
            event.setProductCode(product.getCode());
            event.setQuantity(Long.valueOf(dto.getQuantity()));
            customProps.put("vvmStatus", "1");

            event.setCustomProps(customProps);
            event.setToBinLocationId(dto.getToBinLocationId());

            events.add(event);

        }

        StockCard stockCard = new StockCard();
        stockCard.setFacility(facility);
        stockCard.setProduct(product);
        stockCard.setTotalQuantityOnHand(total);
        stockCard.setNotes("PUTAWAY");
        stockCard.setEffectiveDate(new Date());
        stockCard.setCreatedBy(userId);
        stockCard.setModifiedBy(userId);

        if(stock == null) {
            stockCardService.insertStockCard(stockCard);
        } else {
            stockCard.setId(stock.getId());
            stockCardService.updateStockCard(stockCard);
        }

        processStockCard(facility, stockCard,product,events,userId);
        inspectionService.updateStatus("FINALIZED", items.get(0).getInspectionId());

        System.out.println("Inspection ID"+items.get(0).getInspectionId());
        System.out.println("----Processed Stock cards-----");


       return items.get(0);
    }

    private String processStockCard(Facility facility, StockCard card,Product product , List<StockEventDTO> events, Long userId) {

        if(null == events || events.isEmpty()) {
            return "Empty Events";
        }

        if(facility == null) {
            return " Facility is not available ";
        }

        List<StockCardEntry> entries = new ArrayList<>();

        for(StockEvent event : events) {
            logger.debug("Processing event: " + event);

            if(null == event.getProductCode() && event.getQuantity() < 0) {
                return "Quantity of product is not valid";
            }

            String productCode = event.getProductCode();
            StockCardLocationDTO stockCardLocationDTO =new StockCardLocationDTO();
            if(event.getLot() == null) {
                stockCardLocationDTO.setStockCardId(card.getId());
                stockCardLocationDTO.setLocationId(event.getToBinLocationId());
                stockCardLocationDTO.setCreatedBy(userId);
                stockCardLocationDTO.setModifiedBy(userId);
                repository.deleteExistingStockCardLocation(card.getId(), event.getToBinLocationId());
                repository.insertLocationsWIthoutLots(stockCardLocationDTO);
            }

            // get or create lot, if lot is being used
            StringBuilder str = new StringBuilder();
            Long lotId = event.getLotId();
            Lot lotObj = event.getLot();
            LotOnHand lotOnHand = stockCardService.getLotOnHandFor(lotId, lotObj, productCode, card, str);
            if (!str.toString().equals("")) {
                return "Lot Not created";
            }

            // create entry from event
            StockCardEntryType entryType = StockCardEntryType.CREDIT;

            Long onHand = (null != lotObj) ? lotOnHand.getQuantityOnHand() : card.getTotalQuantityOnHand();
            if (!event.isValidIssueQuantity(onHand)) {
                return "error.stock.quantity.invalid";
            }

            LotOnHand l = stockCardService.getLotOnHandByStockCardAndLot(card.getId(),event.getLotId());

            LotOnHandLocation location = new LotOnHandLocation();
            location.setCreatedBy(userId);
            location.setModifiedBy(userId);
            location.setQuantityOnHand(onHand.intValue());
            location.setLotOnHandId(l.getId());
            location.setLocationId(event.getToBinLocationId());
            repository.deleteExistingByLot(lotOnHand.getId(),event.getToBinLocationId());

            //Insert Lot locations
            repository.insert(location);

            Date occurred = event.getOccurred();
            String referenceNumber  = event.getReferenceNumber();

            StockCardEntry entry = new StockCardEntry(card, entryType, event.getQuantity(), occurred, referenceNumber);
            entry.setLotOnHand(lotOnHand);
            Map<String, String> customProps = event.getCustomProps();
            if (null != customProps) {
                for (String k : customProps.keySet()) {
                    entry.addKeyValue(k, customProps.get(k));
                }
            }
            entry.setCreatedBy(userId);
            entry.setModifiedBy(userId);
            entries.add(entry);

        }

        stockCardService.addStockCardEntries(entries);
        return "success.stock.adjusted";
    }

}
