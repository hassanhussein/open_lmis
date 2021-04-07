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
import org.openlmis.report.model.dto.Equipment;
import org.springframework.stereotype.Component;

@Component
public class EquipmentDeserializer extends EntityDeserializer<Equipment> {


    @Override
    public void createEntity() {
        super.setBaseModel(new Equipment());
    }

    @Override
    public void mapValues() {

        final String name = node.get("name").asText();
        final Long equipmenttypeid = node.get("equipmenttypeid").asLong();
        final String manufacturer = node.get("manufacturer").asText();
        final String model = node.get("model").asText();
        final Long energytypeid = node.get("energytypeid").asLong();
        final Long modelid = node.get("modelid").asLong();




        baseModel.setName(name);
        baseModel.setEquipmentTypeId(equipmenttypeid);
        baseModel.setManufacturer(manufacturer);
        baseModel.setModel(model);
        baseModel.setEnergyTypeId(energytypeid);
        baseModel.setModelId(modelid);

    }
}
