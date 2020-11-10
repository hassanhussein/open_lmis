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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.FacilityType;
import org.openlmis.core.domain.GeographicZone;

import java.util.Date;

@Getter
@Setter
public class Customer {

  @JsonProperty("CustomerId")
  private String customerId;

  @JsonProperty("FacilityCode")
  private String facilityCode;

  @JsonProperty("CustomerUpdatedDate")
  private Date customerUpdatedDate;

  @JsonProperty("CustomerDescription")
  private String customerDescription;

  @JsonProperty("CustomerAddr1")
  private String customerAddr1;

  @JsonProperty("CustomerAddr2")
  private String customerAddr2;

  @JsonProperty("CustomerAddr3")
  private String customerAddr3;

  @JsonProperty("CustomerAddr4")
  private String customerAddr4;

  @JsonProperty("CustomerCity")
  private String customerCity;

  @JsonProperty("CustomerPhone")
  private String customerPhone;

  @JsonProperty("CustomerEmail")
  private String customerEmail;

  public Facility createNewFacility(String facilityTypeCode, String geographicZoneCode) {
    Facility facility = new Facility();
    facility.setFacilityType(new FacilityType());
    facility.getFacilityType().setCode(facilityTypeCode);
    facility.setGeographicZone(new GeographicZone());
    facility.getGeographicZone().setCode(geographicZoneCode);
    facility.setSdp(true);
    facility.setGoLiveDate(new Date());
    updateFacility(facility);
    return facility;
  }

  public void updateFacility(Facility facility) {
    facility.setCode(getCustomerId());
    if(getCustomerDescription().length() >= 50) {
      facility.setName(getCustomerDescription().substring(0, 49));
    }else {
      facility.setName(getCustomerDescription());
    }
    facility.setDescription(getCustomerDescription());
    facility.setEnabled(true);
    facility.setActive(true);
    facility.setAddress1(getCustomerAddr1());
    facility.setAddress2(getCustomerAddr2());
    facility.setMainPhone(getCustomerPhone());

    //facility.setFacilityType();
    //facility.setGeographicZone();
  }
}
