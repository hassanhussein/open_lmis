package org.openlmis.stockmanagement.repository;

import lombok.NoArgsConstructor;
import org.openlmis.stockmanagement.domain.LocationEntry;
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

  public LocationEntry getLotOnHandByStockCardAndLotWms(Long stockCardId, Long lotId) {
    return mapper.getLotOnHandByStockCardAndLotWms(stockCardId, lotId);
  }



  public LotOnHand getLotOnHandByStockCardAndLotObject(Long stockCardId, Lot lot) {
    return mapper.getLotOnHandByStockCardAndLotObject(stockCardId, lot);
  }

  public Lot getOrCreateLot(Lot lot) {
    Lot l = mapper.getByObject(lot);
    if (null == l) {
      Lot lotNumber  = mapper.getByCode(lot.getLotCode());
      if(lotNumber == null) {
        mapper.insert(lot);
        l = lot;
      } else {
        mapper.update(lot);
        l = lot;
      }
    }

    return l;
  }

  public void saveLotOnHandDistribution(LocationEntry lotOnHand){
    //Lot On hand insertLotOnHand function need to be reference insert lot on hand entry function for WMS
    mapper.insertLotOnHandWms(lotOnHand);
  }

  //Please add separate lot on hand for WMS..I have commented the WMS function

  /*public void insertLotOnHandWms(LocationEntry lotOnHand) {
    if (null == lotOnHand.getId()) {
      mapper.insertLotOnHandWms(lotOnHand);
    } else {
      mapper.updateLotOnHandWms(lotOnHand);
    }
  }*/

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

    public LotOnHand getLotOnHandBy(Long stockCardId, Long lotId) {
     return mapper.getLotOnHandBy(stockCardId,lotId);
    }

   //Updtated to lotOnHand from LotOnHandEntry
  public void updateLotOnHand(LotOnHand lotOnHand) {
    mapper.updateLotOnHand(lotOnHand);
  }

  public Integer insertLotOnHandBy(LotOnHand lotOnHand) {
    return mapper.insertLotOnHandBy(lotOnHand);
  }
}
