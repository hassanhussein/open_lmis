package org.openlmis.restapi.service;

import org.openlmis.core.domain.ConfigurationSetting;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.RightName;
import org.openlmis.core.dto.FacilitySupervisor;
import org.openlmis.core.dto.NotificationResponseDTO;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.repository.StockNotificationRepository;
import org.openlmis.core.repository.helper.CommaSeparator;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.core.service.ELMISInterfaceService;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.MessageService;
import org.openlmis.email.service.EmailService;
import org.openlmis.restapi.service.notification.view.NotificationPdfView;
import org.openlmis.order.domain.Order;
import org.openlmis.order.service.OrderService;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.service.RequisitionService;
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
    private EmailService emailService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private ConfigurationSettingService settingService;


    @Transactional
    public StockOutNotificationDTO save(StockOutNotificationDTO notification, HttpServletRequest request) throws Exception {

        if (notification != null) {

            StockOutNotificationDTO not = repository.getByInvoiceNumber(notification.getQuoteNumber());

            if (not == null) {
                repository.insert(notification);
            } else {
                notification.setId(not.getId());
                repository.update(notification);
            }
//Not send comment for now
         //   prepareEmailNotification(notification, request);

        }

        return notification;
    }

    private void prepareEmailNotification(StockOutNotificationDTO notificationData, HttpServletRequest request) throws Exception {

        StockOutNotificationDTO notification = getById(notificationData.getId());

        Order order = null;

                order= orderService.getByOrderNumber(notificationData.elmisOrderNumber);

        if(order == null) {
            order  = orderService.getLatestOrderByFacility(notificationData.getCustomerId());
        }

        if (order != null) {

            Rnr rnr = requisitionService.getFullRequisitionById(order.getId());

            Facility facility = facilityService.getByCodeFor(notification.getCustomerId());

           // List<FacilitySupervisor> supervisorList = facilityService.getFacilitySupervisorsByRight();

            List<FacilitySupervisor> supervisorList = facilityService.getSupervisorFacilityIncludingHomeFacility(facility.getId(), rnr.getProgram().getId());

            Map<String, Object> map = new HashMap<String, Object>();

            map.put("notification", notification);
            map.put("facility", facility);
            map.put("supervisorList", supervisorList);
            map.put("order", order);
            map.put("rnr", rnr);

            NotificationPdfView view = new NotificationPdfView(messageService, emailService, settingService);

            view.render(map, request, null);
        }

    }

    public void sendResponse(StockOutNotificationDTO notification) {

        NotificationResponseDTO notificationResponse = new NotificationResponseDTO();
        notificationResponse.setInvoiceNumber(notification.getQuoteNumber());
        notificationResponse.setMessage("Received Successful");
        notificationResponse.setSource("elmis");
        notificationResponse.setStatusCode("200");
        elmisInterfaceService.processAndSendOutOfStockResponseData(notificationResponse);
    }

    public StockOutNotificationDTO getById(Long id) {
        return repository.getById(id);
    }

    public List<HashMap<String, Object>> getStockOutBy(Long userId) {

        String facilityIds = commaSeparator.commaSeparateIds(facilityService.getUserSupervisedFacilitiesBy(userId, RightName.VIEW_OUT_OF_STOCK_NOTIFICATION));

        return repository.getStockBy(facilityIds,userId);
    }

}
