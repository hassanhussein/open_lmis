package org.openlmis.core.repository;

import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.mapper.notificationMapper.StockNotificationMapper;
import org.openlmis.core.service.notification.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class StockNotificationRepository {

    @Autowired
    private StockNotificationMapper mapper;

    @Autowired
    private CloseToExpireService closeToExpireService;

    @Autowired
    private FullFilledItemService fullFilledItemService;

    @Autowired
    private InSufficientFundingItemService inSufficientFundingItemService;

    @Autowired
    private PhasedOutItemService phasedOutItemService;

    @Autowired
    private RationingItemService rationingItemService;

    @Autowired
    private StockOutItemService stockOutItemService;

    public void insert(StockOutNotificationDTO notification) {
         mapper.insert(notification);
         saveDetails(notification);
    }

    public void update(StockOutNotificationDTO notification) {
        mapper.update(notification);
        saveDetails(notification);
    }

    public StockOutNotificationDTO getByInvoiceNumber(String invoiceNumber) {
        return mapper.getByInvoiceNumber(invoiceNumber);
    }

    private void saveDetails(StockOutNotificationDTO notification) {

        closeToExpireService.save(notification);
        fullFilledItemService.save(notification);
        inSufficientFundingItemService.save(notification);
        phasedOutItemService.save(notification);
        rationingItemService.save(notification);
        stockOutItemService.save(notification);
    }

    public StockOutNotificationDTO getById(Long id) {
        return mapper.getById(id);
    }

    public List<HashMap<String, Object>> getStockBy(String facilityIds) {
        return mapper.getStockBy(facilityIds);
    }
}
