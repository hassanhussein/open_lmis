package org.openlmis.restapi.service;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.RightName;
import org.openlmis.core.dto.FacilitySupervisor;
import org.openlmis.core.dto.NotificationResponseDTO;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.StockNotificationRepository;
import org.openlmis.core.repository.helper.CommaSeparator;
import org.openlmis.core.service.ELMISInterfaceService;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.MessageService;
import org.openlmis.restapi.service.notification.view.NotificationPdfView;
import org.openlmis.order.domain.Order;
import org.openlmis.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestStockNotificationService {

    @Autowired
    private StockNotificationRepository repository;

    @Autowired
    private ELMISInterfaceService elmisInterfaceService;

    @Autowired
    private CommaSeparator commaSeparator;

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private OrderService orderService;




    @Transactional
    public StockOutNotificationDTO save(StockOutNotificationDTO notification, HttpServletRequest request) throws Exception {

        if (notification != null) {

            StockOutNotificationDTO not = repository.getByInvoiceNumber(notification.getInvoiceNumber());

            if (not == null) {
                repository.insert(notification);
            } else {
                notification.setId(not.getId());
                repository.update(notification);
            }

            prepareEmailNotification(notification,request);

        }

        return notification;
    }

    private void prepareEmailNotification(StockOutNotificationDTO notificationData, HttpServletRequest request) throws Exception {

        StockOutNotificationDTO notification = getById(notificationData.getId());

        Order order = orderService.getByOrderNumber(notificationData.elmisOrderNumber);

       // if(order != null) {

            Facility facility = facilityService.getByCodeFor(notification.getSoldTo());

           // List<FacilitySupervisor> supervisorList = facilityService.getSupervisorFacilityIncludingHomeFacility(facility.getId(), order.getRnr().getProgram().getId());

            Map<String, Object> map = new HashMap<String, Object>();

            map.put("notification", notification);
            map.put("facility", facility);
         //   map.put("supervisorList", supervisorList);
           // map.put("order", order);

            NotificationPdfView view = new NotificationPdfView(messageService);

            view.render(map, request, null);
        //}

    }


    public void sendResponse(StockOutNotificationDTO notification) {
        NotificationResponseDTO notificationResponse = new NotificationResponseDTO();
        notificationResponse.setInvoiceNumber(notification.getInvoiceNumber());
        notificationResponse.setMessage("Received Successful");
        notificationResponse.setSource("elmis");
        elmisInterfaceService.processAndSendOutOfStockResponseData(notificationResponse);
    }

    public StockOutNotificationDTO getById(Long id) {
        return repository.getById(id);
    }

    public List<HashMap<String,Object>> getStockOutBy(Long userId, Long programId){
        String facilityIds = commaSeparator.commaSeparateIds(facilityService.getUserSupervisedFacilities(userId, programId, RightName.VIEW_OUT_OF_STOCK_NOTIFICATION));
        return repository.getStockBy(facilityIds);
    }

}
