package org.openlmis.core.repository.mapper.notificationMapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.dto.notification.StockOutNotificationDTO;
import org.springframework.stereotype.Repository;

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


}
