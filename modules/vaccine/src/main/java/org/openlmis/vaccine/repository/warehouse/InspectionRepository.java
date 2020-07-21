package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.Inspection;
import org.openlmis.vaccine.domain.wms.InspectionLineItem;
import org.openlmis.vaccine.domain.wms.dto.InspectionDTO;
import org.openlmis.vaccine.domain.wms.dto.PutAwayDTO;
import org.openlmis.vaccine.domain.wms.dto.VvmStatusDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.inspection.InspectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class InspectionRepository {

   @Autowired
   InspectionMapper mapper;

   @Autowired
    InspectionLineItemRepository lineItemRepository;

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

    public Integer insert(Inspection inspection) {
        return mapper.insert(inspection);
    }

    public void update(Inspection inspection) {
        mapper.update(inspection);
        if(!inspection.getLineItems().isEmpty()) {
            updateLineItemDetails(inspection.getLineItems());
        }
    }

    private void updateLineItemDetails(List<InspectionLineItem> lineItems) {

        for(InspectionLineItem lineItem: lineItems) {
            lineItemRepository.update(lineItem);
        }
    }

    public List<VvmStatusDTO> getAllVvmStatuses() {
        return mapper.getAllVvmStatuses();
    }

    public Integer updateStockCard(Long id) {
        return mapper.updateStockCard(id);
    }

    public List<HashMap<String, Object>> getBy(String product, String startDate, String endDate, String year) {
        return mapper.getBy(product,startDate, endDate,year);

    }

    public String getLastVarNumber() {

        return mapper.generateVarNumber();
    }

    public Integer getTotalSearchResultCountForPutAwayByPoNumber(String searchParam) {
        return mapper.getTotalSearchResultCountForPutAway(searchParam);
    }

    public List<PutAwayDTO> searchPutAwayBy(String searchParam, String column, Pagination pagination) {
        //System.out.println("called:"+searchParam+" "+column);
        return  mapper.searchPutAwayBy(searchParam,column,pagination);
    }

    public List<PutAwayDTO> searchedAllPutAway() {
       return mapper.searchedAllPutAway();
    }

    public void updateStatus(String status, Long inspectionId) {
         mapper.updateStatus(status,inspectionId);
    }
}
