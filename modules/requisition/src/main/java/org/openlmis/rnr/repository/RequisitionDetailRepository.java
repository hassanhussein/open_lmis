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

package org.openlmis.rnr.repository;

import org.openlmis.rnr.dto.RequisitionDetailDto;
import org.openlmis.rnr.repository.mapper.RequisitionDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequisitionDetailRepository {

  @Autowired
  RequisitionDetailMapper mapper;

  public List<RequisitionDetailDto> getDetailsByFacilityPeriod(Long facilityId, Long periodId, Long page, Long pageSize) {
    return mapper.getRequisitionDetailsByFacilityPeriod(facilityId, periodId, page, pageSize);
  }

  public Long getDetailsByFaciiltyPeriodCount(Long facilityId, Long periodId) {
    return mapper.getRequisitionDetailsByFacilityPeriodCount(facilityId, periodId);
  }

  public List<RequisitionDetailDto> getDetailsByPeriod(Long periodId, Long page, Long pageSize) {
    return mapper.getRequisitionDetailsByPeriod(periodId, page, pageSize);
  }

  public Long getDetailByPeriodCount(Long periodId) {
    return mapper.getRequisitionDetailsByPeriodCount(periodId);
  }
}
