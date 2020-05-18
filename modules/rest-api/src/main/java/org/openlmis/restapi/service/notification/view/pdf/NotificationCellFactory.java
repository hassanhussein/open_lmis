package org.openlmis.restapi.service.notification.view.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.openlmis.core.dto.notification.ColumnType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

@Component
public class NotificationCellFactory {
    public static final float CELL_PADDING = 5f;
    public static final BaseColor HEADER_BACKGROUND = new BaseColor(210, 210, 210);
    public static final Font H2_FONT = FontFactory.getFont(FontFactory.TIMES, 20f, Font.BOLD, BaseColor.BLACK);
    public static final int WIDTH_PERCENTAGE = 100;
    private static final String OK_IMAGE = "images/ok-icon.png";

    public static final Logger logger = Logger.getLogger(NotificationCellFactory.class);

    public static PdfPCell numberCell(String value) {
        PdfPCell cell = getPdfPCell(value);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private static PdfPCell getPdfPCell(String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value));
        cell.setPadding(CELL_PADDING);
        return cell;
    }

    public static PdfPCell textCell(String value) {
        PdfPCell cell = getPdfPCell(value);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    public static PdfPCell headingCell(String value) {
        Chunk chunk = new Chunk(value, H2_FONT);
        PdfPCell cell = new PdfPCell(new Phrase(chunk));
        cell.setPadding(CELL_PADDING);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }


    public static PdfPCell imageCell() throws BadElementException, IOException {
        Resource resource = new ClassPathResource(OK_IMAGE);
        Image image = Image.getInstance(resource.getFile().getAbsolutePath());
        PdfPCell cell = new PdfPCell(image);
        cell.setPadding(CELL_PADDING);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }


    public static void createCell2(List<PdfPCell> result,
                                   ColumnType columnType,
                                   String columnValue,
                                   String currency) throws IOException, BadElementException {
        DecimalFormat moneyFormatter = new DecimalFormat("#,##0.00");
        DecimalFormat formatter = new DecimalFormat("#,##0");


        switch (columnType) {
            case TEXT:
                result.add(textCell(columnValue));
                break;
            case BOOLEAN:
                PdfPCell pdfPCell = Boolean.valueOf(columnValue) ? imageCell() : textCell("");
                result.add(pdfPCell);
                break;
            case NUMERIC:
                if (!columnValue.isEmpty() && NumberUtils.isNumber(columnValue))
                    result.add(numberCell(formatter.format(Double.parseDouble(columnValue))));
                else
                    result.add(numberCell(columnValue));
                break;
            case CURRENCY:
                if (!columnValue.isEmpty() && NumberUtils.isNumber(columnValue))
                    result.add(numberCell(currency + moneyFormatter.format(Double.parseDouble(columnValue))));
                else
                    result.add(numberCell(currency));
                break;

        }
    }


}