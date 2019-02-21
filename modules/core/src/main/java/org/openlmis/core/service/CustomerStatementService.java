package org.openlmis.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openlmis.core.domain.BudgetLineItem;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.dto.BudgetDTO;
import org.openlmis.core.dto.BudgetLineItemDTO;
import org.openlmis.core.repository.BudgetLineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class CustomerStatementService {

    public static final String MSD_URL = "MSD_ELMIS_BUDGET_INTERGRATION_URL";
    public static final String MSD_USERNAME = "MSD_ELMIS_BUDGET_INTERGRATION_USERNAME";
    public static final String MSD_PASSWORD = "MSD_ELMIS_BUDGET_INTERGRATION_PASSWORD";


    @Autowired
    ConfigurationSettingService configurationSettingService;
    @Autowired
    BudgetLineItemRepository repository;

    @Autowired
    private FacilityService facilityService;

    private static final Log log = LogFactory.getLog(CustomerStatementService.class);

    public void getFacilityBalance(Long facilityId, String fromDate, String toDate) {
        System.out.println("Running second");

        String jsonData = "";
        Facility facility = facilityService.getById(facilityId);
        String url = configurationSettingService.getByKey(MSD_URL).getValue();
        String username = configurationSettingService.getByKey(MSD_USERNAME).getValue();
        String password = configurationSettingService.getByKey(MSD_PASSWORD).getValue();

        if (facility != null) {

            jsonData = "{\n" +
                    "    \"CustID\":\"" + facility.getCode() + "\",\n" +
                    "    \"fromDate\":\"" + fromDate + "\",\n" +
                    "    \"toDate\":\"" + toDate + "\"\n" +
                    "}";
            if (url != null && username != null && password != null) {
                getCustomData(jsonData, url, username, password, facility);
            }

        }


    }

    private void getCustomData(String jsonInString, String url, String username, String password,Facility facility) {

        HttpURLConnection urlConnection;
        String result = null;
        try {

            String auth = new String(username + ":" + password);
            byte[] data1 = auth.getBytes(UTF_8);
            // String base64 = Base64.encodeToString(data1, Base64.NO_WRAP);
            //Connect
            urlConnection = (HttpURLConnection) ((new URL(url).openConnection()));
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            //  urlConnection.setRequestProperty("Authorization", "Basic "+base64);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(10000);
            urlConnection.connect();
            //Write
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(jsonInString);
            writer.close();
            outputStream.close();
            log.debug(urlConnection.getResponseMessage());
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                //Read
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                result = sb.toString();
                System.out.println(result);

                ObjectMapper mapper = new ObjectMapper();
                BudgetLineItemDTO[] budget = mapper.readValue(result,BudgetLineItemDTO[].class);
                BudgetLineItemDTO dtos = budget[budget.length-1];
                dtos.setFacilityId(facility.getId());
                dtos.setProgramId(1L);
                if(0 > Long.valueOf(dtos.getAllocatedBudget())){
                    dtos.setAdditive(false);
                    dtos.setAllocatedBudget(String.valueOf(Long.valueOf(Long.valueOf(dtos.getAllocatedBudget()) * -1L)));
                }
                dtos.setAdditive(-1 != Long.valueOf(dtos.getAllocatedBudget()));
                repository.saveLineItemDTO(dtos);
                log.debug("Results  "+budget[0].getAllocatedBudget());
            } else {

                log.debug("Network is not available: " + responseCode);

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
