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

package org.openlmis.restapi.processor.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.openlmis.restapi.dtos.sage.ItemPrice;
import org.openlmis.restapi.dtos.sage.ItemStock;
import org.springframework.stereotype.Repository;

@Repository
public interface LogMapper {

  @Insert("insert into integration_item_stock_log " +
      "(itemCode, itemDescription, locationCode, quantity, dateUpdated)" +
      " values " +
      "(#{itemCode}, #{itemDescription}, #{itemLocation}, #{itemQty}, #{itemUpdateDate})")
  int insertStockLog(ItemStock stock);

  @Insert("insert into integration_item_price_log " +
      " (itemCode, itemDescription, price, dateUpdated)" +
      " values " +
      " (#{itemCode}, #{itemDescription}, #{price}, #{itemUpdateDate})")
  int insertPriceLog(ItemPrice price);

  @Select("select * from integration_item_stock_log where itemCode = #{itemCode} and dateUpdated = #{itemUpdateDate} limit 1")
  ItemStock getStock(ItemStock stock);

  @Select("select * from integration_item_price_log" +
      " where itemCode = #{itemCode} and dateUpdated = #{itemUpdateDate} and price = #{price} " +
      " limit 1")
  ItemPrice getPrice(ItemPrice price);
}
