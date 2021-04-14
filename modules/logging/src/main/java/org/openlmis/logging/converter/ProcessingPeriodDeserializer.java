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



import org.openlmis.report.model.dto.ProcessingPeriod;
import org.springframework.stereotype.Component;

@Component
public class ProcessingPeriodDeserializer extends EntityDeserializer<ProcessingPeriod> {


    @Override
    public void createEntity() {
        super.setBaseModel(new ProcessingPeriod());
    }

    @Override
    public void mapValues() {

        final String name = node.get("name").asText();
        final Integer scheduleid = node.get("scheduleid").asInt();
        final String description = node.get("description").asText();
        final String startdate = node.get("startdate").asText();
        final String enddate = node.get("enddate").asText();
        final Integer numberofmonths = node.get("numberofmonths").asInt();
        final Boolean enableorder = node.get("enableorder").asBoolean();


        baseModel.setName(name);
        baseModel.setScheduleId(scheduleid);
        baseModel.setDescription(description);
        baseModel.setStartdate(reaDateValue(startdate,DateFormat.FULL_TIME_STAMP.toString()));
        baseModel.setEnddate(reaDateValue(enddate,DateFormat.FULL_TIME_STAMP.toString()));


    }
}
