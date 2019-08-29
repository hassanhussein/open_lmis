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

import org.openlmis.core.domain.ConfigurationSetting;
import org.openlmis.core.service.ConfigurationSettingService;
import org.openlmis.restapi.dtos.sage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.codehaus.groovy.runtime.InvokerHelper.asList;

@Component
public class SageProcessor {

  private static final String LAST_CUSTOMER_UPDATE_TIME = "LAST_CUSTOMER_UPDATE_TIME";
  private static final String LAST_ITEM_UPDATE_TIME = "LAST_ITEM_UPDATE_TIME";
  private static final String LAST_ITEM_STOCK_UPDATE_TIME = "LAST_ITEM_STOCK_UPDATE_TIME";
  private static final String LAST_ITEM_PRICE_UPDATE_TIME = "LAST_ITEM_PRICE_UPDATE_TIME";
  private static final String LAST_SHIPMENT_UPDATE_TIME = "LAST_SHIPMENT_UPDATE_TIME";
  private static final String INTEGRATION = "INTEGRATION";

  @Autowired
  CustomerProcessor customerProcessor;

  @Autowired
  ItemProcessor itemProcessor;

  @Autowired
  ItemStockProcessor itemStockProcessor;

  @Autowired
  ItemPriceProcessor itemPriceProcessor;

  @Autowired
  ShipmentProcessor shipmentProcessor;

  @Autowired
  ConfigurationSettingService configurationSettingService;

  @Autowired
  UrlFactory urlFactory;

  @Autowired
  SageRestClient restClient;


  public void process() {
    processCustomers();
    processItems();
    processItemPrices();
    processItemStocks();
    processShipments();
  }

  public Map<String, String> processShipments() {
    Date startTime = new Date();
    ConfigurationSetting lastUpdateConfiguration = getOrCreateLastUpdateTime(LAST_SHIPMENT_UPDATE_TIME);

    List<Shipment> items = restClient.callGetShipment(lastUpdateConfiguration.getValue());
    items.parallelStream().forEach(c -> shipmentProcessor.process(c));

    return updateStoreAndReturnSummary(startTime, lastUpdateConfiguration, items);

  }

  public Map<String, String> processItemStocks() {
    Date startTime = new Date();
    ConfigurationSetting lastUpdateConfiguration = getOrCreateLastUpdateTime(LAST_ITEM_STOCK_UPDATE_TIME);

    List<ItemStock> itemStocks = restClient.callGetItemStock(lastUpdateConfiguration.getValue());
    itemStocks.parallelStream().forEach(c -> itemStockProcessor.process(c));

    return updateStoreAndReturnSummary(startTime, lastUpdateConfiguration, itemStocks);
  }

  private Map<String, String> updateStoreAndReturnSummary(Date updateTime, ConfigurationSetting lastUpdateTime, List items) {
    Map<String, String> result = new HashMap<>();
    result.put("PreviousDateProcessed", lastUpdateTime.getValue());

    // update last updated time.
    lastUpdateTime.setValue(getDateString(updateTime));
    configurationSettingService.update(asList(lastUpdateTime));

    result.put("RecordsProcessedToday", String.valueOf(items.size()));
    result.put("ProcessedOn", lastUpdateTime.getValue());
    return result;
  }

  public Map<String, String> processItemPrices() {
    Date startTime = new Date();
    ConfigurationSetting lastUpdateConfiguration = getOrCreateLastUpdateTime(LAST_ITEM_PRICE_UPDATE_TIME);

    List<ItemPrice> items = restClient.callGetItemPrice(lastUpdateConfiguration.getValue());
    items.parallelStream().forEach(c -> itemPriceProcessor.process(c));

    return updateStoreAndReturnSummary(startTime, lastUpdateConfiguration, items);
  }

  public Map<String, String> processItems() {
    Date startTime = new Date();
    ConfigurationSetting lastUpdateConfiguration = getOrCreateLastUpdateTime(LAST_ITEM_UPDATE_TIME);

    List<Item> items = restClient.callGetItems(lastUpdateConfiguration.getValue());
    items.parallelStream().forEach(c -> itemProcessor.process(c));

    return updateStoreAndReturnSummary(startTime, lastUpdateConfiguration, items);
  }

  public Map<String, String> processCustomers() {

    Date startTime = new Date();
    ConfigurationSetting lastUpdateConfiguration = getOrCreateLastUpdateTime(LAST_CUSTOMER_UPDATE_TIME);

    List<Customer> items = restClient.callGetCustomers(lastUpdateConfiguration.getValue());
    items.parallelStream().forEach(c -> customerProcessor.process(c));

    return updateStoreAndReturnSummary(startTime, lastUpdateConfiguration, items);
  }

  private ConfigurationSetting getOrCreateLastUpdateTime(String key) {
    ConfigurationSetting lastUpdateTime = configurationSettingService.getByKey(key);
    if (lastUpdateTime == null) {
      lastUpdateTime = configurationSettingService.create(key, key, INTEGRATION, "");
    }
    return lastUpdateTime;
  }

  private String getDateString(Date date) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    return format.format(date);
  }
}
