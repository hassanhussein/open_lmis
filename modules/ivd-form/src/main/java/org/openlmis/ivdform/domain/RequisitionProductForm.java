package org.openlmis.ivdform.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.serializer.DateDeserializer;

import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequisitionProductForm extends BaseModel {
    private Long productId;
    private Long orderId;
    private String dosageUnit;
    private Integer quantityRequested;
    private String productName;
    private Long maximumStock;
    private Integer reOrderLevel;
    private Integer bufferStock;
    private Long stockOnHand;
    private Long createdBy;
    private Long modifiedBy;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @JsonDeserialize(using= DateDeserializer.class)
    Date orderedDate;

}
