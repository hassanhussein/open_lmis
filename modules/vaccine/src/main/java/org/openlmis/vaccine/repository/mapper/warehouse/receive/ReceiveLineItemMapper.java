package org.openlmis.vaccine.repository.mapper.warehouse.receive;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Product;
import org.openlmis.vaccine.domain.wms.ReceiveLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiveLineItemMapper {

    @Insert(" INSERT INTO receive_line_items (receiveId, productId, expiryDate, manufacturingDate, quantityCounted,boxCounted, lotFlag, unitPrice, " +
            "createdBy, createdDate,modifiedBy,modifiedDate) " +
            " VALUES(#{receive.id}, #{productId}, #{expiryDate}, #{manufacturingDate}, #{quantityCounted},#{boxCounted}, #{lotFlag}, #{unitPrice}, #{createdBy}, NOW(), #{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Integer insert(ReceiveLineItem lineItem);

    @Update(" update receive_line_items set  receiveId = #{receive.id}, productId = #{productId}, expiryDate = #{expiryDate}, " +
            " manufacturingDate = #{manufacturingDate}, quantityCounted = #{quantityCounted}, boxCounted=#{boxCounted},lotFlag = #{lotFlag}, unitPrice = {unitPrice}, "+
            " modifiedDate = now(), " +
            " modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(ReceiveLineItem asnLineItem);

    @Select("select * from receive_line_items where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "receiveLots", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveLotMapper.getByLineItem")),
            @Result(property = "productList", column = "productId", javaType = List.class,
                    many = @Many(select = "org.openlmis.core.repository.mapper.ProductMapper.getById"))
    })
    ReceiveLineItem  getById(@Param("id") Long id);

    @Select("select * from receive_line_items where receiveId = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "receiveLots", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.receive.ReceiveLotMapper.getByLineItem")),
            @Result(property = "productList", column = "productId", javaType = List.class,
                    many = @Many(select = "org.openlmis.core.repository.mapper.ProductMapper.getById")),
            @Result(property = "productId", column = "productId"),
            @Result(property = "product", column = "productId", javaType = Product.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.ProductMapper.getById"))

    })
    List<ReceiveLineItem>  getByReceiveId(@Param("id") Long id);

    @Select(" select * from receive_line_items")
    List<ReceiveLineItem> getAll();

    @Delete("DELETE FROM receive_lots WHERE receiveLineItemId \n" +
            "IN(\n" +
            "SELECT ID FROM receive_line_items WHERE receiveId = #{id}\n" +
            ");\n" +
            " DELETE FROM receive_line_items WHERE receiveId = #{id};" +
            "")
    void deleteByReceiveId(@Param("id") Long id);
}
