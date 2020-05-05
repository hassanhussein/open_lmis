package org.openlmis.web.view.pdf.notification;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.dto.notification.FullFilledItem;
import org.openlmis.core.dto.notification.NotificationLineItem;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.openlmis.core.dto.notification.VisibleColumn;
import org.openlmis.core.service.MessageService;
import org.openlmis.web.model.NotificationColumn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.openlmis.rnr.domain.ColumnType.TEXT;
import static org.openlmis.web.view.pdf.requisition.RequisitionCellFactory.*;


@Data
@NoArgsConstructor
public class NotificationPdfModel {

    public static final String LABEL_CURRENCY_SYMBOL = "label.currency.symbol";
    public static final float PARAGRAPH_SPACING = 30.0f;
    public static final BaseColor ROW_GREY_BACKGROUND = new BaseColor(235, 235, 235);
    public static final Font H1_FONT = FontFactory.getFont(FontFactory.TIMES, 30, Font.BOLD, BaseColor.BLACK);
    public static final Font TITLE_FONT = FontFactory.getFont(FontFactory.TIMES, 16, Font.BOLD, BaseColor.BLACK);
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final int TABLE_SPACING = 25;
    public static final BaseColor HEADER_BACKGROUND = new BaseColor(210, 210, 210);
    public static final int WIDTH_PERCENTAGE = 100;
    private static final String OK_IMAGE = "images/ok-icon.png";
    private static final String MSD_LOGO = "images/msd-logo.jpeg";

    private StockOutNotificationDTO notification;

    private Facility facility;

    private MessageService messageService;

    public NotificationPdfModel(Map<String, Object> model, MessageService messageService) {

        this.notification = (StockOutNotificationDTO) model.get("notification");
        this.facility = (Facility) model.get("facility");
        this.messageService = messageService;

    }

    public PdfPTable getReportHeader() throws DocumentException {
        PdfPTable table = prepareHeaderTable();
        addHeading(table);
        addFirstLine(this.notification, table);
        table.setSpacingAfter(PARAGRAPH_SPACING);
        return table;
    }

    public PdfPTable setTableHeader() throws IOException, DocumentException {

        PdfPTable table = new PdfPTable(1);
        table.addCell(imageCell());
        float[] columnWidths = new float[] { 100};
        table.setWidthPercentage(100f);
        table.setWidths(columnWidths);
        /*ColumnText ct = new ColumnText(cb);
        ct.addElement(table);
        ct.setSimpleColumn(36, 0, 559, 806); //Position goes here
        ct.go();*/
        table.setSpacingAfter(PARAGRAPH_SPACING);
        return table;
    }

    public PdfPTable headerDetails() throws IOException, DocumentException {

        PdfPTable table = new PdfPTable(2);
        Font font1 = FontFactory.getFont(FontFactory.TIMES, 13f, Font.BOLDITALIC,BaseColor.BLACK);
        Chunk header = new Chunk("off Nyerere Road, Keko Mwanga", font1);
        header.append("\n");
        header.append("P.O.Box 9081, Dar es salaam,Tanzania");
        header.append("\n");
        header.append("Telephone: (255 22) 2860890-7").append("\n");
        header.append("Fax: (255 22) 2865814 2865819");
        header.append("\n").append("E-mail : info@msd.go.tz");
        header.append("\n").append("Website: www.msd.go.tz");
        table.setWidthPercentage(100);

        PdfPCell detailCell = new PdfPCell(new Phrase(header));
        detailCell.setBorder(Rectangle.NO_BORDER);
        detailCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        detailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell logoLeftCell = new PdfPCell(imageCell());
        logoLeftCell.setFixedHeight(80);
        logoLeftCell.setBorder(Rectangle.NO_BORDER);
        logoLeftCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        table.setWidths(new int[] { 2, 2});
        table.addCell(logoLeftCell);
        table.addCell(detailCell);

        return table;
    }

