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

package org.openlmis.rnr.domain;

import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.exception.DataException;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegimenDispatchTransaction extends BaseModel {

  private UUID clientId;

  private Long facilityTransactionId;

  private Long facilityId;

  private Long regimenId;

  private Long days;

  private Date transactionDate;

  private Long quantity;

  public void validate() {
    if (this.days <= 0 || quantity <= 0) {
      throw new DataException("Number of days or quantity cannot be less than or equal to 0.");
    }
    if (transactionDate.after(new Date())) {
      throw new DataException("Transaction cannot be recorded for future date.");
    }
  }
}
