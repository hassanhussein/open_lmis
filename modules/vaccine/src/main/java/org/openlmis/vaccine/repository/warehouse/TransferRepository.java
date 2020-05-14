package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.dto.AdjustmentReasonExDTO;
import org.openlmis.vaccine.dto.LotDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
