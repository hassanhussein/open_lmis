package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.vaccine.domain.wms.WareHouse;
import org.openlmis.vaccine.repository.mapper.warehouse.location.WareHouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WareHouseRepository {

    @Autowired
    private WareHouseMapper mapper;

    public Integer insert(WareHouse house){
        return mapper.insert(house);
    }

    public void update(WareHouse house){
        mapper.update(house);
    }



}
