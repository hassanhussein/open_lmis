package org.openlmis.vaccine.repository.mapper.warehouse;

import org.apache.ibatis.annotations.*;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.vaccine.domain.wms.LocationEntry;
import org.openlmis.vaccine.domain.wms.Transfer;
import org.openlmis.vaccine.domain.wms.dto.LotOnHandExtDTO;
import org.openlmis.vaccine.dto.AdjustmentReasonExDTO;
import org.openlmis.vaccine.dto.LotDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface TransferMapper {

    @Insert("INSERT INTO public.wms_transfers(\n" +
            "           fromWarehouseId, toWarehouseId, fromBin, toBin, productId, \n" +
            "            transferDate, reason, lotId, quantity,notify, createdBy, \n" +
            "            createdDate, modifiedBy, modifiedDate)\n" +
            "    VALUES ( #{fromWarehouseId}, #{toWarehouseId}, #{fromBin}, #{toBin}, #{productId}, \n" +
            "            NOW(), #{reason}, #{lotId}, #{quantity}, #{notify}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, now());")
    @Options(useGeneratedKeys = true)
    Integer insert(Transfer transfer);

    @Update("UPDATE public.wms_transfers\n" +
            "   SET fromWarehouseId=#{fromWarehouseId}, toWarehouseId=#{toWarehouseId}, fromBin=#{fromBin}, toBin=#{toBin}, \n" +
            "       productId=#{productId}, transferDate=#{transferDate}, reason=#{reason}, lotId=#{lotId}, quantity=#{quantity}, notify=true, \n" +
            "        modifiedBy=#{modifiedBy}, modifiedDate=#{modifiedDate}\n" +
            " WHERE id = #{id};\n")
    void update(Transfer transfer);

    @Insert("INSERT INTO public.wms_reasons(\n" +
            "             code, name)\n" +
            "    VALUES (#{code}, #{name}); ")
    @Options(useGeneratedKeys = true)
    Integer insertReasons(AdjustmentReasonExDTO reason);

    @Update("UPDATE wms_reasons SET code = #{code}, name=#{name} WHERE id =#{id}")
    void updateReason(AdjustmentReasonExDTO reason);

    @Select(" SELECT * FROM wms_reasons WHERE lower(code) = lower(#{code})")
    AdjustmentReasonExDTO getReasonByCode(@Param("code") String code);

    @Select(" SELECT * FROM wms_reasons ")
    List<AdjustmentReasonExDTO> getTransferReasons();

    @Select(" select * from LOTS_ON_HAND h\n" +
            "\n" +
            "JOIN lot_on_hand_locations L  on h.ID = l.LOTONHANDID\n" +
            "\n" +
            "JOIN lots Lo on h.lotId = Lo.id " +
            "\n" +
            "WHERE LocationId = #{toBin} and lo.productId= #{productId}")
    List<LotOnHand> checkAvailableProduct(@Param("toBin") Long toBin, @Param("productId") Long productId);

    @Select(" \n" +
            " SELECT LOTONHANDID, productId, lotId, locationId, l.id,  h.quantityOnHand quantity, l.quantityOnHand  from lots Lo \n" +
            "\n" +
            " JOIN LOTS_ON_HAND h on h.lotId = Lo.id\n" +
            " JOIN lot_on_hand_locations L  on h.ID = l.LOTONHANDID " +
            "\n" +
            " WHERE LocationId = #{toBin} and lo.productId= #{productId} and lotId=#{lotId}")
    List<LotDTO> checkAvailableProductAndLotBy(@Param("toBin") Long toBin, @Param("productId") Long productId, @Param("lotId") Long lotId);

    @Select("select p.primaryName product, ((coalesce((select sum(quantity) from lot_location_entries lt where lt.vvmid = vvm.id and lt.locationid= e.locationId and lotid=e.lotid and lt.type='CREDIT')+coalesce((select sum(quantity) from lot_location_entries lt where lt.vvmid = vvm.id and  lt.locationid= e.locationId and lotid=e.lotid and lt.type='ADJUSTMENT'),0)-coalesce((select sum(quantity) from lot_location_entries lt\n" +
            "                     where lt.vvmid = vvm.id and  lt.locationid= e.locationId and lotid=e.lotid and lt.type='DEBIT'),0),0))) \n" +
            "             quantityOnHand, lo.id lotId, expirationDate::date expiry, lotNumber,lo.packSize,  \n" +
            "            vvm.name as vvmStatus from lot_location_entries e\n" +
            "            JOIN lots Lo on e.lotId = Lo.id  \n" +
            "            JOIN products P ON lo.productId = P.ID \n" +
            "             JOIN vvm_statuses vvm On e.vvmId = vvm.id\n" +
            "            where productId= #{productId}  and vvm.id<3 and expirationDate>now()\n" +
            "             group by  p.primaryName ,lo.id ,expirationDate, lotNumber,vvm.id, e.lotId,vvm.name,e.locationId\n" +
            "             order by vvm.id desc,Lo.expirationDate asc ")
    List<LotOnHandExtDTO> getLotOnHandExtaBy(@Param("productId") Long id);

    @Select("select * from lot_location_entries h where stockCardId = #{stockCardId} AND locationId = #{locationId} ")
    List<LocationEntry> checkAvailableLocation(@Param("locationId") Long locationId, @Param("stockCardId") Long stockCardId);

    @Select("  select * from lot_location_entries h where stockCardID  = #{stockCardId} AND lotId not in( #{lotId}) and quantity > 0 limit 1")
    LocationEntry getLotByStockCard(@Param("stockCardId") Long id, @Param("lotId") Long lotId);

    @Select("select * from lots where productID = #{productId} and expirationDate::date > NOW()::DATE and Id NOT IN(#{lotId}) limit 1")
    Lot getByProduct(@Param("productId") Long productId , @Param("lotId") Long lotId);

    @Select("\n" +
            "select  (SELECT EXTRACT(DAY FROM l.expirationdate - CURRENT_DATE)) DAYS,\n" +
            "p.id productId, p.primaryName product, p.code productCode, l.lotNumber, l.expirationDate, l.manufacturerNAME\n" +
            " from program_products pp\n" +
            "JOIN products p ON pp.productId = P.ID\n" +
            "JOIN lots l On p.id = l.productId AND PP.PRODUCTCATEGORYID = 100\n" +
            "\n" +
            "WHERE PROGRAMID = (select id from programs where enableivdform=true  limit 1)\n" +
            " AND PP.ACTIVE = TRUE AND l.expirationdate > CURRENT_DATE and \n" +
            "(SELECT EXTRACT(DAY FROM l.expirationdate - CURRENT_DATE)) < (select value::INT from configuration_settings where key = 'MAXIMUM_DAYS_PRODUCT_EXPIRY_RESTRICTION')\n")
    List<HashMap<String, Object>> getNearToExpireItems();
}
