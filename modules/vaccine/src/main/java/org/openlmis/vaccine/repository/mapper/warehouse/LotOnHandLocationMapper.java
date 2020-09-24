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


    @Select("SELECT vvm.name as vvm, to_char(max(lo.expirationDate), 'dd-MM-yyyy') expirationDate, \n" +
            "            p.id productId,p.primaryName product, lotNumber,Lsc.name binLocation, to_char(max(e.modifieddate), 'dd-MM-yyyy') lastUpdated,\n" +
            "            \n" +
            "            \n" +
            "           ((coalesce((select sum(quantity) from lot_location_entries lt where lt.vvmid = vvm.id and lt.locationid= e.locationId and lotid=e.lotid and lt.type='CREDIT')+coalesce((select sum(quantity) from lot_location_entries lt where lt.vvmid = vvm.id and  lt.locationid= e.locationId and lotid=e.lotid and lt.type='ADJUSTMENT'),0)-coalesce((select sum(quantity) from lot_location_entries lt \n" +
            "           where lt.vvmid = vvm.id and  lt.locationid= e.locationId and lotid=e.lotid and lt.type='DEBIT'),0),0))) \n" +
            "             quantityOnHand\n" +
            "            \n" +
            "            \n" +
            "             FROM lot_location_entries e \n" +
            "            JOIN vvm_statuses vvm On e.vvmId = vvm.id\n" +
            "            JOIN lots lo ON e.lotId = lo.id\n" +
            "            JOIN products P on lo.productID = p.id\n" +
            "            join WMS_LOCATIONS Lsc ON e.LocationId = LSC.ID \n" +
            "           WHERE LSC.warehouseId = #{warehouseId} and Lsc.typeid<>9 and e.quantity > 0\n" +
            "            group by vvm.name,vvm.id, e.lotId,p.id,lotNumber,Lsc.name, e.locationId")
            List<SohReportDTO>getSohReport(@Param("facilityId") Long facilityId, @Param("warehouseId")Long warehouseId);

    @Select("             SELECT *, storename as fromBin, locationName as toBin FROM (\n" +
            "\n" +
            "                                       Select distinct primaryname product,id,date ,case when fromBin is not null then locationName else frombin end as fromBin2, \n" +
            "                                                                      case when toBin is null then locationName else facility end as toBin2,\n" +
            "                                                                     facility storeName, received, issued, adjustment,total,locationName,\n" +
            "                                                \n" +
            "                                                             (SUM(total) over(partition by locationName order by id))  as loh," +
            "   CASE WHEN row_number() over (order by id asc) =1 THEN (SUM(total) over(partition by locationName order by id)) else \n" +
            " (SUM(total) over(order by id)) end as soh, " +
            "                                                             vvm,expirationDate,lotNumber\n" +
            "\n" +
            "\n" +
            "\t\t\t\t\t\t\t\tFROM   \n" +
            "                (\n" +
            "\n" +
            "\n" +
            "\n" +
            "                                                                 select  MAX(p.primaryname) primaryname , 0 AS id,  MAX('2020-01-01')::timestamp with time zone as date,\n" +
            "\t\t\t\t\t\t\t\t  NULL as fromBin,\n" +
            "\t\t\t\t\t\t\t\t NULL as toBin,\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t       \n" +
            "\t\t\t\t\t\t\t\t null::TEXT as facility, \n" +
            "\t\t\t\t\t\t\t\t 0::INTEGER as received,   \n" +
            "\t\t\t\t\t\t\t\t0::INTEGER as issued,    \n" +
            "\t\t\t\t\t\t\t\t0::INTEGER as adjustment,\n" +
            "\t\t\t\t\t\t\t\t   loc.name locationName,  \n" +
            "\t\t\t\t\t\t\t\t    SUM(se.quantity)::integer as total,                                      \n" +
            "\t\t\t\t\t\t\t      --case when se.type ='DEBIT' then -1 * se.quantity else se.quantity end as total, \n" +
            "\t\t\t\t\t\t\t\t\t\t    \n" +
            "\t\t\t\t\t\t\t       MAX(vvmst.name) vvm, MAX(expirationDate::DATE) expirationDate,\n" +
            "\t\t\t\t\t\t\t       lotNumber\n" +
            "                                                               \n" +
            "                                                               from lot_location_entries se \n" +
            "                                                                JOIN wms_locations loc ON loc.id = se.locationId\n" +
            "                                                             left join vvm_statuses  vvmst on (vvmst.id=se.vvmId)  \n" +
            "                                                                join stock_cards s ON s.id=se.stockcardid\n" +
            "                                                                \n" +
            "                                                                join products p on p.id=s.productid \n" +
            "                                                                JOIN lots l ON se.lotiD = l.id\n" +
            "                                                                \n" +
            "                                                                LEFT join location_stock_card_entry_key_values skvr on skvr.stockcardentryid=se.id and skvr.keycolumn='receivedfrom' \n" +
            "                                                                                               \n" +
            "                                                \n" +
            "                                                               LEFT join location_stock_card_entry_key_values skvi on skvi.stockcardentryid=se.id and skvi.keycolumn='issuedto'  \n" +
            "                                                                \n" +
            "                                                \n" +
            "                                                              \n" +
            "                                                               WHERE  loc.warehouseID = #{warehouseId}  AND extract ('year' from se.createddate) = #{year} and p.id = #{productId}\n" +
            "                                                                group by l.lotnumber,loc.name\n" +
            "\n" +
            "                                                             \n" +
            "                                                                UNION ALL\n" +
            "                                                                (\n" +
            "                       \n" +
            "                                                                select p.primaryname , se.id, se.createddate as date, \n" +
            "                                                               case when se.type='CREDIT' then skvr.valuecolumn  else '' end as fromBin,\n" +
            "                                                               case when se.type='DEBIT' then skvi.valuecolumn  else '' end as toBin,\n" +
            "                                                               \n" +
            "                                                               case when se.type='CREDIT' then skvr.valuecolumn when se.type='DEBIT' then skvi.valuecolumn end as facility, \n" +
            "                                                                case when se.type ='CREDIT' then se.quantity else 0 end as received, \n" +
            "                                                                case when se.type ='DEBIT' then se.quantity else 0 end as issued, \n" +
            "                                                                case when se.type ='ADJUSTMENT' then quantity else 0 end as adjustment, \n" +
            "                                                                loc.name locationName,\n" +
            "                                                                \n" +
            "                                                                 case when se.type ='DEBIT' then -1 * se.quantity else se.quantity end as total, \n" +
            "                                    \n" +
            "                                                                 \n" +
            "                                                                vvmst.name vvm,(select to_char(max(expirationDate), 'dd-MM-yyyy')::DATE expirationDate from lots where id=se.lotid limit 1) as expirationDate,(select lotnumber from lots where id=se.lotid limit 1) as lotNumber\n" +
            "                                                               \n" +
            "                                                               from lot_location_entries se \n" +
            "                                                                JOIN wms_locations loc ON loc.id = se.locationId\n" +
            "                                                             left join vvm_statuses  vvmst on (vvmst.id=se.vvmId)  \n" +
            "                                                                join stock_cards s ON s.id=se.stockcardid\n" +
            "                                                                \n" +
            "                                                                join products p on p.id=s.productid \n" +
            "                                                                \n" +
            "                                                                LEFT join location_stock_card_entry_key_values skvr on skvr.stockcardentryid=se.id and skvr.keycolumn='receivedfrom' \n" +
            "                                                                                               \n" +
            "                                                \n" +
            "                                                               LEFT join location_stock_card_entry_key_values skvi on skvi.stockcardentryid=se.id and skvi.keycolumn='issuedto'  \n" +
            "                                                               WHERE  \n" +
            "                                                \n" +
            "                                                               \n" +
            "                                                                 loc.warehouseID = #{warehouseId}  AND extract ('year' from se.createddate) = #{year} and p.id = #{productId}\n" +
            "                                                               order by se.createddate\n" +
            "                        \n" +
            "                                                               )\n" +
            "\n" +
            "\n" +
            "\n" +
            "                                                               ) AS ledger order by id ) X ")
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

    @Select("\n" +
            "\n" +
            "select  e.lotId,vvm.name as vvm,vvm.id as vvmId,CONCAT(lotNumber,'/',vvm.id) lotNumberUn, lotNumber,CONCAT(lotNumber,' (',vvm.name, ')') as lotVvm,\n" +
            "\n" +
            "            ((coalesce((select sum(quantity) from lot_location_entries lt where lt.vvmid = vvm.id and lotid=e.lotid and\n" +
            "             lt.type='CREDIT' and lt.locationid=e.locationId)+coalesce((select sum(quantity) from lot_location_entries lt where lt.vvmid = vvm.id and lotid=e.lotid and lt.type='ADJUSTMENT' and lt.locationid=e.locationID),0)-coalesce((select sum(quantity) from lot_location_entries lt where lt.vvmid = vvm.id \n" +
            "             and lotid=e.lotid and lt.type='DEBIT' and lt.locationid=e.locationID),0),0))) quantityOnHand,\n" +
            "             \n" +
            "            p.id productId,p.primaryName productName, stockcardid from lot_location_entries  e\n" +
            "                               join WMS_LOCATIONS Lsc ON e.LocationId = LSC.ID\n" +
            "\n" +
            "                        JOIN  vvm_statuses vvm On e.vvmId = vvm.id\n" +
            "                               JOIN lots LO ON Lo.id = e.lotId\n" +
            "                                   JOIN products P on lo.productID = p.id\n" +
            "                                 WHERE LSC.warehouseId =#{wareHouseId} AND lsc.ID =#{fromBinLocationId} \n" +
            "\n" +
            "                                 group by stockCardId,e.lotId,lotNumber,vvm,p.id,vvm.id, p.primaryName,lo.id,Lsc.id,e.locationId")
    List<TransferDTO> getTransferDetailsBy(@Param("wareHouseId") Long wareHouseId, @Param("fromBinLocationId") Long fromBinLocationId);

    @Update(" UPDATE lot_on_hand_locations SET quantityOnHand=#{total} WHERE locationId=#{locationId} and lotOnHandId=#{lotOnHandId}")
    void updateByLotOnHandAndLocation(@Param("total") Integer total,@Param("locationId") Long locationId, @Param("lotOnHandId") Long lotOnHandId);

    @Select("SELECT * FROM lot_on_hand_locations WHERE locationId=#{locationId} order by id desc Limit 1")
    LotOnHandLocation getBy(@Param("locationId") Long locationId, @Param("lotOnHandId") Long lotOnHandId);

    @Update(" UPDATE lot_on_hand_locations SET quantityOnHand=#{quantity} WHERE id = #{id}   ")
    void updateLotOnHandLocation(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Select("  WITH Q as (  SELECT p.id productId, p.primaryName product, 10 as quantityOnHand, p.code productCode FROM STOCK_CARDS SC  \n" +
            "                          \n" +
            "                        left JOIN lot_location_entries h on SC.ID =  H.STOCKCARDId \n" +
            "             \n" +
            "                        left JOIN lot_on_hand_locations L ON h.id =L.LOTONHANDID \n" +
            "                         \n" +
            "                        left JOIN lots LO ON Lo.id = h.lotId \n" +
            "                         \n" +
            "                        left JOIN products P on lo.productID = p.id \n" +
            "                         \n" +
            "                         where sc.facilityId = #{facilityId}\n" +
            "                         group by p.id, p.primaryName , p.code) select * from q  \n" +
            "             ")
    List<StockCardDTO> getStockCardWithLocationBy(@Param("facilityId") Long facilityId);

    @Select("select VVMstatus vvmId, lotNumber from inspections i\n" +
            "\n" +
            "JOIN inspection_line_items item ON i.id = item.inspectionId\n" +
            "\n" +
            "Join inspection_lots lo ON item.id = lo.inspectionlineitemid \n" +
            "WHERE lo.lotNumber = #{lotNumber} and i.id = #{inspectionId} limit 1 ")
    InspectionLotDTO getByLotAndInspection(@Param("lotNumber") String lotNumber, @Param("inspectionId") Long inspectionId);
}
