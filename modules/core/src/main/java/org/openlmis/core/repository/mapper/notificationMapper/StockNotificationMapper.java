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
            "            invoiceNumber, zone, soldTo, soldToCustomerName, shipTo, \n" +
            "            shipToCustomerName, msdOrderNumber, elmisOrderNumber, invoiceDate, \n" +
            "            shipVia, salesCategory, paymentTerms, salesPerson, comment, invoiceLineTotal, \n" +
            "            invoiceLineDiscount, invoiceMiscellanousCharges, invoiceTotal, \n" +
            "            legalNumber, createdBy, createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES ( #{invoiceNumber}, #{zone}, #{soldTo}, #{soldToCustomerName}, #{shipTo}, \n" +
            "            #{shipToCustomerName}, #{msdOrderNumber}, #{elmisOrderNumber}, #{invoiceDate}, \n" +
            "            #{shipVia}, #{salesCategory}, #{paymentTerms}, #{salesPerson}, #{comment}, #{invoiceLineTotal}, \n" +
            "            #{invoiceLineDiscount}, #{invoiceMiscellanousCharges}, #{invoiceTotal}, \n" +
            "            #{legalNumber}, #{createdBy}, NOW(), #{modifiedBy}, now());  ")

    @Options(useGeneratedKeys = true)
    public Integer insert(StockOutNotificationDTO notification);

    @Update(" UPDATE stock_out_notifications SET  zone = #{zone},soldTo=#{soldTo},soldToCustomerName =#{soldToCustomerName}," +
            " shipTo = #{shipTo},shipToCustomerName= #{shipToCustomerName},msdOrderNumber=#{msdOrderNumber},elmisOrderNumber= #{elmisOrderNumber}," +
            " invoiceDate=#{invoiceDate}, shipVia=#{shipVia},salesCategory=#{salesCategory}, paymentTerms=#{paymentTerms},salesPerson=#{salesPerson}," +
            " comment=#{comment}, invoiceLineTotal=#{invoiceLineTotal}, invoiceLineDiscount=#{invoiceLineDiscount},invoiceMiscellanousCharges=#{invoiceMiscellanousCharges}," +
            "   invoiceTotal=#{invoiceTotal}, legalNumber=#{legalNumber}, modifiedBy=#{modifiedBy},modifiedDate=NOW() " +
            "   WHERE invoiceNumber=#{invoiceNumber} ")
    public void update(StockOutNotificationDTO notification);

    @Select("SELECT * FROM stock_out_notifications where lower(invoiceNumber) = lower(#{invoiceNumber})")
    public StockOutNotificationDTO getByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

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

    @Select(" select n.id, invoiceNumber, soldto,soldtoCustomerName,  d.district_name,region_name,zone_name, orderNumber, msdOrderNumber, invoicedate, salescategory, n.comment\n" +
            " from stock_out_notifications n\n" +
            " JOIN orders o ON n.elmisOrdernumber = o.orderNumber\n" +
            " JOIN facilities f on n.soldTo = f.code\n" +
            " JOIN vw_districts d on f.geographiczoneId = D.DISTRICT_ID\n" +
            " WHERE f.id = ANY( #{facilityIds}::INT[])")
    List<HashMap<String, Object>> getStockBy(@Param("facilityIds") String facilityIds);
}
