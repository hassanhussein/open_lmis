package org.openlmis.core.repository.mapper.notificationMapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.notification.CloseToExpireItem;
import org.openlmis.core.dto.notification.FullFilledItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FullFilledItemMapper {

    @Insert("INSERT INTO public.Full_Filled_Items(\n" +
            "           notificationId, itemCode, itemDescription, uom, quantity, \n" +
            "            batchSerialNo, batchQuantity, expiryDate, unitPrice, amount,createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES (#{notificationId}, #{itemCode}, #{itemDescription}, #{uom}, #{quantity}, \n" +
            "            #{batchSerialNo},#{batchQuantity},#{expiryDate},#{unitPrice},#{amount},#{createdBy}, NOW(), #{modifiedBy}, NOW()); ")
    @Options(useGeneratedKeys = true)
    Integer insert(FullFilledItem item);

    @Update(" UPDATE Full_Filled_Items SET notificationId=#{notificationId},itemCode=#{itemCode}, #{itemDescription}, #{uom}," +
            "  quantity=#{quantity}, batchSerialNo=#{batchSerialNo},batchQuantity=#{batchQuantity}," +
            " expiryDate=#{expiryDate}, unitPrice=#{unitPrice}, amount=#{amount}, " +
            " modifiedBy=#{modifiedBy},modifiedDate=NOW() " +
            " WHERE notificationId=#{notificationId} ")
    void update(FullFilledItem item);

    @Select("SELECT * FROM Full_Filled_Items WHERE notificationId=#{notificationId}")
    List<FullFilledItem> getByNotificationId(@Param("notificationId") Long notificationId);

    @Delete(" DELETE FROM Full_Filled_Items WHERE notificationId=#{id}")
    void deleteByNotificationId(@Param("id") Long id);
}
