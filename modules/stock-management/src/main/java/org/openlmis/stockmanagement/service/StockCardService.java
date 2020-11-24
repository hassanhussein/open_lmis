/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.stockmanagement.service;

import lombok.NoArgsConstructor;
import org.openlmis.core.repository.ProductRepository;
import org.openlmis.core.service.*;
import org.openlmis.stockmanagement.domain.*;
import org.openlmis.stockmanagement.repository.LotRepository;
import org.openlmis.stockmanagement.repository.StockCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * Exposes the services for handling stock cards.
 */

@Service
@NoArgsConstructor
public class StockCardService {

  @Autowired
  FacilityService facilityService;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  LotRepository lotRepository;

  @Autowired
  StockCardRepository repository;

  StockCardService(FacilityService facilityService,
                   ProductRepository productRepository,
                   LotRepository lotRepository,
                   StockCardRepository repository) {
    this.facilityService = Objects.requireNonNull(facilityService);
    this.productRepository = Objects.requireNonNull(productRepository);
    this.lotRepository = Objects.requireNonNull(lotRepository);
    this.repository = Objects.requireNonNull(repository);
  }

  @Transactional
  public LotOnHand getOrCreateLotOnHand(Lot lot, StockCard stockCard) {
    LotOnHand lotOnHand = lotRepository.getLotOnHandByStockCardAndLotObject(stockCard.getId(), lot);
    if (null == lotOnHand) {
      Lot l = lotRepository.getOrCreateLot(lot);
      lotOnHand = LotOnHand.createZeroedLotOnHand(l, stockCard);
      if(lot.getVvmId() != null)
        lotOnHand.setVvmId(lot.getVvmId());
      System.out.println(lot.getVvmId());
      System.out.println("----------------------------------------------------------");
      lotRepository.saveLotOnHand(lotOnHand);
    }

    Objects.requireNonNull(lotOnHand);
    return lotOnHand;
  }

  public LotOnHand getLotOnHandFor(Long lotId, Lot lotObj, String productCode, StockCard card, StringBuilder str) {


    LotOnHand lotOnHand = null;

    if (null != lotId) { // Lot specified by id
      lotOnHand = lotRepository.getLotOnHandByStockCardAndLot(card.getId(), lotId);
      if (null == lotOnHand) {
        Lot lot = lotRepository.getOrCreateLot(lotObj);
        lotOnHand = getOrCreateLotOnHand(lot, card);
      }
    }
    return lotOnHand;
  }


  public LotOnHand getLotOnHandWithVvmStatus(Long vvmId, Long lotId, Lot lotObj, String productCode, StockCard card, StringBuilder str) {
    System.out.println(vvmId);

    LotOnHand lotOnHand = null;

    if (null != lotId) { // Lot specified by id
      lotOnHand = lotRepository.getLotOnHandByStockCardAndLot(card.getId(), lotId);
      if (null == lotOnHand) {
        Lot lot = lotRepository.getOrCreateLot(lotObj);
        if(vvmId != null)
          lot.setVvmId(vvmId);
        System.out.println(lot);
        System.out.println("----------------------------------------------------------");
        lotOnHand = getOrCreateLotOnHand(lot, card);
      }
    }
    return lotOnHand;
  }


  public LotOnHand getLotOnHandByStockCardAndLot(Long stockCardId, Long lotId) {

    return lotRepository.getLotOnHandByStockCardAndLot(stockCardId, lotId);
  }




  public LocationEntry getLotOnHandWms(Long lotId, Lot lotObj, String productCode, StockCard card, StringBuilder str) {
    LocationEntry lotOnHand = null;
    if (null != lotId) { // Lot specified by id
      lotOnHand = lotRepository.getLotOnHandByStockCardAndLotWms(card.getId(), lotId);
      if (null == lotOnHand) {
        str.append("error.lot.unknown");
      }
    } else if (null != lotObj) { // Lot specified by object
      if (null == lotObj.getProduct()) {
        lotObj.setProduct(productRepository.getByCode(productCode));
      }
      if (!lotObj.isValid()) {
        str.append("error.lot.invalid");
      } else {
        //TODO:  this call might create a lot if it doesn't exist, need to implement permission check
      //  lotOnHand = getOrCreateLotOnHand(lotObj, card);
      }
    }

    return lotOnHand;
  }


