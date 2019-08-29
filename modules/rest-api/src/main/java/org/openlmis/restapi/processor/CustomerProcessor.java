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

package org.openlmis.restapi.processor;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.service.FacilityService;
import org.openlmis.restapi.dtos.sage.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomerProcessor {

  @Value("${elmis.default.facility.type.code}")
  private String defaultFacilityTypeCode;

  @Value("${elmis.default.geographic.zone.code}")
  private String defaultGeographicZoneCode;

  @Autowired
  FacilityService facilityService;

  public void process(Customer customer) {
    Facility facility = facilityService.getByCodeFor(customer.getCustomerId());
    if (facility == null) {
      facility = customer.createNewFacility(defaultFacilityTypeCode, defaultGeographicZoneCode);
      facilityService.save(facility);
    } else {
      customer.updateFacility(facility);
      facilityService.update(facility);
    }

  }
}
