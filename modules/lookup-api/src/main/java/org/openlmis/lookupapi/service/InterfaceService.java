package org.openlmis.lookupapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.apache.commons.net.util.Base64;
import org.json.JSONObject;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.ProcessingPeriod;
import org.openlmis.core.domain.Product;
import org.openlmis.core.dto.*;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.ProcessingPeriodService;
import org.openlmis.core.service.ProductService;
import org.openlmis.lookupapi.mapper.ILInterfaceMapper;
import org.openlmis.lookupapi.mapper.InterfaceMapper;
import org.openlmis.lookupapi.model.FacilityMsdCodeDTO;
import org.openlmis.lookupapi.model.HFRDTO;
import org.openlmis.lookupapi.model.HealthFacilityDTO;
import org.openlmis.lookupapi.model.MSDStockDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
@NoArgsConstructor
public class InterfaceService {

    private static final String IL_USERNAME = "IL_USERNAME";
    private static final String IL_PASSWORD = "IL_PASSWORD";
    private static final String IL_URL = "IL_URL";
    public static final String DHIS2_URL = "DHIS2_URL";
    private static final String USERNAME = "DHIS2_USERNAME";
    private static final String PASSWORD = "DHIS2_PASSWORD";

    private RestTemplate restTemplate;

    @Autowired
    private ILInterfaceMapper interfaceMapper;

    @Autowired
    private LookupService lookupService;

    @Autowired
    private ConfigurationSettingService settingService;

    @Autowired
    private FacilityService facilityService;
    @Autowired
    private ProductService productService;

    @Autowired
    private InterfaceMapper interfaceApiMapper;

    @Autowired
    private ProcessingPeriodService periodService;

    public void receiveAndSendResponse(HealthFacilityDTO d) {

        lookupService.saveHFR(d);

    }

    public void  sendResponseToHIM(HealthFacilityDTO d) throws InterruptedException  {

        String username = settingService.getByKey(IL_USERNAME).getValue();
        System.out.println(username);
        String password = settingService.getByKey(IL_PASSWORD).getValue();
        System.out.println(password);
        String il_url = settingService.getByKey(IL_URL).getValue();

        if (d != null) {
            HealthFacilityDTO hfr = interfaceMapper.getByTransactionId(d.getIlIDNumber());

            HealthFacilityDTO dto = new HealthFacilityDTO();
            if (hfr != null) {
                dto.setIlIDNumber(hfr.getIlIDNumber());
                dto.setStatus("Success");
                if (il_url != null && username != null && password != null)
                    sendConfirmationMessage(username, password, il_url, dto.getStatus(), hfr.getIlIDNumber());

            } else {
                dto.setIlIDNumber(d.getIlIDNumber());
                dto.setStatus("Fail");
                if (il_url != null && username != null && password != null)
                    sendConfirmationMessage(username, password, il_url, dto.getStatus(), dto.getIlIDNumber());

                System.out.println("Failure Message");
            }


        }

    }

    @Async("myExecutor")
    public void sendResponse(HealthFacilityDTO d) throws InterruptedException {

        String username = settingService.getByKey(IL_USERNAME).getValue();
        System.out.println(username);
        String password = settingService.getByKey(IL_PASSWORD).getValue();
        System.out.println(password);
        String il_url = settingService.getByKey(IL_URL).getValue();
        System.out.println(il_url);
        lookupService.saveHFR(d);

        // Thread.sleep(2000);
        if (d != null) {
            HealthFacilityDTO hfr = interfaceMapper.getByTransactionId(d.getIlIDNumber());

            HealthFacilityDTO dto = new HealthFacilityDTO();
            if (hfr != null) {
                dto.setIlIDNumber(hfr.getIlIDNumber());
                dto.setStatus("Success");
                if (il_url != null && username != null && password != null)
                    sendConfirmationMessage(username, password, il_url, dto.getStatus(), hfr.getIlIDNumber());

            } else {
                dto.setIlIDNumber(d.getIlIDNumber());
                dto.setStatus("Fail");
                if (il_url != null && username != null && password != null)
                    sendConfirmationMessage(username, password, il_url, dto.getStatus(), dto.getIlIDNumber());

                System.out.println("Failure Message");
            }


        }


    }

