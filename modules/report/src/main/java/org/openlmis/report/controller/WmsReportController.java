package org.openlmis.report.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.json.JSONArray;
import org.openlmis.core.domain.User;
import org.openlmis.core.repository.UserRepository;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.report.model.wmsreport.Facilities;
import org.openlmis.report.model.wmsreport.StockCards;
import org.openlmis.report.model.wmsreport.VaccineDistribution;
import org.openlmis.report.model.wmsreport.VaccineDistributionLineItem;
import org.openlmis.report.repository.WmsReportRepository;
import org.openlmis.report.service.WmsReportService;
import org.openlmis.report.util.JasperReportCompiler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class WmsReportController extends BaseController {

    @Autowired
    WmsReportRepository wmsReportRepository;
    @Autowired
    private WmsReportService wmsReportService;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/stockonhand-report", params = {"wareHouseId", "docType"})
    public void generateReport(@RequestParam String docType, @RequestParam Long wareHouseId, @RequestParam(required = false, defaultValue = "en") String lang, HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
        Long userId = loggedInUserId(request);
        User userDetails = userRepository.getById(userId);
        String currentName = userDetails.getFullName();
        wmsReportService.exportStockOnHandReport(docType, wareHouseId, lang, currentName, response);
    }

    @RequestMapping(value = "/var-report", params = {"inspectionId"})
    public void varReport(@RequestParam Long inspectionId, @RequestParam(required = false, defaultValue = "en") String lang, HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
        Long userId = loggedInUserId(request);
        User userDetails = userRepository.getById(userId);
        String currentName = userDetails.getFullName();
        wmsReportService.exportVarReport(inspectionId, lang, currentName, response);
    }
    @RequestMapping(value = "/grn-report", params = {"receiveId"})
    public void grnReport(@RequestParam Long receiveId, @RequestParam(required = false, defaultValue = "en") String lang, HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
        Long userId = loggedInUserId(request);
        User userDetails = userRepository.getById(userId);
        String currentName = userDetails.getFullName();
        wmsReportService.exportGrnReport(receiveId, lang, currentName, response);
    }



    @RequestMapping(value = "/picklist-report", params = {"docType", "orderId"})
    public void generateReport(@RequestParam String docType, @RequestParam Long orderId, @RequestParam int type, @RequestParam(required = false, defaultValue = "en") String lang, HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
        ///if(type==1){
        Long userId = loggedInUserId(request);
        User userDetails = userRepository.getById(userId);
        String fullName = userDetails.getFullName();
        wmsReportService.exportReportVaccineDistribution(docType, lang, orderId, fullName, response);


    }


    @RequestMapping(value = "/distribution-report", params = {"facilityId", "docType"})
    public void generateVaccineDistributionReport(@RequestParam String docType, @RequestParam Long facilityId, @RequestParam(required = false, defaultValue = "en") String lang, HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
        Long userId = loggedInUserId(request);
        User userDetails = userRepository.getById(userId);
        String currentName = userDetails.getFullName();
        wmsReportService.exportReportVaccineSummary("PDF", facilityId, currentName, response);
    }

    @RequestMapping(value = "/stock-movement-report", params = {"facilityId", "docType"})
    public void generateStockMovementReport(@RequestParam String docType, @RequestParam Long facilityId, @RequestParam(required = false, defaultValue = "en") String lang, HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
        Long userId = loggedInUserId(request);
        User userDetails = userRepository.getById(userId);
        String currentName = userDetails.getFullName();
        wmsReportService.exportStockMovement("PDF", facilityId, currentName, response);
    }


    @RequestMapping(value = "/proof-delivery-report/{orderId}")
    public void generateProofDeliveryReport(@PathVariable Long orderId, @RequestParam(required = false, defaultValue ="1") int type, @RequestParam(required = false, defaultValue = "en") String lang, HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
        Long userId = loggedInUserId(request);
        User userDetails = userRepository.getById(userId);
        String currentName = userDetails.getFullName();
        wmsReportService.exportReportVaccineDeliveryProof(orderId, lang, currentName, response);
    }

    @RequestMapping(value = "/delivery-report", params = {"distId", "docType"})
    public void generateProofReport(@RequestParam String docType, @RequestParam Long distId, @RequestParam(required = false, defaultValue = "en") String lang, HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
        Long userId = loggedInUserId(request);
        User userDetails = userRepository.getById(userId);
        String currentName = userDetails.getFullName();
        wmsReportService.exportReportVaccineDeliveryProof(distId, lang, currentName, response);
    }

    @RequestMapping(value = "/invoice-report/{orderId}")
    public void generateInvoiceReport(@PathVariable Long orderId, @RequestParam(required = false, defaultValue = "en") String lang, HttpServletRequest request
            , HttpServletResponse response) throws IOException, JRException {
        Long userId = loggedInUserId(request);
        User userDetails = userRepository.getById(userId);
        String currentName = userDetails.getFullName();
        wmsReportService.exportReportVaccineInvoice(orderId,currentName, lang, response);
    }


    /*@RequestMapping(value = "/list-reports/{facilityId}")
    public List<StockCards> getListReport(@PathVariable Long facilityId) {
        return wmsReportRepository.getStockProduct(facilityId);
    }*/

    @RequestMapping(value = "/list-picking")
    public String getPickingItems() {
        try {
            long ID = 3588;
            return wmsReportService.getArrayReport(1, ID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/list-vaccine")
    public String getVaccineItems() {
        try {
            long ID = 1;
            return wmsReportService.getStockProduct(ID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/facility-reports/{facilityId}")
    public Facilities getFacility(@PathVariable Long facilityId) {
        return wmsReportRepository.getFacilityDetails(facilityId);
    }

    @RequestMapping(value = "/list-path")
    public String getListPath() throws FileNotFoundException {
        JasperReportCompiler jasperReportCompiler = new JasperReportCompiler();

        File file = new File(jasperReportCompiler.getReportPath() + "/" + "user-summary.jrxml");

        String pathData = file.getAbsolutePath();

        return pathData;
    }

}
