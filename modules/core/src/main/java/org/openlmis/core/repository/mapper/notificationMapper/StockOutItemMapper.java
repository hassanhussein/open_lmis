package org.openlmis.core.repository.mapper.notificationMapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.notification.RationingItem;
import org.openlmis.core.dto.notification.StockOutItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockOutItemMapper {

    @Insert("INSERT INTO public.Stock_Out_Items(\n" +
            "           notificationId, itemCode, itemDescription, uom, quantity, \n" +
            "            missingItemStatus, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES (#{notificationId}, #{itemCode}, #{itemDescription}, #{uom}, #{quantity}, \n" +
            "            #{missingItemStatus}, #{createdBy}, NOW(), #{modifiedBy}, NOW()); ")
    @Options(useGeneratedKeys = true)
    Integer insert(StockOutItem item);

    @Update(" UPDATE Stock_Out_Items SET notificationId=#{notificationId},itemCode=#{itemCode}, #{itemDescription}, #{uom}," +
            "  quantity=#{quantity}, missingItemStatus=#{missingItemStatus}, modifiedBy=#{modifiedBy},modifiedDate=NOW() " +
            " WHERE notificationId=#{notificationId} ")
    void update(StockOutItem item);

    @Select("SELECT * FROM Stock_Out_Items WHERE notificationId=#{notificationId}")
    List<StockOutItem> getByNotificationId(@Param("notificationId") Long notificationId);

    @Delete("DELETE FROM Stock_Out_Items WHERE notificationId=#{id}")
    void deleteByNotificationId(@Param("id") Long id);
}
