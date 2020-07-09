package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.report.model.CustomReport;
import org.openlmis.report.model.dto.Facility;
import org.openlmis.report.model.wmsreport.Facilities;
import org.openlmis.report.model.wmsreport.StockCard;
import org.openlmis.report.model.wmsreport.StockCards;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LotsOnHandMapper {
    @Select("select stock_cards.id,stock_cards.id," +
            "stock_cards.facilityid as facilityName," +
            "stock_cards.productid as productId," +
            "stock_cards.totalquantityonhand as totalQuantityOnHand," +
            "stock_cards.effectivedate as effectiveDate," +
            "stock_cards.modifieddate as modifiedDate," +
            "pr.primaryname as fullName,fa.name as facilityName from stock_cards  left join products pr on(pr.id=stock_cards.productid) " +
            " left join facilities fa on(fa.id=stock_cards.facilityid) where stock_cards.facilityid=#{facilityId}   limit 100")
    List<StockCards> getListWithFullAttributes(@Param("facilityId") Long facilityId);

    @Select("select * from facilities where id=#{facilityId}")
    Facilities getFacilityDetails(@Param("facilityId") Long facilityId);

    @Select("select * from stock_cards limit 100")
    List<Map> getListOfReports();

    @Select("select vvmst.name as vvm,lhl.id,s.facilityid as facilityId,lo.lotnumber as lotNumber,wh.name as warehouseName,lo.expirationdate as expirationDate,s.productid as productId," +
            "h.quantityOnHand as totalQuantityOnHand ,s.effectivedate as effectiveDate,s.modifieddate as modifiedDate ,pr.primaryname as fullName,wl.name  as locationName from lot_on_hand_locations lhl " +
            "left join lots_on_hand h on (lhl.lotonhandid = h.id) " +
            "left join  stock_cards s on (s.id = h.stockcardid) " +
            "left join lots lo on(lo.id=h.lotid) "+
            "left join vvm_statuses vvmst on(vvmst.id=h.vvmId) "+
            "left join products pr on(pr.id=lo.productid) " +
            "left join wms_locations wl on(wl.id=lhl.locationid) " +
            "left join warehouses wh on(wh.id=wl.warehouseid) " +
            "where lo.productid=#{productId} and wl.warehouseid=#{warehouseId} ")
    List<StockCards> getListStockOnHand(@Param("productId") Long productId,@Param("warehouseId") Long warehouseId);



    @Select("select  sum(h.quantityOnHand) as totalQuantityOnHand,lo.productid as productId,pr.primaryname as productName,wh.name as wareHouseName from lot_on_hand_locations lhl \n" +
            "            left join lots_on_hand h on (lhl.lotonhandid = h.id) \n" +
            "            left join  stock_cards s on (s.id = h.stockcardid) \n" +
            "            left join lots lo on(lo.id=h.lotid) \n" +
            "            left join products pr on(pr.id=lo.productid) \n" +
            "            left join wms_locations wl on(wl.id=lhl.locationid) \n" +
            "            left join warehouses wh on(wh.id=wl.warehouseid)  where wl.warehouseid=#{wareHouseId} group by   lo.productid,pr.primaryname,wh.name")
    List<StockCard> getListStockProduct(@Param("wareHouseId") Long wareHouseId);



}
