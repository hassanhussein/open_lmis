package org.openlmis.core.repository.mapper.notificationMapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.notification.RationingItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RationingItemMapper {

    @Insert("INSERT INTO public.Rationing_Items(\n" +
            "           notificationId, itemCode, itemDescription, uom, quantity, \n" +
            "            missingItemStatus, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES (#{notificationId}, #{itemCode}, #{itemDescription}, #{uom}, #{quantity}, \n" +
            "            #{missingItemStatus}, #{createdBy}, NOW(), #{modifiedBy}, NOW()); ")
    @Options(useGeneratedKeys = true)
    Integer insert(RationingItem item);

    @Update(" UPDATE Rationing_Items SET notificationId=#{notificationId},itemCode=#{itemCode}, #{itemDescription}, #{uom}," +
            "  quantity=#{quantity}, missingItemStatus=#{missingItemStatus}, modifiedBy=#{modifiedBy},modifiedDate=NOW() " +
            " WHERE notificationId=#{notificationId} ")
    void update(RationingItem item);

    @Select("SELECT * FROM Rationing_Items WHERE notificationId=#{notificationId}")
    List<RationingItem> getByNotificationId(@Param("notificationId") Long notificationId);

    @Delete("DELETE FROM Rationing_Items WHERE notificationId=#{id}")
    void deleteByNotificationId(@Param("id") Long id);
}
