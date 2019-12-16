package org.openlmis.vaccine.domain.wms.dto;

import lombok.Data;

@Data
public class WareHouseDTO{

   private Long id;

   private String name;

   private String region;

   private String siteName;

   private String productTypeId;

   private boolean active;

}
