package org.openlmis.report.controller;

import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.openlmis.report.model.wmsreport.Facilities;
import org.openlmis.report.model.wmsreport.StockCards;
import org.openlmis.report.repository.WmsReportRepository;
import org.openlmis.report.service.WmsReportService;
import org.openlmis.report.util.JasperReportCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@NoArgsConstructor
@RequestMapping(value = "/wms-reports")
public class WmsReportController {
    private String reportPath = "src/main/template";

    @Autowired
    WmsReportRepository wmsReportRepository;
    @Autowired
    private WmsReportService wmsReportService;
    @RequestMapping(value = "/out-of-stock")
    public List<String> getOutOfStock(){
        return getStrings();
    }
    @RequestMapping(value = "/generate-report/{facilityId}/{format}")
    public  void generateReport(@PathVariable String format,@PathVariable Long facilityId,HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
         wmsReportService.exportReport(format,facilityId,response);
    }
    @RequestMapping(value = "/list-reports/{facilityId}")
    public List<StockCards> getListReport(@PathVariable Long facilityId){
        return wmsReportRepository.getListReports(facilityId);
    }

    @RequestMapping(value = "/facility-reports/{facilityId}")
    public Facilities getFacility(@PathVariable Long facilityId){
       return wmsReportRepository.getFacilityDetails(facilityId);
    }
    @RequestMapping(value = "/list-path")
    public String getListPath() throws FileNotFoundException {
        JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

        File file= new File(jasperReportCompiler.getReportPath()+"/"+"user-summary.jrxml");

        String pathData= file.getAbsolutePath();

        System.out.println("File Path: "+pathData);

        return pathData;
    }

    static List<String> getStrings() {
        List<String> stockList=new ArrayList<>();
        stockList.add("Felix");
        stockList.add("Joseph");
        stockList.add("ALex");
        stockList.add("John");
        System.out.println("Hello john");
        return stockList;
    }
}
