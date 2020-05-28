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

package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.OnScreenNotification;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OnScreenNotificationMapper {

  @Insert("INSERT INTO on_screen_notifications (facilityId, requisitionId ,fromUserId, toUserId, isHandled, type, subject, message, url, createdBy, modifiedBy, modifiedDate) " +
      "VALUES (#{facilityId}, #{requisitionId}, #{fromUserId}, #{toUserId}, #{isHandled}, #{type}, #{subject}, #{message}, #{url}, #{createdBy}, #{modifiedBy}, COALESCE(#{modifiedDate}, CURRENT_TIMESTAMP))")
  @Options(useGeneratedKeys = true)
  Integer insert(OnScreenNotification onScreenNotification);

  @Select("SELECT * FROM on_screen_notifications where toUserId = #{userId}")
  List<OnScreenNotification> getNotificationsForUser(@Param("userId") Long userId);

  @Select("SELECT count(*) FROM on_screen_notifications where toUserId = #{userId}")
  Long getNotificationsCountForUser(@Param("userId") Long userId);

  @Update("update set isHandled = true on_screen_notifications where id = #{id}")
  void markAsProcessed(@Param("id") Long id);

  @Select("SELECT * FROM on_screen_notifications where facilityId in (#{facilityIds})::INTEGER[]")
  List<OnScreenNotification> getNotificationsByFacilityIds(@Param("facilityIds") String facilityIds);

  @Select("SELECT * FROM on_screen_notifications where requisitionId = #{requisitionId}")
  List<OnScreenNotification> getNotificationsForRequisitionId(@Param("requisitionId") Long requisitionId);
}
