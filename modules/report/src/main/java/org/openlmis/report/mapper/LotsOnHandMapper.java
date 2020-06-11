package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.report.model.CustomReport;
import org.openlmis.report.model.dto.Facility;
import org.openlmis.report.model.wmsreport.Facilities;
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
            "pr.fullname as fullName,fa.name as facilityName from stock_cards  left join products pr on(pr.id=stock_cards.productid) " +
            " left join facilities fa on(fa.id=stock_cards.facilityid) where stock_cards.facilityid=#{facilityId}   limit 100")
    List<StockCards> getListWithFullAttributes(@Param("facilityId") Long facilityId);

    @Select("select * from facilities where id=#{facilityId}")
    Facilities getFacilityDetails(@Param("facilityId") Long facilityId);

    @Select("select * from stock_cards limit 100")
    List<Map> getListOfReports();

    @Select("select lhl.id,s.facilityid as facilityId ,s.productid as productId," +
            "lhl.quantityonhand as totalQuantityOnHand,s.effectivedate as effectiveDate,s.modifieddate as modifiedDate ,pr.fullname as fullName,wl.name as facility_name as facilityName from lot_on_hand_locations lhl " +
            "left join lots_on_hand h on (lhl.lotonhandid = h.id) " +
            "left join  stock_cards s on (s.id = h.stockcardId) " +
            "left join products pr on(pr.id=s.productid) " +
            "lef join wms_locations wl on(wl.id=lhl.locationid) where s.facilityid=#{wareHouseId}")
    List<StockCards> getListStockOnHand(@Param("wareHouseId") Long wareHouseId);


}
