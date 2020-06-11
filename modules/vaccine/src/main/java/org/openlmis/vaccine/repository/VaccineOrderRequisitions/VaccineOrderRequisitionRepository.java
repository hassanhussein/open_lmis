package org.openlmis.vaccine.repository.VaccineOrderRequisitions;

import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.vaccine.domain.VaccineOrderRequisition.VaccineOrderRequisition;
import org.openlmis.vaccine.dto.OrderRequisitionDTO;
import org.openlmis.vaccine.dto.OrderRequisitionStockCardDTO;
import org.openlmis.vaccine.dto.VaccineOnTimeInFullDTO;
import org.openlmis.vaccine.repository.mapper.OrderRequisitions.VaccineOrderRequisitionMapper;
import org.openlmis.vaccine.service.VaccineOrderRequisitionServices.VaccineOrderRequisitionLineItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VaccineOrderRequisitionRepository {

    @Autowired
    VaccineOrderRequisitionMapper orderRequisitionMapper;

    @Autowired
    VaccineOrderRequisitionLineItemService lineItemService;


    public void  Insert(VaccineOrderRequisition orderRequisition){
        orderRequisitionMapper.insert(orderRequisition);
        saveDetails(orderRequisition);
    }

    public void Update(VaccineOrderRequisition orderRequisition){
        orderRequisitionMapper.update(orderRequisition);
        saveDetails(orderRequisition);
    }
    public void saveDetails(VaccineOrderRequisition orderRequisition){
        lineItemService.saveVaccineOrderRequisitionLineItems(orderRequisition.getLineItems(),orderRequisition.getId());
    }

    public VaccineOrderRequisition getByFacilityProgram(Long periodId,Long programId, Long facilityId){
        return orderRequisitionMapper.getByFacilityProgram(periodId,programId,facilityId);
    }

    public VaccineOrderRequisition getLastOrder(Long facilityId, Long programId){
        return orderRequisitionMapper.getLastReport(facilityId,programId);
    }
    public VaccineOrderRequisition getAllDetailsById(Long id){
        return orderRequisitionMapper.getAllOrderDetails(id);
    }

    public Long getScheduleFor(Long facilityId, Long programId) {
        return orderRequisitionMapper.getScheduleFor(facilityId, programId);
    }

    public List<OrderRequisitionDTO> getReportedPeriodsForFacility(Long facilityId, Long programId) {
        return orderRequisitionMapper.getReportedPeriodsForFacility(facilityId, programId);
    }
    public Long getReportIdForFacilityAndPeriod(Long facilityId, Long periodId){
        return orderRequisitionMapper.getReportIdForFacilityAndPeriod(facilityId, periodId);
    }

    public List<OrderRequisitionDTO>getPendingRequest(Long userId, Long facilityId, Long programId){
        return  orderRequisitionMapper.getPendingRequest(userId,facilityId,programId);
    }
    public List<OrderRequisitionDTO>getAllBy(Long programId, Long periodId, Long facilityId){
        return orderRequisitionMapper.getAllBy(programId,periodId,facilityId);
    }

    public List<OrderRequisitionDTO>getAllSearchBy(Long facilityId,String dateRangeStart,String dateRangeEnd,Long programId){
        return orderRequisitionMapper.getSearchedDataBy(facilityId, dateRangeStart, dateRangeEnd, programId);
    }


    public Long updateOFRStatus(Long orderId){
        return orderRequisitionMapper.updateORStatus(orderId);
    }

    public List<OrderRequisitionStockCardDTO> getStockCards(Long facilityId, Long programId) {
        return orderRequisitionMapper.getAllByFacilityAndProgram(facilityId, programId);
    }

    public List<OrderRequisitionStockCardDTO> getAllByFacility(Long facilityId, Long programId) {
        return orderRequisitionMapper.getAllByFacility(facilityId, programId);
    }

    public List<OrderRequisitionDTO>getSupervisoryNodeByFacility(Long facilityId){
        return orderRequisitionMapper.getSupervisoryNodeByFacility(facilityId);
    }
    public List<OrderRequisitionDTO>getConsolidatedList(Long program,String facilityIds){
        return orderRequisitionMapper.getConsolidatedList(program,facilityIds);
    }

    public Long verifyVaccineOrderRequisition(Long orderId){
        return orderRequisitionMapper.verifyVaccineOrderRequisition(orderId);
    }

    public Integer getTotalPendingRequest(Long userId, Long facilityId, Long programId) {
        return orderRequisitionMapper.getTotalPendingRequest(userId, facilityId, programId);
    }

    public List<VaccineOnTimeInFullDTO>getOnTimeInFullData(Long facilityId, Long periodId, Long orderId){
        return  orderRequisitionMapper.getOnTimeInFullData(facilityId,periodId,orderId);
    }

    public List<OrderRequisitionDTO>getSearchedDataForOnTimeReportingBy(Long facilityId,String dateRangeStart,String dateRangeEnd,Long programId){
        return orderRequisitionMapper.getSearchedDataForOnTimeReportingBy(facilityId, dateRangeStart, dateRangeEnd, programId);
    }

    public void updateOrderStatus(VaccineOrderRequisition requisition) {
        orderRequisitionMapper.updateOrderStatus(requisition.getStatus().toString(),requisition.getId());
    }

    public List<LotOnHand> getDistributionByOrderIdAndProduct(Long id, Long productId) {
        return orderRequisitionMapper.getDistributionByOrderIdAndProduct(id, productId);
    }
}
