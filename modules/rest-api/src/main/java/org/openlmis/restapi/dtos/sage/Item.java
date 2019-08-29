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

package org.openlmis.restapi.dtos.sage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.DosageUnit;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductForm;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Item {

  @JsonProperty("ItemNum")
  private String code;

  @JsonProperty("ItemUpdatedDate")
  private Date itemUpdatedDate;

  @JsonProperty("ItemDescription")
  private String itemDescription;

  @JsonProperty("ItemStockUnit")
  private String unit;

  @JsonProperty("ItemCategory")
  private String category;

  @JsonProperty("ItemWeightUnit")
  private String itemWeightUnit;

  public Product createNewProduct(String dosageUnitCode, String productFormCode) {
    Product product = new Product();
    product.setCode(code);

    product.setGenericName(itemDescription);
    product.setPrimaryName(itemDescription);
    product.setFullName(itemDescription);
    product.setDispensingUnit(unit);

    product.setPackSize(1);
    product.setMslPackSize(1);
    product.setDosageUnit(new DosageUnit());
    product.getDosageUnit().setCode(dosageUnitCode);
    product.setForm(new ProductForm());
    product.getForm().setCode(productFormCode);

    product.setActive(true);
    return product;
  }
}
