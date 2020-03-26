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

import java.util.HashMap;
import java.util.HashSet;
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


    @Select(" SELECT S.ID stockCardId, s.productId, p.primaryName product, totalQuantityonhand, LotNumber,\n" +
            " to_char(lot.expirationDate, 'dd-MM-yyyy') expirationDate,\n" +
            "to_char(l.effectiveDate, 'dd-MM-yyyy') lastUpdated, loc.name binLocation, l.quantityOnHand\n" +
            "FROM stock_cards s  \n" +
            "JOIN lots_on_hand L on L.stockcardId = s.ID \n" +
            "JOIN lot_on_hand_locations lo ON l.id = lo.lotonhandId\n" +
            "JOIN wms_locations loc ON lo.locationId = LOC.ID\n" +
            "JOIN products p ON S.PRODUCTid = P.ID\n" +
            "JOIN lots lot ON lot.id = l.lotId\n" +
            "WHERE S.FACILITYiD = #{facilityId} and warehouseId = #{warehouseId}")
    List<SohReportDTO>getSohReport(@Param("facilityId") Long facilityId, @Param("warehouseId")Long warehouseId);

    @Select("Select bin, primaryname product,id,date , facility storeName, received, issued, adjustment,total,lotnumber,voucherNumber,manufacturerName,expirationdate,vvm vvmStatus, (SUM(total) over(partition by lotnumber order by id))  as loh,(SUM(total) over(order by id))  as soh\n" +
            "                FROM   \n" +
            "                (WITH Q AS (   \n" +
            "                select MAX(p.primaryname) primaryname  , 0 as id, MAX('2019-01-01')::timestamp with time zone as date,   \n" +
            "                null::TEXT as facility,  \n" +
            "                0::INTEGER as received,   \n" +
            "                0::INTEGER as issued,    \n" +
            "                0::INTEGER as adjustment,  \n" +
            "                l.lotnumber,  \n" +
            "                MAX(l.manufacturerName) as manufacturerName ,  \n" +
            "                MAX(l.expirationdate::DATE) as expirationdate,  \n" +
            "                MAX(skvvvm.valuecolumn) as vvm,   \n" +
            "                SUM(se.quantity)::integer as total,   \n" +
            "                MAX( (select voucherNumber from vaccine_distributions vd    \n" +
            "                LEFT JOIN Vaccine_distribution_line_items li ON vd.id = li.distributionId    \n" +
            "                WHERE li.productId = p.id limit 1)  ) as voucherNumber,   \n" +
            "                loc.name bin\n" +
            "                from stock_card_entries se   \n" +
            "                join stock_cards s ON s.id=se.stockcardid   \n" +
            "                join lots_on_hand lo ON lo.id=se.lotonhandid  \n" +
            "                LEFT JOIN lot_on_hand_locations bin ON lo.id = bin.lotonhandId\n" +
            "                LEFT JOIN wms_locations loc ON bin.locationId = LOC.ID \n" +
            "                join lots l on l.id=lo.lotid   \n" +
            "                join products p on p.id=s.productid   \n" +
            "                join facilities f on f.id=s.facilityid   \n" +
            "                left join stock_card_entry_key_values skvvvm on skvvvm.stockcardentryid=se.id and skvvvm.keycolumn='vvmstatus'   \n" +
            "                where   \n" +
            "                 p.id = 2412::Integer and f.id =  19075 AND S.ID = 1623 and  se.createddate::DATE < '2020/01/01'::date    group by l.lotnumber,loc.name)   \n" +
            "                SELECT * FROM Q   \n" +
            "                UNION ALL   \n" +
            "                (select p.primaryname , se.id, se.createddate as date,   \n" +
            "                case when se.type='CREDIT' then skvr.valuecolumn when se.type='ADJUSTMENT' then (select name from facilities where id =   19075  ) when se.type='DEBIT' then skvi.valuecolumn end as facility,   \n" +
            "                case when se.type ='CREDIT' then se.quantity else 0 end as received,   \n" +
            "                case when se.type ='DEBIT' then se.quantity else 0 end as issued,   \n" +
            "                case when se.type ='ADJUSTMENT' then quantity else 0 end as adjustment,   \n" +
            "                l.lotnumber, l.manufacturerName,  \n" +
            "                l.expirationdate::DATE,   \n" +
            "                skvvvm.valuecolumn as vvm,   \n" +
            "                se.quantity::integer as total,     \n" +
            "                 (SELECT voucherNumber from vaccine_distributions vd    \n" +
            "                LEFT JOIN Vaccine_distribution_line_items li ON vd.id = li.distributionId    \n" +
            "                WHERE li.productId = p.id limit 1) as voucherNumber,  \n" +
            "                loc.name bin\n" +
            "                from stock_card_entries se   \n" +
            "                join stock_cards s ON s.id=se.stockcardid   \n" +
            "                join lots_on_hand lo ON lo.id=se.lotonhandid   \n" +
            "                LEFT JOIN lot_on_hand_locations bin ON lo.id = bin.lotonhandId\n" +
            "                LEFT JOIN wms_locations loc ON bin.locationId = LOC.ID\n" +
            "                join lots l on l.id=lo.lotid   \n" +
            "                join products p on p.id=s.productid   \n" +
            "                join facilities f on f.id=s.facilityid   \n" +
            "                left join stock_card_entry_key_values skvr on skvr.stockcardentryid=se.id and skvr.keycolumn='receivedfrom'   \n" +
            "                left join stock_card_entry_key_values skvi on skvi.stockcardentryid=se.id and skvi.keycolumn='issuedto'    \n" +
            "                left join stock_card_entry_key_values skvvvm on skvvvm.stockcardentryid=se.id and skvvvm.keycolumn='vvmstatus'   \n" +
            "                where s.facilityId = 19075 AND S.ID = 1623 AND LOTnUMBER = '037G152'\n" +
            "                 order by se.createddate)) AS ledger order by id  limit 10")
    List<HashMap<String, Object>>getAllLedgers();

}
