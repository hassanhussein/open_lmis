package org.openlmis.core.repository.mapper.notificationMapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.notification.InSufficientFundingItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InSufficientFundingItemMapper {

    @Insert("INSERT INTO public.In_Sufficient_Funding_Items(\n" +
            "           notificationId, itemCode, itemDescription, uom, quantity, \n" +
            "            missingItemStatus, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES (#{notificationId}, #{itemCode}, #{itemDescription}, #{uom}, #{quantity}, \n" +
            "            #{missingItemStatus}, #{createdBy}, NOW(), #{modifiedBy}, NOW()); ")
    @Options(useGeneratedKeys = true)
    Integer insert(InSufficientFundingItem item);

    @Update(" UPDATE In_Sufficient_Funding_Items SET notificationId=#{notificationId},itemCode=#{itemCode}, #{itemDescription}, #{uom}," +
            "  quantity=#{quantity}, missingItemStatus=#{missingItemStatus}, modifiedBy=#{modifiedBy},modifiedDate=NOW() " +
            " WHERE notificationId=#{notificationId} ")
    void update(InSufficientFundingItem item);

    @Select("SELECT * FROM In_Sufficient_Funding_Items WHERE notificationId=#{notificationId}")
    List<InSufficientFundingItem> getByNotificationId(@Param("notificationId") Long notificationId);

    @Delete(" DELETE FROM In_Sufficient_Funding_Items WHERE notificationId=#{id}")
    void deleteByNotificationId(@Param("id") Long id);
}
