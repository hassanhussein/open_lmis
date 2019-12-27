package org.openlmis.vaccine.service.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.User;
import org.openlmis.core.service.UserService;
import org.openlmis.vaccine.domain.wms.Inspection;
import org.openlmis.vaccine.domain.wms.dto.InspectionDTO;
import org.openlmis.vaccine.repository.warehouse.InspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@NoArgsConstructor
public class InspectionService {

    @Autowired
    InspectionRepository repository;

    @Autowired
    private UserService userService;

    public Integer getTotalSearchResultCount(String searchParam, String column) {

        if (column.equals("asnNumber")) {
            return repository.getTotalSearchResultCountByAsnNumber(searchParam);
        }
        if (column.equals("asnDate")) {
            return repository.getTotalSearchResultCountByAsnDate(searchParam);
        }
        if (column.equals("receiptNumber")) {
            return repository.getTotalSearchResultCountByReceiptNumber(searchParam);
        }
        if (column.equals("receiptDate")) {
            return repository.getTotalSearchResultCountByReceiptDate(searchParam);
        }
        if (column.equals("all")) {
            return repository.getTotalSearchResultCountAll();
        }
        return 0;
    }

    public List<InspectionDTO> searchBy(String searchParam, String column, Pagination pagination) {

        if (column.equals("all")) {
            return repository.getAll();
        }
        return repository.searchBy(searchParam, column, pagination);
    }

    public Inspection getById(Long id) {
        return repository.getById(id);
    }

    @Transactional
    public void save(Inspection inspection, Long userId) {

        User user = userService.getById(userId);
        inspection.setInspectedBy(user.getFirstName()+" - "+ user.getLastName());
        if(inspection.getId() == null) {
            repository.insert(inspection);
        }else
             repository.update(inspection);
    }
}
