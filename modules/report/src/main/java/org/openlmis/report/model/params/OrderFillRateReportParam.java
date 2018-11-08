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

package org.openlmis.report.model.params;

import lombok.*;
import org.openlmis.report.annotations.RequiredParam;
import org.openlmis.report.model.ReportParameter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFillRateReportParam extends BaseParam {

  @RequiredParam
  private Long program;

  @RequiredParam
  private Long schedule;

  @RequiredParam
  private Long period;
  private Long zone;
  private Long facilityType;
  private String products;
  private Long productCategory;
  private Long requisitionGroup;
  private Long rnrId;
  private List<Long> rnrIds;
private String rnrIdsPar;
  @RequiredParam
  private Long facility;


}
