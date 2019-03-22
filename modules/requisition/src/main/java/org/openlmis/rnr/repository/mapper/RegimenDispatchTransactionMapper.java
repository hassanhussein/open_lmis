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

package org.openlmis.rnr.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.rnr.domain.RegimenDispatchTransaction;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegimenDispatchTransactionMapper {

  @Insert("insert into regimen_dispatch_transaction " +
      " (clientId, facilityId, regimenId, days, transactionDate, quantity, createdBy, createdDate)" +
      " values " +
      " (#{clientId, typeHandler=org.openlmis.rnr.utils.UUIDTypeHandler}::UUID, #{facilityId}, #{regimenId}, #{days}, #{transactionDate}, #{quantity}, #{createdBy}, NOW())")
  Long insert(RegimenDispatchTransaction transaction);

  @Select("select * from regimen_dispatch_transaction WHERE " +
      " facilityId = #{facilityId} " +
      " and facilityTransactionId = #{facilityTransactionId}")
  @Results(
      value = {
          @Result(column = "clientId", javaType = UUID.class, typeHandler = org.openlmis.rnr.utils.UUIDTypeHandler.class)
      }
  )
  RegimenDispatchTransaction getByFacilityTransactionId(@Param("facilityId") Long facilityId,
                                                        @Param("facilityTransactionId") Long facilityTransactionId);

  @Delete("delete from regimen_dispatch_transaction where id = #{id}")
  void delete(@Param("id") Long id);
}
