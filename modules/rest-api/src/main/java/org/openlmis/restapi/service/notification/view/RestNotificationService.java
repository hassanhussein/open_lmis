package org.openlmis.restapi.service.notification.view;

import lombok.NoArgsConstructor;
import org.apache.commons.net.util.Base64;
import org.openlmis.core.domain.ConfigurationSettingKey;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.User;
import org.openlmis.core.dto.FacilitySupervisor;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.email.domain.EmailAttachment;
import org.openlmis.email.domain.EmailMessage;
import org.openlmis.email.service.EmailService;
import org.openlmis.rnr.domain.Rnr;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class RestNotificationService {

    public static final String REPORT_PDF = "report.pdf";
    public static final String APPLICATION_PDF = "application/pdf";
    public static final String OUT_OF_STOCK_EMAIL_MESSAGE_TEMPLATE = "OUT_OF_STOCK_EMAIL_MESSAGE_TEMPLATE";
    public static final String OUT_OF_STOCK_EMAIL_SUBJECT_TEMPLATE = "OUT_OF_STOCK_EMAIL_SUBJECT_TEMPLATE";

    private EmailService emailService;

    private ConfigurationSettingService settingService;

    private List<FacilitySupervisor> userList = new ArrayList<>();

    private Rnr rnr;

    private ByteArrayOutputStream byteArrayOutputStream;

    private Facility facility;

    public RestNotificationService(Map<String, Object> model, ByteArrayOutputStream stream, EmailService emailService, ConfigurationSettingService settingService) {

        this.userList = (List<FacilitySupervisor>) model.get("supervisorList");
        this.facility = (Facility) model.get("facility");
        this.byteArrayOutputStream = stream;
        this.emailService = emailService;
        this.settingService = settingService;
        this.rnr = (Rnr) model.get("rnr");

    }

    public void processEmail() {

        byte[] bytes = this.byteArrayOutputStream.toByteArray();

       // if(!this.userList.isEmpty()) {
            insertEmailMessages(bytes, this.userList);
      //  }


    }


    private void insertEmailMessages(byte[] attachmentDataSource, List<FacilitySupervisor> users) {

        List<EmailAttachment> emailAttachments = prepareEmailAttachmentsForStockOutNotification(attachmentDataSource);
        this.emailService.insertEmailAttachmentList(emailAttachments);
        String emailMessage = settingService.getByKey(OUT_OF_STOCK_EMAIL_MESSAGE_TEMPLATE).getValue();

        final String subject = settingService.getByKey(OUT_OF_STOCK_EMAIL_SUBJECT_TEMPLATE).getValue();

        for (FacilitySupervisor user : users) {

            emailMessage = emailMessage.replaceAll("\\{facility_name\\}", this.facility.getName());
            emailMessage = emailMessage.replaceAll("\\{name\\}", user.getName());
            emailMessage = emailMessage.replaceAll("\\{period\\}", this.rnr.getPeriod().getName());
            emailMessage = emailMessage.replaceAll("\\{program\\}", this.rnr.getProgram().getName());

            String emailAddress = user.getContact();

            if (emailAddress != null) {
                EmailMessage email = new EmailMessage();
                email.setTo(emailAddress);
                email.setText(emailMessage);
                email.setSubject(subject);
                email.setEmailAttachments(emailAttachments);
                email.setHtml(true);
                emailService.queueEmailMessage(email);
            }
        }
    }

    private List<EmailAttachment> prepareEmailAttachmentsForStockOutNotification(byte[] path) {

        List<EmailAttachment> emailAttachments = new ArrayList<>();
        EmailAttachment attachment = generateEmailAttachment(path);
        emailAttachments.add(attachment);

        return emailAttachments;
    }

    private EmailAttachment generateEmailAttachment(byte[] filePath) {

        EmailAttachment attachment = new EmailAttachment();
        attachment.setAttachmentName(REPORT_PDF);
        attachment.setAttachmentPath(null);
        attachment.setFileSource(filePath);
        attachment.setAttachmentFileType(APPLICATION_PDF);
        return attachment;

    }

}
