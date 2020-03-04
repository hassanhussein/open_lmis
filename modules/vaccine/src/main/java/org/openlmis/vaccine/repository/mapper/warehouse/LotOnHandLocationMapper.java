package org.openlmis.vaccine.repository.mapper.warehouse;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.dto.PutAwayLineItemDTO;
import org.openlmis.vaccine.domain.wms.dto.StockCardLocationDTO;
import org.springframework.stereotype.Repository;

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

}
