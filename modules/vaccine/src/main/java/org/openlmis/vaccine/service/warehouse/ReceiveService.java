package org.openlmis.vaccine.service.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.Program;
import org.openlmis.core.repository.FacilityRepository;
import org.openlmis.core.service.ProductService;
import org.openlmis.core.service.ProgramService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.stockmanagement.domain.*;
import org.openlmis.stockmanagement.dto.StockEvent;
import org.openlmis.stockmanagement.dto.StockEventType;
import org.openlmis.stockmanagement.repository.LotRepository;
import org.openlmis.stockmanagement.service.StockCardService;
import org.openlmis.vaccine.domain.wms.*;
import org.openlmis.vaccine.domain.wms.dto.StockEventDTO;
import org.openlmis.vaccine.repository.warehouse.ReceiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import org.apache.log4j.Logger;

@Service
@NoArgsConstructor
public class ReceiveService {
    private static Logger logger = Logger.getLogger(ReceiveService.class);
    @Autowired
    private ReceiveRepository repository;

    @Autowired
    private ReceiveLineItemService lineItemService;

    @Autowired
    private ReceiveLotService lotService;

    @Autowired
    private PurchaseDocumentService purchaseDocumentService;

    @Autowired
    private StockCardService stockCardService;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    ProductService productService;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    ProgramService programService;



    ReceiveService(FacilityRepository facilityRepository, ProductService productService, LotRepository lotRepository, ProgramService programService, StockCardService stockCardService) {

        this.facilityRepository = Objects.requireNonNull(facilityRepository);
        this.productService = Objects.requireNonNull(productService);
        this.stockCardService = Objects.requireNonNull(stockCardService);
        this.lotRepository = Objects.requireNonNull(lotRepository);
        this.programService = Objects.requireNonNull(programService);
    }

    @Transactional
    public void save(Receive receive, Long userId, Asn asn) {

        if (receive.getId() == null) {

            repository.insert(receive);

        } else {
            repository.update(receive);
        }

        if (receive.getReceiveLineItems() != null && (!receive.getReceiveLineItems().isEmpty())) {

            for (ReceiveLineItem lineItem : receive.getReceiveLineItems()) {

                lineItem.setReceive(receive);
                lineItem.setCreatedBy(userId);
                lineItem.setModifiedBy(userId);
                lineItemService.save(lineItem);
                if (lineItem.isLotFlag() && (!lineItem.getReceiveLots().isEmpty())) {

                    for (ReceiveLot lot : lineItem.getReceiveLots()) {
                        lot.setReceiveLineItem(lineItem);
                        lot.setCreatedBy(userId);
                        lot.setModifiedBy(userId);
                        lotService.save(lot);
                    }


                }
            }
        }


        purchaseDocumentService.save(asn, receive, userId);

        if (receive.getStatus().equalsIgnoreCase("Received") && (receive.getReceiveLineItems() != null)) {

                for (ReceiveLineItem rece : receive.getReceiveLineItems()) {

                    List<StockEventDTO> events = new ArrayList<>();

                    Product product = productService.getById(rece.getProductId());

                    for (ReceiveLot lot : rece.getReceiveLots()) {
                        StockEventDTO event = new StockEventDTO();
                        event.setType(StockEventType.RECEIPT);
                        event.setProductCode(product.getCode());
                        event.setQuantity(Long.valueOf(lot.getQuantity()));
                        event.setFacilityId(receive.getFacilityId());
                        Lot lot1 = lotRepository.getByCode(lot.getLotNumber());
                        Lot l = new Lot();
                        l.setId(lot1.getId());
                        l.setProduct(product);
                        l.setExpirationDate(lot.getExpiryDate());
                        l.setLotCode(lot.getLotNumber());
                        l.setManufactureDate(lot.getManufacturingDate());
                        l.setProductId(product.getId());
                        l.setManufacturerName("INDIA");
                        event.setLotId(lot1.getId());
                        event.setLot(l);
                        event.setCustomProps(null);
                        events.add(event);
                    }
                    processStockCard(receive.getFacilityId(), events, userId);
                }

        }
       if(asn != null && asn.getStatus().equalsIgnoreCase("Finalized")) {

           for (AsnLineItem lineItem : asn.getAsnLineItems()) {

               ReceiveLineItem receiveLineItem = new ReceiveLineItem();
               receiveLineItem.setReceive(receive);
               receiveLineItem.setReceiveId(receive.getId());
               receiveLineItem.setProductId(lineItem.getProductid());
               receiveLineItem.setExpiryDate(lineItem.getExpirydate());
               receiveLineItem.setManufacturingDate(lineItem.getManufacturingdate());
               receiveLineItem.setQuantityCounted(lineItem.getQuantityexpected());
               receiveLineItem.setUnitPrice(lineItem.getUnitprice());
               receiveLineItem.setBoxCounted(null);
               receiveLineItem.setLotFlag(lineItem.isLotflag());
               receiveLineItem.setCreatedBy(asn.getModifiedBy());
               receiveLineItem.setModifiedBy(asn.getModifiedBy());
               lineItemService.save(receiveLineItem);

               if (receiveLineItem.isLotFlag() && (!lineItem.getAsnLots().isEmpty())) {

                   for (AsnLot lot : lineItem.getAsnLots()) {
                       ReceiveLot lot1 = new ReceiveLot();
                       lot1.setExpiryDate(lot.getExpirydate());
                       lot1.setLocationId(null);
                       lot1.setLotNumber(lot.getLotnumber());
                       lot1.setSerialNumber(lot.getSerialnumber());
                       lot1.setManufacturingDate(lot.getManufacturingdate());
                       lot1.setQuantity(lot.getQuantity());
                       lot1.setCreatedBy(userId);
                       lot1.setModifiedBy(userId);
                       lot1.setReceiveLineItem(receiveLineItem);
                       lotService.save(lot1);
                   }


               }

           }

       }
    }

