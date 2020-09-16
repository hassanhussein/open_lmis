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

import org.openlmis.core.domain.Money;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.domain.ProgramProductPrice;
import org.openlmis.core.service.ProgramProductService;
import org.openlmis.restapi.dtos.sage.ItemPrice;
import org.openlmis.restapi.processor.mappers.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ItemPriceProcessor {

  @Autowired
  private LogMapper mapper;

  @Autowired
  private ProgramProductService programProductService;

  public void process(ItemPrice price) {
    ItemPrice persistedPrice = mapper.getPrice(price);
    if (persistedPrice == null) {
      List<ProgramProduct> list = programProductService.getByProductCode(price.getItemCode());
      list
          .stream()
          .forEach(item -> updatePrice(item, price.getPrice()));
      mapper.insertPriceLog(price);
    }
  }

  private void updatePrice(ProgramProduct programProduct, BigDecimal newPrice) {
    if(programProduct == null || newPrice == null) {
      // there is nothing to save.
      return;
    }
    ProgramProductPrice ppp = new ProgramProductPrice();
    ppp.setPricePerDosage(new Money(newPrice));
    ppp.setProgramProduct(programProduct);
    ppp.setStartDate(new Date());
    ppp.setSource("SAGE");
    programProductService.updateProgramProductPrice(ppp);
  }
}
