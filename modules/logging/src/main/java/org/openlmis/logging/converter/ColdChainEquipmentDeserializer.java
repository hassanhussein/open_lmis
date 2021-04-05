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
import org.openlmis.equipment.domain.ColdChainEquipment;
import org.springframework.stereotype.Component;

@Component
public class ColdChainEquipmentDeserializer extends EntityDeserializer<ColdChainEquipment> {


    @Override
    public void createEntity() {
        super.setBaseModel(new ColdChainEquipment());
    }

    @Override
    public void mapValues() {

        final Long equipmentid = node.get("equipmentid").asLong();
        final Long designationid = node.get("designationid").asLong();
        final String ccecode = node.get("ccecode").asText();
        final String pqscode = node.get("pqscode").asText();
        final Float refrigeratorcapacity = node.get("refrigeratorcapacity").floatValue();
        final String freezercapacity = node.get("freezercapacity").asText();
        final String refrigerant = node.get("refrigerant").asText();
        final String temperaturezone = node.get("temperaturezone").asText();
        final Long maxtemperature = node.get("maxtemperature").asLong();
        final Long mintemperature = node.get("mintemperature").asLong();
        final Float holdovertime = node.get("holdovertime").floatValue();
        final String energyconsumption = node.get("energyconsumption").asText();
        final String dimension = node.get("dimension").asText();
        final Float price = node.get("price").floatValue();
        final Long pqsstatusid = node.get("pqsstatusid").asLong();

        final Long donorid = node.get("donorid").asLong();
        final Float capacity = node.get("capacity").floatValue();




        baseModel.setId(equipmentid);
        baseModel.setDesignationId(designationid);
        baseModel.setCceCode(ccecode);
        baseModel.setPqsCode(pqscode);
        baseModel.setRefrigeratorCapacity(refrigeratorcapacity);
        baseModel.setRefrigerant(refrigerant);
        baseModel.setTemperatureZone(temperaturezone);
        baseModel.setMaxTemperature(maxtemperature);
        baseModel.setMinTemperature(mintemperature);
        baseModel.setHoldoverTime(holdovertime);
        baseModel.setEnergyConsumption(energyconsumption);
        baseModel.setDimension(dimension);
        baseModel.setPrice(price);
        baseModel.setPqsStatusId(pqsstatusid);
        baseModel.setDonorId(donorid);
        baseModel.setCapacity(capacity);

    }
}
