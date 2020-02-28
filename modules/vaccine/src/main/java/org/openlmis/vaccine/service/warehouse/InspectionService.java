package org.openlmis.vaccine.service.warehouse;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.User;
import org.openlmis.core.service.UserService;
import org.openlmis.vaccine.domain.wms.Inspection;
import org.openlmis.vaccine.domain.wms.dto.InspectionDTO;
import org.openlmis.vaccine.domain.wms.dto.VvmStatusDTO;
import org.openlmis.vaccine.repository.warehouse.InspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
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
        if (column.equals("poNumber")) {
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

    public List<VvmStatusDTO> getAllVVM() {
        return repository.getAllVvmStatuses();
    }

    @Transactional
    public void save(Inspection inspection, Long userId) {

        User user = userService.getById(userId);
        inspection.setInspectedBy(user.getFirstName()+" - "+ user.getLastName());

        if(inspection.getStatus().equalsIgnoreCase("RELEASED")) {
            inspection.setVarNumber(generateVarNumber());
        }

        if(inspection.getId() == null) {
            repository.insert(inspection);
        }else
             repository.update(inspection);
    }

    private String generateVarNumber() {

        int year = Calendar.getInstance().get(Calendar.YEAR);

        String number = "TAN-VAR-".concat(String.valueOf(year)).concat("-");
        String lastVarNumber = repository.getLastVarNumber();
        String serialString = "", numberString = "", yearString = "";

        Long newSerial;

        if(lastVarNumber != null) {
            serialString = lastVarNumber.split("-")[3];

            yearString = lastVarNumber.split("-")[2];

            if(Integer.valueOf(yearString).equals(year)) {

                long serial = Long.parseLong(serialString);
                newSerial = serial + 1;
                numberString = number + newSerial;
            } else {
                numberString = number + String.format("%05d",1L);
            }


        } else {
            numberString = number + String.format("%05d",1L);
        }
        return numberString;

    }

    public Integer updateStockCard(Long id) {
      return repository.updateStockCard(id);
    }

    public List<HashMap<String, Object>> getBy(String product, String reportKey, String startDate, String endDate, String year) {
          if(reportKey.equalsIgnoreCase("inspect")) {
              return repository.getBy(product, startDate, endDate, year);
          }
          return null;
    }
}