    private void sendConfirmationMessage(String username, String password, String il_url, String status, String transId) {
        ObjectMapper mapper = new ObjectMapper();
        URL obj = null;
        try {
            obj = new URL(il_url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            HFRDTO hfrdto = new HFRDTO();
            hfrdto.setStatus(status);
            hfrdto.setIL_TransactionIDNumber(transId);

            String jsonInString = mapper.writeValueAsString(hfrdto);

            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + new String(java.util.Base64.getEncoder().encode(userCredentials.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            System.out.println("all connection" + con);

            con.setDoOutput(true);

            System.out.println(jsonInString);

            OutputStream wr = con.getOutputStream();
            wr.write(jsonInString.getBytes("UTF-8"));

            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + il_url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sendBedNetData(String username, String password, String url, ELMISInterfaceDTO data, InterfaceResponseDTO sdp, ResponseExtDTO dto, BudgetDTO budget, SourceOfFundDTO fund, NotificationResponseDTO oosRes) {
        ObjectMapper mapper = new ObjectMapper();
        java.net.URL obj = null;
        try {
            obj = new URL(url);

            System.out.println("is HTTP");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            String jsonInString ="";

            if(oosRes != null) {
                jsonInString = mapper.writeValueAsString(oosRes);
            } else {

                if (budget == null) {

                    if (fund == null) {
                        jsonInString = mapper.writeValueAsString((sdp == null) ? data : dto);
                    } else {

                        jsonInString = mapper.writeValueAsString(fund);
                    }
                } else {
                    jsonInString = mapper.writeValueAsString(budget);

                }
            }

            System.out.println(jsonInString);

            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + new String(java.util.Base64.getEncoder().encode(userCredentials.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            OutputStream wr = con.getOutputStream();
            wr.write(jsonInString.getBytes("UTF-8"));

            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response from DHIS2");
            System.out.println(response);
            //print result

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public List<FacilityMsdCodeDTO> getByHfrCode(String facIdNumber) {

        return interfaceMapper.getByHfrCode(facIdNumber);

    }

    public void activateByMSDFacilityCode(FacilityMsdCodeDTO msd) {
        List<FacilityMsdCodeDTO> list = interfaceMapper.getByHfrCode(msd.getFacIdNumber());
        if(list != null) {
            interfaceMapper.activateByMSDFacilityCode(msd);
        }
    }

    private List<ProcessingPeriod> getCurrentPeriod () {
        return periodService.getByStartDate();
    }


    @Async
    public void methodOne() {

     String username = settingService.getByKey(USERNAME).getValue();
     String password = settingService.getByKey(PASSWORD).getValue();
     String url = settingService.getByKey(DHIS2_URL).getValue();

      if(getCurrentPeriod() != null) {

        for(ProcessingPeriod period: getCurrentPeriod()) {

            List<ZoneDto> zoneDtos = interfaceApiMapper.getZones();

            for(ZoneDto zone: zoneDtos) {
                if(zone != null ) {

                List<ELMISInterfaceDataSetDTO> periods = interfaceApiMapper.getNumberOfExpected(period.getId(), zone.getZoneId());

                if (username != null & password != null & url != null & !periods.isEmpty()) {
                    ELMISInterfaceDTO dto = new ELMISInterfaceDTO();
                    dto.setDataValues(periods);
                    sendBedNetData(username, password, url, dto, null, null, null, null, null);

                }

                }
            }

        }
      }

        System.out.println("Method one called  by Thread : " + Thread.currentThread().getName() + "  at " + new Date());
    }

    @Async
    public void methodTwo() {

        String username = settingService.getByKey(USERNAME).getValue();
        String password = settingService.getByKey(PASSWORD).getValue();
        String url = settingService.getByKey(DHIS2_URL).getValue();

        if(getCurrentPeriod() != null) {

            for(ProcessingPeriod period: getCurrentPeriod()) {

                List<ZoneDto> zoneDtos = interfaceApiMapper.getZones();

                for(ZoneDto zone: zoneDtos) {
                    if(zone != null ) {

                        List<ELMISInterfaceDataSetDTO> periods = interfaceApiMapper.getAvailability(period.getId(), zone.getZoneId());

                        if (username != null & password != null & url != null & !periods.isEmpty()) {
                            ELMISInterfaceDTO dto = new ELMISInterfaceDTO();
                            dto.setDataValues(periods);
                            sendBedNetData(username, password, url, dto, null, null, null, null, null);

                        }

                    }
                }

            }
        }


        System.out.println("Method two called  by Thread : " + Thread.currentThread().getName() + "  at " + new Date());
    }

    @Async
    public void methodThree() {

        String username = settingService.getByKey(USERNAME).getValue();
        String password = settingService.getByKey(PASSWORD).getValue();
        String url = settingService.getByKey(DHIS2_URL).getValue();

        if(getCurrentPeriod() != null) {

            for(ProcessingPeriod period: getCurrentPeriod()) {

                List<ZoneDto> zoneDtos = interfaceApiMapper.getZones();

                for(ZoneDto zone: zoneDtos) {
                    if(zone != null ) {

                        List<ELMISInterfaceDataSetDTO> periods = interfaceApiMapper.getMonthly(period.getId(), period.getScheduleId(), zone.getZoneId());

                        if (username != null & password != null & url != null & !periods.isEmpty()) {
                            ELMISInterfaceDTO dto = new ELMISInterfaceDTO();
                            dto.setDataValues(periods);
                            sendBedNetData(username, password, url, dto, null, null, null, null, null);

                        }

                    }
                }

            }
        }

        System.out.println("Method three called  by Thread : " + Thread.currentThread().getName() + "  at " + new Date());
    }  @Async

    public void methodFour() {

        String username = settingService.getByKey(USERNAME).getValue();
        String password = settingService.getByKey(PASSWORD).getValue();
        String url = settingService.getByKey(DHIS2_URL).getValue();

        if(getCurrentPeriod() != null) {

            for(ProcessingPeriod period: getCurrentPeriod()) {

                List<ZoneDto> zoneDtos = interfaceApiMapper.getZones();

                for(ZoneDto zone: zoneDtos) {
                    if(zone != null ) {

                        List<ELMISInterfaceDataSetDTO> periods = interfaceApiMapper.getOrdered(period.getId(), period.getScheduleId(), zone.getZoneId());

                        if (username != null & password != null & url != null & !periods.isEmpty()) {
                            ELMISInterfaceDTO dto = new ELMISInterfaceDTO();
                            dto.setDataValues(periods);
                            sendBedNetData(username, password, url, dto, null, null, null, null, null);

                        }

                    }
                }

            }
        }

        System.out.println("Method three called  by Thread : " + Thread.currentThread().getName() + "  at " + new Date());
    }

    @Async
    public void methodFive() {

        String username = settingService.getByKey(USERNAME).getValue();
        String password = settingService.getByKey(PASSWORD).getValue();
        String url = settingService.getByKey(DHIS2_URL).getValue();

        if(getCurrentPeriod() != null) {

            for(ProcessingPeriod period: getCurrentPeriod()) {

                List<ZoneDto> zoneDtos = interfaceApiMapper.getZones();

                for(ZoneDto zone: zoneDtos) {
                    if(zone != null ) {

                        List<ELMISInterfaceDataSetDTO> periods = interfaceApiMapper.getQuantityReceived(period.getId(), period.getScheduleId(), zone.getZoneId());

                        if (username != null & password != null & url != null & !periods.isEmpty()) {
                            ELMISInterfaceDTO dto = new ELMISInterfaceDTO();
                            dto.setDataValues(periods);
                            sendBedNetData(username, password, url, dto, null, null, null, null, null);

                        }

                    }
                }

            }
        }

        System.out.println("Method three called  by Thread : " + Thread.currentThread().getName() + "  at " + new Date());
    }
}
