package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.Inspection;
import org.openlmis.vaccine.domain.wms.dto.InspectionDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InspectionRepository {

   @Autowired
   InspectionMapper mapper;

    public Integer getTotalSearchResultCountByAsnNumber(String searchParam) {
        return mapper.getTotalSearchResultCountByAsnNumber(searchParam);
    }

    public Integer getTotalSearchResultCountByAsnDate(String searchParam) {
        return mapper.getTotalSearchResultCountByAsnDate(searchParam);
    }

    public Integer getTotalSearchResultCountByReceiptNumber(String searchParam) {
        return mapper.getTotalSearchResultCountByReceiptNumber(searchParam);
    }

    public Integer getTotalSearchResultCountByReceiptDate(String searchParam) {
       return mapper.getTotalSearchResultCountByReceiptDate(searchParam);
    }

    public Integer getTotalSearchResultCountAll() {
       return mapper.getTotalSearchResultCountAll();
    }

    public List<InspectionDTO> getAll() {
        return mapper.getAll();
    }

    public List<InspectionDTO> searchBy(String searchParam, String column, Pagination pagination) {

        return mapper.searchBy(searchParam, column, pagination);
    }

    public Inspection getById(Long id) {
        return mapper.getById(id);
    }
}
