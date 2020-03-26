package org.openlmis.vaccine.service.warehouse;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.LocationType;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.LocationTypeService;
import org.openlmis.vaccine.domain.wms.WareHouse;
import org.openlmis.vaccine.domain.wms.dto.WareHouseDTO;
import org.openlmis.vaccine.dto.LocationDTO;
import org.openlmis.vaccine.dto.WarehouseLocationDTO;
import org.openlmis.vaccine.repository.warehouse.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private LocationTypeService locationTypeService;


    @Autowired
    private WareHouseLineItemService lineItemService;

    public void save(WareHouse house) {
         try {
             if (house.getId() == null) {
                 repository.insert(house);
                 return;
             }
             repository.update(house);
         }
         catch (DuplicateKeyException e) {
             throw new DataException("error.duplicate.warehouse.code");
         } catch (DataIntegrityViolationException e) {
             throw new DataException("error.incorrect.length");
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

    public WareHouse getByC(String code) {
        return repository.getByC(code);
    }

    public LocationDTO getBy(LocationDTO location) {

        LocationType locationType = locationTypeService.getBy(location.getLocationTypeCode());

        return repository.getBy(location.getCode(), getByC(location.getWarehouseCode()).getId(), locationType.getId());

    }

    public void saveLocations(LocationDTO location) {

       LocationType locationType = locationTypeService.getBy(location.getLocationTypeCode());
       WareHouse wareHouse = getByC(location.getWarehouseCode());
       LocationDTO dto = repository.getBy(location.getCode(), getByC(location.getWarehouseCode()).getId(), locationType.getId());
       location.setTypeId(locationType.getId());
       location.setWarehouseId(wareHouse.getId());

       if(dto == null) {
           repository.saveLocation(location);
       } else {
           repository.updateLocation(location);
       }

    }

    public List<LocationDTO> getAllLocations(Long id) {

        return repository.getAllLocations(id);
    }

    public List<WareHouse> getAllWarehouses() {
        return repository.getAllWarehouses();
    }

    @Transactional
    public void saveLocationFromUI(LocationDTO location){

        LocationType type = locationTypeService.getByDisplayOrder(location.getDisplayOrder());
        location.setTypeId(type.getId());
        try {

            if (location.getId() == null) {
                repository.saveLocation(location);
                return;
            }
            repository.updateLocation(location);
        }
        catch (DuplicateKeyException e) {
            throw new DataException("error.duplicate.bin.location.code");
        } catch (DataIntegrityViolationException e) {
            throw new DataException("error.incorrect.length");
        }
    }

    public Integer getTotalBinsSearchResultCount(String searchParam, String columnName,Long wareHouseId) {

        if (columnName.equals("code")) {
            return repository.getTotalBinsSearchResultCount(searchParam,wareHouseId);
        }
        return 0;
    }

    public List<LocationDTO> searchBinBy(String searchParam, String columnName, Integer page, Long wareHouseId) {

        if (columnName.equals("code")) {
            return repository.searchBinBy(searchParam, getPagination(page),wareHouseId);
        }
        return emptyList();

    }

    public List<LocationDTO> getAllLocationsByCategory(String category) {
        return  repository.getAllLocationsByCategory(category);
    }

    public List<WarehouseLocationDTO> getWarehouseByBinLocation() {
        return repository.getWarehouseByBinLocation();
    }
}
