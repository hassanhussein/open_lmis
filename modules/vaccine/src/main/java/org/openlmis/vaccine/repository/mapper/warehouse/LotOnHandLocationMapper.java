package org.openlmis.vaccine.repository.mapper.warehouse;

import org.apache.camel.language.SpEL;
import org.apache.ibatis.annotations.*;
import org.openlmis.core.domain.Product;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.stockmanagement.domain.StockCard;
import org.openlmis.vaccine.domain.wms.LotOnHandLocation;
import org.openlmis.vaccine.domain.wms.dto.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public interface LotOnHandLocationMapper {

    @Insert("INSERT INTO public.lot_on_hand_locations(\n" +
            "          lotOnHandId, locationId, quantityOnHand, createdBy, createdDate, \n" +
            "            modifiedBy, modifiedDate,fromBinLocationId)\n" +
            "    VALUES (#{lotOnHandId}, #{locationId}, #{quantityOnHand}, #{createdBy}, NOW(), \n" +
            "            #{modifiedBy}, now(),#{fromBinLocationId});")
    @Options(useGeneratedKeys = true)
    Integer insert(LotOnHandLocation location);

    @Insert("UPDATE public.lot_on_hand_locations\n" +
            "   SET  lotOnHandId=#{lotOnHandId}, locationId=#{locationId}, quantityOnHand=#{quantityOnHand},\n" +
            "    modifiedBy=#{modifiedBy}, modifiedDate=NOW(), fromBinLocationId=#{fromBinLocationId}\n" +
            " WHERE id= #{id}; ")
    void update(LotOnHandLocation location);

    @Insert(" INSERT INTO public.putaway_line_items(\n" +
            "            inspectionId, lotId, fromWareHouseId, toWareHouseId, productId, \n" +
            "            quantity, createdBy, createdDate,fromBinLocationId,toBinLocationId)\n" +
            "    VALUES (#{inspectionId}, #{lotId}, #{fromWareHouseId}, #{toWareHouseId}, #{productId}, \n" +
            "            #{quantity}, #{createdBy}, now(), #{fromBinLocationId}, #{toBinLocationId});")
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


    @Select("\n" +
            "            select vvmst.name as vvm,\n" +
            "            to_char(max(lo.expirationDate), 'dd-MM-yyyy') expirationDate,p.id productId, S.ID stockCardId,\n" +
            "            p.primaryName product, sum(h.quantityOnHand) quantityOnHand, lotID, lotNumber, \n" +
            "             to_char(max(h.modifieddate), 'dd-MM-yyyy') lastUpdated, Lsc.name binLocation\n" +
            "             \n" +
            "            from lot_on_hand_locations L\n" +
            "            join WMS_LOCATIONS Lsc ON L.LocationId = LSC.ID \n" +
            "            JOIN lots_on_hand h on L.LOTONHANDID =  H.ID\n" +
            "\n" +
            "            \n left join vvm_statuses  vvmst on (vvmst.id=h.vvmId)" +
            "            JOIN lots LO ON Lo.id = h.lotId\n" +
            "            \n" +
            "            JOIN products P on lo.productID = p.id\n" +
            "\n" +
            "            JOIN  stock_cards s  ON s.id = h.stockcardId\n" +
            "            \n" +
            "            WHERE  facilityId = #{facilityId} AND LSC.warehouseId = #{warehouseId}\n" +
            "            and l.quantityOnHand > 0\n" +
            "            \n" +
            "            group by h.vvmId, h.lotId,lotOnHandId,p.id, p.primaryName, stockCardId,lotNumber,S.ID,Lsc.name,vvmst.name ")
    List<SohReportDTO>getSohReport(@Param("facilityId") Long facilityId, @Param("warehouseId")Long warehouseId);

    @Select("\n" +
            "             Select primaryname product,id,date ,fromBin, toBin, facility storeName, received, issued, adjustment,total,locationName,\n" +
            "\n" +
            "             (SUM(total) over(partition by locationName order by id))  as loh,(SUM(total) over(order by id))  as soh,vvm,expirationDate,lotNumber\n" +
            "                FROM \n" +
            "                (WITH Q AS ( \n" +
            "                select MAX(p.primaryname) primaryname  , 0 as id, MAX(se.createdDATE)::timestamp with time zone as date, \n" +
            "                null::text as fromBin,\n" +
            "                null::text as toBin,\n" +
            "                null::TEXT as facility,\n" +
            "                0::INTEGER as received, \n" +
            "                0::INTEGER as issued,  \n" +
            "                0::INTEGER as adjustment,\n" +
            "                loc.name locationName,\n" +
            "\n" +
            "                SUM(se.quantity)::integer as total,vvmst.name  vvm,to_char(max(l.expirationDate), 'dd-MM-yyyy') expirationDate,l.lotnumber as lotNumber\n" +
            "             \n" +
            "                from lot_location_entries se \n" +
            "\n" +
            "                join lots_on_hand lo ON lo.id=se.lotonhandid \n" +
            "                JOIN lot_on_hand_locations H ON se.locationId = h.LOCATIONID\n" +
            "                JOIN wms_locations loc ON loc.id = h.locationId\n" +
            "            \n left join vvm_statuses  vvmst on (vvmst.id=lo.vvmId) " +
            "                join stock_cards s ON s.id=lo.stockcardid\n" +
            "                join lots l on l.id=lo.lotid \n" +
            "                join products p on p.id=s.productid \n" +
            "                join facilities f on f.id=s.facilityid \n" +
            "                where \n" +
            "                loc.warehouseID = #{warehouseId}  AND extract ('year' from se.createddate) = #{year} and p.id = #{productId}\n" +
            "                 group by    loc.name,vvmst.name,l.lotnumber) \n" +
            "                SELECT * FROM Q \n" +
            "                UNION ALL \n" +
            "                (select p.primaryname , se.id, se.createddate as date, \n" +
            "               case when se.type='CREDIT' then skvr.valuecolumn  else '' end as fromBin,\n" +
            "               case when se.type='DEBIT' then skvi.valuecolumn  else '' end as toBin,\n" +
            "               \n" +
            "               case when se.type='CREDIT' then skvr.valuecolumn when se.type='DEBIT' then skvi.valuecolumn end as facility, \n" +
            "                case when se.type ='CREDIT' then se.quantity else 0 end as received, \n" +
            "                case when se.type ='DEBIT' then se.quantity else 0 end as issued, \n" +
            "                case when se.type ='ADJUSTMENT' then quantity else 0 end as adjustment, \n" +
            "                loc.name locationName,\n" +
            "                se.quantity::integer as total,vvmst.name vvm,(select to_char(max(expirationDate), 'dd-MM-yyyy') expirationDate from lots where id=lo.lotid limit 1) as expiredDate,(select lotnumber from lots where id=lo.lotid limit 1) as lotNumber\n" +
            "               \n" +
            "                from lot_location_entries se \n" +
            "            \n" +
            "                join lots_on_hand lo ON lo.id=se.lotonhandid \n" +
            "                JOIN lot_on_hand_locations H ON se.locationId = h.LOCATIONID\n" +
            "                JOIN wms_locations loc ON loc.id = h.locationId\n" +
            "            \n left join vvm_statuses  vvmst on (vvmst.id=lo.vvmId) " +
            "                join stock_cards s ON s.id=lo.stockcardid\n" +
            "                \n" +
            "                join products p on p.id=s.productid \n" +
            "                \n" +
            "                LEFT join location_stock_card_entry_key_values skvr on skvr.stockcardentryid=se.id and skvr.keycolumn='receivedfrom' \n" +
            "                                               \n" +
            "\n" +
            "               LEFT join location_stock_card_entry_key_values skvi on skvi.stockcardentryid=se.id and skvi.keycolumn='issuedto'  \n" +
            "\n" +
            "               WHERE \n" +
            "                    loc.warehouseID = #{warehouseId}  AND extract ('year' from se.createddate) = #{year} and p.id = #{productId} \n" +
            "               order by se.createddate)) AS ledger order by id \n")
    List<HashMap<String, Object>>getAllLedgers(@Param("productId") Long productId,@Param("warehouseId") Long warehouseId, @Param("year") Long year);

  /*  @Select("SELECT PUT.*,P.PRIMARYnAME FROM lot_on_hand_locations H\n" +
            "\n" +
            "RIGHT JOIN putaway_line_items PUT ON H.locationId = PUT.fromBinLocationId\n" +
            "join PRODUCTS P ON PUT.productId = P.ID\n" +
            " WHERE fromWarehouseId = #{fromWarehouseId} and fromBinLocationId = #{fromBinLocationId}")
    */
    @Select("           SELECT productId, lotId, stockCardId, locationId, lotOnHandId, H.quantityOnHand,P.PRIMARYnAME\n" +
            "             FROM lot_on_hand_locations H\n" +
            "            JOIN wms_locations loc ON h.id = h.locationId\n" +
            "            join LOts_on_hand loh ON H.LOTONHANDID = LOH.ID\n" +
            "            join LOTS LOT oN loh.LOTiD = lot.id\n" +
            "            join PRODUCTS P ON lot.productId = P.ID\n" +
            "             WHERE warehouseId =  #{fromWarehouseId} and locationId = #{fromBinLocationId} and H.quantityOnHand > 0")
    List<HashMap<String, Object>>getAllByWareHouseAndBinLocation(@Param("fromWarehouseId") Long fromWarehouseId, @Param("fromBinLocationId") Long fromBinLocationId);


    @Select("select lotOnHandId, h.lotId, lotNumber, sum(l.quantityOnHand) quantityOnHand,  p.id productId,p.primaryName productName, stockcardid from lot_on_hand_locations L\n" +
            "join WMS_LOCATIONS Lsc ON L.LocationId = LSC.ID \n" +
            "\n" +
            "JOIN lots_on_hand h on L.LOTONHANDID =  H.ID\n" +
            "JOIN lots LO ON Lo.id = h.lotId\n" +
            "JOIN products P on lo.productID = p.id\n" +
            "\n" +
            "WHERE LSC.warehouseId = #{wareHouseId} AND lsc.ID = #{fromBinLocationId} and l.quantityOnHand > 0\n" +
            "\n" +
            "group by h.lotId,lotOnHandId,p.id, p.primaryName, stockCardId,lotNumber ")
    List<TransferDTO> getTransferDetailsBy(@Param("wareHouseId") Long wareHouseId, @Param("fromBinLocationId") Long fromBinLocationId);

    @Update(" UPDATE lot_on_hand_locations SET quantityOnHand=#{total} WHERE locationId=#{locationId} and lotOnHandId=#{lotOnHandId}")
    void updateByLotOnHandAndLocation(@Param("total") Integer total,@Param("locationId") Long locationId, @Param("lotOnHandId") Long lotOnHandId);

    @Select("SELECT * FROM lot_on_hand_locations WHERE locationId=#{locationId} order by id desc Limit 1")
    LotOnHandLocation getBy(@Param("locationId") Long locationId, @Param("lotOnHandId") Long lotOnHandId);

    @Update(" UPDATE lot_on_hand_locations SET quantityOnHand=#{quantity} WHERE id = #{id}   ")
    void updateLotOnHandLocation(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Select(" WITH Q as (\n" +
            "             SELECT p.id productId, p.primaryName product, p.code productCode, sum(h.quantityOnHand) quantityOnHand FROM STOCK_CARDS SC \n" +
            "             \n" +
            "            JOIN lots_on_hand h on SC.ID =  H.STOCKCARDId\n" +
            "\n" +
            "            JOIN lot_on_hand_locations L ON h.id =L.LOTONHANDID\n" +
            "            \n" +
            "            JOIN lots LO ON Lo.id = h.lotId\n" +
            "            \n" +
            "            JOIN products P on lo.productID = p.id\n" +
            "            \n" +
            "             where sc.facilityId = #{facilityId} and totalQuantityOnHand > 0\n" +
            "             group by p.id , p.primaryName , p.code\n" +
            "\n" +
            "             ) select * from q where quantityOnHand> 0")
    List<StockCardDTO> getStockCardWithLocationBy(@Param("facilityId") Long facilityId);

    @Select("select VVMstatus vvmId, lotNumber from inspections i\n" +
            "\n" +
            "JOIN inspection_line_items item ON i.id = item.inspectionId\n" +
            "\n" +
            "Join inspection_lots lo ON item.id = lo.inspectionlineitemid \n" +
            "WHERE lo.lotNumber = #{lotNumber} and i.id = #{inspectionId} limit 1 ")
    InspectionLotDTO getByLotAndInspection(@Param("lotNumber") String lotNumber, @Param("inspectionId") Long inspectionId);
}
