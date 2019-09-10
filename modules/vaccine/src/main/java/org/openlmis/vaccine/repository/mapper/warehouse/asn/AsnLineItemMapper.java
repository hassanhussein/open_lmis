package org.openlmis.vaccine.repository.mapper.warehouse.asn;

import org.apache.ibatis.annotations.*;
import org.openlmis.vaccine.domain.wms.AsnLineItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsnLineItemMapper {

    @Insert(" INSERT INTO asn_details (asnid, productid, expirydate, manufacturingdate, quantityexpected, lotflag, unitprice, " +
            "createdBy, createdDate,modifiedBy,modifiedDate) " +
            " VALUES(#{asn.id}, #{productid}, #{expirydate}, #{manufacturingdate}, #{quantityexpected}, #{lotflag}, #{unitprice}, #{createdBy}, NOW(), #{modifiedBy}, NOW()) ")
    @Options(useGeneratedKeys = true)
    Integer insert(AsnLineItem asnLineItem);

    @Update(" update asn_details set  asnid = #{asn.id}, productid = #{productid}, expirydate = #{expirydate}, " +
            " manufacturingdate = #{manufacturingdate}, quantityexpected = #{quantityexpected}, lotflag = #{lotflag}, unitprice = {unitprice}, "+
            " modifiedDate = now(), " +
            " modifiedBy = #{modifiedBy}  where id = #{id} ")
    void update(AsnLineItem asnLineItem);

    @Select("select * from asn_details where id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "asnLots", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLotMapper.getByAsnDetail"))
    })
    AsnLineItem  getById(@Param("id") Long id);

    @Select("select * from asn_details where asnid = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(property = "asnLots", column = "id", javaType = List.class,
                    many = @Many(select = "org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnLotMapper.getByAsnDetail"))
    })
    List<AsnLineItem>  getByAsnId(@Param("id") Long id);

    @Select(" select * from asn_details")
    List<AsnLineItem> getAll();
}
