package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsnRepository {

    @Autowired
    AsnMapper mapper;


    public Long insert(Asn asn) {
        return mapper.insert(asn);
    }

    public void update(Asn asn) {
        mapper.update(asn);
    }

    public Asn getById (Long id){
        return mapper.getById(id);
    }

    public List<Asn> getAll(){
        return mapper.getAll();
    }

    public Integer getTotalSearchResultCountByAsnumber(String searchParam) {
        return mapper.getTotalSearchResultCountByAsnumber(searchParam);
    }

    public Integer getTotalSearchResultCountByPonumber(String searchParam) {
        return mapper.getTotalSearchResultCountByPonumber(searchParam);
    }
    public Integer getTotalSearchResultCountBySupplier(String searchParam) {
        return mapper.getTotalSearchResultCountBySupplier(searchParam);
    }
    public Integer getTotalSearchResultCountAll() {
        return mapper.getTotalSearchResultCountAll();
    }

    public List<Asn> searchBy(String searchParam, String column,  Pagination pagination){
        return mapper.search(searchParam, column, pagination);
    }

    public void deleteById(Long id) {
        mapper.deleteById(id);
    }
}
