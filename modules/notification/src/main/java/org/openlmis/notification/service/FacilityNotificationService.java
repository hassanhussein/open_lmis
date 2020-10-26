
/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

import org.openlmis.notification.domain.FacilityNotification;
import org.openlmis.notification.domain.Notifications;
import org.openlmis.notification.repository.FacilityNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FacilityNotificationService {
    @Autowired
    FacilityNotificationRepository repository;

    public void addNewNotification(Notifications notification) {
        if (notification.getId() != null && !notification.getId().equals(0L)) {
            repository.updateNotifciation(notification);
        } else {
            repository.addNewNotification(notification);
        }

    }


    public void updateNotifciation(Notifications notification) {
        repository.updateNotifciation(notification);
    }


    public Notifications getNotificationById(Long id) {
        return repository.getNotificationById(id);
    }


    public List<Notifications> getAllNotifications() {
        return repository.getAllNotifications();
    }


    public List<Notifications> getNewNotificationByFacilityCode(String code) {
        return repository.getNewNotificationByFacilityCode(code);
    }


    public List<FacilityNotification> getNotificationByFacilityCode(String code, Boolean acknowledged) {
        return repository.getNotificationByFacilityCode(code, acknowledged);
    }


    public void updateNotificationDownloadStatus(FacilityNotification facilityNotification) {
        repository.updateNotificationDownloadStatus(facilityNotification);
    }


    public void acknowledgeNotificationReceival(FacilityNotification facilityNotification) {
        repository.acknowledgeNotificationReceival(facilityNotification);
    }
}
