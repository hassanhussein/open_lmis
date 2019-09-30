package org.openlmis.stockmanagement.repository;

import lombok.NoArgsConstructor;
import org.openlmis.stockmanagement.domain.Lot;
import org.openlmis.stockmanagement.domain.LotOnHand;
import org.openlmis.stockmanagement.repository.mapper.LotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@NoArgsConstructor
public class LotRepository {

  @Autowired
  LotMapper mapper;

  LotRepository(LotMapper mapper) {
    this.mapper = Objects.requireNonNull(mapper);
  }

  public LotOnHand getLotOnHandByStockCardAndLot(Long stockCardId, Long lotId) {
    return mapper.getLotOnHandByStockCardAndLot(stockCardId, lotId);
  }

  public LotOnHand getLotOnHandByStockCardAndLotObject(Long stockCardId, Lot lot) {
    return mapper.getLotOnHandByStockCardAndLotObject(stockCardId, lot);
  }

  public Lot getOrCreateLot(Lot lot) {

    Lot lot2 = new Lot();

    if(lot != null) {

      Lot lotNumber  = mapper.getByCode(lot.getLotCode());

     if(lotNumber == null) {

       lot2 = mapper.getByObject(lot);
       if (null == lot2) {
         mapper.insert(lot);
         lot2 = lot;
       }
     }
     else {
       return lot;

     }

    }


    return lot2;
  }

  public void saveLotOnHand(LotOnHand lotOnHand) {
    if (null == lotOnHand.getId()) {
      mapper.insertLotOnHand(lotOnHand);
    } else {
      mapper.updateLotOnHand(lotOnHand);
    }
  }

  public void updateLot(Lot lot) {
    mapper.update(lot);
  }

  public void deleteLot(Long id) {
    mapper.delete(id);
  }

  public Lot getById(Long id) {
    return mapper.getById(id);
  }

  public List<Lot> getAll() {
    return mapper.getAll();
  }

    public Lot getByCode(String lotCode) {
    return mapper.getByCode(lotCode);
    }
}
