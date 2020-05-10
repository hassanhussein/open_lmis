package org.openlmis.restapi.service.notification.view;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.dto.FacilitySupervisor;
import org.openlmis.email.service.EmailService;
import org.springframework.stereotype.Component;


import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class RestNotificationService {

    public static final String REPORT_PDF = "report.pdf";
    public static final String APPLICATION_PDF = "application/pdf";

    private EmailService emailService;

    private List<FacilitySupervisor> userList = new ArrayList<>();

    private ByteArrayOutputStream byteArrayOutputStream;

    private Facility facility;

    public RestNotificationService(Map<String, Object> model, ByteArrayOutputStream stream, EmailService emailService) {
        this.userList = (List<FacilitySupervisor>) model.get("supervisorList");
        this.facility = (Facility) model.get("facility");
        this.byteArrayOutputStream = stream;
        this.emailService = emailService;

    }

    public void processEmail() {

        String subject = "Out of Stock Notification";


        byte[] bytes = this.byteArrayOutputStream.toByteArray();

        DataSource attachmentDataSource;

        String message = "Dear ";

        if(!this.userList.isEmpty()) {

            attachmentDataSource = new ByteArrayDataSource(bytes, APPLICATION_PDF);

            for(FacilitySupervisor supervisor : this.userList) {

                String messageBody = message.concat(supervisor.getName()).concat(" Please receive OOS message. asante ");

                if(supervisor.getContact() != null) {
                    this.emailService.sendMimeMessage(supervisor.getContact(),subject,messageBody, REPORT_PDF,attachmentDataSource);
                }

            }

        }


    }


}
