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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.logging.domain.TransactionHistory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class EntityConverterImp {
    private Map<String, TransactionSerializationInfo> transactionSerializationInfoMap;

    public EntityConverterImp(List<TransactionSerializationInfo> transactionSerializationInfoList) {
        if (transactionSerializationInfoList != null && !transactionSerializationInfoList.isEmpty()) {
            this.transactionSerializationInfoMap = new HashMap<>();
            transactionSerializationInfoList.stream().forEach(ti -> {
                this.transactionSerializationInfoMap.put(ti.getKey(), ti);
            });
        }
    }


    public TransactionHistory convert(TransactionHistory transaction) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        TransactionSerializationInfo serializationInfo = transactionSerializationInfoMap.get(transaction.getTabname());
        if (serializationInfo != null) {
            module.addDeserializer(serializationInfo.getObjectType().getClass(), serializationInfo.getJsonDeserializer());
            mapper.registerModule(module);
            BaseModel newProgram = null;
            try {
                newProgram = mapper.readValue(transaction.getNewVal(), serializationInfo.getObjectType().getClass());
                BaseModel oldProgram = mapper.readValue(transaction.getOldVal(), serializationInfo.getObjectType().getClass());
                transaction.setNewBaseModel(newProgram);
                transaction.setOldBaseModel(oldProgram);
                transaction.setObjectName(serializationInfo.getTitle());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return transaction;
    }
}
