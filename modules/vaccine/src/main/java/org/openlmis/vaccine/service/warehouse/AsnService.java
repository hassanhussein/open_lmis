package org.openlmis.vaccine.service.warehouse;

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

    private final String FINALIZE_ASN_REPORT = "RECEIVE_FINALIZED_ASN_REPORT";

    @Transactional
    public void save(Asn asn, Long userId) {

        if (asn.getId() == null) {

            repository.insert(asn);

        }else {
            repository.update(asn);
        }
        notificationService.sendAsnFinalizeEmail();
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

    }

    public Asn getById (Long id) {

        return repository.getById(id);

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
