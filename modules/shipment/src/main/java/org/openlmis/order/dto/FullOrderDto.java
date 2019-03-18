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

package org.openlmis.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.order.domain.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
public class FullOrderDto {

  @Getter
  @Setter
  private String orderNumber;

  @Getter
  @Setter
  private String facilityCode;

  @Getter
  @Setter
  private String facilityName;

  @Getter
  @Setter
  private String supplyingFacilityCode;

  @Getter
  @Setter
  private List<OrderLineItemDto> lineItems = new ArrayList<>();

  public static FullOrderDto generateDto(Order order) {
    FullOrderDto dto = new FullOrderDto();
    dto.setFacilityCode(order.getRnr().getFacility().getCode());
    dto.setFacilityName(order.getRnr().getFacility().getName());
    dto.setOrderNumber(order.getOrderNumber());
    dto.setSupplyingFacilityCode(order.getSupplyingFacility().getCode());

    List<OrderLineItemDto> items = order.getRnr()
        .getAllLineItems()
        .stream()
        .filter(l -> !l.getSkipped() && l.getQuantityApproved() > 0)
        .map(l -> new OrderLineItemDto(l.getProductCode(), l.getProductPrimaryName(), l.getQuantityApproved(), l.getPackSize(), l.getRemarks()))
        .collect(Collectors.toList());
    dto.setLineItems(items);
    return dto;
  }

}
