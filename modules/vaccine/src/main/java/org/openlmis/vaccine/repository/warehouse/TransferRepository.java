package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.vaccine.domain.wms.LocationEntry;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.domain.wms.dto.LotOnHandExtDTO;
import org.openlmis.vaccine.dto.AdjustmentReasonExDTO;
import org.openlmis.vaccine.dto.LotDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class TransferRepository {

    @Autowired
    private TransferMapper mapper;

    public Integer insert(Transfer transfer) {
        return mapper.insert(transfer);
    }

    public void  update(Transfer transfer) {
        mapper.update(transfer);
    }

    public AdjustmentReasonExDTO getReasonByCode(String code) {
        return mapper.getReasonByCode(code);
    }

    public Integer insertReason(AdjustmentReasonExDTO reason) {
        return mapper.insertReasons(reason);
    }

    public void updateReason(AdjustmentReasonExDTO reason){
        mapper.updateReason(reason);
    }

    public List<AdjustmentReasonExDTO> getTransferReasons() {
        return mapper.getTransferReasons();
    }

    public List<LotOnHand> checkAvailableProduct(Long toBin, Long productId) {
        return mapper.checkAvailableProduct(toBin,productId);
    }

    public List<LotDTO> checkAvailableProductAndLotBy(Long toBin, Long productId, Long lotId) {
        return mapper.checkAvailableProductAndLotBy(toBin, productId,lotId);
    }

    public List<LotOnHandExtDTO> getLotOnHandExtraBy(Long id) {
        return mapper.getLotOnHandExtaBy(id);
    }

    public List<LocationEntry> checkAvailableLocation(Long toBin, Long productId) {
        return mapper.checkAvailableLocation(toBin,productId);
    }

    public LocationEntry getLotByStockCard(Long id, Long lotId) {
        return mapper.getLotByStockCard(id,lotId);
    }

    public Lot getLotByProduct(Long productId, Long lotId) {
        return mapper.getByProduct(productId, lotId);
    }

    public List<HashMap<String, Object>> getNearToExpireItems() {
        return mapper.getNearToExpireItems();
    }
}
