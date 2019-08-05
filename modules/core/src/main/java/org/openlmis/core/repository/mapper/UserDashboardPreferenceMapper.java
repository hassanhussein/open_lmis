
/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */
package org.openlmis.core.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.UserDashboardReference;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDashboardPreferenceMapper {
    @Insert("INSERT INTO public.user_dashboard_preferences(\n" +
            " dashboard, userid, show, createdby, createddate, modifiedby, modifieddate)\n" +
            "VALUES ( #{dashboard},  #{user.id},  #{show}," +
            " #{createdBy},  COALESCE(#{modifiedDate}, NOW()),#{modifiedBy},COALESCE(#{modifiedDate}, NOW()));")
    @Options(useGeneratedKeys = true)
    Integer insert(UserDashboardReference dashboardReference);


    @Update("UPDATE public.user_dashboard_preferences\n" +
            "set dashboard=#{dashboard}, " +
            "show=#{show}" +
            "\tWHERE userid= #{user.id} and dashboard=#{dashboard}")
    void update(UserDashboardReference dashboardReference);

    @Select("select * from public.user_dashboard_preferences " +
            " where show='true' and userid=#{userId}")
    List<UserDashboardReference> loadUserPreferences(Long userId);

    @Select("select * from public.user_dashboard_preferences " +
            " where show='false' and userid=#{userId}")
    List<UserDashboardReference> loadHiddenPreferences(Long userId);

    @Select("select * from public.user_dashboard_preferences " +
            " where  dashboard=#{dashboard} and userid=#{userId}")
    UserDashboardReference loaduserPreference(@Param("userId") Long userId, @Param("dashboard") String dashboard);
}