  public LotOnHand getLotOnHand(Long lotId, Lot lotObj, String productCode, StockCard card, StringBuilder str) {
    LotOnHand lotOnHand = null;
    if (null != lotId) { // Lot specified by id
      lotOnHand = lotRepository.getLotOnHandByStockCardAndLot(card.getId(), lotId);
      if (null == lotOnHand) {
        str.append("error.lot.unknown");
      }
    } else if (null != lotObj) { // Lot specified by object
      if (null == lotObj.getProduct()) {
        lotObj.setProduct(productRepository.getByCode(productCode));
      }
      if (!lotObj.isValid()) {
        str.append("error.lot.invalid");
      } else {
        //TODO:  this call might create a lot if it doesn't exist, need to implement permission check
        lotOnHand = getOrCreateLotOnHand(lotObj, card);
      }
    }

    return lotOnHand;
  }

  public StockCard getOrCreateStockCard(Long facilityId, String productCode) {
    return repository.getOrCreateStockCard(facilityId, productCode);
  }

  public StockCard getStockCardById(Long facilityId, Long stockCardId) {
    return repository.getStockCardById(facilityId, stockCardId);
  }

  public List<StockCard> getStockCards(Long facilityId) {
    return repository.getStockCards(facilityId);
  }

  @Transactional
  public void addStockCardEntry(StockCardEntry entry) {

    StockCard card = getOrCreateStockCard(entry.getStockCard().getFacility().getId(), entry.getStockCard().getProduct().getCode());//entry.getStockCard();

    card.addToTotalQuantityOnHand(entry.getQuantity());
    repository.persistStockCardEntry(entry);

    LotOnHand lotOnHand = entry.getLotOnHand();
    if (null != lotOnHand) {
      lotOnHand.addToQuantityOnHand(entry.getQuantity());
      lotRepository.saveLotOnHand(lotOnHand);
    }
    repository.updateStockCard(card);

  }

  @Transactional
  public void addStockCardEntryWms(StockCardEntry entry) {

    System.out.println("Stock called!");

    StockCard card = getOrCreateStockCard(entry.getStockCard().getFacility().getId(), entry.getStockCard().getProduct().getCode());//entry.getStockCard();

    card.addToTotalQuantityOnHand(entry.getQuantity());
    repository.persistStockCardEntry(entry);

    LocationEntry lotOnHand = entry.getLots();
    if (null != lotOnHand) {
      System.out.println("Stock on hand not empty"+lotOnHand.toString());
      lotOnHand.setInputType("DEBIT");

      //lotOnHand.addToQuantityOnHand(entry.getQuantity());
      lotRepository.saveLotOnHandDistribution(lotOnHand);
    }else{
      //System.out.println("Stock on hand empty");
    }
    repository.updateStockCard(card);

  }

  @Transactional
  public void addStockCardEntries(List<StockCardEntry> entries) {
    for (StockCardEntry entry : entries) {
      addStockCardEntry(entry);
    }
  }

  @Transactional
  public void addStockCardEntriesWms(List<StockCardEntry> entries) {
    for (StockCardEntry entry : entries) {
      addStockCardEntryWms(entry);
    }
  }



  public void updateTotalStockOnHand(Long id, Long total) {
    repository.updateTotalStockOnHand(id,total);
  }

  public StockCard getStockCardByFacilityAndProduct(long facilityId, String productCode) {
    return repository.getStockCardByFacilityAndProduct(facilityId, productCode);
  }

  public Integer insertStockCard(StockCard stockCard) {
    return repository.insertStockCard(stockCard);
  }

  public void updateStockCard(StockCard stockCard) {
    repository.updateStockCard(stockCard);
  }

  public LotOnHand getLotOnHandBy(Long stockCardId, Long lotId) {
    return lotRepository.getLotOnHandBy(stockCardId,lotId);
  }

  public void updateLotOnHand(LotOnHand lotOnHand) {
    lotRepository.updateLotOnHand(lotOnHand);
  }

  public Integer insertLotOnHandBy(LotOnHand lotOnHand) {
    return lotRepository.insertLotOnHandBy(lotOnHand);
  }

}