    public static PdfPCell imageCell() throws BadElementException, IOException {
        Resource resource = new ClassPathResource(MSD_LOGO);
        Image image = Image.getInstance(resource.getFile().getAbsolutePath());
        PdfPCell cell = new PdfPCell(image);
        cell.setPadding(CELL_PADDING);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private void addFirstLine(StockOutNotificationDTO notification, PdfPTable table) {
        String text = String.format("Legal Number"+ ": %s", notification.getLegalNumber());
        insertCell(table, text, 1);
        text = String.format("sendTo" + ": %s",
               notification.getSoldTo());
        insertCell(table, text, 1);
      /*  text = String.format(messageService.message("label.facility.maximumStock") + ": %s",
                facility.getFacilityType().getNominalMaxMonth());
        insertCell(table, text, 1);
        text = String.format(messageService.message("label.emergency.order.point") + ": %s",
                facility.getFacilityType().getNominalEop());*/
/*
        insertCell(table, text, 1);
*/
/*
        insertCell(table, "", 1);
*/
    }

    private void insertCell(PdfPTable table, String text, int colSpan) {
        Chunk chunk;
        chunk = new Chunk(text);
        PdfPCell cell = table.getDefaultCell();
        cell.setPhrase(new Phrase(chunk));
        cell.setColspan(colSpan);
        table.addCell(cell);
    }

    private PdfPTable prepareHeaderTable() throws DocumentException {
        int[] columnWidths = {160, 160, 160, 160, 160};
        PdfPTable table = new PdfPTable(columnWidths.length);
        table.setWidths(columnWidths);
        table.getDefaultCell().setBackgroundColor(HEADER_BACKGROUND);
        table.getDefaultCell().setPadding(20);
        table.getDefaultCell().setBorder(0);
        table.setWidthPercentage(WIDTH_PERCENTAGE);
        table.setSpacingBefore(TABLE_SPACING);
        table.setHeaderRows(1);
        return table;
    }

    private void addHeading(PdfPTable table) throws DocumentException {
        Chunk chunk = new Chunk(String.format("MSD Invoice" + ": %s (%s)",
                this.notification.getInvoiceNumber(),
                this.notification.getShipTo()), H1_FONT);
        System.out.println(this.notification.getInvoiceNumber());
        PdfPCell cell = new PdfPCell(new Phrase(chunk));
        cell.setColspan(5);
        cell.setPadding(10);
        cell.setBorder(1);
        table.addCell(cell);
    }

    public Paragraph setTitle() {

        Paragraph preface = new Paragraph("Sales Invoice", H2_FONT);
        preface.setAlignment(Element.ALIGN_CENTER);
        preface.setFont(H2_FONT);

        return preface;
    }

   public PdfPTable reportTitle() throws DocumentException {

    PdfPTable table = new PdfPTable(3);
       table.setWidths(new int[] { 2, 2,2});
       table.getDefaultCell().setBackgroundColor(HEADER_BACKGROUND);
       table.getDefaultCell().setPadding(20);
       table.getDefaultCell().setBorder(1);
       table.setWidthPercentage(WIDTH_PERCENTAGE);
       table.setSpacingBefore(10);

       //table.setHeaderRows(2);

       Font font1 = FontFactory.getFont(FontFactory.TIMES, 25f, Font.BOLD,BaseColor.BLACK);
       Chunk title = new Chunk("Sales Invoice", font1);

       Chunk leftDetails = new Chunk("TIN: 101-060-195", TITLE_FONT);
       leftDetails.append("\n").append("\n").append("Invoice No. ").append(this.notification.getInvoiceNumber());

       Chunk rightDetails = new Chunk("ISO 90001:2015 CERTIFIED",TITLE_FONT);
       rightDetails.append("\n").append("\n").append("Zone: ").append(this.notification.getZone());


       PdfPCell titleCell = new PdfPCell(new Phrase(title));
       titleCell.setBorder(Rectangle.NO_BORDER);
       titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
       //titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

       PdfPCell leftCellDetails = new PdfPCell(new Phrase(leftDetails));
       leftCellDetails.setFixedHeight(80);
       leftCellDetails.setBorder(Rectangle.NO_BORDER);
       leftCellDetails.setHorizontalAlignment(Element.ALIGN_LEFT);

       PdfPCell rightCellDetails = new PdfPCell(new Phrase(rightDetails));
       rightCellDetails.setFixedHeight(80);
       rightCellDetails.setBorder(Rectangle.NO_BORDER);
       rightCellDetails.setHorizontalAlignment(Element.ALIGN_RIGHT);
       table.addCell(leftCellDetails);
       table.addCell(titleCell);
       table.addCell(rightCellDetails);
      /* table.setHeaderRows(1);
       table.setSkipFirstHeader(true);*/
    return table;
   }


    public PdfPTable facilityDetails() throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new int[] { 4, 2,4});
        table.getDefaultCell().setBackgroundColor(HEADER_BACKGROUND);
        table.getDefaultCell().setPadding(20);
        table.getDefaultCell().setBorder(1);
        table.setWidthPercentage(WIDTH_PERCENTAGE);
       // table.setSpacingBefore(TABLE_SPACING);
        //table.setHeaderRows(2);

