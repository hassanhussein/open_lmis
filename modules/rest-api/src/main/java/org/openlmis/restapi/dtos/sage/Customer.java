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

package org.openlmis.restapi.dtos.sage;

import lombok.Getter;
import lombok.Setter;
import org.openlmis.core.domain.Facility;

import java.util.Date;

@Getter
@Setter
public class Customer {

  private String CustomerId;

  private Date CustomerUpdatedDate;

  private String CustomerDescription;

  private String CustomerAddr1;

  private String CustomerAddr2;

  private String CustomerAddr3;

  private String CustomerAddr4;

  private String CustomerCity;

  private String CustomerPhone;

  private String CustomerEmail;

  public Facility createNewFacility() {
    Facility facility = new Facility();
    updateFacility(facility);
    return facility;
  }

  public void updateFacility(Facility facility) {
    facility.setCode(getCustomerId());
    facility.setName(getCustomerDescription());
    facility.setEnabled(true);
    facility.setActive(true);
    facility.setAddress1(getCustomerAddr1());
    facility.setAddress2(getCustomerAddr2());
    facility.setMainPhone(getCustomerPhone());

    //facility.setFacilityType();
    //facility.setGeographicZone();
  }
}
