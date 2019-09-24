package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.MessageService;
import org.openlmis.core.service.SupplyPartnerService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.ivdform.domain.Manufacturer;
import org.openlmis.ivdform.service.ManufacturerService;
import org.openlmis.restapi.controller.BaseController;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.vaccine.domain.wms.ASNDocument;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.DocumentType;
import org.openlmis.vaccine.domain.wms.Port;
import org.openlmis.vaccine.service.warehouse.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public static final String ERROR = "error";
    public static final String SUCESS = "success";

    public static final String UPLOAD_FILE_SUCCESS = "File uploaded successfully";

    @Value("${wms.document.uploadLocation}")
    private String fileStoreLocation;
    @Value("${help.document.accessBaseUrl}")
    private String fileAccessBaseUrl;


    public static final String FILE_UPLOAD_LIST = "fileUploadList";
    private static final Logger LOGGER = Logger.getLogger(AsnController.class);
    public static final String SUCCESS = "success";


    @RequestMapping(value = "asn", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> save(@RequestBody Asn asn, Principal principal) {
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
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("document-types", documentTypes);
        response.getBody().addData("ports", ports);
        response.getBody().addData("manufactures", manufacturers);
        response.getBody().addData("suppliers", supplyPartners);
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
        return OpenLmisResponse.response("asn", asnService.getById(id));
    }
    @RequestMapping(value = "asn/{id}", method =PUT, headers = ACCEPT_JSON)
    public ResponseEntity update(@RequestBody Asn asn, @PathVariable(value = "id") Long id,Principal principal) {

       try{
        asn.setId(id);
        asn.setModifiedBy(loggedInUserId(principal));
        asnService.save(asn, loggedInUserId(principal));
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

        return saveUploadedFiles(asnDocuments);
    }

    private String saveUploadedFiles(MultipartFile file) {

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
            fileName = file.getOriginalFilename();
            filePath = this.fileStoreLocation + fileName;
            inputStream = file.getInputStream();
            int val = inputStream.available();
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

}
