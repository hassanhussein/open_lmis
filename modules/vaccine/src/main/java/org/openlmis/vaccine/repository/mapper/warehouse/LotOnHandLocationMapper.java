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


    @Select("  \n" +
            " SELECT S.ID stockCardId,s.productId, p.primaryName product, QUANTITY quantityOnHand, lotID,\n" +
            " s.modifieddate lastUpdated\n" +
            " ,LotNumber,to_char(lot.expirationDate, 'dd-MM-yyyy') expirationDate\n" +
            "    FROM stock_cards s  \n" +
            "LEFT JOIN putaway_line_items put on s.productID = PUT.productId\n" +
            "JOIN products p on s.productID = p.id\n" +
            "LEFT JOIN lots lot ON lot.id = put.lotId\n" +
            "\n" +
            "WHERE s.facilityId = #{facilityId} and toWarehouseId = #{warehouseId}")
    List<SohReportDTO>getSohReport(@Param("facilityId") Long facilityId, @Param("warehouseId")Long warehouseId);

    @Select("      Select bin, primaryname product,id,date , facility storeName, received, issued, adjustment,total,lotnumber,voucherNumber,manufacturerName,expirationdate,vvm vvmStatus, (SUM(total) over(partition by lotnumber order by id))  as loh,(SUM(total) over(order by id))  as soh\n" +
            "\n" +
            "                            FROM   \n" +
            "                            (\n" +
            "\n" +
            "                            WITH Q AS (   \n" +
            "                            select MAX(p.primaryname) primaryname  , 0 as id, MAX('2019-01-01')::timestamp with time zone as date,   \n" +
            "                            null::TEXT as facility,  \n" +
            "                            0::INTEGER as received,   \n" +
            "                            0::INTEGER as issued,    \n" +
            "                            0::INTEGER as adjustment,  \n" +
            "                            l.lotnumber,  \n" +
            "                            MAX(l.manufacturerName) as manufacturerName ,  \n" +
            "                            MAX(l.expirationdate::DATE) as expirationdate,  \n" +
            "                            MAX(skvvvm.valuecolumn) as vvm,   \n" +
            "                            SUM(se.quantity)::integer as total,   \n" +
            "                            MAX( (select voucherNumber from vaccine_distributions vd    \n" +
            "                            LEFT JOIN Vaccine_distribution_line_items li ON vd.id = li.distributionId    \n" +
            "                            WHERE li.productId = p.id limit 1)  ) as voucherNumber,   \n" +
            "                            loc.name bin\n" +
            "                            from stock_card_entries se   \n" +
            "                            join stock_cards s ON s.id=se.stockcardid   \n" +
            "                            join lots_on_hand lo ON lo.id=se.lotonhandid  \n" +
            "                            LEFT JOIN lot_on_hand_locations bin ON lo.id = bin.lotonhandId\n" +
            "                            LEFT JOIN wms_locations loc ON bin.locationId = LOC.ID \n" +
            "                            join lots l on l.id=lo.lotid   \n" +
            "                            join products p on p.id=s.productid   \n" +
            "                            join facilities f on f.id=s.facilityid   \n" +
            "                            left join stock_card_entry_key_values skvvvm on skvvvm.stockcardentryid=se.id and skvvvm.keycolumn='vvmstatus'   \n" +
            "                            where   \n" +
            "                             p.id = #{productId}::Integer and loc.warehouseId = #{warehouseId} and f.id =  #{facilityId} AND EXTRACT( year from se.createddate::DATE) < #{year}::int    group by l.lotnumber,loc.name)   \n" +
            "                            SELECT * FROM Q   \n" +
            "                            UNION ALL   \n" +
            "                            (\n" +
            "\n" +
            "                            select p.primaryname , se.id, se.createddate as date,   \n" +
            "                            case when se.type='CREDIT' then skvr.valuecolumn when se.type='ADJUSTMENT' then (select name from facilities where id =   19075  ) when se.type='DEBIT' then skvi.valuecolumn end as facility,   \n" +
            "                            case when se.type ='CREDIT' then se.quantity else 0 end as received,   \n" +
            "                            case when se.type ='DEBIT' then se.quantity else 0 end as issued,   \n" +
            "                            case when se.type ='ADJUSTMENT' then quantity else 0 end as adjustment,   \n" +
            "                            l.lotnumber, l.manufacturerName,  \n" +
            "                            l.expirationdate::DATE,   \n" +
            "                            skvvvm.valuecolumn as vvm,   \n" +
            "                            se.quantity::integer as total,     \n" +
            "                             (SELECT voucherNumber from vaccine_distributions vd    \n" +
            "                            LEFT JOIN Vaccine_distribution_line_items li ON vd.id = li.distributionId    \n" +
            "                            WHERE li.productId = p.id limit 1) as voucherNumber,  \n" +
            "                            loc.name bin\n" +
            "                            from stock_card_entries se   \n" +
            "                            join stock_cards s ON s.id=se.stockcardid   \n" +
            "                            join lots_on_hand lo ON lo.id=se.lotonhandid   \n" +
            "                            LEFT JOIN lot_on_hand_locations bin ON lo.id = bin.lotonhandId\n" +
            "                            LEFT JOIN wms_locations loc ON bin.locationId = LOC.ID\n" +
            "                            join lots l on l.id=lo.lotid   \n" +
            "                            join products p on p.id=s.productid   \n" +
            "                            join facilities f on f.id=s.facilityid   \n" +
            "                            left join stock_card_entry_key_values skvr on skvr.stockcardentryid=se.id and skvr.keycolumn='receivedfrom'   \n" +
            "                            left join stock_card_entry_key_values skvi on skvi.stockcardentryid=se.id and skvi.keycolumn='issuedto'    \n" +
            "                            left join stock_card_entry_key_values skvvvm on skvvvm.stockcardentryid=se.id and skvvvm.keycolumn='vvmstatus'   \n" +
            "                            where p.id = #{productId}::Integer and s.facilityId = #{facilityId} and loc.warehouseId = #{warehouseId} AND EXTRACT( year from se.createddate::DATE) < #{year}::int \n" +
            "                             order by se.createddate\n" +
            "\n" +
            "                             )\n" +
            "\n" +
            "                             ) AS ledger order by id ")
    List<HashMap<String, Object>>getAllLedgers(@Param("facilityId") Long facilityId, @Param("productId") Long productId,@Param("warehouseId") Long warehouseId, @Param("year") Long year);

    @Select("SELECT PUT.*,P.PRIMARYnAME FROM lot_on_hand_locations H\n" +
            "\n" +
            "RIGHT JOIN putaway_line_items PUT ON H.locationId = PUT.fromBinLocationId\n" +
            "join PRODUCTS P ON PUT.productId = P.ID\n" +
            " WHERE fromWarehouseId = #{fromWarehouseId} and fromBinLocationId = #{fromBinLocationId}")
    List<HashMap<String, Object>>getAllByWareHouseAndBinLocation(@Param("fromWarehouseId") Long fromWarehouseId, @Param("fromBinLocationId") Long fromBinLocationId);


    @Select("select lotOnHandId, h.lotId, lotNumber, sum(l.quantityOnHand) quantityOnHand,  p.id productId,p.primaryName productName, stockcardid from lot_on_hand_locations L\n" +
            "join WMS_LOCATIONS Lsc ON L.frombinLocationId = LSC.ID \n" +
            "\n" +
            "JOIN lots_on_hand h on L.LOTONHANDID =  H.ID\n" +
            "JOIN lots LO ON Lo.id = h.lotId\n" +
            "JOIN products P on lo.productID = p.id\n" +
            "\n" +
            "WHERE LSC.warehouseId = #{wareHouseId} AND lsc.ID = #{fromBinLocationId}\n" +
            "\n" +
            "group by h.lotId,lotOnHandId,p.id, p.primaryName, stockCardId,lotNumber ")
    List<TransferDTO> getTransferDetailsBy(@Param("wareHouseId") Long wareHouseId, @Param("fromBinLocationId") Long fromBinLocationId);

}
