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
    @Select("SELECT vd.id,vd.distributionid,f.name as facilityName,p.fullname as product,g.name as district,\n" +
            "g.code as region,vd.quantity as quantityIssued\n" +
            "FROM vaccine_distribution_line_items vd\n" +
            "left join products p on(p.id=vd.productid)\n" +
            " left join  vaccine_distributions d on(d.id=vd.distributionid)\n" +
            " left join facilities f on(f.id=d.tofacilityid)\n" +
            " left join geographic_zones g on(g.id=f.geographiczoneid)")
    List<VaccineDistributionLineItem> vaccineDistributionLineItemList();

    @Select("SELECT vd.id,vd.distributionid,f.name as facilityName,p.fullname as product,g.name as district,\n" +
            "g.code as region,vd.quantity as quantityIssued\n" +
            "FROM vaccine_distribution_line_items vd\n" +
            "left join products p on(p.id=vd.productid)\n" +
            " left join  vaccine_distributions d on(d.id=vd.distributionid)\n" +
            " left join facilities f on(f.id=d.tofacilityid)\n" +
            " left join geographic_zones g on(g.id=f.geographiczoneid) where vd.distributionid=#{distID} limit 4")
    List<VaccineDistributionLineItem> vaccineDistributionLineItemListByDistribution(@Param("distID") Long facilityId);

    @Select("SELECT v.id,\n" +
            "\tv.tofacilityid,\n" +
            "\tv.distributiondate,\n" +
            "\tftf.name as facility_type_from,\n" +
            "\tfto.name as facility_type_to,\n" +
            "\tv.periodid,vo.id,vo.orderdate,gf.name as from_zone_name,\n" +
            "\tgo.name as to_zone_name,\n" +
            "\tv.orderid,f.name as facilityName,f.description as to_description,fo.description as from_description,fo.name as from_facility_name\n" +
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
            "\tv.tofacilityid,\n" +
            "\tv.distributiondate,\n" +
            "\tftf.name as facility_type_from,\n" +
            "\tfto.name as facility_type_to,\n" +
            "\tv.periodid,vo.id,vo.orderdate,gf.name as from_zone_name,\n" +
            "\tgo.name as to_zone_name,\n" +
            "\tv.orderid,f.name as facilityName,f.description as to_description,fo.description as from_description,fo.name as from_facility_name\n" +
            "FROM vaccine_distributions v\n" +
            " left join facilities f  on(f.id=v.tofacilityid)\n" +
            " left join facilities fo  on(f.id=v.fromfacilityid)\n" +
            " left join facility_types ftf on(ftf.id=f.typeid)\n" +
            " left join facility_types fto on(fto.id=f.typeid)\n" +
            " left join geographic_zones go on(go.id=fo.geographiczoneid)\n" +
            " left join geographic_zones gf on(gf.id=f.geographiczoneid)\n" +
            "left join vaccine_order_requisitions vo on(vo.id=v.orderid) where v.id=#{distID}")
    List<VaccineDistribution> vaccineDistributionById(@Param("distID") Long distID);



    @Select("SELECT vd.distributionlineitemid,\n" +
            "\tvd.id,vd.distributionlineitemid," +
            "\tvd.lotid,\n" +
            "\tvd.quantity,\n" +
            "\tvd.vvmstatus,\n" +
            "\tl.lotnumber,l.expirationdate\n" +
            "FROM vaccine_distribution_line_item_lots vd left join lots l on(l.id=vd.lotid)\n" +
            " order by RANDOM() limit 2")
    //where vd.distributionlineitemid=#{distID}
    List<VaccineDistributionLots> vaccineDistributionLots(@Param("distID") Long distID);



}
