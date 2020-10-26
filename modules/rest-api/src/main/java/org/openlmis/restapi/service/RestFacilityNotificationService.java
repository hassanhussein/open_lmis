/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.restapi.service;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.service.FacilityService;
import org.openlmis.notification.domain.FacilityNotification;
import org.openlmis.notification.domain.Notifications;
import org.openlmis.notification.repository.FacilityNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestFacilityNotificationService {
    @Autowired
    FacilityNotificationRepository repository;
    @Autowired
    FacilityService facilityService;

    public List<Notifications> loadFacilityNotification(String facilityCode,Long userId) {
        Facility facility = facilityService.getFacilityByCode(facilityCode);
        List<Notifications> facilityNotificationList = repository.getNewNotificationByFacilityCode(facilityCode);
        if (facilityNotificationList != null && !facilityNotificationList.isEmpty()) {
            facilityNotificationList.stream().forEach(n -> {
                FacilityNotification facilityNotification = null;
                facilityNotification = new FacilityNotification();
                facilityNotification.setCreatedBy(userId);
                facilityNotification.setModifiedBy(userId);
                facilityNotification.setFacility(facility);
                facilityNotification.setNotification(n);
                facilityNotification.setDownloaded(true);
                facilityNotification.setAcknowledged(false);
                repository.updateNotificationDownloadStatus(facilityNotification);
            });
        }
        return facilityNotificationList;
    }
}
