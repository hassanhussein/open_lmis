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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UrlFactory {

  @Value("${integration.sage.base.url}")
  private String BASE_URL;

  public String customer() {
    return BASE_URL + "/Customer/Get";
  }

  public String itemStock() {
    return BASE_URL + "/ItemStock/Get";
  }

  public String itemPrice() {
    return BASE_URL + "/ItemPrice/Get";
  }

  public String shipment() {
    return BASE_URL + "/Shipment/Get";
  }

  public String item() {
    return BASE_URL + "/Item/Get";
  }
}
