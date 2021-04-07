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


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.openlmis.report.model.dto.Program;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class ProgramDeserializer extends EntityDeserializer<Program> {


    @Override
    public void createEntity() {
        super.setBaseModel(new Program());
    }

    @Override
    public void mapValues() {

        final String code = node.get("code").asText();
        final String name = node.get("name").asText();
        final String description = node.get("description").asText();
        final Boolean active = node.get("active").asBoolean();
        final Boolean templateconfigured = node.get("templateconfigured").asBoolean();
        final Boolean regimentemplateconfigured = node.get("regimentemplateconfigured").asBoolean();
        final Boolean budgetingapplies = node.get("budgetingapplies").asBoolean();
        final Boolean usesdar = node.get("usesdar").asBoolean();
        final Boolean push = node.get("push").asBoolean();
        final Boolean sendfeed = node.get("sendfeed").asBoolean();
        final Boolean isequipmentconfigured = node.get("isequipmentconfigured").asBoolean();
        final Boolean hideskippedproducts = node.get("hideskippedproducts").asBoolean();
        final Boolean shownonfullsupplytab = node.get("shownonfullsupplytab").asBoolean();
        final Boolean enableskipperiod = node.get("enableskipperiod").asBoolean();
        final Boolean enableivdform = node.get("enableivdform").asBoolean();

        final Boolean usepriceschedule = node.get("usepriceschedule").asBoolean();
        final Boolean isequipmenttestdisabled = node.get("isequipmenttestdisabled").asBoolean();


        baseModel.setCode(code);
        baseModel.setName(name);
        baseModel.setDescription(description);

    }
}
