package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.Receive;
import org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReceiveRepository {

    @Autowired
    private ReceiveMapper mapper;

    public Integer insert(Receive receive) {
        return mapper.insert(receive);
    }

    public void update(Receive receive) {
        mapper.update(receive);
    }


    public List<Receive> getAll() {
        return mapper.getAll();
    }

    public Receive getById(Long id) {
        return mapper.getById(id);
    }


    public Integer getTotalSearchResultCountByBlawBnumber(String searchParam) {
        return mapper.getTotalSearchResultCountByBlawBnumber(searchParam);
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

    public List<Receive> searchBy(String searchParam, String column,  Pagination pagination){
        return mapper.search(searchParam, column, pagination);
    }

}
