package org.openlmis.vaccine.repository.warehouse;

import net.sf.jasperreports.engine.json.expression.member.ObjectKeyExpression;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.dto.PutAwayLineItemDTO;
import org.openlmis.vaccine.domain.wms.dto.SohReportDTO;
import org.openlmis.vaccine.domain.wms.dto.StockCardLocationDTO;
import org.openlmis.vaccine.domain.wms.dto.TransferDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.LotOnHandLocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class LotOnHandLocationRepository {

    @Autowired
    private LotOnHandLocationMapper mapper;

    public Integer insert(LotOnHandLocation location) {
       return mapper.insert(location);
    }

    public void update(LotOnHandLocation location) {
        mapper.update(location);
    }

    public Integer insertPutAwayDetails(PutAwayLineItemDTO item) {
       return mapper.savePutAwayDetails(item);
    }

    public void deleteExistingPutAway(Long inspectionId) {
        mapper.deleteExistingPutAway(inspectionId);
    }

    public void deleteExistingByLot(Long id, Long toBinLocationId) {
        mapper.deleteExistingByLot(id,toBinLocationId);
    }

    public void deleteExistingStockCardLocation(Long id, Long toBinLocationId) {
        mapper.deleteExistingStockCardLocation(id, toBinLocationId);
    }

    public void insertLocationsWIthoutLots(StockCardLocationDTO stockCardLocationDTO) {
        mapper.insertLocationsWIthoutLots(stockCardLocationDTO);
    }


    public List<SohReportDTO> getSOHReport(Long facilityId, Long warehouseId) {
        return mapper.getSohReport(facilityId, warehouseId);
    }

    public List<HashMap<String, Object>> getAllLedgers(Long facilityId, Long productId, Long warehouseId, Long year) {
        return mapper.getAllLedgers(facilityId,productId,warehouseId,year);
    }

    public List<HashMap<String,Object>>getAllByWareHouseAndBinLocation(Long fromWarehouseId, Long fromBinLocationId){
        return mapper.getAllByWareHouseAndBinLocation(fromWarehouseId,fromBinLocationId);
    }

    public List<TransferDTO>getTransferDetailsBy(Long wareHouseId, Long fromBinLocationId) {
        return mapper.getTransferDetailsBy(wareHouseId,fromBinLocationId);
    }

    public void updateByLotOnHandAndLocation(Integer total, Long fromBin, Long lotOnHandId) {
        mapper.updateByLotOnHandAndLocation(total, fromBin, lotOnHandId);
    }

    public LotOnHandLocation getBy(Long fromBin, Long lotOnHandId) {
        return mapper.getBy(fromBin, lotOnHandId);
    }
}
