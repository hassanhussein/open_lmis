/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.restapi.processor;

import org.apache.commons.net.util.Base64;
import org.openlmis.core.exception.DataException;
import org.openlmis.restapi.dtos.sage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.util.StringUtils;

import java.nio.charset.Charset;
import java.util.List;

@Service
public class SageRestClient {

  @Autowired
  UrlFactory urlFactory;

  @Value("${integration.sage.user.name}")
  private String USER_NAME;

  @Value("${integration.sage.user.password}")
  private String PASSWORD;

  public RestTemplate createRestTemplate(){
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(USER_NAME, PASSWORD));
    return restTemplate;
  }

  public List<ItemStock> callGetItemStock(String lastUpdateTime) {
    String queryString = (!StringUtils.isEmpty(lastUpdateTime)) ? "?dtReturn=" + lastUpdateTime : "";

    RestTemplate template = createRestTemplate();
    ResponseEntity<List<ItemStock>> response = template.exchange(urlFactory.itemStock() + queryString, HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemStock>>() {
    });
    return response.getBody();
  }

  public List<ItemPrice> callGetItemPrice(String lastUpdateTime) {
    String queryString = (!StringUtils.isEmpty(lastUpdateTime)) ? "?dtReturn=" + lastUpdateTime : "";

    RestTemplate template = createRestTemplate();
    try {
      ResponseEntity<List<ItemPrice>> response = template.exchange(urlFactory.itemPrice() + queryString, HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemPrice>>() {

      });
      return response.getBody();
    }catch(Exception exp)
    {
      throw new DataException(exp.getMessage(), exp.getLocalizedMessage());
    }
  }

  public List<Customer> callGetCustomers(String lastUpdateTime) {
    String queryString = (!StringUtils.isEmpty(lastUpdateTime)) ? "?dtReturn=" + lastUpdateTime : "";

    RestTemplate template = new RestTemplate();
    ResponseEntity<List<Customer>> response = template.exchange(urlFactory.customer() + queryString, HttpMethod.GET, createHeaders(), new ParameterizedTypeReference<List<Customer>>() {
    });
    return (List<Customer>)response.getBody();
  }

  public List<Item> callGetItems(String lastUpdateTime) {
    String queryString = (!StringUtils.isEmpty(lastUpdateTime)) ? "?dtReturn=" + lastUpdateTime : "";

    RestTemplate template = new RestTemplate();
    ResponseEntity<List<Item>> response = template.exchange(urlFactory.item() + queryString, HttpMethod.GET, createHeaders(), new ParameterizedTypeReference<List<Item>>() {
    });
    return response.getBody();
  }

  private HttpEntity createHeaders() {
    return new HttpEntity<>(
        new HttpHeaders() {
          {
            String auth = USER_NAME + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
          }
        });
  }

  public List<Shipment> callGetShipment(String lastUpdateTime) {
    String queryString = (!StringUtils.isEmpty(lastUpdateTime)) ? "?dtReturn=" + lastUpdateTime : "";

    RestTemplate template = new RestTemplate();
    ResponseEntity<List<Shipment>> response = template.exchange(urlFactory.shipment() + queryString, HttpMethod.GET, createHeaders(), new ParameterizedTypeReference<List<Shipment>>() {
    });
    return response.getBody();
  }
}