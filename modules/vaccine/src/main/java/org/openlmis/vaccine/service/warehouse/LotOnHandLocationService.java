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
import org.openlmis.vaccine.domain.wms.LocationEntry;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.dto.*;
import org.openlmis.vaccine.dto.LocationDTO;
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

    @Autowired
    private WmsLocationService wmsLocationService;

    @Autowired
    private LocationEntryService locationEntryService;

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

        StockCard stockCard = stockCardService.getOrCreateStockCard(facility.getId(), product.getCode());

        for(PutAwayLineItemDTO dto :items) {
            total = total + dto.getQuantity();

/*
            SAVE PutAway
*/


/*
            set stock events
*/
            StockEventDTO event = new StockEventDTO();
            Lot newLot = null;
            InspectionLotDTO vvmData = null;

            if(dto.getLotNumber() != null)  {
               newLot = lotRepository.getById(lotRepository.getByCode(dto.getLotNumber()).getId());
                System.out.println("lot");
                System.out.println(dto.getLotNumber());
                System.out.println("nanaana");
                System.out.println(dto.getInspectionId());
                vvmData = repository.getByLotAndInspection(dto.getLotNumber(), dto.getInspectionId());


               //save PutAway
                dto.setCreatedBy(userId);
                dto.setLotId(newLot.getId());
                dto.setVvmId(vvmData.getVvmId());
                repository.insertPutAwayDetails(dto);

                event.setLotId(newLot.getId());
                newLot.setVvmId(vvmData.getVvmId());
                event.setLot(newLot);
                event.setVvmId(vvmData.getVvmId());

                //Prepare entry values
                LocationEntry entry = new LocationEntry();
                entry.setVvmId(vvmData.getVvmId());
                entry.setLotId(newLot.getId());
                entry.setStockCardId(stockCard.getId());
                entry.setQuantity(dto.getQuantity());
                entry.setLocationId(dto.getToBinLocationId());
                entry.setCreatedBy(userId);
                entry.setModifiedBy(userId);
                entry.setType(StockCardEntryType.CREDIT);
                entry.setIsTransferred(false);

                List<StockCardEntryKV> vl = new ArrayList<>();
                StockCardEntryKV values = new StockCardEntryKV();
                values.setKeyColumn("receivedfrom");
                LocationDTO dto2 = wmsLocationService.getByLocationId(dto.getFromBinLocationId());
                values.setValueColumn(dto2.getName());
                vl.add(values);
                entry.setKeyValues(vl);

                locationEntryService.saveLocationEntry(entry);

            } else {
                event.setLot(null);
            }

            event.setType(StockEventType.PUTAWAY);
            event.setFacilityId(facility.getId());
            event.setProductCode(product.getCode());
            event.setQuantity(Long.valueOf(dto.getQuantity()));

            if(vvmData != null)
             customProps.put("vvmStatus", vvmData.getVvmId().toString());
            else
             customProps.put("vvmStatus", "1");

            event.setCustomProps(customProps);
            event.setToBinLocationId(dto.getToBinLocationId());
            event.setFromBinLocationId(dto.getFromBinLocationId());

            events.add(event);

        }

       /* StockCard stockCard = new StockCard();
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
        processStockCard(facility, stockCard,product,events,userId);*/
        inspectionService.updateStatus("INSPECTED", items.get(0).getInspectionId());

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
            Long vvmId = event.getLot().getVvmId();

            LotOnHand lotOnHand = stockCardService.getLotOnHandWithVvmStatus(vvmId, lotId, lotObj, productCode, card, str);
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
            location.setQuantityOnHand(event.getQuantity());
            location.setLotOnHandId(l.getId());
            location.setLocationId(event.getToBinLocationId());
            location.setFromBinLocationId(event.getFromBinLocationId());
            repository.deleteExistingByLot(lotOnHand.getId(),event.getToBinLocationId());

            //Insert Lot locations
            repository.insert(location);

            Date occurred = event.getOccurred();
            String referenceNumber  = event.getReferenceNumber();

            StockCardEntry entry = new StockCardEntry(card, entryType, event.getQuantity(), occurred, referenceNumber);
            lotOnHand.setVvmId(event.getLot().getVvmId());
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

            //save Entries
       /*     LocationEntry locationEntry = new LocationEntry();
            locationEntry.setCreatedBy(userId);
            locationEntry.setModifiedBy(userId);
            locationEntry.setLocationId(event.getFromBinLocationId());
            locationEntry.setLotOnHandId(onHand);
            locationEntry.setQuantity(Math.toIntExact(event.getQuantity()));
            locationEntry.setType(StockCardEntryType.DEBIT);

            List<StockCardEntryKV> vl = new ArrayList<>();
            StockCardEntryKV values = new StockCardEntryKV();
            values.setKeyColumn("issuedto");
            LocationDTO dto2 = wmsLocationService.getByLocationId(event.getToBinLocationId());
            values.setValueColumn(dto2.getName());
            vl.add(values);
            locationEntry.setKeyValues(vl);
            locationEntryService.saveLocationEntry(locationEntry);*/

            LocationEntry entry2 = new LocationEntry();
            entry2.setCreatedBy(userId);
            entry2.setModifiedBy(userId);
            entry2.setType(StockCardEntryType.CREDIT);
            entry2.setLotOnHandId(lotOnHand.getId());
            entry2.setLocationId(event.getToBinLocationId());
            entry2.setQuantity(Math.toIntExact(event.getQuantity()));

            List<StockCardEntryKV> vl2 = new ArrayList<>();
            StockCardEntryKV values2 = new StockCardEntryKV();
            values2.setKeyColumn("receivedfrom");
            LocationDTO dto3 = wmsLocationService.getByLocationId(event.getFromBinLocationId());
            values2.setValueColumn(dto3.getName());
            vl2.add(values2);
            entry2.setKeyValues(vl2);

            locationEntryService.saveLocationEntry(entry2);




        }
        stockCardService.addStockCardEntries(entries);
        return "success.stock.adjusted";
    }

    public List<SohReportDTO> getSOHReport(Long facilityId, Long warehouseId) {
        return repository.getSOHReport(facilityId, warehouseId);
    }

    public List<HashMap<String, Object>> getAllLedgers(Long productId,Long warehouseId,  Long year) {
        return repository.getAllLedgers(productId,warehouseId,year);
    }

    public List<HashMap<String,Object>>getAllByWareHouseAndBinLocation(Long fromWarehouseId, Long fromBinLocationId){
        return repository.getAllByWareHouseAndBinLocation(fromWarehouseId,fromBinLocationId);
    }

    public List<TransferDTO>getTransferDetailsBy(Long wareHouseId, Long fromBinLocationId) {
        return repository.getTransferDetailsBy(wareHouseId,fromBinLocationId);
    }

    public void updateByLotOnHandAndLocation(Integer total, Long fromBin, Long lotOnHandId) {
        repository.updateByLotOnHandAndLocation(total,fromBin,lotOnHandId);
    }

    public LotOnHandLocation getBy(Long fromBin, Long lotOnHandId) {
        return repository.getBy(fromBin, lotOnHandId);
    }

    public void updateLotOnHandLocation(Long id, Integer quantity) {
        repository.updateLotOnHandLocation(id, quantity);
    }

    public List<StockCardDTO> getStockCardWithLocationBy(Long  facilityId) {
        return repository.getStockCardWithLocationBy(facilityId);
    }


}
