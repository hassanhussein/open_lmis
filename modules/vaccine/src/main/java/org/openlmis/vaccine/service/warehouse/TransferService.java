package org.openlmis.vaccine.service.warehouse;

import com.lowagie.text.pdf.PRAcroForm;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.stockmanagement.domain.StockCardEntryKV;
import org.openlmis.stockmanagement.domain.StockCardEntryType;
import org.openlmis.stockmanagement.service.StockCardService;
import org.openlmis.vaccine.domain.wms.LocationEntry;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.dto.AdjustmentReasonExDTO;
import org.openlmis.vaccine.dto.LocationDTO;
import org.openlmis.vaccine.repository.warehouse.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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

    @Transactional
    public Transfer save(Transfer item, Long userId) {

       if(item.getId() == null) {
           repository.insert(item);
       }else
           repository.update(item);

        Integer total = 0;

        total = Integer.valueOf(item.getSoh().toString()) - Integer.valueOf(item.getQuantity().toString());

        LotOnHandLocation toBinSavedData = locationService.getBy(item.getToBin(),item.getLotOnHandId());

        locationService.updateByLotOnHandAndLocation(total,item.getFromBin(), item.getLotOnHandId());


        //save Quantity On Hand
        if(toBinSavedData != null) {

            total = Integer.valueOf(toBinSavedData.getQuantityOnHand().toString()) + item.getQuantity();

            locationService.updateByLotOnHandAndLocation(total, item.getToBin(), item.getLotOnHandId());
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

        LotOnHand lotOnHand = new LotOnHand();
        lotOnHand.setId(item.getLotOnHandId());
        lotOnHand.setModifiedBy(userId);
        lotOnHand.setQuantityOnHand(total + item.getQuantity().longValue());
        stockCardService.updateLotOnHand(lotOnHand);
        return item;

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
}