    public List<Receive> getAll() {
       return repository.getAll();
    }

    public Receive getById(Long id) {
        return repository.getById(id);
    }


    public Integer getTotalSearchResultCount(String searchParam, String column) {

        if(column.equals("blawBnumber")){
            return repository.getTotalSearchResultCountByBlawBnumber(searchParam);
        }
        if(column.equals("poNumber")){
            return repository.getTotalSearchResultCountByPonumber(searchParam);
        }
        if(column.equals("supplier")){
            return repository.getTotalSearchResultCountBySupplier(searchParam);
        }
        if(column.equals("all")){
            return repository.getTotalSearchResultCountAll();
        }
        return 0;
    }

    public List<Receive> searchBy(String searchParam, String column, Pagination pagination) {

        if(column.equals("all")){
            return repository.getAll();
        }
        return repository.searchBy(searchParam, column, pagination);
    }

    public String processStockCard(Long facilityId , List<StockEventDTO> events, Long userId) {

        if(null == events || events.isEmpty()) {
          return "Empty Events";
        }

        Facility facility = facilityRepository.getById(facilityId);
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
        Product product = productService.getByCode(productCode);

            //Create Stock Cards
         StockCard card = stockCardService.getOrCreateStockCard(facility.getId(), product.getCode());
         if(card == null){
             return "Stock is not created";
         }

            // get or create lot, if lot is being used
            StringBuilder str = new StringBuilder();
            Long lotId = event.getLotId();
            Lot lotObj = event.getLot();
            LotOnHand lotOnHand = stockCardService.getLotOnHand(lotId, lotObj, productCode, card, str);
            if (!str.toString().equals("")) {
                return "Lot Not created";
            }

            // create entry from event
            StockCardEntryType entryType = StockCardEntryType.CREDIT;

            Long onHand = (null != lotObj) ? lotOnHand.getQuantityOnHand() : card.getTotalQuantityOnHand();
            if (!event.isValidIssueQuantity(onHand)) {
                return "error.stock.quantity.invalid";
            }

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
