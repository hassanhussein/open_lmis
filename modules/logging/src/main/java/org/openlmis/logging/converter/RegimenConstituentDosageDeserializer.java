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


import org.openlmis.core.domain.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RegimenConstituentDosageDeserializer extends EntityDeserializer<RegimenConstituentDosage> {


    @Override
    public void createEntity() {

        super.setBaseModel(new RegimenConstituentDosage());
    }

    @Override
    public void mapValues() {

        final Long regimenproductid = node.get("regimenproductid").asLong();
        final Double quantity = node.get("quantity").asDouble();
        final Long dosageunitid = node.get("dosageunitid").asLong();
        final Long dosagefrequencyid = node.get("dosagefrequencyid").asLong();
        RegimenCombinationConstituent constituent = new RegimenCombinationConstituent();
        DosageUnit dosageUnit = new DosageUnit();
        DosageFrequency dosageFrequency = new DosageFrequency();
        constituent.setId(regimenproductid);
        dosageUnit.setId(dosageunitid);
        dosageFrequency.setId(dosagefrequencyid);

        BigDecimal quantityBigDecimal = new BigDecimal(quantity);
        baseModel.setRegimenConstituent(constituent);
        baseModel.setQuantity(quantityBigDecimal);
        baseModel.setDosageUnit(dosageUnit);
        baseModel.setDosageFrequency(dosageFrequency);

    }
}
