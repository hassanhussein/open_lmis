package org.openlmis.vaccine.service.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.WareHouse;
import org.openlmis.vaccine.domain.wms.dto.WareHouseDTO;
import org.openlmis.vaccine.repository.warehouse.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class WareHouseService {


    private Integer pageSize;

    @Autowired
    public void setPageSize(@Value("${search.page.size}") String pageSize) {
        this.pageSize = Integer.parseInt(pageSize);
    }

    @Autowired
    private WareHouseRepository repository;

    @Autowired
    private WareHouseLineItemService lineItemService;

    public void save(WareHouse house) {

        if(house.getId() == null) {
            repository.insert(house);
        }else {
            repository.update(house);
        }
    }

    public Pagination getPagination(Integer page) {
        return new Pagination(page, pageSize);
    }

    public Integer getTotalSearchResultCount(String param, String columnName) {

        if (columnName.equals("name")) {
            return repository.getTotalSearchResultCount(param);
        }
        return 0;
    }

    public List<WareHouse> searchBy(String searchParam, String columnName, Integer page) {

        if (columnName.equals("name")) {
            return repository.searchByName(searchParam, getPagination(page));
        }
        return emptyList();

    }

    public WareHouse getById(Long id) {
        return repository.getById(id);
    }
}
