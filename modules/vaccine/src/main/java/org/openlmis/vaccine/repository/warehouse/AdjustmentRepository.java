package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.Adjustment;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.repository.mapper.warehouse.AdjustmentMapper;
import org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdjustmentRepository {
    @Autowired
    AdjustmentMapper mapper;

    public Integer insert(Adjustment adjustment) {
        return mapper.insert(adjustment);
    }


}
