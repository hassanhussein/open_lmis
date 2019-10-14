package org.openlmis.vaccine.service.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.Product;
import org.openlmis.core.service.ProductService;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.domain.StockCard;
import org.openlmis.stockmanagement.dto.StockEventType;
import org.openlmis.stockmanagement.service.StockCardService;
import org.openlmis.vaccine.domain.wms.*;
import org.openlmis.vaccine.domain.wms.dto.StockEventDTO;
import org.openlmis.vaccine.repository.warehouse.ReceiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReceiveService {

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
    ProductService productService;

    @Transactional
    public void save(Receive receive, Long userId, Asn asn) {

        if (receive.getId() == null) {

            repository.insert(receive);

        }else {
            repository.update(receive);
        }
        if(receive.getReceiveLineItems() != null) {

            if(asn != null && (!receive.getReceiveLineItems().isEmpty())) {

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

        }


       /* List<PurchaseDocument> purchaseDocuments = receive.getPurchaseDocuments();
        for(PurchaseDocument document : purchaseDocuments) {
            document.setReceive(receive);
            document.setCreatedBy(userId);
            document.setModifiedBy(userId);
            purchaseDocumentService.save(document);
        }*/
/*
       if(receive.getReceiveLineItems() != null) {

            for(ReceiveLineItem rece: receive.getReceiveLineItems()) {


               Product product = productService.getById(rece.getProductId());
                  StockCard stockCard = stockCardService.getOrCreateStockCard(receive.getFacilityId(), product.getCode());
               for(ReceiveLot lot: rece.getReceiveLots()) {
                Lot l = new Lot();
                l.setId(lot.getId());
                l.setProduct(product);
                l.setExpirationDate(lot.getExpiryDate());
                l.setLotCode(lot.getLotNumber());
                l.setManufactureDate(lot.getManufacturingDate());
                l.setProductId(product.getId());
                l.setManufacturerName("INDIA");

                stockCardService.getOrCreateLotOnHand(l,stockCard);


               }

            }
       }
*/

       if(receive.getStatus().equals("Finalized")&& !receive.getReceiveLineItems().isEmpty()) {

           for(ReceiveLineItem item : receive.getReceiveLineItems()) {
               StockEventDTO eventDTO = new StockEventDTO();
               Product product = productService.getById(item.getProductId());
               eventDTO.setType(StockEventType.RECEIPT);
               eventDTO.setFacilityId(receive.getFacilityId());
               eventDTO.setProductCode(product.getCode());
               eventDTO.setQuantity(Long.valueOf(item.getQuantityCounted()));
               if (item.isLotFlag() && (!item.getReceiveLots().isEmpty())) {

               Lot lot = new Lot();
               for(ReceiveLot l : item.getReceiveLots()) {

                lot.setLotCode(l.getLotNumber());
                lot.setManufacturerName("INDIA");
                lot.setProductId(product.getId());
                lot.setProduct(product);
                lot.setExpirationDate(l.getExpiryDate());

                }
               eventDTO.setLot(lot);
               eventDTO.setOccurred(new Date());



               }

           }

        }


       if(asn != null && asn.getStatus().equals("Finalized")) {


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

}
