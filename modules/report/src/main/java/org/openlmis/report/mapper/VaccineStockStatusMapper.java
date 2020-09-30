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
            "g.code as region,vd.quantity as quantityIssued\n" +
            "FROM vaccine_distribution_line_items vd\n" +
            "left join products p on(p.id=vd.productid)\n" +
            " left join  vaccine_distributions d on(d.id=vd.distributionid)\n" +
            " left join facilities f on(f.id=d.tofacilityid)\n" +
            " left join geographic_zones g on(g.id=f.geographiczoneid) where f.id=#{facilityId}")
    List<VaccineDistributionLineItem> vaccineDistributionLineItemList(@Param("facilityId") Long facilityId);

    @Select("select * from (SELECT vd.id,vd.distributionid,(select count(id) from vaccine_distribution_line_item_lots  where distributionlineitemid=vd.id) itemNo,p.code as productCode,f.name as facilityName,p.primaryname as product,g.name as district,\n" +
            "g.code as region,vd.quantity as quantityIssued\n" +
            "FROM vaccine_distribution_line_items vd\n" +
            "left join products p on(p.id=vd.productid)\n" +
            ""+
            " left join  vaccine_distributions d on(d.id=vd.distributionid)\n" +
            " left join facilities f on(f.id=d.tofacilityid)\n" +
            " left join geographic_zones g on(g.id=f.geographiczoneid) where vd.distributionid=#{distID}) as ditItems where itemNo>0")
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
            "\tv.orderid as orderId ,f.name as facilityName,f.description as to_description,fo.description as fromDescription,fo.name as fromFacilityName\n" +
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
            "\tv.orderid as orderId,f.name as facilityName,f.description as toDescription,fo.description as fromDescription,fo.name as fromFacilityName\n" +
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
            "\tvd.quantity,\n" +
            "\tvs.name as vvmStatus,\n" +
            "\tl.lotnumber as lotNumber,l.expirationdate as expirationDate\n" +
            "FROM vaccine_distribution_line_item_lots vd " +
            "left join vvm_statuses vs on(vs.id=vd.vvmid)" +
            " left JOIN wms_locations loc on (loc.id=vd.locationid) " +
            " left join lots l on(l.id=vd.lotid)\n" +
            " where vd.distributionlineitemid=#{distID}")

    List<VaccineDistributionLots> vaccineDistributionLots(@Param("distID") Long distID);



}
