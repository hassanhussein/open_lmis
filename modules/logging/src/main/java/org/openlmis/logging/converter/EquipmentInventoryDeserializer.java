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
import org.openlmis.equipment.domain.EquipmentInventory;
import org.springframework.stereotype.Component;

@Component
public class EquipmentInventoryDeserializer extends EntityDeserializer<EquipmentInventory> {


    @Override
    public void createEntity() {
        super.setBaseModel(new EquipmentInventory());
    }

    @Override
    public void mapValues() {

        final Long facilityid = node.get("facilityid").asLong();
        final Long programid = node.get("programid").asLong();
        final Long equipmentid = node.get("equipmentid").asLong();
        final String serialnumber = node.get("serialnumber").asText();
        final Integer yearofinstallation = node.get("yearofinstallation").asInt();
        final Float purchaseprice = node.get("purchaseprice").floatValue();
        final String sourceoffund = node.get("sourceoffund").asText();
        final Boolean replacementrecommended = node.get("replacementrecommended").asBoolean();
        final String reasonforreplacement = node.get("reasonforreplacement").asText();
        final String nameofassessor = node.get("nameofassessor").asText();
        final String datelastassessed = node.get("datelastassessed").asText();
        final Boolean isactive = node.get("isactive").asBoolean();
        final String datedecommissioned = node.get("datedecommissioned").asText();
        final Long primarydonorid = node.get("primarydonorid").asLong();
        final Boolean hasstabilizer = node.get("hasstabilizer").asBoolean();

        final String nameofsparepart = node.get("nameofsparepart").asText();

        final String remark = node.get("remark").asText();




        baseModel.setFacilityId(facilityid);
        baseModel.setProgramId(programid);
        baseModel.setEquipmentId(equipmentid);
        baseModel.setSerialNumber(serialnumber);
        baseModel.setYearOfInstallation(yearofinstallation);
        baseModel.setPurchasePrice(purchaseprice);
        baseModel.setSourceOfFund(sourceoffund);
        baseModel.setReplacementRecommended(replacementrecommended);
        baseModel.setReasonForReplacement(reasonforreplacement);
        baseModel.setNameOfAssessor(nameofassessor);
        //baseModel.setDateLastAssessed(datelastassessed);
        baseModel.setIsActive(isactive);
       // baseModel.setDateDecommissioned(datedecommissioned);
        baseModel.setPrimaryDonorId(primarydonorid);
        baseModel.setHasStabilizer(hasstabilizer);
        baseModel.setNameOfSparePart(nameofsparepart);
        baseModel.setRemark(remark);

    }
}
