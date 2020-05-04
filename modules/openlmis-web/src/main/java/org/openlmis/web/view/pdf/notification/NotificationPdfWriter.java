package org.openlmis.web.view.pdf.notification;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import org.openlmis.core.service.MessageService;
import org.openlmis.web.view.pdf.PdfPageEventHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class NotificationPdfWriter extends PdfWriter {
    //PageSize.A4, 20, 20, 50, 25
    //public static final Rectangle PAGE_SIZE = new Rectangle(1500, 1059);
    public static final Rectangle PAGE_SIZE = new Rectangle(1300, 1059);
    public static final float LEFT_MARGIN = 50;
    public static final float RIGHT_MARGIN = 50;
    public static final float TOP_MARGIN = 0;
    public static final float BOTTOM_MARGIN = 30;
    private MessageService messageService;

    public NotificationPdfWriter(PdfDocument document,
                                OutputStream stream,
                                MessageService messageService
    ) throws DocumentException {
        super(document, stream);
        document.addWriter(this);
        setDocumentAttributes(document);
        this.setViewerPreferences(getViewerPreferences());
        this.messageService = messageService;
        this.setPageEvent(new PdfPageEventHandler(messageService));
    }
    private void setDocumentAttributes(PdfDocument document) {
        document.setPageSize(PAGE_SIZE);
        document.setMargins(LEFT_MARGIN, RIGHT_MARGIN, TOP_MARGIN, BOTTOM_MARGIN);
    }


    protected int getViewerPreferences() {
        return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
    }

    public void buildWith(Map<String, Object> model) throws DocumentException, IllegalAccessException, NoSuchFieldException, IOException {
        NotificationPdfModel notificationPdfModel = new NotificationPdfModel(model,messageService);
        document.open();
       // document.add(notificationPdfModel.getReportHeader());

        //document.add(notificationPdfModel.setTableHeader());

        document.add(notificationPdfModel.headerDetails());
        document.add(notificationPdfModel.reportTitle());

        document.add(notificationPdfModel.facilityDetails());

        document.add(notificationPdfModel.salesDetails());

        document.add(notificationPdfModel.commentInfo());

        document.add(notificationPdfModel.getFullFilledItemsHeader());
        document.add(notificationPdfModel.getFullFilledItemsTable());

        document.add(notificationPdfModel.stockOutItemsHeader());
        document.add(notificationPdfModel.getStockOutItemsTable());

        document.add(notificationPdfModel.inSufficientFundingHeader());
        document.add(notificationPdfModel.getInSufficientFundingTable());
        document.newPage();
        document.add(notificationPdfModel.reportTitle());
        document.add(notificationPdfModel.rationingItemsHeader());
        document.add(notificationPdfModel.rationingItemsTable());

        document.add(notificationPdfModel.closeToExpireItemsHeader());
        document.add(notificationPdfModel.closeToExpireItemsTable());

        document.add(notificationPdfModel.phasedOutItemsHeader());
        document.add(notificationPdfModel.phasedOutItemsTable());
      //  document.newPage();
        document.close();
    }
}
