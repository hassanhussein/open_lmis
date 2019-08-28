/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.core.service;

import org.openlmis.core.domain.UserDashboardReference;
import org.openlmis.core.repository.UserDashboardPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDashboardReferenceService {
    @Autowired
    UserDashboardPreferenceRepository repository;

    public Long addPreference(UserDashboardReference dashboardReference) {
        if (dashboardReference != null && dashboardReference.getUser() != null && dashboardReference.getDashboard() != null) {
            UserDashboardReference savedPreference = repository.loaduserPreference(dashboardReference.getUser().getId(), dashboardReference.getDashboard());
            if (savedPreference == null) {
                repository.addPreference(dashboardReference);
            } else {
                repository.updatePreference(dashboardReference);
            }

        }
        return dashboardReference.getId();
    }


    public List<UserDashboardReference> loadEndabledDashlets(Long userId) {
        return repository.loadEndabledDashlets(userId);
    }

    public List<UserDashboardReference> loadDisabledDashlets(Long userId) {
        return repository.loadDisabledDashlets(userId);
    }

    public UserDashboardReference loaduserPreference(Long userId, String dashboard) {
        return repository.loaduserPreference(userId, dashboard);
    }

    public List<UserDashboardReference> loadDashlets(Long userId) {
        return repository.loadDashlets(userId);
    }
}
