package org.openlmis.web.controller.warehouse;

import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import org.openlmis.core.domain.ConfigurationSetting;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.core.web.OpenLmisResponse;
import org.openlmis.core.web.controller.BaseController;
import org.openlmis.report.util.Constants;
import org.openlmis.reporting.model.Template;
import org.openlmis.reporting.service.JasperReportsViewFactory;
import org.openlmis.reporting.service.TemplateService;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.Inspection;
import org.openlmis.vaccine.domain.wms.dto.InspectionDTO;
import org.openlmis.vaccine.domain.wms.dto.PutAwayDTO;
import org.openlmis.vaccine.service.warehouse.InspectionService;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static java.lang.Integer.parseInt;
import static org.openlmis.restapi.response.RestResponse.error;
import static org.openlmis.restapi.response.RestResponse.success;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/rest-api/warehouse")
public class InspectionController extends BaseController {

    @Autowired
   private InspectionService service;
    @Autowired
    TemplateService templateService;

    @Autowired
    ConfigurationSettingService settingService;

    @Autowired
    private JasperReportsViewFactory jasperReportsViewFactory;

    @RequestMapping(value = "inspection", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getInspectionByPagination(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                                   @RequestParam(value = "column") String column,
                                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @Value("${search.page.size}") String limit) {
        Pagination pagination = new Pagination(page, parseInt(limit));
        pagination.setTotalRecords(service.getTotalSearchResultCount(searchParam, column));
        List<InspectionDTO> inspectList = service.searchBy(searchParam, column, pagination);
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("inspections", inspectList);
        response.getBody().addData("pagination", pagination);
        return response;
    }

    @RequestMapping(value = "inspection/{id}", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getById(@PathVariable Long id) {

        return OpenLmisResponse.response("inspection",service.getById(id) );
    }

    @RequestMapping(value = "inspection/{id}", method =PUT, headers = ACCEPT_JSON)
    public ResponseEntity update(@RequestBody Inspection inspect, @PathVariable(value = "id") Long id, HttpServletRequest request) {

        try{
            inspect.setId(id);
            inspect.setModifiedBy(loggedInUserId(request));
            service.save(inspect, loggedInUserId(request));

            if(inspect.getStatus().equalsIgnoreCase("RELEASED")) {
                service.updateStockCard(inspect.getId());
            }
            return success("Inspection Updated Successiful");

        } catch (DataException e) {
            return error(e.getOpenLmisMessage(), BAD_REQUEST);
        }
    }


    @RequestMapping(value = "inspection/vvm-status", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getAllVVM() {

        return OpenLmisResponse.response("vvms",service.getAllVVM() );
    }

   @RequestMapping(value = "inspection/reports", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> getBy(@RequestParam(value = "product", required = false) String product,
                                                  @RequestParam(value = "report_key", required = false) String reportKey,
                                                  @RequestParam(value = "startDate", required = false) String startDate,
                                                  @RequestParam(value = "endDate", required = false) String endDate,
                                                  @RequestParam(value = "year", required = false) String year

                                                  ) {

        return OpenLmisResponse.response("reportList",service.getBy(product,reportKey,startDate,endDate, year));
    }


    @RequestMapping(value = "inspection/var/print/{inspectionId}", method = GET, headers = ACCEPT_JSON)
    public ModelAndView printConsolidatedList(@PathVariable Long inspectionId) throws JRException, IOException, ClassNotFoundException {
        Template orPrintTemplate = templateService.getByName("print_avr_report");
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
        map.put("ORDER_ID", inspectionId.intValue());

        return new ModelAndView(jasperView, map);
    }

    @RequestMapping(value = "inspection/put-away", method = GET, headers = ACCEPT_JSON)
    public ResponseEntity<OpenLmisResponse> searchPutAwayBy(@RequestParam(value = "searchParam", required = false) String searchParam,
                                                                      @RequestParam(value = "column") String column,
                                                                      @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                      @Value("${search.page.size}") String limit) {
        Pagination pagination = new Pagination(page, parseInt(limit));
        pagination.setTotalRecords(service.getTotalSearchResultCountForPutAway(searchParam, column));
        List<PutAwayDTO> inspectList = service.searchPutAwayBy(searchParam, column, pagination);
        ResponseEntity<OpenLmisResponse> response = OpenLmisResponse.response("aways", inspectList);
        response.getBody().addData("pagination", pagination);
        return response;
    }


}
