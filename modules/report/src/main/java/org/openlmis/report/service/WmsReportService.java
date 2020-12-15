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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Service
public class WmsReportService {
    @Autowired
    WmsReportRepository wmsReportRepository;

    public void exportStockOnHandReport(String reportFormat, Long wareHouseId, String language, String currentName, HttpServletResponse response) throws IOException, JRException {

        try {
            String stockList = getStockProduct(wareHouseId);


            ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(stockList.getBytes());

            JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

            File imagePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath( "template/headerimage.png"));


            File file = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/wms-stock-hand.jrxml"));
            File fileSubReport = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/substockonhand.jrxml"));

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JsonDataSource dataSource = new JsonDataSource(jsonDataStream);


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", currentName);
            parameters.put("StockListData", dataSource);
            parameters.put("wareHouseId", wareHouseId);
            parameters.put("HEADER_IMAGE", imagePath.getAbsolutePath());
            parameters.put("product_item_template", fileSubReport.getAbsolutePath());
            parameters.put(JRParameter.REPORT_LOCALE, new Locale(language));

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
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    public void exportGrnReport(Long receiveId,String language,String currentName, HttpServletResponse response)throws IOException, JRException{
        try {
            // String dataList= String.valueOf(wmsReportRepository.getVarReportById(inspectionId));
            List<HashMap<String, Object>> dataListData=wmsReportRepository.getGrnReportById(receiveId);


            String json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(dataListData);
            ArrayList<String> dataListStorage=wmsReportRepository.getStorageLocation(receiveId);


            String storageString=dataListStorage.toString();
            // System.out.println(dataListStorage.toString());
            //System.out.println(json);

            ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(json.getBytes());

            JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

            File imagePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath( "template/headerimage.png"));


            File file = ResourceUtils.getFile(jasperReportCompiler.getReportPath("report/grn-report.jrxml"));

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JsonDataSource dataSource = new JsonDataSource(jsonDataStream);


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", currentName);
            parameters.put("storageString",storageString);

            parameters.put("OPERATOR_NAME", currentName);

            parameters.put("HEADER_IMAGE", imagePath.getAbsolutePath());
            parameters.put(JRParameter.REPORT_LOCALE, new Locale(language));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "inline; filename=StatisticsrReport1.pdf");
            OutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void exportVarReport(Long inspectionId,String language,String currentName, HttpServletResponse response)throws IOException, JRException{
        try {
           // String dataList= String.valueOf(wmsReportRepository.getVarReportById(inspectionId));
            List<HashMap<String, Object>> dataListData=wmsReportRepository.getVarReportById(inspectionId);


            String json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(dataListData);
            ArrayList<String> dataListStorage=wmsReportRepository.getStorageLocation(inspectionId);


            String storageString=dataListStorage.toString();
           // System.out.println(dataListStorage.toString());
           // System.out.println(json);

            ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(json.getBytes());

            JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

            File imagePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath( "template/headerimage.png"));


            File file = ResourceUtils.getFile(jasperReportCompiler.getReportPath("report/avr-report.jrxml"));

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JsonDataSource dataSource = new JsonDataSource(jsonDataStream);


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", currentName);
            parameters.put("storageString",storageString);

            parameters.put("OPERATOR_NAME", currentName);

            parameters.put("HEADER_IMAGE", imagePath.getAbsolutePath());
            parameters.put(JRParameter.REPORT_LOCALE, new Locale(language));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

                response.setContentType("application/pdf");
                response.addHeader("Content-disposition", "inline; filename=StatisticsrReport1.pdf");
                OutputStream out = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void exportReportVaccineSummary(String reportFormat, Long facilityId, String currentName, HttpServletResponse response) throws IOException, JRException {
        try {
            List<VaccineDistributionLineItem> stockList = wmsReportRepository.getReportVaccine(facilityId);

            JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

            File file = new File(jasperReportCompiler.getReportPath() + "/" + "vaccine-distribution-summary-report.jrxml");

            File imagePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/headerimage.png"));

            //Load file and compile it
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stockList);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", currentName);
            parameters.put("VaccineListData", dataSource);
            parameters.put("HEADER_IMAGE", imagePath.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            //JasperExportManager.exportReportToPdfFile(jasperPrint,path+"\\"+reportName+".pdf");

            response.setContentType("application/pdf");
            response.addHeader("Content-disposition", "inline; filename=StatisticsrReport1.pdf");
            OutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            //return "Report generated in Path: "+path;
        }catch (Exception e){

        }
    }

    public void exportReportVaccineDistribution(String reportFormat, String language, Long orderId, String fullName, HttpServletResponse response) throws IOException, JRException {

        try {

            String vaccineList = getArrayReport(1, orderId);
            JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();
            File imagePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/headerimage.png"));

            File file =ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/vaccine_picking_list.jrxml"));

            File filePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/vaccine_distribution_product.jrxml"));
            File filePathLots =ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/vaccine_distribution_order.jrxml"));

            ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(vaccineList.getBytes());

            //Load file and compile it
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JsonDataSource dataSource = new JsonDataSource(jsonDataStream);


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", fullName);
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void exportReportVaccineInvoice(Long orderId,String currentName, String language, HttpServletResponse response) throws IOException, JRException {

        try {
            String vaccineList = getArrayReport(2, orderId);
            JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

            File file = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/vaccine_invoice.jrxml"));

            //  String fileProdLots = jasperReportCompiler.getReportPath() + "/" + "vaccine_delivery_prooft_items.jrxml";

            File filePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/vaccine_invoice_products.jrxml"));
            //File filePathLots = new File(fileProdLots);
            File imagePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/headerimage.png"));

            ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(vaccineList.getBytes());

            //Load file and compile it
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JsonDataSource dataSource = new JsonDataSource(jsonDataStream);


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", currentName);
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void exportReportVaccineDeliveryProof(Long orderId, String language, String currentName, HttpServletResponse response) throws IOException, JRException {

        try {
            String vaccineList = getArrayReport(2, orderId);
            //System.out.println(vaccineList);
            JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

            File file = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/vaccine_proof_of_delivery.jrxml"));

            File filePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/vaccine_distibution_product_proof.jrxml"));
            File filePathLots = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/vaccine_delivery_prooft_items.jrxml"));
            File imagePath = ResourceUtils.getFile(jasperReportCompiler.getReportPath("template/headerimage.png"));

            ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(vaccineList.getBytes());

            //Load file and compile it
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JsonDataSource dataSource = new JsonDataSource(jsonDataStream);


            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", currentName);
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

        }catch (Exception e){
            e.printStackTrace();
        }

        //return "Report generated in Path: "+path;
    }


    public String getStockProduct(Long ID){
        JSONArray jsonArray = new JSONArray();

        try {
            List<StockCard> stockList = wmsReportRepository.getListStockProduct(ID);
            String json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(stockList);
            JSONArray outputData = new JSONArray(json);
            for (int i = 0; i < outputData.length(); i++) {
                JSONObject stockListObject = outputData.getJSONObject(i);

                Long productID = stockListObject.getLong("productId");

                List<StockCards> stockListProd = wmsReportRepository.getListReports(productID,ID);

                stockListObject.put("productItem",stockListProd);

                String jsonProd = new ObjectMapper().writerWithDefaultPrettyPrinter()
                        .writeValueAsString(stockListProd);
                JSONArray outputDataProd= new JSONArray(jsonProd);

                int totalProduct=0;
                for (int j = 0; j < outputDataProd.length(); j++) {
                    JSONObject stockListObjectProd = outputDataProd.getJSONObject(j);

                    int totalQuantityOnHand=stockListObjectProd.getInt("totalQuantityOnHand");
                    totalProduct=totalProduct+totalQuantityOnHand;
                }


                stockListObject.put("totalQuantityOnHand",totalProduct);
                jsonArray.put(stockListObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();
    }
    public String getArrayReport(int queryType, Long ID) {
        JSONArray jsonArray = new JSONArray();

        try {
            List<VaccineDistribution> vaccineList = null;
            if (queryType == 1) {
                vaccineList = wmsReportRepository.getReportVaccineDistribution(ID);
            } else if (queryType == 2) {
                vaccineList = wmsReportRepository.getVaccineDistributionByID(ID);
            }

            String json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(vaccineList);
            JSONArray outputData = new JSONArray(json);
            //System.out.println(outputData);
            for (int i = 0; i < outputData.length(); i++) {
                JSONObject vaccineListObject = outputData.getJSONObject(i);

                Long distID = vaccineListObject.getLong("id");
                List<VaccineDistributionLineItem> vaccineListItem = wmsReportRepository.vaccineDistributionLineItemListByDistribution(distID);


                String jsonItem = new ObjectMapper().writerWithDefaultPrettyPrinter()
                        .writeValueAsString(vaccineListItem);


                JSONArray outputDataItem = new JSONArray(jsonItem);
                JSONArray jsonArrayLot = new JSONArray();

                int sumAmount=0;
                for (int j = 0; j < outputDataItem.length(); j++) {
                    JSONObject vaccineListObjectLot = outputDataItem.getJSONObject(j);
                    long vacItemID = vaccineListObjectLot.getLong("id");
                    List<VaccineDistributionLots> vaccineListItemLOt = wmsReportRepository.vaccineDistributionLotList(vacItemID);

                    int unitPrice=vaccineListObjectLot.getInt("unitPrice");
                    int quantityTotal=vaccineListItemLOt.stream().mapToInt(VaccineDistributionLots::getQuantity).sum();

                    int amount= unitPrice* quantityTotal;

                    sumAmount=sumAmount+amount;

                    if(vaccineListItemLOt.size()>0) {
                        vaccineListObjectLot.put("quantityIssued", quantityTotal);
                        vaccineListObjectLot.put("itemLots", vaccineListItemLOt);
                        vaccineListObjectLot.put("amount", amount);

                        jsonArrayLot.put(vaccineListObjectLot);
                    }


                }

                    vaccineListObject.put("sumAmount",sumAmount);
                    vaccineListObject.put("distributionListItem", jsonArrayLot);



                jsonArray.put(vaccineListObject);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return jsonArray.toString();
    }


}
