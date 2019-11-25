package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.WareHouse;
import org.openlmis.vaccine.repository.warehouse.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WareHouseService {

    @Autowired
    private WareHouseRepository repository;

    @Autowired
    private WareHouseLineItemService lineItemService;

    public void save(WareHouse house) {

        if(house.getId() == null) {
            repository.insert(house);
            lineItemService.save(house.getLineItem(), house);
        }else {
            repository.update(house);
            lineItemService.save(house.getLineItem(), house);
        }
    }
}