        Font font1 = FontFactory.getFont(FontFactory.TIMES, 16f, Font.BOLD,BaseColor.BLACK);

        Chunk leftDetails = new Chunk("Sold To ", font1);

        leftDetails.append("\n").append(this.notification.getSoldTo())
                .append("\n").append(this.notification.getSoldToCustomerName())
                .append(" ( ").append(this.facility.getGeographicZone().getName()).append(") ")
                 .append("\n").append("P.O.Box ").append((this.facility.getAddress1()==null)?" ":this.facility.getAddress1())
                .append(" ")
                 .append(this.facility.getGeographicZone().getName());
        leftDetails.append("\n").append("\n").append(this.facility.getGeographicZone().getParent().getName())
                .append("\n").append("Tanzania");


        Chunk rightDetails = new Chunk("Ship To ", font1);

        rightDetails.append("\n").append(this.notification.getShipTo())
                .append("\n").append(this.notification.getShipToCustomerName())
                .append(" ( ").append(this.facility.getGeographicZone().getName()).append(") ")
                .append("\n").append("P.O.Box ").append((this.facility.getAddress1()==null)?" ":this.facility.getAddress1())
                .append(" ")
                .append(this.facility.getGeographicZone().getName());
        rightDetails.append("\n").append("\n").append(this.facility.getGeographicZone().getParent().getName())
                .append("\n").append("Tanzania");


        PdfPCell titleCell = new PdfPCell(new Phrase(" "));
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell leftCellDetails = new PdfPCell(new Phrase(leftDetails));
        leftCellDetails.setFixedHeight(120);
        leftCellDetails.setBorder(Rectangle.BOX);
        leftCellDetails.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell rightCellDetails = new PdfPCell(new Phrase(rightDetails));
        rightCellDetails.setFixedHeight(120);
        rightCellDetails.setBorder(Rectangle.BOX);
        rightCellDetails.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(leftCellDetails);
        table.addCell(titleCell);
        table.addCell(rightCellDetails);

