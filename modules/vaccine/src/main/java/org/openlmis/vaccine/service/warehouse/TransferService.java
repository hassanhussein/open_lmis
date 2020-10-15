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
import java.util.HashMap;
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

        checkProductAvailabilityAndSave(item,userId,facilityId);

        return item;

    }

    private void checkProductAvailabilityAndSave(Transfer item, Long userId, Long facilityId) {

        Product product = productService.getById(item.getProductId());
        StockCard stockCard = stockCardService.getOrCreateStockCard(facilityId,product.getCode());
        List<LocationEntry> locationProductList = repository.checkAvailableLocation(item.getToBin(), stockCard.getId());

       /* LocationEntry availableLot = repository.getLotByStockCard(stockCard.getId(), item.getLotId());
        Long newLotId;

        if(availableLot== null){
            newLotId = repository.getLotByProduct(item.getProductId(), item.getLotId()).getId();
            System.out.println("new Lot");
            System.out.println(newLotId);
        } else {
            newLotId = availableLot.getLotId();
            System.out.println("old Lot");
            System.out.println(newLotId);
        }*/

        //Debit entry
        LocationEntry entry = new LocationEntry();



        String transfer_logs=null;
        Long tobinLocation=item.getToBin();

        try{
            LocationDTO locationDTO= wmsLocationService.getByLocationId(item.getFromBin());
            transfer_logs=locationDTO.getName();

            if(tobinLocation != null && tobinLocation != 0){
                LocationDTO locationDTOto= wmsLocationService.getByLocationId(tobinLocation);
                transfer_logs=transfer_logs+"-"+locationDTOto.getName();
            }

            entry.setTransferLogs(transfer_logs);
            //System.out.println("h: "+locationDTO.getName());

        }catch (Exception e){
            e.printStackTrace();
        }



        entry.setCreatedBy(userId);
        entry.setModifiedBy(userId);
        entry.setLocationId(item.getFromBin());
        entry.setQuantity(item.getQuantity());
        entry.setType(StockCardEntryType.DEBIT);
        entry.setStockCardId(stockCard.getId());
        entry.setLotId(item.getLotId());
        entry.setVvmId(1L);
        List<StockCardEntryKV> vl = new ArrayList<>();
        StockCardEntryKV values = new StockCardEntryKV();
        values.setKeyColumn("issuedto");
        entry.setIsTransferred(true);
        LocationDTO dto = wmsLocationService.getByLocationId(item.getToBin());
        values.setValueColumn(dto.getName());
        vl.add(values);
        entry.setKeyValues(vl);
        locationEntryService.saveLocationEntry(entry);


        LocationEntry entry2 = new LocationEntry();

        transfer_logs=null;
         tobinLocation=item.getToBin();

        try{
            LocationDTO locationDTO= wmsLocationService.getByLocationId(item.getFromBin());
            transfer_logs=locationDTO.getName();

            if(tobinLocation != null && tobinLocation != 0){
                LocationDTO locationDTOto= wmsLocationService.getByLocationId(tobinLocation);
                transfer_logs=transfer_logs+"-"+locationDTOto.getName();
            }

            entry2.setTransferLogs(transfer_logs);
            //System.out.println("h: "+locationDTO.getName());

        }catch (Exception e){
            e.printStackTrace();
        }

        if(locationProductList.isEmpty()) {

            entry2.setCreatedBy(userId);
            entry2.setModifiedBy(userId);
            entry2.setType(StockCardEntryType.CREDIT);
            entry2.setLocationId(item.getToBin());
            entry2.setQuantity(item.getQuantity());
            entry2.setStockCardId(stockCard.getId());
            entry2.setVvmId(1L);
            entry2.setLotId(item.getLotId());
            List<StockCardEntryKV> vl2 = new ArrayList<>();
            StockCardEntryKV values2 = new StockCardEntryKV();
            entry2.setIsTransferred(true);
            values2.setKeyColumn("receivedfrom");
            LocationDTO dto2 = wmsLocationService.getByLocationId(item.getFromBin());
            values2.setValueColumn(dto2.getName());
            vl2.add(values2);
            entry2.setKeyValues(vl2);
            locationEntryService.saveLocationEntry(entry2);

        }else  {

            entry2.setCreatedBy(userId);
            entry2.setModifiedBy(userId);
            entry2.setType(StockCardEntryType.CREDIT);
            entry2.setLocationId(item.getToBin());
            entry2.setQuantity(item.getQuantity());
            entry2.setStockCardId(stockCard.getId());
            entry2.setVvmId(1L);
            entry2.setLotId(item.getLotId());
            List<StockCardEntryKV> vl2 = new ArrayList<>();
            StockCardEntryKV values2 = new StockCardEntryKV();
            values2.setKeyColumn("receivedfrom");
            entry2.setIsTransferred(true);
            LocationDTO dto2 = wmsLocationService.getByLocationId(item.getFromBin());
            values2.setValueColumn(dto2.getName());
            vl2.add(values2);
            entry2.setKeyValues(vl2);
            locationEntryService.saveLocationEntry(entry2);

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
        //System.out.println("passed here 1");

        if(!stockCardList.isEmpty()) {


           // System.out.println(stockCardList.toString());
            for (StockCardDTO stockCard : stockCardList) {

            StockOnHandSummaryDTO summary = new StockOnHandSummaryDTO();


                //System.out.println(" "+stockCard.getProductId());


                List<LotOnHandExtDTO> lotOnHandExtDTOList = repository.getLotOnHandExtraBy(stockCard.getProductId());

             if(!lotOnHandExtDTOList.isEmpty() && stockCard.getProduct() !=null) {
                 //System.out.println(lotOnHandExtDTOList);

                 summary.setProduct(stockCard.getProduct());
                 summary.setProductCode(stockCard.getProductCode());
                 summary.setProductId(stockCard.getProductId());
                 Long total = 0L;
                 int index = 0;

                 List<LotOnHandDTO> lots = new ArrayList<>();

                 for (LotOnHandExtDTO lot : lotOnHandExtDTOList) {

                     if(lot.getLotNumber() != null) {

                         LotOnHandDTO lotOnHandDTO = new LotOnHandDTO();
                         long quantityWithPackSize=0;
                         try{
                             quantityWithPackSize = (lot.getPackSize() != null)?lot.getQuantityOnHand() * Long.valueOf(lot.getPackSize()):lot.getQuantityOnHand();
                             lotOnHandDTO.setPackSize(Long.valueOf(lot.getPackSize()));

                         }catch (Exception e){

                         }


                         lotOnHandDTO.setAmount(quantityWithPackSize);
                         lotOnHandDTO.setExpiry(lot.getExpiry());
                         lotOnHandDTO.setId(index++);
                         lotOnHandDTO.setLotId(lot.getLotId());
                         lotOnHandDTO.setStockCardId(lot.getStockCardId());
                         lotOnHandDTO.setLocationId(lot.getLocationId());
                         lotOnHandDTO.setVvm(lot.getVvmStatus());
                         lotOnHandDTO.setVvmId(lot.getVvmId());
                         lotOnHandDTO.setBinLocation(lot.getBinLocation());
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
    //System.out.println(products.toString());
    return products;

    }

    public List<HashMap<String,Object>> getNearToExpireItems() {
        return repository.getNearToExpireItems();
    }
}
