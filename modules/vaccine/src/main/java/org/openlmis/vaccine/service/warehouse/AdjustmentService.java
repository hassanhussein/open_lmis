package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.Adjustment;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.repository.warehouse.AdjustmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdjustmentService {
    @Autowired
    AdjustmentRepository repository;
    @Transactional
    public Adjustment save(Adjustment item, Long userId, Long facilityId) {


        if(item.getId() == null) {
            repository.insert(item);
        }else
           // repository.update(item);
        System.out.println("DOne");
        return item;
    }
}
