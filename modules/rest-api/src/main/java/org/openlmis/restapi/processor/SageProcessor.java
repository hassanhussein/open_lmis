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

import org.openlmis.restapi.dtos.sage.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class SageProcessor {

  @Autowired
  CustomerProcessor customerProcessor;
  @Value("${integration.sage.base.url}")
  private String BASE_URL;
  @Value("${integration.sage.user.name}")
  private String USER_NAME;
  @Value("${integration.sage.user.password}")
  private String PASSWORD;

  public void process() {
    getCustomers();
  }

  private void getCustomers() {
    RestTemplate template = new RestTemplate();

    ResponseEntity<List<Customer>> response = template
        .exchange(BASE_URL + "/Customer/Get", HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>>() {
        });
    List<Customer> customers = response.getBody();

    customers.stream().forEach(c -> customerProcessor.process(c));

  }
}
