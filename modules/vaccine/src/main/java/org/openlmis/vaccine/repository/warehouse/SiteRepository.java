package org.openlmis.vaccine.repository.warehouse;


import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.Site;
import org.openlmis.vaccine.domain.wms.dto.WareHouseDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.location.SiteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SiteRepository {

    @Autowired
    private SiteMapper mapper;

    public Integer insert(Site site){
      return mapper.insert(site);
    }

    public void update(Site site){
     mapper.update(site);
    }

    public List<Site> getAll(Long regionId){

        return mapper.getAllBy(regionId);
    }

    public Site getAllById(Long regionId){

        return mapper.getAllById(regionId);
    }


    public Integer getTotalSearchResultCount(String param) {
        return mapper.getTotalSearchResultCount(param);
    }

    public List<Site> searchByName(String searchParam, Pagination pagination) {
        return mapper.searchByName(searchParam, pagination);
    }

    public List<Site> getAllSites() {
        return mapper.getAllSites();
    }
}
