/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.restapi.converter;

import org.openlmis.core.domain.BaseModel;
import org.openlmis.logging.converter.TransactionSerializationInfo;
import org.openlmis.restapi.domain.ReportableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelConversionProcessor {

    private Map<String, ModelConversionInfo> conversionInfoMap;

    public ModelConversionProcessor(List<ModelConversionInfo> modelConversionInfoList) {
        if (modelConversionInfoList != null && !modelConversionInfoList.isEmpty()) {
            this.conversionInfoMap = new HashMap<>();
            modelConversionInfoList.stream().forEach(ti -> {
                this.conversionInfoMap.put(ti.getKey(), ti);
            });
        }
    }

    public List<? extends BaseModel> process(List<? extends ReportableModel> reportableModelList, String converterKey) {
        ModelConversionInfo conversionInfo= conversionInfoMap.get(converterKey);
        List<BaseModel> modelList = new ArrayList<>();
        if (conversionInfo!=null && reportableModelList != null && !reportableModelList.isEmpty()) {
            reportableModelList.stream().forEach(rm->
                    modelList.add( conversionInfo.getEntityConverter().convert(rm))
            );
        }
        return modelList;
    }
}
