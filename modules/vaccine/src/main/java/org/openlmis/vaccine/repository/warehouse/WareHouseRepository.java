package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.WareHouse;
import org.openlmis.vaccine.domain.wms.dto.WareHouseDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.location.WareHouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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


    public Integer getTotalSearchResultCount(String param) {
        return mapper.getTotalSearchResultCount(param);
    }

    public List<WareHouseDTO> searchByName(String searchParam, Pagination pagination) {
        return mapper.searchByName(searchParam, pagination);
    }

    public WareHouse getById(Long id) {
        return mapper.getById(id);
    }
}
