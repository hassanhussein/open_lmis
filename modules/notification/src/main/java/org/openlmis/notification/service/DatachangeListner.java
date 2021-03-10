/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.notification.service;

import org.openlmis.core.event.DataChangeEvent;
import org.openlmis.notification.utils.NotificationConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DatachangeListner implements ApplicationListener<DataChangeEvent> {
    @Autowired
    ProducerService producerService;
    @Override
    public void onApplicationEvent(DataChangeEvent event) {
        producerService.produceMessage("{\"cmd\":\""+ NotificationConstant.CORE_TABLE_DATA_CHANGED
                +"\" , "+event.getMessage()+"}");
    }
}

