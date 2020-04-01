package org.openlmis.rnr.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductCategory;

import static com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion.NON_EMPTY;

@Data
@NoArgsConstructor
@JsonSerialize(include = NON_EMPTY)
@EqualsAndHashCode(callSuper = false)
public class MonitoringReportLineItem extends BaseModel {

  private Long reportId;

  private Long productId;

  private String product;

  private Long productCategoryId;

  private Integer stockOnHand;

  private Integer quantityRequested;

  private String productCode;

  private String productCategory;

  private Integer packSize;

  private String dispensingUnit;

  private Product products;

  private ProductCategory productCategories;

}
