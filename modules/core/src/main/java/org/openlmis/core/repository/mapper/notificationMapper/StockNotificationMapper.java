package org.openlmis.core.repository.mapper.notificationMapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface StockNotificationMapper {

    @Insert("INSERT INTO public.stock_out_notifications(\n" +
            " quoteNumber, customerId, customerName, hfrCode, elmisOrderNumber, \n" +
            " noticationDate, zone, comment, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            " VALUES (#{quoteNumber}, #{customerId}, #{customerName}, #{hfrCode}, #{elmisOrderNumber},\n" +
            " #{noticationDate}, #{zone}, #{comment}, #{createdBy}, NOW(), #{modifiedBy}, NOW());")
    @Options(useGeneratedKeys = true)
    public Integer insert(StockOutNotificationDTO notification);

    @Update(" UPDATE public.stock_out_notifications\n" +
            "\tSET  customerId=#{customerId}, \n" +
            "\tcustomerName=#{customerName}, hfrCode=#{hfrCode}, elmisOrderNumber=#{elmisOrderNumber},\n" +
            "\tnoticationDate=#{noticationDate}, zone=#{zone}, comment=#{comment}, modifiedBy=#{modifiedBy}, modifieddate=NOW()\n" +
            "\tWHERE quoteNumber=#{quoteNumber};")
    public void update(StockOutNotificationDTO notification);

    @Select("SELECT * FROM stock_out_notifications where lower(quoteNumber) = lower(#{quoteNumber})")
    public StockOutNotificationDTO getByInvoiceNumber(@Param("quoteNumber") String invoiceNumber);

    @Select(" SELECT * FROM stock_out_notifications WHERE id = #{id} ")
            @Results(value = {
                    @Result(property = "id", column = "id"),
                    @Result(property = "fullFilledItems", javaType = List.class, column = "id",
                            many = @Many(select = "org.openlmis.core.repository.mapper.notificationMapper.FullFilledItemMapper.getByNotificationId")),
                    @Result(property = "stockOutItems", javaType = List.class, column = "id",
                            many = @Many(select = "org.openlmis.core.repository.mapper.notificationMapper.StockOutItemMapper.getByNotificationId")),
                    @Result(property = "inSufficientFundingItems", javaType = List.class, column = "id",
                            many = @Many(select = "org.openlmis.core.repository.mapper.notificationMapper.InSufficientFundingItemMapper.getByNotificationId")),
                    @Result(property = "rationingItems", javaType = List.class, column = "id",
                            many = @Many(select = "org.openlmis.core.repository.mapper.notificationMapper.RationingItemMapper.getByNotificationId")),
                    @Result(property = "phasedOutItems", javaType = List.class, column = "id",
                            many = @Many(select = "org.openlmis.core.repository.mapper.notificationMapper.PhasedOutItemMapper.getByNotificationId")),
                    @Result(property = "closeToExpireItems", javaType = List.class, column = "id",
                            many = @Many(select = "org.openlmis.core.repository.mapper.notificationMapper.CloseToExpireItemMapper.getByNotificationId"))


            })
    StockOutNotificationDTO getById(Long id);

   /*@Select("select P.NAME programName, n.id, invoiceNumber, soldto,soldtoCustomerName,  d.district_name,region_name,zone_name, orderNumber, msdOrderNumber, invoicedate, salescategory, n.comment\n" +
            "             from stock_out_notifications n\n" +
            "            JOIN orders o ON n.elmisOrdernumber = o.orderNumber\n" +
            "            JOIN requisitions r ON o.id = r.id \n" +
            "             JOIN programs p ON r.programId = P.ID \n" +
            "             JOIN facilities f on LOWER(n.soldTo) = LOWER(f.code)\n" +
            "             JOIN vw_districts d on f.geographiczoneId = D.DISTRICT_ID\n" +
            "             WHERE f.id = ANY( #{facilityIds}::INT[])\n" +
            "\n" +
            "             UNION(\n" +
            "\n" +
            "           select P.NAME programName, n.id, invoiceNumber, soldto,soldtoCustomerName,  d.district_name,region_name,zone_name, orderNumber, msdOrderNumber, invoicedate, salescategory, n.comment\n" +
            "             from stock_out_notifications n\n" +
            "            JOIN orders o ON n.elmisOrdernumber = o.orderNumber\n" +
            "            JOIN requisitions r ON o.id = r.id \n" +
            "             JOIN programs p ON r.programId = P.ID \n" +
            "             JOIN facilities f on LOWER(n.soldTo) = LOWER(f.code)\n" +
            "             JOIN vw_districts d on f.geographiczoneId = D.DISTRICT_ID\n" +
            "            WHERE  f.id in (\n" +
            "SELECT DISTINCT F.id FROM facilities F INNER JOIN users U ON U.facilityId = F.id\n" +
            "INNER JOIN role_assignments RA ON RA.userId = U.id INNER JOIN role_rights RR ON RR.roleId = RA.roleId\n" +
            "WHERE U.id =#{userId} AND RR.rightName in ('VIEW_OUT_OF_STOCK_NOTIFICATION') AND RA.supervisoryNodeId IS NULL)\n" +
            ")")*/
    @Select("")
    List<HashMap<String, Object>> getStockBy(@Param("facilityIds") String facilityIds, @Param("userId") Long userId);
}
