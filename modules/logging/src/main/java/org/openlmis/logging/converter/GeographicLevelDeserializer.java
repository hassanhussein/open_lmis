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


import org.openlmis.core.domain.GeographicLevel;
import org.openlmis.core.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class GeographicLevelDeserializer extends EntityDeserializer<GeographicLevel> {


    @Override
    public void createEntity() {
        super.setBaseModel(new GeographicLevel());
    }

    @Override
    public void mapValues() {

        final String code = node.get("code").asText();
        final String name = node.get("name").asText();
        final Integer levelnumber = node.get("levelnumber").asInt();




        baseModel.setCode(code);
        baseModel.setName(name);
        baseModel.setLevelNumber(levelnumber);

    }
}
