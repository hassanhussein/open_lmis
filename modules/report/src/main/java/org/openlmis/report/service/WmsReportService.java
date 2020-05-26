package org.openlmis.report.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.openlmis.report.model.wmsreport.Facilities;
import org.openlmis.report.model.wmsreport.StockCards;
import org.openlmis.report.repository.WmsReportRepository;
import org.openlmis.report.util.JasperReportCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WmsReportService {
    @Autowired
    WmsReportRepository wmsReportRepository;
    public void   exportReport(String reportFormat, Long facilityId, HttpServletResponse response) throws IOException, JRException {
        String path="C:\\Users\\user\\Desktop\\ExpotedPdf";
        List<StockCards> stockList=wmsReportRepository.getListReports(facilityId);

        Facilities facilityDetails=wmsReportRepository.getFacilityDetails(facilityId);


        JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

        File file= new File(jasperReportCompiler.getReportPath()+"/"+"wms-stock-on-hand.jrxml");

        File imagePath=new File(jasperReportCompiler.getReportPathImage()+"/"+"headerimage.png");

        //Load file and compile it

        System.out.println("File Path: "+file.getAbsolutePath());

        JasperReport jasperReport= JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(stockList);


        Map<String,Object> parameters=new HashMap<>();
        parameters.put("createdBy","Felix Joseph");
        parameters.put("StockListData",dataSource);
        parameters.put("facilityName",facilityDetails.getName());
        parameters.put("HEADER_IMAGE",imagePath.getAbsolutePath());
        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        String reportName=Long.toString(System.currentTimeMillis());
        //JasperExportManager.exportReportToPdfFile(jasperPrint,path+"\\"+reportName+".pdf");


      //  File pdf = File.createTempFile(reportName+".", ".pdf");
       // JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));

        response.setContentType("application/x-download");
        response.addHeader("Content-disposition", "attachment; filename=StatisticsrReport1.pdf");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint,out);

        //return "Report generated in Path: "+path;
    }
}
