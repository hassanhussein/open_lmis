package org.openlmis.vaccine.service.warehouse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
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

import java.io.IOException;
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

    public void save(LotOnHandLocation location) {

        if (location.getId() == null) {
            repository.insert(location);
        } else {
            repository.update(location);
        }
    }

    @Transactional
    public PutAwayLineItemDTO savePutAwayDetails(List<PutAwayLineItemDTO> items, Long userId) {

        repository.deleteExistingPutAway(items.get(0).getInspectionId());
        Long total = 0L;
        List<StockEventDTO> events = new ArrayList<>();
        Map<String, String> customProps = new HashMap<String, String>();


        Facility facility = facilityService.getAllByFacilityTypeCode(CVS_CODE).get(0);
        Product product = productService.getById(items.get(0).getProductId());

        StockCard stockCard = stockCardService.getOrCreateStockCard(facility.getId(), product.getCode());

        for (PutAwayLineItemDTO dto : items) {
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

            if (dto.getLotNumber() != null) {
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

            if (vvmData != null)
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

        System.out.println("Inspection ID" + items.get(0).getInspectionId());
        System.out.println("----Processed Stock cards-----");


        return items.get(0);
    }

    private String processStockCard(Facility facility, StockCard card, Product product, List<StockEventDTO> events, Long userId) {

        if (null == events || events.isEmpty()) {
            return "Empty Events";
        }

        if (facility == null) {
            return " Facility is not available ";
        }

        List<StockCardEntry> entries = new ArrayList<>();

        for (StockEvent event : events) {
            logger.debug("Processing event: " + event);

            if (null == event.getProductCode() && event.getQuantity() < 0) {
                return "Quantity of product is not valid";
            }

            String productCode = event.getProductCode();
            StockCardLocationDTO stockCardLocationDTO = new StockCardLocationDTO();
            if (event.getLot() == null) {
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

            LotOnHand l = stockCardService.getLotOnHandByStockCardAndLot(card.getId(), event.getLotId());

            LotOnHandLocation location = new LotOnHandLocation();
            location.setCreatedBy(userId);
            location.setModifiedBy(userId);
            location.setQuantityOnHand(event.getQuantity());
            location.setLotOnHandId(l.getId());
            location.setLocationId(event.getToBinLocationId());
            location.setFromBinLocationId(event.getFromBinLocationId());
            repository.deleteExistingByLot(lotOnHand.getId(), event.getToBinLocationId());

            //Insert Lot locations
            repository.insert(location);

            Date occurred = event.getOccurred();
            String referenceNumber = event.getReferenceNumber();

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

    public List<HashMap<String, Object>> getAllLedgers(Long productId, Long warehouseId, Long year) throws IOException {
        List<HashMap<String, Object>> ledgers = repository.getAllLedgers(productId, warehouseId, year);
        JSONArray jsonLedger = new JSONArray(ledgers);

        JSONArray selectedJsonYear = new JSONArray();

        JSONArray previousYear = new JSONArray();

        Map<String, Integer> uniqueLedgerItem = new HashMap<String, Integer>();

        //  selectedJsonYear.put("{}");

        for (int i = 0; i < jsonLedger.length(); i++) {
            //JSONObject jsonObject=jsonLedger.get(i);
            JSONObject ledgerObject = jsonLedger.getJSONObject(i);
            String locationname = ledgerObject.getString("locationname");
            if (ledgerObject.has("transferlogs")) {
                String transferlogs = ledgerObject.getString("transferlogs");

                String[] arrayLogs = transferlogs.split("-");

                if (arrayLogs.length == 2) {
                    String fromBin = arrayLogs[0];
                    String toBin = arrayLogs[1];

                    /// System.out.println(fromBin+" :hh: "+toBin);

                    ledgerObject.put("frombin", fromBin);
                    ledgerObject.put("toBinCustom", toBin);


                } else {
                    String fromBin = arrayLogs[0];

                    ledgerObject.put("frombin", fromBin);


                }

                //System.out.println(transferlogs);
            } else {
                ledgerObject.put("toBinCustom", locationname);

            }
            Long lotYear = ledgerObject.getLong("lotyear");

            Integer loh = ledgerObject.getInt("loh");


            if (lotYear.equals(year)) {
                selectedJsonYear.put(ledgerObject);
            } else {
                if (lotYear < year) {
                    previousYear.put(ledgerObject);
                }
            }


            String keyValue = ledgerObject.getString("lotnumber") + "/" + ledgerObject.getString("locationname") + "/" + ledgerObject.getString("vvm") + "/" + ledgerObject.getString("expirationdate") + "/" + lotYear;

            if (uniqueLedgerItem.containsKey(keyValue)) {
                int totalInHand = uniqueLedgerItem.get(keyValue) + loh;
                uniqueLedgerItem.put(keyValue, totalInHand);

            } else {
                uniqueLedgerItem.put(keyValue, loh);

            }

        }


        JSONArray previousYearBalance = new JSONArray();


        for (Map.Entry m : uniqueLedgerItem.entrySet()) {

            String ledgerKey = m.getKey().toString();
            //uniqueLedgerItem.put(ledgerKey,0);
            Integer valueLedger = Integer.parseInt(m.getValue().toString());
            String[] arrayValue = ledgerKey.split("/");
            String lotnumber = arrayValue[0];
            String locationname = arrayValue[1];
            String vvm = arrayValue[2];
            String expirationdate = arrayValue[3];
            Long yearDate = Long.parseLong(arrayValue[4]);


            JSONObject newPreviousLedgerObject = new JSONObject();
            newPreviousLedgerObject.put("frombin", locationname);

            //newPreviousLedgerObject.put("toBinCustom",locationname);
            newPreviousLedgerObject.put("date", year + "-01-01");

            newPreviousLedgerObject.put("lotnumber", lotnumber);
            newPreviousLedgerObject.put("locationname", locationname);
            newPreviousLedgerObject.put("vvm", vvm);
            newPreviousLedgerObject.put("expirationdate", expirationdate);

            if (yearDate < year) {
                newPreviousLedgerObject.put("soh", valueLedger);
                newPreviousLedgerObject.put("loh", 0);
            } else {
                newPreviousLedgerObject.put("soh", 0);
                newPreviousLedgerObject.put("loh", 0);
            }

            // System.out.println(valueLedger+" donne "+ledgerKey);


            int totalPrevious = 0;
            for (int k = 0; k < previousYear.length(); k++) {
                JSONObject ledgerObject = previousYear.getJSONObject(k);
                int loh = ledgerObject.getInt("total");
                totalPrevious = totalPrevious + loh;
            }
            newPreviousLedgerObject.put("total", totalPrevious);

            previousYearBalance.put(newPreviousLedgerObject);

            //System.out.println(m.getKey()+" "+m.getValue());
        }


        ObjectMapper mapper = new ObjectMapper();

        JSONArray sortedLedger = sortArrayList(previousYearBalance, selectedJsonYear);

        List<HashMap<String, Object>> convertLedgerObject = mapper.readValue(sortedLedger.toString(),
                new TypeReference<List<HashMap<String, Object>>>() {
                });

        return convertLedgerObject;
    }

    private JSONArray sortArrayList(JSONArray previousLedger, JSONArray selectedLedger) {
        if (selectedLedger.length() != 0) {
            for (int i = 0; i < selectedLedger.length(); i++) {
                JSONObject ledgerObject = selectedLedger.getJSONObject(i);

                String date = ledgerObject.getString("date");
                ledgerObject.put("date", date);

                previousLedger.put(ledgerObject);
            }

            int loh = 0;
            int totalCount = previousLedger.length();

            Map<String, Integer> mapLoh = new HashMap<String, Integer>();


            for (int l = 0; l < previousLedger.length(); l++) {

                JSONObject ledgerObject = previousLedger.getJSONObject(l);
                String lotNumber = ledgerObject.getString("lotnumber") + "/" + ledgerObject.getString("locationname");
                if (mapLoh.containsKey(lotNumber)) {
                    loh = mapLoh.get(lotNumber);
                }


                int received = 0;
                if (ledgerObject.has("received")) {
                    received = ledgerObject.getInt("received");
                }

                String type = "CREDIT";

                int issued = 0;
                if (ledgerObject.has("issued")) {
                    issued = ledgerObject.getInt("issued");
                }

                if (ledgerObject.has("type")) {
                    type = ledgerObject.getString("type");
                }


                int adjustment = 0;
                if (ledgerObject.has("adjustment")) {
                    adjustment = ledgerObject.getInt("adjustment");
                }
                if (totalCount > l + 1) {
                    JSONObject nextLedgerObject = previousLedger.getJSONObject(l + 1);

                    if (issued > 0) {

                        int nextObject = nextLedgerObject.getInt("received");
                        String lotNumberNext = nextLedgerObject.getString("lotnumber");
                        if (nextObject == issued && lotNumberNext.equals(lotNumber)) {
                            System.out.println("Equals");
                            issued = 0;
                        } else {
                            if (type.equals("DEBIT")) {
                                issued = issued * -1;
                            }
                            //   System.out.println("Not Equals"+nextObject+":"+received+" - "+lotNumberNext+" "+lotNumber);
                        }

                        loh = loh + received + issued + adjustment;
                    } else {
                        if (l > 0) {
                            JSONObject prevLedgerObject = previousLedger.getJSONObject(l - 1);
                            int prevObjectIssued = 0;
                            if (prevLedgerObject.has("issued")) {
                                prevObjectIssued = prevLedgerObject.getInt("issued");
                            }

                            String lotNumberPrev = prevLedgerObject.getString("lotnumber");
                            if (prevObjectIssued > 0) {
                                if (prevObjectIssued == received && lotNumberPrev.equals(lotNumber)) {
                                    received = 0;
                                }
                            }
                        }


                        loh = loh + received + issued + adjustment;
                    }
                } else {
                    if (l > 0) {
                        JSONObject prevLedgerObject = previousLedger.getJSONObject(l - 1);
                        int prevObjectIssued = 0;
                        if (prevLedgerObject.has("issued")) {
                            prevObjectIssued = prevLedgerObject.getInt("issued");
                        }
                        String lotNumberPrev = prevLedgerObject.getString("lotnumber");
                        if (prevObjectIssued > 0) {
                            if (prevObjectIssued == received && lotNumberPrev.equals(lotNumber)) {
                                received = 0;
                            }
                        }
                    }

                    if (type.equals("DEBIT") && issued > 0) {
                        issued = issued * -1;
                    }


                    loh = loh + received + issued + adjustment;
                }


                mapLoh.put(lotNumber, loh);

                ledgerObject.put("loh", loh);
                //totalCount++;
            }

            return previousLedger;
        } else {
            return null;
        }
    }

    public List<HashMap<String, Object>> getAllByWareHouseAndBinLocation(Long fromWarehouseId, Long fromBinLocationId) {
        return repository.getAllByWareHouseAndBinLocation(fromWarehouseId, fromBinLocationId);
    }

    public List<TransferDTO> getTransferDetailsBy(Long wareHouseId, Long fromBinLocationId) {
        return repository.getTransferDetailsBy(wareHouseId, fromBinLocationId);
    }

    public void updateByLotOnHandAndLocation(Integer total, Long fromBin, Long lotOnHandId) {
        repository.updateByLotOnHandAndLocation(total, fromBin, lotOnHandId);
    }

    public LotOnHandLocation getBy(Long fromBin, Long lotOnHandId) {
        return repository.getBy(fromBin, lotOnHandId);
    }

    public void updateLotOnHandLocation(Long id, Integer quantity) {
        repository.updateLotOnHandLocation(id, quantity);
    }

    public List<StockCardDTO> getStockCardWithLocationBy(Long facilityId) {
        return repository.getStockCardWithLocationBy(facilityId);
    }


}
