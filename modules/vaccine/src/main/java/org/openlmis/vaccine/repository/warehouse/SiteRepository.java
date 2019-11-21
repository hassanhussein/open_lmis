package org.openlmis.vaccine.repository.warehouse;


import org.openlmis.vaccine.domain.wms.Site;
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

}
