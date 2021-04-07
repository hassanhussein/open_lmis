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



import org.openlmis.report.model.dto.Vendor;
import org.springframework.stereotype.Component;

@Component
public class VendorDeserializer extends EntityDeserializer<Vendor> {


    @Override
    public void createEntity() {
        super.setBaseModel(new Vendor());
    }

    @Override
    public void mapValues() {

        final String name = node.get("name").asText();
        final String website = node.get("website").asText();
        final String contactperson = node.get("contactperson").asText();
        final String primaryphone = node.get("primaryphone").asText();
        final String email = node.get("email").asText();
        final String description = node.get("description").asText();
        final String specialization = node.get("specialization").asText();
        final String geographiccoverage = node.get("geographiccoverage").asText();
        final String registrationdate = node.get("registrationdate").asText();




        baseModel.setName(name);
        baseModel.setWebsite(website);
        baseModel.setContactPerson(contactperson);
        baseModel.setPrimaryPhone(primaryphone);
        baseModel.setEmail(email);
        baseModel.setDescription(description);

    }
}
