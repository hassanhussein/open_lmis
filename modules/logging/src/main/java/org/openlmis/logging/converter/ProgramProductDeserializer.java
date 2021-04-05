/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.logging.converter;


import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.Program;
import org.openlmis.core.domain.ProgramProduct;
import org.openlmis.core.serializer.MoneySerializer;
import org.springframework.stereotype.Component;

@Component
public class ProgramProductDeserializer extends EntityDeserializer<ProgramProduct> {


    @Override
    public void createEntity() {
        super.setBaseModel(new ProgramProduct());
    }

    @Override
    public void mapValues() {

        final Long programid = node.get("programid").asLong();
        final Long productid = node.get("productid").asLong();
        final Integer dosespermonth = node.get("dosespermonth").asInt();
        final Boolean active = node.get("active").asBoolean();
        final String currentprice = node.get("currentprice").asText();
        final Integer displayorder = node.get("displayorder").asInt();
        final Long productcategoryid = node.get("productcategoryid").asLong();
        final Boolean fullsupply = node.get("fullsupply").asBoolean();
        final String isacoefficientsid = node.get("isacoefficientsid").asText();


        Program program = new Program(programid);
        Product product = new Product();
        product.setId(productid);
        baseModel.setProgram(program);
        baseModel.setProduct(product);
        baseModel.setDosesPerMonth(dosespermonth);
        baseModel.setActive(active);
        baseModel.setCurrentPrice(readMoneyValue(currentprice));
        baseModel.setDisplayOrder(displayorder);
        baseModel.setProductCategoryId(productcategoryid);
        baseModel.setFullSupply(fullsupply);

    }
}
