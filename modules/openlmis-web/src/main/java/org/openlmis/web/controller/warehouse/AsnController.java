package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import org.apache.log4j.Logger;
import org.openlmis.core.domain.ConfigurationSetting;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.core.service.MessageService;
import org.openlmis.core.service.SupplyPartnerService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.ivdform.domain.Manufacturer;
import org.openlmis.ivdform.service.ManufacturerService;
import org.openlmis.report.util.Constants;
import org.openlmis.reporting.model.Template;
import org.openlmis.reporting.service.JasperReportsViewFactory;
import org.openlmis.reporting.service.TemplateService;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.vaccine.domain.wms.*;
import org.openlmis.vaccine.dto.CurrencyDTO;
import org.openlmis.vaccine.service.warehouse.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import static java.lang.Integer.parseInt;
import static org.openlmis.core.web.OpenLmisResponse.response;
import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/warehouse")
public class AsnController extends BaseController {
    private static final String PRINT_ASN = "Print Asn";

    @Autowired
    AsnService asnService;
    @Autowired
    AsnLineItemService asnLineItemService;
    @Autowired
    AsnLotService asnLotService;
    @Autowired
    PurchaseDocumentService purchaseDocumentService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    PortService portService;
    @Autowired
    ManufacturerService manufacturerService;
    @Autowired
    SupplyPartnerService supplyPartnerService;

    @Autowired
    MessageService messageService;

    @Autowired
    TemplateService templateService;

    @Autowired
    ConfigurationSettingService settingService;

    @Autowired
    private JasperReportsViewFactory jasperReportsViewFactory;

    public static final String ERROR = "error";
    public static final String SUCESS = "success";

    public static final String UPLOAD_FILE_SUCCESS = "File uploaded successfully";

    @Value("${wms.document.uploadLocation}")
    private String fileStoreLocation;
    @Value("${wms.document.accessBaseUrl}")
    private String fileAccessBaseUrl;


    public static final String FILE_UPLOAD_LIST = "fileUploadList";
    private static final Logger LOGGER = Logger.getLogger(AsnController.class);
    public static final String SUCCESS = "success";


