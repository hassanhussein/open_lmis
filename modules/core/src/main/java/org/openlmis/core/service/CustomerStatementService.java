package org.openlmis.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.dto.BudgetLineItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.Base64;

@Service
public class CustomerStatementService {

    public static final String MSD_URL = "MSD_ELMIS_BUDGET_INTERGRATION_URL";
    public static final String MSD_USERNAME = "MSD_ELMIS_BUDGET_INTERGRATION_USERNAME";
    public static final String MSD_PASSWORD = "MSD_ELMIS_BUDGET_INTERGRATION_PASSWORD";

    @Autowired
    ConfigurationSettingService configurationSettingService;

    @Autowired
    private FacilityService facilityService;

    @Async("myExecutor")
    public void getFacilityBalance(Long facilityId, String fromDate, String toDate ) {
        System.out.println(" Started Here");

        System.out.println(facilityId);

        Facility facility = facilityService.getById(facilityId);
        String url = configurationSettingService.getByKey(MSD_URL).getValue();
        String username = configurationSettingService.getByKey(MSD_USERNAME).getValue();
        String password = configurationSettingService.getByKey(MSD_PASSWORD).getValue();

        if(facility != null){

            String jsonString = new JSONObject()
                    .put("CustId", facility.getCode())
                     .put("fromDate", fromDate)
                     .put("toDate",toDate).toString();
            System.out.println(jsonString);
            if (url != null && username != null && password != null) {
                sendHttps(jsonString, url, username, password,fromDate,toDate);
            }

        }


    }

    private void sendHttps(String jsonInString, String url, String username, String password,String fromDate,String toDate) {
        System.out.println(username);
        System.out.println("I'm second ....................");

        ObjectMapper mapper = new ObjectMapper();

        System.out.println(username);
        System.out.println(password);
        System.out.println(url);
        try {
            //String jsonInString = mapper.writeValueAsString(distribution);

            URL obj = new URL(url);

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            // disableSslVerification();
            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            System.out.println("all connection" + con);

            con.setDoOutput(true);

            System.out.println(jsonInString);

            OutputStream wr = con.getOutputStream();
            wr.write(jsonInString.getBytes("UTF-8"));

            wr.flush();
            wr.close();

            System.out.println(con.getErrorStream());

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
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
        } catch (Exception e) {
            System.out.println("e" + e.getMessage());
            e.printStackTrace();
        }


    }

}
