package org.openlmis.lookupapi.mapper;

import org.apache.ibatis.annotations.*;
import org.openlmis.lookupapi.model.FacilityMsdCodeDTO;
import org.openlmis.lookupapi.model.HealthFacilityDTO;
import org.openlmis.lookupapi.model.MSDStockDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public interface ILInterfaceMapper {

    @Insert("INSERT INTO public.hfr_facilities(\n" +
            "             commfacname, council, createdat, district, facidnumber, facilitytype, \n" +
            "            facilitytypegroup, latitude, longitude, name, oschangeclosedtooperational, \n" +
            "            oschangeopenedtoclose, operatingstatus, ownership, ownershipgroup, \n" +
            "            postorupdate, region, registrationstatus, updatedat, villagemtaa, \n" +
            "            ward, zone, districtCode, councilCode,facilityTypeGroupCode,ownershipCode)\n" +
            "    VALUES ( #{commFacName}, #{council}, #{createdAt}, #{district}, #{facIDNumber}, #{facilityType}, \n" +
            "            #{facilityTypeGroup},CAST(#{latitude} as double precision), CAST(#{longitude} as double precision), #{name}, #{oSchangeClosedtoOperational}, \n" +
            "            #{oSchangeOpenedtoClose}, #{operatingStatus}, #{ownership}, #{ownershipGroup}, \n" +
            "            #{postorUpdate}, #{region}, #{registrationStatus}, #{updatedAt}, #{villageMtaa}, \n" +
            "            #{ward}, #{zone}, #{districtCode}, #{councilCode}, #{facilityTypeGroupCode}, #{ownershipCode}); ")
    @Options(useGeneratedKeys = true)
    Integer insert(HealthFacilityDTO dto);

    @Update("UPDATE public.hfr_facilities\n" +
            "   SET  commfacname=#{commFacName}, council=#{council}, createdat=#{createdAt}, district=#{district}, \n" +
            "       facilitytype=#{facilityType}, facilitytypegroup= #{facilityTypeGroup}, latitude=CAST(#{latitude} as double precision), longitude=CAST(#{longitude} AS double precision), \n" +
            "       name=#{name}, oschangeclosedtooperational=#{oSchangeClosedtoOperational}, oschangeopenedtoclose=#{oSchangeOpenedtoClose}, \n" +
            "       operatingstatus=#{operatingStatus}, ownership=#{ownership}, ownershipgroup=#{ownershipGroup}, postorupdate=#{postorUpdate}, \n" +
            "       region=#{region}, registrationstatus= #{registrationStatus}, updatedat=#{updatedAt}, villagemtaa=#{villageMtaa}, ward=#{ward}, \n" +
            "       zone=#{zone}, districtCode = #{districtCode},councilCode = #{councilCode}, facilityTypeGroupCode=#{facilityTypeGroupCode}, ownershipCode=#{ownershipCode}  \n" +
            " WHERE facidNumber=#{facIDNumber} ;")
    void update(HealthFacilityDTO dto);

    @Select("select * from hfr_facilities WHERE IlIDNumber = #{IlIDNumber} limit 1")
    HealthFacilityDTO getByTransactionId(@Param("IlIDNumber") String IlIDNumber);

    @Select("select * from hfr_facilities where id = #{id} limit 1  ")
    HealthFacilityDTO getById(@Param("id") Integer id);

    @Select("select * from hfr_facilities where facIDNumber = #{facIDNumber} limit 1")
    HealthFacilityDTO getByFacilityCode(@Param("facIDNumber") String facIDNumber);

    @Select("select * from hfr_facilities")
    List<HashMap<String,Object>>getAllHFRFacilities();


    @Insert(" INSERT INTO public.msd_stock_statuses(\n" +
            "             ilId, facilityId, productId, onHandDate, onHandQuantity, \n" +
            "            mos, createdDate, createdBy)\n" +
            "    VALUES ( #{ilID}, #{facilityId}, #{productId}, #{onHandDate}, #{onHandQuantity}, \n" +
            "            #{mos}, #{createdDate}, #{createdBy}) ")
    @Options(useGeneratedKeys = true)
    Integer insertMsdStock(MSDStockDTO dto);

    @Select("select * from msd_stock_statuses where ilId=#{ilId} ")
    MSDStockDTO getByMSDILId(String ilId);


    @Select("SELECT * FROM refresh_view_via_api(#{view}) ")
    void refreshViewsBy(@Param("view") String view);


    @Select("SELECT * FROM hfr_facilities WHERE facIdNumber = #{facIdNumber}")
    List<FacilityMsdCodeDTO> getByHfrCode(@Param("facIdNumber") String facIdNumber);


    @Update("update hfr_facilities SET activatedByMsd = true, msdCode = #{msdCode}, activatedDate = #{activatedDate} WHERE facIdNumber = #{facIdNumber}\n")
    void activateByMSDFacilityCode(FacilityMsdCodeDTO msd);

    @Select("select pr.code as \"programCode\", o.ordernumber as \"orderId\", p.code as \"productCode\", F.CODE as \"orderFromFacilityId\",  \n" +
            "case when (r.emergency = false) then 'Emergency' else 'Regular' end as \"orderType\" , \n" +
            "QuantityApproved as \"orderedQuantity\", quantityReceived as \"deliveredQuantity\", to_char(r.createdDate, 'yyyy-MM-dd') as \"orderDate\",\n" +
            "\n" +
            "to_char(r.modifieddate, 'yyyy-MM-dd') as \"deliveryPromiseDate\",\n" +
            "to_char(r.modifieddate, 'yyyy-MM-dd') as \"deliveryDate\",\n" +
            " 189790-1 as \"deliveryFromFacilityId\",\n" +
            "r.status as \"orderStatus\",\n" +
            "30 as targetDays\n" +
            "from requisitions r\n" +
            "\n" +
            "JOIN requisition_line_items i On r.id = i.rnrid\n" +
            "JOIN orders o ON r.id = o.id\n" +
            "JOIN products p ON i.productcode = p.code\n" +
            "JOIN program_products pp On pp.productid = p.id" +
            " JOIN programs pr ON r.programId = pr.id" +
            " JOIN facilities f ON r.facilityId = F.ID" +
            " WHERE f.code NOT IN('.') AND f.code IS NOT NULL " +
            "limit 100")
    List<HashMap<String, Object>> getOrderDelivery();

    @Select(" select pr.code as \"programCode\", p.code as \"productCode\", f.code as \"facility_id\",  to_char(r.modifieddate, 'yyyy-MM-dd') as \"period\",\n" +
            "stockinhand as \"availableQuantity\", quantityReceived as \"stockQuantity\", case when amc > 0 then stockinhand/ amc else 0 end as \"stockOfMonth\"\n" +
            "from requisitions r\n" +
            "JOIN requisition_line_items i On r.id = i.rnrid\n" +
            "JOIN orders o ON r.id = o.id\n" +
            "JOIN products p ON i.productcode = p.code\n" +
            "JOIN program_products pp On pp.productid = p.id" +
            " JOIN programs pr ON r.programId = pr.id" +
            " JOIN facilities f ON r.facilityId = F.ID" +
            " where f.code is not null \n" +
            "limit 100")
    List<HashMap<String, Object>> getEmergencyCommodites();
}
