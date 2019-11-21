package org.openlmis.vaccine.service.warehouse;

import org.openlmis.vaccine.domain.wms.Site;
import org.openlmis.vaccine.repository.warehouse.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

 @Autowired
 private SiteRepository repository;

    public void save(Site site, Long userId) {

        if(site.getId() == null) {
           repository.insert(site);
        }else {
            repository.update(site);
        }
    }

    public List<Site> getAll(Long regionId) {
        return repository.getAll(regionId);
    }
}
