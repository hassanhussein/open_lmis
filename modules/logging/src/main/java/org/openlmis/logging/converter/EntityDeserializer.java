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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Money;
import org.openlmis.core.serializer.DateDeserializer;
import org.openlmis.core.serializer.DateSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonSerialize()
public abstract class EntityDeserializer<T extends BaseModel> extends JsonDeserializer {
    protected T baseModel;
    protected ObjectCodec oc;
    protected JsonNode node;
    protected ObjectMapper mapper;
    private static final ThreadLocal<SimpleDateFormat> sdf =
            ThreadLocal.<SimpleDateFormat>withInitial(
                    () -> {
                        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    });

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        oc = jp.getCodec();
        node = oc.readTree(jp);
        this.mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Date.class, new DateDeserializer());
        this.mapper.registerModule(module);
        createEntity();
        mapBasicValues();
        mapValues();
        return this.baseModel;
    }

    public abstract void createEntity();

    public void mapBasicValues() {
        final Long id = node.get("id").asLong();
        final Long createdby = node.get("createdby").asLong();
        final String createddate = node.get("createddate").asText();
        final Long modifiedby = node.get("modifiedby").asLong();
        final String modifieddate = node.get("modifieddate").asText();

        baseModel.setId(id);
        baseModel.setCreatedBy(createdby);
        baseModel.setCreatedDate(reaDateValue(createddate));
        baseModel.setModifiedBy(modifiedby);
        baseModel.setModifiedBy(modifiedby);
        baseModel.setModifiedDate(reaDateValue(modifieddate));

    }

    public abstract void mapValues();

    public Date reaDateValue(String stringDate) {

        Date date = null;
        try {
            date = sdf.get().parse(stringDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    public Money readMoneyValue(String moneyAsString) {
        Money money = null;
        try {
            money = mapper.readValue(moneyAsString, Money.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return money;
    }
}
