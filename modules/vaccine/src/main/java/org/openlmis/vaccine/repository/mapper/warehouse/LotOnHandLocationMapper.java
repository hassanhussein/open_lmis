package org.openlmis.vaccine.repository.mapper.warehouse;

import org.apache.camel.language.SpEL;
import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Product;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.stockmanagement.domain.StockCard;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.dto.LotOnHandLocationDTO;
import org.openlmis.vaccine.domain.wms.dto.PutAwayLineItemDTO;
import org.openlmis.vaccine.domain.wms.dto.SohReportDTO;
import org.openlmis.vaccine.domain.wms.dto.StockCardLocationDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotOnHandLocationMapper {

    @Insert("INSERT INTO public.lot_on_hand_locations(\n" +
            "          lotOnHandId, locationId, quantityOnHand, createdBy, createdDate, \n" +
            "            modifiedBy, modifiedDate)\n" +
            "    VALUES (#{lotOnHandId}, #{locationId}, #{quantityOnHand}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, now());")
    @Options(useGeneratedKeys = true)
    Integer insert(LotOnHandLocation location);

    @Insert("UPDATE public.lot_on_hand_locations\n" +
            "   SET  lotOnHandId=#{lotOnHandId}, locationId=#{locationId}, quantityOnHand=#{quantityOnHand},\n" +
            "    modifiedBy=#{modifiedBy}, modifiedDate=NOW()\n" +
            " WHERE id= #{id}; ")
    void update(LotOnHandLocation location);

    @Insert(" INSERT INTO public.putaway_line_items(\n" +
            "            inspectionId, lotId, fromWareHouseId, toWareHouseId, productId, \n" +
            "            quantity, createdBy, createdDate)\n" +
            "    VALUES (#{inspectionId}, #{lotId}, #{fromWareHouseId}, #{toWareHouseId}, #{productId}, \n" +
            "            #{quantity}, #{createdBy}, now());")
    Integer savePutAwayDetails(PutAwayLineItemDTO item);

    @Delete(" DELETE FROM putaway_line_items WHERE inspectionId = #{inspectionId} ")
    void deleteExistingPutAway(@Param("inspectionId") Long inspectionId);

    @Delete(" DELETE FROM lot_on_hand_locations WHERE lotOnHandId = #{id} and locationId=#{toBinLocationId} ")
    void deleteExistingByLot(@Param("id") Long id,@Param("toBinLocationId") Long toBinLocationId);

    @Insert("INSERT INTO public.stock_card_locations(\n" +
            "             stockCardId, locationId, createdBy, createdDate, modifiedBy, \n" +
            "            modifiedDate)\n" +
            "    VALUES ( #{stockCardId}, #{locationId}, #{createdBy}, NOW(), #{modifiedBy}, \n" +
            "            NOW());")
    Integer insertLocationsWIthoutLots(StockCardLocationDTO stockCard);

    @Delete(" DELETE FROM stock_card_locations WHERE stockCardId = #{id} and locationId=#{toBinLocationId} ")
    void deleteExistingStockCardLocation(@Param("id") Long id,@Param("toBinLocationId") Long toBinLocationId);


    @Select(" SELECT S.ID stockCardId, s.productId, p.primaryName product, totalQuantityonhand, LotNumber,\n" +
            " to_char(lot.expirationDate, 'dd-MM-yyyy') expirationDate,\n" +
            "to_char(l.effectiveDate, 'dd-MM-yyyy') lastUpdated, loc.name binLocation, lo.quantityOnHand\n" +
            "FROM stock_cards s  \n" +
            "JOIN lots_on_hand L on L.stockcardId = s.ID \n" +
            "JOIN lot_on_hand_locations lo ON l.id = lo.lotonhandId\n" +
            "JOIN wms_locations loc ON lo.locationId = LOC.ID\n" +
            "JOIN products p ON S.PRODUCTid = P.ID\n" +
            "JOIN lots lot ON lot.id = l.lotId\n" +
            "WHERE S.FACILITYiD = #{facilityId} and warehouseId = #{warehouseId}")
    List<SohReportDTO>getSohReport(@Param("facilityId") Long facilityId, @Param("warehouseId")Long warehouseId);

}
