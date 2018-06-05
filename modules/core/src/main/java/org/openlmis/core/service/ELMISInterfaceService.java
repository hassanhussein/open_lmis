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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openlmis.core.domain.*;
import org.openlmis.core.dto.ELMISInterfaceDTO;
import org.openlmis.core.dto.ELMISInterfaceFacilityMappingDTO;
import org.openlmis.core.repository.ELMISInterfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class ELMISInterfaceService {
    public final String HIM_USERNAME = "HIM_USERNAME_FOR_DHIS";
    public final String HIM_PASSWORD = "HIM_PASSWORD_FOR_DHIS";
    public final String HIM_URL = "HIM_PASSWORD_FOR_DHIS";

    @Autowired
    private ELMISInterfaceRepository repository;

    @Autowired
    private ConfigurationSettingService service;

    public ELMISInterface get(long interfaceId) {
        return repository.get(interfaceId);
    }

    public void save(ELMISInterface elmisInterface) {

        if(elmisInterface.getId() != null)
            repository.update(elmisInterface);
        else
            repository.insert(elmisInterface);

        repository.updateELMISInterfaceDataSets(elmisInterface);
    }

    public List<ELMISInterface> getAllInterfaces(){
        return repository.getAllInterfaces();
    }

    public List<ELMISInterfaceFacilityMapping> getInterfaceFacilityMappings(){
        return repository.getInterfaceFacilityMappings();
    }

    public List<ELMISInterfaceFacilityMapping> getFacilityInterfaceMappingById(Long facilityId){
        return repository.getFacilityInterfaceMappingById(facilityId);
    }

    public List<ELMISInterface> getAllActiveInterfaces() {
        return repository.getAllActiveInterfaces();
    }

    public void updateFacilityInterfaceMapping(Facility facility){
        repository.updateFacilityInterfaceMapping(facility);
    }

    public void uploadELMISInterface(ELMISInterfaceFacilityMappingDTO record) {
            repository.save(record);
    }

    public BaseModel getELMISInterface(ELMISInterfaceFacilityMappingDTO record) {
      return repository.getByDataset(record.getDataSetId());
    }


    @Scheduled(cron = "${batch.job.send.immunization.data}")
    public void processMosquitoNetData() {
        //Populate Data
       // repository.refreshMaterializedView();
        String username = service.getByKey(HIM_USERNAME).getValue();
        String password = service.getByKey(HIM_PASSWORD).getValue();
        String url = service.getByKey(HIM_URL).getValue();

        ELMISInterfaceDTO dto = new ELMISInterfaceDTO();

        if (username != null & password != null & url != null) {
            dto.setDataValues(repository.getMosquitoNetData());
            sendBedNetData(username, password, url, dto);
        }

    }

    private void sendBedNetData(String username, String password, String url, ELMISInterfaceDTO data) {
        ObjectMapper mapper = new ObjectMapper();
        java.net.URL obj = null;
        try {
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            String jsonInString = mapper.writeValueAsString(data);
            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + new String(java.util.Base64.getEncoder().encode(userCredentials.getBytes()));
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            //  System.out.println("all connection" + con);

            con.setDoOutput(true);
            System.out.println("file");
            System.out.println(jsonInString);
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

            //print result
            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
