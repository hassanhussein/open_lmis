package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.RowBounds;

import org.openlmis.report.builder.VaccineStockStatusQueryBuilder;

import org.openlmis.report.model.params.VaccineStockStatusParam;

import org.openlmis.report.model.report.VaccineStockStatusReport;
import org.openlmis.report.model.wmsreport.StockCards;
import org.openlmis.report.model.wmsreport.VaccineDistribution;
import org.openlmis.report.model.wmsreport.VaccineDistributionLineItem;
import org.openlmis.report.model.wmsreport.VaccineDistributionLots;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface VaccineStockStatusMapper {



    @SelectProvider(type = VaccineStockStatusQueryBuilder.class, method = "getQuery")
    @Options(resultSetType = ResultSetType.SCROLL_SENSITIVE, fetchSize = 10, timeout = 0, useCache = true, flushCache = true)
    public List<VaccineStockStatusReport> getReport(@Param("filterCriteria") VaccineStockStatusParam params,
                                                    @Param("userId") Long userId,
                                                    @Param("RowBounds") RowBounds rowBounds
                                                    );
    @Select("SELECT vd.id,vd.distributionid as distributionId,f.name as facilityName,p.primaryname as product,g.name as district,\n" +
            "f.name as region,vd.quantity as quantityIssued\n" +
            "FROM vaccine_distribution_line_items vd\n" +
            "left join products p on(p.id=vd.productid)\n" +
            " left join  vaccine_distributions d on(d.id=vd.distributionid)\n" +
            " left join facilities f on(f.id=d.tofacilityid)\n " +
            " left join facilities fr on(fr.id=d.fromfacilityid)" +
            " left join geographic_zones g on(g.id=f.geographiczoneid) " +
            "where fr.id=#{facilityId}")
    List<VaccineDistributionLineItem> vaccineDistributionLineItemList(@Param("facilityId") Long facilityId);

    @Select("SELECT vd.id,vd.distributionid as distributionId,l.lotNumber,f.name as facilityName,p.primaryname as product,g.name as district,\n" +
            "f.name as region,vdl.quantity as quantityIssued\n" +
            "FROM vaccine_distribution_line_item_lots vdl\n " +
            " left join vaccine_distribution_line_items vd on(vd.id=vdl.distributionlineitemid) " +
            "left join products p on(p.id=vd.productid)\n " +
            " left join lots l on (l.id=vdl.lotid)" +
            " left join  vaccine_distributions d on(d.id=vd.distributionid)\n" +
            " left join facilities f on(f.id=d.tofacilityid)\n " +
            " left join facilities fr on(fr.id=d.fromfacilityid)" +
            " left join geographic_zones g on(g.id=f.geographiczoneid) " +
            "where fr.id=#{facilityId} and distributiondate>'2020-07-01'")
    List<VaccineDistributionLineItem> vaccineDistributionLineItemListLot(@Param("facilityId") Long facilityId);




    @Select("             SELECT *, storename as fromBin, locationName as toBin,'john' as otherObject  FROM ( \n" +
            "             \n" +
            "                                                   Select distinct primaryname product,id,date,extract(year from date) as lotyear,type,transferlogs, case when fromBin is not null then locationName else frombin end as fromBin2,  \n" +
            "                                                                                  case when toBin is null then locationName else facility end as toBin2, \n" +
            "                                                                                 facility storeName, received, issued, adjustment,total,locationName, \n" +
            "                                                             \n" +
            "                                                                         (SUM(total) over(partition by locationName order by id))  as loh, \n" +
            "               CASE WHEN row_number() over (order by id asc) =1 THEN (SUM(total) over(partition by locationName order by id)) else  \n" +
            "             (SUM(total) over(order by id)) end as soh,  \n" +
            "                                                                         vvm,expirationDate,lotNumber,createdName, createdDate,movementType,reason\n" +
            "             \n" +
            "             \n" +
            "            FROM    \n" +
            "                            ( \n" +
            "             \n" +
            "             \n" +
            "             \n" +
            "\n" +
            "                                                                             \n" +
            "                                    \n" +
            "                                                                            select p.primaryname , se.id, date(se.createddate) as date,se.transferlogs,se.type, concat(u.firstname,' ',u.lastname) as createdName,TO_CHAR(se.createddate, 'DD/MM/YYYY')  createdDate,se.reason,se.movementType, \n" +
            "                                                                           case when se.type='CREDIT' then skvr.valuecolumn  else '' end as fromBin, \n" +
            "                                                                           case when se.type='DEBIT' then skvi.valuecolumn  else '' end as toBin, \n" +
            "                                                                            \n" +
            "                                                                           case when se.type='CREDIT' then skvr.valuecolumn when se.type='DEBIT' then skvi.valuecolumn end as facility,  \n" +
            "                                                                            case when se.type ='CREDIT' then se.quantity else 0 end as received,  \n" +
            "                                                                            case when se.type ='DEBIT' then se.quantity else 0 end as issued,  \n" +
            "                                                                            case when se.type ='ADJUSTMENT' then quantity else 0 end as adjustment,  \n" +
            "                                                                            loc.name locationName, \n" +
            "                                                                             \n" +
            "                                                                             case when se.type ='DEBIT' then -1 * se.quantity else se.quantity end as total,  \n" +
            "                                                 \n" +
            "                                                                              \n" +
            "                                                                            vvmst.name vvm,(select to_char(max(expirationDate), 'yyyy-MM-dd')::DATE expirationDate from lots where id=se.lotid limit 1) as expirationDate,(select lotnumber from lots where id=se.lotid limit 1) as lotNumber \n" +
            "                                                                            \n" +
            "                                                                           from lot_location_entries se  \n" +
            "                                                                            JOIN wms_locations loc ON loc.id = se.locationId \n" +
            "                                                                         left join vvm_statuses  vvmst on (vvmst.id=se.vvmId)   \n" +
            "                                                                            join stock_cards s ON s.id=se.stockcardid \n" +
            "                                                                             \n" +
            "                                                                            join products p on p.id=s.productid  \n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t join users u on u.id=se.createdby\n" +
            "                                                                             \n" +
            "                                                                            LEFT join location_stock_card_entry_key_values skvr on skvr.stockcardentryid=se.id and skvr.keycolumn='receivedfrom'  \n" +
            "                                                                                                            \n" +
            "                                                             \n" +
            "                                                                           LEFT join location_stock_card_entry_key_values skvi on skvi.stockcardentryid=se.id and skvi.keycolumn='issuedto'   \n" +
            "                                                                           WHERE   \n" +
            "                                                             \n" +
            "                                                                            \n" +
            "                                                                              loc.warehouseID =1 and vvmid<3 and  loc.typeid<>9  and p.id = 2412\n" +
            "                                                                           order by se.createddate \n" +
            "                                     \n" +
            "                                                                            \n" +
            "             \n" +
            "             \n" +
            "             \n" +
            "                                                                           ) AS ledger order by id ) X ")
    List<HashMap<String, Object>>getAllStockMovement(@Param("facilityId") Long facilityId);
    @Select("select ((coalesce((select sum(quantity) from lot_location_entries lt where  stockcardid=lhl.stockcardid and lt.type='CREDIT')+coalesce((select sum(quantity) from lot_location_entries lt where   stockcardid=lhl.stockcardid and lt.type='ADJUSTMENT'),0)-coalesce((select sum(quantity) from lot_location_entries lt \n" +
            "                                  where   stockcardid=lhl.stockcardid and lt.type='DEBIT'),0),0)))  \n" +
            "                                     totalQuantityOnHand,pr.primaryname as productName,(select to_char(modifieddate, 'dd/MM/YYYY')  from lots where productid=lo.productid limit 1) modifieddate,0.5 as mos from lot_location_entries lhl    \n" +
            "                                                   \n" +
            "                                                 join  stock_cards s on (s.id = lhl.stockcardid)    \n" +
            "                                                 join lots lo on(lo.id=lhl.lotid)    \n" +
            "                                                 join products pr on(pr.id=lo.productid)    \n" +
            "                                                 join wms_locations wl on(wl.id=lhl.locationid)    \n" +
            "                                                 join warehouses wh on(wh.id=wl.warehouseid) group by   lhl.stockcardid, lo.productid,pr.primaryname,wh.name")
    List<HashMap<String, Object>>getAllStockStatus(@Param("facilityId") Long facilityId);



    @Select("SELECT vd.id,vd.distributionid as distributionId,f.name as facilityName,p.primaryname as product,g.name as district,\n" +
            "g.code as region,vd.quantity as quantityIssued\n" +
            "FROM vaccine_distribution_line_items vd\n" +
            "left join products p on(p.id=vd.productid)\n" +
            " left join  vaccine_distributions d on(d.id=vd.distributionid)\n" +
            " left join facilities f on(f.id=d.tofacilityid)\n" +
            " left join geographic_zones g on(g.id=f.geographiczoneid) " )
    List<VaccineDistributionLineItem> vaccineAllDistributionLineItemList();



    @Select("select * from (SELECT vd.id,vd.distributionid,(select count(id) from vaccine_distribution_line_item_lots  where distributionlineitemid=vd.id) itemNo,p.code as productCode,f.name as facilityName,p.primaryname as product,g.name as district, \n" +
            "            g.code as region,(select currentprice from program_products where productid=p.id  and active=true limit 1) as unitPrice,vd.quantity as quantityIssued \n" +
            "            FROM vaccine_distribution_line_items vd \n" +
            "            left join products p on(p.id=vd.productid) \n" +
            "          \n" +
            "             left join  vaccine_distributions d on(d.id=vd.distributionid) \n" +
            "             left join facilities f on(f.id=d.tofacilityid) \n" +
            "             left join geographic_zones g on(g.id=f.geographiczoneid) where vd.distributionid=#{distID}) as ditItems where itemNo>0")
    List<VaccineDistributionLineItem> vaccineDistributionLineItemListByDistribution(@Param("distID") Long facilityId);

    @Select("SELECT v.id,\n" +
            "\tv.tofacilityid as toFacilityId ,\n" +
            "\tv.distributiondate as distributionDate,v.picklistid as pickListId,\n" +
            "\tftf.name as facilityTypeFrom,\n" +
            "\tfto.name as facilityTypeTo,\n" +
            "\tv.periodid as periodId,vo.id,vo.orderdate as orderDate,gf.name as fromZoneName,\n" +
            "\tgo.name as toZoneName,\n" +
            "\tv.orderid as orderId ,f.name as facilityName,f.description as to_description,fo.description as fromDescription,fo.name as fromFacilityName\n" +
            "FROM vaccine_distributions v\n" +
            " left join facilities f  on(f.id=v.tofacilityid)\n" +
            " left join facilities fo  on(f.id=v.fromfacilityid)\n" +
            " left join facility_types ftf on(ftf.id=f.typeid)\n" +
            " left join facility_types fto on(fto.id=f.typeid)\n" +
            " left join geographic_zones go on(go.id=fo.geographiczoneid)\n" +
            " left join geographic_zones gf on(gf.id=f.geographiczoneid)\n" +
            "left join vaccine_order_requisitions vo on(vo.id=v.orderid) limit 4")
    List<VaccineDistribution> vaccineDistributionList();


    @Select("SELECT v.id,\n" +
            "\tv.tofacilityid as toFacilityId ,\n" +
            "\tv.distributiondate as distributionDate, v.picklistid as pickListId,\n" +
            "\tftf.name as facilityTypeFrom,\n" +
            "\tfto.name as facilityTypeTo,\n" +
            "\tv.periodid as periodId,vo.orderdate as orderDate,gf.name as fromZoneName,\n" +
            "\tgo.name as toZoneName,\n" +
            "\tv.orderid as orderId,v.voucherNumber,f.name as facilityName,f.description as to_description,fo.description as fromDescription,fo.name as fromFacilityName\n" +
            "FROM vaccine_distributions v\n" +
            " left join facilities f  on(f.id=v.tofacilityid)\n" +
            " left join facilities fo  on(f.id=v.fromfacilityid)\n" +
            " left join facility_types ftf on(ftf.id=f.typeid)\n" +
            " left join facility_types fto on(fto.id=f.typeid)\n" +
            " left join geographic_zones go on(go.id=fo.geographiczoneid)\n" +
            " left join geographic_zones gf on(gf.id=f.geographiczoneid)\n" +
            "left join vaccine_order_requisitions vo on(vo.id=v.orderid) where vo.id=#{orderID}")
    List<VaccineDistribution> vaccineDistributionListByOrderId(@Param("orderID") Long orderID);

    @Select("SELECT v.id,\n" +
            "\tv.tofacilityid as toFacilityId,\n" +
            "\tv.distributiondate as distributionDate,v.picklistid as pickListId,\n" +
            "\tftf.name as facilityTypeFrom,\n" +
            "\tfto.name as facilityTypeTo,\n" +
            "\tv.periodid as periodId ,vo.id,vo.orderdate as orderDate,gf.name as fromZoneName,\n" +
            "\tgf.name as toZoneName,\n" +
            "\tv.orderid as orderId,v.voucherNumber as voucherNumber,f.name as facilityName,f.description as toDescription,fo.description as fromDescription,fo.name as fromFacilityName\n" +
            "FROM vaccine_distributions v\n" +
            " left join facilities f  on(f.id=v.tofacilityid)\n" +
            " left join facilities fo  on(f.id=v.fromfacilityid)\n" +
            " left join facility_types ftf on(ftf.id=f.typeid)\n" +
            " left join facility_types fto on(fto.id=f.typeid)\n" +
            " left join geographic_zones go on(go.id=fo.geographiczoneid)\n" +
            " left join geographic_zones gf on(gf.id=f.geographiczoneid)\n" +
            "left join vaccine_order_requisitions vo on(vo.id=v.orderid) where v.orderid=#{orderId}")
    List<VaccineDistribution> vaccineDistributionById(@Param("orderId") Long orderId);



    @Select("SELECT vd.distributionlineitemid as distributionLineItemId,\n" +
            "\tvd.id,loc.code binLocation,vd.distributionlineitemid," +
            "\tvd.lotid as lotId ,\n" +
            "\tvd.quantity,(vd.quantity/vd.packsize) vialsNumber,\n" +
            "\tvs.name as vvmStatus,\n" +
            "\tl.lotnumber as lotNumber,l.expirationdate as expirationDate\n" +
            "FROM vaccine_distribution_line_item_lots vd " +
            "left join vvm_statuses vs on(vs.id=vd.vvmid)" +
            " left JOIN wms_locations loc on (loc.id=vd.locationid) " +
            " left join lots l on(l.id=vd.lotid)\n" +
            " where vd.distributionlineitemid=#{distID}")

    List<VaccineDistributionLots> vaccineDistributionLots(@Param("distID") Long distID);



}
