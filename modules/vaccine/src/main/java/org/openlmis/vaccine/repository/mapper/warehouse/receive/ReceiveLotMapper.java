package org.openlmis.vaccine.repository.mapper.warehouse.receive;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.AsnLot;
import org.openlmis.vaccine.domain.wms.ReceiveLot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiveLotMapper {

    @Insert(" INSERT INTO receive_lots (receiveLineItemId, lotNumber, serialNumber, expiryDate, manufacturingDate, quantity,  " +
            "createdBy, createdDate,modifiedBy,modifiedDate) " +
            " VALUES(#{receiveLineItem.id}, #{lotNumber}, #{serialNumber}, #{expiryDate}, #{manufacturingDate}, #{quantity}, #{createdBy}, NOW(),#{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Integer insert(ReceiveLot receiveLot);

    @Update(" update receive_lots set  receiveLineItemId = #{receiveLineItem.id}, lotNumber = #{lotNumber}, serialNumber = #{serialNumber}, expiryDate = #{expiryDate}, " +
            " manufacturingDate = #{manufacturingDate}, quantity = #{quantity}, "+
            " modifiedDate = now(), " +
            " modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(ReceiveLot receiveLot);

    @Select("select * from receive_lots where id = #{id}")
    ReceiveLot  getById(@Param("id") Long id);

    @Select(" select * from receive_lots")
    List<ReceiveLot> getAll();

    @Select("select * from receive_lots where receiveLineItemId = #{id}")
    List<ReceiveLot>  getByLineItem(@Param("id") Long id);

    @Select("select * from receive_lots where receiveLineItemId = #{id}")
    List<ReceiveLot> getByLotNumber( @Param("id") Long id);
}
