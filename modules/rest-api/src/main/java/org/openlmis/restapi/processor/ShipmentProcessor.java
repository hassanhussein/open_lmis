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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.openlmis.order.domain.Order;
import org.openlmis.order.domain.OrderStatus;
import org.openlmis.order.service.OrderService;
import org.openlmis.restapi.dtos.sage.Shipment;
import org.openlmis.restapi.dtos.sage.ShipmentDetail;
import org.openlmis.shipment.domain.ShipmentFileInfo;
import org.openlmis.shipment.domain.ShipmentLineItem;
import org.openlmis.shipment.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ShipmentProcessor {

  Logger logger = Logger.getLogger(ShipmentProcessor.class.toString());

  @Autowired
  private ShipmentService shipmentService;

  @Autowired
  private OrderService orderService;

  @SneakyThrows
  public void process(Shipment shipment) {
    ObjectMapper mapper = new ObjectMapper();
    Order order = orderService.getByOrderNumber(shipment.getOrderNumber());
    logger.info("Shipment is being processed.");
    logger.info(shipment.getOrderNumber());
    logger.info("Number of line Items: " + shipment.getDetail().size());
    if (order == null) {

      // this shipment cannot be processed becasue a corresponding order was not found for it.
      ShipmentFileInfo shipmentFileInfo = new ShipmentFileInfo();
      shipmentFileInfo.setOrderNumber(shipment.getOrderNumber());
      shipmentFileInfo.setFileName(shipment.getShipmentNumber());
      shipmentFileInfo.setHasSkippedLineItems(true);
      shipmentFileInfo.setSkippedShipmentLineItems(mapper.writeValueAsString(shipment));
      shipmentService.insertShipmentFileInfo(shipmentFileInfo);
      return;
    }
    if (OrderStatus.RECEIVED.equals(order.getStatus())) {
      // there must be another shipment that was processed for this order.
      // Skip the processing.
      return;
    }


    shipment
        .getDetail()
        .parallelStream()
        .forEach(detail -> shipmentService.save(lineItemFromDetail(order, detail)));

    ShipmentFileInfo shipmentFileInfo = new ShipmentFileInfo();
    shipmentFileInfo.setFileName(shipment.getShipmentNumber());
    shipmentFileInfo.setOrderNumber(shipment.getOrderNumber());
    shipmentFileInfo.setHasSkippedLineItems(false);
    shipmentFileInfo.setSkippedShipmentLineItems(mapper.writeValueAsString(shipment));
    shipmentService.insertShipmentFileInfo(shipmentFileInfo);


    // mark the order as processed
    order.setStatus(OrderStatus.RECEIVED);
    orderService.updateOrderStatus(order);
    List<ShipmentLineItem> lineItems = new ArrayList<>();
    shipment.getDetail().forEach(l -> addLineItem(l, order, lineItems));
    lineItems.forEach(l -> shipmentService.save(l));
  }

  private void addLineItem(ShipmentDetail detail, Order order, List<ShipmentLineItem> lineItems) {
    ShipmentLineItem lineItem = new ShipmentLineItem();
    lineItem.setProductCode(detail.getShipmentDetailItemNum());
    lineItem.setQuantityShipped(detail.getShipmentDetailQty().intValue());
    lineItem.setProductName(detail.getShipmentDetailItemDescription());
    if (detail.getShipmentDetailBatchNumber() != null) {
      lineItem.setBatch(detail.getShipmentDetailBatchNumber());
      lineItem.setQuantityShipped(detail.getShipmentDetailLotQty().intValue());
    }
    lineItem.setDispensingUnit(" "); //TOFIX: Dispensing unit is not
    lineItem.setOrderNumber(order.getOrderNumber());
    lineItem.setOrderId(order.getId());

    if(lineItem.getOrderNumber() != null) {
      lineItems.add(lineItem);
    }
  }

  private ShipmentLineItem lineItemFromDetail(Order order, ShipmentDetail detail) {
    ShipmentLineItem lineItem = new ShipmentLineItem();
    lineItem.setOrderId(order.getId());
    lineItem.setProductCode(detail.getShipmentDetailItemNum());
    lineItem.setProductName(detail.getShipmentDetailItemDescription());
    lineItem.setQuantityShipped(detail.getShipmentDetailQty().intValue());
    return lineItem;
  }

}
