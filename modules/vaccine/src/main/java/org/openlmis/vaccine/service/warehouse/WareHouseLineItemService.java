package org.openlmis.vaccine.service.warehouse;

import org.openlmis.rnr.domain.LineItem;
import org.openlmis.vaccine.domain.wms.WareHouse;
import org.openlmis.vaccine.domain.wms.WareHouseLineItem;
import org.openlmis.vaccine.repository.warehouse.WareHouseLineItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WareHouseLineItemService {

  @Autowired
  private WareHouseLineItemRepository repository;

  public void save(WareHouseLineItem lineItem, WareHouse savedWareHouse){
      repository.deleteByWareHouseId(savedWareHouse.getId());
      lineItem.setWareHouse(savedWareHouse);
      lineItem.setCreatedBy(savedWareHouse.getCreatedBy());
      lineItem.setModifiedBy(savedWareHouse.getModifiedBy());
      repository.insert(lineItem);
  }



}
