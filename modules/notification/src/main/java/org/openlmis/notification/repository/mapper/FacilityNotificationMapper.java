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

/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.notification.repository.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.aspectj.weaver.ast.Not;
import org.openlmis.notification.domain.FacilityNotification;
import org.openlmis.notification.domain.Notifications;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityNotificationMapper {

    @Insert({" insert into notifications",
            "( name, code, description, message, " +
                    " urgency, type," +
                    "createdby, createddate, modifiedby, modifieddate)" +
                    "VALUES ( #{name}, #{code}, #{description}, #{message}," +
                    "#{urgency}, #{type}," +
                    "  #{createdBy}, #{createdDate}, #{modifiedBy}, #{modifiedDate});"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Long insert(Notifications notification);

    @Update({" update notifications set" +
            " name=#{name}, " +
            "code=#{code}," +
            "urgency=#{urgency}," +
            "type=#{type}," +
            "description=#{description}," +
            "message=#{message} " +
            " where id= #{id}"})
    void update(Notifications notification);

    @Select({"select * from notifications where id=#{id}"})
    Notifications getById(Long id);

    @Select({"select * from notifications"})
    List<Notifications> getAll();



    @Select({"select * from notifications where id not in" +
            "(select notificationid from facility_notifications " +
            "where facilitycode= #{code})"})
    List<Notifications> getNewNotificationByFacilityCode(String code);

    @Select({"select n.id,n.code,n.name,n.description,n.message," +
            " fn.id facilitynotificationid,fn.facilityid, fn.facilitycode,fn.downloaded,fn.acknowledged" +
            " from notifications n" +
            " inner join facility_notifications fn on n.id=fn.notificationid" +
            " where fn.acknowledged=#{acknowledged} and fn.facilitycode=#{code}"})
    List<FacilityNotification> getNotificationByFacilityCode(String code, Boolean acknowledged);


    @Insert({" insert into facility_notifications",
            "( facilityid, facilitycode, notificationid, downloaded, acknowledged, " +
                    "createdby, createddate, modifiedby, modifieddate)" +
                    "VALUES ( #{facility.id}, #{facility.code}, #{notification.id}, #{downloaded},#{acknowledged}," +
                    "  #{createdBy}, #{createdDate}, #{modifiedBy}, #{modifiedDate});"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Long updateNotificationDownloadStatus(FacilityNotification facilityNotification);


    @Update({" update facility_notifications set" +
            " acknowledged=#{acknowledged} " +
            " where id= #{id}"})
    Long acknowledgeNotificationReceival(FacilityNotification facilityNotification);
}
