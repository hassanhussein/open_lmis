/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.report.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.openlmis.core.exception.DataException;
import org.openlmis.upload.Importable;
import org.openlmis.upload.annotation.ImportField;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

/**
 * Product represents real world entity Product. It also defines the contract for creation/upload of product entity.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonSerialize(include = NON_EMPTY)
public class ProductDTO extends BaseDtoModel {


  private String code;


  private String alternateItemCode;


  private String manufacturer;


  private String manufacturerCode;


  private String manufacturerBarCode;


  private String mohBarCode;

  @ImportField(name = "GTIN")
  private String gtin;


  private String type;


  private String primaryName;


  private String fullName;

  @ImportField(name = "Generic Name")
  private String genericName;


  private String alternateName;


  private String description;


  private String strength;


  private ProductForm form;

  private Long formId;


  private ProductGroup productGroup;

  private Long productGroupId;


  private DosageUnit dosageUnit;

  private Long dosageUnitId;


  private String dispensingUnit;


  private Integer dosesPerDispensingUnit;


  private Boolean storeRefrigerated;


  private Boolean storeRoomTemperature;


  private Boolean hazardous;


  private Boolean flammable;


  private Boolean controlledSubstance;


  private Boolean lightSensitive;


  private Boolean approvedByWHO;


  private Double contraceptiveCYP;


  private Integer mslPackSize;

  private Integer packSize;


  private Integer alternatePackSize;


  private Double packLength;


  private Double packWidth;


  private Double packHeight;


  private Double packWeight;


  private Integer packsPerCarton;


  private Double cartonLength;


  private Double cartonWidth;


  private Double cartonHeight;


  private Integer cartonsPerPallet;


  private Integer expectedShelfLife;


  private String specialStorageInstructions;


  private String specialTransportInstructions;


  private Boolean active;


  private Boolean fullSupply;


  private Boolean tracer;


  private Integer packRoundingThreshold;


  private Boolean roundToZero;


  private Boolean archived;



}
