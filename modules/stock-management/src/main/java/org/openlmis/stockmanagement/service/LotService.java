package org.openlmis.stockmanagement.service;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.DeliveryZoneWarehouse;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Product;
import org.openlmis.core.exception.DataException;
import org.openlmis.core.service.ProductService;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.repository.LotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hassan on 8/22/17.
 */
@Service
@NoArgsConstructor
public class LotService {

    @Autowired
    private LotRepository repository;

    @Autowired
    private ProductService service;


    public void insertLot(Lot lot){
        setProduct(lot);
        if(lot.getId()==null)
        repository.getOrCreateLot(lot);
        else {
            repository.updateLot(lot);
        }
    }

    public void insertLotProduct(Lot lot){
        Product product = service.getById(lot.getProductId());
        lot.setProduct(product);
        if(lot.getId()==null)
        repository.getOrCreateLot(lot);
        else {
            repository.updateLot(lot);
        }
    }

    public void delete(Long id){
        repository.deleteLot(id);
    }

    public Lot getById(Long id){
      Lot l =  repository.getById(id);
      l.setProductId(l.getProduct().getId());
        return l;
    }

    public List<Lot> getAll(){
        return repository.getAll();
    }

    private void setProduct(Lot lot) {
        Product product = service.getByCode(lot.getProduct().getCode());
        if (product == null) throw new DataException("product.code.invalid");
        lot.setProduct(product);
    }
    public Lot getByCode(Lot record) {
        if(record.getLotCode() != null){
            return repository.getByCode(record.getLotCode());
        }
        return null;
    }
}
