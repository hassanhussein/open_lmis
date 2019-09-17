/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 *   Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 *   This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.core.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openlmis.core.domain.*;
import org.openlmis.core.dto.*;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.repository.ELMISInterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Service
@EnableScheduling
public class ELMISInterfaceService {

    private static final String FACILITY_MAPPING_KEY = "FACILITY_ELMIS_MAPPING";
    @Autowired
    private ELMISInterfaceRepository repository;
    @Autowired
    private ConfigurationSettingService settingService;
    @Autowired
    private FacilityService facilityService;

    public static final String URL = "LLIN_DHIS_URL";
    private static final String URL2 = "LLIN_DHIS_SECOND_URL";
    private static final String ELMIS_SDP_URL = "ELMIS_SDP_URL";
    private static final String ELMIS_SDP_USERNAME = "ELMIS_SDP_USERNAME";
    private static final String ELMIS_SDP_PASS_WORD = "ELMIS_SDP_PASSWORD";
    private static final String USERNAME = "LLIN_USERNAME";
    private static final String PASSWORD = "LLIN_PASSWORD";
    private static final String IL_BUDGET_USERNAME = "IL_BUDGET_USERNAME";
    private static final String IL_BUDGET_PASSWORD = "IL_BUDGET_PASSWORD";
    private static final String IL_BUDGET_URL = "IL_BUDGET_URL";



    public ELMISInterface get(long interfaceId) {
        return repository.get(interfaceId);
    }

    public void save(ELMISInterface elmisInterface) {

        if (elmisInterface.getId() != null)
            repository.update(elmisInterface);
        else
            repository.insert(elmisInterface);

        repository.updateELMISInterfaceDataSets(elmisInterface);
    }

    public List<ELMISInterface> getAllInterfaces() {
        return repository.getAllInterfaces();
    }

    public List<ELMISInterfaceFacilityMapping> getInterfaceFacilityMappings() {
        return repository.getInterfaceFacilityMappings();
    }

    public List<ELMISInterfaceFacilityMapping> getFacilityInterfaceMappingById(Long facilityId) {
        return repository.getFacilityInterfaceMappingById(facilityId);
    }

    public List<ELMISInterface> getAllActiveInterfaces() {
        return repository.getAllActiveInterfaces();
    }

    public void updateFacilityInterfaceMapping(Facility facility) {
        repository.updateFacilityInterfaceMapping(facility);
    }


    // @Scheduled(cron = "${batch.job.send.bed.net.data}")
    // @Scheduled(fixedRate = 900000)
    public void processMosquitoNetReportingData() {
        //Populate Data
        // repository.refreshMaterializedView();
        String username = settingService.getByKey(USERNAME).getValue();
        String password = settingService.getByKey(PASSWORD).getValue();
        String url = settingService.getByKey(URL2).getValue();

        ELMISInterfaceDTO dto = new ELMISInterfaceDTO();

        if (username != null & password != null & url != null) {
            dto.setDataValues(repository.getMosquitoNetReportingRateData());
            sendBedNetData(username, password, url, dto, null,null,null,null);
        }

    }

    // @Scheduled(cron = "${batch.job.send.bed.net.data}")
    ///@Scheduled(fixedRate = 900000)
    public void processMosquitoNetData() {
        //Populate Data
        repository.refreshMaterializedView();
        String username = settingService.getByKey(USERNAME).getValue();
        String password = settingService.getByKey(PASSWORD).getValue();
        String url = settingService.getByKey(URL).getValue();

        ELMISInterfaceDTO dto = new ELMISInterfaceDTO();

        if (username != null & password != null & url != null) {
            dto.setDataValues(repository.getMosquitoNetData());
            sendBedNetData(username, password, url, dto, null,null,null,null);
        }

    }

    private void sendBedNetData(String username, String password, String url, ELMISInterfaceDTO data, InterfaceResponseDTO sdp,ResponseExtDTO dto,BudgetDTO budget,SourceOfFundDTO fund) {
        ObjectMapper mapper = new ObjectMapper();
        java.net.URL obj = null;
        try {
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            String jsonInString ="";

            if(budget == null){
             if(fund == null) {
                 jsonInString = mapper.writeValueAsString((sdp == null) ? data : dto);
             }else {

                 jsonInString = mapper.writeValueAsString(fund);
             }
            }else {
            jsonInString = mapper.writeValueAsString(budget);

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
            System.out.println(response);
            //print result

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public ELMISResponseMessageDTO getResponseMessageByCode(String code) {
        return repository.getResponseMessageByCode(code);
    }


    public FacilityMappingDTO getFacilityMappingByCode(FacilityMappingDTO facility) {
        return repository.getByMappedCode(facility.getMappedId());
    }

    public void insertFacility(FacilityMappingDTO facility) {

        FacilityMappingDTO dto = repository.getByMappedCode(facility.getMappedId());
        String interFaceKey = settingService.getByKey(FACILITY_MAPPING_KEY).getValue();

        if (interFaceKey == null)
            throw new DataException("Facility Mapping Interface Key Missing");
        else {
            facility.setInterfaceId(repository.getByName(interFaceKey).getId());
        }

        Facility f = facilityService.getByCodeFor(facility.getFacilityCode());

         if(f != null) {

             if (null == dto) {


                 facility.setFacilityId(facilityService.getByCodeFor(facility.getFacilityCode()).getId());
                 facility.setCreatedBy(2L);
                 facility.setModifiedBy(2L);
                 facility.setActive(true);
                 repository.insertFacility(facility);
             } else {
                 facility.setFacilityId(facilityService.getByCodeFor(facility.getFacilityCode()).getId());
                 facility.setActive(true);
                 repository.updateFacility(facility);
             }


         }

    }

    public ELMISInterface getByName(String name) {
        return repository.getByName(name);
    }


    public void postBudgetToHIM(BudgetDTO dto,SourceOfFundDTO fund) {
        processBudgetResponseData(dto,fund);
    }

    private void processBudgetResponseData(BudgetDTO dto, SourceOfFundDTO fund) {

        String username = settingService.getByKey(IL_BUDGET_USERNAME).getValue();
        String password = settingService.getByKey(IL_BUDGET_PASSWORD).getValue();
        String url = settingService.getByKey(IL_BUDGET_URL).getValue();



        if (username != null && password != null && url != null) {

                sendBedNetData(username, password, url, null, null,null,dto,fund);


        }

    }

    public void refreshViews() {
        repository.refreshMaterializedViews();
    }

    public void runReviewed() {
        System.out.println("passed");
        new Thread(new Runnable() {
            public void run(){
                refreshViews();
            }
        }).start();
    }

    public void refreshViewsBy(String tableName) {

        System.out.println("passed");
        new Thread(new Runnable() {
            public void run(){
                repository.refreshViewsByName(tableName);
            }
        }).start();
    }
}
