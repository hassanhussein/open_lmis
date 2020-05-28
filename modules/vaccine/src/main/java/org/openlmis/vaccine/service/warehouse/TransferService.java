package org.openlmis.vaccine.service.warehouse;

import org.openlmis.core.domain.Product;
import org.openlmis.core.service.ProductService;
import org.openlmis.stockmanagement.domain.*;
import org.openlmis.stockmanagement.service.StockCardService;
import org.openlmis.vaccine.domain.wms.LocationEntry;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.domain.wms.dto.LotOnHandDTO;
import org.openlmis.vaccine.domain.wms.dto.LotOnHandExtDTO;
import org.openlmis.vaccine.domain.wms.dto.StockCardDTO;
import org.openlmis.vaccine.dto.AdjustmentReasonExDTO;
import org.openlmis.vaccine.dto.LocationDTO;
import org.openlmis.vaccine.dto.LotDTO;
import org.openlmis.vaccine.dto.StockOnHandSummaryDTO;
import org.openlmis.vaccine.repository.warehouse.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    private TransferRepository repository;

    @Autowired
    private StockCardService stockCardService;

    @Autowired
    private LotOnHandLocationService locationService;

    @Autowired
    private LocationEntryService locationEntryService;

    @Autowired
    private WmsLocationService wmsLocationService;

    @Autowired
    private ProductService productService;

    @Transactional
    public Transfer save(Transfer item, Long userId, Long facilityId) {

       if(item.getId() == null) {
           repository.insert(item);
       }else
           repository.update(item);

       checkProductAvailability(item,userId,facilityId);


        Integer total = 0;

        total = Integer.valueOf(item.getSoh().toString()) - Integer.valueOf(item.getQuantity().toString());

        //locationService.updateByLotOnHandAndLocation(total,item.getFromBin(), item.getLotOnHandId());

        /*LotOnHandLocation toBinSavedData = locationService.getBy(item.getToBin(),item.getLotOnHandId());*/

        List<LotDTO> lotList  = repository.checkAvailableProductAndLotBy(item.getFromBin(), item.getProductId(), item.getLotId());

        for(LotDTO lots: lotList) {
            locationService.updateByLotOnHandAndLocation(total,lots.getLocationId(),lots.getLotOnHandId());
        }

        LocationEntry entry = new LocationEntry();
        entry.setCreatedBy(userId);
        entry.setModifiedBy(userId);
        entry.setLocationId(item.getFromBin());
        entry.setLotOnHandId(item.getLotOnHandId());
        entry.setQuantity(total);
        entry.setType(StockCardEntryType.DEBIT);

        List<StockCardEntryKV> vl = new ArrayList<>();
        StockCardEntryKV values = new StockCardEntryKV();
        values.setKeyColumn("issuedto");
        LocationDTO dto = wmsLocationService.getByLocationId(item.getToBin());
        values.setValueColumn(dto.getName());
        vl.add(values);
        entry.setKeyValues(vl);
        locationEntryService.saveLocationEntry(entry);

        LocationEntry entry2 = new LocationEntry();
        entry2.setCreatedBy(userId);
        entry2.setModifiedBy(userId);
        entry2.setType(StockCardEntryType.CREDIT);
        entry2.setLotOnHandId(item.getLotOnHandId());
        entry2.setLocationId(item.getToBin());
        entry2.setQuantity(item.getQuantity());

        List<StockCardEntryKV> vl2 = new ArrayList<>();
        StockCardEntryKV values2 = new StockCardEntryKV();
        values2.setKeyColumn("receivedfrom");
        LocationDTO dto2 = wmsLocationService.getByLocationId(item.getFromBin());
        values2.setValueColumn(dto2.getName());
        vl2.add(values2);
        entry2.setKeyValues(vl2);

        locationEntryService.saveLocationEntry(entry2);


        //save Quantity On Hand
        // if(toBinSavedData != null) {

  /*      Integer total2 = 0;
        total2 = Integer.valueOf(toBinSavedData.getQuantityOnHand().toString()) + item.getQuantity();
        locationService.updateByLotOnHandAndLocation(total2, item.getToBin(), toBinSavedData.getLotOnHandId());
       */ //}


       /* LotOnHand lotOnHand = new LotOnHand();
        lotOnHand.setId(item.getLotOnHandId());
        lotOnHand.setModifiedBy(userId);
        lotOnHand.setQuantityOnHand(total + item.getQuantity().longValue());
        stockCardService.updateLotOnHand(lotOnHand);*/
        return item;

    }

    private void checkProductAvailability(Transfer item, Long userId, Long facilityId) {

      List<LotOnHand> products = repository.checkAvailableProduct(item.getToBin(), item.getProductId());
        Product product = productService.getById(item.getProductId());
        StockCard stockCard = stockCardService.getOrCreateStockCard(facilityId,product.getCode());

        System.out.println("product-------");
        System.out.println(item.getProductId());
        System.out.println("Bin");
        System.out.println(item.getToBin());
        System.out.println("Loot");

        System.out.println(item.getLotId());

      if(!products.isEmpty()) {

      List<LotDTO> lotList  = repository.checkAvailableProductAndLotBy(item.getToBin(),item.getProductId(), item.getLotId());

      if(!lotList.isEmpty()) {

          for(LotDTO lot : lotList) {
              System.out.println(lot.getLotOnHandId());

              LotOnHand lotOnHand = new LotOnHand();
              lotOnHand.setId(lot.getLotOnHandId());
              lotOnHand.setModifiedBy(userId);
              lotOnHand.setQuantityOnHand(lot.getQuantity() + item.getQuantity().longValue());
              stockCardService.updateLotOnHand(lotOnHand);

              locationService.updateLotOnHandLocation(lot.getId(), lot.getQuantityOnHand() + item.getQuantity());

          }

      } else {
          //Create New Lot

          LotOnHand lotOnHand = new LotOnHand();
          lotOnHand.setLotId(item.getLotId());
          lotOnHand.setStockCard(stockCard);
          lotOnHand.setModifiedBy(userId);
          lotOnHand.setQuantityOnHand(item.getQuantity().longValue());
          stockCardService.insertLotOnHandBy(lotOnHand);

          LotOnHandLocation lotOnHandLocation = new LotOnHandLocation();
          lotOnHandLocation.setLotOnHandId(lotOnHand.getId());
          lotOnHandLocation.setLocationId(item.getToBin());
          lotOnHandLocation.setStockCardId(stockCard.getId());
          lotOnHandLocation.setCreatedBy(userId);
          lotOnHandLocation.setModifiedBy(userId);
          lotOnHandLocation.setQuantityOnHand(item.getQuantity().longValue());
          locationService.save(lotOnHandLocation);

          System.out.println("Lot Not available");
       //if Lot not available create Lot
      }

      } else {
          System.out.println("Product Not Available");
         // if not available create Product + Add Lot

      }
    }

    @Transactional
    public void  insertReason(AdjustmentReasonExDTO reason) {

        AdjustmentReasonExDTO getAll = repository.getReasonByCode(reason.getCode());

        if(getAll == null) {

            repository.insertReason(reason);

        } else {

            repository.updateReason(reason);
        }

    }

    public AdjustmentReasonExDTO getAllAdjumentReasons(AdjustmentReasonExDTO reason) {

        if(reason.getCode() != null) {
            return repository.getReasonByCode(reason.getCode());
        }
        return null;
    }



    public List<Transfer> search(String searchParam) {
        return null;
    }

    public List<Transfer> getAll() {
        return null;
    }

    public List<AdjustmentReasonExDTO> getTransferReasons() {
        return repository.getTransferReasons();
    }

    public List<StockOnHandSummaryDTO> getCurrentStockOnHand(Long userId, Long facilityId) {

        List<StockCardDTO>stockCardList = locationService.getStockCardWithLocationBy(facilityId);

        List<StockOnHandSummaryDTO> products =new ArrayList<>();

        if(!stockCardList.isEmpty()) {



            for (StockCardDTO stockCard : stockCardList) {

            StockOnHandSummaryDTO summary = new StockOnHandSummaryDTO();

            List<LotOnHandExtDTO> lotOnHandExtDTOList = repository.getLotOnHandExtraBy(stockCard.getProductId());

             if(!lotOnHandExtDTOList.isEmpty() && stockCard.getProduct() !=null) {

                 summary.setProduct(stockCard.getProduct());
                 summary.setProductCode(stockCard.getProductCode());
                 summary.setProductId(stockCard.getProductId());
                 Long total = 0L;
                 int index = 0;

                 List<LotOnHandDTO> lots = new ArrayList<>();

                 for (LotOnHandExtDTO lot : lotOnHandExtDTOList) {

                     if(lot.getLotNumber() != null) {

                         LotOnHandDTO lotOnHandDTO = new LotOnHandDTO();

                         Long quantityWithPackSize = (lot.getPackSize() != null)?lot.getQuantityOnHand() * Long.valueOf(lot.getPackSize()):lot.getQuantityOnHand();

                         lotOnHandDTO.setAmount(quantityWithPackSize);
                         lotOnHandDTO.setExpiry(lot.getExpiry());
                         lotOnHandDTO.setId(index++);
                         lotOnHandDTO.setLotId(lot.getLotId());
                         lotOnHandDTO.setVvm("VVM1");
                         lotOnHandDTO.setMaxSoh(quantityWithPackSize);
                         lotOnHandDTO.setNumber(lot.getLotNumber());
                         lots.add(lotOnHandDTO);
                     }
                 }

                 if(summary.getProduct() != null)
                    summary.setLots(lots);

             }     if(summary.getProduct() != null)
                      products.add(summary);

            }

        }

    return products;

    }
}
