package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import org.openlmis.core.domain.ConfigurationSetting;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.core.service.MessageService;
import org.openlmis.core.service.SupplyPartnerService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.ivdform.domain.Manufacturer;
import org.openlmis.ivdform.service.ManufacturerService;
import org.openlmis.report.util.Constants;
import org.openlmis.reporting.model.Template;
import org.openlmis.reporting.service.JasperReportsViewFactory;
import org.openlmis.reporting.service.TemplateService;
import org.openlmis.restapi.controller.BaseController;
import org.openlmis.restapi.response.RestResponse;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.DocumentType;
import org.openlmis.vaccine.domain.wms.Port;
import org.openlmis.vaccine.domain.wms.Receive;
import org.openlmis.vaccine.service.warehouse.AsnService;
import org.openlmis.vaccine.service.warehouse.DocumentTypeService;
import org.openlmis.vaccine.service.warehouse.PortService;
import org.openlmis.vaccine.service.warehouse.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsMultiFormatView;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

import static java.lang.Integer.parseInt;
import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/warehouse")
public class ReceiveController extends BaseController {

    private static final String GENERATE_GRN = "generate_grn_report";
    @Autowired
    private ReceiveService service;
    @Autowired
    MessageService messageService;

    @Autowired
    DocumentTypeService documentTypeService;

    @Autowired
    PortService portService;

    @Autowired
    ManufacturerService manufacturerService;
    @Autowired
    SupplyPartnerService supplyPartnerService;
    @Autowired
    TemplateService templateService;

    @Autowired
    ConfigurationSettingService settingService;

    @Autowired
    private JasperReportsViewFactory jasperReportsViewFactory;

    @Autowired
    private AsnService asnService;


    public static final String ERROR = "error";
    public static final String SUCESS = "success";


    @RequestMapping(value = "receive", method = POST, headers = ACCEPT_JSON)
    public ResponseEntity<RestResponse> save(@RequestBody Receive receive, Principal principal) {
        try {
            Long userId = loggedInUserId(principal);
            receive.setCreatedBy(userId);
            receive.setModifiedBy(userId);
            Receive savedReceive = service.save(receive, userId, null, false);
            if(receive.getStatus().equalsIgnoreCase("Received")) {
             service.updateStockCardDetails(savedReceive,userId);
            }
            return success("message.success.warehouse.created");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "receive/{id}", method =PUT, headers = ACCEPT_JSON)
    public ResponseEntity update(@RequestBody Receive receive, @PathVariable(value = "id") Long id,Principal principal) {

        try{
            receive.setId(id);
            receive.setModifiedBy(loggedInUserId(principal));

            Receive savedReceive;

            if(receive.getStatus().equalsIgnoreCase("Received")) {
                savedReceive = service.save(receive, loggedInUserId(principal), null,false);

                service.updateInspection(id);

              //  service.updateStockCardDetails(savedReceive,loggedInUserId(principal));
            }else {

               service.save(receive, loggedInUserId(principal), null,true);

            }

            return success("message.success.warehouse.updated");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }

    @RequestMapping(value = "getAll",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAllReceived() {
        return OpenLmisResponse.response("receive", service.getAll());
    }

    @RequestMapping(value = "receive/template",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getReceiveLookupTemplate() {

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

    @RequestMapping(value = "receive/{id}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getBy(@PathVariable Long id) {

        Receive receive = service.getById(id);
        receive.setAsnNumber(asnService.getById(receive.getAsnId()).getAsnnumber());
        receive.setAsnReceiveDate(asnService.getById(receive.getAsnId()).getAsndate());
        return OpenLmisResponse.response("receive", receive);
    }


    @RequestMapping(value = "receive", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getReceiveByPagination(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                               @RequestParam(value = "column") String column,
                                                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               @Value("${search.page.size}") String limit) {
        Pagination pagination = new Pagination(page, parseInt(limit));
        pagination.setTotalRecords(service.getTotalSearchResultCount(searchParam, column));
        List<Receive> receives = service.searchBy(searchParam, column, pagination);
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("receives", receives);
        response.getBody().addData("pagination", pagination);
        return response;
    }

    @RequestMapping(value = "get-id",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getById(@RequestParam(value = "id") Long id) {
        return OpenLmisResponse.response("ids", service.updateInspection(id));
    }

  /*  @RequestMapping(value = "test-case",method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getTested() {
        return OpenLmisResponse.response("receive", service.generateReceiveNumber());
    }*/

    @RequestMapping(value = "/grn-report/{id}/print", method = GET, headers = ACCEPT_JSON)
    public ModelAndView printOrder(@PathVariable Long id) throws JRException, IOException, ClassNotFoundException {
        Template orPrintTemplate = templateService.getByName(GENERATE_GRN);

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
