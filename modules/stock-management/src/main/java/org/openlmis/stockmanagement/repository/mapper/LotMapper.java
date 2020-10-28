package org.openlmis.stockmanagement.repository.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Product;
import org.openlmis.stockmanagement.domain.LocationEntry;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotMapper {

  @Select("SELECT *" +
      " FROM lots" +
      " WHERE id = #{id} limit 1")
  @Results({
      @Result(
          property = "product", column = "productId", javaType = Product.class,
          one = @One(select = "org.openlmis.core.repository.mapper.ProductMapper.getById")),
      @Result(property = "lotCode", column = "lotnumber")
  })
  Lot getById(@Param("id")Long id);

  @Select("SELECT *" +
      " FROM lots" +
      " WHERE LOWER(lotnumber) = LOWER(#{lotCode})" +
      "   AND LOWER(manufacturername) = LOWER(#{manufacturerName})" +
      "   AND EXTRACT(year from expirationdate) = date_part('year', #{expirationDate}::TIMESTAMP)" +
      "  AND EXTRACT(month from expirationdate) = date_part('month', #{expirationDate}::TIMESTAMP)")
  @Results({
      @Result(
          property = "product", column = "productId", javaType = Product.class,
          one = @One(select = "org.openlmis.core.repository.mapper.ProductMapper.getById")),
      @Result(property = "lotCode", column = "lotnumber")
  })
  Lot getByObject(Lot lot);

  //The commented query is for WMS ..please create a separate function
/*  @Select("SELECT DISTINCT ON(lots_on_hand.lotId) *,quantity as quantityOnHand " +
      " FROM lot_location_entries" +
      " WHERE stockcardid = #{stockCardId}" +
      "   AND lotid = #{lotId}")
  @Results({
      @Result(
          property = "lot", column = "lotId", javaType = Lot.class,
          one = @One(select = "getById"))
  })*/

  @Select("SELECT DISTINCT ON(lots_on_hand.lotId) * " +
          " FROM lots_on_hand" +
          " WHERE stockcardid = #{stockCardId}" +
          "   AND lotid = #{lotId}")
  @Results({
          @Result(
                  property = "lot", column = "lotId", javaType = Lot.class,
                  one = @One(select = "getById"))
  })
  LotOnHand getLotOnHandByStockCardAndLot(@Param("stockCardId")Long stockCardId, @Param("lotId")Long lotId);

  @Select("SELECT  DISTINCT ON(loh.lotId) *" +
      " FROM lots_on_hand loh" +
      "   JOIN lots l ON l.id = loh.lotid" +
      " WHERE loh.stockcardid = #{stockCardId}" +
      "   AND LOWER(l.lotnumber) = LOWER(#{lot.lotCode})" +
      "   AND LOWER(l.manufacturername) = LOWER(#{lot.manufacturerName})" +
      "   AND EXTRACT(year from l.expirationdate) = date_part('year', #{lot.expirationDate}::TIMESTAMP)" +
      "   AND EXTRACT(month from l.expirationdate) = date_part('month', #{lot.expirationDate}::TIMESTAMP)"
      )
  @Results({
      @Result(
          property = "lot", column = "lotId", javaType = Lot.class,
          one = @One(select = "getById"))
  })
  LotOnHand getLotOnHandByStockCardAndLotObject(@Param("stockCardId")Long stockCardId, @Param("lot")Lot lot);

  @Insert("INSERT into lots " +
      " (productId, lotNumber, manufacturerName, manufactureDate, expirationDate" +
      ", createdBy, createdDate, modifiedBy, modifiedDate,packSize) " +
      "values " +
      " (#{product.id}, #{lotCode}, #{manufacturerName}, #{manufactureDate}, #{expirationDate}" +
      ", #{createdBy}, NOW(), #{modifiedBy}, NOW(), #{packSize})")
  @Options(useGeneratedKeys = true)
  void insert(Lot lot);

  @Update("UPDATE lots " +
      "SET lotNumber = #{lotCode}" +
      ", manufacturerName = #{manufacturerName}" +
      ", manufactureDate = #{manufactureDate}" +
      ", expirationDate = #{expirationDate}" +
      ", modifiedBy = #{modifiedBy}" +
      ", modifiedDate = NOW()" +
          ", packSize = #{packSize}" +
      "WHERE id = #{id}")
  int update(Lot lot);

 /* @Insert("INSERT into lots_on_hand " +
      " (stockCardId, lotId, quantityOnHand, effectiveDate " +
      ", createdBy, createdDate, modifiedBy, modifiedDate,vvmId) " +
      "values " +
      " (#{stockCard.id}, #{lot.id}, #{quantityOnHand}, #{effectiveDate}" +
      ", #{createdBy}, NOW(), #{modifiedBy}, NOW(), #{vvmId})")*/
 @Insert("INSERT INTO public.lot_location_entries(\n" +
         "           lotid, type, quantity, locationid,vvmid,stockCardId, \n" +
         "            createdBy, \n" +
         "            createdDate, modifiedBy, modifiedDate)\n" +
         "    VALUES ( #{lotId}, #{inputType},#{quantity}, #{locationId},#{vvmId},#{stockCardId}, \n" +
         "             #{createdBy}, NOW(), \n" +
         "            #{modifiedBy}, now());")

  @Options(useGeneratedKeys = true)
  void insertLotOnHand(LocationEntry lotOnHand);



  @Update("UPDATE lots_on_hand " +
      "SET quantityOnHand = #{quantityOnHand}" +
          ", effectiveDate = NOW()" +
          ", modifiedBy = #{modifiedBy}" +
          ", modifiedDate = NOW(), vvmId = #{vvmId}" +
      "WHERE id = #{id}")
  int updateLotOnHand(LocationEntry lotOnHand);

    @Delete("delete from lots where id = #{id}")
    void delete(@Param("id") Long id);

    @Select("select * from lots")
    @Results({
            @Result(
                    property = "product", column = "productId", javaType = Product.class,
                    one = @One(select = "org.openlmis.core.repository.mapper.ProductMapper.getById")),
            @Result(property = "lotCode", column = "lotnumber")
    })
    List<Lot> getAll();

    @Select("select * from lots where lower(lotnumber) = lower(#{lotCode}) limit 1")
    Lot getByCode(@Param("lotCode") String lotCode);

    @Select("select * from lots_on_hand where stockcardid = #{stockCardId} and lotId = #{lotId} ")
    LotOnHand getLotOnHandBy(@Param("stockCardId") Long stockCardId,@Param("lotId") Long lotId);

  @Insert("INSERT into lots_on_hand " +
          " (stockCardId, lotId, quantityOnHand, effectiveDate" +
          ", createdBy, createdDate, modifiedBy, modifiedDate) " +
          "values " +
          " (#{stockCard.id}, #{lotId}, #{quantityOnHand}, #{effectiveDate}" +
          ", #{createdBy}, NOW(), #{modifiedBy}, NOW())")
  @Options(useGeneratedKeys = true)
  Integer insertLotOnHandBy(LotOnHand lotOnHand);
}
