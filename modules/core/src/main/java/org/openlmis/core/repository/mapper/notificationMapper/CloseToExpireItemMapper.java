package org.openlmis.core.repository.mapper.notificationMapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.notification.CloseToExpireItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CloseToExpireItemMapper {

    @Insert("INSERT INTO public.close_to_expire_items(\n" +
            "           notificationId, itemCode, itemDescription, uom, quantity, \n" +
            "            missingItemStatus, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES (#{notificationId}, #{itemCode}, #{itemDescription}, #{uom}, #{quantity}, \n" +
            "            #{missingItemStatus}, #{createdBy}, NOW(), #{modifiedBy}, NOW()); ")
    @Options(useGeneratedKeys = true)
    Integer insert(CloseToExpireItem item);

    @Update(" UPDATE close_to_expire_items SET notificationId=#{notificationId},itemCode=#{itemCode}, #{itemDescription}, #{uom}," +
            "  quantity=#{quantity}, missingItemStatus=#{missingItemStatus}, modifiedBy=#{modifiedBy},modifiedDate=NOW() " +
            " WHERE notificationId=#{notificationId} ")
    void update(CloseToExpireItem item);

    @Select("SELECT * FROM close_to_expire_items WHERE notificationId=#{notificationId}")
    List<CloseToExpireItem> getByNotificationId(@Param("notificationId") Long notificationId);

    @Delete("DELETE FROM close_to_expire_items WHERE notificationId=#{id}")
    void deleteByNotificationId(@Param("id") Long id);
}
