package org.openlmis.vaccine.repository.mapper.warehouse.receive;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.Location;
import org.openlmis.vaccine.domain.wms.ReceiveLot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiveLotMapper {

    @Insert(" INSERT INTO receive_lots (receiveLineItemId,locationId, lotNumber, serialNumber, expiryDate, manufacturingDate, quantity,  " +
            "createdBy, createdDate,modifiedBy,modifiedDate, packSize, boxNumber) " +
            " VALUES(#{receiveLineItem.id},#{locationId}, #{lotNumber}, #{serialNumber}, #{expiryDate}, #{manufacturingDate}, #{quantity}, #{createdBy}, NOW(),#{modifiedBy}, NOW(), #{packSize}, #{boxNumber}) ")
    @Options(useGeneratedKeys = true)
    Integer insert(ReceiveLot receiveLot);

    @Update(" update receive_lots set  receiveLineItemId = #{receiveLineItem.id},locationId=#{locationId}, lotNumber = #{lotNumber}, serialNumber = #{serialNumber}, expiryDate = #{expiryDate}, " +
            " manufacturingDate = #{manufacturingDate}, quantity = #{quantity}, packSize = #{packSize}, boxNumber = #{boxNumber}, "+
            " modifiedDate = now(), " +
            " modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(ReceiveLot receiveLot);

    @Select("select * from receive_lots where id = #{id}")
    @Results(value = {
            @Result(property = "locationId", column = "locationId", javaType = Integer.class),
            @Result(property = "location", column = "locationId", javaType = Location.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.LocationMapper.getById"))
    })

    ReceiveLot  getById(@Param("id") Long id);

    @Select(" select * from receive_lots")
    List<ReceiveLot> getAll();

    @Select("select * from receive_lots where receiveLineItemId = #{id}")
    @Results(value = {
            @Result(property = "locationId", column = "locationId", javaType = Integer.class),
            @Result(property = "location", column = "locationId", javaType = Location.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.LocationMapper.getById"))
    })
    List<ReceiveLot>  getByLineItem(@Param("id") Long id);

    @Select("select * from receive_lots where receiveLineItemId = #{id}")
    List<ReceiveLot> getByLotNumber( @Param("id") Long id);

    @Delete(" DELETE from receive_lots where receiveLineItemId = #{id}")
    void deleteByLineItem(@Param("id") Long id);
}
