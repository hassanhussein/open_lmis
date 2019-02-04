/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 *
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.restapi.service;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.FacilityOperator;
import org.openlmis.core.domain.FacilityType;
import org.openlmis.core.domain.GeographicZone;
import org.openlmis.core.dto.FacilityFeedDTO;
import org.openlmis.core.dto.HFRFacilityDTO;
import org.openlmis.core.service.FacilityService;
import org.openlmis.core.service.FacilityTypeService;
import org.openlmis.core.service.GeographicZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * This service exposes methods for get facility details accepting facility code as input.
 */

@Service
public class RestFacilityService {
  @Autowired
  FacilityService facilityService;
  @Autowired
  GeographicZoneService geographicZoneService;
  @Autowired
  FacilityTypeService facilityTypeService;

  public FacilityFeedDTO getFacilityByCode(String facilityCode) {
    Facility facility = facilityService.getFacilityByCode(facilityCode);
    Facility parentFacility = null;
    if (facility.getParentFacilityId() != null) {
      parentFacility = facilityService.getById(facility.getParentFacilityId());
    }
    return new FacilityFeedDTO(facility, parentFacility);
  }

  public HFRFacilityDTO saveInterfaceFacilityInfo(HFRFacilityDTO dto) {
   if(dto != null){
     GeographicZone geographicZone = geographicZoneService.getGeoZoneByMappedCode(dto.getDistrictCode());
     if (geographicZone != null){
       FacilityType facilityType = facilityTypeService.getFacilityTypeByMappedCode(dto.getFacilityTypeCode());
       FacilityOperator facilityOperator = facilityTypeService.getFacilityTypeByMappedOwner(dto.getOwnershipCode());

       Facility facility1 = facilityService.getFacilityByCode(dto.getCode());
       Facility newFac = new Facility();
       newFac.setName(dto.getName());
       newFac.setActive(true);
       newFac.setCode(dto.getCode());
       newFac.setDescription(dto.getDescription());
       newFac.setLongitude(dto.getLongitude());
       newFac.setLongitude(dto.getLatitude());
       newFac.setGoLiveDate(new Date());
       newFac.setGoDownDate(new Date());
       newFac.setGeographicZone(geographicZone);
       newFac.setFacilityType(facilityType);
       newFac.setOperatedBy(facilityOperator);
       newFac.setEnabled(true);
       newFac.setSdp(true);

       if(facility1 != null){
         newFac.setId(facility1.getId());
         facilityService.update(newFac);
       }else {
         facilityService.save(newFac);
       }

     }
   }
    return dto;
  }
}
