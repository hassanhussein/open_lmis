package org.openlmis.vaccine.service.warehouse;

import com.lowagie.text.pdf.PRAcroForm;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.stockmanagement.service.StockCardService;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.repository.warehouse.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Transfer save(Transfer item, Long loggedInUserId) {

       if(item.getId() == null) {
           repository.insert(item);
       }else
           repository.update(item);

        Long total = 0L;

        total = item.getSoh() - item.getQuantity();

        LotOnHand l = stockCardService.getLotOnHandBy(item.getStockCardId(),item.getLotId());

        if(l != null) {
            LotOnHand lotOnHand = new LotOnHand();
            lotOnHand.setId(l.getId());
            lotOnHand.setModifiedBy(l.getModifiedBy());
            lotOnHand.setQuantityOnHand(total);
            stockCardService.updateLotOnHand(lotOnHand);
        }

        return item;

    }


    public List<Transfer> search(String searchParam) {
        return null;
    }

    public List<Transfer> getAll() {
        return null;
    }
}
