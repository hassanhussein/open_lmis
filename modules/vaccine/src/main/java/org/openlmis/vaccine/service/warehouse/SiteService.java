package org.openlmis.vaccine.service.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.Site;
import org.openlmis.vaccine.domain.wms.dto.WareHouseDTO;
import org.openlmis.vaccine.repository.warehouse.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class SiteService {

    private Integer pageSize;

    @Autowired
    public void setPageSize(@Value("${search.page.size}") String pageSize) {
        this.pageSize = Integer.parseInt(pageSize);
    }

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

   public Site getAllById(Long regionId) {
        return repository.getAllById(regionId);
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

    public List<Site> searchBy(String searchParam, String columnName, Integer page) {

        if (columnName.equals("name")) {
            return repository.searchByName(searchParam, getPagination(page));
        }
        return emptyList();

    }
}
