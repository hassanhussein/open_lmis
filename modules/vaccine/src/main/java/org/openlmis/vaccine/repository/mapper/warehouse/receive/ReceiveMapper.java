package org.openlmis.vaccine.repository.mapper.warehouse.receive;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.domain.wms.Receive;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiveMapper {

 @Insert("INSERT INTO public.receives(\n" +
         "             purchaseOrderId,poDate,supplierId, poNumber,asnId, \n" +
         "            receiveDate,blawBnumber,country,flightVesselNumber, \n" +
         "            portOfArrival,expectedArrivalDate,actualArrivalDate, \n" +
         "            clearingAgent,shippingAgent,status,note,noteToSupplier,description, \n" +
         "            isForeignProcurement,createdBy,createdDate,modifiedBy,modifiedDate)\n" +
         "    VALUES (#{purchaseOrderId}, #{poDate},#{supplyPartner.id}, #{poNumber},#{asnId}, \n" +
         "            #{receiveDate},#{blawBnumber},#{country},#{flightVesselNumber}, \n" +
         "            #{portOfArrival},#{expectedArrivalDate},#{actualArrivalDate}, \n" +
         "            #{clearingAgent}, #{shippingAgent},#{status},#{note},#{noteToSupplier},#{description},#{isForeignProcurement}, \n" +
         "            #{createdBy}, NOW(),#{modifiedBy},NOW());")
    @Options(useGeneratedKeys = true)
    Integer insert(Receive receive);

  @Update(" UPDATE public.receives\n" +
          "   SET  purchaseOrderId=#{purchaseOrderId}, poDate=#{poDate}, supplierId=#{supplyPartner.id}, poNumber= #{poNumber}, \n" +
          "       asnId=#{asnId}, receiveDate=#{receiveDate}, blawBnumber=#{blawBnumber}, country=#{country}, flightVesselNumber=#{flightVesselNumber}, \n" +
          "       portOfArrival=#{portOfArrival}, expectedArrivalDate=#{expectedArrivalDate}, actualArrivalDate=#{actualArrivalDate}, \n" +
          "       clearingAgent=#{clearingAgent}, shippingAgent=#{shippingAgent},status=#{status}, note=#{note}, noteToSupplier=#{noteToSupplier}, \n" +
          "       description=#{description}, isForeignProcurement=#{isForeignProcurement}, modifiedBy=#{modifiedBy}, modifiedDate=NOW()\n" +
          " WHERE id = #{id}")
  void update(Receive receive);


    @Select("select * from receives where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "receiveLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveLineItemMapper.getByReceiveId")),
            @Result(property = "supplier", column = "supplierId", javaType = SupplyPartner.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.SupplyPartnerMapper.getById")),
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByAsnId"))
    })
    Receive getById(@Param("id") Long id);

    @Select(" select * from receives")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "receiveLineItems", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveLineItemMapper.getByReceiveId")),
            @Result(property = "supplier", column = "supplierid", javaType = SupplyPartner.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.SupplyPartnerMapper.getById")),
            @Result(property = "purchaseDocuments", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.PurchaseDocumentMapper.getByReceiveId"))
    })
    List<Receive> getAll();

}
