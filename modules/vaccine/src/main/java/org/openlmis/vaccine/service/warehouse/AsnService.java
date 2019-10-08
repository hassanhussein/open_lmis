package org.openlmis.vaccine.service.warehouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.User;
import org.openlmis.core.service.UserService;
import org.openlmis.vaccine.domain.wms.*;
import org.openlmis.vaccine.repository.warehouse.AsnRepository;
import org.openlmis.vaccine.service.VaccineOrderRequisitionServices.VaccineNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class AsnService {

    @Autowired
    AsnRepository repository;

    @Autowired
    AsnLineItemService asnLineItemService;

    @Autowired
    AsnLotService asnLotService;

    @Autowired
    PurchaseDocumentService purchaseDocumentService;

    @Autowired
    UserService userService;

    @Autowired
    VaccineNotificationService notificationService;

    @Autowired
    private ReceiveService receiveService;

    private final String FINALIZE_ASN_REPORT = "RECEIVE_FINALIZED_ASN_REPORT";

    @Transactional
    public void save(Asn asn, Long userId) {

        if (asn.getId() == null) {

            repository.insert(asn);

        }else {
            repository.update(asn);
        }

        List<AsnLineItem> asnLineItems = asn.getAsnLineItems();

        for(AsnLineItem lineItem : asnLineItems){

            lineItem.setAsn(asn);
            lineItem.setCreatedBy(userId);
            lineItem.setModifiedBy(userId);
            asnLineItemService.save(lineItem);
            asnLotService.deleteByAsnDetail(lineItem.getId());
            if(lineItem.isLotflag()) {
                for (AsnLot asnLot : lineItem.getAsnLots()) {
                    asnLot.setAsnLineItem(lineItem);
                    asnLot.setCreatedBy(userId);
                    asnLot.setModifiedBy(userId);
                    asnLotService.save(asnLot);
                }
            }
        }

        List<PurchaseDocument> purchaseDocuments = asn.getPurchaseDocuments();
        for(PurchaseDocument document : purchaseDocuments){
            document.setAsn(asn);
            document.setCreatedBy(userId);
            document.setModifiedBy(userId);
            purchaseDocumentService.save(document);
        }


        if(asn.getStatus().equals("Finalized")) {

            //Copy ASN to rceive

          //  Receive receive = null;
           // receive.copyAsnValues(getById(asn.getId()));



            Receive res = new Receive();
            res.setId(null);
            res.setAsnId(asn.getId());
            res.setPoNumber(asn.getPonumber());
            res.setPoDate(asn.getAsndate());
            res.setSupplierId(asn.getSupplierid());
            res.setReceiveDate(asn.getAsndate());
            res.setBlawBnumber(asn.getBlawbnumber());
            res.setCountry("Tanzania");
            res.setFlightVesselNumber(asn.getFlightvesselnumber());
            res.setPortOfArrival(asn.getPortofarrival());
            res.setExpectedArrivalDate(asn.getExpectedarrivaldate());
            res.setActualArrivalDate(asn.getExpecteddeliverydate());
            res.setClearingAgent(asn.getClearingagent());
            res.setShippingAgent(null);
            res.setStatus("DRAFT");
            res.setNote(asn.getNote());
            res.setNoteToSupplier(asn.getNote());
            res.setDescription(null);
            res.setCreatedBy(asn.getCreatedBy());
            res.setModifiedBy(asn.getModifiedBy());
            res.setIsForeignProcurement(true);
            res.setSupplyPartner(asn.getSupplier());
            res.setPurchaseOrderId(null);
            receiveService.save(res, asn.getModifiedBy());
            notificationService.sendAsnFinalizeEmail();

        }

    }

    public Asn getById (Long id) {
        Asn asn = repository.getById(id);
        List<PurchaseDocument> purchaseDocumentList = purchaseDocumentService.getByAsnId(id);
        asn.setPurchaseDocuments(purchaseDocumentList);
        return asn;

    }
    public List<Asn> getAll(){

        return  repository.getAll();
    }

    public Integer getTotalSearchResultCount(String searchParam, String column) {
        if(column.equals("asnumber")){
            return repository.getTotalSearchResultCountByAsnumber(searchParam);
        }
        if(column.equals("ponumber")){
            return repository.getTotalSearchResultCountByPonumber(searchParam);
        }
        if(column.equals("supplier")){
            return repository.getTotalSearchResultCountBySupplier(searchParam);
        }
        if(column.equals("all")){
            return repository.getTotalSearchResultCountAll();
        }
        return 0;
    }
    public List<Asn> searchBy(String searchParam, String column, Pagination pagination) {

        if(column.equals("all")){
            return repository.getAll();
        }
        return repository.searchBy(searchParam, column, pagination);
    }

    public Object uploadDocument(ASNDocument asnDocument) {
        repository.uploadDocument(asnDocument);
        return null;
    }

    public void deleteById(Long id) {
       repository.deleteById(id);
    }

}
