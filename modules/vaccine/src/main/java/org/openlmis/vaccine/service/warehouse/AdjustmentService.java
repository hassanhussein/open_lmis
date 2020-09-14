package org.openlmis.vaccine.service.warehouse;

import org.openlmis.stockmanagement.domain.StockCardEntryKV;
import org.openlmis.vaccine.domain.wms.Adjustment;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.dto.LocationDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.LocationEntryMapper;
import org.openlmis.vaccine.repository.warehouse.AdjustmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdjustmentService {
    @Autowired
    AdjustmentRepository repository;

    @Autowired
    private LocationEntryMapper mapper;

    @Autowired
    private WmsLocationService wmsLocationService;

    @Transactional
    public Adjustment save(Adjustment item, Long userId, Long facilityId) {


        if(item.getId() == null) {
            repository.insert(item);
            updateKeyValue(item);

        }
        //Why do we need this else ?
        //else
           // repository.update(item);
        return item;
    }

    private void updateKeyValue(Adjustment item) {

        List<StockCardEntryKV> vl2 = new ArrayList<>();
        StockCardEntryKV values2 = new StockCardEntryKV();
        values2.setKeyColumn("adjustment");
        LocationDTO dto2 = wmsLocationService.getByLocationId(item.getLocationid());
        values2.setValueColumn(dto2.getName());
        vl2.add(values2);
        item.setKeyValues(vl2);
    }
}