    @RequestMapping(value = "asn", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> save(@RequestBody Asn asn, HttpServletRequest principal) {
        try {
            Long userId = loggedInUserId(principal);
            asn.setCreatedBy(userId);
            asn.setModifiedBy(userId);
            asnService.save(asn, userId);
            return success("message.success.warehouse.created");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "document-types", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getDocumentTypes() {
        return OpenLmisResponse.response("documents", documentTypeService.getAll());
    }
    @RequestMapping(value = "ports", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getPorts() {
        return OpenLmisResponse.response("ports", portService.getAll());
    }
    @RequestMapping(value = "all",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> retriveAllAsNs() {
        return OpenLmisResponse.response("asns", asnService.getAll());
    }
    @RequestMapping(value = "asn/template",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAsnLookupTemplate() {

        List<DocumentType> documentTypes = documentTypeService.getAll();
        List<Port> ports = portService.getAll();
        List<Manufacturer> manufacturers = manufacturerService.getAll();
        List<SupplyPartner> supplyPartners = supplyPartnerService.getAll();
        List<CurrencyDTO> currencies = asnService.getAllCurrencies();
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("documentTypes", documentTypes);
        response.getBody().addData("ports", ports);
        response.getBody().addData("manufactures", manufacturers);
        response.getBody().addData("suppliers", supplyPartners);
        response.getBody().addData("currencies", currencies);
        return response;
    }
    @RequestMapping(value = "asn", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAsnByPagination(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                               @RequestParam(value = "column") String column,
                                                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               @Value("${search.page.size}") String limit) {
        Pagination pagination = new Pagination(page, parseInt(limit));
        pagination.setTotalRecords(asnService.getTotalSearchResultCount(searchParam, column));
        List<Asn> asns = asnService.searchBy(searchParam, column, pagination);
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("asns", asns);
        response.getBody().addData("pagination", pagination);
        return response;
    }
    @RequestMapping(value = "asn/{id}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getById(@PathVariable Long id) {
         Asn list = asnService.getById(id);
         List<PurchaseDocument> documentList;
         String uriPath;
         uriPath = this.fileAccessBaseUrl;
         documentList = this.purchaseDocumentService.getByAsnId(id);
         for(PurchaseDocument document: documentList) {
             String documentUrl = document.getFileLocation();
             document.setFileLocation(documentUrl);
         }
         list.setPurchaseDocuments(documentList);
         return OpenLmisResponse.response("asn",list );
    }
    @RequestMapping(value = "asn/{id}", method =PUT, headers = ACCEPT_JSON)
    public ResponseEntity update(@RequestBody Asn asn, @PathVariable(value = "id") Long id,HttpServletRequest request) {

       try{
        asn.setId(id);
        asn.setModifiedBy(loggedInUserId(request));
        asnService.save(asn, loggedInUserId(request));
        return success("message.success.warehouse.updated");

    } catch (DataException e) {
        return error(e.getOpenLmisMessage(), BAD_REQUEST);
    }
    }

    private  ResponseEntity<OpenLmisResponse> successPage(int recordsProcessed) {
        Map<String, String> responseMessages = new HashMap<>();
        String message = messageService.message(UPLOAD_FILE_SUCCESS, recordsProcessed);
        responseMessages.put(SUCCESS, message);
        return response(responseMessages, OK, TEXT_HTML_VALUE);
    }

    private static ResponseEntity<OpenLmisResponse> errorPage(String message) {
        Map<String, String> responseMessages = new HashMap<>();
        responseMessages.put(ERROR, message);
        return response(responseMessages, NOT_FOUND, TEXT_HTML_VALUE);
    }

    //Upload file function
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam(value="file") MultipartFile asnDocuments, HttpServletRequest request) throws IOException {

        String asnNumber = request.getParameter("params");
        String documentType = request.getParameter("documentType");
        return saveUploadedFiles(asnDocuments,asnNumber,documentType,loggedInUserId(request));
    }

    private String saveUploadedFiles(MultipartFile file, String asnNumber,String documentType,Long userId) {

        String fileName;
        String filePath;
        FileOutputStream outputStream = null;

        if (file.isEmpty()) {
            return "Failed to Store Empty File";
        }

        try {

            InputStream inputStream;
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-"));
            byte[] byteFile;
            Document document = new Document();
            fileName = asnNumber+"-"+file.getOriginalFilename();
            filePath = this.fileStoreLocation + fileName;
            inputStream = file.getInputStream();
            int val = inputStream.available();
            document.setFileLocation(fileName);
            document.setCreatedDate(new Date());
            document.setCreatedBy(userId);
            document.setDocumentType(documentTypeService.getByName(documentType));
            document.setAsnNumber(asnNumber);
            byteFile = new byte[val];
            inputStream.read(byteFile);
            File newFile = new File(filePath);
            File directory = new File(this.fileStoreLocation);

            boolean isFileExist = directory.exists();

            if(!isFileExist) {

                Path mypath = Paths.get(this.fileStoreLocation);
                Files.createDirectories(mypath);
                LOGGER.debug("Directory created");

            }

            boolean isWritePermitted = directory.canWrite();
            if (isWritePermitted) {

                outputStream = new FileOutputStream(newFile);
                outputStream.write(byteFile);
                outputStream.flush();
                outputStream.close();
              this.purchaseDocumentService.saveDocument(document);

            } else {
                return "No Permission To Upload At Specified Path";
            }

        } catch (Exception  ex){

            LOGGER.warn("Cannot upload in this location",ex);
            return "Cannot upload in this location";

        }
        return  "Successfully Uploaded";
    }


    @RequestMapping(value = "/asn/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteLocation(@PathVariable("id") Long id) {

        try {
            asnService.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            return OpenLmisResponse.error("asn.data.already.in.use", HttpStatus.BAD_REQUEST);
        }

        return OpenLmisResponse.success("message.asn.deleted.success");
    }


    @RequestMapping("/downloadFile")
    public void downloadFile(@RequestParam String filename, HttpServletResponse response) throws IOException {
        OutputStream outputStream = null;
        InputStream in = null;

        System.out.println(filename);

        try {
            in = new FileInputStream(this.fileStoreLocation + filename);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            response.setHeader(
                    "Content-Disposition",
                    "attachment;filename=\"" + filename + "\"");
            outputStream = response.getOutputStream();
            while( 0 < ( bytesRead = in.read( buffer ) ) )
            {
                outputStream.write( buffer, 0, bytesRead );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally
        {
            if ( null != in )
            {
                in.close();
            }
        }

    }

    @RequestMapping(value = "documentList/{code}", method =GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getDocumentList(@PathVariable(value = "code") String code,HttpServletRequest request) {

            return OpenLmisResponse.response("list",purchaseDocumentService.getByASNCode(code));
    }

    @RequestMapping(value = "deleteDocument/{id}/{code}", method = GET)
    public ResponseEntity<OpenLmisResponse> deleteFile(@PathVariable(value = "id") Long id,
                                                       @PathVariable(value = "code") String code) {

        return OpenLmisResponse.response("list", purchaseDocumentService.deleteById(id,code));
    }


    @RequestMapping(value = "/asn/disable/{id}", method = PUT)
    public ResponseEntity disableAsnBy(@PathVariable("id") Long id) {

        try {
            asnService.disableAsnBy(id);
        } catch (DataIntegrityViolationException ex) {
            return OpenLmisResponse.error("asn.data.already.in.use", HttpStatus.BAD_REQUEST);
        }

        return OpenLmisResponse.success("message.asn.deleted.success");
    }

    @RequestMapping(value = "/all-currencies", method =GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAllCurrencies(HttpServletRequest request) {

        return OpenLmisResponse.response("list",asnService.getAllCurrencies());
    }

    @RequestMapping(value = "/print{id}/asn", method = GET, headers = ACCEPT_JSON)
    public ModelAndView printOrder(@PathVariable Long id) throws JRException, IOException, ClassNotFoundException {
        Template orPrintTemplate = templateService.getByName(PRINT_ASN);

        JasperReportsMultiFormatView jasperView = jasperReportsViewFactory.getJasperReportsView(orPrintTemplate);
        Map<String, Object> map = new HashMap<>();
        map.put("format", "pdf");

        Locale currentLocale = messageService.getCurrentLocale();
        map.put(JRParameter.REPORT_LOCALE, currentLocale);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
        map.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
        Resource reportResource = new ClassPathResource("report");
        Resource imgResource = new ClassPathResource("images");
        ConfigurationSetting configuration = settingService.getByKey(Constants.OPERATOR_NAME);
        map.put(Constants.OPERATOR_NAME, configuration.getValue());

        String separator = System.getProperty("file.separator");
        map.put("image_dir", imgResource.getFile().getAbsolutePath() + separator);
        map.put("ORDER_ID", id.intValue());

        return new ModelAndView(jasperView, map);
    }

}
