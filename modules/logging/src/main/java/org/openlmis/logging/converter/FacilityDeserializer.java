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

import org.openlmis.report.model.dto.Facility;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;

@Component
public class FacilityDeserializer extends EntityDeserializer<Facility> {


    @Override
    public void createEntity() {
        super.setBaseModel( new Facility());
    }

    @Override
    public void mapValues() {
        final String code = node.get("code").asText();
        final String name = node.get("name").asText();
        final String description = node.get("description").asText();
        final String gln = node.get("gln").asText();
        final String mainphone = node.get("mainphone").asText();
        final String fax = node.get("fax").asText();

        final String address1 = node.get("address1").asText();
        final String address2 = node.get("address2").asText();
        final Integer geographiczoneid = node.get("geographiczoneid").asInt();
        final Integer typeid = node.get("typeid").asInt();
        final Long catchmentpopulation = node.get("catchmentpopulation").asLong();
        final Double latitude = node.get("latitude").asDouble();
        final Double longitude = node.get("longitude").asDouble();
        final Double altitude = node.get("altitude").asDouble();
        final Integer operatedbyid = node.get("operatedbyid").asInt();
        final Double coldstoragegrosscapacity = node.get("coldstoragegrosscapacity").asDouble();
        final Double coldstoragenetcapacity = node.get("coldstoragenetcapacity").asDouble();
        final Boolean suppliesothers = node.get("suppliesothers").asBoolean();
        final Boolean sdp = node.get("sdp").asBoolean();
        final Boolean online = node.get("online").asBoolean();
        final Boolean satellite = node.get("satellite").asBoolean();
        final Integer parentfacilityid = node.get("parentfacilityid").asInt();
        final Boolean haselectricity = node.get("haselectricity").asBoolean();
        final Boolean haselectronicscc = node.get("haselectronicscc").asBoolean();
        final Boolean haselectronicdar = node.get("haselectronicdar").asBoolean();
        final Boolean active = node.get("active").asBoolean();

        final String golivedate = node.get("golivedate").asText();
        final String godowndate = node.get("godowndate").asText();
        final String comment = node.get("comment").asText();
        final Boolean enabled = node.get("enabled").asBoolean();
        final Boolean virtualfacility = node.get("virtualfacility").asBoolean();
        final Long pricescheduleid = node.get("pricescheduleid").asLong();
        final Boolean feconfigured = node.get("feconfigured").asBoolean();

        baseModel.setCode(code);
        baseModel.setName(name);
        baseModel.setDescription(description);
        baseModel.setActive(active);
        baseModel.setGln(gln);
        baseModel.setMainPhone(mainphone);
        baseModel.setFax(fax);
        baseModel.setAddress1(address1);
        baseModel.setAddress2(address2);

        baseModel.setGeographicZoneId(geographiczoneid);


        baseModel.setTypeId(typeid);
//baseModel.setFac

        baseModel.setCatchmentPopulation(catchmentpopulation);
        baseModel.setLatitude(latitude);
        baseModel.setLongitude(longitude);
        baseModel.setAltitude(altitude);

        baseModel.setOperatedById(operatedbyid);
        baseModel.setColdStorageGrossCapacity(coldstoragegrosscapacity);
        baseModel.setColdStorageNetCapacity(coldstoragegrosscapacity);
        baseModel.setSuppliesOthers(suppliesothers);
        baseModel.setSdp(sdp);
        baseModel.setOnline(online);

        baseModel.setSatellite(satellite);
        baseModel.setSatelliteParentId(parentfacilityid);

        baseModel.setHasElectricity(haselectricity);
        baseModel.setHasElectricity(haselectronicscc);
        baseModel.setHasElectronicDar(haselectronicdar);

        baseModel.setGoLiveDate(reaDateValue(golivedate,DateFormat.DATE_ONLY.toString()));
         baseModel.setGoDownDate(reaDateValue(godowndate,DateFormat.DATE_ONLY.toString()));
        baseModel.setComment(comment);


    }
}
