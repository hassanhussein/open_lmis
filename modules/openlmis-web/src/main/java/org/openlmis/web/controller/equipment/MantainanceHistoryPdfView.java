package org.openlmis.web.controller.equipment;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import org.openlmis.equipment.dto.Log;
import org.openlmis.rnr.domain.RequisitionStatusChange;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Component("equipmentHistory")
public class MantainanceHistoryPdfView extends AbstractPdfView {

    Font regular = new Font(Font.FontFamily.HELVETICA, 12);
    Font font = new Font(Font.FontFamily.HELVETICA, 12,Font.BOLD);

    private List<Log> logs;
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

            List<Log> logList = (List<Log>) model.get("history");

            Table table = new Table(5);
            table.addCell(String.valueOf(new Chunk("Request Date", regular)));
            table.addCell("Status");
            table.addCell("Remarks");
            table.addCell("Resolved Date");
            table.addCell("verification");
            table.setAlignment(Element.ALIGN_MIDDLE);

        for(Log log : logList) {
             String date = null;

             if(log.getDate() != null) {
                 date = simpleDateFormat.format(log.getDate());
             }

             table.addCell(date);
             table.addCell(log.getType()+"-"+log.getReason());
             table.addCell(log.getComment());
             table.addCell(date);
             table.addCell((log.getApproved())?"Approved":"Not Approved");

            }
            document.add(table);
    }
}
