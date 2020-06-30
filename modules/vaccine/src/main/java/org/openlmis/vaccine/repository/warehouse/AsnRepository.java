package org.openlmis.vaccine.repository.warehouse;

import org.openlmis.core.domain.Pagination;
import org.openlmis.vaccine.domain.wms.ASNDocument;
import org.openlmis.vaccine.domain.wms.Asn;
import org.openlmis.vaccine.dto.CurrencyDTO;
import org.openlmis.vaccine.repository.mapper.warehouse.asn.AsnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class AsnRepository {

    @Autowired
    AsnMapper mapper;


    public Long insert(Asn asn) {
        return mapper.insert(asn);
    }

    public void update(Asn asn) {
        mapper.update(asn);
        mapper.deleteByAsnDetail(asn.getId());
        mapper.deleteByAsn(asn.getId());
    }

    public Asn getById (Long id){
        return mapper.getById(id);
    }

    public List<Asn> getAll(){
        return mapper.getAll();
    }

    public Integer getTotalSearchResultCountByAsnumber(String searchParam) {
        return mapper.getTotalSearchResultCountByAsnumber(searchParam);
    }

    public Integer getTotalSearchResultCountByPonumber(String searchParam) {
        return mapper.getTotalSearchResultCountByPonumber(searchParam);
    }
    public Integer getTotalSearchResultCountBySupplier(String searchParam) {
        return mapper.getTotalSearchResultCountBySupplier(searchParam);
    }
    public Integer getTotalSearchResultCountAll() {
        return mapper.getTotalSearchResultCountAll();
    }

    public List<Asn> searchBy(String searchParam, String column,  Pagination pagination){
        return mapper.search(searchParam, column, pagination);
    }

    public void uploadDocument(ASNDocument asnDocument) {
      this.mapper.uploadDocument(asnDocument);

    }

    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    public void deleteByAsnDetail(Long id) {
        this.mapper.deleteByAsnDetail(id);
    }


    public void disableAsnBy(Long id) {
        mapper.disableAsnBy(id);
    }

    public List<CurrencyDTO> getAllCurrencies(){

        return mapper.getAllCurrencies();
    }

    public Asn getByAsnNumber(String asnNumber) {

        return mapper.getByAsnNumber(asnNumber);
    }

    public List<HashMap> getAllClearingAgents() {
        return mapper.getAllClearingAgents();
    }
}
