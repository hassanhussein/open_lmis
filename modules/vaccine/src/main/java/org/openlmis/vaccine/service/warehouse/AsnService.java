package org.openlmis.vaccine.service.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.core.domain.SupplyPartner;
import org.openlmis.core.domain.User;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.SupplyPartnerService;
import org.openlmis.core.service.UserService;
import org.openlmis.rnr.service.NotificationServices;
import org.openlmis.vaccine.domain.wms.*;
import org.openlmis.core.dto.ExpectedArrivalDTO;
import org.openlmis.vaccine.dto.CurrencyDTO;
import org.openlmis.vaccine.repository.warehouse.AsnRepository;
import org.openlmis.vaccine.service.VaccineOrderRequisitionServices.VaccineNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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

    @Autowired
    private SupplyPartnerService supplyPartnerService;

    private final String FINALIZE_ASN_REPORT = "RECEIVE_FINALIZED_ASN_REPORT";

    @Autowired
    private NotificationServices notificationServices;

    @Transactional
    public void save(Asn asn, Long userId) {

        SupplyPartner supplyPartner = supplyPartnerService.getById(asn.getSupplierid());
        asn.setSupplier(supplyPartner);
        asn.setSupplierid(supplyPartner.getId());
        try {
            if (asn.getId() == null) {

                repository.insert(asn);

            } else {
                repository.update(asn);
            }
        } catch (DuplicateKeyException e) {
            throw new DataException("error.duplicate.po.number");
        }

        List<AsnLineItem> asnLineItems = asn.getAsnLineItems();

        for (AsnLineItem lineItem : asnLineItems) {

            lineItem.setAsn(asn);
            lineItem.setCreatedBy(userId);
            lineItem.setModifiedBy(userId);
            asnLineItemService.save(lineItem);
            asnLotService.deleteByAsnDetail(lineItem.getId());
            if (lineItem.isLotflag()) {
                for (AsnLot asnLot : lineItem.getAsnLots()) {
                    asnLot.setAsnLineItem(lineItem);
                    asnLot.setCreatedBy(userId);
                    asnLot.setModifiedBy(userId);
                    asnLotService.save(asnLot);
                }
            }
        }
        purchaseDocumentService.save(asn, null, userId);
 /*       List<PurchaseDocument> purchaseDocuments = asn.getPurchaseDocuments();
        for(PurchaseDocument document : purchaseDocuments){
            document.setAsn(asn);
            document.setCreatedBy(userId);
            document.setModifiedBy(userId);

        }*/


        if (asn.getStatus().equalsIgnoreCase("Finalized")) {

            //Copy ASN to rceive

            //  Receive receive = null;
            // receive.copyAsnValues(getById(asn.getId()));


            Receive res = new Receive();
            res.setId(null);
            res.setAsnId(asn.getId());
            res.setInvoiceNumber(asn.getInvoiceNumber());
            res.setPoNumber(asn.getPonumber());
            res.setPoDate(asn.getAsndate());
            res.setSupplierId(asn.getSupplierid());
            res.setCurrencyId(asn.getCurrencyId());
            // res.setReceiveDate(asn.getAsndate());
            res.setBlawBnumber(asn.getBlawbnumber());
            res.setCountry("Tanzania");
            res.setFlightVesselNumber(asn.getFlightvesselnumber());
            res.setPortOfArrival(asn.getPortofarrival());
            res.setExpectedArrivalDate(asn.getExpectedarrivaldate());
            //res.setActualArrivalDate(asn.getExpecteddeliverydate());
            res.setClearingAgent(asn.getClearingagent());
            res.setShippingAgent(null);
            res.setStatus("DRAFT");
            res.setNote(asn.getNote());
            res.setNoteToSupplier(asn.getNote());
            res.setDescription(null);
            res.setCreatedBy(asn.getCreatedBy());
            res.setModifiedBy(asn.getModifiedBy());
            res.setIsForeignProcurement(true);
            res.setSupplier(asn.getSupplier());
            res.setPurchaseOrderId(null);
            res.setReceiveLineItems(null);
            receiveService.save(res, asn.getModifiedBy(), asn, false);
            // notificationService.sendAsnFinalizeEmail();

        }
        // System.out.println("---- Output--- "+asn.getExpectedarrivaldate());

    }

    public Asn getById(Long id) {
        Asn asn = repository.getById(id);
        if (asn != null) {
            asn.setPurchaseDocuments(purchaseDocumentService.getByAsnId(id));

            return asn;
        }
        return null;
    }

    public List<Asn> getAll() {

        return repository.getAll();
    }

    public Integer getTotalSearchResultCount(String searchParam, String column) {
        if (column.equals("asnumber")) {
            return repository.getTotalSearchResultCountByAsnumber(searchParam);
        }
        if (column.equals("ponumber")) {
            return repository.getTotalSearchResultCountByPonumber(searchParam);
        }
        if (column.equals("supplier")) {
            return repository.getTotalSearchResultCountBySupplier(searchParam);
        }
        if (column.equals("all")) {
            return repository.getTotalSearchResultCountAll();
        }
        return 0;
    }

    public List<Asn> searchBy(String searchParam, String column, Pagination pagination) {

        if (column.equals("all")) {
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

    public void disableAsnBy(Long id) {
        repository.disableAsnBy(id);
    }

    public List<CurrencyDTO> getAllCurrencies() {

        return repository.getAllCurrencies();
    }

    public List<HashMap> getAllClearingAgents() {
        return repository.getAllClearingAgents();
    }

    public void updateExpectedArrivalAlert() {

        repository.updateExpectedArrivalAlert();
       // sendEmailNotification();
    }
 /*   public void saveDocument(PurchaseDocument d) {
        purchaseDocumentService.save(d);
    }*/

    public String sendEmailNotification() {

       List<User> user = userService.getUsersWithRight("WMS_RECEIVE_CONSIGNMENT");
       List<ExpectedArrivalDTO> dtoList = repository.getExpectedToReceive();
       if(!dtoList.isEmpty() && !user.isEmpty()) {
           for(ExpectedArrivalDTO dto: dtoList)
            notificationServices.sendExpectedArrivalNotification(dto, user);
       }
       return null;
    }


}
