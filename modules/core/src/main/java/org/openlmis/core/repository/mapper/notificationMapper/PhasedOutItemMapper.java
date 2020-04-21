package org.openlmis.core.repository.mapper.notificationMapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.notification.CloseToExpireItem;
import org.openlmis.core.dto.notification.PhasedOutItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhasedOutItemMapper {

    @Insert("INSERT INTO public.phased_Out_Items(\n" +
            "           notificationId, itemCode, itemDescription, uom, quantity, \n" +
            "            missingItemStatus, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES (#{notificationId}, #{itemCode}, #{itemDescription}, #{uom}, #{quantity}, \n" +
            "            #{missingItemStatus}, #{createdBy}, NOW(), #{modifiedBy}, NOW()); ")
    @Options(useGeneratedKeys = true)
    Integer insert(PhasedOutItem item);

    @Update(" UPDATE phased_Out_Items SET notificationId=#{notificationId},itemCode=#{itemCode}, #{itemDescription}, #{uom}," +
            "  quantity=#{quantity}, missingItemStatus=#{missingItemStatus}, modifiedBy=#{modifiedBy},modifiedDate=NOW() " +
            " WHERE notificationId=#{notificationId} ")
    void update(PhasedOutItem item);

    @Select("SELECT * FROM phased_Out_Items WHERE notificationId=#{notificationId}")
    List<PhasedOutItem> getByNotificationId(@Param("notificationId") Long notificationId);

    @Delete(" DELETE FROM phased_Out_Items WHERE notificationId=#{id}")
    void deleteByNotificationId(@Param("id") Long id);
}
