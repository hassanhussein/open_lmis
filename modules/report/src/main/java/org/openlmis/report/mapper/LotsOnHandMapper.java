package org.openlmis.report.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.openlmis.report.model.CustomReport;
import org.openlmis.report.model.dto.Facility;
import org.openlmis.report.model.wmsreport.Facilities;
import org.openlmis.report.model.wmsreport.StockCard;
import org.openlmis.report.model.wmsreport.StockCards;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
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
    @Select("select  \n" +
            "            r.country as hdr_country, \n" +
            "            RECEIPTNUMBER as hdr_reportnumber, \n" +
            "            to_char(now(), 'DD/MM/YYYY') as hdr_dateofreport, \n" +
            "            'AAABBCC'||', '||to_char(r.receivedate, 'DD/MM/YYYY') as hdr_nameofcoldstore, \n" +
            "            'CVS'||', '|| to_char(inspectionDate, 'DD/MM/YYYY HH24:MI') as hdr_completiontime, \n" +
            "            i.id as ins_id, \n" +
            "            i.receiveid as ins_receiveid, \n" +
            "             to_char(i.inspectiondate , 'DD/MM/YYYY HH24:MI') as ins_inspectiondate, \n" +
            "            i.inspectionnote as ins_inspectionnote, \n" +
            "            i.inspectedby as ins_inspectedby, \n" +
            "            i.status as ins_status, \n" +
            "            i.createby as ins_createby, \n" +
            "            to_char(i.createddate,'DD/MM/YYYY') as ins_createddate, \n" +
            "            i.modifiedby as ins_modifiedby, \n" +
            "            to_char(i.modifieddate,'DD/MM/YYYY') as ins_modifieddate, \n" +
            "            i.receiptnumber as ins_receiptnumber, \n" +
            "            i.descriptionofinspection as ins_descriptionofinspection, \n" +
            "            coalesce(i.isshippedprovided,false) as ins_isshippedprovided, \n" +
            "            i.isshipped as ins_isshipped, \n" +
            "            i.shippedcomment as ins_shippedcomment, \n" +
            "            i.shippedprovidedcomment as ins_shippedprovidedcomment, \n" +
            "            coalesce(i.conditionofbox,'') as ins_conditionofbox, \n" +
            "            i.labelattachedcomment as ins_labelattachedcomment, \n" +
            "            ii.id as insi_id, \n" +
            "            ii.inspectionid as insi_inspectionid, \n" +
            "            ii.productid as insi_productid, \n" +
            "            ii.quantitycounted as insi_quantitycounted, \n" +
            "            lot.boxnumber as insi_boxcounted, \n" +
            "            lot.passquantity as insi_passquantity, \n" +
            "            ii.passlocationid as insi_passlocationid, \n" +
            "            ii.failquantity as insi_failquantity, \n" +
            "            ii.failreason as insi_failreason, \n" +
            "            ii.faillocationid as insi_faillocationid, \n" +
            "            ii.lotflag as insi_lotflag, \n" +
            "            ii.dryiceflag as insi_dryiceflag, \n" +
            "            ii.vvmflag as insi_vvmflag, \n" +
            "            concat(lot.lotNumber,' (',v.name,')') as ins_lot_vvm_status," +
            "            ii.cccardflag as insi_cccardflag, \n" +
            "            ii.electronicdeviceflag as insi_electronicdeviceflag, \n" +
            "            ii.createdby as insi_createdby, \n" +
            "            to_char(ii.createddate,'DD/MM/YYYY HH24:MI')  as insi_createddate, \n" +
            "            ii.modifiedby as insi_modifiedby, \n" +
            "            to_char(ii.modifieddate,'DD/MM/YYYY') as insi_modifieddate, \n" +
            "            ii.othermonitor as insi_othermonitor, \n" +
            "            ii.icepackflag as insi_icepackflag, \n" +
            "            ii.nocoolantflag as insi_nocoolantflag, \n" +
            "            r.id as rec_id, \n" +
            "            r.purchaseorderid as rec_purchaseorderid, \n" +
            "            r.ponumber as rec_ponumber, \n" +
            "            r.podate as rec_podate, \n" +
            "            r.supplierid as rec_supplierid, \n" +
            "            r.asnid as rec_asnid, \n" +
            "            to_char(r.receivedate, 'DD/MM/YYYY') as rec_receivedate, \n" +
            "            r.blawbnumber as rec_blawbnumber, \n" +
            "            r.country as rec_country, \n" +
            "            r.flightvesselnumber as rec_flightvesselnumber, \n" +
            "            p.name as rec_portofarrival, \n" +
            "            to_char(r.expectedarrivaldate, 'DD/MM/YYYY HH24:MI') as rec_expectedarrivaldate, \n" +
            "            to_char(r.actualarrivaldate, 'DD/MM/YYYY HH24:MI') as rec_actualarrivaldate, \n" +
            "            r.clearingagent as rec_clearingagent, \n" +
            "            r.shippingagent as rec_shippingagent, \n" +
            "            r.status as rec_status, \n" +
            "            r.note as rec_note, \n" +
            "            r.notetosupplier as rec_notetosupplier, \n" +
            "            r.description as rec_description, \n" +
            "            r.createdby as rec_createdby, \n" +
            "            to_char(r.createddate,'DD/MM/YYYY') as rec_createddate, \n" +
            "            r.modifiedby as rec_modifiedby, \n" +
            "            to_char(r.modifieddate,'DD/MM/YYYY') as rec_modifieddate, \n" +
            "            r.isforeignprocurement as rec_isforeignprocurement, \n" +
            "            r.currencyid as rec_currencyid, \n" +
            "            ri.id as reci_id, \n" +
            "            ri.receiveid as reci_receiveid, \n" +
            "            ri.productid as reci_productid, \n" +
            "            to_char(ri.expirydate, 'MM/YYYY') as reci_expirydate, \n" +
            "            ri.manufacturingdate as reci_manufacturingdate, \n" +
            "            ri.quantitycounted as reci_quantitycounted, \n" +
            "            ri.unitprice as reci_unitprice, \n" +
            "            ri.boxcounted as reci_boxcounted, \n" +
            "            ri.lotflag as reci_lotflag, \n" +
            "            ri.createdby as reci_createdby, \n" +
            "            to_char(ri.createddate,'DD/MM/YYYY')  as reci_createddate, \n" +
            "            ri.modifiedby as reci_modifiedby, \n" +
            "            to_char(ri.modifieddate,'DD/MM/YYYY')  as reci_modifieddate, \n" +
            "            a.id as asn_id, \n" +
            "            a.purchaseorderid as asn_purchaseorderid, \n" +
            "            a.ponumber as asn_ponumber, \n" +
            "            a.podate as asn_podate, \n" +
            "            a.supplierid as asn_supplierid, \n" +
            "            a.asnnumber as asn_asnnumber, \n" +
            "            to_char(a.asndate, 'DD/MM/YYYY') as asn_asndate, \n" +
            "            a.blawbnumber as asn_blawbnumber, \n" +
            "            a.flightvesselnumber as asn_flightvesselnumber, \n" +
            "            a.portofarrival as asn_portofarrival, \n" +
            "            to_char(a.expectedarrivaldate, 'DD/MM/YYYY')  as asn_expectedarrivaldate, \n" +
            "            a.clearingagent as asn_clearingagent, \n" +
            "            a.status as asn_status, \n" +
            "            a.note as asn_note, \n" +
            "            a.createdby as asn_createdby, \n" +
            "            a.createddate as asn_createddate, \n" +
            "            a.modifiedby as asn_modifiedby, \n" +
            "            a.modifieddate as asn_modifieddate, \n" +
            "            a.expecteddeliverydate as asn_expecteddeliverydate, \n" +
            "            a.active as asn_active, \n" +
            "            a.currencyid as asn_currencyid, \n" +
            "            ai.id as asni_id, \n" +
            "            ai.asnid as asni_asnid, \n" +
            "            ai.productid as asni_productid, \n" +
            "            to_char(ai.expirydate, 'MM/YYYY') as asni_expirydate, \n" +
            "            ai.manufacturingdate as asni_manufacturingdate, \n" +
            "            ai.quantityexpected as asni_quantityexpected, \n" +
            "            ai.lotflag as asni_lotflag, \n" +
            "            ai.createdby as asni_createdby, \n" +
            "            ai.createddate as asni_createddate, \n" +
            "            ai.modifiedby as asni_modifiedby, \n" +
            "            ai.modifieddate as asni_modifieddate, \n" +
            "            ai.unitprice as asni_unitprice, \n" +
            "            COALESCE ((select case when (documentType = 1) then 'Yes' else 'No' end as rec_airBill from documents doc \n" +
            "            JOIN document_types T on doc.documentType = t.id \n" +
            "             where doc.asnNumber = a.asnNumber and documentType=1 and deleted=false limit 1),'No') as rec_airBill, \n" +
            "             \n" +
            "            COALESCE ((select case when (documentType = 2) then 'Yes' else 'No' end as rec_packList from documents doc \n" +
            "            JOIN document_types T on doc.documentType = t.id \n" +
            "             where doc.asnNumber = a.asnNumber and documentType=2 and deleted=false limit 1),'No') as rec_packList, \n" +
            "             \n" +
            "             COALESCE ((select case when (documentType = 3) then 'Yes' else 'No' end as rec_invoice from documents doc \n" +
            "            JOIN document_types T on doc.documentType = t.id \n" +
            "             where doc.asnNumber = a.asnNumber and documentType=3 and deleted=false limit 1),'No') as rec_invoice, \n" +
            "             \n" +
            "              COALESCE ((select case when (documentType = 4) then 'Yes' else 'No' end as rec_releaseCertificate \n" +
            "               from documents doc \n" +
            "            JOIN document_types T on doc.documentType = t.id \n" +
            "             where doc.asnNumber = a.asnNumber and documentType=4 and deleted=false limit 1),'No') as rec_releaseCertificate, \n" +
            "              \n" +
            "\t\t\t   COALESCE ((select case when (documentType = 10) then 'Yes' else 'No' end  from documents doc \n" +
            "            JOIN document_types T on doc.documentType = t.id \n" +
            "             where doc.asnNumber = a.asnNumber and documentType=10 and deleted=false limit 1),'No') as var_report, \n" +
            "             \n" +
            "\t\t\t \n" +
            "             \n" +
            "                pp.primaryname product, \n" +
            "               lot.lotNumber ins_lotNumber, \n" +
            "            to_char(LOT.EXPIRYDATE, 'MM/YYYY') as ins_expirydate, \n" +
            "              \n" +
            "            pp.primaryname ||' ('|| PACKSIZE ||' '|| u.code ||' )' productDescription, \n" +
            "            pp.manufacturer, \n" +
            "             \n" +
            "            case when i.isshipped=true then 'Yes' else 'No' end as isshipped, \n" +
            "            case when ii.icepackflag=true then ', Icepacks' else '' end as icepackflag, \n" +
            "            case when ii.cccardflag=true then ', Cold-chain Card' else '' end as cccardflag, \n" +
            "            case when ii.electronicdeviceflag=true then ', Electronic devices' else '' end as electronicdevices, \n" +
            "            case when ii.dryiceflag=true then 'Dry Ice' else '' end as dryice, \n" +
            "            case when ii.vvmflag=true then 'VVM' else '' end as vvmflag, \n" +
            "            case when isshippedprovided= true then 'Yes' else 'No' end as isshippedprovided, \n" +
            "            shippedcomment, shippedprovidedcomment, \n" +
            "            i.varNumber  \n" +
            "             \n" +
            "             \n" +
            "            from inspections i \n" +
            "            JOIN inspection_line_items ii on i.id = ii.inspectionid \n" +
            "            JOIN Receives r on i.receiveId = r.id \n" +
            "            JOIN receive_line_items ri ON ri.receiveid = r.id \n" +
            "            JOIN asns a ON r.asnId = a.id \n " +

            "            JOIN asn_details ai ON a.id = ai.asnId \n" +
            "            JOIN products pp ON ii.productId = pp.id \n" +
            "            JOIN inspection_lots LOT ON ii.id = LOT.inspectionLINEITEMID \n" +
            " JOIN vvm_statuses v on v.id=LOT.vvmstatus " +
            "            JOIN dosage_units u on pp.dosageunitid = u.id \n" +
            "            JOIN ports P ON r.portofarrival = p.id \n" +
            "            WHERE I.ID  = #{inspectionId}")
    List<HashMap<String, Object>>getListVarReport(@Param("inspectionId") Long inspectionId);

    @Select("SELECT distinct l.lotNumber,to_char(receiveDate, 'dd/MM/YYYY') as receiveDate, i.receiveNumber, lo.code binLocation, S.name supplyName,\n" +
            "             r.invoiceNumber, r.poNumber, p.code productCode, p.primaryName product, u.code dosageUnit,\n" +
            " \n" +
            "            asnL.quantity  quantityOrdered, l.lotNumber, to_char(L.manufacturingDate,'dd/MM/YYYY') manufacturingDate, to_char(L.expiryDate, 'dd/MM/YYYY') expiryDate, \n" +
            "            L.quantity quantityReceived,  (date_part('month',age(L.expiryDate::date,receiveDate::date)) + date_part('year',age(L.expiryDate::date,receiveDate::date))*12)  as shelfLife,r.note \n" +
            "            \n" +
            "            FROM RECEIVES R \n" +
            "            JOIN receive_line_items i ON r.id = i.receiveid \n" +
            "            JOIN Receive_lots L ON i.id = L.receivelineItemId \n" +
            "            JOIN products p ON i.productId = P.ID \n" +
            "            LEFT JOIN wms_locations Lo ON L.locationId = Lo.id \n" +
            "            JOIN SUpply_partners S on r.supplierid = s.id \n" +
            "            LEFT JOIN dosage_units U on p.wmsDosageunitId = U.ID \n" +
            "            JOIN ASNs ON R.asnId = ASNS.ID \n" +
            "            JOIN ASN_details it On asns.id = it.asnId \n" +
            "            Join asn_lots asnL ON it.ID = asnL.asndetailId \n" +
            "             \n" +
            "            WHERE r.ID = #{receiveId}")
    List<HashMap<String, Object>>getListGrnReport(@Param("receiveId") Long inspectionId);




    @Select(" select distinct wl.name from inspection_lots lo " +
            " left join wms_locations wl  on (wl.id=lo.passlocationid)" +
            " left join inspection_line_items li on(li.id=lo.inspectionlineitemid) where inspectionid=#{inspectionId}")
    ArrayList<String> getListVarStorage(@Param("inspectionId") Long inspectionId);




    @Select("select vvm.name as vvm,lo.lotnumber as lotNumber,lo.expirationdate as expirationDate,  \n" +
            "                         ((coalesce((select sum(quantity) from lot_location_entries lt where lt.vvmid = vvm.id and lt.locationid= e.locationId and lotid=e.lotid and lt.type='CREDIT')+coalesce((select sum(quantity) from lot_location_entries lt where lt.vvmid = vvm.id and  lt.locationid= e.locationId and lotid=e.lotid and lt.type='ADJUSTMENT'),0)-coalesce((select sum(quantity) from lot_location_entries lt  \n" +
            "                       where lt.vvmid = vvm.id and  lt.locationid= e.locationId and lotid=e.lotid and lt.type='DEBIT'),0),0)))  \n" +
            "                         totalQuantityOnHand,\n" +
            "\t\t\t\t\t   s.effectivedate as effectiveDate,s.modifieddate as modifiedDate ,pr.primaryname as fullName from lot_location_entries e  \n" +
            "                        \n" +
            "                        left join  stock_cards s on (s.id = e.stockcardid)   \n" +
            "                        left join lots lo on(lo.id=e.lotid)  \n" +
            "                        left join vvm_statuses vvm on(vvm.id=e.vvmId)  \n" +
            "                        left join products pr on(pr.id=lo.productid)   \n" +
            "                        left join wms_locations wl on(wl.id=e.locationid)   \n" +
            "                        left join warehouses wh on(wh.id=wl.warehouseid)   \n" +
            "                        where lo.productid=#{productId} and wl.warehouseid=#{warehouseId} and wl.typeid<>9 and e.quantity > 0 \n" +
            "\t\t\t\t\t\t group by vvm.name,vvm.id, e.lotId,pr.id,lotNumber, e.locationId,lo.expirationdate,s.effectivedate,s.modifieddate ")
    List<StockCards> getListStockOnHand(@Param("productId") Long productId,@Param("warehouseId") Long warehouseId);



    @Select("select  sum(lhl.quantity) as totalQuantityOnHand,lo.productid as productId,pr.primaryname as productName,wh.name as wareHouseName from lot_location_entries lhl  \n" +
            "                         \n" +
            "                        left join  stock_cards s on (s.id = lhl.stockcardid)  \n" +
            "                        left join lots lo on(lo.id=lhl.lotid)  \n" +
            "                        left join products pr on(pr.id=lo.productid)  \n" +
            "                        left join wms_locations wl on(wl.id=lhl.locationid)  \n" +
            "                        left join warehouses wh on(wh.id=wl.warehouseid)  where wl.warehouseid=#{wareHouseId} group by   lo.productid,pr.primaryname,wh.name")
    List<StockCard> getListStockProduct(@Param("wareHouseId") Long wareHouseId);






}
