package org.openlmis.restapi.service.notification.view;


import com.itextpdf.text.pdf.PdfDocument;
import org.openlmis.core.domain.ConfigurationSetting;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.core.service.MessageService;
import org.openlmis.restapi.service.notification.view.pdf.NotificationPdfWriter;
import org.openlmis.email.service.EmailService;
import org.openlmis.rnr.domain.Rnr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Properties;

@Component("processNotificationPDF")
public class NotificationPdfView extends AbstractView {

    private MessageService messageService;

    private EmailService emailService;

    private ConfigurationSettingService settingService;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public NotificationPdfView(MessageService messageService, EmailService emailService,ConfigurationSettingService settingService) {

        this.messageService = messageService;
        this.emailService = emailService;
        this.settingService = settingService;

        setContentType("application/pdf");
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        try (ByteArrayOutputStream stream = createTemporaryOutputStream()) {

            NotificationPdfWriter notificationPdfWriter = new NotificationPdfWriter(new PdfDocument(), stream, messageService,model);
            notificationPdfWriter.buildWith(model);
          //  writeToResponse(response, stream);
            RestNotificationService service = new RestNotificationService(model, stream, emailService, this.settingService);
            service.processEmail();

          //  sendEmail(stream);


        }

    }

    private void sendEmail(ByteArrayOutputStream stream) {

        byte[] bytes = stream.toByteArray();
        //construct the pdf body part

        String smtpHost = "smtp.gmail.com"; //replace this with a valid host
        int smtpPort = 465; //replace this with a valid port

        String sender = "test.hhassan.developer@gmail.com"; //replace this with a valid sender email address

        String recipient = "hhassan.developer@gmail.com"; //replace this with a valid recipient email address

        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("test.hhassan.developer@gmail.com", "8819rukia");

            }

        });

        session.setDebug(true);


        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(sender));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Set Subject: header field
            message.setSubject("MSD Out of stock notification");

            //send Attachment

            Multipart multipart = new MimeMultipart();
            MimeBodyPart pdfBodyPart = new MimeBodyPart();

            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");

            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("MSD_OOS_Notification.pdf");


            MimeBodyPart textPart = new MimeBodyPart();

            textPart.setText(" Dear Dpharm, This is a test MSD Out of stock notification notification");
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(pdfBodyPart);

            message.setContent(multipart);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

/*    public void sendMailWithInlineResources(String to, String subject, String fileToAttach, ByteArrayOutputStream stream)
    {
        MimeMessagePreparator preparator = new MimeMessagePreparator()
        {
            public void prepare(MimeMessage mimeMessage) throws Exception
            {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress("test.hhassan.developer@gmail.com"));
                mimeMessage.setSubject(subject);

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                byte[] bytes = stream.toByteArray();

                helper.setText("<html><body><img src='cid:identifier1234'></body></html>", true);


                FileSystemResource res = new FileSystemResource(new File(fileToAttach));
                helper.addInline("identifier1234", res);
            }
        };

        try {
            mailSender.send(preparator);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }*/
}
