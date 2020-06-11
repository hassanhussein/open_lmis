package org.openlmis.report.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openlmis.report.model.wmsreport.*;
import org.openlmis.report.repository.WmsReportRepository;
import org.openlmis.report.util.JasperReportCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class WmsReportService {
    @Autowired
    WmsReportRepository wmsReportRepository;

    public void exportReport(String reportFormat, Long facilityId,String language, HttpServletResponse response) throws IOException, JRException {
        //String path="C:\\Users\\user\\Desktop\\ExpotedPdf";
        List<StockCards> stockList = wmsReportRepository.getListReports(facilityId);

        Facilities facilityDetails = wmsReportRepository.getFacilityDetails(facilityId);


        JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

        File file = new File(jasperReportCompiler.getReportPath() + "/" + "wms-stock-on-hand.jrxml");

        File imagePath = new File(jasperReportCompiler.getReportPathImage() + "/" + "headerimage.png");

        //Load file and compile it
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stockList);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Felix Joseph");
        parameters.put("StockListData", dataSource);
        parameters.put("facilityName", facilityDetails.getName());
        parameters.put("HEADER_IMAGE", imagePath.getAbsolutePath());
        parameters.put(JRParameter.REPORT_LOCALE,new Locale(language));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        if (reportFormat.equals("pdf")) {
            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "inline; filename=StatisticsrReport1.pdf");
            OutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        } else if (reportFormat.equals("EXCEL")) {
            response.setContentType("application/x-download");
            response.addHeader("Content-disposition", "inline; filename=StatisticsrReport1.pdf");
            OutputStream out = response.getOutputStream();

        }
    }


    public void exportReportVaccineSummary(String reportFormat, HttpServletResponse response) throws IOException, JRException {
        List<VaccineDistributionLineItem> stockList = wmsReportRepository.getReportVaccine();

        JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

        File file = new File(jasperReportCompiler.getReportPath() + "/" + "vaccine-distribution-summary-report.jrxml");

        File imagePath = new File(jasperReportCompiler.getReportPathImage() + "/" + "headerimage.png");

        //Load file and compile it
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stockList);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Felix Joseph");
        parameters.put("VaccineListData", dataSource);
        parameters.put("HEADER_IMAGE", imagePath.getAbsolutePath());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        //JasperExportManager.exportReportToPdfFile(jasperPrint,path+"\\"+reportName+".pdf");

        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "inline; filename=StatisticsrReport1.pdf");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        //return "Report generated in Path: "+path;
    }

    public void exportReportVaccineDistribution(String reportFormat,String language, HttpServletResponse response) throws IOException, JRException {

        long ID=9;
        String vaccineList = getArrayReport(1,ID);
        JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

        File file = new File(jasperReportCompiler.getReportPath() + "/" + "vaccine_picking_list.jrxml");
        String fileProd = jasperReportCompiler.getReportPath() + "/" + "vaccine_distribution_product.jrxml";

        String fileProdLots = jasperReportCompiler.getReportPath() + "/" + "vaccine_distribution_order.jrxml";

        File filePath = new File(fileProd);
        File filePathLots = new File(fileProdLots);
        File imagePath = new File(jasperReportCompiler.getReportPathImage() + "/" + "headerimage.png");

        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(vaccineList.getBytes());

        //Load file and compile it
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JsonDataSource dataSource = new JsonDataSource(jsonDataStream);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Felix Joseph");
        parameters.put("VaccineListData", dataSource);
        parameters.put("HEADER_IMAGE", imagePath.getAbsolutePath());
        parameters.put("vaccine_distribution_product", filePath.getAbsolutePath());
        parameters.put("vaccine_distribution_lots", filePathLots.getAbsolutePath());
        parameters.put(JRParameter.REPORT_LOCALE, new Locale(language));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "inline; filename=StatisticsrReport1.pdf");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        //return "Report generated in Path: "+path;
    }

    public void exportReportVaccineInvoice(Long distId,String language, HttpServletResponse response) throws IOException, JRException {
        String vaccineList = getArrayReport(2,distId);
        JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

        File file = new File(jasperReportCompiler.getReportPath() + "/" + "vaccine_invoice.jrxml");
        String fileProd = jasperReportCompiler.getReportPath() + "/" + "vaccine_invoice_products.jrxml";

      //  String fileProdLots = jasperReportCompiler.getReportPath() + "/" + "vaccine_delivery_prooft_items.jrxml";

        File filePath = new File(fileProd);
        //File filePathLots = new File(fileProdLots);
        File imagePath = new File(jasperReportCompiler.getReportPathImage() + "/" + "headerimage.png");

        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(vaccineList.getBytes());

        //Load file and compile it
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JsonDataSource dataSource = new JsonDataSource(jsonDataStream);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Felix Joseph");
        parameters.put("VaccineListData", dataSource);
        parameters.put("HEADER_IMAGE", imagePath.getAbsolutePath());
        parameters.put("vaccine_distribution_product", filePath.getAbsolutePath());
        //parameters.put("vaccine_distribution_lots", filePathLots.getAbsolutePath());
        parameters.put(JRParameter.REPORT_LOCALE, new Locale(language));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "inline; filename=StatisticsReport1.pdf");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        //return "Report generated in Path: "+path;
    }


    public void exportReportVaccineDeliveryProof(Long distId,String language, HttpServletResponse response) throws IOException, JRException {
        String vaccineList = getArrayReport(2,distId);
        JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

        File file = new File(jasperReportCompiler.getReportPath() + "/" + "vaccine_proof_of_delivery.jrxml");
        String fileProd = jasperReportCompiler.getReportPath() + "/" + "vaccine_distibution_product_proof.jrxml";

        String fileProdLots = jasperReportCompiler.getReportPath() + "/" + "vaccine_delivery_prooft_items.jrxml";

        File filePath = new File(fileProd);
        File filePathLots = new File(fileProdLots);
        File imagePath = new File(jasperReportCompiler.getReportPathImage() + "/" + "headerimage.png");

        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(vaccineList.getBytes());

        //Load file and compile it
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JsonDataSource dataSource = new JsonDataSource(jsonDataStream);


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Felix Joseph");
        parameters.put("VaccineListData", dataSource);
        parameters.put("HEADER_IMAGE", imagePath.getAbsolutePath());
        parameters.put("vaccine_distribution_product", filePath.getAbsolutePath());
        parameters.put("vaccine_distribution_lots", filePathLots.getAbsolutePath());
        parameters.put(JRParameter.REPORT_LOCALE, new Locale(language));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "inline; filename=StatisticsReport1.pdf");
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        //return "Report generated in Path: "+path;
    }




    public String getArrayReport(int queryType,Long distributionID) {
        JSONArray jsonArray = new JSONArray();

        try {
            List<VaccineDistribution> vaccineList =null;
            if(queryType==1){
                vaccineList= wmsReportRepository.getReportVaccineDistribution();
            }else if(queryType==2){
                vaccineList= wmsReportRepository.getVaccineDistributionByID(distributionID);
            }

            String json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(vaccineList);
            JSONArray outputData = new JSONArray(json);
            for (int i = 0; i < outputData.length(); i++) {
                JSONObject vaccineListObject = outputData.getJSONObject(i);

                Long distID = vaccineListObject.getLong("id");
                List<VaccineDistributionLineItem> vaccineListItem = wmsReportRepository.vaccineDistributionLineItemListByDistribution(distID);


                String jsonItem = new ObjectMapper().writerWithDefaultPrettyPrinter()
                        .writeValueAsString(vaccineListItem);
                JSONArray outputDataItem = new JSONArray(jsonItem);
                JSONArray jsonArrayLot = new JSONArray();

                for (int j = 0; j < outputDataItem.length(); j++) {
                    JSONObject vaccineListObjectLot = outputDataItem.getJSONObject(j);
                    long vacItemID = vaccineListObjectLot.getLong("id");
                    List<VaccineDistributionLots> vaccineListItemLOt = wmsReportRepository.vaccineDistributionLotList(vacItemID);

                    vaccineListObjectLot.put("itemLots", vaccineListItemLOt);

                    jsonArrayLot.put(vaccineListObjectLot);


                }

                vaccineListObject.put("distributionListItem", jsonArrayLot);


                jsonArray.put(vaccineListObject);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return jsonArray.toString();
    }


}