        return table;
    }

    public PdfPTable salesDetails() throws DocumentException {

        PdfPTable table = new PdfPTable(3);
        table.setWidths(new int[] { 4, 2,4});
        table.getDefaultCell().setBackgroundColor(HEADER_BACKGROUND);
        table.getDefaultCell().setPadding(20);
        table.getDefaultCell().setBorder(1);
        table.setWidthPercentage(WIDTH_PERCENTAGE);
        table.setSpacingBefore(10);

        Font font1 = FontFactory.getFont(FontFactory.TIMES, 16f, Font.BOLD,BaseColor.BLACK);

       /* Chunk leftDetails = new Chunk(" Sales Order no: ", font1);

        leftDetails.append(this.notification.getMsdOrderNumber())
                .append("\n").append(" ").append("Invoice Date:").append(this.notification.getInvoiceDate())
                .append("\n").append(" ").append("Cust Ref:").append(this.notification.getSoldToCustomerName())
                .append("\n ").append(" ").append("Ship Via: ").append(this.notification.getShipVia());
*/
        Phrase leftPhrase = new Phrase();
        leftPhrase.add(new Chunk(" Sales Order no :  ", font1));
        leftPhrase.add(new Chunk(this.notification.getMsdOrderNumber()));
        leftPhrase.add(Chunk.NEWLINE);
        leftPhrase.add(Chunk.NEWLINE);
        leftPhrase.add(new Chunk(" Invoice Date: ", font1));
        leftPhrase.add(new Chunk(this.notification.getInvoiceDate()));
        leftPhrase.add(Chunk.NEWLINE);
        leftPhrase.add(Chunk.NEWLINE);
        leftPhrase.add(new Chunk(" Cust Ref: ", font1));
        leftPhrase.add(new Chunk(this.notification.getShipToCustomerName()));
        leftPhrase.add(Chunk.NEWLINE);
        leftPhrase.add(Chunk.NEWLINE);
        leftPhrase.add(new Chunk(" Ship Via: ", font1));
        leftPhrase.add(new Chunk(this.notification.getShipVia()));

        Phrase rightPhrase = new Phrase();
        rightPhrase.add(new Chunk(" Sales Category : ", font1));
        rightPhrase.add(new Chunk(this.notification.getSalesCategory()));
        rightPhrase.add(Chunk.NEWLINE);
        rightPhrase.add(Chunk.NEWLINE);
        rightPhrase.add(new Chunk(" Payment Terms : ", font1));
        rightPhrase.add(new Chunk(this.notification.getPaymentTerms()));
        rightPhrase.add(Chunk.NEWLINE);
        rightPhrase.add(Chunk.NEWLINE);
        rightPhrase.add(new Chunk(" Sales Person: ", font1));
        rightPhrase.add(new Chunk(this.notification.getSalesPerson()));
        rightPhrase.add(Chunk.NEWLINE);
        rightPhrase.add(Chunk.NEWLINE);
        rightPhrase.add(new Chunk(" Del Term: ", font1));
        rightPhrase.add(new Chunk(this.notification.getPaymentTerms()));

        PdfPCell titleCell = new PdfPCell(new Phrase(" "));
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        //titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell leftCellDetails = new PdfPCell(leftPhrase);
        leftCellDetails.setFixedHeight(120);
        leftCellDetails.setBorder(Rectangle.NO_BORDER);
        leftCellDetails.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell rightCellDetails = new PdfPCell(rightPhrase);
        rightCellDetails.setFixedHeight(120);
        rightCellDetails.setBorder(Rectangle.NO_BORDER);
        rightCellDetails.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(leftCellDetails);
        table.addCell(titleCell);
        table.addCell(rightCellDetails);

        return table;
    }

    public PdfPTable commentInfo() throws DocumentException {

        PdfPTable table = new PdfPTable(1);
        table.setWidths(new int[] { 12});
        table.getDefaultCell().setBackgroundColor(HEADER_BACKGROUND);
        table.getDefaultCell().setPadding(20);
        table.getDefaultCell().setBorder(1);
        table.setWidthPercentage(WIDTH_PERCENTAGE);
        table.setSpacingBefore(4);

        Font font = FontFactory.getFont(FontFactory.TIMES, 14f, Font.BOLD,BaseColor.BLACK);
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(" Comment : ", font));
        phrase.add(new Chunk(this.notification.getComment()));

        PdfPCell commentCell = new PdfPCell(phrase);
        commentCell.setBorder(Rectangle.BOX);
        commentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        commentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        commentCell.setFixedHeight(30);
        table.addCell(commentCell);
        return table;
    }

    public Paragraph getFullFilledItemsHeader() {
      return new Paragraph("Full Filled Items", H2_FONT);
    }

    public PdfPTable getFullFilledItemsTable() throws IOException, IllegalAccessException, DocumentException, NoSuchFieldException {
        return getTableFor(notification.getFullFilledItems(),notification.getFullFilledItems().get(0).getColumns());
    }


    public Paragraph stockOutItemsHeader() {
      return new Paragraph("Stock Out Items", H2_FONT);
    }

    public PdfPTable getStockOutItemsTable() throws IOException, IllegalAccessException, DocumentException, NoSuchFieldException {
        return getTableFor(notification.getStockOutItems(), notification.getStockOutItems().get(0).getColumns());
    }

    public Paragraph inSufficientFundingHeader() {
        return new Paragraph("InSufficient Funding Items", H2_FONT);
    }

    public PdfPTable getInSufficientFundingTable() throws IOException, IllegalAccessException, DocumentException, NoSuchFieldException {
        return getTableFor(notification.getInSufficientFundingItems(), notification.getInSufficientFundingItems().get(0).getColumns());
    }

    public Paragraph rationingItemsHeader() {

        return new Paragraph("Rationing Items", H2_FONT);
    }

    public PdfPTable rationingItemsTable() throws IOException, IllegalAccessException, DocumentException, NoSuchFieldException {
        return getTableFor(notification.getRationingItems(), notification.getRationingItems().get(0).getColumns());
    }

    public Paragraph closeToExpireItemsHeader() {

        return new Paragraph("Items close to expire", H2_FONT);
    }

    public PdfPTable closeToExpireItemsTable() throws IOException, IllegalAccessException, DocumentException, NoSuchFieldException {
        return getTableFor(notification.getCloseToExpireItems(), notification.getCloseToExpireItems().get(0).getColumns());
    }

    public Paragraph phasedOutItemsHeader() {

        return new Paragraph("Phased Out Items", H2_FONT);
    }

    public PdfPTable phasedOutItemsTable() throws IOException, IllegalAccessException, DocumentException, NoSuchFieldException {
        return getTableFor(notification.getPhasedOutItems(), notification.getPhasedOutItems().get(0).getColumns());
    }



    private PdfPTable getTableFor(java.util.List<? extends NotificationLineItem> lineItems, List<VisibleColumn> columns) throws DocumentException, NoSuchFieldException, IllegalAccessException, IOException {

        PdfPTable table = prepareTable(columns);

        boolean odd = true;

        for (NotificationLineItem lineItem : lineItems) {
            List<PdfPCell> cells = getCells1(columns, lineItem);
            odd = !odd;

            for (PdfPCell cell : cells) {
                cell.setBackgroundColor(odd ? BaseColor.WHITE : ROW_GREY_BACKGROUND);
                table.addCell(cell);
            }
        }
        return table;
    }

    public static List<PdfPCell> getCells1(List<VisibleColumn> visibleColumns,
                                           NotificationLineItem lineItem) throws NoSuchFieldException, IllegalAccessException, IOException, BadElementException {
        List<PdfPCell> result = new ArrayList<>();
        for (VisibleColumn column : visibleColumns) {
            String value = lineItem.getValueFor(column.getName()).toString();
            createCell2(result, TEXT, value,null);
        }
        return result;
    }

    private void setTableHeader(PdfPTable table, List<VisibleColumn> visibleColumns) {
        for (VisibleColumn column : visibleColumns) {
            table.addCell(column.getLabel());
        }
    }
    private void setBlankFooter(PdfPTable table, Integer visibleColumnsSize) {
        PdfPCell cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(0);
        cell.setColspan(visibleColumnsSize);
        cell.setBackgroundColor(BaseColor.WHITE);
        table.addCell(cell);
    }

    private PdfPTable prepareTable(List<VisibleColumn> visibleColumns) throws DocumentException {
        java.util.List<Integer> widths = new ArrayList<>();
        for (VisibleColumn column : visibleColumns) {
            widths.add(column.getColumnWidth());
        }
        PdfPTable table = new PdfPTable(widths.size());

        table.setWidths(ArrayUtils.toPrimitive(widths.toArray(new Integer[widths.size()])));
        table.getDefaultCell().setBackgroundColor(HEADER_BACKGROUND);
        table.getDefaultCell().setPadding(CELL_PADDING);
        table.setWidthPercentage(WIDTH_PERCENTAGE);
        table.setSpacingBefore(10);
        table.setHeaderRows(2);
        table.setFooterRows(1);
        setTableHeader(table, visibleColumns);
        setBlankFooter(table, visibleColumns.size());
        return table;
    }


}
