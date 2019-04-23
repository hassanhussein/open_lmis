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

package org.openlmis.restapi.controller;

import lombok.NoArgsConstructor;
import org.openlmis.restapi.processor.SageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@NoArgsConstructor
public class IntegrationController extends BaseController {

  @Autowired
  SageProcessor sageProcessor;

  @RequestMapping(value = "/rest-api/integration/process-all", method = RequestMethod.GET)
  public void callIntegrationProcessor() {
    sageProcessor.process();
  }

  @RequestMapping(value = "/rest-api/integration/process-customer", method = RequestMethod.GET)
  public void callCustomerProcessor() {
    sageProcessor.processCustomers();
  }

  @RequestMapping(value = "/rest-api/integration/process-item-price", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, String> callItemPriceProcessor() {
    return sageProcessor.processItemPrices();
  }

  @RequestMapping(value = "/rest-api/integration/process-item-stock", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, String> callItemStockProcessor() {
    return sageProcessor.processItemStocks();
  }

  @RequestMapping(value = "/rest-api/integration/process-shipment", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, String> callShipmentProcessor() {
    return sageProcessor.processShipments();
  }
}
